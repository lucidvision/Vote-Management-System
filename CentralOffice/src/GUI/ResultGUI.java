package GUI;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import ManageRepository.RepositoryManagement;
import ManageUsers.UserManagement;
import ReportGenerator.ReportGenerator;
/**
 * @author Tomy Tao
 */
@SuppressWarnings("serial")
public class ResultGUI extends JFrame implements ActionListener{
	//variables 
	private int initialWidth = 600;
	private int initialHeight = 700;
	private Container content;
	private DefaultListModel model;
	private JList list;
	private JButton logout,back,mainmenu;
	private UserManagement UserManager;
	private RepositoryManagement RepManager;
	private ViewResultGUI last;
	public ResultGUI( UserManagement UM, RepositoryManagement RM, ViewResultGUI lastScreen )
	{
		RepManager = RM;
		UserManager = UM;
		last = lastScreen;
		//Initial size of frame.
		this.setSize(initialWidth, initialHeight);
		this.setResizable(true);
		//Title bar name.
		this.setTitle("Display Result");
		// default close operation
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		
		JPanel top = new JPanel(new BorderLayout());
		top.add(getInfo(),BorderLayout.WEST);
		top.add(getNav(),BorderLayout.EAST);

		//
		String[] ridings = RepManager.getRidingList();
		String Display = "";
		ReportGenerator x = new ReportGenerator( "dat/RidingRep.sqlite" );
		for(int i = 0; i < ridings.length ; i++){
			try{ Display = Display + x.viewResult( ridings[i] ); }
			catch(SQLException E){
				E.printStackTrace();
			}
		}
		JTextArea text = new JTextArea(Display );
		JScrollPane pane = new JScrollPane(text);
		pane.setPreferredSize(new Dimension(this.getWidth()-50,this.getHeight()-150));
		JPanel resultPanel = new JPanel();
		pane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		resultPanel.add(pane,BorderLayout.NORTH);
		content = getContentPane();
		content.add(top,BorderLayout.NORTH);
		content.add(resultPanel,BorderLayout.CENTER);
		//content.add(resultPanel,BorderLayout.SOUTH);
		
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
		e.gridx = 1;
		nav.add(back,e);
		//e.gridx = 1;
		//nav.add(mainmenu,e);
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
	
	@SuppressWarnings("deprecation")
	public void actionPerformed(ActionEvent a) {
		// TODO Auto-generated method stub
		
		if(a.getSource() == back){
			last.show();
			this.dispose();
		} else if (a.getSource() == mainmenu){
			last.show();
			this.dispose();
		} else if(a.getSource() == logout){
			UserManager.logoff();
			new LoginWindow( UserManager, RepManager);
			last.dispose();
			this.dispose();
		}
	}
}
