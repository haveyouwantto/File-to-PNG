package hywt.conv;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.io.File;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;
import javax.swing.JProgressBar;

public class Window {
	
	public File selected;
	public static JProgressBar progressBar = new JProgressBar();
	public static JProgressBar progressBar1 = new JProgressBar();

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Window window = new Window();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Window() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		Runnable r = new Runnable(){
			   public void run(){
				   Main.toImage(selected);
			   }
			};
			
		frame = new JFrame();
		frame.setTitle("\u6587\u4EF6\u8F6C\u56FE\u7247");
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JTextArea fileInfo = new JTextArea();
		fileInfo.setBounds(10, 37, 414, 173);
		frame.getContentPane().add(fileInfo);
		
		JButton encode = new JButton("\u52A0\u5BC6");
		encode.setBounds(259, 4, 81, 23);
		encode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new Thread(r).start();
			}
		});
		frame.getContentPane().add(encode);
		
		JButton selectFile = new JButton("\u9009\u62E9\u6587\u4EF6");
		selectFile.setBounds(116, 4, 81, 23);
		selectFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser filec=new JFileChooser();
				filec.setMultiSelectionEnabled(false);
				filec.setCurrentDirectory(new File("."));
				int returnValue = filec.showOpenDialog(null);
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					selected=filec.getSelectedFile();
					fileInfo.setText(selected.getAbsolutePath()+"\n´óÐ¡£º"+selected.length()+"×Ö½Ú");
				}
			}
		});
		frame.getContentPane().add(selectFile);
		

		progressBar.setBounds(10, 235, 414, 16);
		progressBar.setStringPainted(true);
		frame.getContentPane().add(progressBar);
		

		progressBar1.setStringPainted(true);
		progressBar1.setBounds(10, 220, 414, 16);
		frame.getContentPane().add(progressBar1);
        
		

	}
}
