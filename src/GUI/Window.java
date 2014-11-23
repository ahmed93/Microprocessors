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
import java.util.HashMap;
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
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

import org.eclipse.wb.swing.FocusTraversalOnArray;

import simulator.Simulator;
import Abstracts.Cache;
import factories.CacheFactory;

public class Window {

	private JFrame frame;
	private JTextArea codeInput;
	private static JTextPane consoleTP;
	private JTable registerTB;
	private JButton loadBT, saveBT, runBT;
	private JPanel debuging;
	private DefaultTableModel dataModel;
	private JTable memoryTB;
	private JComboBox cacheLevelsCB;
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
		OptionsPanel.setBounds(771, 5, 503, 39);
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
		OptionsPanel.add(stioBT);
		OptionsPanel.setFocusTraversalPolicy(new FocusTraversalOnArray(
				new Component[] { loadBT, saveBT, runBT }));

		Panel panel = new Panel();
		panel.setBounds(1023, 50, 247, 496);
		frame.getContentPane().add(panel);
		panel.setLayout(new BorderLayout(0, 0));

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		panel.add(tabbedPane, BorderLayout.CENTER);

		JLayeredPane MemoryPane = new JLayeredPane();
		tabbedPane.addTab("Memory", null, MemoryPane, null);

		memoryTB = new JTable();
		memoryTB.setBounds(6, 6, 214, 438);
		MemoryPane.add(memoryTB);

		JTabbedPane SettingsTabbedPane = new JTabbedPane(JTabbedPane.TOP);
		SettingsTabbedPane.setBounds(771, 50, 246, 556);
		frame.getContentPane().add(SettingsTabbedPane);

		JLayeredPane layeredPane = new JLayeredPane();
		SettingsTabbedPane.addTab("Settings", null, layeredPane, null);

		startAdressTF = new JTextField();
		startAdressTF.setColumns(10);
		startAdressTF.setBounds(111, 41, 108, 28);
		startAdressTF.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}
		});
		layeredPane.add(startAdressTF);

		JLabel label = new JLabel("Start Address");
		label.setBounds(6, 47, 93, 16);
		layeredPane.add(label);

		JLabel label_1 = new JLabel("Memory Settings");
		label_1.setHorizontalAlignment(SwingConstants.CENTER);
		label_1.setBounds(54, 13, 112, 16);
		layeredPane.add(label_1);

		JLabel label_2 = new JLabel("Cache Settings");
		label_2.setHorizontalAlignment(SwingConstants.CENTER);
		label_2.setBounds(63, 75, 103, 16);
		layeredPane.add(label_2);

		Vector<String> items = new Vector<>();
		items.add("none");
		items.add("one");
		items.add("two");
		items.add("three");
		cacheLevelsCB = new JComboBox(items);
		cacheLevelsCB.setBounds(126, 103, 93, 27);
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
		label_3.setBounds(6, 107, 121, 16);
		layeredPane.add(label_3);

		JPanel panel_1 = new JPanel();
		panel_1.setLayout(null);
		panel_1.setBounds(6, 246, 213, 106);
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

		JPanel panel_2 = new JPanel();
		panel_2.setLayout(null);
		panel_2.setBounds(6, 135, 213, 106);
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

		JPanel panel_3 = new JPanel();
		panel_3.setLayout(null);
		panel_3.setBounds(6, 360, 213, 106);
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
			if (!status) {
				l1CashSizeTF.setText("");
				l1BlockLengthTF.setText("");
				l1AssociativityTF.setText("");
			}
			break;
		case 2:
			l2CashSizeTF.setEnabled(status);
			l2BlockLengthTF.setEnabled(status);
			l2AssociativityTF.setEnabled(status);
			if (!status) {
				l2CashSizeTF.setText("");
				l2BlockLengthTF.setText("");
				l2AssociativityTF.setText("");
			}
			break;
		case 3:
			l3CashSizeTF.setEnabled(status);
			l3BlockLengthTF.setEnabled(status);
			l3AssociativityTF.setEnabled(status);
			if (!status) {
				l3CashSizeTF.setText("");
				l3BlockLengthTF.setText("");
				l3AssociativityTF.setText("");
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
		consoleTP.setEditable(false);
		consolePannel.add(consoleTP);
		JScrollPane consoleScroll = new JScrollPane(consoleTP,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		consolePannel.add(consoleScroll);
	}

	public static void printError() {

	}

	public static void printWarnning() {

	}

	public static void printOutput(String message) {

	}

	private void createInputPanel() {
		JPanel InputPanel = new JPanel();
		InputPanel.setBounds(6, 6, 761, 593);
		frame.getContentPane().add(InputPanel);
		InputPanel.setLayout(new BoxLayout(InputPanel, BoxLayout.X_AXIS));

		codeInput = new JTextArea();
		codeInput.setColumns(1);
		codeInput.setTabSize(2);
		codeInput.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void changedUpdate(DocumentEvent arg0) {
				// TODO Auto-generated method stub
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
		codeInput.setLineWrap(true);
		InputPanel.add(codeInput);

		JScrollPane scroll = new JScrollPane(codeInput,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

		InputPanel.add(scroll);
	}

	private void createRegisterPanel() {
		debuging = new JPanel();
		debuging.setBounds(1025, 560, 246, 204);
		frame.getContentPane().add(debuging);
		debuging.setLayout(null);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(0, 0, 246, 204);
		debuging.add(tabbedPane);

		JLayeredPane RegisterPane = new JLayeredPane();
		tabbedPane.addTab("Registers", null, RegisterPane, null);
		tabbedPane.setEnabledAt(0, true);
		initData();

		registerTB = new JTable(dataValues, columnNames);
		registerTB.setBorder(UIManager.getBorder("TableHeader.cellBorder"));
		registerTB.setGridColor(Color.BLACK);
		registerTB.setSurrendersFocusOnKeystroke(true);
		registerTB.setBounds(6, 6, 213, 146);
		registerTB.setFillsViewportHeight(true);
		registerTB.setEnabled(false);
		registerTB
				.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
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
				codeInput.append(reader.readLine() + "\n");
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

	private void onClickrunBT() {
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
			simulator = new Simulator(data, instructions, getCacheSettings(),
					instruction_starting_address);

			System.out.println(data + "\n" + instructions + "\n"
					+ instruction_starting_address);
			try {
				simulator.Initialize();
				simulator.runInstructions();
				simulator.printMemroy();
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

	private void setRegisterData(HashMap<String, Integer> data) {

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
		return data.matches(NUMBERS_ONLY_REGIX) ? Integer.parseInt(data) : -1;
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
	private Cache initCache(JTextField associativityTF, JTextField blockSizeTF,
			JTextField cacheSizeTF) {
		String tmpData = associativityTF.getText().toString();
		int associativity = tmpData.matches(NUMBERS_ONLY_REGIX) ? Integer
				.parseInt(tmpData) : -1;
		tmpData = blockSizeTF.getText().toString();
		int blockSize = tmpData.matches(NUMBERS_ONLY_REGIX) ? Integer
				.parseInt(tmpData) : -1;
		tmpData = cacheSizeTF.getText().toString();
		int cacheSize = tmpData.matches(NUMBERS_ONLY_REGIX) ? Integer
				.parseInt(tmpData) : -1;

		return associativity == -1 || blockSize == -1 || cacheSize == -1 ? null
				: CacheFactory.createCache(associativity, blockSize, cacheSize);
	}

	private boolean[] setPolisy(int cache_Level) {
		boolean[] polisys = { false, false, false, false };
		switch (cache_Level) {
		case 1:
			break;
		case 2:

			break;
		case 3:

			break;
		default:
			break;
		}

		return polisys;
	}

	private Cache[] getCacheSettings() {
		Cache[] tmp = new Cache[3];
		// / L1 - Cache
		tmp[0] = initCache(l1AssociativityTF, l1BlockLengthTF, l1CashSizeTF);
		// / L2 - Cache
		tmp[1] = initCache(l2AssociativityTF, l2BlockLengthTF, l2CashSizeTF);
		// / L3 - Cache
		tmp[2] = initCache(l3AssociativityTF, l3BlockLengthTF, l3CashSizeTF);
		return tmp;
	}
}