package ballot.mangement;

import java.util.Arrays;


@SuppressWarnings("rawtypes")
public class ballot implements Comparable{
	private int ballotID;
	private String[] rankOfCandidates;
	private boolean spoiled;
	private double[] countedvote;
	
	public ballot(String[] rank, int ID){
		ballotID = ID; 
		rankOfCandidates = rank;
		spoiled = false;
	}
	
	public ballot(int ID){
		ballotID = ID; 
		spoiled = true;
	}
	
	public int getID(){
		return ballotID;
	}
	public String[] getRanking(){
		if(rankOfCandidates != null) return rankOfCandidates.clone();
		return null;
	}
	
	public boolean getSpoiled(){
		return spoiled;
	}

	public int compareTo(Object ob) {
		if(this.ballotID == ((ballot) ob).ballotID){
			if(this.spoiled == false && ((ballot) ob).spoiled == false){
				if(this.rankOfCandidates.length == ((ballot) ob).getRanking().length){
					for(int i = 0; i < this.rankOfCandidates.length;i++){
						if(!this.rankOfCandidates[i].equals(((ballot) ob).getRanking()[i])){
							return 0;
						}
					}
					return 1;
				} else { return 0; }
			} else if(this.spoiled == true && ((ballot) ob).spoiled == true){
				return 1;
			} else {
				return 0;
			}
		}
		return -1;
	}
	
	/**
	 * This third constructor is only used by central polling station for the purposes of calculating
	 * results. an array of how this ballots vote is being applied is added for this class and two methods
	 * transfer from eliminated transfer from elected have been added.
	 * @param rank candidates ranked in order of their ranking
	 * @param ID ballotID
	 * @param indicator any integer to indicate that we are selecting this constructor.
	 */
	public ballot(String[] rank, int ID, int indicator){
		ballotID = ID; 
		rankOfCandidates = rank;
		countedvote = new double[rankOfCandidates.length];
		Arrays.fill(countedvote,0);
		countedvote[0] = 1;
		spoiled = false;
	}
	
	public double countVoteFor( String candidate ){
		for(int i = 0; i < rankOfCandidates.length;i++){
			if(rankOfCandidates[i].equals( candidate )){
				return countedvote[i];
			}
		}
		return 0;
	}
	
	public void transferEliminated( String[] eliminated ){
		double transfer = 0;
		for(int i = 0; i < countedvote.length;i++){
			for(int j = 0; j < eliminated.length; j++){
				if(rankOfCandidates[i].equals( eliminated[j] ) && countedvote[i] != 0){
					transfer = countedvote[i];
					countedvote[i] = 0;
					if((i+1) < countedvote.length){
						countedvote[i+1] = countedvote[i+1] + transfer;
					}
				}
			}
		}
	}
	
	public void transferElected( String Elected , double rate){
		double transfer = 0;
		for(int i = 0; i < countedvote.length;i++){
			if(rankOfCandidates[i].equals( Elected ) && countedvote[i] != 0 &&
						(i+1) > countedvote.length){
				transfer = countedvote[i] * rate;
				countedvote[i] = countedvote[i] - transfer;
				countedvote[i+1] = countedvote[i+1] + transfer;
			}
		}
	}
}