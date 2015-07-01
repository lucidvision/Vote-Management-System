package GUI;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import ManageUsers.UserManagement;
import StatusManager.ElectionStatusManagement;

@SuppressWarnings("serial")
public class StatusManagementGUI extends JFrame implements ActionListener{
	private UserManagement UserManager;
	private int InitialWidth  = 580;
	private int IntitalHeight = 300;
	
	Container content;
	JButton toNext, toOption, back, logout, mainmenu;
	SAMainMenu SA;
	int status,optional;
	public StatusManagementGUI(UserManagement  UM, SAMainMenu last) {
		// TODO Auto-generated constructor stub
		UserManager = UM;
		SA = last;
		
		
		//SA.RepManager
		JPanel top = new JPanel(new BorderLayout());
		top.add(getInfo(),BorderLayout.WEST);
		top.add(getNav(),BorderLayout.EAST);
		
		// for the south panel
		JPanel south = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets (10, 10, 10, 10);
		c.fill = GridBagConstraints.HORIZONTAL;
		toNext = new JButton("Advance to Next Status");
		toNext.addActionListener(this);
		toOption = new JButton("Advance to Optional Status");
		toOption.addActionListener(this);
		status = SA.RepManager.getCurrStatus();
		optional = -1;
		c.gridx = 1; c.gridy =0; 
		int temp = (status+1);
		if (temp == 3)
		{
			south.add(new JLabel("Next Status is: Before Election"), c);
		}
		else if (temp == 1)
		{
			south.add(new JLabel("Next Status is: During Election"), c);
		}
		else if (temp == 2)
		{
			south.add(new JLabel("Next Status is: After Election"), c);
		}
		else if (temp == 4)
		{
			south.add(new JLabel("Next Status is: After Election"), c);
		}
		else
		{
			south.add(new JLabel("Next Status is: Recount"), c);
		}
		c.gridx = 1; c.gridy =1; 
		if (status == 2 && SA.RepManager.checkIfRecount() )
		{
			optional = 3;
			south.add(new JLabel("Optional Status is: Recount status"), c);
		}
		else{
			south.add(new JLabel("Optional Status is: null "), c);
		}
		c.gridx = 0; c.gridy =2; 
		south.add(toNext,c);
		c.gridx = 2; c.gridy =2; 
		south.add(toOption,c);
		
		
		
		content = getContentPane();
		content.add(south,BorderLayout.SOUTH);
		content.add(top,BorderLayout.NORTH);
		
		this.setSize(InitialWidth, IntitalHeight);
		this.setTitle("Status Manager");
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
		if (SA.RepManager.getCurrStatus()== 0)
		{
			info.add(new JLabel("Before Election"), e);
		}
		else if (SA.RepManager.getCurrStatus()== 1)
		{
			info.add(new JLabel("During Election"), e);
		}
		else if (SA.RepManager.getCurrStatus()== 2)
		{
			info.add(new JLabel("After Election"), e);
		}
		else
		{
			info.add(new JLabel("Recount"), e);
		}
		return info;
	}

	@Override
	public void actionPerformed(ActionEvent a) {
		ElectionStatusManagement manager = new ElectionStatusManagement("dat/RidingRep.sqlite");
		if(a.getSource() == back){
			SA.show();
			this.dispose();
		} else if (a.getSource() == mainmenu){
			SA.show();
			this.dispose();
		} else if(a.getSource() == logout){
			UserManager.logoff();
			new LoginWindow( UserManager, SA.RepManager);
			SA.dispose();
			this.dispose();
		} else if (a.getSource() == toNext){
			status = manager.AdvanceToNextStatus(status);
			JOptionPane.showMessageDialog(null,"Status changed!",
					"Congradulations!", JOptionPane.WARNING_MESSAGE);

			this.dispose();
			new SAMainMenu( UserManager, SA.RepManager);
			
		} else if (a.getSource() == toOption){
			if (optional == 3)
			{
				manager.AdvanceToOptionalStatus(status);
				JOptionPane.showMessageDialog(null,"Status changed!",
						"Congradulations!", JOptionPane.WARNING_MESSAGE);
				this.dispose();
				new SAMainMenu( UserManager, SA.RepManager);
			}
			else
			{
				JOptionPane.showMessageDialog(null,"No optional Status!",
						"Congradulations!", JOptionPane.ERROR_MESSAGE);
			}
		}

	}

}
