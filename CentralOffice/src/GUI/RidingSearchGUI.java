package GUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.Border;


public class RidingSearchGUI extends JFrame implements ActionListener {
	//variables 
	private int initialWidth = 800;
	private int initialHeight = 400;
	// these labels are corresponding to to the text in the window 
	private JLabel label1,label2,label3,label4;
	private JLabel label5,label6,label7,label8,label9,label10;
	// buttons
	private JButton mainMenu, search;
	//text field
	private JTextField  criteria1,criteria2,criteria3,criteria4;
	//combo box
	private JComboBox key1,key2,key3,key4;
	private JPanel panel1, panel2;
	private Container content;
	// ************ to be added in further development
	private String [] keywordList = {"All","Riding Name", "Candidate's Party", "Unused"};
	
	public RidingSearchGUI(String user, String status, String location) {
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
		label4.setText("Please Enter Search Criteria to find Riding that you wish to obrain report on.");
		label5 = new JLabel();
		label5.setText("Criteria1");
		label6 = new JLabel();
		label6.setText("Criteria2");
		label7 = new JLabel();
		label7.setText("Criteria3");
		label8 = new JLabel();
		label8.setText("Criteria4");
		label9 = new JLabel();
		label9.setText("Criteria to Search for: ");
		label10 = new JLabel();
		label10.setText("Keywords to Search: ");
		//buttons
		mainMenu = new JButton("Main Menu");
		search = new JButton("Search");
		mainMenu.addActionListener(this);
		search.addActionListener(this);
		//text field
		criteria1 = new JTextField(15);
		criteria2 = new JTextField(15);
		criteria3 = new JTextField(15);
		criteria4 = new JTextField(15);
		//combo box
		key1 = new JComboBox(keywordList);
		key2 = new JComboBox(keywordList);
		key3 = new JComboBox(keywordList);
		key4 = new JComboBox(keywordList);
		// panel 1
		panel1 = new JPanel(new GridBagLayout());
		GridBagConstraints c1 = new GridBagConstraints();
		c1.insets = new Insets(0,20,0,20);
		
		c1.gridx = 0; c1.gridy =0;
		panel1.add(label1,c1);
		c1.gridx = 0; c1.gridy =1;
		panel1.add(label2,c1);
		c1.gridx = 0; c1.gridy =2;
		panel1.add(label3,c1);
		c1.gridx = 2; c1.gridy =0;
		panel1.add(mainMenu,c1);
		c1.gridx = 1; c1.gridy =4;
		panel1.add(label4,c1);
		
		//panel 2
		
		panel2 = new JPanel(new GridBagLayout());
		GridBagConstraints c2 = new GridBagConstraints();
		Border blackline = BorderFactory.createLineBorder(Color.black);
		panel2.setBorder(blackline);
		c2.fill = GridBagConstraints.HORIZONTAL ;
		c2.insets = new Insets(10,10,10,10);
		// For labels
		c2.gridx = 1; c2.gridy =0;
		panel2.add(label5,c2);
		c2.gridx = 2; c2.gridy =0;
		panel2.add(label6,c2);
		c2.gridx = 3; c2.gridy =0;
		panel2.add(label7,c2);
		c2.gridx = 4; c2.gridy =0;
		panel2.add(label8,c2);
		c2.gridx = 0; c2.gridy =1;
		panel2.add(label9,c2);
		c2.gridx = 0; c2.gridy =2;
		panel2.add(label10,c2);
		// For textfield
		c2.gridx = 1; c2.gridy =1;
		panel2.add(criteria1,c2);
		c2.gridx = 2; c2.gridy =1;
		panel2.add(criteria2,c2);
		c2.gridx = 3; c2.gridy =1;
		panel2.add(criteria3,c2);
		c2.gridx = 4; c2.gridy =1;
		panel2.add(criteria4,c2);
		//For combo box
		c2.gridx = 1; c2.gridy =2;
		panel2.add(key1,c2);
		c2.gridx = 2; c2.gridy =2;
		panel2.add(key2,c2);
		c2.gridx = 3; c2.gridy =2;
		panel2.add(key3,c2);
		c2.gridx = 4; c2.gridy =2;
		panel2.add(key4,c2);
		c2.gridx = 4; c2.gridy =4;
		panel2.add(search,c2);
		//add panels to contents
		content.add(panel1,BorderLayout.NORTH);
		content.add(panel2,BorderLayout.CENTER);
		Dimension scr = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(scr.width/2-this.getWidth()/2,scr.height/2-this.getHeight()/2);
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		String actionCommand = e.getActionCommand();
		
		System.out.println(actionCommand);
		
		if (actionCommand.equals("Main Menu"))
		{
			this.dispose();
		}
		else // action of search function
		{
			// Get information from different criteria
			String Cone =criteria1.getText();
			String Ctwo =criteria2.getText();
			String Cthree =criteria2.getText();
			String Cfour =criteria2.getText();
			// Get information from different keys
			String k1 = (String) key1.getSelectedItem();
			String k2 = (String) key2.getSelectedItem();
			String k3 = (String) key3.getSelectedItem();
			String k4 = (String) key4.getSelectedItem();
			
			RidingResultGUI ridingResult = new RidingResultGUI("tomy","Before Election", "Central Office");
			
		}
		
	}

}
