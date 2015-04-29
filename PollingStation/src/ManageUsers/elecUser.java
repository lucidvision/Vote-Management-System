package ManageUsers;
/**
 * @author Bruce Yi
 */
public class elecUser {
		String login = null;
		String pass = null;
		String FName = null;
		String LName = null;
		boolean ifBlock = false;
		boolean role[] = new boolean[4];
		int inUse = 0;
		
	public elecUser(){}
	
	public void setLogin(String newLogin){
		login = newLogin;
	}
	
	public String getLogin(){
		return(login);
	}
	
	public void setPass(String newPass){
		pass = newPass;
	}
	
	public String getPass(){
		return(pass);
	}
	
	public void setFName(String newName){
		FName = newName;
	}
	
	public String getFName(){
		return(FName);
	}
	
	public void setLName(String newName){
		LName = newName;
	}
	
	public String getLName(){
		return(LName);
	}
	
	public void setRole(boolean newRole[]){
		for(int i = 0; i <= 3; i++){
			role[i] = newRole[i];
		}
	}
	
	public boolean[] getRole(){
		return(role);
	}
	
	public void setBlock(boolean block){
		ifBlock = block;
	}
	
	public boolean getBlock(){
		return(ifBlock);
	}

	public void setUse(int uses) {
		inUse = uses;
	}
	
	public int getUse(){
		return inUse;
	}
	
	public int addUse(){
		inUse = inUse + 1;
		return inUse;
	}
	
	public int removeUse(){
		if(inUse > 0) inUse = inUse - 1;
		return inUse;
	}
	

}