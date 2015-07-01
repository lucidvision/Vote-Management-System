package GUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.Border;
public class RidingResultGUI extends JFrame implements ActionListener{
	//variables 
	private int initialWidth = 800;
	private int initialHeight = 400;
	// these labels are corresponding to to the text in the window 
	private JLabel label1,label2,label3,label4;
	private JLabel label5,label6,label7;
	private JRadioButton button1,button2, button3;
	private JPanel panel;
	private JScrollPane scrollPane;
	private Container content;
	// buttons
	private JButton back,mainMenu,createRidingInfoFile;
	public RidingResultGUI(String user, String status, String location) {
		//Initial size of frame.
		this.setSize(initialWidth, initialHeight);
		this.setResizable(true);
		//Title bar name.
		this.setTitle("Search for Riding Based on Keywords");
		// default close operation
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		//major pane
		content = getContentPane();
		// initialization of variables
		label1 = new JLabel();
		label1.setText("Returnig Officer: " + user);
		label2 = new JLabel();
		label2.setText("Status: " + status);
		label3 = new JLabel();
		label3.setText("Location: " + location);
		label4 = new JLabel();
		label4.setText("<html>The following Ridings matched your search criteria. " +
				"<p>Please select the riding you wish to include in your report.</html>");
		
		// this should be automatically got from ridings by searching. to be implemented! 
		label5 = new JLabel();
		label5.setText("Capital Hill North");
		label6 = new JLabel();
		label6.setText("Hasting North");
		label7 = new JLabel();
		label7.setText("Burnaby Mountain North");
		button1 = new JRadioButton();
		button2 = new JRadioButton();
		button3 = new JRadioButton();
		//buttons
		mainMenu = new JButton("Main Menu");
		back = new JButton("Back");
		createRidingInfoFile = new JButton("Create Riding Info File");
		mainMenu.addActionListener(this);
		back.addActionListener(this);
		createRidingInfoFile.addActionListener(this);
		//up panel
		panel = new JPanel(new GridBagLayout());
		GridBagConstraints c1 = new GridBagConstraints();
		c1.insets = new Insets(0,20,0,20);
		c1.gridx = 0; c1.gridy =0;
		panel.add(label1,c1);
		c1.gridx = 0; c1.gridy =1;
		panel.add(label2,c1);
		c1.gridx = 0; c1.gridy =2;
		panel.add(label3,c1);
		c1.gridx = 2; c1.gridy =1;
		panel.add(back,c1);
		c1.gridx = 3; c1.gridy =1;
		panel.add(mainMenu,c1);
		c1.gridx = 1; c1.gridy =4;
		panel.add(label4,c1);
		//scroll pane
		JPanel p = new JPanel(new GridBagLayout());
		GridBagConstraints c2 = new GridBagConstraints();
		c2.gridx = 0; c2.gridy =0;
		p.add(button1,c2);
		c2.gridx = 1; c2.gridy =0;
		p.add(label5,c2);
		c2.gridx = 0; c2.gridy =1;
		p.add(button2,c2);
		c2.gridx = 1; c2.gridy =1;
		p.add(label6,c2);
		c2.gridx = 0; c2.gridy =2;
		p.add(button3,c2);
		c2.gridx = 1; c2.gridy =2;
		p.add(label7,c2);
		scrollPane = new JScrollPane(p);
		
		content.add(panel,BorderLayout.NORTH);
		content.add(scrollPane,BorderLayout.CENTER);
		content.add(createRidingInfoFile,BorderLayout.AFTER_LINE_ENDS);
		Dimension scr = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(scr.width/2-this.getWidth()/2,scr.height/2-this.getHeight()/2);
		setVisible(true);
		
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
