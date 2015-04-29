import GUI.*;
import ManageRepository.RepositoryManagement;
import ManageUsers.UserManagement;



class CentralOffice {
	  public static void main(String[] args) {
		    UserManagement UserManager = null;
		    String UserList = "dat/UserList.sqlite";
		    RepositoryManagement RepManager = null;
		    String RidingRep = "dat/RidingRep.sqlite";
		    UserManager = new UserManagement(UserList);
	    	RepManager = new RepositoryManagement(RidingRep);
	    	new LoginWindow( UserManager,RepManager );
	  }
	  

}
