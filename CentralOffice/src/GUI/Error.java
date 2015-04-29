package GUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import javax.swing.*;


public class Error extends JFrame implements ActionListener{
	
	private static final long serialVersionUID = 1L;
	private int initialWidth = 500;
	private int initialHeight = 200;
	Container content;
	JButton ok;
	JLabel[] msg;
	JPanel panel1, panel2;

	
	public Error(String[] err){
		this.setLocationRelativeTo(null);
		this.setSize(initialWidth, initialHeight);
		this.setTitle("Error");
		
		msg = new JLabel[err.length];
		for(int i = 0; i < err.length;i++){
			msg[i] = new JLabel();
			msg[i].setText(err[i]);
		}
				
		ok = new JButton();
		ok.setText("ok");
		
		panel1 = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(5, 5, 5, 5);
		c.fill = GridBagConstraints.VERTICAL ;
		for(int i = 0; i < err.length;i++){
			c.gridx = 0; c.gridy = i;
			panel1.add(msg[i], c);
		}
		
		panel2 = new JPanel(new GridBagLayout());
		GridBagConstraints d = new GridBagConstraints();
		d.insets = new Insets(5, 5, 5, 5);
		d.fill = GridBagConstraints.VERTICAL ;
		
		d.gridx=0; d.gridy = 0;
		panel2.add(ok,c);
		content = getContentPane();
		content.add(panel1,BorderLayout.NORTH);
		content.add(panel2,BorderLayout.CENTER);
		// action
		
		ok.addActionListener(this);
		this.setVisible(true);
		
	}

	@Override
	public void actionPerformed(ActionEvent a) {
		this.dispose();
	}
}
	