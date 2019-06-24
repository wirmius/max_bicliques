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

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
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
import bicliques.algorithms.MICA;
import bicliques.algorithms.MaximalBicliquesAlgorithm;
import bicliques.graphs.Graph;
import bicliques.graphs.GraphVendingMachine;

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
			if (mba != null)
				// TODO: return value Biclique[]
				mba.findMaxBicliques(graph);
			if (isInterrupted())
				return;
			btnCompute.setEnabled(false);
			isComputing = false;
			txtOutput.setText("set of maximal bicliques computed");
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
	
	private boolean graphLoaded = false;
	private boolean algorithmChosen = false;
	private boolean isComputing = false;
	
	// algorithm
	private static MaximalBicliquesAlgorithm mba;
	
	// computation thread
	private Computation computation;
	
	// graph
	private Graph<String, String> graph;
	
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
/*		
		// create file chooser implementing security confirmation (overwrite existing file)
		fileChooser = new JFileChooser() {
			
			private static final long serialVersionUID = -967216909278102943L;

			@Override
		    public void approveSelection() {
		        File file = getSelectedFile();
		        if (file.exists() && getDialogType() == SAVE_DIALOG) {
		            int result = JOptionPane.showConfirmDialog(this,
		            		MSG_FILE_EXISTS_OVERWRITE,
		            		TITLE_FILE_EXISTS, JOptionPane.YES_NO_CANCEL_OPTION);
		            
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
*/
		
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
		
/*				
		// input page start panel
		JPanel inputStartPage = new JPanel(new BorderLayout());
		inputStartPage.add(lblInput, BorderLayout.LINE_START);
		inputStartPage.setPreferredSize(new Dimension(0, 25));
		
		// input panel for vertices U
		JPanel inputPanelForU = new JPanel(new FlowLayout(FlowLayout.LEFT));
		inputPanelForU.add(new JLabel("U: "));
		inputPanelForU.add(new JLabel("nodes from 1 to"));
		inputPanelForU.add(txtMaxU);
		
		// input panel for vertices V
		JPanel inputPanelForV = new JPanel(new FlowLayout(FlowLayout.LEFT));
		inputPanelForV.add(new JLabel("V: "));
		inputPanelForV.add(new JLabel("nodes from"));
		inputPanelForV.add(lblMinV);
		inputPanelForV.add(new JLabel("to"));
		inputPanelForV.add(txtMaxV);
		
		// input panel for vertices
		JPanel inputPanelForVertices = new JPanel(new GridLayout(2,1));
		inputPanelForVertices.add(inputPanelForU);
		inputPanelForVertices.add(inputPanelForV);
		
		// input panel for edges
		JPanel inputPanelForEdges = new JPanel(new BorderLayout());
		inputPanelForEdges.add(new JLabel("  E: "), BorderLayout.LINE_START);
		inputPanelForEdges.add(scrollPaneForEdges, BorderLayout.CENTER);
		
		// input panel for vertices and edges
		JPanel inputPanelForVE = new JPanel(new BorderLayout());
		inputPanelForVE.add(inputPanelForVertices, BorderLayout.PAGE_START);
		inputPanelForVE.add(inputPanelForEdges, BorderLayout.CENTER);
		
		// input panel
		JPanel inputPanel = new JPanel(new BorderLayout());
		inputPanel.add(inputStartPage, BorderLayout.PAGE_START);
		inputPanel.add(inputPanelForVE, BorderLayout.CENTER);
		
		// output page start panel
		JPanel outputStartPage = new JPanel(new BorderLayout());
		outputStartPage.add(lblOutput, BorderLayout.LINE_START);
		outputStartPage.add(lblAlgorithm, BorderLayout.CENTER);
		outputStartPage.add(btnCompute, BorderLayout.LINE_END);
		outputStartPage.setPreferredSize(new Dimension(0, 25));
		
		// output page end panel
		JPanel outputEndPage = new JPanel(new FlowLayout(FlowLayout.LEFT));
		outputEndPage.add(lblSize);
		outputEndPage.setPreferredSize(new Dimension(0, 25));
		
		// output panel
		JPanel outputPanel = new JPanel(new BorderLayout());
		outputPanel.add(outputStartPage, BorderLayout.PAGE_START);
		outputPanel.add(outputScrollPane, BorderLayout.CENTER);
		outputPanel.add(outputEndPage, BorderLayout.PAGE_END);
		
		// create panel for input and output
		JPanel inOut = new JPanel(new GridLayout(2, 1));
		inOut.add(inputPanel);
		inOut.add(outputPanel);
		
		// create panel
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(Box.createRigidArea(new Dimension(10, 0)), BorderLayout.LINE_START);
		panel.add(inOut, BorderLayout.CENTER);
		panel.add(Box.createRigidArea(new Dimension(10, 0)), BorderLayout.LINE_END);
*/		
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
		
		// TODO
/*
		// add component listener for resizing event
		frame.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				showEcosystem();
		    }			
		});
*/		
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
			// TODO
			graph = GraphVendingMachine.lemmeHaveAnEmptyGraph(false);
			txtInput.setText("graph is loaded");
			// TODO
			graphLoaded = true;
			if (algorithmChosen) {
				btnCompute.setEnabled(true);
			}
			txtOutput.setText(LBL_NOT_COMPUTED);
			menuOutput.setEnabled(false);
			return;
			
		case ACT_MENU_SAVE_OUTPUT:
			// TODO
			return;
			
		case ACT_MENU_ALGORITHM_LEX:
			lblAlgorithm.setText(LBL_ALGORITHM_CAPTION + LBL_LEX);
			mba = new MICA(); // TODO
			setAlgorithmChosen();
			return;
			
		case ACT_MENU_ALGORITHM_MBEA:
			lblAlgorithm.setText(LBL_ALGORITHM_CAPTION + LBL_MBEA);
			mba = new MICA(); // TODO
			setAlgorithmChosen();
			return;
			
		case ACT_MENU_ALGORITHM_MICA:
			lblAlgorithm.setText(LBL_ALGORITHM_CAPTION + LBL_MICA);
			mba = new MICA();
			setAlgorithmChosen();
			return;
			
		case ACT_MENU_HELP_DIALOG:
			// TODO
			return;
			
		case ACT_MENU_HELP_ABOUT:
			JOptionPane.showMessageDialog(frame,
					"Version 1.0, June 2019\n"
					+ "Roland Koppenberger\n"
					+ "Mykyta Ielanskyi\n"
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
	 * Prepare GUI after algorithm has been chosen.
	 */
	private void setAlgorithmChosen() {
		algorithmChosen = true;
		if (graphLoaded)
			btnCompute.setEnabled(true);
		txtOutput.setText(LBL_NOT_COMPUTED);
		menuOutput.setEnabled(false);
	}

}