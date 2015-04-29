package GUI;
import ManageUsers.*;
import initialize.Controller;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
/**
 * @author Bruce Yi
 */

@SuppressWarnings("serial")
public class LoginWindow extends JFrame implements ActionListener {
	private int initialWidth = 300;
	private int initialHeight = 120;
	private Controller ctlr;
	
	JButton submit; 
	JTextField  userName,password;
	String[] roles = { "System Administer","Electoral Official","Returning Officer","Reporter" };
	JComboBox roleSelection;
	public LoginWindow( Controller con ) {
		ctlr =con;
		
		//Initial the frame.
		
		this.setSize(initialWidth, initialHeight);
		this.setResizable(false);
		this.setTitle("Login");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		roleSelection = new JComboBox(roles);
		submit=new JButton("Submit");
		submit.addActionListener(this);
		
		userName = new JTextField(15);
		password = new JPasswordField(15);
		
		JPanel panel = new JPanel();
		panel.add(new JLabel("UserName:"),BorderLayout.WEST);
		panel.add(userName,BorderLayout.EAST);
		panel.add(new JLabel("Password:"),BorderLayout.WEST);
		panel.add(password,BorderLayout.EAST);
		panel.add(new JLabel("Select Role:"),BorderLayout.SOUTH);
		panel.add(roleSelection,BorderLayout.SOUTH);
		panel.add(submit,BorderLayout.SOUTH);
		add(panel,BorderLayout.CENTER);
		
		//ACTION

		
		Dimension scr = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(scr.width/2-this.getWidth()/2,scr.height/2-this.getHeight()/2);
		this.setVisible(true);
	}
	
	public void actionPerformed (ActionEvent e)
	{   
		int role = roleSelection.getSelectedIndex();
		if( ctlr.getUserMan().logon(userName.getText(), password.getText(),role) ) {
			if(role == 0)
				new SAMainMenu( ctlr ); 
			if(role == 1){
				new EOMainMenu( ctlr );
			}
			if(role == 2){
				new ROMainMenu( ctlr );
			}
			if(role == 3){
				JOptionPane.showMessageDialog(this, 
						"<html>Reporters do not have access to any functions at this location.<br>" +
						"Please select a different roll to login as.");
				new LoginWindow( ctlr );
			}
			this.dispose();
		} else {
			String[] err = {"message"};
			elecUser user = ctlr.getUserMan().getUser(userName.getText());
			if(user != null){
				if(!password.getText().equals(user.getPass())){
					String [] err1 = { "Unable to logon" , "Password did not match stored password" };
					err = err1;
				} else if(!user.getRole()[role]) {
					String [] err1 = { "Unable to logon" , "You do not have permission to access requested role." };
					err = err1;
				} else if(user.getUse() > 0){
					String [] err1 = { "Unable to logon" , "You are already logged on to another computer." };
					err = err1;
				} else if(user.getBlock()){
				String [] err1 = { "Unable to logon" , "You are Blocked" };
				err = err1;
			}
			} else {
				String [] err1 = { "Unable to logon" , "Logon not found." };
				err = err1;
			}
			JOptionPane.showMessageDialog(this,err);
		}
	}

}
