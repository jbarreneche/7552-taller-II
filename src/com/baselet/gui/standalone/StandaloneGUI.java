package com.baselet.gui.standalone;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ToolTipManager;
import javax.swing.WindowConstants;

import ar.uba.fi.taller2.tp.control.context.AppContext;

import com.baselet.control.Constants;
import com.baselet.control.Constants.Program;
import com.baselet.control.Constants.ProgramName;
import com.baselet.control.Main;
import com.baselet.control.Path;
import com.baselet.control.Utils;
import com.baselet.diagram.CustomPreviewHandler;
import com.baselet.diagram.DiagramHandler;
import com.baselet.diagram.DrawPanel;
import com.baselet.diagram.PaletteHandler;
import com.baselet.diagram.io.DiagramFileHandler;
import com.baselet.diagram.io.Logger;
import com.baselet.gui.BaseGUI;
import com.baselet.gui.MenuFactory;
import com.baselet.gui.MenuFactorySwing;
import com.baselet.gui.OwnSyntaxPane;
import com.baselet.gui.TabComponent;
import com.baselet.gui.actionlistener.EntidadActionListener;
import com.baselet.gui.actionlistener.EntidadGlobalActionListener;
import com.baselet.gui.actionlistener.RelacionActionListener;
import com.baselet.gui.actionlistener.ValidarActionListener;
import com.baselet.gui.actionlistener.ValidarTodoActionListener;
import com.baselet.gui.listener.DividerListener;
import com.baselet.gui.listener.GUIListener;
import com.baselet.gui.listener.PaletteComboBoxListener;
import com.umlet.custom.CustomElementHandler;
import com.umlet.gui.CustomElementPanel;

@SuppressWarnings("serial")
public class StandaloneGUI extends BaseGUI {

	private final static Logger log = Logger.getLogger(Utils.getClassName());

	private MenuFactorySwing menuFactory;
	private JComboBox zoomComboBox;
	private JFrame window;
	private JSplitPane customSplit;
	private JSplitPane rightSplit;
	private JSplitPane mainSplit;
	private JSplitPane mailSplit;
	private JPanel diagramspanel;
	private JPanel palettepanel;
	private JTabbedPane diagramtabs;
	private JMenu editMenu;
	private JMenuItem editUndo;
	private JMenuItem editRedo;
	private JMenuItem editDelete;
	private JMenuItem editSelectAll;
	private JMenuItem editGroup;
	private JMenuItem editUngroup;
	private JMenuItem editCut;
	private JMenuItem editCopy;
	private JMenuItem editPaste;
	private JMenuItem customEntidad;
	private JMenuItem validarTodoElProyecto;
	private JMenuItem customRelacion = null;
	private JMenuItem customEntidadConAtributos;
	private JMenuItem customEditar;
	private JTextField searchField;
	private OwnSyntaxPane propertyTextPane;
	private ZoomListener zoomListener;
//	private PaletteEntityListener entityListener;
	private JPanel rightPanel;
	private JPanel topPanel;
	private JPanel treePanelControl;
	private JComboBox paletteList;

	protected String selected_palette;

	private CustomElementHandler customelementhandler;
	private CustomElementPanel customPanel;
	private boolean custom_element_selected;
	private boolean custom_panel_visible;

	private JButton validarButton;
	private JButton validarProyButton;
	private JButton entidadButton;
	private JButton entidadGButton;
	private JButton relacionButton;

	public StandaloneGUI(Main main) {
		super(main);
		custom_element_selected = false;
		custom_panel_visible = false;
		selected_palette = "";
	}

	private void onDiagramOpened() {
		DrawPanel p = this.getCurrentDiagram();
		if (p != null)
			Main.getInstance().setCurrentDiagramHandler(p.getHandler());
		else
			Main.getInstance().setCurrentDiagramHandler(null);
	}

	private void onDiagramClosed() {
		DrawPanel p = this.getCurrentDiagram();
		if (p != null)
			Main.getInstance().setCurrentDiagramHandler(p.getHandler());
		else
			Main.getInstance().setCurrentDiagramHandler(null);
	}

	@Override
	public void updateDiagramName(DiagramHandler diagram, String name) {
		int index = this.diagramtabs.indexOfComponent(diagram.getDrawPanel()
				.getScrollPane());
		if (index != -1) {
			this.diagramtabs.setTitleAt(index, name);
		}
		this.diagramtabs.updateUI();
	}

	@Override
	public void setDiagramChanged(DiagramHandler diagram, boolean changed) {
		String change_string = "";
		if (changed)
			change_string = " *";

		this.updateDiagramName(diagram, diagram.getName() + change_string);
	}

	@Override
	public void setCustomElementChanged(CustomElementHandler handler,
			boolean changed) {

	}

	@Override
	public void closeWindow() {
		if (Main.getInstance().askSaveIfDirty()) {
			this.main.closeProgram();
			this.window.dispose();
			System.exit(0);
		}
	}

	@Override
	protected void init() {

		this.addKeyListener(new GUIListener());

		this.window = new JFrame();
		this.window.setContentPane(this);
		this.window.addWindowListener(new WindowListener());
		this.window
				.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.window.setBounds(Constants.program_location.x,
				Constants.program_location.y, Constants.program_size.width,
				Constants.program_size.height);
		if (Program.PROGRAM_NAME.equals(ProgramName.UMLET))
			this.window.setTitle("Diagramas de Entidades y Relaciones");
		else if (Program.PROGRAM_NAME.equals(ProgramName.PLOTLET))
			this.window.setTitle("Plotlet - Free Tool for Fast Plots");

		/************************ SET PROGRAM ICON ************************/
		String iconPath = Path.homeProgram() + "img/" + "Logo-fiuba.gif";
		this.window.setIconImage(new ImageIcon(iconPath).getImage());
		/*************************************************************/

		/************************ CREATE PROP PANE *****************/
		this.propertyTextPane = this.createPropertyTextPane();

		/*********** SET WINDOW BOUNDS **************/
		if (Constants.start_maximized) {
			// If Main starts maximized we set fixed bounds and must set the
			// frame visible
			// now to avoid a bug where the right sidebar doesn't have the
			// correct size
			this.window.setExtendedState(this.window.getExtendedState()
					| Frame.MAXIMIZED_BOTH);
			this.window.setVisible(true);
		}

		/***************************************/

		paletteList = new JComboBox();
		paletteList.setMaximumRowCount(15);

		/*********** CREATE MENU *****************/
		JMenuBar menu = new JMenuBar();
		menuFactory = MenuFactorySwing.getInstance();

		/*********** CREATE PALLETTE MENU ********/
		// JMenuBar menuPallette = new JMenuBar();

		// Create Palettes and Edit Palette menuitem
		List<String> palettenames = this.main.getPaletteNames();
		for (String palette : palettenames) {
			this.paletteList.addItem(palette);
		}
		PaletteComboBoxListener pl = new PaletteComboBoxListener();
		this.paletteList.addActionListener(pl);
		this.paletteList.addMouseWheelListener(pl);

		JMenu fileMenu = new JMenu(MenuFactory.FILE);
		fileMenu.setMnemonic(KeyEvent.VK_F);
		fileMenu.add(menuFactory.createNewProject());
		fileMenu.add(menuFactory.createOpenProject());
		fileMenu.add(menuFactory.createNew());
//		fileMenu.add(menuFactory.createOpen());
//		fileMenu.add(menuFactory.createRecentFiles());
		fileMenu.add(menuFactory.createSave());
//		fileMenu.add(menuFactory.createSaveAs());
		fileMenu.add(menuFactory.createExportAs());
//		fileMenu.addSeparator();
//		fileMenu.add(menuFactory.createEditCurrentPalette());
		fileMenu.addSeparator();
		fileMenu.add(menuFactory.createOptions());
		fileMenu.addSeparator();
		fileMenu.add(menuFactory.createPrint());
		fileMenu.addSeparator();
		fileMenu.add(menuFactory.createExit());
		menu.add(fileMenu);

		editMenu = new JMenu(MenuFactory.EDIT);
		editMenu.setMnemonic(KeyEvent.VK_E);
		editMenu.add(editUndo = menuFactory.createUndo());
		editMenu.add(editRedo = menuFactory.createRedo());
		editMenu.add(editDelete = menuFactory.createDelete());
		editMenu.addSeparator();
		editMenu.add(editSelectAll = menuFactory.createSelectAll());
		editMenu.add(editGroup = menuFactory.createGroup());
		editMenu.add(editUngroup = menuFactory.createUngroup());
		editMenu.addSeparator();
		editMenu.add(editCut = menuFactory.createCut());
		editMenu.add(editCopy = menuFactory.createCopy());
		editMenu.add(editPaste = menuFactory.createPaste());
		menu.add(editMenu);
		editDelete.setEnabled(false);
		editGroup.setEnabled(false);
		editCut.setEnabled(false);
		editPaste.setEnabled(false);
		editUngroup.setEnabled(false);

		// Help Menu
		JMenu helpMenu = new JMenu(MenuFactory.HELP);
		helpMenu.setMnemonic(KeyEvent.VK_H);
		helpMenu.add(menuFactory.createAboutProgram());
		menu.add(helpMenu);

		// Search Field
		JPanel searchPanel = new JPanel();
		searchPanel.setOpaque(false);
		searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.X_AXIS));
		JLabel searchLabel = new JLabel("Buscar:   ");
		searchField = new JTextField(10);
		searchField.setMinimumSize(searchField.getPreferredSize());
		searchField.setMaximumSize(searchField.getPreferredSize());
		searchField.addKeyListener(new SearchListener());

		searchPanel.add(Box.createHorizontalGlue());
		searchPanel.add(Box.createRigidArea(new Dimension(50, 0)));
		searchPanel.add(searchLabel);
		searchPanel.add(searchField);
		searchPanel.add(Box.createRigidArea(new Dimension(20, 0)));

		// menu.add(searchPanel);

		/* Zoom field */

		JPanel zoomPanel = new JPanel();
		zoomPanel.setOpaque(false);
		zoomPanel.setLayout(new BoxLayout(zoomPanel, BoxLayout.X_AXIS));
		JLabel zoomLabel = new JLabel("Zoom:   ");
		zoomComboBox = new JComboBox();
		zoomComboBox.setPreferredSize(new Dimension(80, 24));
		zoomComboBox.setMinimumSize(zoomComboBox.getPreferredSize());
		zoomComboBox.setMaximumSize(zoomComboBox.getPreferredSize());
		zoomListener = new ZoomListener();
		zoomComboBox.addActionListener(zoomListener);
		zoomComboBox.addMouseWheelListener(zoomListener);
		zoomComboBox.setToolTipText("Use ± or Ctrl+mouse wheel to zoom");

		String[] zoomValues = Constants.zoomValueList
				.toArray(new String[Constants.zoomValueList.size()]);
		zoomComboBox.setModel(new DefaultComboBoxModel(zoomValues));
		zoomComboBox.setSelectedIndex(9);

		zoomPanel.add(zoomLabel);
		zoomPanel.add(zoomComboBox);
		zoomPanel.add(Box.createRigidArea(new Dimension(20, 0)));

		Dimension d = new Dimension(300, 60);

		// Add Validar button to Upper Menu
		String buttonPath = Path.homeProgram() + "img/" + "tilde.gif";
		Icon validarIcon = new ImageIcon(buttonPath);
		validarButton = new JButton("Validar diagrama", validarIcon);
		validarButton.setPreferredSize(d);
		validarButton.addActionListener(new ValidarActionListener(main));
		
		buttonPath = Path.homeProgram() + "img/" + "tilde.gif";
		Icon validarProyIcon = new ImageIcon(buttonPath);
		validarProyButton = new JButton("Validar proyecto", validarProyIcon);
		validarProyButton.setPreferredSize(d);
		validarProyButton.addActionListener(new ValidarTodoActionListener(main));

		// Add Entidad button to Upper Menu
		buttonPath = Path.homeProgram() + "img/" + "entidad.gif";
		Icon entidadIcon = new ImageIcon(buttonPath);
		entidadButton = new JButton("Entidad", entidadIcon);
		entidadButton.setPreferredSize(d);
		entidadButton.addActionListener(new EntidadActionListener(main));

		// Add EntidadGlobal button to Upper Menu
		buttonPath = Path.homeProgram() + "img/" + "entidad.gif";
		entidadIcon = new ImageIcon(buttonPath);
		entidadGButton = new JButton("Entidad Global", entidadIcon);
		entidadGButton.addActionListener(new EntidadGlobalActionListener(main));
		entidadGButton.setPreferredSize(d);
		
		// Add Relacion button to Upper Menu
		buttonPath = Path.homeProgram() + "img/" + "relacion.gif";
		Icon relacionIcon = new ImageIcon(buttonPath);
		relacionButton = new JButton("Relacion", relacionIcon);
		relacionButton.addActionListener(new RelacionActionListener(main));
		relacionButton.setPreferredSize(d);
		
		// Add Relacion button to Upper Menu
		buttonPath = Path.homeProgram() + "img/" + "relacion.gif";
		relacionIcon = new ImageIcon(buttonPath);

		// Set the finished menu
		this.window.setJMenuBar(menu);
		// this.window.setJMenuBar(menuPallette);

		/************************ CREATE SUB PANELS ******************/

		// create custom element handler
		this.customelementhandler = new CustomElementHandler();
		this.customPanel = this.customelementhandler.getPanel();

		diagramspanel = new JPanel();
		this.palettepanel = new JPanel(new CardLayout());
		new FileDrop(diagramspanel, new FileDropListener()); // enable drag&drop
																// from desktop
																// into
																// diagrampanel

		rightSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, palettepanel,
				this.propertyTextPane.getPanel());
		rightSplit.setDividerSize(2);
		rightSplit.setDividerLocation(Constants.right_split_position);
		rightSplit.setResizeWeight(1);
		rightSplit.setBorder(javax.swing.BorderFactory.createLineBorder(
				new java.awt.Color(0, 0, 0), 0));
		rightSplit.setAlignmentX(Component.RIGHT_ALIGNMENT);

		// paletteControlsPanel = new JPanel();
		// paletteControlsPanel.setLayout(new BoxLayout(paletteControlsPanel,
		// BoxLayout.X_AXIS));
		// paletteControlsPanel.add(paletteList);

		rightPanel = new JPanel();

		topPanel = new JPanel();
		topPanel.add(validarButton);
		topPanel.add(validarProyButton);
		topPanel.add(entidadButton);
		topPanel.add(entidadGButton);
		topPanel.add(relacionButton);
		topPanel.add(searchPanel);
		topPanel.add(zoomPanel);
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.LINE_AXIS));
		treePanelControl = new PanelArbolJerarquiaDiagramas();
		treePanelControl.setLayout(new BoxLayout(treePanelControl, BoxLayout.LINE_AXIS));
		rightPanel.add(treePanelControl);
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
		// rightPanel.add(paletteControlsPanel);
		rightPanel.add(rightSplit);
		paletteList.setAlignmentX(Component.CENTER_ALIGNMENT);
		rightSplit.setAlignmentX(Component.CENTER_ALIGNMENT);

		/**
		 * CODIGO PARA LA JERARQUIA DE ARBOLES
		 * 
		 * si no queremos ver el arbol, simplemente comentamos la siguiente
		 * linea.
		 * 
		 **/

		mainSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, rightPanel,
				diagramspanel);
		mainSplit.setDividerSize(2);
		mainSplit.setDividerLocation(Constants.main_split_position);
		mainSplit.setResizeWeight(1);
		mainSplit.setBorder(javax.swing.BorderFactory.createLineBorder(
				new java.awt.Color(0, 0, 0), 0));

		customSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, mainSplit,
				customPanel);
		customSplit.setDividerSize(0);
		customSplit.setResizeWeight(1);
		customSplit.setBorder(javax.swing.BorderFactory.createLineBorder(
				new java.awt.Color(0, 0, 0), 0));
		customPanel.setVisible(false);

		mailSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, topPanel,
				customSplit);
		mailSplit.setDividerSize(0);
		mailSplit.setDividerLocation(45);
		mailSplit.setResizeWeight(1);
		mailSplit.setBorder(javax.swing.BorderFactory.createLineBorder(
				new java.awt.Color(0, 0, 0), 0));

		// Adding the DividerListener which refreshes Scrollbars here is enough
		// for all dividers
		palettepanel.addComponentListener(new DividerListener());

		palettepanel.setVisible(false);
		/******************* ADD PALETTES ***************************/
		Hashtable<String, PaletteHandler> palettetable = main.getPalettes();
		for (String palname : palettetable.keySet()) {
			DrawPanel panel = palettetable.get(palname).getDrawPanel();
			palettepanel.add(panel.getScrollPane(), palname);
		}

		/***************** SETTING TABS AND TAB LAYOUT ***********************/

		diagramtabs = new JTabbedPane();
		diagramtabs.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		diagramspanel.setLayout(new GridLayout(1, 1));
		diagramspanel.add(diagramtabs);

		/********************* ADD KEYBOARD ACTIONS ************************/
		Action findaction = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Main.getInstance().getGUI().enableSearch(true);
			}
		};
		this.getActionMap().put("focussearch", findaction);
		this.getInputMap().put(KeyStroke.getKeyStroke('/'), "focussearch");

		/************************ ADD TOP COMPONENT ************************/
		this.add(mailSplit);
		/************************** SET DEFAULT INITIALIZATION VALUES ******/
		if (!palettenames.isEmpty())
			this.selectPalette(palettenames.get(0));

		ToolTipManager.sharedInstance().setInitialDelay(100);
		/*************************************************************/

		this.window.setVisible(true);
	}

	@Override
	public void selectPalette(String palette) {
		if (this.selected_palette.equals(palette))
			return;

		this.selected_palette = palette;

		paletteList.setSelectedItem(this.selected_palette);

		CardLayout cl = (CardLayout) (this.palettepanel.getLayout());
		cl.show(this.palettepanel, this.getSelectedPalette());
	}

	@Override
	public String getSelectedPalette() {
		return this.selected_palette + "." + Program.EXTENSION;
	}

	@Override
	public void close(DiagramHandler diagram) {
		diagramtabs.remove(diagram.getDrawPanel().getScrollPane());
		this.onDiagramClosed();
	}

	@Override
	public void open(DiagramHandler diagram) {
		diagramtabs.add(diagram.getName(), diagram.getDrawPanel()
				.getScrollPane());
		diagramtabs.setTabComponentAt(diagramtabs.getTabCount() - 1,
				new TabComponent(diagramtabs, diagram));
		diagramtabs
				.setSelectedComponent(diagram.getDrawPanel().getScrollPane());
		diagram.getDrawPanel().getSelector().updateSelectorInformation();
		this.onDiagramOpened();
	}

	@Override
	public DrawPanel getCurrentDiagram() {
		JScrollPane scr = (JScrollPane) this.diagramtabs.getSelectedComponent();
		if (scr != null)
			return (DrawPanel) scr.getViewport().getView();
		else
			return null;
	}

	@Override
	public void elementsSelected(int count) {
		super.elementsSelected(count);
		if (count > 0) {
			editDelete.setEnabled(true);
			editCut.setEnabled(true);
			if (count > 1)
				editGroup.setEnabled(true);
			else
				editGroup.setEnabled(false);
		} else {
			editDelete.setEnabled(false);
			editGroup.setEnabled(false);
			editCut.setEnabled(false);
			// menu_edit_copy must remain enabled even if no entity is selected
			// to allow the export of the full diagram to the system clipboard.
		}
	}

	@Override
	public void setPaste(boolean value) {
		editPaste.setEnabled(value);
	}

	@Override
	public void setUngroupEnabled(boolean enabled) {
		editUngroup.setEnabled(enabled);
	}

	@Override
	public void setCustomPanelEnabled(boolean enable) {
		log.info("Set CustomPanel enabled=" + Boolean.toString(enable));
		this.custom_panel_visible = enable;
		if (this.customPanel.isVisible() != enable) {
			int loc = this.mainSplit.getDividerLocation();
			log.info("mainSplit Divider Location: " + loc);

			this.customPanel.setVisible(enable);

			if (enable) {
				int rightloc = this.rightSplit.getDividerLocation();
				log.info("rightSplit Divider Location: " + rightloc);
				this.customSplit.setDividerSize(2);
				this.rightSplit.setDividerSize(0);

				this.customPanel.getLeftSplit().setLeftComponent(
						this.propertyTextPane.getPanel());
				log.info("customPanel.leftSplit Divider Location: "
						+ customPanel.getLeftSplit().getDividerLocation());

				this.customSplit.setDividerLocation(rightloc);
				this.customPanel.getRightSplit().setDividerLocation(loc);
				this.customPanel.getLeftSplit().setDividerLocation(
						this.diagramspanel.getWidth() / 2);
				log.info("customPanel.leftSplit New Divider Location: "
						+ customPanel.getLeftSplit().getDividerLocation());
				this.customPanel.getLeftSplit().updateUI();
			} else {
				int rightloc = this.customSplit.getDividerLocation();
				log.info("customSplit Divider Location: " + rightloc);
				this.customSplit.setDividerSize(0);

				this.rightSplit.setDividerSize(2);
				this.rightSplit.setRightComponent(this.propertyTextPane
						.getPanel());
				this.rightSplit.setDividerLocation(rightloc);
			}
			this.mainSplit.setDividerLocation(loc);
			log.info("mainSplit Divider Location: " + loc);
			this.customEditar.setEnabled(!enable
					&& this.custom_element_selected);
			this.customEntidad.setEnabled(!enable);
			this.customEntidadConAtributos.setEnabled(!enable);
			this.customRelacion.setEnabled(!enable);
		}
		this.setDiagramsEnabled(!enable);
	}

	// @Override
	// public boolean isMailPanelVisible() {
	// return mail_panel_visible;
	// }

	@Override
	public void setCustomElementSelected(boolean selected) {
		this.custom_element_selected = selected;
		if (customEditar != null)
			customEditar.setEnabled(selected && !this.custom_panel_visible);
	}

	@Override
	public void diagramSelected(DiagramHandler handler) {
		updateGrayedOutMenuItems(handler);
	}

	public void updateGrayedOutMenuItems(DiagramHandler handler) {
		
		// These menuitems only get changed if this is not the palette or custompreview
		if (!(handler instanceof PaletteHandler) && !(handler instanceof CustomPreviewHandler)) {
			menuFactory.updateDiagramDependendComponents();
		if ( AppContext.getInstance().getProjectEntity().getName() == null )
				desactivarBotones();
		}

		// The menu_edit menuitems always work with the actual selected diagram (diagram, palette or custompreview), therefore we change it everytime
		if ((handler == null) || handler.getDrawPanel().getAllEntities().isEmpty()) {
			editCopy.setEnabled(false);
			editSelectAll.setEnabled(false);
		}
		else if (handler instanceof CustomPreviewHandler) {
			setCustomElementEditMenuEnabled(false);
		}
		else {
			editMenu.setEnabled(true); // must be set to enabled explicitely because it could be deactivated from CustomPreview
			setCustomElementEditMenuEnabled(true);
		}

		if ((handler == null) || !handler.getController().isUndoable()) editUndo.setEnabled(false);
		else editUndo.setEnabled(true);
		if ((handler == null) || !handler.getController().isRedoable()) editRedo.setEnabled(false);
		else editRedo.setEnabled(true);
	}

	@Override
	public void enableSearch(boolean enable) {
		this.searchField.requestFocus();
	}

	private void setCustomElementEditMenuEnabled(boolean enabled) {
		editGroup.setEnabled(enabled);
		editUngroup.setEnabled(enabled);
		editDelete.setEnabled(enabled);
		editCut.setEnabled(enabled);
		editPaste.setEnabled(enabled);
		editCopy.setEnabled(enabled);
		editSelectAll.setEnabled(enabled);
	}

	private void setDiagramsEnabled(boolean enable) {
		this.palettepanel.setEnabled(enable);
		for (Component c : this.palettepanel.getComponents())
			c.setEnabled(enable);
		this.diagramtabs.setEnabled(enable);
		for (Component c : this.diagramtabs.getComponents())
			c.setEnabled(enable);
		for (int i = 0; i < this.diagramtabs.getTabCount(); i++)
			this.diagramtabs.getTabComponentAt(i).setEnabled(enable);
		this.searchField.setEnabled(enable);
	}

	@Override
	public int getMainSplitPosition() {
		return this.mainSplit.getDividerLocation();
	}

	@Override
	public int getRightSplitPosition() {
		return this.rightSplit.getDividerLocation();
	}

	@Override
	public JFrame getTopContainer() {
		return this.window;
	}

	@Override
	public CustomElementHandler getCurrentCustomHandler() {
		return this.customelementhandler;
	}

	@Override
	public OwnSyntaxPane getPropertyPane() {
		return this.propertyTextPane;
	}

	@Override
	public String getPropertyPanelText() {
		if (this.propertyTextPane != null)
			return this.propertyTextPane.getText();
		else
			return "";
	}

	@Override
	public void setPropertyPanelText(String text) {
		if (this.propertyTextPane != null) { // needed because of convert
												// function
			this.propertyTextPane.setText(text);

			// Reset the vertical and horizontal scrollbar position to the upper
			// left corner
			this.propertyTextPane.setCaretPosition(0);
		}
	}

	@Override
	public String chooseFileName() {
		String fileName = null;
		int returnVal = DiagramFileHandler.getOpenFileChooser().showOpenDialog(
				this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			fileName = DiagramFileHandler.getOpenFileChooser()
					.getSelectedFile().getAbsolutePath();
		}
		return fileName;
	}

	@Override
	public List<String> chooseFolderName() {
		String fileName = null;
		List<String> list=new ArrayList<String>();
		JFileChooser openFileChooser = DiagramFileHandler
				.getOpenFolderChooser();
		openFileChooser.setDialogTitle(Constants.TITLE_CHOOSE);
		String name = (String) JOptionPane.showInputDialog(null,
				Constants.MESSAGE_PROJECT, Constants.NAME_PROJECT,
				JOptionPane.PLAIN_MESSAGE, null, null, "");
		int returnVal = openFileChooser.showOpenDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			fileName = DiagramFileHandler.getOpenFolderChooser()
					.getSelectedFile().getPath();
			 list.add(fileName);
			    list.add(name);
		}
	   
		return list;
	}

	@Override
	public void openDialog(String title, JComponent component) {
		JDialog dialog = (new JOptionPane(component, JOptionPane.PLAIN_MESSAGE))
				.createDialog(title);
		dialog.setVisible(true);
	}

	@Override
	public void setValueOfZoomDisplay(int i) {
		// This method should just set the value without ActionEvent therefore
		// we remove the listener temporarily
		if (zoomComboBox != null) {
			zoomComboBox.removeActionListener(zoomListener);
			zoomComboBox.setSelectedIndex(i - 1);
			zoomComboBox.addActionListener(zoomListener);
		}
	}

	@Override
	public void actualizarArbol() {
		((PanelArbolJerarquiaDiagramas)treePanelControl).crearArbol();
		treePanelControl.validate();
		rightPanel.validate();
	}
	
	public void activarBotones() {
		entidadButton.setEnabled(true);
		entidadGButton.setEnabled(true);
		relacionButton.setEnabled(true);
		validarButton.setEnabled(true);
		validarProyButton.setEnabled(true);
	}	
	
	public void desactivarBotones() {
		entidadButton.setEnabled(false);
		entidadGButton.setEnabled(false);
		relacionButton.setEnabled(false);
		validarButton.setEnabled(false);
		validarProyButton.setEnabled(false);
	}
}
