
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
import java.net.URL;
import java.net.MalformedURLException;
import java.util.Observer;
import java.util.Observable;

public class RedSqaure extends JFrame implements Observer {
	
	private JTextField addTextfield;
	private JTable table;
	private JButton pauseButton, resumeButton, cancelButton, clearButton, addButton;
	DownloadsTableModel tableModel = new DownloadsTableModel();
	private Download selectedDownload;
	private boolean clearing;
	
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

		table = new JTable(tableModel);
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
		pauseButton.setEnabled(false);
		buttonPanel.add(pauseButton);
		resumeButton = new JButton("Resume");
		resumeButton.setEnabled(false);
		buttonPanel.add(resumeButton);
		cancelButton = new JButton("Cancel");
		cancelButton.setEnabled(false);
		buttonPanel.add(cancelButton);
		clearButton = new JButton("Clear");
		clearButton.setEnabled(false);
		buttonPanel.add(clearButton);
		
		//Functionality declarations 
		
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
		URL validUrl = verifyUrl(addTextfield.getText().toString());
		if (validUrl != null) {
			tableModel.addDownload(new Download(validUrl));
			addTextfield.setText("");
		}
		else {
			JOptionPane.showMessageDialog(this, "Invalid URL address!", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private URL verifyUrl(String url) {
		if (!url.toLowerCase().startsWith("http://")) {
			if (!url.toLowerCase().startsWith("https://"))
			return null;
		}
		
		URL validUrl = null;
		try {
			validUrl = new URL(url);
			}
			catch (MalformedURLException err) {
				System.out.println(err.getMessage());
				return null;
			}
			
		if (validUrl.getFile().length() < 2)
			return null;
		return validUrl;
	}
	
	public void tableSelectionChanged() {
		if (selectedDownload != null) 
			selectedDownload.deleteObserver(RedSqaure.this);
			
			if (!clearing && table.getSelectedRow() > -1) {
				selectedDownload = tableModel.getDownload(table.getSelectedRow());
				selectedDownload.addObserver(RedSqaure.this);
				updateButtons();
			}
	}
	
	private void actionPause() {
		selectedDownload.pause();
		updateButtons();
	}
	
	private void actionResume() {
		selectedDownload.resume();
		updateButtons();
	}
	
	private void actionCancel() {
		selectedDownload.cancel();
		updateButtons();
	}
	
	private void actionClear() {
		clearing = true;
		tableModel.clearDownload(table.getSelectedRow());
		selectedDownload = null;
		updateButtons();
	}
	
	public void update(Observable o, Object arg) {
		if (selectedDownload != null && selectedDownload.equals(o))
			updateButtons();
	}
	
	public void updateButtons() {
		if (selectedDownload != null) {
			int status = selectedDownload.getStatus();
			switch (status) {
			case Download.DOWNLOADING:
				pauseButton.setEnabled(true);
				resumeButton.setEnabled(false);
				cancelButton.setEnabled(true);
				clearButton.setEnabled(false);
			case Download.PAUSED:
				pauseButton.setEnabled(false);
				resumeButton.setEnabled(true);
				cancelButton.setEnabled(false);
				clearButton.setEnabled(false);
			case Download.ERROR:
				pauseButton.setEnabled(false);
				resumeButton.setEnabled(true);
				cancelButton.setEnabled(false);
				clearButton.setEnabled(true);
			default:
				pauseButton.setEnabled(false);
				resumeButton.setEnabled(false);
				cancelButton.setEnabled(false);
				clearButton.setEnabled(true);
			}
		}
		else {
			pauseButton.setEnabled(false);
			resumeButton.setEnabled(false);
			cancelButton.setEnabled(false);
			clearButton.setEnabled(false);
		}
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
