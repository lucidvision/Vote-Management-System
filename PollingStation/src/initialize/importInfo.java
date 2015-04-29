package initialize;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.*;
import java.sql.*;
import javax.swing.*;

@SuppressWarnings("serial")
public class importInfo extends JFrame implements ActionListener{
	private String infoLoc;
	Container content;
	
	public importInfo(String dest){
		infoLoc = dest;
		content = getContentPane();
		this.setTitle("Import Riding Info File");
		content.add(new JLabel("<html>No Riding Info File has been imported to intialize this station.<br>" +
				"Please locate a Riding Info File to initialize this polling station.</html>"), BorderLayout.CENTER);
		JButton locate = new JButton("Locate Riding Info File");
		locate.addActionListener(this);
		content.add(locate,BorderLayout.SOUTH);
		this.setSize(400, 200);
		Dimension scr = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation((int)(scr.getWidth()/2-this.getWidth()/2), (int)(scr.getHeight()/2-this.getHeight()/2));
		this.setVisible(true);
		
	}
	
	public void actionPerformed(ActionEvent a) {
		JFileChooser fc = new JFileChooser();
		if(fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION){
			checkFile(fc.getSelectedFile());
		} else {
			JOptionPane.showMessageDialog(this, "You did not selet a valid file.");
		}
		
	}
	
	private void copyFile(File source){
		File cop = new File(infoLoc);
		try{
		if(cop.createNewFile()){
			InputStream in = new FileInputStream(source);
			OutputStream out =  new FileOutputStream(cop);
			byte[] buffer = new byte[1024];
			int curr;
			while((curr = in.read(buffer))>0){
				out.write(buffer,0,curr);
			}
			int del = JOptionPane.showConfirmDialog(this, 
					"<html>Riding Info File Successfully Imported<br>" +
					"Would you like to delete the source file you used for import?</html>");
			if(del == JOptionPane.OK_OPTION){
				source.delete();
			}
			this.dispose();
		} else {
			JOptionPane.showMessageDialog(this, "<html>Could not create destination file at: <br>" +
					infoLoc);
			this.dispose();
		}
		} catch(IOException e){
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
	}
	
	private void checkFile(File file){
		try{
			Class.forName("org.sqlite.JDBC");
			try{
				Connection con = DriverManager.getConnection(
						"jdbc:sqlite:" + file.getAbsolutePath());
				String[] basics = { "riding_name", "num_voters", "num_seats", "num_candidates",
						"central"};
				String[] candidates = {"riding_name", "name","party"};
				String[] polls =  { "riding_name", "poll_num", "recounts", "poll_counted" };
				String[] poll_worked = { "riding_name", "poll_num", "ro_login" };
				Statement stat = con.createStatement();
				ResultSet rs = stat.executeQuery("PRAGMA table_info(basics)");
				for(int i = 0;i < basics.length;i++){
					rs.next();
					if(!basics[i].equals(rs.getString(2))){
						throw new SQLException("");
					}
				}
				
				rs = stat.executeQuery("PRAGMA table_info(candidates)");
				for(int i = 0;i < candidates.length;i++){
					rs.next();
					if(!candidates[i].equals(rs.getString(2))){
						throw new SQLException("");
					}
				}
				
				rs = stat.executeQuery("PRAGMA table_info(polls)");
				for(int i = 0;i < polls.length;i++){
					rs.next();
					if(!polls[i].equals(rs.getString(2))){
						throw new SQLException("");
					}
				}
				rs = stat.executeQuery("PRAGMA table_info(poll_worked)");
				for(int i = 0;i < poll_worked.length;i++){
					rs.next();
					if(!poll_worked[i].equals(rs.getString(2))){
						throw new SQLException("");
					}
				}
				copyFile(file);
			} catch (SQLException e) {
				JOptionPane.showMessageDialog(this, "<html>The proposed riding info file is not formatted correctly<br>" +
						"Therefore System can not be initilized</html>");
				this.dispose();	
			}
			} catch(ClassNotFoundException e){
				JOptionPane.showMessageDialog(null, 
						"<html>SQLite Driver not installed<br>" +
						"Please tech suppor for help</html>");
			}
		}
	}




