package GUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

import ManageRepository.*;
import ManageUsers.UserManagement;

public class RepManage extends JFrame implements ActionListener{
	private RepositoryManagement RepManager;
	private UserManagement UserManager;
	private int InitialWidth  = 500;
	private int IntitalHeight = 500;
	
	String[] rideList;
	JRadioButton[] rideButtons;
	JList rides;
	Container content;
	JButton add, edit, delete, back, logout, mainmenu;
	EOMainMenu EO;
	String AddorEdit;
	String oldname;
	
	
	public RepManage( RepositoryManagement RM, UserManagement  UM, EOMainMenu last){
		RepManager = RM;
		UserManager = UM;
		EO = last;
		
		JPanel top = new JPanel(new BorderLayout());
		top.add(getInfo(),BorderLayout.WEST);
		top.add(getNav(),BorderLayout.EAST);
		
		JPanel center = new JPanel();
		center.add(getRideRes());
		
		
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
	
	/**
	 * getInfo() a JPanel that has basic info:
	 * 		currUser's real name
	 * 		Current location
	 * 		Current ElectionStatus
	 */
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

	private JScrollPane getRideRes(){
		JScrollPane pane = new JScrollPane();
		pane.setPreferredSize(new Dimension(200,350));
		pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		rideList = RepManager.getRidingList();
		rides = new JList( rideList );
		rides.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		rides.setSelectedIndex(0);
		pane.getViewport().add(rides);
		return pane;
	}
	
	private JPanel getButtons(){
		JPanel buttons = new JPanel(new GridBagLayout());
		GridBagConstraints e = new GridBagConstraints();
		e.insets = new Insets (50, 50, 50, 5);
		e.fill = GridBagConstraints.HORIZONTAL;
		add = new JButton("Add new Riding");
		edit = new JButton("Edit Selected Riding");
		delete = new JButton("Delete Selected Riding");
		
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
				EO.show();
				this.dispose();
			} else if (a.getSource() == mainmenu){
				EO.show();
				this.dispose();
			} else if(a.getSource() == logout){
				UserManager.logoff();
				new LoginWindow(UserManager, RepManager);
				this.dispose();
			} else if(a.getSource() == add){
				AddorEdit = "add";
				new CreateRiding( new riding(), this );
				this.dispose();
			} else if (a.getSource() == edit){
				oldname = null;
				AddorEdit = "edit";
				int i = rides.getSelectedIndex();
				oldname = rideList[i];
				new CreateRiding( RepManager.getRiding(rideList[i]), this );
				this.hide();
			} else if (a.getSource() == delete){
				int i = rides.getSelectedIndex();
				riding ride = RepManager.getRiding(rideList[i]);
				if(JOptionPane.showConfirmDialog(null, "Are you sure want to delete riding: " 
								+  ride.getRidingName()) == 0){
					RepManager.deleteRiding(ride);
					new RepManage( RepManager, UserManager, EO);
					this.dispose();
				}
			}
	}
	
	public void launchanew(){
		new RepManage(RepManager,UserManager,EO);
		this.dispose();	
	}
	
	public void AddEdit(riding ride){
		if(AddorEdit.equals("add")){
			RepManager.saveNewRiding(ride);
		} else {
			RepManager.updateRiding(ride, oldname);
		}
		
	}
		
}

