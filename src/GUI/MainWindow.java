package GUI;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

import simulator.Simulator;
import java.awt.Component;
import javax.swing.Box;
import org.eclipse.wb.swing.FocusTraversalOnArray;

public class MainWindow implements DocumentListener {

	private JFrame frame;
	private JTextArea codeInput;
	private JTable registerTB;
	private JButton loadBT, saveBT, runBT;
	private JPanel debuging;
	private DefaultTableModel dataModel;
	
	/****************************
	 **    Data Variables 	 **
	 ****************************/
	private boolean modified;
	private Vector<String> list;
	private String FilePath;
	private Simulator simulator;
	private HashMap<String, Integer> REGISTER;
	
	/*******************************
	 **    Static Variables 	 **
	 *******************************/
	private static final String FILE_TYPE_Viewed = "TEXT-File";
	private static final String FILE_TYPE = "txt";

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
		/*************************************
		 **    Initializing the Frame 	 **
		 *************************************/
		frame = new JFrame();
		frame.setBounds(100, 100, 1280, 800);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		/***********************************************
		 **    JTabbedPane: Tabs-Console 	 **
		 ***********************************************/
		JTabbedPane bottomPart = new JTabbedPane(JTabbedPane.TOP);
		bottomPart.setBounds(0, 598, 1023, 181);
		frame.getContentPane().add(bottomPart);

		JLayeredPane consolePannel = new JLayeredPane();
		bottomPart.addTab("Console", null, consolePannel, null);
		
		/***********************************************
		 **          InputPanel: CodeInput   	 **
		 ***********************************************/
		createInputPanel();
		
		/***************************************************************
		 **          DebuggingPanel: Registers/Cashes/..   	 **
		 ***************************************************************/
		createRegisterPanel();
		
		/***********************************************
		 **          Buttons Settings Options   	 **
		 ***********************************************/
		createOptionPanel();
	}
	
	private void createOptionPanel() {
		JPanel OptionsPanel = new JPanel();
		OptionsPanel.setBounds(771, 5, 503, 39);
		frame.getContentPane().add(OptionsPanel);
		OptionsPanel.setLayout(new GridLayout(1, 0, 0, 0));

		// Load Button
		loadBT = new JButton("Load");
		loadBT.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter(
						FILE_TYPE_Viewed,FILE_TYPE);
				chooser.setFileFilter(filter);
				int returnVal = chooser.showOpenDialog(frame);
				if (returnVal == JFileChooser.APPROVE_OPTION) {	
					FilePath = chooser.getSelectedFile().getPath();
					readFile(FilePath);	
				}
			}
		});
		OptionsPanel.add(loadBT);

		// Save Button
		saveBT = new JButton("Save");
		saveBT.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				onClickSaveBT();
			}
		});
		OptionsPanel.add(saveBT);
		
		Component verticalStrut = Box.createVerticalStrut(1);
		OptionsPanel.add(verticalStrut);
		
		JButton btnDebug = new JButton("Debug");
		OptionsPanel.add(btnDebug);
		
				// Run Button
				runBT = new JButton("Run");
				runBT.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
//				if (modified) JOptionPane.showMessageDialog(frame,	"Save File Then Run ... !");
						codeInput.setText(dataModel.getDataVector().toString());
						dataModel.setValueAt(200, 3, 1);
					}
				});
				OptionsPanel.add(runBT);
				OptionsPanel.setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{loadBT, saveBT, runBT}));
	}

	private void onClickSaveBT() {
		if(FilePath != null && !FilePath.equals(" ")){
			saveBT.setSelected(false);
			File file = new File(FilePath);
			if (file != null)
				saveFile();
		}else if(modified) {
			JFileChooser fileChooser = new JFileChooser();
			FileNameExtensionFilter filter = new FileNameExtensionFilter(
					FILE_TYPE_Viewed,FILE_TYPE);
			fileChooser.setFileFilter(filter);
			if (fileChooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
				FilePath = fileChooser.getSelectedFile().getPath();
				if(!FilePath.toLowerCase().endsWith(".txt")) FilePath += "."+FILE_TYPE;
			  	if(saveFile()) modified = false;
			}
		}
	}
	
	private void createInputPanel() {
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
	}
	
	private void createRegisterPanel()	{
		debuging = new JPanel();
		debuging.setBounds(771, 200, 246, 204);
		frame.getContentPane().add(debuging);
		debuging.setLayout(new BorderLayout(0, 0));

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		debuging.add(tabbedPane);

		JLayeredPane RegisterPane = new JLayeredPane();
		tabbedPane.addTab("Registers", null, RegisterPane, null);
		tabbedPane.setEnabledAt(0, true);
		initData();
		
		registerTB = new JTable(dataModel);
		registerTB.setSurrendersFocusOnKeystroke(true);
		registerTB.setBounds(6, 6, 213, 146);
		registerTB.setFillsViewportHeight(true);
		registerTB.setEnabled(false);
		registerTB.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		RegisterPane.add(registerTB);
	}
	
	/***************************************************************
	 **          Overriding the Editor Change Events   	 **
	 ***************************************************************/
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
	
	/*************************************
	 ********  Other Methods ********
	 *************************************/
	/**
	 *	saveFile: Saving the Text to a file if not exist
	 *	parameters: file: The File to be saved in.
	 *	return: none
	 * */
	private boolean saveFile() {
		File file = new File(FilePath);
		FileWriter fw= null;
		try {
			fw = new FileWriter(file.getAbsoluteFile(), false);
			codeInput.write(fw);
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if (fw != null) {
				try {
					fw.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return false;
				}
			}
			else return false;
		}
		return true;
	}

	/**
	 *	readFile: Reading a Text File and add it to the Editor
	 *	parameters: path: The File Path
	 *	return: none
	 * */
	private void readFile(String path) {
		list = new Vector<String>();
		File file = new File(path);
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			while (reader.ready()) 
				list.add(reader.readLine());
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
		InitTextArea();
	}

	private void InitTextArea() {
		codeInput.setText("");
		for (String line : list)
			codeInput.append(line + "\n");
	}

	/**
	 *	Creating and initializing the data for the rows and columns of the Registers Table.
	 *	initData: Calls the methods for init the table
	 * */
	private String columnNames[];
	private String dataValues[][];
	private void initData() {
		CreateColumns();
		CreateData();

		dataModel = new DefaultTableModel(); 
		dataModel.addColumn("Registers");
		dataModel.addColumn("Values");
		
		for (int row = 0; row < dataValues.length; row++) {
		dataModel.addRow(dataValues[row]);
		}	
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

		for (int i = 0; i < 8; i++) {
			dataValues[i][0] = "R" + i;
		}
		for (int i = 0; i < 8; i++) {
			dataValues[i][1] = "0";
		}
	}
}