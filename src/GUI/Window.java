package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
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
	private JButton loadBT, saveBT, runBT, debugBT, stopBT, nextBT;
	private JPanel RegistersPanel;
	private JTable memoryTB;
	private JComboBox<String> cacheLevelsCB, Miss1CB, Miss2CB, Miss3CB, Hit1CB,
			Hit2CB, Hit3CB;
	private JTextField startAdressTF, l2CacheSizeTF, l2BlockSizeTF,
			l2AssociativityTF, l1CacheSizeTF, l1BlockSizeTF, l1AssociativityTF,
			l3CacheSizeTF, l3BlockSizeTF, l3AssociativityTF;

	/****************************
	 ** Data Variables **
	 ****************************/
	private ArrayList<String> errors;
	private ArrayList<String> warrnings;
	private ArrayList<String> output;
	private boolean modified;
	private String FilePath;
	private Simulator simulator;
	private String columnNames[] = { "Register", "Value" };
	private String dataValues[][];
	private DefaultTableModel dataModel;
	
	private String MemoryColumnNames[] = { "Location", "value" };
	private String MemoryDataValues[][];
	private DefaultTableModel MemoryDataModel;
	
	private Vector<String> data = new Vector<>();
	private Vector<String> instructions = new Vector<>();

	/*************************
	 ** Static Variables **
	 *************************/
	// private static final String NUMBERS_ONLY_REGIX = "[0-9]+";
	private static final String FILE_TYPE_Viewed = "TEXT-File";
	private static final String FILE_TYPE = "txt";
	private Vector<String> HITPOLISYS = new Vector<String>();
	private Vector<String> MISSPOLISYS = new Vector<String>();
	private JTextField l2HitTimeTF;
	private JTextField l2MissTimeTF;
	private JTextField l1HitTimeTF;
	private JTextField l1MissTimeTF;
	private JTextField l3HitTimeTF;
	private JTextField l3MissTimeTF;
	private JTextField memoAccessTimeTF;

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
		initData();
		/*************************************
		 ** Initializing the Frame **
		 *************************************/
		frame = new JFrame();
		frame.setBounds(100, 100, 1280, 800);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setResizable(false);

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
		saveBT.setEnabled(false);
		saveBT.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				onClickSaveBT();
			}
		});
		OptionsPanel.add(saveBT);

		debugBT = new JButton("Debug");
		debugBT.setBounds(202, 4, 85, 29);
//		debugBT.setEnabled(false);
		debugBT.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
//				if (validate()) {
//					showWarrnings();
//					runBT.setEnabled(false);
//					debugBT.setEnabled(false);
//					stopBT.setEnabled(true);
//					nextBT.setEnabled(true);
//				} else {
//					showErrors();
//					runBT.setEnabled(true);
//					debugBT.setEnabled(true);
//					stopBT.setEnabled(false);
//					nextBT.setEnabled(false);
//				}
				
				HashMap<Integer, Integer> ssss = new HashMap<Integer, Integer>();
				ssss.put(2, 2);
				ssss.put(1, 200000000);
				ssss.put(444, 200);
				
				
				 setMamoryData(ssss);
				//
				// memoryTB.setValueAt(200, 1, 1);
				// memoryTB.repaint();

			}

		});
		OptionsPanel.add(debugBT);

		// Run Button
		runBT = new JButton("Run");
		runBT.setBounds(442, 4, 85, 29);
		runBT.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				runBT.setEnabled(false);
				// debugBT.setEnabled(false);
				if (validate()) {
					showWarrnings();
					setSimulatorVectors();
					simulator = new Simulator(data, instructions, getCaches(),
							getStartingAddress(), Integer
									.parseInt(memoAccessTimeTF.getText()));
					try {
						simulator.Initialize();
						simulator.runInstructions();
						simulator.printMemory();
						showMessages(simulator.output());
						setMamoryData(simulator.getMemoryValues());
						setRegisterData(simulator.getRegistersValues());
					} catch (IOException ea) {
						ea.printStackTrace();
					}
				} else {
					showErrors();
				}
				// debugBT.setEnabled(true);
				runBT.setEnabled(true);
			}
		});
		OptionsPanel.add(runBT);

		stopBT = new JButton("Stop");
		stopBT.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				runBT.setEnabled(true);
				debugBT.setEnabled(true);
				stopBT.setEnabled(false);
				nextBT.setEnabled(false);
				printE("******* **** Session has been terminated by the user **** *******");
			}
		});
		stopBT.setBounds(345, 3, 85, 30);
		stopBT.setEnabled(false);
		OptionsPanel.add(stopBT);

		nextBT = new JButton("");
		nextBT.setEnabled(false);
		nextBT.setIcon(new ImageIcon(
				Window.class
						.getResource("/com/sun/javafx/webkit/prism/resources/mediaPlayDisabled.png")));
		nextBT.setBounds(299, 8, 28, 20);
		nextBT.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

			}
		});
		OptionsPanel.add(nextBT);
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
		MemoryPane.setLayout(null);

		String[] CoLNames = { "Location", "Value" };
		Integer[][] memroyData = new Integer[1][2]; 
		memroyData[0][0] = 0;
		memroyData[0][1] = 0;
		memoryTB = new JTable(MemoryDataValues, MemoryColumnNames);
		memoryTB.setBounds(2, 2, 0, 468);

		memoryTB.setGridColor(Color.LIGHT_GRAY);
		memoryTB.setSurrendersFocusOnKeystroke(true);
		memoryTB.setFillsViewportHeight(true);
		memoryTB.setEnabled(false);
		memoryTB.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		memoryTB.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		MemoryPane.add(memoryTB);

		JScrollPane memoryS = new JScrollPane(memoryTB,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		memoryS.setBounds(0, 0, 218, 472);
		// memoryTB.add(memoryS);

		MemoryPane.add(memoryS);

		JTabbedPane SettingsTabbedPane = new JTabbedPane(JTabbedPane.TOP);
		SettingsTabbedPane.setBounds(741, 38, 276, 579);
		frame.getContentPane().add(SettingsTabbedPane);

		JLayeredPane layeredPane = new JLayeredPane();
		SettingsTabbedPane.addTab("Settings", null, layeredPane, null);

		startAdressTF = new JTextField();
		startAdressTF.setColumns(10);
		startAdressTF.setBounds(141, 6, 108, 28);
		PlainDocument doc = (PlainDocument) startAdressTF.getDocument();
		doc.setDocumentFilter(new NumbersFilter());
		layeredPane.add(startAdressTF);

		JLabel label = new JLabel("Start Address");
		label.setBounds(6, 12, 93, 16);
		layeredPane.add(label);

		JLabel label_2 = new JLabel("Cache Settings");
		label_2.setForeground(Color.GRAY);
		label_2.setHorizontalAlignment(SwingConstants.LEFT);
		label_2.setBounds(6, 55, 112, 16);
		layeredPane.add(label_2);

		Vector<String> items = new Vector<>();
		items.add("none");
		items.add("one");
		items.add("two");
		items.add("three");
		cacheLevelsCB = new JComboBox<>(items);
		cacheLevelsCB.setBounds(156, 69, 93, 27);
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
		label_3.setBounds(6, 73, 121, 16);
		layeredPane.add(label_3);

		JPanel panel_1 = new JPanel();
		panel_1.setLayout(null);
		panel_1.setBounds(6, 242, 243, 142);
		layeredPane.add(panel_1);

		JLabel label_4 = new JLabel("L2-Cache");
		label_4.setBounds(6, 6, 69, 16);
		panel_1.add(label_4);

		l2CacheSizeTF = new JTextField();
		l2CacheSizeTF.setEnabled(false);
		l2CacheSizeTF.setColumns(10);
		l2CacheSizeTF.setBounds(94, 19, 56, 28);
		PlainDocument l2CacheSizeDoc = (PlainDocument) l2CacheSizeTF
				.getDocument();
		l2CacheSizeDoc.setDocumentFilter(new NumbersFilter());
		panel_1.add(l2CacheSizeTF);

		l2BlockSizeTF = new JTextField();
		l2BlockSizeTF.setEnabled(false);
		l2BlockSizeTF.setColumns(10);
		l2BlockSizeTF.setBounds(94, 43, 56, 28);
		PlainDocument l2BlockSizeDoc = (PlainDocument) l2BlockSizeTF
				.getDocument();
		l2BlockSizeDoc.setDocumentFilter(new NumbersFilter());
		panel_1.add(l2BlockSizeTF);

		l2AssociativityTF = new JTextField();
		l2AssociativityTF.setEnabled(false);
		l2AssociativityTF.setColumns(10);
		l2AssociativityTF.setBounds(94, 70, 56, 28);
		PlainDocument l2AssociativityDoc = (PlainDocument) l2AssociativityTF
				.getDocument();
		l2AssociativityDoc.setDocumentFilter(new NumbersFilter());
		panel_1.add(l2AssociativityTF);

		JLabel label_5 = new JLabel("Cache Size");
		label_5.setBounds(6, 25, 89, 16);
		panel_1.add(label_5);

		JLabel label_6 = new JLabel("Block Size");
		label_6.setBounds(6, 49, 89, 16);
		panel_1.add(label_6);

		JLabel label_7 = new JLabel("Associativity");
		label_7.setBounds(6, 76, 89, 16);
		panel_1.add(label_7);

		JLabel label_1 = new JLabel("HitP");
		label_1.setBounds(178, 6, 26, 16);
		panel_1.add(label_1);

		Hit2CB = new JComboBox<>(HITPOLISYS);
		Hit2CB.setBounds(155, 22, 82, 25);
		Hit2CB.setEnabled(false);
		panel_1.add(Hit2CB);

		Miss2CB = new JComboBox<>(MISSPOLISYS);
		Miss2CB.setBounds(155, 74, 82, 23);
		Miss2CB.setEnabled(false);
		panel_1.add(Miss2CB);

		JLabel label_16 = new JLabel("MissP");
		label_16.setBounds(162, 49, 42, 16);
		panel_1.add(label_16);

		l2HitTimeTF = new JTextField();
		l2HitTimeTF.setEnabled(false);
		l2HitTimeTF.setBounds(45, 104, 56, 28);
		l2HitTimeTF.setColumns(10);
		PlainDocument l2HitTimeDoc = (PlainDocument) l2HitTimeTF.getDocument();
		l2HitTimeDoc.setDocumentFilter(new NumbersFilter());
		panel_1.add(l2HitTimeTF);

		JLabel lblHt = new JLabel("HT");
		lblHt.setBounds(16, 110, 27, 16);
		panel_1.add(lblHt);

		l2MissTimeTF = new JTextField();
		l2MissTimeTF.setEnabled(false);
		l2MissTimeTF.setColumns(10);
		l2MissTimeTF.setBounds(148, 104, 56, 28);
		PlainDocument l2MissTimeDoc = (PlainDocument) l2MissTimeTF
				.getDocument();
		l2MissTimeDoc.setDocumentFilter(new NumbersFilter());
		panel_1.add(l2MissTimeTF);

		JLabel lblMt = new JLabel("MT");
		lblMt.setBounds(119, 110, 27, 16);
		panel_1.add(lblMt);

		JPanel panel_2 = new JPanel();
		panel_2.setLayout(null);
		panel_2.setBounds(6, 96, 243, 142);
		layeredPane.add(panel_2);

		JLabel label_8 = new JLabel("L1-Cache");
		label_8.setBounds(6, 6, 69, 16);
		panel_2.add(label_8);

		l1CacheSizeTF = new JTextField();
		l1CacheSizeTF.setEnabled(false);
		l1CacheSizeTF.setColumns(10);
		l1CacheSizeTF.setBounds(94, 23, 56, 28);
		PlainDocument l1CacheSizeDoc = (PlainDocument) l1CacheSizeTF
				.getDocument();
		l1CacheSizeDoc.setDocumentFilter(new NumbersFilter());
		panel_2.add(l1CacheSizeTF);

		l1BlockSizeTF = new JTextField();
		l1BlockSizeTF.setEnabled(false);
		l1BlockSizeTF.setColumns(10);
		l1BlockSizeTF.setBounds(94, 47, 56, 28);
		PlainDocument l1BlockLengtDoc = (PlainDocument) l1BlockSizeTF
				.getDocument();
		l1BlockLengtDoc.setDocumentFilter(new NumbersFilter());
		panel_2.add(l1BlockSizeTF);

		l1AssociativityTF = new JTextField();
		l1AssociativityTF.setEnabled(false);
		l1AssociativityTF.setColumns(10);
		l1AssociativityTF.setBounds(94, 74, 56, 28);
		PlainDocument l1AssociativityDoc = (PlainDocument) l1AssociativityTF
				.getDocument();
		l1AssociativityDoc.setDocumentFilter(new NumbersFilter());
		panel_2.add(l1AssociativityTF);

		JLabel label_9 = new JLabel("Cache Size");
		label_9.setBounds(6, 29, 89, 16);
		panel_2.add(label_9);

		JLabel label_10 = new JLabel("Block Size");
		label_10.setBounds(6, 53, 89, 16);
		panel_2.add(label_10);

		JLabel label_11 = new JLabel("Associativity");
		label_11.setBounds(6, 80, 89, 16);
		panel_2.add(label_11);

		JLabel lblHitp = new JLabel("HitP");
		lblHitp.setBounds(168, 6, 26, 16);
		panel_2.add(lblHitp);

		Hit1CB = new JComboBox<>(HITPOLISYS);
		Hit1CB.setBounds(155, 26, 82, 25);
		Hit1CB.setEnabled(false);
		panel_2.add(Hit1CB);

		Miss1CB = new JComboBox<>(MISSPOLISYS);
		Miss1CB.setBounds(155, 74, 82, 23);
		Miss1CB.setEnabled(false);
		panel_2.add(Miss1CB);

		JLabel lblMissp = new JLabel("MissP");
		lblMissp.setBounds(168, 53, 42, 16);
		panel_2.add(lblMissp);

		l1HitTimeTF = new JTextField();
		l1HitTimeTF.setEnabled(false);
		l1HitTimeTF.setColumns(10);
		l1HitTimeTF.setBounds(45, 108, 56, 28);
		PlainDocument l1HitTimeDoc = (PlainDocument) l1HitTimeTF.getDocument();
		l1HitTimeDoc.setDocumentFilter(new NumbersFilter());
		panel_2.add(l1HitTimeTF);

		JLabel label_19 = new JLabel("HT");
		label_19.setBounds(16, 114, 27, 16);
		panel_2.add(label_19);

		l1MissTimeTF = new JTextField();
		l1MissTimeTF.setEnabled(false);
		l1MissTimeTF.setColumns(10);
		l1MissTimeTF.setBounds(148, 108, 56, 28);
		PlainDocument l1MissTimeDoc = (PlainDocument) l1MissTimeTF
				.getDocument();
		l1MissTimeDoc.setDocumentFilter(new NumbersFilter());
		panel_2.add(l1MissTimeTF);

		JLabel label_20 = new JLabel("MT");
		label_20.setBounds(119, 114, 27, 16);
		panel_2.add(label_20);

		JPanel panel_3 = new JPanel();
		panel_3.setLayout(null);
		panel_3.setBounds(6, 388, 243, 142);
		layeredPane.add(panel_3);

		JLabel label_12 = new JLabel("L3-Cache");
		label_12.setBounds(6, 6, 69, 16);
		panel_3.add(label_12);

		l3CacheSizeTF = new JTextField();
		l3CacheSizeTF.setEnabled(false);
		l3CacheSizeTF.setColumns(10);
		l3CacheSizeTF.setBounds(94, 21, 56, 28);
		PlainDocument l3CacheSizeDoc = (PlainDocument) l3CacheSizeTF
				.getDocument();
		l3CacheSizeDoc.setDocumentFilter(new NumbersFilter());
		panel_3.add(l3CacheSizeTF);

		l3BlockSizeTF = new JTextField();
		l3BlockSizeTF.setEnabled(false);
		l3BlockSizeTF.setColumns(10);
		l3BlockSizeTF.setBounds(94, 45, 56, 28);
		PlainDocument l3BlockSizeDoc = (PlainDocument) l3BlockSizeTF
				.getDocument();
		l3BlockSizeDoc.setDocumentFilter(new NumbersFilter());
		panel_3.add(l3BlockSizeTF);

		l3AssociativityTF = new JTextField();
		l3AssociativityTF.setEnabled(false);
		l3AssociativityTF.setColumns(10);
		l3AssociativityTF.setBounds(94, 72, 56, 28);
		PlainDocument l3AssociativityDoc = (PlainDocument) l3AssociativityTF
				.getDocument();
		l3AssociativityDoc.setDocumentFilter(new NumbersFilter());
		panel_3.add(l3AssociativityTF);

		JLabel label_13 = new JLabel("Cache Size");
		label_13.setBounds(6, 27, 89, 16);
		panel_3.add(label_13);

		JLabel label_14 = new JLabel("Block Size");
		label_14.setBounds(6, 51, 89, 16);
		panel_3.add(label_14);

		JLabel label_15 = new JLabel("Associativity");
		label_15.setBounds(6, 78, 89, 16);
		panel_3.add(label_15);

		JLabel label_17 = new JLabel("HitP");
		label_17.setBounds(175, 6, 26, 16);
		panel_3.add(label_17);

		Hit3CB = new JComboBox<>(HITPOLISYS);
		Hit3CB.setBounds(155, 24, 82, 25);
		Hit3CB.setEnabled(false);
		panel_3.add(Hit3CB);

		Miss3CB = new JComboBox<>(MISSPOLISYS);
		Miss3CB.setBounds(155, 76, 82, 23);
		Miss3CB.setEnabled(false);
		panel_3.add(Miss3CB);

		JLabel label_18 = new JLabel("MissP");
		label_18.setBounds(165, 51, 42, 16);
		panel_3.add(label_18);

		l3HitTimeTF = new JTextField();
		l3HitTimeTF.setEnabled(false);
		l3HitTimeTF.setColumns(10);
		l3HitTimeTF.setBounds(45, 106, 56, 28);
		PlainDocument l3HitTimeDoc = (PlainDocument) l3HitTimeTF.getDocument();
		l3HitTimeDoc.setDocumentFilter(new NumbersFilter());
		panel_3.add(l3HitTimeTF);

		JLabel label_21 = new JLabel("HT");
		label_21.setBounds(16, 112, 27, 16);
		panel_3.add(label_21);

		l3MissTimeTF = new JTextField();
		l3MissTimeTF.setEnabled(false);
		l3MissTimeTF.setColumns(10);
		l3MissTimeTF.setBounds(148, 106, 56, 28);
		PlainDocument l3MissTimeDoc = (PlainDocument) l3MissTimeTF
				.getDocument();
		l3MissTimeDoc.setDocumentFilter(new NumbersFilter());
		panel_3.add(l3MissTimeTF);

		JLabel label_22 = new JLabel("MT");
		label_22.setBounds(119, 112, 27, 16);
		panel_3.add(label_22);

		memoAccessTimeTF = new JTextField();
		memoAccessTimeTF.setColumns(10);
		memoAccessTimeTF.setBounds(141, 29, 108, 28);
		PlainDocument memoAccessTimeDoc = (PlainDocument) l3MissTimeTF
				.getDocument();
		memoAccessTimeDoc.setDocumentFilter(new NumbersFilter());
		layeredPane.add(memoAccessTimeTF);

		JLabel lblMemoryAccessTime = new JLabel("Memory Access Time");
		lblMemoryAccessTime.setBounds(6, 35, 133, 16);
		layeredPane.add(lblMemoryAccessTime);
	}

	private void onClickSaveBT() {
		if (FilePath != null && !FilePath.equals(" ")) {
			saveBT.setEnabled(false);
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
			l1CacheSizeTF.setEnabled(status);
			l1BlockSizeTF.setEnabled(status);
			l1AssociativityTF.setEnabled(status);
			Miss1CB.setEnabled(true);
			Hit1CB.setEnabled(true);
			l1HitTimeTF.setEnabled(true);
			l1MissTimeTF.setEnabled(true);
			if (!status) {
				l1CacheSizeTF.setText("");
				l1BlockSizeTF.setText("");
				l1AssociativityTF.setText("");
				Miss1CB.setEnabled(false);
				Hit1CB.setEnabled(false);
				l1HitTimeTF.setEnabled(false);
				l1MissTimeTF.setEnabled(false);
			}
			break;
		case 2:
			l2CacheSizeTF.setEnabled(status);
			l2BlockSizeTF.setEnabled(status);
			l2AssociativityTF.setEnabled(status);
			Miss2CB.setEnabled(true);
			Hit2CB.setEnabled(true);
			l2HitTimeTF.setEnabled(true);
			l2MissTimeTF.setEnabled(true);
			if (!status) {
				l2CacheSizeTF.setText("");
				l2BlockSizeTF.setText("");
				l2AssociativityTF.setText("");
				Miss2CB.setEnabled(false);
				Hit2CB.setEnabled(false);
				l2HitTimeTF.setEnabled(false);
				l2MissTimeTF.setEnabled(false);
			}
			break;
		case 3:
			l3CacheSizeTF.setEnabled(status);
			l3BlockSizeTF.setEnabled(status);
			l3AssociativityTF.setEnabled(status);
			Miss3CB.setEnabled(true);
			Hit3CB.setEnabled(true);
			l3HitTimeTF.setEnabled(true);
			l3MissTimeTF.setEnabled(true);
			if (!status) {
				l3CacheSizeTF.setText("");
				l3BlockSizeTF.setText("");
				l3AssociativityTF.setText("");
				Miss3CB.setEnabled(false);
				Hit3CB.setEnabled(false);
				l3HitTimeTF.setEnabled(false);
				l3MissTimeTF.setEnabled(false);
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
		StyleConstants.setBold(style, true);
		StyleConstants.setFontSize(style, 14);
		StyleConstants.setFontFamily(style, "Arial");
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
		StyleConstants.setFontSize(style, 12);
		StyleConstants.setFontFamily(style, "Arial");

		try {
			doc.insertString(doc.getLength(), stru, style);
		} catch (BadLocationException ex) {
		}
	}

	public void printM(String message) {
		StyledDocument doc = consoleTP.getStyledDocument();

		Style style = consoleTP.addStyle(message, null);
		StyleConstants.setForeground(style, Color.WHITE);
		StyleConstants.setFontSize(style, 12);
		StyleConstants.setFontFamily(style, "Arial");

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
		codeInput.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void changedUpdate(DocumentEvent arg0) {

			}

			@Override
			public void insertUpdate(DocumentEvent arg0) {
				modified = true;
				saveBT.setEnabled(true);
			}

			@Override
			public void removeUpdate(DocumentEvent arg0) {
				modified = true;
				saveBT.setEnabled(true);
			}
		});
		InputPanel.add(codeInput);

		JScrollPane scroll = new JScrollPane(codeInput,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

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
		RegisterPane.setLayout(null);

		registerTB = new JTable(dataValues, columnNames);
		registerTB.setBounds(2, 18, 206, 138);
		registerTB.setGridColor(Color.LIGHT_GRAY);
		registerTB.setSurrendersFocusOnKeystroke(true);
		registerTB.setFillsViewportHeight(true);
		registerTB.setEnabled(false);
		registerTB
				.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		registerTB.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

		RegisterPane.add(registerTB);

		JScrollPane regS = new JScrollPane(registerTB,
				JScrollPane.VERTICAL_SCROLLBAR_NEVER,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		regS.setBounds(0, 0, 225, 158);
		RegisterPane.add(regS);
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
			} else {
				return false;
			}
		}
		modified = false;
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

		modified = false;
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

	/**
	 * Creating and initializing the data for the rows and columns of the
	 * Registers Table. initData: Calls the methods for init the table
	 * */
	private void initData() {
		CreateData();
		CreateMemoryData();
	}

	private void setRegisterData(HashMap<Integer, Integer> data) {
		for (Entry<Integer, Integer> entry : data.entrySet()) {
			registerTB.setValueAt(entry.getValue().toString(),
					(int) entry.getKey(), 1);
		}
		registerTB.repaint();
	}

	private void setMamoryData(HashMap<Integer, Integer> data) {
		int counter = 0;
		for (Entry<Integer, Integer> entry : data.entrySet()) {
			System.out.println(entry.getKey() + "   " +entry.getValue());
			memoryTB.setValueAt(entry.getKey().toString(),
					counter, 0);
			memoryTB.setValueAt(entry.getValue().toString(),
					counter, 1);
		}
		memoryTB.repaint();
	}

	public void CreateData() {
		// Create data for each element
		dataValues = new String[8][2];
		for (int i = 0; i < 8; i++)
			dataValues[i][0] = "R" + i;
		for (int i = 0; i < 8; i++)
			dataValues[i][1] = "0";
	}
	public void CreateMemoryData() {
		// Create data for each element
		MemoryDataValues = new String[3500][2];
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
			if (line.trim().isEmpty())
				continue;
			if (line.toLowerCase().contains("#data")) {
				dataFound = true;
				instructionFound = false;
				continue;
			} else if (line.toLowerCase().contains("#instructions")) {
				instructionFound = true;
				dataFound = false;
				continue;
			}
			System.out.println(line.trim() + "    ->DataFound:  " + dataFound
					+ "  --  instructionFound:  " + instructionFound);
			if (dataFound)
				data.add(line.trim());
			else if (instructionFound)
				instructions.add(line.trim());
		}
		System.out.println(Arrays.toString(instructions.toArray()));
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
			cache.put("cacheSize", Integer.parseInt(l1CacheSizeTF.getText()));
			cache.put("blockSize", Integer.parseInt(l1BlockSizeTF.getText()));
			wb = Hit1CB.getSelectedIndex();
			cache.put("writeBack", wb == 0 ? 1 : 0);
			cache.put("writeThrough", wb == 1 ? 1 : 0);
			wa = Miss1CB.getSelectedIndex();
			cache.put("writeAllocate", wa == 1 ? 1 : 0);
			cache.put("writeAround", wa == 0 ? 1 : 0);
			cache.put("writeAround", Integer.parseInt(l1HitTimeTF.getText()));
			cache.put("hitTime", Integer.parseInt(l1HitTimeTF.getText()));
			cache.put("missTime", Integer.parseInt(l1MissTimeTF.getText()));
			break;
		case 2:
			cache.put("associativity",
					Integer.parseInt(l2AssociativityTF.getText()));
			cache.put("cacheSize", Integer.parseInt(l2CacheSizeTF.getText()));
			cache.put("blockSize", Integer.parseInt(l2BlockSizeTF.getText()));
			wb = Hit2CB.getSelectedIndex();
			cache.put("writeBack", wb == 0 ? 1 : 0);
			cache.put("writeThrough", wb == 1 ? 1 : 0);
			wa = Miss2CB.getSelectedIndex();
			cache.put("writeAllocate", wa == 1 ? 1 : 0);
			cache.put("writeAround", wa == 0 ? 1 : 0);
			cache.put("hitTime", Integer.parseInt(l2HitTimeTF.getText()));
			cache.put("missTime", Integer.parseInt(l2MissTimeTF.getText()));
			break;
		case 3:
			cache.put("associativity",
					Integer.parseInt(l3AssociativityTF.getText()));
			cache.put("cacheSize", Integer.parseInt(l3CacheSizeTF.getText()));
			cache.put("blockSize", Integer.parseInt(l3BlockSizeTF.getText()));
			wb = Hit3CB.getSelectedIndex();
			cache.put("writeBack", wb == 0 ? 1 : 0);
			cache.put("writeThrough", wb == 1 ? 1 : 0);
			wa = Miss3CB.getSelectedIndex();
			cache.put("writeAllocate", wa == 1 ? 1 : 0);
			cache.put("writeAround", wa == 0 ? 1 : 0);
			cache.put("hitTime", Integer.parseInt(l3HitTimeTF.getText()));
			cache.put("missTime", Integer.parseInt(l3MissTimeTF.getText()));
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

	private void showErrors() {
		for (String err : errors)
			printE(err);
	}
	private void showMessages(ArrayList<String> messages) {
		for (String err : messages)
			printM(err);
	}

	private void showWarrnings() {
		for (String warr : warrnings)
			printE(warr);
	}

	private boolean validate() {
		consoleTP.setText("");
		errors = new ArrayList<String>();
		warrnings = new ArrayList<String>();
		if (codeInput.getText().trim().isEmpty()) {
			errors.add("Can't run, Please Enter code first \n ====================================");
			return false;
		}
		if (modified)
			onClickSaveBT();

		if (startAdressTF.getText().trim().isEmpty())
			errors.add("Starting Address can't be blank. \n ====================================");
		if (memoAccessTimeTF.getText().trim().isEmpty())
			errors.add("Memory Access Time can't be blank. \n ====================================");
		if (cacheLevelsCB.getSelectedIndex() == 0)
			errors.add("Select the number of caches levels needed \n ====================================");
		else if (cacheLevelsCB.getSelectedIndex() > 0) {
			if (l1CacheSizeTF.getText().trim().isEmpty())
				errors.add("L1-Cache: Cache-Size can't be blank");
			if (l1BlockSizeTF.getText().trim().isEmpty())
				errors.add("L1-Cache: Block-Size can't be blank");
			if (l1AssociativityTF.getText().trim().isEmpty())
				errors.add("L1-Cache: Associativity can't be blank");
			// errors.add("====================================");
			if (cacheLevelsCB.getSelectedIndex() > 1) {
				if (l2CacheSizeTF.getText().trim().isEmpty())
					errors.add("L2-Cache: Cache-Size can't be blank");
				if (l2BlockSizeTF.getText().trim().isEmpty())
					errors.add("L2-Cache: Block-Size can't be blank");
				if (l2AssociativityTF.getText().trim().isEmpty())
					errors.add("L2-Cache: Associativity can't be blank");
				// errors.add("====================================");
				if (cacheLevelsCB.getSelectedIndex() > 2) {
					if (l3CacheSizeTF.getText().trim().isEmpty())
						errors.add("L3-Cache: Cache-Size can't be blank");
					if (l3BlockSizeTF.getText().trim().isEmpty())
						errors.add("L3-Cache: Block-Size can't be blank");
					if (l3AssociativityTF.getText().trim().isEmpty())
						errors.add("L3-Cache: Associativity can't be blank");
					// errors.add("====================================");
				}
			}
		}
		return errors.isEmpty() ? true : false;
	}
}
