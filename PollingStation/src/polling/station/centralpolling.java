package polling.station;
import initialize.Controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayDeque;
import java.util.Arrays;

import ballot.mangement.ballot;

public class centralpolling {
	private String resultFile = null;
	private Controller ctlr;
	Connection con = null;
	Statement stat = null;
	ResultSet rs = null;
	pollingstation PollStat = null;
	
	
	public centralpolling( Controller ct , String results){
		ctlr = ct;
		resultFile = results;
		try{
			 Class.forName("org.sqlite.JDBC");
		      try{
			      boolean ULExists = new File(resultFile).exists();
			      if ( ULExists ) {
			    	   connectToResults();
			      } else {
			    	  try{new File(resultFile).createNewFile();}catch(IOException e){e.printStackTrace();}
			    	  con = DriverManager.getConnection("jdbc:sqlite:" + resultFile);
			    	  stat = con.createStatement();
			    	  stat.execute("CREATE TABLE polls(riding_name varchar(25), poll_num integer NOT NULL, recounts integer NOT NULL, poll_counted integer NOT NULL, PRIMARY KEY(poll_num))");
			    	  stat.execute("CREATE TABLE ballots(riding_name varchar(25), poll_num integer NOT NULL, recount integer, ballotId integer NOT NULL," +
			    			  " spoiled varchar(6), candidate varchar(50), rank integer)");
			    	  stat.execute("CREATE TABLE poll_worked(riding_name varchar(25) NOT NULL, poll_num integer NOT NULL, ro_login varchar(15) NOT NULL)");
			    	  stat.execute("CREATE TABLE results(riding_name varchar(25), candidate varchar(50) NOT NULL, party varchar(25), elected bit, predicted bit, eliminated" +
			    			  " bit, first_vote int, transferred_elected integer, transferred_eliminated integer, PRIMARY KEY(candidate))");
			    	  enterPollNums();
			      }
			      closeConnection();
			      
		      }catch(SQLException e){
		    	  e.printStackTrace();  
				  System.out.println(e.getMessage());
		      }
		} catch(ClassNotFoundException e){
			System.out.println("Error: Can not find sqlite driver!\n");
		}	

	}
	
	private void enterPollNums() throws SQLException{
		int[] polls = ctlr.getPollStat().getPollInfo();
		for(int i = 0; i < polls.length/3;i++){
			stat.execute("INSERT INTO polls(riding_name, poll_num, recounts, poll_counted) VALUES('"+
						ctlr.getPollStat().getName()	+"', '"+
						polls[i]						+"', '"+
						polls[i+polls.length/3]			+"', '"+
						polls[i+polls.length/3*2]		+"')  ");
		}
	}
	
	private void connectToResults(){
		try{
			 Class.forName("org.sqlite.JDBC");
		} catch(ClassNotFoundException e){
			System.out.println("Error: Can not find sqlite driver!\n");
		}
		try{
			con = DriverManager.getConnection("jdbc:sqlite:" + resultFile);
			stat = con.createStatement();
		} catch(SQLException e){
			e.printStackTrace();  
			System.out.println(e.getMessage());
		}
	}
	
	private void closeConnection(){
		try{
			con.close();
			stat = null;
		} catch(SQLException e){
			e.printStackTrace();  
			System.out.println(e.getMessage());
		}
	}
	
	public String[] getElected(){
		connectToResults();
		try{
			ResultSet rs = stat.executeQuery("SELECT COUNT(candidate) FROM results WHERE elected = 'true'");
			String[] elect = new String[rs.getInt(1)];
			rs = stat.executeQuery("SELECT candidate FROM results WHERE elected = 'true'");
			rs.next();
			for(int i = 0; i < elect.length; i++){
				elect[i] = rs.getString("candidate");
				rs.next();
			}
			closeConnection();
			return elect;
		}catch(SQLException e){
			e.printStackTrace();
			System.out.println(e.getMessage());
			closeConnection();
			return null;
		}
	}

	public String[] getPredicted(){
		connectToResults();
		try{
			ResultSet rs = stat.executeQuery("SELECT COUNT(candidate) FROM results WHERE predicted = 'true'");
			String[] predict = new String[rs.getInt(1)];
			rs = stat.executeQuery("SELECT candidate FROM results WHERE predicted = 'true'");
			rs.next();
			for(int i = 0; i < predict.length; i++){
				predict[i] = rs.getString("candidate");
				rs.next();
			}
			closeConnection();
			return predict;
		}catch(SQLException e){
			e.printStackTrace();
			System.out.println(e.getMessage());
			closeConnection();
			return null;
		}
	}

	/**
	 * @author Steve Evans
	 */
	public void importBallotFile( File toImport ) throws Exception{
			try{
				Connection bcon = DriverManager.getConnection("jdbc:sqlite:" + toImport.getAbsolutePath() );
				Statement bstat = bcon.createStatement();
				ResultSet bres = null;
				bres = bstat.executeQuery("SELECT * FROM ballots");
				bres.next();
				int poll = bres.getInt("poll_num");
				
		//CHECK to see that this listing file corresponds to the polling station we are at.
				if(!bres.getString("riding_name").equals(ctlr.getPollStat().riding_name)){
					throw new Exception("<html>Error: Cannot import chosen ballot listing file<br>" +
							"The file you have chosen isn't for this riding.</html>");
				}
				connectToResults();
				boolean correctPoll = false;
				for(int i = 0; i < ctlr.getPollStat().getPollInfo().length/3;i++){
					if(ctlr.getPollStat().getPollInfo()[i] == poll){
						correctPoll=true;
					}
				}
				ResultSet rs = stat.executeQuery("SELECT * FROM ballots WHERE poll_num = " + poll);
				rs.next();
				
		//Check to see that the poll this listing file is for corresponds to a poll at this central polling station
				if( !correctPoll ){
					throw new Exception("<html>The Ballot Listing File as it does not correspond to any known poll <br>" +
							"numbers in this riding.</html>");		
					
		//CHECK to see whether this poll can accept another listing file(hasn't already been counted)
				} else if( !rs.isAfterLast() ){
					throw new Exception("<html>The Ballot Listing File could not be imported<br>" +
							"as it has already been imported.</html>");
				}
				
		//INSERT ballots into results file
				while(!bres.isAfterLast()){
					stat.execute("INSERT INTO ballots(riding_name, poll_num, ballotID, spoiled, candidate, rank) VALUES('" +
								ctlr.getPollStat().riding_name	+"', '"+
								bres.getInt("poll_num")			+"', '"+
								bres.getInt("BallotID")			+"', '"+
								bres.getString("spoiled") 		+"', '"+
								bres.getString("candidate")		+"', '"+
								bres.getString("rank")			+"')  ");
					bres.next();
				}
		
				bres = bstat.executeQuery("SELECT * FROM poll_worked");
				bres.next();
				
		//INSERT returning officers names to list of returning officers who cannot work on this poll
				while(!bres.isAfterLast()){
					stat.execute("INSERT INTO poll_worked(riding_name, poll_num, ro_login) VALUES('" +
								bres.getString("riding_name")	+"', '"+
								bres.getInt("poll_num")			+"', '"+
								bres.getString("ro_login") 		+"')  ");
					bres.next();
				}
				closeConnection();
				bstat.close();
				bcon.close();
				beginCalculate();
			}catch(SQLException e){
	    	  e.printStackTrace();  
			  System.out.println(e.getMessage());
		    }
	}

	public void beginCalculate(){
		int numSeat = ctlr.getPollStat().getNumSeats();
		String[] cand = ctlr.getPollStat().getCanInfo();
		double[] total_votes = new double[cand.length/2];
		Arrays.fill( total_votes , 0 );
		double[] transferred_elim = total_votes.clone();
		double[] transferred_elec = total_votes.clone(); 
		String[] elim = new String[0];
		
		
		ArrayDeque<ballot> ballots = getCalcBallots();
		int numberOfVotes = ballots.size();
		int quota = numberOfVotes/(numSeat+1)+1;
		total_votes = countCurrVote(ballots);
		double[] first_choice = total_votes.clone();
		if(cand.length/2 > numSeat){
			
			for(int i = 0; i < cand.length-numSeat; i++){
				for(int j = 0; j < total_votes.length; j++){
					System.out.println("Candidate:" + cand[j] + "\t votes: " + total_votes[j]);
				}
				if( checkIfElected( ballots, cand, total_votes, quota) ){
					total_votes  = countCurrVote( ballots );
					for(int j = 0; j < total_votes.length; j++){
						transferred_elec[j] = total_votes[j] - first_choice[j] - transferred_elim[j]; 
					}
				}
				
				elim = eliminate(ballots, cand, total_votes, elim);
				total_votes  = countCurrVote( ballots );
				for(int j = 0; j < total_votes.length; j++){
					transferred_elim[j] = total_votes[j] - first_choice[j] - transferred_elec[j]; 
				}
			}
		}
		
		boolean[] eliminated = new boolean[cand.length/2];
		for(int i = 0; i < cand.length/2; i++){
			eliminated[i] = (total_votes[i] == 0);
		}
		connectToResults();
		try{
			stat.execute("DELETE FROM results");
			for(int i = 0; i < cand.length/2; i++){
				stat.execute("INSERT INTO results(riding_name, candidate, party, elected, predicted, eliminated, first_vote, transferred_elected, transferred_eliminated) VALUES('" +
						ctlr.getPollStat().getName()		+"', '"+
						cand[i]								+"', '"+
						cand[i+cand.length/2]				+"', '"+
						(total_votes[i] >= quota)			+"', '"+
						 !eliminated[i]						+"', '"+
						 eliminated[i]						+"', '"+
						(int) first_choice[i]				+"', '"+
						(int) transferred_elec[i]			+"', '"+
						(int) transferred_elim[i]			+"') ");
			}
		} catch(SQLException e){
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}
	
	private ArrayDeque<ballot> getCalcBallots(){
		ArrayDeque<ballot> ballots = new ArrayDeque<ballot>();
		try{ 
			connectToResults();
			ResultSet rs = stat.executeQuery(
				"SELECT * FROM ballots WHERE spoiled = 'false' ORDER BY ballotID,rank");
			rs.next();
			while(!rs.isAfterLast() ){
				int ballotID = rs.getInt("ballotID");
				ArrayDeque<String> ranking = new ArrayDeque<String>();
				while( !rs.isAfterLast() && ballotID == rs.getInt("ballotID") ){
					ranking.add(rs.getString("candidate"));
					rs.next();
				}
				String[] ranks = new String[ranking.size()];
				for(int i = 0; i < ranks.length; i++){
					ranks[i] = ranking.removeFirst();
				}
				ballots.add( new ballot( ranks, ballotID , 0));
			}
		} catch(SQLException e){
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		return ballots;
	}
	
	private double[] countCurrVote( ArrayDeque<ballot> ballots ){
		String[] cand = ctlr.getPollStat().getCanInfo();
		double[] total_votes = new double[ctlr.getPollStat().getNumCan()];
		Arrays.fill(total_votes, 0);
		for(int i = 0; i < cand.length/2; i++){
			for(int j = 0; j < ballots.size(); j++){
				ballot Ballot = ballots.removeFirst();
				total_votes[i] = total_votes[i] + Ballot.countVoteFor(cand[i]);
				ballots.addLast(Ballot);
			}
		}
		return total_votes;
	}
	
	private boolean checkIfElected( ArrayDeque<ballot> ballots, String[] cand ,double[] total_votes, int quota ){
		boolean elect = false;
		for(int i = 0; i < cand.length/2; i++){
			if( total_votes[i] > quota ){
				int rate = (int) ((total_votes[i] - quota)/total_votes[i]);
				for(int j = 0; j < ballots.size();j++ ){
					ballot b = ballots.remove();
					b.transferElected(cand[i], rate);
					ballots.add(b);
				}
				elect=true;
			}
		}
		return elect;
	}
	
	private String[] eliminate(ArrayDeque<ballot> ballots, String[] cand, double[] total_votes, String[] elim ){
		int index = 0;
		String[] eliminated = new String[elim.length+1];
		for(int i = 0; i < elim.length;i++){
			eliminated[i] = elim[i];
		}
		for(int i = 1; i < total_votes.length; i++){
			if(total_votes[index] > total_votes[i]){
				index =i;
			}
		}
		eliminated[elim.length] = cand[index];
		for(int j = 0; j < ballots.size();j++){
			ballot b = ballots.remove();
			b.transferEliminated( eliminated );
			ballots.addLast(b);
		}
		return eliminated;
	}
	
	/**
	 * @author Steve Evans
	 */
	public void createResults ( File dest )throws Exception{
		dest.createNewFile();
		File source = new File( resultFile );
		InputStream in = new FileInputStream( source );
		OutputStream out = new FileOutputStream( dest );
		byte[] buffer = new byte[1024];
		int curr;
		while((curr = in.read(buffer))>0){
			out.write(buffer,0,curr);
		}		
	}
}