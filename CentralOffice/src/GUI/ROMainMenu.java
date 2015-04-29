package GUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import ManageRepository.*;
import ManageUsers.UserManagement;


@SuppressWarnings("serial")
public class ROMainMenu extends JFrame implements ActionListener {
	private int initialWidth = 600;
	private int initialHeight = 200;
	
	JButton rideInfo,enterPolls, electionFiles, logout; 
	Container content;
	private UserManagement UserManager;
	private RepositoryManagement RepManager;
	
	
	public ROMainMenu(UserManagement UM, RepositoryManagement RM) {
		RepManager = RM;
		UserManager = UM;
		
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(initialWidth, initialHeight);
		this.setTitle("Main Menu for Electoral Official");
		
		JPanel top = new JPanel();
		top.add(getInfo(),BorderLayout.WEST);
		top.add(getNav(),BorderLayout.EAST);
		
		content = getContentPane();
		content.add(top,BorderLayout.NORTH);
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
		info.add(new JLabel(UserManager.currUser.getLName() + ", " + UserManager.currUser.getFName()),e);
		e.gridy = 1;
		info.add(new JLabel("Central Office"),e);
		e.gridx = 0;
		info.add(new JLabel("location:\t"),e);
		e.gridy = 2;
		info.add(new JLabel("Election Status:\t"), e);
		e.gridx = 1;
		info.add(new JLabel(RepManager.getCurrStatusName()), e);
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
	
	private JPanel getMenu(){
		rideInfo = new JButton("<html><center>Create<br>Riding Info Files</html>"); 
		rideInfo.addActionListener(this);
		enterPolls = new JButton("<html><center>Enter Ballots</html>");
		electionFiles = new JButton("<html><center>Manage <br> Election Files</html>");
		JPanel menu = new JPanel(new GridLayout());
		GridBagConstraints e = new GridBagConstraints();
		e.fill = GridBagConstraints.BOTH;
		e.insets = new Insets (10,10,10,10);
		e.gridx = 0; e.gridy = 0;
		menu.add(rideInfo,e);
		e.gridx = 1;
		menu.add(enterPolls,e);
		return menu;
	}
	
	

	@SuppressWarnings("deprecation")
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == logout ){
			UserManager.logoff();
			new LoginWindow( UserManager,RepManager );
			this.dispose();
		} else if (e.getSource() == rideInfo){
			//new RideManage( RepManager, UserManager , this);
			this.hide();
			
		}
		
	}

	
	
}
