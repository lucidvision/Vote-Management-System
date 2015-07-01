package ManageUsers;
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

 
public class UserManagement {
  private Connection connection = null;
  private Statement statement = null;
  private ResultSet resultSet = null;
  private String UserList;
  public elecUser currUser = null;
  public int currRole = -1;
 
  /**
   * UserManagement constructor attempts to establish a connection with the given 
   * userlist or else throws errors
   * 
   * @param UserList
   */
  public UserManagement(String userlist){
	  UserList = userlist;
      try{
    	  Class.forName("org.sqlite.JDBC");
    	  try{
    		  boolean ULExists = new File(UserList).exists();
    	      if( ULExists ){
    		  connection = DriverManager.getConnection("jdbc:sqlite:" + UserList);
    	      statement = connection.createStatement();
    	      } else {
    	    	  System.out.println("Error: User List be found.\n"
    	    			  + "UserManager can not be created.\n" +
    	    			  "Save UserList database to " + UserList + "and run program again.");
    	      }
    	  } catch(SQLException e) {
    		  e.printStackTrace();
    		  System.out.println(e.getMessage());
    	  }
      } catch(ClassNotFoundException e) {
    	  System.out.println("Error: SQLITE driver not loaded!!");
      }
      
  }
 
  	private void connect(){
  		try{
  			connection = DriverManager.getConnection("jdbc:sqlite:" + UserList);
  			statement = connection.createStatement();
  		} catch(SQLException e) {
  		  e.printStackTrace();
  		  System.out.println(e.getMessage());
  		}
  	}
  			
  /**
   * Close the userList (when done using it).
   */
  public void close() {
    try {
	      statement.close();
	      connection.close();
    } catch (Exception e) {
    	System.out.println(e.getMessage());
    	e.printStackTrace();
    }
  }
 
  /**
   * Returns an instantiation of class elecUser with the desired
   * user login provided user is found in userList. If not returns
   * null
   */
  public elecUser getUser(String login){
	  elecUser use = null;
	  String query = "SELECT * FROM users WHERE login = '" + login + "'";
	  try {
		  	  connect();
		  	  resultSet = statement.executeQuery( query );
		      use = new elecUser();
		      use.setLogin(resultSet.getString("login"));
		      use.setPass(resultSet.getString("password"));
		      use.setFName(resultSet.getString("f_name"));
		      use.setLName(resultSet.getString("l_name"));
		      use.setBlock(resultSet.getString("blocked").equals("true"));
		      boolean[] roles = { resultSet.getString("sa").equals("true"),
		    		  			  resultSet.getString("eo").equals("true"),
		    		  			  resultSet.getString("ro").equals("true"),
		    		  			  resultSet.getString("re").equals("true")};
		      use.setRole(roles);
		      use.setUse(resultSet.getInt("use"));
		      resultSet = null;
		      close();
	  } catch (Exception e) {
	    	System.out.println(e.getMessage());
	    	e.printStackTrace();
	  }	      
    return use;
  }
  
  /**
   * deleteUser() delete a given user from the user list.
   */
  public void deleteUser(elecUser user){
	  String query = "DELETE FROM users WHERE login = '" + user.getLogin() + "'";
	  try {
		  	  connect();
		  	  statement.execute( query );
		      close();
	  } catch (Exception e) {
	    	System.out.println(e.getMessage());
	    	e.printStackTrace();
	  }	      
  }
 
  /**
   * LogOn() Grabs desired user from UserList an checks that the
   * provided password matches and they have access to role requested
   * before logonn in:
   * 			update userlist with one more login
   * 			setting currUser to desired user
   * 			setting role to desired role 
   */
  public boolean logon(String login, String pass,int role){
	  elecUser user = getUser( login );
	  if(!pass.equals(user.getPass())) return false;
	  else if (user.getBlock()){ return false; }
	  else if (!user.getRole()[role]) return false;
	  else if (role != 3 && user.getUse() > 0) return false;
	  else {
		  doUpdate ("UPDATE users SET use = '" + user.addUse() 
				  + "'" + "WHERE login = '" + login + "'");
		  currUser = user;
		  currRole = role;
		  return true;
	  }  
  }
  
  /**
   * Logoff() Removes a logon of the current user in the user list
   * and sets 
   * 		currUser to null 
   * 		currRole to  -1
   * 		to signify that no user is logged in
   */
  public void logoff(){
	  doUpdate("UPDATE users SET use = '" + currUser.removeUse() 
			  + "' WHERE login = '" + currUser.getLogin() + "'");
	  currUser = null;
	  currRole = -1;	  
  }
  
  /**
   * Updates user list item that has login = parameter login 
   * with all values stored in the elecUser user values
   */
  public void updateUser(elecUser user, String login){
	  String query;
	  query = "UPDATE users SET " +
			  "login = '" + user.getLogin() + "', " +
			  "password = '" + user.getPass() + "', " +
			  "f_name = '" + user.getFName() + "', " +
			  "l_name = '" + user.getLName() + "', " +
			  "blocked = '" + user.getBlock() + "', " +
			  "sa = '" + user.getRole()[0] + "', " +
			  "eo = '" + user.getRole()[1] + "', " +
			  "ro = '" + user.getRole()[2] + "', " +
			  "re = '" + user.getRole()[3] + "' " +
			  "WHERE login = '" + login + "'";
	  doUpdate(query);  
  }
  
  /**
   * Inserts newly created user into USER LIST
   */
  public void addUser(elecUser user){
	  String query;
	  query = "INSERT INTO users(login, f_name, l_name, " +
	  		  "password, blocked, sa, eo, re, ro, use)" +
	  		  " VALUES(" +
			  "'" + user.getLogin() + 	"', " +
			  "'" + user.getFName() + 	"', " +
			  "'" + user.getLName() + 	"', " +
			  "'" + user.getPass() + 	"', " +
			  "'" + user.getBlock() + 	"', " +
			  "'" + user.getRole()[0] + "', " +
			  "'" + user.getRole()[1] + "', " +
			  "'" + user.getRole()[2] + "', " +
			  "'" + user.getRole()[3] + "', " +
			  "'0')";
	  System.out.println(query);
	  doUpdate(query);
  }
  
  /**
   * Method if able the method returns a String array
   * representation of the user list.
   * 	first third of array is logins of users
   * 	2nd third of array is First Names of users
   * 	3rd third of array is Last Names of users
   */
  public String[] getUserList(){
	  String[] ret = null;
	  connect();
	  try{
		  resultSet = statement.executeQuery("SELECT COUNT(login) FROM users");
		  int i = resultSet.getInt(1);
		  resultSet = statement.executeQuery("SELECT login, l_name, f_name FROM users");
		  ret = new String[i*3];
		  resultSet.next();
		  for(int j = 0;j < i;j++){
			  ret[j] = resultSet.getString("login");
			  ret[j+1*i] = resultSet.getString("l_name");
			  ret[j+2*i] = resultSet.getString("f_name");
			  resultSet.next();
		  }
		  resultSet = null;
	  } catch(SQLException e){
		  e.printStackTrace();
	  }
	  	close();
  		return ret;
  }
  
  public void archive(File file) throws Exception{
		if(file.createNewFile()){
			File rep = new File(UserList);
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
  
  /**
   * A method to execute CREATE, DROP, and UPDATE queries.
   */
  public void doUpdate( String query ) {
     connect();
	  try {
      statement.executeUpdate( query );
    } catch (SQLException e) {
    	System.out.println(e.getMessage());
    	e.printStackTrace();
    }
	  close();
  }
}

 


