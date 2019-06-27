/**
 * 
 */
package bicliques.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import bicliques.algorithms.LEX;
import bicliques.algorithms.MICA;
import bicliques.algorithms.MaximalBicliquesAlgorithm;
import bicliques.graphs.Biclique;
import bicliques.graphs.Graph;
import bicliques.graphs.GraphVendingMachine;
import bicliques.graphs.Graph.Vertex;

/**
 * GUI for computing the set of maximal bicliques of a graph.
 * @author Roland Koppenberger
 * @version 1.0, June 24th 2019.
 */
public class MaxBicliquesGUI implements Runnable, ActionListener {

	/* =======================================================================================
	 * 
	 *   thread for computation
	 * 
	 * =======================================================================================
	 */
	
	/**
	 * Computation thread implemented as inner class.<br>
	 * Computes the set of maximal bicliques of a given graph.
	 */
	private class Computation extends Thread {
		
		/**
		 * Computes set of maximal bicliques.
		 */
		public void run() {
			if (mba == null) {
				setGUIafterComputationFailed("(no algorithm object for computation)");
				return;
			}
			setOfMaxBicliques = mba.findMaxBicliques(graph);
			if (setOfMaxBicliques == null) {
				setGUIafterComputationFailed(LBL_NOT_COMPUTED);
				return;
			}
			
			if (isInterrupted())
				return;
			btnCompute.setEnabled(false);
			isComputing = false;
			
			// output
			int numBicliques = setOfMaxBicliques.size();
			String txt = "Set of ";
			txt += (numBicliques == 1 ? "1" : numBicliques);
			txt += " maximal biclique";
			txt += (numBicliques == 1 ? "" : "s");
			txt += " computed\n";
			if (numBicliques < 30)
				for (Biclique<String, Integer> bic : setOfMaxBicliques) {
					txt += "\n" + bic;
				}
			else
				txt += "\n(too many bicliques to show)";
			txt += "\n";
			txtOutput.setText(txt);
			
			menuInput.setEnabled(true);
			menuOutput.setEnabled(true);
			menuAlgorithm.setEnabled(true);
			btnCompute.setText(LBL_COMPUTE);
			btnCompute.setEnabled(true);			
		}
		
	}
	
	/* =======================================================================================
	 * 
	 *   constants
	 * 
	 * =======================================================================================
	 */
	
	private static final int MIN_WIDTH  = 640;
	private static final int MIN_HEIGHT = 480;
	
	// labels
	private static final String LBL_CAPTION = "Maximal bicliques of a graph";
	
	private static final String LBL_NO_GRAPH            = "(no graph)";
	private static final String LBL_NOT_COMPUTED        = "(not computed)";
	private static final String LBL_COMPUTATION_ABORTED = "(computation aborted)";
	private static final String LBL_IS_COMPUTING        = "... is computing ...";
	private static final String LBL_NONE_CHOOSEN        = "(none choosen)";
	
	private static final String LBL_INPUT             = " INPUT";
	private static final String LBL_ALGORITHM_CAPTION = "Algorithm: ";
	private static final String LBL_ALGORITHM         = LBL_ALGORITHM_CAPTION + LBL_NONE_CHOOSEN;
	private static final String LBL_OUTPUT            = " OUTPUT";
	
	private static final String LBL_LEX  = "LEX";
	private static final String LBL_MBEA = "MBEA";
	private static final String LBL_MICA = "MICA";
	
	private static final String LBL_COMPUTE = "Compute";
	private static final String LBL_STOP    = "Stop";
	
	// action commands for menu and buttons (have to be unique)
	private static final String ACT_MENU_LOAD_INPUT     = "load input";
	private static final String ACT_MENU_SAVE_OUTPUT    = "save output";
	private static final String ACT_MENU_ALGORITHM_LEX  = "algorithm LEX";
	private static final String ACT_MENU_ALGORITHM_MBEA = "algorithm MBEA";
	private static final String ACT_MENU_ALGORITHM_MICA = "algorithm MICA";
	private static final String ACT_MENU_HELP_DIALOG    = "help dialog";
	private static final String ACT_MENU_HELP_ABOUT     = "help about";	
	private static final String ACT_BTN_COMPUTE         = "compute";
	
	// fonts
	private static final Font FONT_PLAIN = new Font(Font.MONOSPACED, Font.PLAIN, 16);
	
	// help file name
	private static final String HELP_FILE = "help.htm";
	
	// icon
	private final static String ICON = "icon.png";
	
	/* =======================================================================================
	 * 
	 *   fields
	 * 
	 * =======================================================================================
	 */
	
	// windows
	private JFrame frame;
	private JDialog helpDialog;
	
	// accessible components
	private JLabel lblAlgorithm;
	private JTextArea txtInput;
	private JTextArea txtOutput;
	private JButton btnCompute;
	private JMenu menuInput;
	private JMenu menuAlgorithm;
	private JMenu menuOutput;
	private JRadioButtonMenuItem algorithmLEX;
	private JRadioButtonMenuItem algorithmMBEA;
	private JRadioButtonMenuItem algorithmMICA;
	
	// flags
	private boolean graphLoaded = false;
	private boolean algorithmChosen = false;
	private boolean isComputing = false;
	
	// algorithm
	private static MaximalBicliquesAlgorithm<String, Integer> mba;
	
	// computation thread
	private Computation computation;

	// file chooser
	private JFileChooser fileChooser;
	
	// graph
	private Graph<String, Integer> graph;

	// set of maximal bicliques
	private Set<Biclique<String, Integer>> setOfMaxBicliques;
	
	/**
	 * Constructs GUI.
	 */
	@Override
	public void run() {
		
		/* =======================================================================================
		 * 
		 *   create other stuff
		 * 
		 * =======================================================================================
		 */
		
		// create file chooser implementing security confirmation (overwrite existing file)
		fileChooser = new JFileChooser() {
			
			private static final long serialVersionUID = -967216909278102943L;

			@Override
		    public void approveSelection() {
		        File file = getSelectedFile();
		        if (file.exists() && getDialogType() == SAVE_DIALOG) {
		            int result = JOptionPane.showConfirmDialog(this,
		            		"The file already exists, overwrite?",
		            		"File exists", JOptionPane.YES_NO_CANCEL_OPTION);
		            
		            switch (result) {
		            case JOptionPane.YES_OPTION:
		            	super.approveSelection();
		            	return;
		            	
		            case JOptionPane.CANCEL_OPTION:
		                cancelSelection();
		                return;
		                
		            default:
		            	return;
		            }
		        }
		        super.approveSelection();
		    }        
		};
		
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		
		/* =======================================================================================
		 * 
		 *   define components
		 * 
		 * =======================================================================================
		 */
		
		// lblInput
		JLabel lblInput = new JLabel(LBL_INPUT);
		
		// txtInput
		txtInput = new JTextArea();
		txtInput.setText(LBL_NO_GRAPH);
		txtInput.setFont(FONT_PLAIN);
		txtInput.setEditable(false);		
		JScrollPane inputScrollPane = new JScrollPane(txtInput);		
		
		// lblOutput
		JLabel lblOutput = new JLabel(LBL_OUTPUT);
				
		// lblAlgorithm
		lblAlgorithm = new JLabel(LBL_ALGORITHM, SwingConstants.CENTER);
		
		// txtOutput
		txtOutput = new JTextArea();
		txtOutput.setText(LBL_NOT_COMPUTED);
		txtOutput.setFont(FONT_PLAIN);
		txtOutput.setEditable(false);		
		JScrollPane outputScrollPane = new JScrollPane(txtOutput);
		
		// btnCompute
		btnCompute = new JButton(LBL_COMPUTE);
		btnCompute.setEnabled(false);
		btnCompute.addActionListener(this);
		btnCompute.setActionCommand(ACT_BTN_COMPUTE);
		btnCompute.setPreferredSize(new Dimension(100, 25));
				
		/* =======================================================================================
		 * 
		 *   create panels and add components
		 * 
		 * =======================================================================================
		 */
		final int captionHeight = 30;
		final int borderWidth = 10;
		
		// input caption
		JPanel inputCaptionPanel = new JPanel(new GridLayout(1, 1));
		inputCaptionPanel.add(lblInput);
		inputCaptionPanel.setPreferredSize(new Dimension(0, captionHeight));
		
		// input panel
		JPanel inputPanel = new JPanel(new BorderLayout());
		inputPanel.add(inputCaptionPanel, BorderLayout.PAGE_START);
		inputPanel.add(inputScrollPane, BorderLayout.CENTER);
		//inputPanel.add(Box.createRigidArea(new Dimension(0, 10)), BorderLayout.PAGE_END);
		inputPanel.add(Box.createRigidArea(new Dimension(borderWidth, 0)), BorderLayout.LINE_START);
		inputPanel.add(Box.createRigidArea(new Dimension(borderWidth, 0)), BorderLayout.LINE_END);
		
		// output caption
		JPanel outputCaptionPanel = new JPanel(new BorderLayout());
		outputCaptionPanel.add(lblOutput, BorderLayout.LINE_START);
		outputCaptionPanel.add(lblAlgorithm, BorderLayout.CENTER);
		outputCaptionPanel.add(btnCompute, BorderLayout.LINE_END);
		outputCaptionPanel.setPreferredSize(new Dimension(0, captionHeight));
		
		// output panel
		JPanel outputPanel = new JPanel(new BorderLayout());
		outputPanel.add(outputCaptionPanel, BorderLayout.PAGE_START);
		outputPanel.add(outputScrollPane, BorderLayout.CENTER);
		outputPanel.add(Box.createRigidArea(new Dimension(0, borderWidth)), BorderLayout.PAGE_END);
		outputPanel.add(Box.createRigidArea(new Dimension(borderWidth, 0)), BorderLayout.LINE_START);
		outputPanel.add(Box.createRigidArea(new Dimension(borderWidth, 0)), BorderLayout.LINE_END);
		
		// panel
		JPanel panel = new JPanel(new GridLayout(2, 1));
		panel.add(inputPanel);
		panel.add(outputPanel);
		
		/* =======================================================================================
		 * 
		 *   create menu
		 * 
		 * =======================================================================================
		 */
		
		// menu
		JMenuBar menu = new JMenuBar();
		
		// menu input
		menuInput = new JMenu("Input");
		JMenuItem loadInput = new JMenuItem("Load...");
		loadInput.addActionListener(this);
		loadInput.setActionCommand(ACT_MENU_LOAD_INPUT);
		menuInput.add(loadInput);
		
		// menu output
		menuOutput = new JMenu("Output");
		menuOutput.setEnabled(false);
		JMenuItem saveOutput = new JMenuItem("Save...");
		saveOutput.addActionListener(this);
		saveOutput.setActionCommand(ACT_MENU_SAVE_OUTPUT);
		menuOutput.add(saveOutput);
		
		// menu algorithm
		menuAlgorithm = new JMenu("Algorithm");
		algorithmLEX = new JRadioButtonMenuItem(LBL_LEX, false);
		algorithmLEX.addActionListener(this);
		algorithmLEX.setActionCommand(ACT_MENU_ALGORITHM_LEX);
		algorithmMBEA = new JRadioButtonMenuItem(LBL_MBEA, false);
		algorithmMBEA.addActionListener(this);
		algorithmMBEA.setActionCommand(ACT_MENU_ALGORITHM_MBEA);
		// TODO
		algorithmMBEA.setEnabled(false);
		// TODO
		algorithmMICA = new JRadioButtonMenuItem(LBL_MICA, false);
		algorithmMICA.addActionListener(this);
		algorithmMICA.setActionCommand(ACT_MENU_ALGORITHM_MICA);
		ButtonGroup group = new ButtonGroup();
		group.add(algorithmLEX);
		group.add(algorithmMBEA);
		group.add(algorithmMICA);
		menuAlgorithm.add(algorithmLEX);
		menuAlgorithm.add(algorithmMBEA);
		menuAlgorithm.add(algorithmMICA);
		
		// menu help
		JMenu menuHelp = new JMenu("Help");
		JMenuItem helpDialog = new JMenuItem("Help...");
		helpDialog.addActionListener(this);
		helpDialog.setActionCommand(ACT_MENU_HELP_DIALOG);
		JMenuItem helpAbout = new JMenuItem("About...");
		helpAbout.addActionListener(this);
		helpAbout.setActionCommand(ACT_MENU_HELP_ABOUT);
		menuHelp.add(helpDialog);
		menuHelp.add(helpAbout);
		
		// add menus
		menu.add(menuInput);
		menu.add(menuOutput);
		menu.add(menuAlgorithm);
		menu.add(menuHelp);
		
		/* =======================================================================================
		 * 
		 *   create frame and add components
		 * 
		 * =======================================================================================
		 */
		
		// create frame
		frame = new JFrame(LBL_CAPTION);
		frame.setPreferredSize(new Dimension(MIN_WIDTH, MIN_HEIGHT));
		frame.setMinimumSize(frame.getPreferredSize());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(ICON));
		
		// add window listener for closing event
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				// interrupt thread if applicable
				if (computation != null)
					computation.interrupt();
			}
		});
		
		// set panel and menu into frame and show frame
		frame.setJMenuBar(menu);
		frame.setContentPane(panel);
		frame.setVisible(true);
		
	}

	/**
	 * Main method to create and run GUI.
	 * @param args not used.
	 */
	public static void main(String[] args) {
		MaxBicliquesGUI mbGUI = new MaxBicliquesGUI();
		SwingUtilities.invokeLater(mbGUI);
	}

	/**
	 * Handles action events.
	 */
	@Override
	public void actionPerformed(ActionEvent event) {
		switch (event.getActionCommand()) {
		
		case ACT_MENU_LOAD_INPUT:
			// open dialog
			if (fileChooser.showOpenDialog(frame) != JFileChooser.APPROVE_OPTION)
				return;
			// try to load graph
			graph = loadGraph(fileChooser.getSelectedFile());
			graphLoaded = (graph != null);
			if (graphLoaded) {
				showGraph(graph);
				if (algorithmChosen)
					btnCompute.setEnabled(true);
				txtOutput.setText(LBL_NOT_COMPUTED);
				menuOutput.setEnabled(false);				
			} else {
				txtInput.setText(LBL_NO_GRAPH);
				btnCompute.setEnabled(false);
				txtOutput.setText(LBL_NOT_COMPUTED);
				menuOutput.setEnabled(false);				
			}
			return;
			
		case ACT_MENU_SAVE_OUTPUT:
			// save dialog
			if (fileChooser.showSaveDialog(frame) != JFileChooser.APPROVE_OPTION)
				return;
			// try to save bicliques
			if (setOfMaxBicliques != null)
				saveBicliques(fileChooser.getSelectedFile(), setOfMaxBicliques);
			return;
			
		case ACT_MENU_ALGORITHM_LEX:
			lblAlgorithm.setText(LBL_ALGORITHM_CAPTION + LBL_LEX);
			mba = new LEX<>();
			setGUIafterAlgorithmChosen();
			return;
			
		case ACT_MENU_ALGORITHM_MBEA:
			lblAlgorithm.setText(LBL_ALGORITHM_CAPTION + LBL_MBEA);
			// TODO
			// mba = new MBEA();
			setGUIafterAlgorithmChosen();
			return;
			
		case ACT_MENU_ALGORITHM_MICA:
			lblAlgorithm.setText(LBL_ALGORITHM_CAPTION + LBL_MICA);
			mba = new MICA<>();
			setGUIafterAlgorithmChosen();
			return;
			
		case ACT_MENU_HELP_DIALOG:
			showHelpDialog();
			return;
			
		case ACT_MENU_HELP_ABOUT:
			JOptionPane.showMessageDialog(frame,
				"Version 1.0, June 2019\n"
				+ "Mykyta Ielanskyi\n"
				+ "Roland Koppenberger\n"
				+ "Hadi Sanaei",
				"Maximal bicliques",
				JOptionPane.INFORMATION_MESSAGE,
				new ImageIcon(ICON));
			return;
			
		case ACT_BTN_COMPUTE:
			btnCompute.setEnabled(false);
			menuOutput.setEnabled(false);
			if (isComputing) {
				// interrupt simulation
				isComputing = false;
				if (computation != null)
					computation.interrupt();				
				txtOutput.setText(LBL_COMPUTATION_ABORTED);
				btnCompute.setText(LBL_COMPUTE);
				menuInput.setEnabled(true);
				menuAlgorithm.setEnabled(true);
			} else {
				// start computation
				isComputing = true;
				txtOutput.setText(LBL_IS_COMPUTING);
				btnCompute.setText(LBL_STOP);
				menuInput.setEnabled(false);
				menuAlgorithm.setEnabled(false);
				computation = new Computation();
				computation.start();
			}
			btnCompute.setEnabled(true);
			return;
		}
	}
	
	/**
	 * Loads an undirected graph from a text file.<p>
	 * <b>Fileformat:</b>
	 * <ul>
	 * <li>Each line represents a vertex, the content is the vertex name. 
	 * <li>Lines consisting only of white spaces or empty ones are ignored.
	 * <li>Two consecutive lines build an edge.
	 * <li>A remaining single vertex is not allowed.
	 * </ul> 
	 * @param file File to load.
	 * @return True if graph is loaded, false otherwise.
	 */
	private Graph<String, Integer> loadGraph(File file) {
	    Graph<String, Integer> graph = GraphVendingMachine.lemmeHaveAnEmptyGraph(false);
	    
	    int edgeNumber = 1;
	    
		try(BufferedReader reader = new BufferedReader(new FileReader(file))) {
			
		    String firstLine;
		    String secondLine;
		    
		    outer: while ((firstLine = reader.readLine()) != null) {
		    	
		    	// skip empty first line
				firstLine = firstLine.trim();
		    	while (firstLine.equals("")) {
					firstLine = reader.readLine();
					if (firstLine == null)
						break outer;
					firstLine = firstLine.trim();
		    	}
		    	
		    	// read second line
		    	if ((secondLine = reader.readLine()) == null) {
					JOptionPane.showMessageDialog(frame,
							"Remaining single vertex in data.",
							"Graph could not be loaded",
							JOptionPane.ERROR_MESSAGE);
		    		return null;
		    	}
		    	
		    	// skip empty second line
		    	secondLine = secondLine.trim();
		    	while (secondLine.equals("")) {
		    		secondLine = reader.readLine();
					if (secondLine == null)
						break outer;
					secondLine = secondLine.trim();
		    	}
		    	
		    	// add edge to graph
		    	graph.addEdge(edgeNumber++, firstLine, secondLine);
			}
			
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(frame,
					"File" + file.getName() + "not found.",
					"Graph could not be loaded",
					JOptionPane.ERROR_MESSAGE);
    		return null;
		
		} catch (IOException e) {
			JOptionPane.showMessageDialog(frame,
					"I/O-Error: " + e.getMessage() + ".",
					"Graph could not be loaded",
					JOptionPane.ERROR_MESSAGE);
    		return null;
		
		}
		
		return graph;
	}
	
	/**
	 * Saves an set of bicliques in a text file.<p>
	 * <b>Fileformat:</b>
	 * <ul>
	 * <li>Each line represents a vertex, the content is the vertex name. 
	 * <li>After each partition of vertices an empty line is placed.
	 * </ul> 
	 * @param file File to save.
	 * @param setOfMaxBicliques Set of bicliques to save.
	 */
	private void saveBicliques(File file, Set<Biclique<String, Integer>> setOfMaxBicliques) {
		
	    try(BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
	    	for (Biclique<String, Integer> bic : setOfMaxBicliques) {
	    		// write left partition of biclique
	    		for (Vertex<String> v : bic.getLeft()) {
	    			writer.write(v.getElem());
		    		writer.newLine();
	    		}
	    		writer.newLine();
	    		// write right partition of biclique
	    		for (Vertex<String> v : bic.getRight()) {
	    			writer.write(v.getElem());
		    		writer.newLine();
	    		}
	    		writer.newLine();
	    	}
		} catch (IOException e) {
			JOptionPane.showMessageDialog(frame,
					"I/O-Error: " + e.getMessage() + ".",
					"Bicliques could not be saved",
					JOptionPane.ERROR_MESSAGE);
		}
	}
	
	/**
	 * Displays graph data.
	 * @param graph Graph to display.
	 */
	private void showGraph(Graph<String, Integer> graph) {
		int numVertices = graph.getVertexCount();
		int numEdges = graph.getEdgeCount();
		String txt = "Graph with ";
		txt += (numVertices == 1 ? "1 vertex" : numVertices + " vertices");
		txt += " and ";
		txt += (numEdges == 1 ? "1 edge" : numEdges + " edges");
		txt += "\n\n";
		if (numVertices < 20) {
			txt += graph.prettyPrintVertices();
		} else {
			txt += "(too many vertices to show)";
		}
		txt += "\n\n";
		if (numEdges < 20) {
			txt += graph.prettyPrintEdges();
		} else {
			txt += "(too many edges to show)";
		}
		txt += "\n";
		txtInput.setText(txt);
	}
	
	/**
	 * Prepare GUI after algorithm has been chosen.
	 */
	private void setGUIafterAlgorithmChosen() {
		algorithmChosen = true;
		if (graphLoaded)
			btnCompute.setEnabled(true);
		txtOutput.setText(LBL_NOT_COMPUTED);
		menuOutput.setEnabled(false);
	}

	/**
	 * Prepare GUI after computation failed.
	 */
	private void setGUIafterComputationFailed(String msg) {
		btnCompute.setEnabled(false);
		menuOutput.setEnabled(false);
		isComputing = false;
		txtOutput.setText(msg);
		btnCompute.setText(LBL_COMPUTE);
		menuInput.setEnabled(true);
		menuAlgorithm.setEnabled(true);
		btnCompute.setEnabled(true);
	}

	/**
	 * Displays non modal help dialog.
	 */
	private void showHelpDialog() {
		
		// create help dialog if not existing
		if (helpDialog == null) {
		
			// editor pane in scroll pane
			JEditorPane edit = new JEditorPane();
			JScrollPane scrollPane = new JScrollPane(edit);
			
			// load content
			File file = new File(HELP_FILE);
			try {
				edit.setPage(file.toURI().toURL());
			} catch (IOException e) {
				edit.setText(
					"<b>We are very sorry but the content cannot be loaded.</b><p>" + e.getMessage()
				);
			}
			edit.setEditable(false);
			
			// close button in button pane
			JButton btnClose = new JButton("Close");
			JPanel buttonPane = new JPanel();
			buttonPane.add(btnClose);
			
			// scroll and button panes in panel
			JPanel panel = new JPanel(new BorderLayout());
			panel.add(scrollPane, BorderLayout.CENTER);
			panel.add(buttonPane, BorderLayout.PAGE_END);
			
			// create dialog
			helpDialog = new JDialog(frame, "Help", false);		
			helpDialog.setPreferredSize(new Dimension(MIN_WIDTH, MIN_HEIGHT));
			helpDialog.setMinimumSize(helpDialog.getPreferredSize());
			
			// panel in dialog
			helpDialog.setContentPane(panel);
			
			// action for close button
			btnClose.addActionListener(e->helpDialog.setVisible(false));
		}
		
		// show dialog
		helpDialog.setVisible(true);
	}
	
}