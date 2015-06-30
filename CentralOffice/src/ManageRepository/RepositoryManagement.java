package ManageRepository;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class RepositoryManagement {
	  private Connection con = null;
	  private Statement stat = null;
	  private ResultSet rs = null;
	  private String RideRep;
	  
	  public RepositoryManagement(String RepFile){
	      try{
			  Class.forName("org.sqlite.JDBC");
			  try{
			      RideRep = RepFile;
			      boolean ULExists = new File(RideRep).exists();
			      if(ULExists){
			      con = DriverManager.getConnection("jdbc:sqlite:" + RideRep);
			      stat = con.createStatement();
			      con.close();
			      stat = null;
			      } else {
			    	  System.out.println("Error: Riding Repository could not be found.\n"
			    			  + "RepositoryManager can not be created.\n" +
			    			  "Save Repository database to " + RideRep + " and run program again.");
			      }
			  } catch (SQLException e){
			    e.printStackTrace();
			    System.out.println(e.getMessage());
			  }
	      } catch(ClassNotFoundException e){
	    	  System.out.println("SQLite Driver not found.");
	      }
	  }
	  
	  private void connect(){
		  try{
			  con = DriverManager.getConnection("jdbc:sqlite:" + RideRep);
			  stat = con.createStatement();
		  } catch (SQLException e){
			    e.printStackTrace();
			    System.out.println(e.getMessage());
		  }
	  }
	  
	  private void closeCon(){
		  try{
			  con.close();
			  stat = null;
			  rs = null;
		  } catch (SQLException e){
			    e.printStackTrace();
			    System.out.println(e.getMessage());
		  }
	  }
	  
	  
	   public riding getRiding(String rideName){
		   connect();
		   try{
			   rs = stat.executeQuery("Select * FROM ridings WHERE riding_name = '" + rideName + "'");
			   riding ride = new riding();
			   ride.setRidingName(rs.getString("riding_name"));
			   ride.setDate(rs.getString("date_created"));
			   ride.setNumVote(rs.getInt("number_of_voters"));
			   ride.setNumCandi(rs.getInt("number_of_candidates"));
			   ride.setNumIncum(rs.getInt("number_of_incumbents"));
			   ride.setMap(rs.getString("map"));
			   ride.setNumPolls(rs.getInt("number_of_polls"));
			   ride.setNumSeats(rs.getInt("number_of_seats"));
			   
			   int c = ride.getNumCandi();
			   String[] name = new String[c];
			   String[] party = new String[c];
			   rs = stat.executeQuery("Select * FROM candidates WHERE riding_name = '" + rideName + "'");
			   rs.next();
			   for(int i = 0;i < c;i++){
				   name[i] = rs.getString("name");
				   party[i] = rs.getString("party");
				   rs.next();
			   }
			   ride.setCandiName(name);
			   ride.setCandiAff(party);
			   
			   c = ride.getNumIncum();
			   name = new String[c];
			   party = new String[c];
			   rs = stat.executeQuery("Select * FROM incumbents WHERE riding_name = '" + rideName + "'");
			   rs.next();
			   for(int i = 0;i <c;i++){
				   name[i] = rs.getString("name");
				   party[i] = rs.getString("party");
				   rs.next();
			   }
			   ride.setIncumbets(name);
			   ride.setIncumAff(party);
			   
			   c = ride.getNumPolls();
			   int[] polls = new int[c];
			   rs = stat.executeQuery("Select * FROM polls WHERE riding_name = '" + rideName + "'");
			   rs.next();
			   for(int i = 0;i < ride.getNumPolls();i++){
				   polls[i] = rs.getInt("poll_num");
				   rs.next();
			   }
			   ride.setPolls(polls);
			   closeCon();
			   return ride;
		   }
		   catch(SQLException e){
			   e.printStackTrace();
			   System.out.println(e.getMessage());
		   }
		   return null;
	   }
	   
	   public void saveNewRiding(riding ride){
		   connect();
		   try{
			   String query = "INSERT INTO ridings(riding_name,date_created,number_of_voters,number_of_candidates,number_of_incumbents,map," +
			   		"number_of_polls,number_of_seats,info_created) VALUES('" +
					   ride.getRidingName() + "', '" +
					   ride.getDateCr() + "', '" +
					   ride.getNumVote() + "', '" +
					   ride.getNumCandi() + "', '" +
					   ride.getNumIncum() + "', '" +
					   ride.getMap() + "', '" +
					   ride.getNumPolls() + "', '" +
					   ride.getNumSeats() + "', '" +
					   "0')";
			   stat.execute( query );
			   for(int i = 0;i < ride.getNumCandi();i++){
				   query = "INSERT INTO candidates(riding_name,name,party) VALUES('" + ride.getRidingName() + "','" + ride.getCandiName()[i] + "','" + ride.getCandiAff()[i] + "')";
				   stat.execute( query );
			   }
			   for(int i = 0; i < ride.getNumIncum();i++){
				   query = "INSERT INTO incumbents(riding_name,name,party) VALUES('" + ride.getRidingName() + "','" + ride.getIncumbets()[i] + "','" + ride.getIncumbAff()[i] + "')";
				   stat.execute( query );
			   }
			   for(int i = 0; i < ride.getNumPolls();i++){
				   query = "INSERT INTO polls(riding_name,poll_num,recount,poll_counted) VALUES('" +
						   	ride.getRidingName() + "','" + ride.getPolls()[i] + "',0,-1)";
				   stat.execute( query );
			   }
		   }catch(SQLException e){
			   e.printStackTrace();
			   System.out.println(e.getMessage());
		   }	   		   
	   }
	   
	   public void updateRiding(riding ride, String oldname){
		   connect();
		   try{
			   String query = "UPDATE ridings SET " +
					   "riding_name = '" + ride.getRidingName() + "'," +
					   "date_created = '" + ride.getDateCr() + "'," +
					   "number_of_voters= '" + ride.getNumVote() + "'," +
					   "number_of_candidates='" + ride.getNumCandi() + "'," +
					   "number_of_incumbents='" + ride.getNumIncum() + "'," +
					   "map ='" + ride.getMap() + "'," +
					   "number_of_polls ='" + ride.getNumPolls() + "'," +
					   "number_of_seats ='" + ride.getNumSeats() + "' WHERE riding_name = '" + oldname + "'";
			   stat.execute( query );
			   stat.execute("DELETE from candidates WHERE riding_name = '" + oldname + "'");
			   stat.execute("DELETE from incumbents WHERE riding_name = '" + oldname + "'");
			   stat.execute("DELETE from polls WHERE riding_name = '" + oldname + "'");
			   for(int i = 0;i < ride.getNumCandi();i++){
				   query = "INSERT INTO candidates(riding_name,name,party) VALUES('" + ride.getRidingName() + "','" + ride.getCandiName()[i] + "','" + ride.getCandiAff()[i] + "')";
				   stat.execute( query );
			   }
			   for(int i = 0; i < ride.getNumIncum();i++){
				   query = "INSERT INTO incumbents(riding_name,name,party) VALUES('" + ride.getRidingName() + "','" + ride.getIncumbets()[i] + "','" + ride.getIncumbAff()[i] + "')";
				   stat.execute( query );
			   }
			   for(int i = 0; i < ride.getNumPolls();i++){
				   query = "INSERT INTO polls(riding_name,poll_num) VALUES('" + ride.getRidingName() + "','" + ride.getPolls()[i] + "')";
				   stat.execute( query );
			   }
		   }catch(SQLException e){
			   e.printStackTrace();
			   System.out.println(e.getMessage());
		   }	   		   
	   }
	   
	   public void deleteRiding( riding ride ){
		   connect();
		   try{
			   stat.execute("DELETE FROM ridings WHERE riding_name = '" + ride.getRidingName() + "'");
			   stat.execute("DELETE FROM polls WHERE riding_name = '" + ride.getRidingName() + "'");
			   stat.execute("DELETE FROM candidates WHERE riding_name = '" + ride.getRidingName() + "'");
			   stat.execute("DELETE FROM incumbents WHERE riding_name = '" + ride.getRidingName() + "'");
		   }catch(SQLException e){
			   e.printStackTrace();
			   System.out.println(e.getMessage());
		   }	   		   
	   }
	   
	   public String[] getRidingList(){
		   connect();
		   String[] ridings;
		   try{
			   rs = stat.executeQuery("SELECT COUNT(riding_name) FROM ridings");
			   ridings = new String[rs.getInt(1)];
			   rs = stat.executeQuery("SELECT riding_name FROM ridings");
			   rs .next();
			   for(int i = 0;i < ridings.length;i++){
				   ridings[i] = rs.getString("riding_name");
				   rs.next();
				}
			   closeCon();
		   } catch(SQLException e){
			   e.printStackTrace();
			   System.out.println();
			   ridings = null;
		   }
		   return ridings;
	   }
	   
	   public void commitResults( File resultsfile) throws Exception{
		   if( resultsfile.exists() ){
			   try {
				   Connection rcon = DriverManager.getConnection("jdbc:sqlite:" + resultsfile.getAbsolutePath());
				   Statement rstat = rcon.createStatement();
				   connect();
				   ResultSet rrs = rstat.executeQuery("SELECT riding_name FROM ballots");
				   String ridename = rrs.getString("riding_name");
				   rrs = rstat.executeQuery("SELECT DISTINCT poll_num FROM ballots");
				   ResultSet rs = stat.executeQuery("SELECT COUNT(riding_name) FROM ridings WHERE riding_name = '" + ridename + "'");
				   if(rs.getInt(1) == 0 ){
					   throw new Exception("This results file does not correspond with any known riding.");
				   }
				   rrs = stat.executeQuery("SELECT * FROM polls WHERE riding_name = '" +ridename +"'");
				   rrs.next();
				   while(!rrs.isAfterLast()){
					   int poll_num = rs.getInt("poll_num");
					   int recount = rs.getInt("recount");
					   int poll_counted = rs.getInt("poll_counted");
					   stat.execute("UPDATE polls SET recount = '" + recount + "',poll_counted ='"+poll_counted + "' WHERE poll_num = '" + poll_num + "' AND '" + ridename + "'");
					   if(recount != poll_counted){
						   System.out.println(poll_num);
						   ResultSet rts = rstat.executeQuery("SELECT * FROM ballots WHERE poll_num = '" + poll_num + "'");
						   stat.execute("DELETE FROM ballots WHERE poll_num = '" + poll_num + "' AND recount ='" + recount + "' AND '" + ridename + "'");
						   rts.next();
						   while(!rts.isAfterLast()){
							   stat.execute("INSERT INTO ballots(riding_name, ballotId, recount,spoiled,candidate,rank) VALUES ('" +
									   ridename	 					+ "', '" +
									   rts.getInt("ballotId") 		+ "', '" + 
									   (recount+1)			 		+ "', '" + 
									   rts.getString("spoiled") 	+ "', '" + 
									   rts.getInt("candidate") 		+ "', '" + 
									   rts.getInt("rank") 			+ "')");
							   rts.next();
						   }
					   }
					   rrs.next();
				   }
				   
				   rrs = rstat.executeQuery("SELECT * FROM results");
				   stat.execute("DELETE FROM results WHERE riding_name = '" + ridename + "'");
				   rrs.next();
				   while(!rrs.isAfterLast()){
					   stat.execute("INSERT INTO results(riding_name, candidate, party, elected, predicted, eliminated, first_choice, transferred_eliminated, transferred_elected) VALUES('"+
							   ridename 							+ "', '" +
							   rrs.getString("candidate") 			+ "', '" +
							   rrs.getString("party")				+ "', '" +
							   rrs.getString("elected") 			+ "', '" +
							   rrs.getString("predicted")			+ "', '" +
							   rrs.getString("eliminated")	 		+ "', '" +
							   rrs.getInt("first_vote") 			+ "', '" +
							   rrs.getInt("transferred_eliminated") + "', '" +
							   rrs.getInt("transferred_elected") + "')");
					   rrs.next();
				   }
				   
				   rrs = rstat.executeQuery("SELECT * FROM poll_worked");
				   rrs.next();
				   while(!rrs.isAfterLast()){
					   	stat.execute("INSERT INTO poll_worked(riding_name, poll_num, ro_login) VALUES('"+
					   			rrs.getString("riding_name")	+"', '" +
					   			rrs.getString("poll_num")		+"', '" +
					   			rrs.getString("ro_login")		+"') "	);
					   	rrs.next();
				   }
				   rstat.close();
				   rcon.close();
				   	resultsfile.delete();
				   	
				    closeCon();
				 } catch(Exception e ){
					 e.printStackTrace();
					 throw new Exception("<html>This results file is not formatted correctly <br>" +
					 		"and therefore can not be imported.</html>");
					 	
			   }
		   }
	   }
	   
	   public File createRidingInforFile( riding ride, File file, boolean central) throws Exception
	{
    	// create local driver and create the file	
    	// get the connection with riding information file
		   
		   String path = file.getAbsolutePath();
	    	try{
				  Connection icon = DriverManager.getConnection( "jdbc:sqlite:" + path );
				  Statement istat = icon.createStatement();
			  
	    	
	    	//  CREATE RIDING INFO FILE'S REQUIRED TABLES
	    	istat.execute("CREATE TABLE basics(riding_name varchar(25)," +
	    				"num_voters integer, num_seats integer, num_candidates " + 
	    				"integer, central bit)");
	        istat.execute("create table candidates (riding_name varchar(25),name "+
	    				"varchar(50), party varchar(25));");
	    	istat.execute("create table polls(riding_name varchar(25),poll_num " +
    				"integer, recounts integer, poll_counted integer);");
	    	istat.execute("create table poll_worked(riding_name varchar(25), poll_num " +
    				"integer, ro_login varchar(15))");
	    	
	    	// update the table basics
	    	istat.execute("INSERT INTO basics (riding_name, num_voters, num_seats " +
	    				", num_candidates, central) VALUES( '" + 
	    				ride.getRidingName() 	+ "','" +
	    				ride.getNumVote()		+ "','" + 
	    				ride.getNumSeats()		+ "','" +
	    				ride.getNumCandi()		+ "','" +
	    				central 				+ "') " );
	    	// insert for candidate table
	    	for (int i= 0; i < ride.getCandiName().length; i++){
	    		istat.execute("INSERT INTO candidates (riding_name ,name , party )" +
	    				" values ( '" + 
	    				ride.getRidingName() 	+ "','" +
	    				ride.getCandiName()[i] 	+ "','" + 
	    				ride.getCandiAff()[i]	+ "') " );
	    	}
	    	// insert for polls table
	    	connect();
	    	rs = stat.executeQuery("SELECT * from polls WHERE riding_name = '" + 
	    				ride.getRidingName()+ "'");
	    	rs.next();
	    	while(!rs.isAfterLast()){
	    		istat.execute("INSERT INTO polls (riding_name,poll_num, recounts, poll_counted)" +
	    				" VALUES ( '" + 
	    				ride.getRidingName() 	 	 + "','" +
	    				rs.getString("poll_num") 	 + "','" +
	    				rs.getString("recount") 	 + "','" +
	    				rs.getString("poll_counted") +"') " );
	    		
	    		rs.next();
	    	}
	    	rs = stat.executeQuery("SELECT * FROM poll_worked WHERE riding_name = '" + 
	    			ride.getRidingName() + "'");
	    	rs.next();
	    	while(!rs.isAfterLast()){
	    		istat.execute("INSERT INTO poll_worked(riding_name, poll_num, ro_login) VALUES('" + 
	    				rs.getString("riding_name")	+"', '" +
	    				rs.getInt("poll_num")		+"', '" +
	    				rs.getString("ro_login")	+"')  ");
	    		rs.next();
	    	}
	    	return file;
    	}catch (SQLException e){
			    e.printStackTrace();
			    System.out.println(e.getMessage());
		}
    
	   return null;
	}
	
	public void archive(File file) throws Exception{
		if(file.createNewFile()){
			File rep = new File(RideRep);
			InputStream in = new FileInputStream(rep);
			OutputStream out = new FileOutputStream(file);
			byte[] buffer = new byte[1024];
			int curr;
			while((curr = in.read(buffer))> 0 ){
				out.write(buffer,0,curr);
			} 
		} else { 
			throw new Exception(
					"File was not abled to be created.");
			
		}
	}
	
	public int getCurrStatus() 
    {
    	int status = -1;
    	connect();
    	try{
    		String query = "select status from status;";
    		rs = stat.executeQuery(query);
    		status = rs.getInt("status");
    		
    	}
    	catch(SQLException e){
		   e.printStackTrace();
		   System.out.println(e.getMessage());
		}
    	
    	closeCon();
    	return status;
    }
	
	public String getCurrStatusName(){
		String[] status = {"Before Election", "During Election", "After Election", "Recount"};
		int index = getCurrStatus();
		return status[index];
	}
	
	public boolean checkIfRecount()
	{
		boolean isRecount = false;
		connect();
		try{
    		String query = "select recount from polls;";
    		rs = stat.executeQuery(query);
    		while(rs.next())
    		{
    			if (rs.getInt("recount") == 1)
    			{
    				isRecount = true;
    			}
    		}
    		
    	}
    	catch(SQLException e){
		   e.printStackTrace();
		   System.out.println(e.getMessage());
		}
    	closeCon();
		return isRecount;
	}
}

