package polling.station;
import initialize.Controller;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayDeque;

import ballot.mangement.ballot;

public class pollingstation {
	 private Controller ctlr;
	 private String infoFile = "ridingInfoFile.sqlite";
	 String riding_name = null;
	 private String location = null;
	 private int num_voters = -1;
	 private int num_seats = -1;
	 private int num_can = -1;
	 private int num_poll = -1;
	 private String[] can_party = null;
	 private int[] polls = null;
	 private Connection con;
	 private Statement stat;
	 

	 /**
	  * Constructor: creates polling station and polulates with infoFile data
	  * infoFile should have 
	  * 	TABLE basics(riding_name varchar(25), num_voters int, num_seats 
	  * 	int, num_candidates int, central bit) with only one entry.
	  * 	TABLE candidates(name varchar(50), party varchar(25)) with a tupple
	  * 	for every candidate
	  * 	TABLE polls(poll_num int, recounts int, poll_counted) with a tupple
	  * 	for every poll and time recounted including initial (recount 0).
	  * 
	  * @param infofile
	  */
	 public pollingstation(Controller con, String infofile){
		 ctlr = con;
		 infoFile = infofile;
		 boolean exists = new File(infoFile).exists();
		 if(exists){
			 getBasics();
		 } else {
			 System.out.println("Error: cannot create polling station!\n"
					 + "Could not find info file:" + infoFile);
		 }
	 }
	 
	 
	 
	 /**
	  * getBasics() popoulates this polling station with info in info file provided
	  * info is formatted correctly
	  */
	 public void getBasics(){
		 try{
			 Class.forName("org.sqlite.JDBC");
			 try{
	             Connection icon = DriverManager.getConnection("jdbc:sqlite:" + infoFile);
	             Statement istat = icon.createStatement();
	             ResultSet ires = istat.executeQuery("SELECT * FROM basics");
	             
	             riding_name = ires.getString("riding_name");
	             if(ires.getString("central").equals("true")){ location = "Central Polling Station"; }
	             else { location = "Polling Station"; }
	             num_voters = ires.getInt("num_voters");
	             num_seats = ires.getInt("num_seats");
	             num_can = ires.getInt("num_candidates");
	            
	             ires = istat.executeQuery("SELECT COUNT(poll_num) FROM polls");
	             num_poll = ires.getInt(1);
	             
	             ires = istat.executeQuery("SELECT * FROM candidates");
	             can_party = new String[2*num_can];
	             ires.next();
	             for(int i = 0; i < num_can;i++){
	            	 can_party[i] = ires.getString("name");
	            	 can_party[i+num_can] = ires.getString("party");
	            	 ires.next();
	             }
	             
	             ires = istat.executeQuery("SELECT * FROM polls");
	             polls = new int[3*num_poll];
	             ires.next();
	             for(int j = 0; j < num_poll;j++){
	            	 polls[j] = ires.getInt("poll_num");
	            	 polls[j+num_poll]=ires.getInt("recounts");
	            	 polls[j+num_poll*2]=ires.getInt("poll_counted");
	            	 ires.next();
	             }
	             
	             icon.close();
	             
			 } catch(SQLException e){
				 e.printStackTrace();  
				 System.out.println(e.getMessage());
			 }
      	 } catch(ClassNotFoundException e){
      			 System.out.println("Error: Unable to load SQL driver please ensure driver :" +
      					 " sqlitejdbc-v056 exists and is part of java buildpath.");
      	 }
	 }
	 /**
	  * connect() is a helper function that opens a connection and statement to the  database
	  * Riding Info file
	  */
	 private void connect(){
		 try{
			 Class.forName("org.sqlite.JDBC");
		     con = DriverManager.getConnection("jdbc:sqlite:" + infoFile);
             stat = con.createStatement();
		 } catch(Exception e){ System.out.println(e.getMessage());}
	 }
	 
	 /**
	  * closeCon() is helper function that closes an connection ant statement
	  * that were open to the riding info file.
	  */
	 private void closeCon(){
		 try{
			 con.close();
			 stat.close();
		 } catch(Exception e){ System.out.println(); }
	 }
	 
	 /**
	  * getLocal() returns the location as read from info file when polling station
	  * created
	  * 
	  * @return the location of this pollingstation will either be:
	  * 		"Central Polling Station"
	  * 		"Polling Station"
	  */
	 public String getLocal(){return location;}
	 
	 /**
	  * getName() returns riding name of this polling station as read from info file
	  * when pollingstation created
	  * @return riding name
	  */
	 public String getName(){return riding_name;}
	 
	 /**
	  * getMaxVotes() returns the total number of eligible voters as read from info file
	  * when pollingstation created
	  * @return number of eligible voters in riding
	  */
	 public int getMaxVotes(){return num_voters;}
	 
	 /**
	  * getNumCan() returns the number of candidates running in this riding as read from
	  * info file when polligstation created
	  * 
	  * @return number of candidates running.
	  */
	 public int getNumCan(){return num_can;}
	 
	 /**
	  * getNumPoll() returns the number of polls to be counted in this riding as read from
	  * info file when pollingstation created
	  * 
	  * @return number of polls
	  */
	 public int getNumPoll(){return num_poll;}
	 
	 /**
	  * getNumSeats() returns the number of seats that candidates can be elected to as read
	  * from info file when pollingstation created
	  * 
	  * @return number of seats open in the election
	  */
	 public int getNumSeats(){return num_seats;}
	 
	 /**
	  * getCanInfo() returns a String array that has all cadidate names and their party affiliation
	  * as read from the info file when polling station created
	  *  
	  * @return String Array candidate names & party affiliation
	  * 		first 1/2 of array is names
	  * 		second 1/2 of array is party affiliation (same order)
	  */
	 public String[] getCanInfo(){return can_party.clone();}
	 
	 /**
	  * getPollInfo() returns an int array that has all poll numbers as read from info file when
	  * pollingstation created. Recounts and poll_counted numbers can be eddited after the initial reading
	  * 
	  * @return int array with the number of all the polls which recount we are on and whether this current
	  * 		recount has been counted.
	  */
	 public int[] getPollInfo(){return polls.clone();}
	 
	 
	 /**
	  * doUpdatePolls() updates a specific poll/recount combo in the info file
	  * to reflect the current value of that stored in array polls[]
	  * 
	  * @param which poll_num are you updating
	  * @param where what is the index in polls[]
	  */
	 public void doUpdatePolls( int poll , int phase ){
		 try{
			 connect();
			 if( phase == 3){
				 stat.executeUpdate("UPDATE polls SET poll_counted = recounts WHERE poll_num = " + poll);
			 }
			 stat.executeUpdate("INSERT INTO poll_worked(riding_name, poll_num, ro_login) VALUES('" +
					 riding_name							+"', '"+
					 poll									+"', '"+
					 ctlr.getUserMan().currUser.getLogin()	+"')");
	         closeCon();
		 } catch(Exception e){
			 e.printStackTrace();  
			 System.out.println(e.getMessage());
		 }
	 }

	/**
	 * checkCanEnter() checks whether a returning officer is able to enter a certain poll number.
	 * given as a parameter
	 * @param poll a poll number for a poll in this riding
	 * @throws Exception used as error messages for the user.
	 */
	 public void checkCanEnter(int poll) throws Exception{
		//Check to see if user has required permission to do poll entry.
		if(ctlr.getUserMan().currRole != 2){
			throw new Exception("<html>You are not logged in as a Returning Officer.<br>" +
					"Only Returning Officeres can do ballot entry.");
		}
		//Check to see if poll has already been counted.
		connect();
		ResultSet rs = stat.executeQuery("SELECT COUNT(poll_num) FROM polls WHERE poll_num = " + poll + " AND recounts = poll_counted");
		if(rs.getInt(1) != 0){
			throw new Exception("This poll has already been counted, does not exist or " +
					"no recounts have been issued.");
		}
		
		//Check to see if that this user has not already worked on this poll
		rs = stat.executeQuery("SELECT COUNT(poll_num) FROM poll_worked WHERE ro_login = '" + 
		ctlr.getUserMan().currUser.getLogin() +"' AND poll_num = " + poll );
		if(rs.getInt(1) != 0){
			throw new Exception("<html>You have already worked on this poll and<br>" + 
					"therefore can not work on it again");
			
		}
		closeCon();
	}


/**
 * @author Steve Evans
 */
	public void createBallotList(File selectedFile, int poll) {
		try{ 
			if(!selectedFile.exists()){
				selectedFile.createNewFile();
				ArrayDeque<ballot> toADD = ctlr.getBallMan().getFinal( poll );
				Connection ball = DriverManager.getConnection("jdbc:sqlite:" + selectedFile);
				Statement bstat = ball.createStatement();
				bstat.execute("CREATE TABLE ballots(riding_name varchar(25) NOT NULL, poll_num integer NOT NULL, ballotID integer NOT NULL, spoiled bit NOT NULL, candidate varchar(50), rank integer)");
				while(!toADD.isEmpty()){
					ballot Ballot = toADD.removeFirst();
					if(Ballot.getSpoiled()){
						bstat.execute("INSERT INTO ballots(riding_name, poll_num, ballotID, spoiled) VALUES ('" +
									riding_name			+"', '" +
									poll				+"', '" +
									Ballot.getID()		+"', '" +
									Ballot.getSpoiled() +"')  "	);
					} else {
						for(int i = 0; i < Ballot.getRanking().length; i++){
							bstat.execute("INSERT INTO ballots(riding_name, poll_num, ballotID, spoiled, candidate, rank) VALUES('"+
									riding_name				+"', '" +
									poll					+"', '" +
									Ballot.getID()			+"', '"	+
									Ballot.getSpoiled()		+"', '" +
									Ballot.getRanking()[i]	+"', '" +
									(i+1)					+"')  ");
						}
					}
				}
				bstat.execute("CREATE TABLE poll_worked(riding_name varchar(25) NOT NULL, poll_num integer NOT NULL, ro_login varchar(15))");
				connect();
				ResultSet rs = stat.executeQuery("SELECT * FROM poll_worked WHERE poll_num = " + poll);
				rs.next();
				while(!rs.isAfterLast()){
					bstat.execute("INSERT INTO poll_worked(riding_name, poll_num, ro_login) VALUES('" +
							rs.getString("riding_name")		+"', '"+
							rs.getInt("poll_num")			+"', '"+
							rs.getString("ro_login")		+"')  ");
					rs.next();		
				}
				bstat.close();
				ball.close();
				closeCon();				
			}
		} catch(Exception e){
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		
	}
	
	public int[] completedPolls(){
		connect();
		try{
			ResultSet rs = stat.executeQuery("SELECT COUNT(Distinct poll_num) FROM polls WHERE recounts = poll_counted");
			int[] res = new int[rs.getInt(1)];
			rs = stat.executeQuery("SELECT DISTINCT poll_num FROM polls WHERE recounts = poll_counted");
			rs.next();
			for(int i = 0; i < res.length ; i++){
				res[i] = rs.getInt("poll_num");
				rs.next();
			}
			return res;
		} catch(SQLException e){
			e.printStackTrace();
			System.out.println(e.getMessage());
			return null;
		}
		
	}
}
