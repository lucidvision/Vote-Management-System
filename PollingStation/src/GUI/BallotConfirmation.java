package GUI;
import initialize.Controller;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import ballot.mangement.*;

import java.util.ArrayDeque;
import javax.swing.*;
import polling.station.*;
/**
 * @author Tomy Tao
 */
@SuppressWarnings("serial")
public class BallotConfirmation extends JFrame implements ActionListener{
	Controller ctlr;
	ballotmanagement ballman;
	pollingstation PollStat;
	
	private int initialWidth = 800;
	private int initialHeight = 580;
	Container content;
	
	JButton neither, delete;		JCheckBox[] spoiled;
	JTextField ballotID;			JComboBox[] cans;
									JButton[] correct;
	
	String[] can_party = null;	int num_cans;	
	String[] ranks;
	
	ArrayDeque<ballot> pass1;	ArrayDeque<ballot> pass2;
	ArrayDeque<ballot> ballotDeque;
	
	public BallotConfirmation( Controller con , ArrayDeque<ballot> p1, ArrayDeque<ballot> p2){
		ctlr = con;
		ballman = ctlr.getBallMan();
		PollStat = ctlr.getPollStat();
		
		ballotDeque = new ArrayDeque<ballot>();
		pass1 = p1;	pass2 = p2;
		
		
		this.setSize(initialWidth, initialHeight);
		this.setTitle("Ballot Entry");
		

		content = getContentPane();
		content.add(getTop(),BorderLayout.NORTH);
		content.add(getBallotBox(),BorderLayout.CENTER);
		content.add(getButtons(),BorderLayout.SOUTH);
		Dimension scr = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(scr.width/2-this.getWidth()/2,scr.height/2-this.getHeight()/2);
		this.setVisible(true);
		stepthrough();
	}

	private void stepthrough() {
		ballot one = null ,two = null;
		if( pass1.isEmpty() && pass2.isEmpty() ){
			JOptionPane.showMessageDialog( this ,
					"Submitted " + ballotDeque.size() + " ballots for final entry.");
			ballman.submit(ballotDeque , 3 );
			new ROMainMenu( ctlr );
			this.dispose();
		} else {
			boolean displayed = false;
			if(pass1.size() >= pass2.size()){
				one = pass1.getFirst();
				for(int i = 0; i < pass2.size(); i++){
					two = pass2.removeFirst();
					int j = one.compareTo(two);
					if(j == 1){
						ballotDeque.add( pass1.removeFirst() );
						displayed = true;
						JOptionPane.showMessageDialog(this, "Ballot " + one.getID() + " matched and has been added.");
						stepthrough();
						break;
					} else if (j == 0){
						toDisplay( one , two );
						displayed = true;
						pass2.addFirst( two );
						correct[0].setEnabled( true );
						correct[1].setEnabled( true );
						break;
					} else {
						pass2.addLast( two );
					}
				}
				if(!displayed){
					toDisplay( one , 1 );
					correct[0].setEnabled( true );
					correct[1].setEnabled( false );
				}
			} else {
				two = pass2.getFirst();
				for(int i = 0; i < pass1.size(); i++){
					one = pass1.removeFirst();
					int j = two.compareTo( pass2.removeFirst() );
					if(j == 1){
						ballotDeque.add( two );
						JOptionPane.showMessageDialog(this, "Ballot " + one.getID() + " matched and has been added.");
						displayed = true;
						stepthrough();
						break;
					} else if (j == 0){
						toDisplay( one , two );
						displayed = true;
						pass1.addFirst( two );
						correct[0].setEnabled( true );
						correct[1].setEnabled( true );
						break;
					} else {
						pass1.addLast( one );
					}
				}
				if(!displayed){
					toDisplay( two , 2 );
					correct[0].setEnabled( false );
					correct[1].setEnabled( true );
				}
			}
		}
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
		
		top.add(new JLabel("<html><center>Please Select which of these two entries is correct for this ballot id"+
				"<br>If neither is please select enter your own ballot.<br>" +
				"If the ID is incorrect please select delete ballot.</html>"),BorderLayout.NORTH);
		top.add(id,BorderLayout.SOUTH);
		
		
		top.setPreferredSize(new Dimension(450,80));
		return top;
	}
	
	private JPanel getBallotBox(){
		can_party = PollStat.getCanInfo();
		num_cans = PollStat.getNumCan();
		
		JPanel Ballot = new JPanel(new GridBagLayout());
		Ballot.setPreferredSize(new Dimension(this.getWidth(),500));
		Ballot.setBorder(BorderFactory.createEtchedBorder());
		GridBagConstraints e = new GridBagConstraints();
		e.insets = new Insets(5,5,5,5);
		JPanel[] Ballots = new JPanel[2];
		spoiled = new JCheckBox[2];
		correct = new JButton[2];
		
		getComboBoxes(num_cans*2);
		JScrollPane[] Candidates = getCandidatePane();
		
		for(int i = 0; i < Ballots.length;i++){
			Ballots[i] = new JPanel();
			Ballots[i].setPreferredSize(new Dimension(370,380));
			
			Ballots[i].add(new JLabel("Phase " + (i+1) + " Ballot Entry")
					,BorderLayout.NORTH);
			Ballots[i].add(Candidates[i],BorderLayout.CENTER);
			Ballots[i].setBorder(BorderFactory.createEtchedBorder());
			
			JPanel bottom = new JPanel();
			spoiled[i] = new JCheckBox("Spoiled Ballot");
			spoiled[i].setEnabled(false);
			bottom.add(spoiled[i],BorderLayout.WEST);
			correct[i] = new JButton("Correct Ballot");
			correct[i].addActionListener(this);
			bottom.add(correct[i],BorderLayout.EAST);
			
			Ballots[i].add(bottom,BorderLayout.SOUTH);
			e.gridx = i; e.gridy = 0;
			Ballot.add(Ballots[i],e);
		}
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
			cans[i].setEditable(false);
		}
	}
	
	private JScrollPane[] getCandidatePane(){
		JScrollPane[] Candidates = new JScrollPane[2];
		JPanel[] candidateboxes = new JPanel[num_cans*2];
		for(int j = 0; j < 2 ; j++){
			Candidates[j] = new JScrollPane();
			Candidates[j].setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
			Candidates[j].setPreferredSize(new Dimension(350,300));
			for(int i = 0; i < num_cans; i++){
				candidateboxes[i+j*num_cans] = new JPanel(new GridBagLayout());
				candidateboxes[i+j*num_cans].setBorder(BorderFactory.createLineBorder(Color.black));
				GridBagConstraints d = new GridBagConstraints();
				d.fill = GridBagConstraints.HORIZONTAL;
				d.insets = new Insets(1, 1, 1, 1);
				d.fill = GridBagConstraints.VERTICAL;
				d.gridy = 0;
				candidateboxes[i+j*num_cans].add(new JLabel(can_party[i]),d);
				d.gridy++;
				candidateboxes[i+j*num_cans].add(new JLabel(can_party[i+num_cans]),d);
				d.gridy++;
				candidateboxes[i+j*num_cans].add(cans[i+num_cans*j],d);
			}
			JPanel Ballot = new JPanel(new GridBagLayout());
			GridBagConstraints e = new GridBagConstraints();
			e.insets = new Insets(5, 5, 5, 5);
			e.fill = GridBagConstraints.HORIZONTAL;
			for(int i = 0; i < num_cans; i++){
				e.gridx = i % 3; e.gridy = i / 3;
				Ballot.add(candidateboxes[i+j*num_cans],e);
			}
			Candidates[j].getViewport().add(Ballot);
		}
		return Candidates;
	}
	
	private JPanel getButtons(){
		neither = new JButton("<html><center>Enter Correct<br>Ballot Information</html>");	
		neither.addActionListener(this);
		delete = new JButton("<html><center>Delete Incorrect<br>Ballot ID</html>");	
		delete.addActionListener(this);
		
		JPanel buttons = new JPanel(new GridLayout());
		GridBagConstraints e = new GridBagConstraints();
		e.gridx = 0;	e.gridy = 0;	buttons.add(neither,e);
		e.gridx++;	buttons.add(delete,e);
		
		return buttons;
	}

	
	@Override
	public void actionPerformed(ActionEvent a){
		if(a.getSource() == correct[0]){
			ballotDeque.add( pass1.removeFirst());
			if(correct[1].isEnabled()){
				pass2.removeFirst();
			}
		}else if (a.getSource() == correct[1]){
			ballotDeque.add(pass2.removeFirst());
			if(correct[0].isEnabled()){
				pass1.removeFirst();
			}
		} else if( a.getSource() == neither ){
			new BallotEntry3( ballotDeque , can_party );
		} else if( a.getSource() == delete ){
			if( correct[0].isEnabled() ) pass1.removeFirst();
			if( correct[1].isEnabled() ) pass2.removeFirst();
		}
		
		
		clearBallot();
		stepthrough();
		
	}
	
	private void toDisplay(ballot one,ballot two){
		ballotID.setText( Integer.toString(one.getID()));
		if(!one.getSpoiled()){
			String[] rank1 = one.getRanking();
			spoiled[0].setSelected(false);
			for(int i = 0; i < num_cans;i++){
				for(int j = 0;j < rank1.length;j++){
					if(can_party[i].equals(rank1[j])){
						cans[i].setSelectedIndex(j+1);
					}
				}
			}
		} else {
			spoiled[0].setSelected(true);
		}
		if(!two.getSpoiled()){
			String[] rank2 = two.getRanking();
			spoiled[1].setSelected(false);
			for(int i = 0; i < num_cans;i++){
				for(int j = 0;j < rank2.length;j++){
					if(can_party[i].equals(rank2[j])){
						cans[i+num_cans].setSelectedIndex(j+1);
					}
				}
			}
		} else {
			spoiled[1].setSelected(true);
		}
	}
	
	private void toDisplay( ballot disp, int phase ){
		ballotID.setText(Integer.toString(disp.getID()));
		if( disp.getSpoiled() ){
			spoiled[phase - 1].setSelected( true );
		} else {
			String[] rank = disp.getRanking();
			spoiled[phase - 1].setSelected( false );
			for(int i = 0; i < num_cans;i++){
				for(int j = 0;j < rank.length;j++){
					if(can_party[i+num_cans*( phase -1 )].equals(rank[j])){
						cans[i+num_cans*( phase -1 )].setSelectedIndex(j+1);
					}
				}
			}
		}
		
	}

	private void clearBallot(){
		ballotID.setText("");
		spoiled[0].setSelected(false);
		spoiled[1].setSelected(false);
		for(int i = 0; i < cans.length;i++){
			cans[i].setSelectedIndex(0);
		}
	}
}
