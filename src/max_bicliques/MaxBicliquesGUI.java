/**
 * 
 */
package max_bicliques;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

/**
 * @author Roland Koppenberger
 * @version 1.0, May 24th 2019.
 */
public class MaxBicliquesGUI implements Runnable, ActionListener {

	/* =======================================================================================
	 * 
	 *   constants
	 * 
	 * =======================================================================================
	 */
	
	private static final int MIN_WIDTH  = 640;
	private static final int MIN_HEIGHT = 480;
	
	// labels
	private static final String LBL_CAPTION      = "Maximal bicliques of bipartite graph";	
	private static final String LBL_INPUT        = "Input: Bipartite graph (U, V, E)";
	private static final String LBL_OUTPUT       = "Output: Maximal bicliques {(U_k, V_k)}";
	private static final String LBL_SIZE         = "Size: ";
	private static final String LBL_MAXBICLIQUES = " maximal biclique(s)";
	private static final String LBL_ALGORITHM    = "Algorithm: ";	
	private static final String LBL_NOT_COMPUTED = "\n  (not computed)";	
	private static final String LBL_NO_ALGORITHM = "(none)";
	private static final String LBL_MICA         = "MICA";	
	private static final String LBL_COMPUTE      = "Compute";
	
	// action commands for menu and buttons (have to be unique)
	private static final String ACT_MENU_LOAD_INPUT      = "load input";
	private static final String ACT_MENU_SAVE_OUTPUT     = "save output";
	private static final String ACT_MENU_ALGORITHM_MICA  = "algorithm MICA";
	private static final String ACT_MENU_HELP_DIALOG     = "help dialog";
	private static final String ACT_MENU_HELP_ABOUT      = "help about";	
	private static final String ACT_BTN_COMPUTE          = "compute";
	
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
	private JTextField txtMaxU;
	private JTextField txtMaxV;
	private JLabel lblMinV;
	private JTextArea txtEdges;
	private JTextArea txtOutput;
	private JLabel lblAlgorithm;
	private JLabel lblSize;
	private JButton btnCompute;
	private JRadioButtonMenuItem algorithmMICA;
		
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
		
		// input label
		JLabel lblInput = new JLabel(LBL_INPUT);
		
		// input vertices U and V
		txtMaxU = new JTextField("1");
		txtMaxU.setFont(FONT_PLAIN);
		txtMaxU.setColumns(10);
		txtMaxV = new JTextField("2");
		txtMaxV.setFont(FONT_PLAIN);
		txtMaxV.setColumns(10);
		lblMinV = new JLabel("4");
		
		// input edges and embed into scroll pane
		txtEdges = new JTextArea();
		txtEdges.setText("");
		txtEdges.setFont(FONT_PLAIN);
		txtEdges.setEditable(true);
		JScrollPane scrollPaneForEdges = new JScrollPane(txtEdges);
		
		// output label
		JLabel lblOutput = new JLabel(LBL_OUTPUT);
		
		// output JTextArea
		txtOutput = new JTextArea();
		txtOutput.setText(LBL_NOT_COMPUTED);
		txtOutput.setFont(FONT_PLAIN);
		txtOutput.setEditable(false);
		
		// embed into scroll pane
		JScrollPane outputScrollPane = new JScrollPane(txtOutput);
		
		// size label
		lblSize = new JLabel(LBL_SIZE + 0 + LBL_MAXBICLIQUES);
		
		// algorithm label
		lblAlgorithm = new JLabel(LBL_ALGORITHM + LBL_NO_ALGORITHM, SwingConstants.CENTER);
		
		// button compute
		btnCompute = new JButton(LBL_COMPUTE);
		btnCompute.setEnabled(false);
		btnCompute.addActionListener(this);
		btnCompute.setActionCommand(ACT_BTN_COMPUTE);
				
		/* =======================================================================================
		 * 
		 *   create panels and add components
		 * 
		 * =======================================================================================
		 */
				
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
		
		/* =======================================================================================
		 * 
		 *   create menu
		 * 
		 * =======================================================================================
		 */
		
		// menu
		JMenuBar menu = new JMenuBar();
		
		// menu input
		JMenu menuInput = new JMenu("Input");
		JMenuItem loadInput = new JMenuItem("Load...");
		loadInput.addActionListener(this);
		loadInput.setActionCommand(ACT_MENU_LOAD_INPUT);
		menuInput.add(loadInput);
		
		// menu output
		JMenu menuOutput = new JMenu("Output");
		JMenuItem saveOutput = new JMenuItem("Save...");
		saveOutput.addActionListener(this);
		saveOutput.setActionCommand(ACT_MENU_SAVE_OUTPUT);
		menuOutput.add(saveOutput);
		
		// menu algorithm
		JMenu menuAlgorithm = new JMenu("Algorithm");
		algorithmMICA = new JRadioButtonMenuItem(LBL_MICA, false);
		algorithmMICA.addActionListener(this);
		algorithmMICA.setActionCommand(ACT_MENU_ALGORITHM_MICA);
		ButtonGroup group = new ButtonGroup();
		group.add(algorithmMICA);
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
		
/*		// add window listener for closing event
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				// interrupt thread if applicable
				if (simulation != null)
					simulation.interrupt();
			}
		});
		
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
			return;
			
		case ACT_MENU_SAVE_OUTPUT:
			return;
			
		case ACT_MENU_ALGORITHM_MICA:
			lblAlgorithm.setText(LBL_ALGORITHM + LBL_MICA);
			btnCompute.setEnabled(true);
			return;
			
		case ACT_MENU_HELP_DIALOG:
			return;
			
		case ACT_MENU_HELP_ABOUT:
			return;
			
		case ACT_BTN_COMPUTE:
			return;
		}
	}

}
