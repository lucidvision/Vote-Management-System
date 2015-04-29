package GUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.*;

import ManageRepository.*;
import ManageUsers.UserManagement;


public class SAMainMenu extends JFrame implements ActionListener {
	private int initialWidth = 600;
	private int initialHeight = 200;
	
	JButton users,status,archive,logout; 
	JPanel panel1,panel2, panel3;
	Container content;
	
	JFileChooser fc;
	
	private UserManagement UserManager;
	public RepositoryManagement RepManager;
	
	
	public SAMainMenu(UserManagement UM, RepositoryManagement RM) {
		RepManager = RM;
		UserManager = UM;
		
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(initialWidth, initialHeight);
		this.setTitle("Main Menu for System Administrator");
		
		
		
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
		if (RepManager.getCurrStatus()== 0)
		{
			info.add(new JLabel("Before Election"), e);
		}
		else if (RepManager.getCurrStatus()== 1)
		{
			info.add(new JLabel("During Election"), e);
		}
		else if (RepManager.getCurrStatus()== 2)
		{
			info.add(new JLabel("After Election"), e);
		}
		else
		{
			info.add(new JLabel("Recount"), e);
		}
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
		users = new JButton("Manage User List"); 
		users.addActionListener(this);
		status = new JButton("Manage Election Status");
		status.addActionListener(this);
		archive = new JButton("<html><center>Archive and Save<br>Election Data</html>");
		archive.addActionListener(this);
		JPanel menu = new JPanel(new GridLayout());
		GridBagConstraints e = new GridBagConstraints();
		e.fill = GridBagConstraints.BOTH;
		e.insets = new Insets (10,10,10,10);
		e.gridx = 0; e.gridy = 0;
		menu.add(users,e);
		e.gridx = 1;
		menu.add(status,e);
		e.gridx = 2;
		menu.add(archive,e);
		return menu;
	}
	
	@SuppressWarnings("deprecation")
	public void actionPerformed(ActionEvent a) {
		if(a.getSource() == logout ){
			UserManager.logoff();
			new LoginWindow( UserManager,RepManager );
			this.dispose();
		} else if (a.getSource() == users){
			new UserManage( UserManager , this);
			this.hide();	
		} else if (a.getSource() == archive){
			fc = new JFileChooser();
			if(fc.showSaveDialog(this)==JFileChooser.APPROVE_OPTION){
				try{
					File dir = fc.getSelectedFile();
					dir.mkdir();
					File rep = new File(dir.getAbsolutePath() + "/RidingRep.sqlite");
					RepManager.archive( rep );
					File use = new File(dir.getAbsolutePath() + "/UserList.sqlite");
					UserManager.archive( use );
				} catch(Exception e){
					e.printStackTrace();
					JOptionPane.showMessageDialog(this,e.getMessage());
				}
			} else {
				JOptionPane.showMessageDialog(this, "You Did not Select a valid location to save election data to.");
			}
			
		}else if (a.getSource() == status ){
			System.out.println(status);
			new StatusManagementGUI( UserManager , this);
			this.hide();
		}

	}

	
	
}
