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
public class EnterIncumbent extends  JFrame implements ActionListener {
	private int initialWidth = 600;
	private int initialHeight = 200;
	private JLabel label0, label1,label2,label3,label4;
	private JLabel[] labelsCan;
	private JTextField[]  input;
	private JPanel panel0,panel1,panel2;
	private Container content;
	private JButton back,con;
	private riding forRiding;
	private RepManage RM;
	
	public EnterIncumbent(riding ride , RepManage last) {
		RM = last;
		forRiding = ride;
		// TODO Auto-generated constructor stub
		this.setSize(initialWidth, initialHeight+forRiding.getNumIncum()*17);
		this.setResizable(true);
		//Title bar name.
		this.setTitle("Enter Incumbent Names and Party Affilition");
		// default close operation
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		content = getContentPane();
		
		label0 = new JLabel();
		label0.setText("Enter the incumbent name and their party affilication ");
		label1 = new JLabel();
		label1.setText("as they will appear on the ballots to continue creating riding:");		
		label2 = new JLabel();
		label2.setText(forRiding.getRidingName());
		label3 = new JLabel();		
		label3.setText("Cantidate Names");
		label4 = new JLabel();
		label4.setText("Party Affilitions");
		
		int num_incum = forRiding.getNumIncum();
		labelsCan = new JLabel[num_incum];
		for(int i = 0;i < num_incum;i++){
			labelsCan[i] = new JLabel();
			labelsCan[i].setText("Incumbent #" + (i+1));
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

		
		input = new JTextField[num_incum*2];
		for (int i = 0; i < num_incum; i ++){
			c1.gridx = 1; c1.gridy = i+1;
			panel1.add(labelsCan[i],c1);
			c1.gridx = 2; c1.gridy = i+1;
			input[i] = new JTextField(10);
			panel1.add(input[i],c1);
			input[i+num_incum] = new JTextField(10);
			c1.gridx = 3; c1.gridy = i+1;
			panel1.add(input[i+num_incum],c1);
		}
		
		if(forRiding.getIncumbets() != null) {populateIncum(num_incum);}
		if(forRiding.getIncumbAff() != null) {populateAff(num_incum);}
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

	private void populateIncum(int num_incum){
		String[] storedIncum = forRiding.getIncumbets();
		int min = storedIncum.length;
		if(num_incum < storedIncum.length){ 
			min = num_incum;
		}
		for(int i = 0;i < min;i++){
			input[i].setText(storedIncum[i]);
		}
	}
	
	private void populateAff(int num_incum){
		String[] storedAff = forRiding.getIncumbAff();
		int min = storedAff.length;
		if(num_incum < storedAff.length){ 
			min = num_incum;
		}
		for(int i = 0;i < min;i++){
			input[i+num_incum].setText(storedAff[i]);
		}
	}
	private boolean saveInfo(){
		boolean got = true;
		int num_incum = forRiding.getNumIncum();
		
		String[] incum = new String[num_incum];
		String[] part = new String[num_incum];
		for(int i = 0;i < num_incum; i++){
			String incumb = input[i].getText().trim();
			String par = input[i+num_incum].getText().trim();
			if(incumb.equals("") || par.equals("")){
				String[] err = {"Error could not save info.",
						"One or more text field is blank.",
						"Please enter the value before pressing continue again", 
						"or change the number of incumbents by pressing back."};
				new Error(err);
				got = false;
				break;
			}
			else{
				incum[i] = incumb;
				part[i] = par;
			}
		}
		if(got) {
			forRiding.setIncumbets(incum);
			forRiding.setIncumAff(part);
		}
		return got;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == con){
			if(saveInfo()){
				new LocateMap(forRiding , RM);
				this.dispose();
			} 
		} else if(e.getSource() == back){
			new EnterCandidates(forRiding , RM);
			this.dispose();
		}
		
	}
}