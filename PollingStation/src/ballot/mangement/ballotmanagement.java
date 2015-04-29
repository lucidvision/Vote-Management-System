package ballot.mangement;
import initialize.Controller;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.io.File;

public class ballotmanagement {
	private String Destination; private int curr_poll = -1;
	private Controller ctlr;
	private Connection con;
	private Statement stat;
	
	public ballotmanagement( Controller ctr , String dest ){
		ctlr = ctr;
		Destination = dest;
	}
	
	private void connect(){
		try{
			Class.forName("org.sqlite.JDBC");
			try{
				con = DriverManager.getConnection("jdbc:sqlite:" + 
						Destination + "EntryForPoll-" + curr_poll + ".sqlite");
				stat = con.createStatement();
			} catch(SQLException e){
				System.out.println(e.getMessage());
			}
		} catch(ClassNotFoundException e){
			System.out.println(e.getMessage());
		}
	}
	
	private void closeCon(){
		try{
			con.close();
			stat = null;
		} catch(SQLException e){
			System.out.println(e.getMessage());
		}
	}
	/**
	 * @author Tomy Tao
	 */
	public int beginEntry(int poll) throws Exception{
			curr_poll = poll;
			ctlr.getPollStat().checkCanEnter(poll);
			File file= new File(Destination + "EntryForPoll-" + curr_poll + ".sqlite");
			if(!file.exists()){
				file.createNewFile();
				connect();
				stat.execute("CREATE TABLE ballots(ballotID integer, poll_num integer, phase integer, spoiled bit, candidate varchar(50), rank integer)");
				closeCon();
				return 1;
			} else {
				connect();
				ResultSet rs = stat.executeQuery("Select * FROM ballots WHERE poll_num = " + curr_poll);
				int i = 0;
				while(!rs.isAfterLast()){
					if(rs.getInt("phase") > i){
						i = rs.getInt("phase");
						System.out.println(i);
					}
					rs.next();
				}
				closeCon();
				return (i+1);
			}
	}
	/**
	 * @author Tomy Tao
	 */
	public void submit(ArrayDeque<ballot> ballots, int phase){
		try{
			connect();
			while(!ballots.isEmpty()){
				ballot Ballot = ballots.removeFirst();
				if(!Ballot.getSpoiled()){
					for(int i = 0; i < Ballot.getRanking().length; i++){
						stat.execute("INSERT INTO ballots(ballotID, poll_num, phase, spoiled, candidate, rank) VALUES ('" +
								Ballot.getID() 			+"', '"+
								curr_poll				+"', '"+
								phase					+"', '"+
								Ballot.getSpoiled()		+"', '"+
								Ballot.getRanking()[i]	+"', '"+
								(i+1)					+"')  ");
					}
				} else {
					stat.execute("INSERT INTO ballots(ballotID, poll_num, phase, spoiled) VALUES ('" +
								Ballot.getID() 			+"', '"+
								curr_poll				+"', '"+
								phase					+"', '"+
								Ballot.getSpoiled()		+"')  ");
				}
					
			}
			closeCon();
		} catch(SQLException e){
			System.out.println(e.getMessage());
		}
		ctlr.getPollStat().doUpdatePolls( curr_poll , phase );
	}
	/**
	 * @author Tomy Tao
	 */
	public ballot getBallot( int id, int phase ){
		connect();
		try{
			ResultSet rs = stat.executeQuery("SELECT * FROM ballots WHERE phase = " + phase + " AND  ballotID = " + id );
			if("true".equals(rs.getString("spoiled"))){
				closeCon();
				return new ballot( id );
			} else {
				ArrayDeque<String> ranks = new ArrayDeque<String>();
				rs.next();
				while(!rs.isAfterLast()){
					ranks.add(rs.getString("candidate"));
					System.out.println("Adding: " + rs.getString("candidate"));
					rs.next();
				}
				
				closeCon();
				int num = ranks.size();
				String[] rank = new String[num];
				
				for(int i = 0; i < num; i++ ){
					rank[i] = ranks.removeFirst();
					System.out.println( rank[i]);
				}
				System.out.println( id + " " + Arrays.toString(rank));
				return new ballot( rank , id );
			}
		} catch(SQLException e){
			System.out.println( e.getMessage() );
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * @author Tomy Tao
	 */
	public ArrayDeque<ballot> getPass( int phase ){
		ArrayDeque<ballot> pass = new ArrayDeque<ballot>();
		connect();
		try{
			System.out.println("Hmm");
			ResultSet rs = stat.executeQuery("SELECT COUNT(DISTINCT ballotID) FROM ballots where phase = " + phase + " AND poll_num = " + curr_poll );
			int size = rs.getInt(1);
			int[] ids = new int[size];
			System.out.println("yep");
			rs = stat.executeQuery("SELECT DISTINCT ballotID FROM ballots WHERE phase = " + phase + " AND poll_num = " + curr_poll);
			for( int i = 0; i < size; i++){
				rs.next();
				ids[i] = rs.getInt("ballotId");
			}
			closeCon();
			for( int i = 0; i < size; i++){
				pass.add( getBallot( ids[i] , phase));
			}
			return pass;
		} catch(SQLException e){
			System.out.println(e.getMessage());
			e.printStackTrace();
			return null;
		}
		
	}
	/**
	 * @author Tomy Tao
	 */
	public ArrayDeque<ballot> getFinal( int poll ){
		ArrayDeque<ballot> pass = new ArrayDeque<ballot>();
		curr_poll = poll;
		connect();
		try{
			System.out.println("Hmm");
			ResultSet rs = stat.executeQuery("SELECT COUNT(DISTINCT ballotID) FROM ballots");
			int size = rs.getInt(1);
			int[] ids = new int[size];
			System.out.println("yep");
			rs = stat.executeQuery("SELECT DISTINCT ballotID FROM ballots");
			for( int i = 0; i < size; i++){
				rs.next();
				ids[i] = rs.getInt("ballotId");
			}
			closeCon();
			for( int i = 0; i < size; i++){
				pass.add( getBallot( ids[i] , 3));
			}
			return pass;
		} catch(SQLException e){
			e.printStackTrace();
			return null;
		}
		
	}

}
