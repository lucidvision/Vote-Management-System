package GUI;
import initialize.Controller;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;


@SuppressWarnings("serial")
public class ShowResults extends JFrame implements ActionListener {
	private int initialWidth = 600;
	private int initialHeight = 600;
	
	JButton logout, back;
	Container content;
	
	int[] poll;
	
	private Controller ctlr;
	
	
	public ShowResults( Controller con ) {
		ctlr = con;
		
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(initialWidth, initialHeight);
		this.setTitle("Select Poll for Ballot Listing File");
		
		JPanel top = new JPanel();
		top.setPreferredSize(new Dimension( this.getWidth(), 50));
		top.add(new JLabel("On-Going Election Results: "), BorderLayout.WEST);
		top.add(getNav(),BorderLayout.EAST);
		
		content = getContentPane();
		content.add(top,BorderLayout.NORTH);
		content.add(getRes(), BorderLayout.CENTER);
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
		nav.add(logout,BorderLayout.WEST);
		nav.add(back,BorderLayout.EAST);
		back.addActionListener(this);
		logout.addActionListener(this);
		return nav;
	}
	
	private JPanel getRes(){
		String[] elected = ctlr.getCentral().getElected();
		String[] predicted = ctlr.getCentral().getPredicted();
		String[] results = new String[3 + elected.length + predicted.length];
		results[0] = "Results for Riding: "+ ctlr.getPollStat().getName() + "\n";
		results[1] = "Candidates that have won seats in this riding:\n";
		for(int i = 0; i < elected.length;i++){
			results[2+i] = "\t\t"+elected[i]+"\n";
		}
		results[2+elected.length] = "Candidates predicted to win a seats in this riding:\n";
		for(int i = 0; i < predicted.length;i++){
			results[3+i+elected.length] = "\t\t"+predicted[i]+"\n";
		}
		JScrollPane center = new JScrollPane();
		center.setPreferredSize(new Dimension(550,400));
		center.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		String res = "";
		for(int j = 0; j < results.length;j++){
			res = res + results[j];
		}
		JTextArea result = new JTextArea( res );
		center.getViewport().add(result);
		JPanel pollPan = new JPanel();
		pollPan.add(center,BorderLayout.CENTER);
		return pollPan;
		
	}

	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == logout ){
			ctlr.getUserMan().logoff();
			ctlr.begin();
			this.dispose();
		} else if( e.getSource() == back){
			new elecFiles( ctlr );
			this.dispose();
		}
	}

	
	
}
