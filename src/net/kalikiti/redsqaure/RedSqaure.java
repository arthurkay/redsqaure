
package net.kalikiti.redsqaure;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.JTable;
import javax.swing.ImageIcon;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class RedSqaure extends JFrame {
	
	private JTextField addTextfield;
	private JTable table;
	private JButton pauseButton, resumeButton, cancelButton, clearButton, addButton;
	
	public RedSqaure() {
		//Set UI
		try {
			UIManager.setLookAndFeel(UIManager.getInstalledLookAndFeels()[1].getClassName());
		}
		catch(Exception err) {
			System.out.println("Look and feel issue: "+err.getMessage());
		}
		
		setTitle("RedSqaure Download Manager");
		setSize(740, 480);
		setIconImage(new ImageIcon(getClass().getResource("redsqaure.png")).getImage());
		JMenuBar menuBar = new JMenuBar();
		JMenu file = new JMenu("File");
		JMenu about = new JMenu("About");
		file.setMnemonic(KeyEvent.VK_F);
		about.setMnemonic(KeyEvent.VK_HOME);
		JMenuItem menuExit = new JMenuItem("Exit", KeyEvent.VK_X);
		JMenuItem aboutVersion = new JMenuItem("Info", KeyEvent.VK_A);
		
		//Add a menu bar to the user interface
		
		setJMenuBar(menuBar);
		
		//Adding options to the menu bar
		
		menuBar.add(file);
		menuBar.add(about);
		
		//Add menu items to the menu bar options
		
		file.add(menuExit);
		about.add(aboutVersion);
		
		//Main User Interface components
		
		JPanel addPanel = new JPanel();
		JPanel downloadsPanel = new JPanel();
		JPanel buttonPanel = new JPanel();
		
		//Downloads Panel coloring
		downloadsPanel.setBackground(Color.WHITE);
		
		//Adding panels with layouts to the User interface
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(addPanel, BorderLayout.NORTH);
		getContentPane().add(downloadsPanel, BorderLayout.CENTER);
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);
		
		//Top panel
		
		addTextfield = new JTextField(30);
		addPanel.add(addTextfield);
		addButton = new JButton("Add Download");
		addPanel.add(addButton);
		
		//Middle Panel
		table = new JTable(new TableModel());
		JScrollPane scrollPane = new JScrollPane(table);
		downloadsPanel.add(scrollPane);
		table.setAutoCreateRowSorter(true);
		table.setShowGrid(true);
		table.setGridColor(Color.BLACK);
		table.setOpaque(false);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		scrollPane.getViewport().setBackground(Color.WHITE);
		
		//Bottom Panel
		pauseButton = new JButton("Pause");
		buttonPanel.add(pauseButton);
		resumeButton = new JButton("Resume");
		buttonPanel.add(resumeButton);
		cancelButton = new JButton("Cancel");
		buttonPanel.add(cancelButton);
		clearButton = new JButton("Clear");
		buttonPanel.add(clearButton);
		
		
		//Setting up the table to hold files being downloaded
		
		

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				actionExit();
			}
		});
		
		menuExit.addActionListener(e -> {
			actionExit();
		});
		
		aboutVersion.addActionListener(e -> {
			JOptionPane.showMessageDialog(rootPane, " Developer: Arthur Kalikiti \n RedSqaure Download Manager \n Version: 0.0.2 \n E-Mail: arthurkalikiti@gmail.com"
		              + "\n Twitter: @ArthurKalikiti \n Website: http://redsqaure.rf.gd", "Information", JOptionPane.INFORMATION_MESSAGE);
		});
		
		addButton.addActionListener(e -> {
			addUrl();
		});
	}
	
	private void actionExit() {
		System.exit(0);
	}
	
	private void addUrl() {
		//Logic of adding urls for further file download processing
	}
	
	//Now run the App
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				RedSqaure rs = new RedSqaure();
				rs.setVisible(true);
			}
		});
	}
	
}
