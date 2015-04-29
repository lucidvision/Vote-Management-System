package initialize;
import java.io.File;
import GUI.LoginWindow;
import ManageUsers.UserManagement;
import ballot.mangement.ballotmanagement;
import polling.station.centralpolling;
import polling.station.pollingstation;


public class Controller {
	private static String UserList = "dat/UserList.sqlite";
	private static String infoFile = "dat/infoFile.sqlite";
	private static String ballotDest = "dat/polls/";
	private static String resultsDest = "dat/results/results.sqlite";
	private static pollingstation PollStat;
	private static UserManagement UseMan;
	private static ballotmanagement Ballman;
	private static centralpolling Central;
	
	private Controller(){
			PollStat = new pollingstation( this, infoFile );
			UseMan = new UserManagement( UserList );
			Ballman = new ballotmanagement ( this, ballotDest );
			if(PollStat.getLocal().equals("Central Polling Station")){
				Central = new centralpolling( this , resultsDest );
			} else {
				Central = null;
			}
			begin();
	}
	

	public void begin(){
		new LoginWindow(this);
	}
	
	public pollingstation getPollStat()	{ return PollStat; }
	public UserManagement getUserMan()	{ return UseMan;   }
	public ballotmanagement getBallMan(){ return Ballman;  }
	public centralpolling getCentral() { return Central; }
	
	public static void main(String[] arg0){
		if(!new File( UserList).exists())	new importUsers( UserList );
		if(!new File( infoFile ).exists())	new importInfo( infoFile );
		while(!new File( UserList ).exists() || !new File(infoFile).exists()){
			
		}
		new Controller();
		
	}


	

}
	