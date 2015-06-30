package StatusManager;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ElectionStatusManagement {
	private int status;
	private Connection con = null;
    private Statement stat = null;
    private ResultSet rs = null;
    private String RideRep;
    
	public ElectionStatusManagement( String RepFile )
	{
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
	
	
	public int AdvanceToNextStatus(int currStatus)
	{
		
		if (currStatus == 3)
		{
			status = 2;
		}
		else
		{
			status = (currStatus +1)%3;
		}
		connect();
    	try{
    		/*
    		boolean ridingInfoCreated = true;
    		int rowsNumber = 0;
    		rowsNumber = stat.executeUpdate("select count (*) from ridings");
    		for(int i = 0; i < rowsNumber; i++ )
    		{
    			select 
    		}
    		*/
    		
    		String query = "update status set status = " + status + ";" ;
    		stat.execute(query);

    	}
    	catch(SQLException e){
		   e.printStackTrace();
		   System.out.println(e.getMessage());
		}
    	
    	closeCon();
    	return status;
	}
	
	public void AdvanceToOptionalStatus(int currStatus)
	{
		
		status = 3;
		connect();
    	try{
    		String query = "update status set status = " + status + ";" ;
    		stat.execute(query);
    		
    	}
    	catch(SQLException e){
		   e.printStackTrace();
		   System.out.println(e.getMessage());
		}
    	
    	closeCon();
	}
}



