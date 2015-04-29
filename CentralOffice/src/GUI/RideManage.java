package GUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.*;
import ManageRepository.*;
import ManageUsers.UserManagement;

public class RideManage extends JFrame implements ActionListener{
	private RepositoryManagement RepManager;
	private UserManagement UserManager;
	private int InitialWidth  = 500;
	private int IntitalHeight = 500;
	
	String[] rideList;
	JList rides;
	Container content;
	JButton locate, create, back, logout, mainmenu;
	String AddorEdit;
	String oldname;
	JFileChooser fc;
	
	
	public RideManage( RepositoryManagement RM, UserManagement  UM ){
		RepManager = RM;
		UserManager = UM;
		
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
		this.setTitle("Create Riding Info File");
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
		JPanel buttons = new JPanel();
		create = new JButton("<html><center>Create Info File<br>For Selected Riding</html>");
		buttons.add(create,BorderLayout.SOUTH);
		create.addActionListener(this);
		return buttons;
	}
	
	public void actionPerformed(ActionEvent a){
			if(a.getSource() == back){
				new elecFiles( UserManager, RepManager );
				this.dispose();
			} else if (a.getSource() == mainmenu){
				new EOMainMenu( UserManager, RepManager );
				this.dispose();
			} else if(a.getSource() == logout){
				UserManager.logoff();
				new LoginWindow(UserManager, RepManager);
				this.dispose();
			} else if(a.getSource() == create){
				riding ride = RepManager.getRiding( rideList[rides.getSelectedIndex()]);
			
				int n = JOptionPane.showConfirmDialog(
					    this,
					    "Will this info file be for a Central Polling Station?",
					    "",
					    JOptionPane.YES_NO_OPTION);
				if(n == JOptionPane.NO_OPTION || n == JOptionPane.YES_OPTION){
					boolean central = true;
					if(n == JOptionPane.NO_OPTION){
						central = false;
					}
				
					try {
						fc = new JFileChooser();
						int ret = fc.showSaveDialog( null );
							if(ret == JFileChooser.APPROVE_OPTION){
								File file = fc.getSelectedFile();
								RepManager.createRidingInforFile(ride , file , central);
								JOptionPane.showMessageDialog(null,"Success Riding Info File Created");
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
			}
	}

	public void AddEdit(riding ride){
		if(AddorEdit.equals("add")){
			RepManager.saveNewRiding(ride);
		} else {
			RepManager.updateRiding(ride, oldname);
		}
		
	}

}


