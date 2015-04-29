package GUI;
import initialize.Controller;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

import ManageUsers.UserManagement;


@SuppressWarnings("serial")
public class elecFiles extends JFrame implements ActionListener {
	private int initialWidth = 600;
	private int initialHeight = 200;
	
	JButton importB, createB, createR, back, logout; 
	Container content;
	
	private Controller ctlr;
	private UserManagement UserManager;
	JFileChooser fc = new JFileChooser();
	
	public elecFiles( Controller con) {
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
		back = new JButton("Back");
		back.addActionListener(this);
		nav.add(logout,BorderLayout.EAST);
		nav.add(back, BorderLayout.WEST);
		logout.addActionListener(this);
		return nav;
	}
	
	private JPanel getMenu(){
		importB = new JButton("<html><center>Import Ballot<br> Listing File </html>");
		createB = new JButton("<html><center>Create Ballot<br> Listing File </html>");
		createR = new JButton("<html><center>Create Results<br>Listing File </html>");
		importB.addActionListener(this);
		createB.addActionListener(this);
		createR.addActionListener(this);
		JPanel menu = new JPanel();
		menu.add(createB, BorderLayout.WEST);
		menu.add(importB, BorderLayout.EAST);
		menu.add(createR, BorderLayout.SOUTH);
		return menu;
	}
	
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == logout ){
			UserManager.logoff();
			new LoginWindow( ctlr );
			this.dispose();
		} else if (e.getSource() ==  createB ){
			new SelectCompleted( ctlr );
			this.dispose();
		} else if(e.getSource() == createR){
			if(ctlr.getPollStat().getLocal().equals("Central Polling Station")){
				if(fc.showSaveDialog( this )==JFileChooser.APPROVE_OPTION){
					try{ 
						ctlr.getCentral().createResults( fc.getSelectedFile() );
					} catch( Exception a ){
						JOptionPane.showMessageDialog(this, a.getMessage());
					}
				} else {
					JOptionPane.showMessageDialog(this, "You did not select a valid type for Ballot Listing File.");
				}
			} 
		} else if(e.getSource() == importB ){
			System.out.println("HUh.");
			if(ctlr.getPollStat().getLocal().equals("Central Polling Station")){
				System.out.println( "yeah huh");
				if(fc.showOpenDialog( this )==JFileChooser.APPROVE_OPTION){
					try{
							ctlr.getCentral().importBallotFile( fc.getSelectedFile() );
					} catch(Exception err){
						JOptionPane.showMessageDialog(null,err.getMessage(),
								"Error", JOptionPane.ERROR_MESSAGE);
					}
				} else {
					JOptionPane.showMessageDialog(this, "You did not select a valid type for Ballot Listing File.");
				}
			}
		} else if(e.getSource() == back){
			new EOMainMenu( ctlr );
			this.dispose();
		}
	}

	
	
}
