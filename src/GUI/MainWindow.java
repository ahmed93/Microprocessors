package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

public class MainWindow {

	private JFrame frame;
	private JTable table;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
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
	public MainWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 1023, 801);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JTabbedPane bottomPart = new JTabbedPane(JTabbedPane.TOP);
		bottomPart.setBounds(0, 598, 1023, 181);
		frame.getContentPane().add(bottomPart);
		
		JLayeredPane consolePannel = new JLayeredPane();
		bottomPart.addTab("Console", null, consolePannel, null);
		
		TextArea console = new TextArea();
		console.setBackground(Color.WHITE);
		console.setEnabled(false);
		console.setColumns(100);
		console.setRows(100);
		console.setEditable(false);
		console.setBounds(0, 0, 1002, 135);
		consolePannel.add(console);
		
		JLayeredPane layeredPane_1 = new JLayeredPane();
		bottomPart.addTab("New tab", null, layeredPane_1, null);
		
		JLayeredPane layeredPane_2 = new JLayeredPane();
		bottomPart.addTab("New tab", null, layeredPane_2, null);
		
		JPanel InputPanel = new JPanel();
		InputPanel.setBounds(6, 6, 761, 593);
		frame.getContentPane().add(InputPanel);
		InputPanel.setLayout(new BoxLayout(InputPanel, BoxLayout.X_AXIS));
		
		TextArea textArea = new TextArea();
		InputPanel.add(textArea);
		
		JPanel debuging = new JPanel();
		debuging.setBounds(771, 78, 246, 522);
		frame.getContentPane().add(debuging);
		debuging.setLayout(new BorderLayout(0, 0));
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		debuging.add(tabbedPane);
		
		JLayeredPane layeredPane = new JLayeredPane();
		tabbedPane.addTab("New tab", null, layeredPane, null);
		
		table = new JTable();
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		table.setBounds(6, 472, 213, -446);
		layeredPane.add(table);
		
		JLayeredPane layeredPane_3 = new JLayeredPane();
		tabbedPane.addTab("New tab", null, layeredPane_3, null);
		
		JPanel OptionsPan = new JPanel();
		OptionsPan.setBounds(771, 15, 246, 39);
		frame.getContentPane().add(OptionsPan);
		OptionsPan.setLayout(new GridLayout(1, 0, 0, 0));
		
		JButton btnNewButton = new JButton("Open");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("txt");
				chooser.setFileFilter(filter);
				int returnVal = chooser.showOpenDialog(frame);
				if(returnVal == JFileChooser.APPROVE_OPTION) {
				   System.out.println("You chose to open this file: " +
				        chooser.getSelectedFile().getName());
				}
			}
		});
		OptionsPan.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("Run");
		OptionsPan.add(btnNewButton_1);
		
		JButton btnNewButton_2 = new JButton("New button");
		OptionsPan.add(btnNewButton_2);
	}
	private static void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}
}
