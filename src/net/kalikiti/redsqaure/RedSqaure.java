
package net.kalikiti.redsqaure;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

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
import javax.swing.JPopupMenu;
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
	private JPopupMenu contextMenu;
	private Clipboard clipboard;
	private JMenuItem cut;
	private JMenuItem copy;
	private JMenuItem paste;
	private JMenuItem close;
	
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
		setLocationRelativeTo(null);
		setIconImage(new ImageIcon("images/redsqaure.png").getImage());
		JMenuBar menuBar = new JMenuBar();
		JMenu file = new JMenu("File");
		JMenu about = new JMenu("About");
		file.setMnemonic(KeyEvent.VK_F);
		about.setMnemonic(KeyEvent.VK_HOME);
		JMenuItem menuExit = new JMenuItem("Exit", KeyEvent.VK_X);
		JMenuItem aboutVersion = new JMenuItem("Info", KeyEvent.VK_A);

		//Context Menu Options
		contextMenu = new JPopupMenu("Options Menu");
		
		copy = new JMenuItem("Copy ");
		cut = new JMenuItem("Cut ");
		paste = new JMenuItem("Paste ");
		close = new JMenuItem("Close");

		contextMenu.add(copy);
		contextMenu.add(cut);
		contextMenu.add(paste);
		contextMenu.addSeparator();
		contextMenu.add(close);

		
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
		
		// Attach a mouse listener to textfield
		addTextfield.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				popUpMenu(evt);
				}
		});
		
		menuExit.addActionListener(e -> {
			actionExit();
		});

		copy.addActionListener(ae -> {
			String txt = addTextfield.getSelectedText();
			StringSelection strTxt = new StringSelection(txt);
			clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			clipboard.setContents(strTxt, strTxt);
		});

		cut.addActionListener(ae -> {
			String str = addTextfield.getText();
			String selectedStr = addTextfield.getSelectedText();
			String newStr = str.replace(selectedStr, "");
			addTextfield.setText(newStr);

			// Now add the new value to system clipboard
			StringSelection strData = new StringSelection(selectedStr);
			clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			clipboard.setContents(strData, strData);
			
		});

		paste.addActionListener(ae -> {
			clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			Transferable trans = clipboard.getContents(null);
			String currentStr = addTextfield.getText();
			try {
			if (trans.isDataFlavorSupported(DataFlavor.stringFlavor)) {
				addTextfield.setText(trans.getTransferData(DataFlavor.stringFlavor).toString());
			}
			else {
				paste.setEnabled(false);
			}
		}
		catch(UnsupportedFlavorException | IOException err) {
			System.out.println("Error: "+err.getMessage());
		}
		});
		
		aboutVersion.addActionListener(e -> {
			JOptionPane.showMessageDialog(rootPane, " Developer: Arthur Kalikiti \n RedSqaure Download Manager \n Version: 0.0.2 \n E-Mail: arthur@kalikiti.net"
		              + "\n Twitter: @ArthurKalikiti \n Website: https://arthur.kalikiti.net", "Information", JOptionPane.INFORMATION_MESSAGE);
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

	public void popUpMenu(MouseEvent evt) {
			contextMenu.show(evt.getComponent(), evt.getX(), evt.getY());
			clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

				Transferable tData = clipboard.getContents(null);
				if (tData.isDataFlavorSupported(DataFlavor.stringFlavor)) {
					paste.setEnabled(true);
					close.setEnabled(false);
				}
				else {
					paste.setEnabled(false);
					close.setEnabled(false);
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
