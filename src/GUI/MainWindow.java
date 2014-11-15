package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JTextPane;
import javax.swing.JEditorPane;

public class MainWindow implements DocumentListener {

	private Vector<String> list;
	private String FilePath;
	private boolean modifid;

	private JFrame frame;
	private JTextArea codeInput;
	private JTable registerTB;
	

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

	private void saveFile(File file) {
		FileWriter fw;
		try {
			fw = new FileWriter(file.getAbsoluteFile(), false);
			codeInput.write(fw);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void readFile(String path) {

		list = new Vector<String>();
		File file = new File(path);
		BufferedReader reader = null;

		try {
			reader = new BufferedReader(new FileReader(file));

			while (reader.ready()) {
				String line = reader.readLine();
				list.add(line);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {
			}
		}

		InitTextArea();
	}

	private void InitTextArea() {
		codeInput.setText("");
		for (String line : list) {
			codeInput.append(line + "\n");
		}
	}

	private String columnNames[];
	private String dataValues[][];

	private void initData() {
		CreateColumns();
		CreateData();
	}

	public void CreateColumns() {
		// Create column string labels
		columnNames = new String[2];

		for (int iCtr = 0; iCtr < 2; iCtr++)
			columnNames[iCtr] = "Col:" + iCtr;
	}

	public void CreateData() {
		// Create data for each element
		dataValues = new String[8][2];

		for (int iY = 0; iY < 8; iY++) {
			for (int iX = 0; iX < 2; iX++) {
				dataValues[iY][iX] = "" + iX + "," + iY;
			}
		}
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

		JLayeredPane layeredPane_1 = new JLayeredPane();
		bottomPart.addTab("New tab", null, layeredPane_1, null);

		JLayeredPane layeredPane_2 = new JLayeredPane();
		bottomPart.addTab("New tab", null, layeredPane_2, null);

		JPanel InputPanel = new JPanel();
		InputPanel.setBounds(6, 6, 761, 593);
		frame.getContentPane().add(InputPanel);
		InputPanel.setLayout(new BoxLayout(InputPanel, BoxLayout.X_AXIS));

		codeInput = new JTextArea();
		codeInput.setColumns(1);
		codeInput.getDocument().addDocumentListener(this);

		InputPanel.add(codeInput);

		JScrollPane scroll = new JScrollPane(codeInput,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

		InputPanel.add(scroll);

		JPanel debuging = new JPanel();
		debuging.setBounds(771, 112, 246, 488);
		frame.getContentPane().add(debuging);
		debuging.setLayout(new BorderLayout(0, 0));

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		debuging.add(tabbedPane);

		JLayeredPane RegisterPane = new JLayeredPane();
		tabbedPane.addTab("Registers", null, RegisterPane, null);
		initData();
		registerTB = new JTable(dataValues, columnNames);
		registerTB.setBounds(6, 6, 213, 430);
		// registerTB.add
		JScrollPane tableScroller = new JScrollPane(registerTB,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		RegisterPane.add(tableScroller);
		RegisterPane.add(registerTB);

		JLayeredPane CashePane = new JLayeredPane();
		tabbedPane.addTab("Cashes", null, CashePane, null);

		JPanel OptionsPan = new JPanel();
		OptionsPan.setBounds(771, 5, 246, 39);
		frame.getContentPane().add(OptionsPan);
		OptionsPan.setLayout(new GridLayout(1, 0, 0, 0));

		JButton btnNewButton = new JButton("Load");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter(
						"TEXT-file", "txt");
				chooser.setFileFilter(filter);
				int returnVal = chooser.showOpenDialog(frame);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					// System.out.println("You chose to open this file: "
					// + chooser.getSelectedFile().getPath());
					FilePath = chooser.getSelectedFile().getPath();
					readFile(FilePath);
				}
			}
		});
		OptionsPan.add(btnNewButton);

		JButton btnNewButton_2 = new JButton("Save");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				File file = new File(FilePath);
				if (file != null)
					saveFile(file);
			}
		});
		OptionsPan.add(btnNewButton_2);

		JButton btnNewButton_1 = new JButton("Run");
		OptionsPan.add(btnNewButton_1);

		JPanel panel = new JPanel();
		panel.setBounds(771, 56, 246, 56);
		frame.getContentPane().add(panel);
	}

	@Override
	public void changedUpdate(DocumentEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void insertUpdate(DocumentEvent arg0) {
		modifid = true;
		JOptionPane.showMessageDialog(frame,
				"Eggs are not supposed to be green.");
	}

	@Override
	public void removeUpdate(DocumentEvent arg0) {
		// TODO Auto-generated method stub
	}
}
