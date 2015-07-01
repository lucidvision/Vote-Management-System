package GUI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.Border;
import ManageUsers.UserManagement;
import ManageRepository.RepositoryManagement;

public class ROGUI extends JFrame implements ActionListener{
	private int initialWidth = 500;
	private int initialHeight = 400;
	private JLabel label1,label2,label3,label4;
	private JTextField  input;
	private JPanel panel1,panel2,panel3;
	private Container content;
	private JButton submit,logout, createRindingInforFile; 
	private UserManagement UserManager;
	private RepositoryManagement RepManager;
	
	public ROGUI(UserManagement UM, RepositoryManagement RM) {
		RepManager = RM;
		UserManager = UM;
		//Initial size of frame.
		this.setLocationRelativeTo(null);
		this.setSize(initialWidth, initialHeight);
		this.setResizable(true);
		//Title bar name.
		this.setTitle("Returnig Officer");
		// default close operation
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		//major pane
		content = getContentPane();
		//content.setBackground(Color.lightGray);		
		// initialization of variables
		label1 = new JLabel();
		label1.setText("Returnig Officer: " + UserManager.currUser.getLName() + ", " + UserManager.currUser.getFName());
		label2 = new JLabel();
		label2.setText("Status: " + 0);
		label3 = new JLabel();
		label3.setText("Location: Central Office");
		
		// for panel 
		label4 = new JLabel();
		label4.setText("Enter Poll Number:");
		input =new JTextField(15);
		input.setText("To be implemented");
		submit=new JButton("Submit");
		
		logout=new JButton("Logout");
		createRindingInforFile = new JButton("<html>Create Rinding <p>Information File </html>");
		//for left panel 1 
		panel1 = new JPanel(new GridBagLayout());
		GridBagConstraints c1 = new GridBagConstraints();
		Border blackline = BorderFactory.createLineBorder(Color.black);
		panel1.setBorder(blackline);
		c1.gridx = 0; c1.gridy =0; 
		panel1.add(label1,c1);
		c1.gridx = 0; c1.gridy =1;
		panel1.add(label2,c1);
		c1.gridx = 0; c1.gridy =3; 
		panel1.add(label3,c1);
		
		// for panel 2  ie. middle panel
		panel2=new JPanel(new GridBagLayout());
		GridBagConstraints c2 = new GridBagConstraints();
		blackline = BorderFactory.createLineBorder(Color.black);
		panel2.setBorder(blackline);
		c2.gridx = 0; c2.gridy =0; 
		c2.insets = new Insets(10,10,10,10);
		panel2.add(label4,c2);
		c2.gridx = 0; c2.gridy = 1; 
		panel2.add(input,c2);
		c2.gridx = 0; c2.gridy = 2; 
		panel2.add(submit,c2);
		
		//for panel 3 ie left panel
		panel3=new JPanel(new GridBagLayout());
		GridBagConstraints c3 = new GridBagConstraints();
		blackline = BorderFactory.createLineBorder(Color.black);
		panel3.setBorder(blackline);
		c3.insets = new Insets(20,20,20,20);
		c3.gridx = 0; c3.gridy =0; 
		panel3.add(logout,c3);
		c3.gridx = 0; c3.gridy =1; 
		panel3.add(createRindingInforFile,c3);
		
		content.add(panel1,BorderLayout.PAGE_START);
		content.add(panel2,BorderLayout.CENTER);
		content.add(panel3,BorderLayout.EAST);
		
		// add action for three buttons.
		submit.addActionListener(this);
		createRindingInforFile.addActionListener(this);
		logout.addActionListener(this);
		Dimension scr = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(scr.width/2-this.getWidth()/2,scr.height/2-this.getHeight()/2);
		this.setVisible(true);
	}
	public void actionPerformed(ActionEvent e) {
		//String actionCommand = e.getActionCommand();
		
		//System.out.println(actionCommand);
		
		if (e.getSource() == logout)
		{
			UserManager.logoff();
			new LoginWindow( UserManager, RepManager );
			this.dispose();
		}
		else if (e.getSource() == submit)
		{
			String pollNumber = input.getText();  // need to be converted to integer!!! 
		}
		else // if createRindingInforFile
		{
//			RidingSearchGUI searchRiding = new RidingSearchGUI(UserManager,RepManager);
		}
		content.validate();
	}

}
