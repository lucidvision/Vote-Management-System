package GUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.*;
import ManageRepository.riding;
import java.io.File;
import java.io.IOException;

public class LocateMap extends JFrame implements ActionListener {
	
	private static final long serialVersionUID = 1L;
	private int initialWidth = 500;
	private int initialHeight = 300;
	private RepManage RM;
	JButton back, ccr, locate;
	JPanel panel1, panel2;
	JLabel label1, label2;
	JTextField location;
	JScrollPane sp;
	BufferedImage map = null;
	Container content;
	riding forRiding;
	JLabel ml;
	JScrollPane ms = new JScrollPane();
	JFileChooser fc = new JFileChooser();

	public LocateMap( riding ride , RepManage last ) {
		RM = last;
		forRiding = ride;
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(initialWidth, initialHeight);
		this.setTitle("Locate Map");
		
		
		label1 = new JLabel();
		label1.setText("Locate map file for riding:"+ forRiding.getRidingName());
		
		label2 = new JLabel();
		label2.setText("Location Selected: ");
		
		location = new JTextField(50);
		location.setText(forRiding.getMap());
		
		back = new JButton("Back");
		ccr = new JButton("Continue Creating Riding");
		locate = new JButton("Locate Map");
		
		
		
		panel1 = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(5, 5, 5, 5);
		c.fill = GridBagConstraints.HORIZONTAL ;
		c.ipadx = 100;
		c.gridx = 0; c.gridy = 0;
		panel1.add(label1, c);
		
		c.gridx = 0; c.gridy = 1;
		panel1.add(label2,c);
		c.gridx = 1; c.gridy = 1;
		panel1.add(location,c);
		c.gridx = 1; c.gridy = 0;
		panel1.add(locate, c);
		
		panel2 = new JPanel(new GridBagLayout());
		panel2.setVisible(true);
		panel1.setVisible(true);
		GridBagConstraints d = new GridBagConstraints();
		d.insets = new Insets(5, 5, 5, 5);

		
		
		d.gridx = 3; d.gridy = 0;
		panel2.add(back, d);
		d.gridx = 4; d.gridy = 0;
		panel2.add(ccr, d);
		
		
		back.addActionListener(this);
		ccr.addActionListener(this);
		locate.addActionListener(this);
		content = getContentPane();
		content.add(panel1,BorderLayout.NORTH);
		content.add(panel2,BorderLayout.SOUTH);
		ms.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		if(forRiding.getMap() != null){
			try{
				File Map = new File(forRiding.getMap());
				map = ImageIO.read( Map );
				ml = new JLabel( new ImageIcon( map ) );
				location.setText( Map.getAbsolutePath());
				ms.getViewport().add(ml);					
			} catch(IOException E){
				E.printStackTrace();
				JOptionPane.showMessageDialog(this, "Stored Map Location could not be is not a valid image");
			}
			
		}
		content.add( ms, BorderLayout.CENTER );
		Dimension scr = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(scr.width/2-this.getWidth()/2,scr.height/2-this.getHeight()/2);
		this.setVisible(true);

	}


	
	private boolean saveInfo(){
		boolean got = true;
		forRiding.setMap(location.getText());
		return got;
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() ==  back){
			new EnterIncumbent(forRiding , RM );
			this.dispose();
		} else if(e.getSource() == ccr){
			if(saveInfo()){
				RM.AddEdit( forRiding );
				RM.launchanew();
				this.dispose();
			}
		} else if(e.getSource() == locate){
			int ret;
			
			ret = fc.showOpenDialog( null );
			if(ret == JFileChooser.APPROVE_OPTION){
				File Map = fc.getSelectedFile();
				try{
					map = ImageIO.read( Map );
					forRiding.setMap(Map.getAbsolutePath());
					new LocateMap( forRiding, RM );
					this.dispose();				
				} catch(IOException E){
					JOptionPane.showMessageDialog(this, "You did not select and image file");
				}
				location.setText(Map.getAbsolutePath());
				
			}
		}
		
	}

}
	
