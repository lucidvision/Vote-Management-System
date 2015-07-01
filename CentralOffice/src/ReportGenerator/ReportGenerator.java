package ReportGenerator;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class ReportGenerator {
	private Connection con = null;
	  private Statement stat = null;
	  private String RideRep;
	  private ArrayList<String> winners = new ArrayList<String>();
	  private ArrayList<String> predicted = new ArrayList<String>();
	
	  public ReportGenerator(String RepFile) {
		// TODO Auto-generated constructor stub
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
		  } catch (SQLException e){
			    e.printStackTrace();
			    System.out.println(e.getMessage());
		  }
	  
	  }
	
	public String viewResult( String rideName ) throws SQLException
	{
		connect();
		String query = "select candidate from results where elected = 'true' AND riding_name = '"+rideName+"'; " ;
		ResultSet resultSet = stat.executeQuery( query );

		while (resultSet.next())
		{
			winners.add(resultSet.getString("candidate"));
			
		}
		
		query = "select candidate from results where predicted = 'true'  AND riding_name = '"+rideName+"'" ;
		resultSet = stat.executeQuery( query );
		
		while (resultSet.next())
		{
			predicted.add(resultSet.getString("candidate"));
		}
		resultSet = stat.executeQuery("SELECT number_of_voters FROM ridings WHERE riding_name = '"+rideName+"'");
		int max = resultSet.getInt("number_of_voters");
		resultSet = stat.executeQuery("SELECT COUNT(distinct ballotID) from ballots where riding_name = '" +rideName+"'");
		int act = resultSet.getInt(1);
		double percent = (double)act/(double)max*100;
		closeCon();
		String ret = "";
		ret = ret + "Results for Riding:\t" + rideName + "\n";
		ret = ret +"\tPercent of maximum votes counted: " + percent + "%\n";
		if(winners.size()>0){
			ret = ret + "\tCandidates Elected:\n";
			int numW = winners.size();
			for(int i = 0;i < numW ; i++){
				ret = ret + "\t\t"+winners.get(i)+"\n";
			}
		} else {
			ret = ret + "\tNo Candidates have officially been elected.\n";
		}
		if(predicted.size()>0){
			ret = ret + "\tPredicted Winners:\n";
			int numP = predicted.size();
			for(int i = 0;i < numP ; i++){
				ret = ret + "\t\t"+predicted.get(i)+"\n";
			}		
		} else {
			ret = ret + "\tNot enough of the vote has been completed to predict winners.\n";
		}
		ret = ret + "\n";
		return ret;
	}

}
