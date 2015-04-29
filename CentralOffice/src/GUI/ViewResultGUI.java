package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import ManageRepository.RepositoryManagement;
import ManageUsers.UserManagement;
/**
 * @author Tomy Tao
 */
@SuppressWarnings("serial")
public class ViewResultGUI extends JFrame implements ActionListener {
	private int initialWidth = 500;
	private int initialHeight = 200;
	private JLabel label1,label2,label3;
	private JPanel panel1,panel2;
	private Container content;
	private JButton viewResult,logout,back,mainmenu;
	private UserManagement UserManager;
	private RepositoryManagement RepManager;
	public ViewResultGUI(UserManagement UM, RepositoryManagement RM) {
		// TODO Auto-generated constructor stub
		RepManager = RM;
		UserManager = UM;
		//Initial size of frame.
		this.setSize(initialWidth, initialHeight);
		this.setResizable(true);
		//Title bar name.
		this.setTitle("View Result");
		// default close operation
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		
		//content.setBackground(Color.lightGray);		
		// initialization of variables
		JPanel top = new JPanel(new BorderLayout());
		top.add(getInfo(),BorderLayout.WEST);
		top.add(getNav(),BorderLayout.EAST);
		//button

		viewResult = new JButton("View Result");
		
		
		// for panel 2
		panel2=new JPanel(new GridBagLayout());
		GridBagConstraints c2 = new GridBagConstraints();
		Border blackline = BorderFactory.createLineBorder(Color.black);
		panel2.setBorder(blackline);
		c2.insets = new Insets(20,20,20,20);
		c2.gridx = 0; c2.gridy =0; 
		panel2.add(viewResult,c2);
		
		//major pane
		content = getContentPane();
		content.add(top,BorderLayout.NORTH);
		content.add(panel2,BorderLayout.CENTER);
		
		viewResult.addActionListener(this);
		
		Dimension scr = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(scr.width/2-this.getWidth()/2,scr.height/2-this.getHeight()/2);
		this.setVisible(true);
		
	}
	
	/**
	 * getNav() returns a JPanel a with JButtons back, mainmenu and
	 * logout with ActionListeners attached to each.
	 */
	public JPanel getNav(){
		back = new JButton("Back");
		mainmenu = new JButton("Main Menu");
		logout = new JButton("Log Out");
		JPanel nav = new JPanel(new GridBagLayout());
		GridBagConstraints e = new GridBagConstraints();
		e.insets = new Insets (5, 5, 5, 5);
		e.fill = GridBagConstraints.HORIZONTAL;
		e.gridx = 0; e.gridy = 0;
		nav.add(back,e);
		e.gridx = 1;
		nav.add(mainmenu,e);
		e.gridy = 1;
		nav.add(logout,e);
		back.addActionListener(this);
		mainmenu.addActionListener(this);
		logout.addActionListener(this);
		return nav;
		
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
	
	@SuppressWarnings("deprecation")
	public void actionPerformed(ActionEvent a) {
		// TODO Auto-generated method stub
		
		
		if(a.getSource() == back){
			if( UserManager.currRole == 1){
				new EOMainMenu( UserManager , RepManager );
			} else{
				UserManager.logoff();
				new LoginWindow( UserManager ,  RepManager );
			}
			this.dispose();
		} else if (a.getSource() == mainmenu){
			if( UserManager.currRole == 1){
				new EOMainMenu( UserManager , RepManager );
			} else{
				UserManager.logoff();
				new LoginWindow( UserManager ,  RepManager );
			}
			this.dispose();
		} else if(a.getSource() == logout){
			UserManager.logoff();
			new LoginWindow( UserManager, RepManager);
			this.dispose();
		}
		else if(a.getSource() == viewResult)
		{
			if(RepManager.getCurrStatus() != 0 ){
				new ResultGUI( UserManager,RepManager,this );
			} else {
				JOptionPane.showMessageDialog(this, 
						"Results cannot be viewed before an election has started.");
			}
		}
	}

}
