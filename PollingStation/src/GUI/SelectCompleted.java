package GUI;
import initialize.Controller;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;


@SuppressWarnings("serial")
public class SelectCompleted extends JFrame implements ActionListener {
	private int initialWidth = 600;
	private int initialHeight = 350;
	
	JButton enterPolls, logout, back; 	JList polls;
	Container content;
	
	int[] poll;
	
	private Controller ctlr;
	
	
	public SelectCompleted( Controller con ) {
		ctlr = con;
		
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(initialWidth, initialHeight);
		this.setTitle("Select Poll for Ballot Listing File");
		
		
		
		content = getContentPane();
		content.add(getNav(),BorderLayout.NORTH);
		content.add(getPoll(), BorderLayout.CENTER);
		content.add(getMenu(),BorderLayout.SOUTH);
		Dimension scr = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(scr.width/2-this.getWidth()/2,scr.height/2-this.getHeight()/2);
		this.setVisible(true);
	}
		
	/**
	 * getNav() returns a JPanel a with JButtons back, mainmenu and
	 * logout with ActionListeners attached to each.
	 */
	public JPanel getNav(){
		logout = new JButton("Log Out");
		back = new JButton("Back");
		JPanel nav = new JPanel();
		nav.add(logout,BorderLayout.EAST);
		nav.add(back,BorderLayout.WEST);
		back.addActionListener(this);
		logout.addActionListener(this);
		return nav;
	}
	
	private JPanel getPoll(){
		poll = ctlr.getPollStat().completedPolls();
		String[] pol = new String[poll.length];
		for(int i = 0; i < poll.length; i++){
			pol[i] = Integer.toString(poll[i]);
			
		}
		polls = new JList( pol );
		polls.setSelectedIndex(0);
		JScrollPane center = new JScrollPane();
		center.setPreferredSize(new Dimension(200,150));
		center.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		center.getViewport().add(polls);
		JPanel pollPan = new JPanel();
		pollPan.add(new JLabel("Completed Poll Numbers: "),BorderLayout.NORTH);
		pollPan.add(center,BorderLayout.CENTER);
		return pollPan;
		
	}
	private JPanel getMenu(){
		enterPolls = new JButton("<html><center>Create Ballot Listing File<br>for Selected Poll</html>");
		enterPolls.addActionListener(this);
		
		JPanel menu = new JPanel();
		menu.add(enterPolls,BorderLayout.SOUTH);
		return menu;
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == logout ){
			ctlr.getUserMan().logoff();
			ctlr.begin();
			this.dispose();
		} else if (e.getSource() == enterPolls){
			try {
					JFileChooser fc = new JFileChooser();
					if(fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION){
						ctlr.getPollStat().createBallotList(fc.getSelectedFile(), poll[polls.getSelectedIndex()]);
						new elecFiles( ctlr );
						this.dispose();
					} else {
						JOptionPane.showMessageDialog(this, "You did not select a valid file.");
					}
			} catch (Exception e1) {
				JOptionPane.showMessageDialog(this, e1.getMessage());
			}
		} else if( e.getSource() == back){
			new elecFiles( ctlr );
			this.dispose();
		}
	}

	
	
}
