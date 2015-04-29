package GUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import ManageUsers.*;

@SuppressWarnings("serial")
public class CreateUser extends  JFrame implements ActionListener {
	private int initialWidth = 500;
	private int initialHeight = 350;
	private JCheckBox blocked;
	private JRadioButton[] roles;
	private JTextField[]  input;
	private Container content;
	private JButton back, save;
	private elecUser forUser;
	private UserManage UM;
	
	public CreateUser( elecUser user, UserManage lastScreen) {
		UM = lastScreen;
		forUser = user;
		
		this.setSize(initialWidth, initialHeight);
		this.setResizable(true);
		this.setTitle("Enter User Information");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		content = getContentPane();
		content.add(new JLabel("Please Enter the User's Information."),BorderLayout.NORTH);
		content.add(getInfo(),BorderLayout.CENTER);
		content.add(getButtons(),BorderLayout.SOUTH);
		Dimension scr = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(scr.width/2-this.getWidth()/2,scr.height/2-this.getHeight()/2);
		this.setVisible(true);
		
		
	}

	private JPanel getInfo(){
		JPanel center = new JPanel(new GridBagLayout());
		GridBagConstraints e = new GridBagConstraints();
		e.fill = GridBagConstraints.HORIZONTAL;
		input = new JTextField[4];
		input[0] = new JTextField();
		input[1] = new JTextField();
		input[2] = new JTextField();
		input[3] = new JTextField();
		blocked = new JCheckBox();
		blocked.addActionListener(this);
		roles = new JRadioButton[4];
		roles[0] = new JRadioButton();
		roles[1] = new JRadioButton();
		roles[2] = new JRadioButton();
		roles[3] = new JRadioButton();
		e.ipadx = 90;
		e.gridx = 0; e.gridy = 0;
		center.add(new JLabel("Enter user's desired Login:"),e);
		e.gridx++;
		center.add(input[0],e);
		e.gridx--; e.gridy++;
		center.add(new JLabel("Enter user's Password: "),e);
		e.gridx++;
		center.add(input[3],e);
		e.gridx--; e.gridy++;
		center.add(new JLabel("Enter user's Last Name: "),e);
		e.gridx++;
		center.add(input[1],e);
		e.gridx--; e.gridy++;
		center.add(new JLabel("Enter user's First Name: "),e);
		e.gridx++;
		center.add(input[2],e);
		e.gridx--; e.gridy++;
		center.add(new JLabel("Is user currently blocked: "),e);
		e.gridx = 1;
		center.add(blocked,e);
		e.gridx--; e.gridy++;
		center.add(new JLabel("System Administrator "),e);
		e.gridx++;
		center.add(roles[0],e);
		e.gridx--; e.gridy++;
		center.add(new JLabel("Electoral Official "),e);
		e.gridx++;
		center.add(roles[1],e);
		e.gridx--; e.gridy++;
		center.add(new JLabel("Returning Officer "),e);
		e.gridx++;
		center.add(roles[2],e);
		e.gridx--; e.gridy++;
		center.add(new JLabel("Reporter"),e);
		e.gridx++;
		center.add(roles[3],e);
		if(forUser.getLogin() != null){
			setUser();
		}
		return center;
	}
	
	private JPanel getButtons(){
		back = new JButton("Back");	
		back.addActionListener(this);
		save = new JButton("Save User"); 
		save.addActionListener(this);
		JPanel menu = new JPanel(new GridLayout());
		GridBagConstraints e = new GridBagConstraints();
		e.fill = GridBagConstraints.BOTH;
		e.insets = new Insets (10,10,10,10);
		e.gridx = 0; e.gridy = 0;
		menu.add(back,e);
		e.gridx = 1;
		menu.add(save,e);
		return menu;
		
	}
	
	private void setUser(){
		input[0].setText(forUser.getLogin());
		input[3].setText(forUser.getPass());
		input[1].setText(forUser.getLName());
		input[2].setText(forUser.getFName());
		blocked.setSelected(forUser.getBlock());
		roles[0].setSelected(forUser.getRole()[0]);
		roles[1].setSelected(forUser.getRole()[1]);
		roles[2].setSelected(forUser.getRole()[2]);
		roles[3].setSelected(forUser.getRole()[3]);
	}
	
	private boolean saveInfo(){
		boolean got = false;
		String login = input[0].getText().trim();
		String LName = input[1].getText().trim();
		String FName = input[2].getText().trim();
		String Pass = input[3].getText().trim();
		boolean[] role = new boolean[4];
		role[0] = roles[0].isSelected();
		role[1] = roles[1].isSelected();
		role[2] = roles[2].isSelected();
		role[3] = roles[3].isSelected();
		if(login.equals("") || LName.equals("")|| FName.equals("") || Pass.equals("") ){
			JOptionPane.showMessageDialog(null, "User must have values for all fields");
			return false;
		} else if( ! UM.checkLogin( login ) && UM.AddorEdit.equals("add")){
			JOptionPane.showMessageDialog(null, "UserName is already taken");
			return false;
		} else {
			forUser.setLogin( login );
			forUser.setPass( Pass );
			forUser.setLName( LName );
			forUser.setFName( FName );
			forUser.setBlock(blocked.isSelected());
			forUser.setRole( role );
			got = true;
		}
		return got;
	}
	
	@SuppressWarnings("deprecation")
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == save){
			if(saveInfo()){ 
				UM.AddEdit( forUser );
				UM.show();
				this.dispose();
			} 
		} else if(e.getSource() == back){
			UM.show();
			UM.repaint();
			this.dispose();
		}
		
	}
}