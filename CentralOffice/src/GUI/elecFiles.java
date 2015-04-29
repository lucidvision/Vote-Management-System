package GUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

import ManageRepository.RepositoryManagement;
import ManageUsers.UserManagement;

/**
 * @author Rogene Guinto
 */
@SuppressWarnings("serial")
public class elecFiles extends JFrame implements ActionListener {
	private int initialWidth = 600;
	private int initialHeight = 200;
	
	JButton importR, createI, back, logout; 
	Container content;
	
	private RepositoryManagement RepManager;
	private UserManagement UserManager;
	JFileChooser fc = new JFileChooser();
	
	public elecFiles( UserManagement UM, RepositoryManagement RM ) {
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
		info.add(new JLabel( "Central Office"),e);
		e.gridx = 0;
		info.add(new JLabel("location:\t"),e);
		e.gridy = 2;
		info.add(new JLabel("Status:\t"),e);
		e.gridx = 1;
		info.add(new JLabel(RepManager.getCurrStatusName()),e);
		return info;
	}
	
	/**
	 * getNav() returns a JPanel a with JButtons back, mainmenu and
	 * logout with ActionListeners attached to each.
	 */
	public JPanel getNav(){
		logout = new JButton("Log Out");
		JPanel nav = new JPanel();
		back = new JButton("Back");
		back.addActionListener(this);
		nav.add(logout,BorderLayout.EAST);
		nav.add(back, BorderLayout.WEST);
		logout.addActionListener(this);
		return nav;
	}
	
	private JPanel getMenu(){
		importR = new JButton("<html><center>Import Results<br>File</html>");
		createI = new JButton("<html><center>Create Riding<br> Info File </html>");
		importR.addActionListener(this);
		createI.addActionListener(this);
		JPanel menu = new JPanel();
		menu.add(createI, BorderLayout.WEST);
		menu.add(importR, BorderLayout.EAST);
		return menu;
	}
	
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == logout ){
			UserManager.logoff();
			new LoginWindow( UserManager , RepManager );
			this.dispose();
		} else if (e.getSource() ==  createI ){
			if( RepManager.getCurrStatus() == 0 || RepManager.getCurrStatus() == 3){
				new RideManage( RepManager, UserManager );
				this.dispose();
			} else {
				JOptionPane.showMessageDialog(this, 
						"Riding Info Files can only be created before an election or during a recount");
			}
		} else if(e.getSource() == importR){
			if(RepManager.getCurrStatus() != 0){
				if( fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION){
					try{
						RepManager.commitResults( fc.getSelectedFile() );
					}catch(Exception E){
						JOptionPane.showMessageDialog(this, E.getMessage());
					}
				} else {
					JOptionPane.showMessageDialog(this,
							"You did not select a valid file to import.");
				}
			} else {
				JOptionPane.showMessageDialog(this,
						"Results files cannot be imported before an election starts.");
			}
		
		} else if(e.getSource() == back){
			new EOMainMenu( UserManager, RepManager );
			this.dispose();
		}
	}

	
	
}
