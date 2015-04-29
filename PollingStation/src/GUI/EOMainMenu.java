package GUI;
import initialize.Controller;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import ManageUsers.UserManagement;


@SuppressWarnings("serial")
public class EOMainMenu extends JFrame implements ActionListener {
	private int initialWidth = 600;
	private int initialHeight = 200;
	
	JButton viewresults, electionFiles, logout; 
	Container content;
	
	private Controller ctlr;
	private UserManagement UserManager;
	
	public EOMainMenu( Controller con) {
		ctlr = con;
		UserManager = ctlr.getUserMan();
		
		
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
		info.add(new JLabel( ctlr.getPollStat().getLocal()),e);
		e.gridx = 0;
		info.add(new JLabel("location:\t"),e);
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
		viewresults = new JButton("<html><center>View  Results<br>For This Riding</html>");
		viewresults.addActionListener(this);
		electionFiles = new JButton("<html><center>Manage <br> Election Files</html>");
		electionFiles.addActionListener(this);
		JPanel menu = new JPanel(new GridLayout());
		GridBagConstraints e = new GridBagConstraints();
		e.fill = GridBagConstraints.BOTH;
		e.insets = new Insets (10,10,10,10);
		e.gridx = 1;
		menu.add(viewresults,e);
		e.gridx = 2;
		menu.add(electionFiles,e);
		return menu;
	}
	
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == logout ){
			UserManager.logoff();
			new LoginWindow( ctlr );
			this.dispose();
		} else if (e.getSource() ==  electionFiles ){
			new elecFiles( ctlr );
			this.dispose();	
		} else if( e.getSource() == viewresults){
			new ShowResults( ctlr );
			this.dispose();
		}
		
	}
	
}
