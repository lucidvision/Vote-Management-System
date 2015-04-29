package GUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import ballot.mangement.*;
import java.util.ArrayDeque;
import javax.swing.*;
/**
 * @author Tomy Tao
 */
@SuppressWarnings("serial")
public class BallotEntry3 extends JFrame implements ActionListener{
	private int initialWidth = 450;
	private int initialHeight = 530;
	Container content;
	JButton submit;		JCheckBox spoiled;
	JTextField ballotID;		JComboBox[] cans;
	
	String[] can_party = null;	int num_cans;	
	String[] ranks;	
	
	ArrayDeque<ballot> ballotDeque;	
	ArrayDeque<Integer> ids;
	
	public BallotEntry3( ArrayDeque<ballot> ballots, String[] canpart) {
		can_party = canpart;
		ballotDeque = ballots;
		ids = new ArrayDeque<Integer>();
		
		this.setSize(initialWidth, initialHeight);
		this.setTitle("Ballot Entry");
		

		content = getContentPane();
		content.add(getTop(),BorderLayout.NORTH);
		content.add(getBallotBox(),BorderLayout.CENTER);
		content.add(getButtons(),BorderLayout.SOUTH);
		Dimension scr = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(scr.width/2-this.getWidth()/2,scr.height/2-this.getHeight()/2);
		this.setVisible(true);
	}
	

	private JPanel getTop(){
		JPanel top = new JPanel();
		JPanel id = new JPanel(new GridLayout());
		GridBagConstraints e = new GridBagConstraints();
		e.insets = new Insets(5, 5, 5, 5);
		e.ipadx = 100;
		e.gridx = 0; e.gridy = 0;
		id.add(new JLabel("Enter Ballot ID: "),e);
		e.gridx++;
		ballotID = new JTextField();
		id.add(ballotID,e);
		
		top.add(new JLabel("<html><left>Please enter the correct ballot information<br> "+
				"and press submit.</html>"),BorderLayout.CENTER);
		top.add(id,BorderLayout.SOUTH);
		
		
		top.setPreferredSize(new Dimension(450,80));
		return top;
	}
	
	private JPanel getBallotBox(){
		JPanel Ballot = new JPanel();
		num_cans = can_party.length/2;
		Ballot.add(new JLabel(
				"<html><center>Please enter the information from the ballot.<br>" 
				+"If ballot was incorrectly filled out<br>"
				+ "please indicate that it is spoiled.</html<")
				,BorderLayout.NORTH);
		
		getComboBoxes(num_cans);
		Ballot.add(getCandidatePane(),BorderLayout.CENTER);
		Ballot.setBorder(BorderFactory.createEtchedBorder());
		spoiled = new JCheckBox("Spoiled Ballot");
		Ballot.add(spoiled,BorderLayout.SOUTH);
		return Ballot;
	}
	
	private void getComboBoxes(int j){
		ranks = new String[num_cans+1];
		ranks[0] = "Unranked";
		for(int i = 0; i < num_cans;i++){
			ranks[i+1]=Integer.toString(i+1);
		}
		cans = new JComboBox[j];
		for(int i = 0; i < j; i++){
			cans[i] = new JComboBox(ranks);
		}
	}
	
	private JScrollPane getCandidatePane(){
		JScrollPane Candidates = new JScrollPane();
		Candidates.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		Candidates.setPreferredSize(new Dimension(350,300));
		JPanel[] candidateboxes = new JPanel[num_cans];
		for(int i = 0; i < num_cans; i++){
			candidateboxes[i] = new JPanel(new GridBagLayout());
			candidateboxes[i].setBorder(BorderFactory.createLineBorder(Color.black));
			GridBagConstraints d = new GridBagConstraints();
			d.fill = GridBagConstraints.HORIZONTAL;
			d.insets = new Insets(1, 1, 1, 1);
			d.fill = GridBagConstraints.VERTICAL;
			d.gridy = 0;
			candidateboxes[i].add(new JLabel(can_party[i]),d);
			d.gridy++;
			candidateboxes[i].add(new JLabel(can_party[i+num_cans]),d);
			d.gridy++;
			candidateboxes[i].add(cans[i],d);
		}
		JPanel Ballot = new JPanel(new GridBagLayout());
		GridBagConstraints e = new GridBagConstraints();
		e.insets = new Insets(5, 5, 5, 5);
		e.fill = GridBagConstraints.HORIZONTAL;
		for(int i = 0; i < num_cans; i++){
			e.gridx = i % 3; e.gridy = i / 3;
			Ballot.add(candidateboxes[i],e);
		}
		Candidates.getViewport().add(Ballot);
		return Candidates;
	}
	
	private JPanel getButtons(){
		submit = new JButton("Submit Ballot");	submit.addActionListener(this);
		
		JPanel buttons = new JPanel();

		buttons.add(submit);
		
		return buttons;
	}

	
	@Override
	public void actionPerformed(ActionEvent a) {
		if(toDeque()){this.dispose();}	
	}
	

	private boolean toDeque(){
		try{
			int id = Integer.parseInt(ballotID.getText());
			if(ids.contains(id)){
				JOptionPane.showMessageDialog(this,
						"You have already used this ballot id.");
				return false;
			}
			if(spoiled.isSelected()){
				ids.add(id);
				return ballotDeque.add(new ballot(id));
			} else {
				String[]  CaninRank = getRanks();
				if(CaninRank != null){
					ids.add(id);
					return ballotDeque.add(new ballot(CaninRank,id));
				}
			}
				
		} catch(NumberFormatException e){
			String err = "<html>Invalid Ballot Id number.<br>"
						+	"please click ok and reenter a valid<br>" 
						+	"number before clicking submit.";
			JOptionPane.showMessageDialog(null, err);
		}
		return false;
	}
	
	private String[] getRanks(){
		String[] CaninRank = new String[num_cans];
		int[] ranks = new int[num_cans];
		int cursor = 0;
		for(int i = 0;i < num_cans;i++){
			int rank = cans[i].getSelectedIndex();
			if(rank != 0){
				ranks[cursor] = rank;
				CaninRank[cursor] = can_party[i];
				for(int j = cursor; j > 0;j--){
					if(ranks[j] < ranks[j-1]){
						int itemp = ranks[j];
						String temp = CaninRank[j];
						ranks[j] = ranks[j-1];
						CaninRank[j] = CaninRank[j-1];
						ranks[j-1] = itemp;
						CaninRank[j-1] = temp;
					} else if(ranks[j] == ranks[j-1]){
						String err = "<html>Invalid ranking.<br>"
								+ "At least two rankings are the same<br>" 
								+ "Please fix this before clicking submit again.</html>";
						JOptionPane.showMessageDialog(this, err);
						return null;
					}
				}
				cursor++;
			}
		}
		if(cursor <= 0){
			String err = "<html>Invalid ranking.<br>"
					+ "At least one rankings is required<br>" 
					+ "Please fix this before clicking submit again.</html>";
			JOptionPane.showMessageDialog(this,err);
			return null;
		}
		String[] ranky = new String[cursor];
		for(int i = 0; i < cursor;i++){
			ranky[i] = CaninRank[i];
		}
		return ranky;
	}
}
