package ManageRepository;

public class riding {
	private String rideId = "";
	private String rideName = "";
	private String dateCr = "";
	private String map = null;
	private int numPolls = 0;
	private int numSeat = 0;
	private int numIncum = 0;
	private int numVotes = 0;
	private int numCandidate = 0;
	private String[] candiAff = null;
	private String[] candiName = null;
	private String[] incumbents = null;
	private String[] incumbAff = null;
	private String[] customKeys = null;
	private int[] polls;

	public riding(){}
			
	public String getRidingName(){
		return rideName;
	}
	public void setRidingName(String name){
		rideName = name;
	}
	public String getRideId(){
		return rideId;
	}
	public void setRideId(String id){
		rideId = id;
	}
	public String getDateCr(){
		return dateCr;
	}
	public void setDate(String cr){
		dateCr = cr;
	}
	public String getMap(){
		return map;
	}
	public void setMap(String m){
		map = m;
	}
	public int getNumPolls(){
		return numPolls;
	}
	public void setNumPolls(int p){
		numPolls = p;
	}
	public int getNumSeats(){
		return numSeat;
	}
	public void setNumSeats(int s){
		numSeat = s;
	}
	public int getNumIncum(){
		return numIncum;
	}
	public void setNumIncum(int in){
		numIncum = in;
	}
	public int getNumVote(){
		return numVotes;
	}
	public void setNumVote(int vote){
		numVotes = vote;
	}
	public int getNumCandi(){
		return numCandidate;
	}
	public void setNumCandi(int c){
		numCandidate = c;
	}
	public String[] getCandiAff(){
		if(candiAff != null){return candiAff.clone();}
		return  null;
	}
	public void setCandiAff(String[] Aff){
		candiAff = Aff.clone();
	}
	public String[] getCandiName(){
		return candiName;
	}
	public void setCandiName(String[] c){
		candiName = c;
	}
	public String[] getIncumbets(){
		if(incumbents != null){	return incumbents.clone();}
		else{return null;}
	}
	public void setIncumbets(String[] In){
		incumbents = In.clone();
	}
	public void setIncumAff(String[] in){
		incumbAff = in.clone();
	}
	public String[] getIncumbAff(){
		if(incumbAff != null){return incumbAff.clone();}
		else{return null;}
	}
	public void setPolls(int[] in){
		polls = in.clone();
	}
	public int[] getPolls(){
		if(polls != null){return polls.clone();}
		else{return null;}
	}
	
	public String[] getCustomKeys(){
		return customKeys;
	}
	public void setCustomKeys(String[] k){
		customKeys = k;
	}

}
