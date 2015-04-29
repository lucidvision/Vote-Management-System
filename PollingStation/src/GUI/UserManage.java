package GUI;
import initialize.Controller;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import ManageUsers.*;

@SuppressWarnings("serial")
public class UserManage extends JFrame implements ActionListener{
	private Controller ctlr;
	private UserManagement UserManager;
	private int InitialWidth  = 500;
	private int IntitalHeight = 500;
	
	String[] userList;
	JList usersL = new JList(userList);
	Container content;
	JButton add, edit, delete, back, logout, mainmenu;
	String AddorEdit;
	String oldname;
	
	
	public UserManage(   Controller con ){
		ctlr = con;
		UserManager = ctlr.getUserMan();
		
		JPanel top = new JPanel(new BorderLayout());
		top.add(getInfo(),BorderLayout.WEST);
		top.add(getNav(),BorderLayout.EAST);
		
		JPanel center = new JPanel();
		center.add(getUserList());
		
		
		content = getContentPane();
		content.add(center,BorderLayout.CENTER);
		content.add(top,BorderLayout.NORTH);
		content.add(getButtons(),BorderLayout.EAST);
		this.setSize(InitialWidth, IntitalHeight);
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
		info.add(new JLabel("Before Election"), e);
		return info;
	}

	private JScrollPane getUserList(){
		JScrollPane pane = new JScrollPane();
		pane.setPreferredSize(new Dimension(200,350));
		pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		userList = UserManager.getUserList();
		String[] justLogins = new String[userList.length/3];
		for(int i = 0; i < justLogins.length; i++){
			justLogins[i]=userList[i];
		}
		usersL = new JList( justLogins );
		usersL.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		usersL.setSelectedIndex(0);
		pane.getViewport().add(usersL);
		return pane;
	}
	
	private JPanel getButtons(){
		JPanel buttons = new JPanel(new GridBagLayout());
		GridBagConstraints e = new GridBagConstraints();
		e.insets = new Insets (50, 50, 50, 5);
		e.fill = GridBagConstraints.HORIZONTAL;
		add = new JButton("Add new User");
		edit = new JButton("Edit Selected User");
		delete = new JButton("Delete Selected User");
		
		e.gridx = 0; e.gridy = 0;
		buttons.add(add,e);
		e.gridy = 1;
		buttons.add(edit,e);
		e.gridy = 2;
		buttons.add(delete,e);
		add.addActionListener(this);
		edit.addActionListener(this);
		delete.addActionListener(this);
		return buttons;
	}
	
	@SuppressWarnings("deprecation")
	public void actionPerformed(ActionEvent a){
			if(a.getSource() == back){
				new SAMainMenu( ctlr );
				this.dispose();
			} else if (a.getSource() == mainmenu){
				new SAMainMenu( ctlr );
				this.dispose();
			} else if(a.getSource() == logout){
				UserManager.logoff();
				new LoginWindow( ctlr );
				this.dispose();
			} else if(a.getSource() == add){
				AddorEdit = "add";
				new CreateUser( new elecUser(), this );
				this.dispose();
			} else if (a.getSource() == edit){
				oldname = null;
				int i = usersL.getSelectedIndex();
				AddorEdit = "edit";
				oldname = userList[i];
				new CreateUser( UserManager.getUser(oldname), this);
				this.hide();
			} else if (a.getSource() == delete){
				int i = usersL.getSelectedIndex();
				oldname = userList[i];
				UserManager.deleteUser( UserManager.getUser( oldname ) );
				new UserManage( ctlr );
				this.dispose();
			}
	}
	
	
	public void launchanew(){
		new UserManage( ctlr );
		this.dispose();	
	}
	
	public boolean checkLogin( String login ){
		boolean check = true;
		for(int i = 0; i < userList.length/3;i++){
			if(userList[i].equals(login)) return false;
		}
		return check;
	}
	public void AddEdit( elecUser user ){
		if(AddorEdit.equals("add")){
			user.setUse(0);
			UserManager.addUser(user);
		} else {
			UserManager.updateUser( user, oldname);
		}
		
	}
		
}

