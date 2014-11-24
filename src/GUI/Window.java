package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;
import javax.swing.text.PlainDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import org.eclipse.wb.swing.FocusTraversalOnArray;

import simulator.Simulator;
import GUI.utilities.NumbersFilter;

public class Window {

	private JFrame frame;
	private JTextPane codeInput;
	private JTextPane consoleTP;
	private JTable registerTB;
	private JButton loadBT, saveBT, runBT;
	private JPanel RegistersPanel;
	private DefaultTableModel dataModel;
	private JTable memoryTB;
	private JComboBox cacheLevelsCB, Miss1CB, Miss2CB, Miss3CB, Hit1CB, Hit2CB,
			Hit3CB;
	private JTextField startAdressTF, l2CashSizeTF, l2BlockLengthTF,
			l2AssociativityTF, l1CashSizeTF, l1BlockLengthTF,
			l1AssociativityTF, l3CashSizeTF, l3BlockLengthTF,
			l3AssociativityTF;

	/****************************
	 ** Data Variables **
	 ****************************/
	private boolean modified;
	private String FilePath;
	private Simulator simulator;
	private String columnNames[] = { "register", "values" };
	private String dataValues[][];
	private Vector<String> data = new Vector<>();
	private Vector<String> instructions = new Vector<>();

	/*************************
	 ** Static Variables **
	 *************************/
	private static final String NUMBERS_ONLY_REGIX = "[0-9]+";
	private static final String FILE_TYPE_Viewed = "TEXT-File";
	private static final String FILE_TYPE = "txt";
	private Vector<String> HITPOLISYS = new Vector<String>();
	private Vector<String> MISSPOLISYS = new Vector<String>();

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
		HITPOLISYS.add("WB");
		HITPOLISYS.add("WT");
		MISSPOLISYS.add("WA");
		MISSPOLISYS.add("WL");
		/*************************************
		 ** Initializing the Frame **
		 *************************************/
		frame = new JFrame();
		frame.setBounds(100, 100, 1280, 800);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		/*******************************************
		 ** JTabbedPane: Tabs-Console **
		 *******************************************/
		createConsolePanel();

		/**********************************
		 ** InputPanel: CodeInput **
		 **********************************/
		createInputPanel();

		/*****************************************************
		 ** DebuggingPanel: Registers/Caches/.. **
		 *****************************************************/
		createRegisterPanel();

		createOptionPanel();
	}

	/*************************************
	 ** Buttons Settings Options **
	 *************************************/
	private void createOptionPanel() {
		JPanel OptionsPanel = new JPanel();
		OptionsPanel.setBounds(741, 5, 533, 39);
		frame.getContentPane().add(OptionsPanel);

		// Load Button
		loadBT = new JButton("Load");
		loadBT.setBounds(6, 4, 85, 29);
		loadBT.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter(
						FILE_TYPE_Viewed, FILE_TYPE);
				chooser.setFileFilter(filter);
				int returnVal = chooser.showOpenDialog(frame);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					FilePath = chooser.getSelectedFile().getPath();
					readFile(FilePath);
				}
			}
		});
		OptionsPanel.setLayout(null);
		OptionsPanel.add(loadBT);

		// Save Button
		saveBT = new JButton("Save");
		saveBT.setBounds(103, 4, 85, 29);
		saveBT.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				onClickSaveBT();
			}
		});
		OptionsPanel.add(saveBT);

		JButton debugBT = new JButton("Debug");
		debugBT.setBounds(202, 4, 85, 29);
		debugBT.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dataModel.setValueAt("4", 2, 1);
				System.out.println(dataModel.getValueAt(2, 1));
				
				
			}
		});
		OptionsPanel.add(debugBT);

		// Run Button
		runBT = new JButton("Run");
		runBT.setBounds(406, 4, 85, 29);
		runBT.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				runBT.setEnabled(false);
				onClickrunBT();
				runBT.setEnabled(true);
			}
		});
		OptionsPanel.add(runBT);

		JButton stioBT = new JButton("Stop");
		stioBT.setBounds(309, 3, 85, 30);
		stioBT.setEnabled(false);
		OptionsPanel.add(stioBT);
		OptionsPanel.setFocusTraversalPolicy(new FocusTraversalOnArray(
				new Component[] { loadBT, saveBT, runBT }));

		Panel MemoryPanel = new Panel();
		MemoryPanel.setBounds(1025, 50, 245, 518);
		frame.getContentPane().add(MemoryPanel);
		MemoryPanel.setLayout(new BorderLayout(0, 0));

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		MemoryPanel.add(tabbedPane, BorderLayout.CENTER);

		JLayeredPane MemoryPane = new JLayeredPane();
		tabbedPane.addTab("Memory", null, MemoryPane, null);

		memoryTB = new JTable();
		memoryTB.setBounds(6, 6, 214, 460);
		MemoryPane.add(memoryTB);

		JTabbedPane SettingsTabbedPane = new JTabbedPane(JTabbedPane.TOP);
		SettingsTabbedPane.setBounds(741, 50, 276, 556);
		frame.getContentPane().add(SettingsTabbedPane);

		JLayeredPane layeredPane = new JLayeredPane();
		SettingsTabbedPane.addTab("Settings", null, layeredPane, null);

		startAdressTF = new JTextField();
		startAdressTF.setColumns(10);
		startAdressTF.setBounds(141, 6, 108, 28);
		PlainDocument doc = (PlainDocument) startAdressTF.getDocument();
		doc.setDocumentFilter(new NumbersFilter());

		startAdressTF.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (startAdressTF.getText().matches(NUMBERS_ONLY_REGIX)) {

				}
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}
		});
		layeredPane.add(startAdressTF);

		JLabel label = new JLabel("Start Address");
		label.setBounds(6, 12, 93, 16);
		layeredPane.add(label);

		JLabel label_2 = new JLabel("Cache Settings");
		label_2.setHorizontalAlignment(SwingConstants.CENTER);
		label_2.setBounds(72, 40, 112, 16);
		layeredPane.add(label_2);

		Vector<String> items = new Vector<>();
		items.add("none");
		items.add("one");
		items.add("two");
		items.add("three");
		cacheLevelsCB = new JComboBox(items);
		cacheLevelsCB.setBounds(156, 58, 93, 27);
		cacheLevelsCB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				switch (cacheLevelsCB.getSelectedIndex()) {
				case 1:
					EnableCacheLevel(1, true);
					EnableCacheLevel(2, false);
					EnableCacheLevel(3, false);
					break;
				case 2:
					EnableCacheLevel(1, true);
					EnableCacheLevel(2, true);
					EnableCacheLevel(3, false);
					break;
				case 3:
					EnableCacheLevel(1, true);
					EnableCacheLevel(2, true);
					EnableCacheLevel(3, true);
					break;
				default:
					EnableCacheLevel(1, false);
					EnableCacheLevel(2, false);
					EnableCacheLevel(3, false);
					break;
				}
			}
		});

		layeredPane.add(cacheLevelsCB);

		JLabel label_3 = new JLabel("Number Of Levels");
		label_3.setBounds(6, 62, 121, 16);
		layeredPane.add(label_3);

		JPanel panel_1 = new JPanel();
		panel_1.setLayout(null);
		panel_1.setBounds(6, 226, 243, 132);
		layeredPane.add(panel_1);

		JLabel label_4 = new JLabel("L2-Cache");
		label_4.setBounds(6, 6, 69, 16);
		panel_1.add(label_4);

		l2CashSizeTF = new JTextField();
		l2CashSizeTF.setEnabled(false);
		l2CashSizeTF.setColumns(10);
		l2CashSizeTF.setBounds(94, 20, 113, 28);
		panel_1.add(l2CashSizeTF);

		l2BlockLengthTF = new JTextField();
		l2BlockLengthTF.setEnabled(false);
		l2BlockLengthTF.setColumns(10);
		l2BlockLengthTF.setBounds(94, 44, 113, 28);
		panel_1.add(l2BlockLengthTF);

		l2AssociativityTF = new JTextField();
		l2AssociativityTF.setEnabled(false);
		l2AssociativityTF.setColumns(10);
		l2AssociativityTF.setBounds(94, 71, 113, 28);
		panel_1.add(l2AssociativityTF);

		JLabel label_5 = new JLabel("Cache Size");
		label_5.setBounds(6, 26, 89, 16);
		panel_1.add(label_5);

		JLabel label_6 = new JLabel("Block Length");
		label_6.setBounds(6, 50, 89, 16);
		panel_1.add(label_6);

		JLabel label_7 = new JLabel("Associativity");
		label_7.setBounds(6, 77, 89, 16);
		panel_1.add(label_7);

		JLabel label_1 = new JLabel("HitP");
		label_1.setBounds(6, 104, 26, 16);
		panel_1.add(label_1);

		Hit2CB = new JComboBox(HITPOLISYS);
		Hit2CB.setBounds(37, 101, 76, 25);
		Hit2CB.setEnabled(false);
		panel_1.add(Hit2CB);

		Miss2CB = new JComboBox(MISSPOLISYS);
		Miss2CB.setBounds(155, 102, 82, 23);
		Miss2CB.setEnabled(false);
		panel_1.add(Miss2CB);

		JLabel label_16 = new JLabel("MissP");
		label_16.setBounds(114, 104, 42, 16);
		panel_1.add(label_16);

		JPanel panel_2 = new JPanel();
		panel_2.setLayout(null);
		panel_2.setBounds(6, 90, 243, 132);
		layeredPane.add(panel_2);

		JLabel label_8 = new JLabel("L1-Cache");
		label_8.setBounds(6, 6, 69, 16);
		panel_2.add(label_8);

		l1CashSizeTF = new JTextField();
		l1CashSizeTF.setEnabled(false);
		l1CashSizeTF.setColumns(10);
		l1CashSizeTF.setBounds(94, 20, 113, 28);
		panel_2.add(l1CashSizeTF);

		l1BlockLengthTF = new JTextField();
		l1BlockLengthTF.setEnabled(false);
		l1BlockLengthTF.setColumns(10);
		l1BlockLengthTF.setBounds(94, 44, 113, 28);
		panel_2.add(l1BlockLengthTF);

		l1AssociativityTF = new JTextField();
		l1AssociativityTF.setEnabled(false);
		l1AssociativityTF.setColumns(10);
		l1AssociativityTF.setBounds(94, 71, 113, 28);
		panel_2.add(l1AssociativityTF);

		JLabel label_9 = new JLabel("Cache Size");
		label_9.setBounds(6, 26, 89, 16);
		panel_2.add(label_9);

		JLabel label_10 = new JLabel("Block Length");
		label_10.setBounds(6, 50, 89, 16);
		panel_2.add(label_10);

		JLabel label_11 = new JLabel("Associativity");
		label_11.setBounds(6, 77, 89, 16);
		panel_2.add(label_11);

		JLabel lblHitp = new JLabel("HitP");
		lblHitp.setBounds(6, 105, 26, 16);
		panel_2.add(lblHitp);

		Hit1CB = new JComboBox(HITPOLISYS);
		Hit1CB.setBounds(37, 102, 76, 25);
		Hit1CB.setEnabled(false);
		panel_2.add(Hit1CB);

		Miss1CB = new JComboBox(MISSPOLISYS);
		Miss1CB.setBounds(155, 103, 82, 23);
		Miss1CB.setEnabled(false);
		panel_2.add(Miss1CB);

		JLabel lblMissp = new JLabel("MissP");
		lblMissp.setBounds(114, 105, 42, 16);
		panel_2.add(lblMissp);

		JPanel panel_3 = new JPanel();
		panel_3.setLayout(null);
		panel_3.setBounds(6, 361, 243, 132);
		layeredPane.add(panel_3);

		JLabel label_12 = new JLabel("L3-Cache");
		label_12.setBounds(6, 6, 69, 16);
		panel_3.add(label_12);

		l3CashSizeTF = new JTextField();
		l3CashSizeTF.setEnabled(false);
		l3CashSizeTF.setColumns(10);
		l3CashSizeTF.setBounds(94, 20, 113, 28);
		panel_3.add(l3CashSizeTF);

		l3BlockLengthTF = new JTextField();
		l3BlockLengthTF.setEnabled(false);
		l3BlockLengthTF.setColumns(10);
		l3BlockLengthTF.setBounds(94, 44, 113, 28);
		panel_3.add(l3BlockLengthTF);

		l3AssociativityTF = new JTextField();
		l3AssociativityTF.setEnabled(false);
		l3AssociativityTF.setColumns(10);
		l3AssociativityTF.setBounds(94, 71, 113, 28);
		panel_3.add(l3AssociativityTF);

		JLabel label_13 = new JLabel("Cache Size");
		label_13.setBounds(6, 26, 89, 16);
		panel_3.add(label_13);

		JLabel label_14 = new JLabel("Block Length");
		label_14.setBounds(6, 50, 89, 16);
		panel_3.add(label_14);

		JLabel label_15 = new JLabel("Associativity");
		label_15.setBounds(6, 77, 89, 16);
		panel_3.add(label_15);

		JLabel label_17 = new JLabel("HitP");
		label_17.setBounds(6, 104, 26, 16);
		panel_3.add(label_17);

		Hit3CB = new JComboBox(HITPOLISYS);
		Hit3CB.setBounds(37, 101, 76, 25);
		Hit3CB.setEnabled(false);
		panel_3.add(Hit3CB);

		Miss3CB = new JComboBox(MISSPOLISYS);
		Miss3CB.setBounds(155, 102, 82, 23);
		Miss3CB.setEnabled(false);
		panel_3.add(Miss3CB);

		JLabel label_18 = new JLabel("MissP");
		label_18.setBounds(114, 104, 42, 16);
		panel_3.add(label_18);
	}

	private void onClickSaveBT() {
		if (FilePath != null && !FilePath.equals(" ")) {
			saveBT.setSelected(false);
			File file = new File(FilePath);
			if (file != null)
				saveFile();
		} else if (modified) {
			JFileChooser fileChooser = new JFileChooser();
			FileNameExtensionFilter filter = new FileNameExtensionFilter(
					FILE_TYPE_Viewed, FILE_TYPE);
			fileChooser.setFileFilter(filter);
			if (fileChooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
				FilePath = fileChooser.getSelectedFile().getPath();
				if (!FilePath.toLowerCase().endsWith(".txt"))
					FilePath += "." + FILE_TYPE;
				if (saveFile())
					modified = false;
			}
		}
	}

	private void EnableCacheLevel(int level, boolean status) {
		switch (level) {
		case 1:
			l1CashSizeTF.setEnabled(status);
			l1BlockLengthTF.setEnabled(status);
			l1AssociativityTF.setEnabled(status);
			Miss1CB.setEnabled(true);
			Hit1CB.setEnabled(true);
			if (!status) {
				l1CashSizeTF.setText("");
				l1BlockLengthTF.setText("");
				l1AssociativityTF.setText("");
				Miss1CB.setEnabled(false);
				Hit1CB.setEnabled(false);
			}
			break;
		case 2:
			l2CashSizeTF.setEnabled(status);
			l2BlockLengthTF.setEnabled(status);
			l2AssociativityTF.setEnabled(status);
			Miss2CB.setEnabled(true);
			Hit2CB.setEnabled(true);
			if (!status) {
				l2CashSizeTF.setText("");
				l2BlockLengthTF.setText("");
				l2AssociativityTF.setText("");
				Miss2CB.setEnabled(false);
				Hit2CB.setEnabled(false);
			}
			break;
		case 3:
			l3CashSizeTF.setEnabled(status);
			l3BlockLengthTF.setEnabled(status);
			l3AssociativityTF.setEnabled(status);
			Miss3CB.setEnabled(true);
			Hit3CB.setEnabled(true);
			if (!status) {
				l3CashSizeTF.setText("");
				l3BlockLengthTF.setText("");
				l3AssociativityTF.setText("");
				Miss3CB.setEnabled(false);
				Hit3CB.setEnabled(false);
			}
			break;
		}
	}

	private void createConsolePanel() {
		JTabbedPane bottomPart = new JTabbedPane(JTabbedPane.TOP);
		bottomPart.setBounds(0, 598, 1023, 181);
		frame.getContentPane().add(bottomPart);

		JLayeredPane consolePannel = new JLayeredPane();
		bottomPart.addTab("Console", null, consolePannel, null);
		consolePannel.setLayout(new BorderLayout(0, 0));

		consoleTP = new JTextPane();
		consoleTP.setBackground(Color.DARK_GRAY);
		consoleTP.setEditable(false);
		consolePannel.add(consoleTP);
		JScrollPane consoleScroll = new JScrollPane(consoleTP,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		consolePannel.add(consoleScroll);
	}

	public void printE(String message) {
		StyledDocument doc = consoleTP.getStyledDocument();
		Style style = consoleTP.addStyle(message, null);
		StyleConstants.setForeground(style, Color.RED);
		try {
			doc.insertString(doc.getLength(), message + "\n", style);
		} catch (BadLocationException ex) {
		}
	}

	public void printW(String message) {
		StyledDocument doc = consoleTP.getStyledDocument();
		String stru = "Warrning: " + message + "\n";
		Style style = consoleTP.addStyle(stru, null);
		StyleConstants.setForeground(style, Color.DARK_GRAY);

		try {
			doc.insertString(doc.getLength(), stru, style);
		} catch (BadLocationException ex) {
		}
	}

	public void printM(String message) {
		StyledDocument doc = consoleTP.getStyledDocument();

		Style style = consoleTP.addStyle(message, null);
		StyleConstants.setForeground(style, Color.WHITE);

		try {
			doc.insertString(doc.getLength(), message + "\n", style);
		} catch (BadLocationException ex) {
		}
	}

	private void createInputPanel() {
		JPanel InputPanel = new JPanel();
		InputPanel.setBounds(6, 6, 723, 593);
		frame.getContentPane().add(InputPanel);
		InputPanel.setLayout(new BoxLayout(InputPanel, BoxLayout.X_AXIS));

		codeInput = new JTextPane();
		// codeInput.setColumns(1);
		// codeInput.setTabSize(2);
//		codeInput.addCaretListener(new CaretListener() {
//			public void caretUpdate(CaretEvent caretEvent) {
//				System.out.println("dot:" + caretEvent.getDot());
//				System.out.println("mark" + caretEvent.getMark());
//			}
//		});
		
		Document d = codeInput.getDocument();
//		PlainDocument doc = (PlainDocument) codeInput.getDocument();
//		DocumentFilter test = new DocumentFilter();
//		
//		
//		doc.setDocumentFilter(new DocumentFilter());

		codeInput.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void changedUpdate(DocumentEvent arg0) {

			}

			@Override
			public void insertUpdate(DocumentEvent arg0) {
				modified = true;
				saveBT.setSelected(true);
			}

			@Override
			public void removeUpdate(DocumentEvent arg0) {
				// TODO Auto-generated method stub
				if (codeInput.getText().equals("")) {
					modified = false;
					saveBT.setSelected(false);
				}
			}
		});
		InputPanel.add(codeInput);

		JScrollPane scroll = new JScrollPane(codeInput,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

		InputPanel.add(scroll);
	}

	private void createRegisterPanel() {
		RegistersPanel = new JPanel();
		RegistersPanel.setBounds(1025, 572, 246, 204);
		frame.getContentPane().add(RegistersPanel);
		RegistersPanel.setLayout(null);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(0, 0, 246, 204);
		RegistersPanel.add(tabbedPane);

		JLayeredPane RegisterPane = new JLayeredPane();
		tabbedPane.addTab("Registers", null, RegisterPane, null);
		tabbedPane.setEnabledAt(0, true);
		initData();

		registerTB = new JTable(dataValues, columnNames);
		registerTB.setCellSelectionEnabled(true);
		registerTB.setColumnSelectionAllowed(true);

		registerTB.setGridColor(Color.LIGHT_GRAY);
		registerTB.setSurrendersFocusOnKeystroke(true);
		registerTB.setBounds(6, 18, 213, 128);
		registerTB.setFillsViewportHeight(true);

		registerTB.setTableHeader(registerTB.getTableHeader());
		registerTB.add(registerTB.getTableHeader(), BorderLayout.PAGE_START);
		registerTB.setEnabled(false);

		registerTB
				.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		registerTB.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		RegisterPane.add(registerTB);
	}

	/*************************************
	 ******** Other Methods ********
	 *************************************/
	/**
	 * saveFile: Saving the Text to a file if not exist parameters: file: The
	 * File to be saved in. return: none
	 * */
	private boolean saveFile() {
		File file = new File(FilePath);
		FileWriter fw = null;
		try {
			fw = new FileWriter(file.getAbsoluteFile(), false);
			codeInput.write(fw);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fw != null) {
				try {
					fw.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return false;
				}
			} else
				return false;
		}
		return true;
	}

	/**
	 * readFile: Reading a Text File and add it to the Editor parameters: path:
	 * The File Path return: none
	 * */
	private void readFile(String path) {
		File file = new File(path);
		BufferedReader reader = null;
		codeInput.setText("");
		try {
			reader = new BufferedReader(new FileReader(file));

			while (reader.ready())
				appendCode(reader.readLine());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (reader != null)
					reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void appendCode(String line) {
		StyledDocument doc = codeInput.getStyledDocument();
		Style style = consoleTP.addStyle(line, null);
		StyleConstants.setForeground(style, Color.BLACK);
		try {
			doc.insertString(doc.getLength(), line + "\n", style);
		} catch (BadLocationException ex) {
		}
	}

	private void onClickrunBT() {
		System.out.println(getCaches().toArray());

		int instruction_starting_address = -1;
		if (!modified) {
			JOptionPane.showMessageDialog(frame, "Save File Then Run ... !");
		} else {
			instruction_starting_address = getStartingAddress();
			if (instruction_starting_address < 0) {
				JOptionPane
						.showMessageDialog(frame, "Wrong start Address .. !");
				return;
			}
			setSimulatorVectors();
			simulator = new Simulator(data, instructions, getCaches(),
					getStartingAddress());

			try {
				simulator.Initialize();
				simulator.runInstructions();
				simulator.printMemory();
				simulator.printRegisters();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	private void changeMemoryTB() {
		// for (int i = 0; i < 10; i++) {
		// System.out.println(i + " : " +
		// simulator.getMemory().getInstructionAt(i));
		// }
		// System.out.println("#################");
		// for (int i = simulator.getMemory().DATA_STARTING_ADDRESS; i <=
		// simulator.getMemory().DATA_STARTING_ADDRESS + 10; i++) {
		// System.out.println(i + " : " +
		// this.simulator.getMemory().getDataAt(i));
		// }
	}

	/**
	 * Creating and initializing the data for the rows and columns of the
	 * Registers Table. initData: Calls the methods for init the table
	 * */
	private void initData() {
		CreateData();
		dataModel = new DefaultTableModel();
		dataModel.addColumn("Registers");
		dataModel.addColumn("Values");

		for (int row = 0; row < dataValues.length; row++) {
			dataModel.addRow(dataValues[row]);
		}
	}

	private void setRegisterData(HashMap<Integer, Integer> data) {
		Iterator<?> it = data.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pairs = (Map.Entry) it.next();
			registerTB.setValueAt((int) pairs.getValue(), (int) pairs.getKey(),
					1);
			System.out.println(pairs.getKey() + " = " + pairs.getValue());
			it.remove(); // avoids a ConcurrentModificationException
		}
		dataModel.fireTableDataChanged();
	}

	public void CreateData() {
		// Create data for each element
		dataValues = new String[8][2];
		for (int i = 0; i < 8; i++)
			dataValues[i][0] = "R" + i;
		for (int i = 0; i < 8; i++)
			dataValues[i][1] = "0";
	}

	/**
	 * Getting the Starting address
	 * **/
	private int getStartingAddress() {
		String data = startAdressTF.getText();
		return Integer.parseInt(data);
	}

	/**
	 * Data and Instruction Settings initializing Both data and instruction
	 * Vectors
	 * **/
	private void setSimulatorVectors() {
		boolean dataFound = false, instructionFound = false;
		for (String line : codeInput.getText().split("\\n")) {
			if (line.toLowerCase().contains("#data")) {
				dataFound = true;
				instructionFound = false;
				continue;
			} else if (line.toLowerCase().contains("#instructions")) {
				instructionFound = true;
				dataFound = false;
				continue;
			}
			if (dataFound)
				data.add(line);
			else if (instructionFound)
				instructions.add(line);
		}
	}

	/**
	 * Cache Settings initCache all three caches : Init the cache Array with the
	 * Three Level Cache return Array of Caches
	 * **/
	private HashMap<String, Integer> initCache(int cache_Level) {
		HashMap<String, Integer> cache = new HashMap<String, Integer>();
		int wb = -1, wa = -1;
		switch (cache_Level) {
		case 1:
			cache.put("associativity",
					Integer.parseInt(l1AssociativityTF.getText()));
			cache.put("cacheSize", Integer.parseInt(l1CashSizeTF.getText()));
			cache.put("blockSize", Integer.parseInt(l1BlockLengthTF.getText()));
			wb = Hit1CB.getSelectedIndex();
			cache.put("writeBack", wb == 0 ? 1 : 0);
			cache.put("writeThrough", wb == 1 ? 1 : 0);
			wa = Miss1CB.getSelectedIndex();
			cache.put("writeAllocate", wa == 1 ? 1 : 0);
			cache.put("writeAround", wa == 0 ? 1 : 0);
			break;
		case 2:
			cache.put("associativity",
					Integer.parseInt(l2AssociativityTF.getText()));
			cache.put("cacheSize", Integer.parseInt(l2CashSizeTF.getText()));
			cache.put("blockSize", Integer.parseInt(l2BlockLengthTF.getText()));
			wb = Hit2CB.getSelectedIndex();
			cache.put("writeBack", wb == 0 ? 1 : 0);
			cache.put("writeThrough", wb == 1 ? 1 : 0);
			wa = Miss2CB.getSelectedIndex();
			cache.put("writeAllocate", wa == 1 ? 1 : 0);
			cache.put("writeAround", wa == 0 ? 1 : 0);
			break;
		case 3:
			cache.put("associativity",
					Integer.parseInt(l3AssociativityTF.getText()));
			cache.put("cacheSize", Integer.parseInt(l3CashSizeTF.getText()));
			cache.put("blockSize", Integer.parseInt(l3BlockLengthTF.getText()));
			wb = Hit3CB.getSelectedIndex();
			cache.put("writeBack", wb == 0 ? 1 : 0);
			cache.put("writeThrough", wb == 1 ? 1 : 0);
			wa = Miss3CB.getSelectedIndex();
			cache.put("writeAllocate", wa == 1 ? 1 : 0);
			cache.put("writeAround", wa == 0 ? 1 : 0);
			break;
		}

		return cache;
	}

	private ArrayList<HashMap<String, Integer>> getCaches() {
		ArrayList<HashMap<String, Integer>> tmp = new ArrayList<HashMap<String, Integer>>();
		// L1 - Cache
		if (cacheLevelsCB.getSelectedIndex() > 0)
			tmp.add(initCache(1));
		// L2 - Cache
		if (cacheLevelsCB.getSelectedIndex() > 1)
			tmp.add(initCache(2));
		// L3 - Cache
		if (cacheLevelsCB.getSelectedIndex() > 2)
			tmp.add(initCache(3));

		return tmp;
	}
}
