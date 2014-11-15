package GUI;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileView;

import simulator.Simulator;

public class MainWindow implements DocumentListener {

	private Vector<String> list;
	private String FilePath;
	private boolean modified;
	private JFrame frame;
	private JTextArea codeInput;
	private JTable registerTB;
	private JButton loadBT, saveBT, runBT;
	private Simulator simulator;
	
	private HashMap<String, Integer> REGISTER;
	
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

		JLayeredPane layeredPane_1 = new JLayeredPane();
		bottomPart.addTab("New tab", null, layeredPane_1, null);

		JLayeredPane layeredPane_2 = new JLayeredPane();
		bottomPart.addTab("New tab", null, layeredPane_2, null);
		
		/***********************************************
		 **          InputPanel: CodeInput   	 **
		 ***********************************************/
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
		
		/***************************************************************
		 **          DebuggingPanel: Registers/Cashes/..   	 **
		 ***************************************************************/
		JPanel debuging = new JPanel();
		debuging.setBounds(771, 47, 246, 488);
		frame.getContentPane().add(debuging);
		debuging.setLayout(new BorderLayout(0, 0));

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		debuging.add(tabbedPane);

		JLayeredPane RegisterPane = new JLayeredPane();
		tabbedPane.addTab("Registers", null, RegisterPane, null);
		initData();
		registerTB = new JTable(dataValues, columnNames);
		registerTB.setEnabled(false);
		registerTB.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		registerTB.setBounds(6, 6, 213, 430);
		RegisterPane.add(registerTB);

		JLayeredPane CashePane = new JLayeredPane();
		tabbedPane.addTab("Cashes", null, CashePane, null);

		JPanel OptionsPanel = new JPanel();
		OptionsPanel.setBounds(771, 5, 246, 39);
		frame.getContentPane().add(OptionsPanel);
		OptionsPanel.setLayout(new GridLayout(1, 0, 0, 0));
		
		/***********************************************
		 **          Buttons Settings Options   	 **
		 ***********************************************/
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
				if(FilePath != null && !FilePath.equals(" ")){
					saveBT.setSelected(false);
					File file = new File(FilePath);
					if (file != null)
						saveFile(file);
				}else if(modified) {
					JFileChooser fileChooser = new JFileChooser();
					FileNameExtensionFilter filter = new FileNameExtensionFilter(
							FILE_TYPE_Viewed,FILE_TYPE);
					fileChooser.setFileFilter(filter);
					FileView fileView = new FileView() {
						public String getTypeDescription(File f){
							return f.getName() + FILE_TYPE;
						}
					};
					fileChooser.setFileView(fileView);
					
					if (fileChooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
						fileChooser.setName(fileChooser.getName() + FILE_TYPE);
						File file = fileChooser.getSelectedFile();
					  	String code = codeInput.getText();
					  	BufferedWriter sFile = null;
		                try {
		                	 sFile = new BufferedWriter(new FileWriter(file));
							sFile.write(code);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}finally {
							try {
								sFile.close();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
		                
//					  	fileChooser.setName(fileChooser.getName() + ".ssam");
					  	FilePath = file.getPath();
					  	modified = false;
					}
				}
			}
		});
		OptionsPanel.add(saveBT);
		
		// Run Button
		runBT = new JButton("Run");
		runBT.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (modified) JOptionPane.showMessageDialog(frame,	"Save File Then Run ... !");
			}
		});
		OptionsPanel.add(runBT);
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
	private void saveFile(File file) {
		FileWriter fw;
		try {
			fw = new FileWriter(file.getAbsoluteFile(), false);
			codeInput.write(fw);
		} catch (IOException e) {
			e.printStackTrace();
		}
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
