package GUI;
import ManageRepository.RepositoryManagement;
import ManageUsers.*;
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
	private UserManagement UserManager;
	private RepositoryManagement RepManager;
	
	JButton submit; 
	JPanel panel;
	JLabel label1,label2,label3;
	JTextField  userName,password;
	String roles[] = { "System Administer","Electoral Official","Returning Officer","Reporter" };
	JComboBox roleSelection;
	public LoginWindow(UserManagement UM, RepositoryManagement RM) {
		UserManager = UM;
		RepManager = RM;
		
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
		
		panel=new JPanel();
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
		if(UserManager.logon(userName.getText(),password.getText(),role)) {
			if(role == 0)
				new SAMainMenu( UserManager, RepManager ); 
			if(role == 1){
				new EOMainMenu( UserManager, RepManager);
			}
			if(role == 2){
				//new ROMainMenu(UserManager , RepManager);
				new LoginWindow( UserManager, RepManager );
				JOptionPane.showMessageDialog(this, 
						"<html>Returning Officer have no access to any functions at the central office.<br>" +
						"If you have permission please select a different role to log on as.</html>");
				UserManager.logoff();
			}
			if(role == 3){
				new ViewResultGUI( UserManager , RepManager );
			}
			this.dispose();
		} else {
			String[] err = {"message"};
			elecUser user = UserManager.getUser(userName.getText());
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
			new Error(err);
		}
	}

}
