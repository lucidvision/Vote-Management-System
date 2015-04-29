package GUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import ManageRepository.riding;

/**
 * 
 * @author Charles Ferguson
 *
 */
@SuppressWarnings("serial")
public class EnterCandidates extends  JFrame implements ActionListener {
	private int initialWidth = 500;
	private int initialHeight = 200;
	private JLabel label0, label1,label2,label3,label4;
	private JLabel[] labelsCan;
	private JTextField[]  input;
	private JPanel panel0,panel1,panel2;
	private Container content;
	private JButton back,con;
	private riding forRiding;
	private RepManage lastScreen;
	
	public EnterCandidates( riding ride, RepManage last ) {
		lastScreen = last;
		
		forRiding = ride;
		// TODO Auto-generated constructor stub
		this.setSize(initialWidth, initialHeight+forRiding.getNumCandi()*17);
		this.setResizable(true);
		//Title bar name.
		this.setTitle("Enter Candidate Names and Party Affilition");
		// default close operation
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		content = getContentPane();
		
		label0 = new JLabel();
		label0.setText("Enter the candidates name and their party affilication ");
		label1 = new JLabel();
		label1.setText(" as they will appear on the ballots to continue creating riding:");		
		label2 = new JLabel();
		label2.setText(forRiding.getRidingName());
		label3 = new JLabel();		
		label3.setText("Cantidate Names");
		label4 = new JLabel();
		label4.setText("Party Affilitions");
		
		int num_can = forRiding.getNumCandi();
		labelsCan = new JLabel[num_can];
		for(int i = 0;i < num_can;i++){
			labelsCan[i] = new JLabel();
			labelsCan[i].setText("Candidate #" + (i+1));
		}
		
		back=new JButton("Back");
		con = new JButton("Continue");
		panel0 = new JPanel(new GridBagLayout());
		panel1 = new JPanel(new GridBagLayout());
		panel2 = new JPanel(new GridBagLayout());
		GridBagConstraints c1 = new GridBagConstraints();
		c1.gridx = 0; c1.gridy =0; 
		panel0.add(label0,c1);
		c1.gridx = 0; c1.gridy =1;
		panel0.add(label1,c1);
		c1.gridx = 0; c1.gridy =2;
		panel0.add(label2,c1);
		
		c1.gridx = 2; c1.gridy = 0;
		panel1.add(label3,c1);
		c1.gridx = 3; c1.gridy = 0;
		panel1.add(label4,c1);

		
		input = new JTextField[num_can*2];
		for (int i = 0; i < num_can; i ++){
			c1.gridx = 1; c1.gridy = i+1;
			panel1.add(labelsCan[i],c1);
			c1.gridx = 2; c1.gridy = i+1;
			input[i] = new JTextField(10);
			panel1.add(input[i],c1);
			input[i+num_can] = new JTextField(10);
			c1.gridx = 3; c1.gridy = i+1;
			panel1.add(input[i+num_can],c1);
		}
		
		
		if(forRiding.getCandiName() != null){ populateCan(num_can); }
		if(forRiding.getCandiAff() != null){ populateAff(num_can); }
		c1.gridx = 5; c1.gridy = 0;
		panel2.add(back,c1);
		c1.gridx = 6; c1.gridy = 0;
		panel2.add(con,c1);
		
		content.add(panel0,BorderLayout.PAGE_START);
		content.add(panel1,BorderLayout.CENTER);
		content.add(panel2,BorderLayout.SOUTH);
		
		con.addActionListener(this);
		back.addActionListener(this);
		Dimension scr = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(scr.width/2-this.getWidth()/2,scr.height/2-this.getHeight()/2);
		this.setVisible(true);
		
	}
	
	private void populateCan(int num_can){
		String[] storedCan = forRiding.getCandiName();
		int min = storedCan.length;
		if(num_can < storedCan.length){ 
			min = num_can;
		}
		for(int i = 0;i < min;i++){
			input[i].setText(storedCan[i]);
		}
	}
	
	private void populateAff(int num_can){
		String[] storedAff = forRiding.getCandiAff();
		int min = storedAff.length;
		if(num_can < storedAff.length){ 
			min = num_can;
		}
		for(int i = 0;i < min;i++){
			input[i+num_can].setText(storedAff[i]);
		}
	}

	private boolean saveInfo(){
		boolean got = true;
		int num_can = forRiding.getNumCandi();
		
		String[] cand = new String[num_can];
		String[] part = new String[num_can];
		for(int i = 0;i < num_can; i++){
			String can = input[i].getText().trim();
			String par = input[i+num_can].getText().trim();
			if(can.equals("") || par.equals("")){
				String[] err = {"Error could not save info.",
						"One or more text field is blank.",
						"Please enter the value before pressing continue again", 
						"or change the number of candidates by pressing back."};
				new Error(err);
				got = false;
				break;
			}
			else{
				cand[i] = can;
				part[i] = par;
			}
		}
		if(got) {
			forRiding.setCandiName(cand);
			forRiding.setCandiAff(part);
		}
		return got;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == con){
			if(saveInfo()){ 
				new EnterIncumbent( forRiding , lastScreen);
				this.dispose();
			} 
		} else if(e.getSource() == back){
			new CreateRiding(forRiding , lastScreen );
			this.dispose();
		}
		
	}
	
}