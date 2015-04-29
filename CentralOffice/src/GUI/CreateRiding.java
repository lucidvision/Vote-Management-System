package GUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import ManageRepository.riding;

@SuppressWarnings("serial")
public class CreateRiding extends  JFrame implements ActionListener {
	private int initialWidth = 500;
	private int initialHeight = 250;
	private JLabel label0;
	private JLabel[] labels = new JLabel[8];
	private JTextField[]  input = new JTextField[8];
	private JPanel panel0,panel1,panel2;
	private Container content;
	private JButton back,con;
	private riding forRiding;
	private RepManage last;
	
	public CreateRiding(riding ride, RepManage lastScreen) {
		last = lastScreen;
		forRiding = ride;
		// TODO Auto-generated constructor stub
		this.setSize(initialWidth, initialHeight);
		this.setResizable(true);
		//Title bar name.
		this.setTitle("Enter Basic Keywords");
		// default close operation
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		content = getContentPane();
		
		label0 = new JLabel();
		label0.setText("Please enter the basic infprmation for this riding");
		
		labels[0] = new JLabel();
		labels[0].setText("Riding Name: ");
		labels[1] = new JLabel();
		labels[1].setText("Date Riding Created: (DD/MM/YYYY)");		
		labels[2] = new JLabel();
		labels[2].setText("Number of Eligible Voters: ");
		labels[3] = new JLabel();		
		labels[3].setText("Number of Seats to be filled: ");
		labels[4]= new JLabel();
		labels[4].setText("Number of Candidates Running: ");
		labels[5]= new JLabel();
		labels[5].setText("Number of Incumbent MLAs: ");
		labels[6]= new JLabel();
		labels[6].setText("Number of Polls to be counted: ");
		labels[7]= new JLabel();
		labels[7].setText("First poll number: ");
		
		input[0] = new JTextField(10);
		input[0].setText(forRiding.getRidingName());
		input[1] = new JTextField(10);
		input[1].setText(forRiding.getDateCr());
		input[2] = new JTextField(10);
		input[2].setText(Integer.toString(forRiding.getNumVote()));
		input[3] = new JTextField(10);
		input[3].setText(Integer.toString(forRiding.getNumSeats()));
		input[4] = new JTextField(10);
		input[4].setText(Integer.toString(forRiding.getNumCandi()));
		input[5] = new JTextField(10);
		input[5].setText(Integer.toString(forRiding.getNumIncum()));
		input[6] = new JTextField(10);
		input[6].setText(Integer.toString(forRiding.getNumPolls()));
		input[7] = new JTextField(10);
		input[7].setText("");
		if(forRiding.getPolls() != null){input[7].setText(Integer.toString(forRiding.getPolls()[0]));}
		
		
		back=new JButton("Back");
		con = new JButton("Continue");
		
		panel0 = new JPanel(new GridBagLayout());
		panel1 = new JPanel(new GridBagLayout());
		panel2 = new JPanel(new GridBagLayout());
		GridBagConstraints c1 = new GridBagConstraints();
		c1.gridx = 0; c1.gridy =0; 
		panel0.add(label0,c1);
		
		
		

		for (int i = 0; i <= 7; i ++){
			c1.gridx = 0; c1.gridy = i;
			panel1.add(labels[i],c1);
			c1.gridx = 1; c1.gridy = i;
			panel1.add(input[i],c1);
		}
		
		
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

	private boolean saveInfo(){
		boolean got = false;
		
		String ride = input[0].getText().trim();
		String date = input[1].getText().trim();
		String votes = input[2].getText().trim();
		String seats = input[3].getText().trim();
		String cans = input[4].getText().trim();
		String incums = input[5].getText().trim();
		String polls = input[6].getText().trim();
		String polln = input[7].getText().trim();
		
		if(ride.equals("") || date.equals("")|| votes.equals("") || polln.equals("") ||
			seats.equals("") || cans.equals("") ||incums.equals("") || polls.equals("")){
			String[] err = {"Error could not save info.",
					"One or more of the fields is blank.",
					"Please enter the value before pressing continue again"};
			new Error(err);
		} else {
			forRiding.setRidingName(ride);
			forRiding.setDate(date);
			try{
				int ivote = Integer.parseInt(votes);
				forRiding.setNumVote(ivote);
				int iseats = Integer.parseInt(seats);
				forRiding.setNumSeats(iseats);
				int icans = Integer.parseInt(cans);
				forRiding.setNumCandi(icans);
				int iincums = Integer.parseInt(incums);
				forRiding.setNumIncum(iincums);
				int ipolls = Integer.parseInt(polls);
				forRiding.setNumPolls(ipolls);
				int ipolln = Integer.parseInt(polln);
				int[] nums = new int[ipolls];
				for(int i = 0; i < ipolls;i++){
					nums[i] = ipolln + i;
				}
				forRiding.setPolls(nums);
				got = true;
			} catch(NumberFormatException e){
				String[] err = {"Error could not save info.",
						"All values except Riding Name and date created have to be numeric values",
						"Please enter the value before pressing continue again"};
				new Error(err);
			}
		}
		return got;
	}
	@SuppressWarnings("deprecation")
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == con){
			if(saveInfo()){ 
				new EnterCandidates(forRiding, last);
				this.dispose();
			} 
		} else if(e.getSource() == back){
			last.show();
			this.dispose();
		}
		
	}
}