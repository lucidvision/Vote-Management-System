package GUI;
import initialize.Controller;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayDeque;
import javax.swing.*;
import ballot.mangement.ballot;


@SuppressWarnings("serial")
public class ROMainMenu extends JFrame implements ActionListener {
	private int initialWidth = 600;
	private int initialHeight = 350;
	
	JButton enterPolls, logout; 	JList polls;
	Container content;
	
	private Controller ctlr;
	
	
	public ROMainMenu( Controller con ) {
		ctlr = con;
		
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(initialWidth, initialHeight);
		this.setTitle("Main Menu for Electoral Official");
		
		JPanel top = new JPanel();
		top.add(getInfo(),BorderLayout.WEST);
		top.add(getNav(),BorderLayout.EAST);
		
		content = getContentPane();
		content.add(top,BorderLayout.NORTH);
		content.add(getPoll(), BorderLayout.CENTER);
		content.add(getMenu(),BorderLayout.SOUTH);
		Dimension scr = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(scr.width/2-this.getWidth()/2,scr.height/2-this.getHeight()/2);
		this.setVisible(true);
	}
	
	/**
	 * getInfo() a JPanel that has basic info:
	 * 		currUser's real name
	 * 		Current location
	 * 		Current ElectionStatus
	 */
	private JPanel getInfo(){
		JPanel info = new JPanel(new GridBagLayout());
		GridBagConstraints e = new GridBagConstraints();
		e.fill = GridBagConstraints.HORIZONTAL;
		e.insets = new Insets(5, 5, 5, 5);
		e.gridx = 0; e.gridy = 0;
		info.add(new JLabel("Logged in User:\t",SwingConstants.LEFT), e);
		e.gridx = 1;
		info.add(new JLabel(ctlr.getUserMan().currUser.getLName() + ", " + ctlr.getUserMan().currUser.getFName()),e);
		e.gridy = 1;
		info.add(new JLabel(ctlr.getPollStat().getLocal()),e);
		e.gridx = 0;
		info.add(new JLabel("location:\t"),e);
		e.gridy = 2;
		return info;
	}
	
	/**
	 * getNav() returns a JPanel a with JButtons back, mainmenu and
	 * logout with ActionListeners attached to each.
	 */
	public JPanel getNav(){
		logout = new JButton("Log Out");
		JPanel nav = new JPanel();
		nav.add(logout,BorderLayout.EAST);
		logout.addActionListener(this);
		return nav;
	}
	
	private JPanel getPoll(){
		int[] poll = ctlr.getPollStat().getPollInfo();
		String[] pol = new String[poll.length/3];
		for(int i = 0; i< poll.length/3; i++){
			pol[i] = Integer.toString(poll[i]);
			
		}
		polls = new JList( pol );
		polls.setSelectedIndex(0);
		JScrollPane center = new JScrollPane();
		center.setPreferredSize(new Dimension(200,150));
		center.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		center.getViewport().add(polls);
		JPanel pollPan = new JPanel();
		pollPan.add(new JLabel("Polls Numbers: "),BorderLayout.NORTH);
		pollPan.add(center,BorderLayout.CENTER);
		return pollPan;
		
	}
	private JPanel getMenu(){
		enterPolls = new JButton("<html><center>Enter Ballots<br> for Selected Poll</html>");
		enterPolls.addActionListener(this);
		
		JPanel menu = new JPanel();
		menu.add(enterPolls,BorderLayout.SOUTH);
		return menu;
	}
	
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == logout ){
			ctlr.getUserMan().logoff();
			ctlr.begin();
			this.dispose();
		} else if (e.getSource() == enterPolls){
			try {
				int phase = ctlr.getBallMan().beginEntry( ctlr.getPollStat().getPollInfo()[polls.getSelectedIndex()] );
				System.out.println(phase);
				if( phase == 1 || phase == 2){
					new BallotEntry( ctlr , phase );
					this.dispose();
				} else if ( phase == 3 ){
					ArrayDeque<ballot> pass1 = ctlr.getBallMan().getPass( 1 );
					ArrayDeque<ballot> pass2 = ctlr.getBallMan().getPass( 2 );
					new BallotConfirmation( ctlr , pass1 , pass2 );
					this.dispose();
				} else if( phase > 3 ){
					JOptionPane.showMessageDialog(this , "All three phases of the ballot enrty" +
							" have already been completed.");
				}			
			} catch (Exception e1) {
				JOptionPane.showMessageDialog(this, e1.getMessage());
			}
		}
	}

	
	
}
