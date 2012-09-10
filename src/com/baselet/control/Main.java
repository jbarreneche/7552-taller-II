package com.baselet.control;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Timer;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import javax.swing.filechooser.FileSystemView;

import ar.uba.fi.taller2.tp.control.context.AppContext;
import ar.uba.fi.taller2.tp.control.context.ProjectContext;
import ar.uba.fi.taller2.tp.control.context.ProjectEntity;
import ar.uba.fi.taller2.tp.xml.XmlGenerator;

import com.baselet.control.Constants.Program;
import com.baselet.control.Constants.ProgramName;
import com.baselet.control.Constants.RuntimeType;
import com.baselet.diagram.DiagramHandler;
import com.baselet.diagram.DrawPanel;
import com.baselet.diagram.PaletteHandler;
import com.baselet.diagram.io.DiagramFileHandler;
import com.baselet.diagram.io.Logger;
import com.baselet.element.GridElement;
import com.baselet.gui.BaseGUI;
import com.baselet.gui.MenuFactorySwing;
import com.baselet.gui.standalone.StandaloneGUI;

public class Main {

	private final static Logger log = Logger.getLogger(Utils.getClassName());

	private static Main instance;

	private static String tmp_file;
	private static String tmp_read_file;
	private static boolean file_created = false;
	private static Timer timer;

	private BaseGUI gui;
	private GridElement editedGridElement;
	private Hashtable<String, PaletteHandler> palettes;
	private ArrayList<DiagramHandler> diagrams = new ArrayList<DiagramHandler>();
	private DiagramHandler currentDiagramHandler;
	private DiagramHandler currentInfoDiagramHandler;
	private ClassLoader classLoader;

	private List<String> recentFiles = new ArrayList<String>();

	public static Main getInstance() {
		if (instance == null) {
			instance = new Main();
		}
		return instance;
	}

	public static void main(String args[]) {

		
		
//		System.setSecurityManager(new CustomElementSecurityManager());

		Main main = Main.getInstance();
		main.initOverallSettings();
		tmp_file = Program.PROGRAM_NAME.toLowerCase() + ".tmp";
		tmp_read_file = Program.PROGRAM_NAME.toLowerCase() + "_1.tmp";

		try {
			if (args.length != 0) {
				String action = null;
				String format = null;
				String filename = null;
				String output = null;
				for (int i = 0; i < args.length; i++) {
					if (args[i].startsWith("-action=")) action = args[i].substring(8);
					else if (args[i].startsWith("-format=")) format = args[i].substring(8);
					else if (args[i].startsWith("-filename=")) filename = args[i].substring(10);
					else if (args[i].startsWith("-output=")) output = args[i].substring(8);
				}
				// Program started by double-click on diagram file
				if ((action == null) && (format == null) && (filename != null)) {
					if (!alreadyRunningChecker(false) || !sendFileNameToRunningApplication(filename)) {
						main.init(new StandaloneGUI(main));
						main.doOpen(filename);
					}
				}
				else if ((action != null) && (format != null) && (filename != null)) {
					if (action.equals("convert")) {
						Program.RUNTIME_TYPE = RuntimeType.BATCH;
						doConvert(filename, format, output);
					}
					else printUsage();
				}
				else printUsage();
			}
			else { // no arguments specified
				alreadyRunningChecker(true); // start checker
				main.init(new StandaloneGUI(main));
				main.doNew();
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Initialization or uncaught outer Exception", e);
		}
	}

	public void init(BaseGUI gui) throws Exception {
		this.gui = gui;
		Config.loadConfig(); // only load config after gui is set (because of homepath)
		ToolTipManager.sharedInstance().setDismissDelay(Integer.MAX_VALUE); // Tooltips should not hide after some time
		gui.initGUI(); // show gui
	}

	public void initOverallSettings() {
		readManifestInfo();
		initLogger();
	}

	private void initLogger() {
		String log4jFilePath = Path.homeProgram() + "log4j.properties";
		try {
			// If no log4j.properties file exists, we create a simple one
			if (!new File(log4jFilePath).exists()) {
				log4jFilePath = Path.temp() + Program.PROGRAM_NAME.toLowerCase() + "_log4j.properties";
				File tempLog4jFile = new File(log4jFilePath);
				tempLog4jFile.deleteOnExit();
				Writer writer = new BufferedWriter(new FileWriter(tempLog4jFile));
				writer.write(
						"log4j.rootLogger=ERROR, SYSTEM_OUT\n" +
						"log4j.appender.SYSTEM_OUT=com.baselet.diagram.io.ConsoleAppender\n" + 
						"log4j.appender.SYSTEM_OUT.layout=com.baselet.diagram.io.PatternLayout\n" + 
				"log4j.appender.SYSTEM_OUT.layout.ConversionPattern=%6r | %-5p | %-30c - \"%m\"%n\n");
				writer.close();
			}
			Properties props = new Properties();
			props.put("PROJECT_PATH", Path.homeProgram()); // Put homepath as relative variable in properties file
			props.load(new FileInputStream(log4jFilePath));
			log.info("Logger configuration initialized");
		} catch (IOException e) {
			log.error("Initialization of log4j.properties failed", e);
		}
	}

	private void readManifestInfo() {
		try {
			Manifest manifest;
			if (Path.executable().endsWith(".jar")) manifest = new JarFile(Path.executable()).getManifest();
			else manifest = new Manifest(new FileInputStream(Path.homeProgram() + "META-INF" + File.separator + "MANIFEST.MF"));

			Attributes attributes = manifest.getMainAttributes();
			String versionString = attributes.getValue("Bundle-Version");
			String progNameString = attributes.getValue("Bundle-Name");
			ProgramName programName = progNameString.equals("Umlet") ? ProgramName.UMLET : ProgramName.PLOTLET;
			Program.init(programName, versionString);

		} catch (Exception e) {
//			log.error(null, e);
			e.printStackTrace(); // Logger is not initialized here
		}
	}

	public static void displayError(String error) {
		JOptionPane.showMessageDialog(null, error, "ERROR", JOptionPane.ERROR_MESSAGE);
	}

	private static void printToConsole(String text) {
		System.out.println(text);
	}

	private static void printUsage() {
		String formats = "pdf|svg|eps";
		for (String format : ImageIO.getWriterFileSuffixes())
			formats += "|" + format;
		printToConsole("USAGE: -action=convert -format=(" + formats + ") -filename=inputfile." + Program.EXTENSION + " [-output=outputfile[.extension]]");
	}

	public static void doConvert(String fileName, String format, String outputfilename) {
		File file = new File(fileName);
		if (!file.exists()) {
			printToConsole("File '" + file.getAbsolutePath() + "' not found.");
			return;
		}
		DiagramHandler handler = new DiagramHandler(file);
		if (outputfilename == null) {
			if (fileName.contains("." + Program.EXTENSION)) fileName = fileName.substring(0, fileName.indexOf("." + Program.EXTENSION));
			outputfilename = fileName + "." + format;
		}
		else if (!outputfilename.endsWith("." + format)) outputfilename += "." + format;

		try {
			if (format != null) handler.getFileHandler().doExportAs(format, new File(outputfilename));
		} catch (Exception e) {
			printUsage();
		}
	}

	private static boolean alreadyRunningChecker(boolean force) {
		try {
			File f = new File(Path.temp() + tmp_file);
			if (f.exists() && !force) return true;
			f.createNewFile();
			file_created = true;
			timer = new Timer(true);
			timer.schedule(new RunningFileChecker(Path.temp() + tmp_file, Path.temp() + tmp_read_file), 0, 1000);
		} catch (Exception ex) {
			ex.printStackTrace();
			return true;
		}
		return false;
	}

	private static boolean sendFileNameToRunningApplication(String filename) {
		// send the filename per file to the running application
		File f1 = new File(Path.temp() + tmp_file);
		boolean write_successful = true;
		try {
			PrintWriter writer = new PrintWriter(f1);
			writer.println(filename);
			writer.close();
		} catch (Exception ex) {
			write_successful = false;
		}
		try {
			Thread.sleep(2000);
		} catch (Exception ex) {}
		File f2 = new File(Path.temp() + tmp_read_file);
		if (!f2.exists() || !write_successful) // if the ok file does not exist or the filename couldnt be written.
		{
			alreadyRunningChecker(true);
			return false;
		}
		else f2.delete();
		return true;
	}

	// sets the current diagram the user works with - that may be a palette too
	public void setCurrentDiagramHandler(DiagramHandler handler) {
		this.setCurrentInfoDiagramHandler(handler);
		this.currentDiagramHandler = handler;
		if (gui != null) gui.diagramSelected(handler);
	}

	public DiagramHandler getCurrentInfoDiagramHandler() {
		return this.currentInfoDiagramHandler;
	}

	public void setCurrentInfoDiagramHandler(DiagramHandler handler) {
		log.debug("trying to setCurrentInfoDiagram");
		log.debug("this.currentdiagram::");
		if(this.currentInfoDiagramHandler!=null)
		log.debug(this.currentInfoDiagramHandler.getName());
		log.debug("handler::");
		if(handler!=null)
		log.debug(handler.getName());
		if ((!(handler instanceof PaletteHandler)) && (handler != null)) {
			log.debug("SETTING currentInfoDiagram");
			this.currentInfoDiagramHandler = handler;
		}
	}

	public void setPropertyPanelToCustomElement(GridElement e) {
		editedGridElement = e;
	}

	public void setPropertyPanelToGridElement(final GridElement e) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				setPropertyPanelToGridElementHelper(e);
			}
		});
	}

	private void setPropertyPanelToGridElementHelper(GridElement e) {
		editedGridElement = e;
		if (e != null) this.gui.setPropertyPanelText(e.getPanelAttributes());
		else {			
			DiagramHandler handler = this.getDiagramHandler();
			if (handler == null) this.gui.setPropertyPanelText("");
			else this.gui.setPropertyPanelText(handler.getHelpText());
		}
	}

	public GridElement getGridElementFromPath(String path) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		if (classLoader == null) classLoader = Thread.currentThread().getContextClassLoader(); // use classloader of current thread (not systemclassloader - important for eclipse)
		Class<?> foundClass = null;
		String[] possiblePackages = new String[] {"com.umlet.element", "com.umlet.element.custom", "com.plotlet.element", "com.baselet.element"};
		try {
			foundClass = classLoader.loadClass(path);
		}
		catch (ClassNotFoundException e) {
			String className = path.substring(path.lastIndexOf("."));
			for (String possPackage : possiblePackages) {
				try {
					foundClass = classLoader.loadClass(possPackage + className);
					break;
				} catch (ClassNotFoundException e1) {/*do nothing; try next package*/}
			}
		}
		if (foundClass == null) {
			ClassNotFoundException ex = new ClassNotFoundException("class " + path + " not found");
			log.error(null, ex);
			throw ex;
		}
		else return (GridElement) foundClass.newInstance();
	}

	
	//TODO   IMPORTANTE CON ESTE METODO SE ABREN LOS DIAGRAMA DEL PROJECTO
	public void doNew() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				doNewHelper();
			}
		});
	}
	
	private String parsePath(String path){
			String[] subDirs = path.split(Pattern.quote(File.separator));
			String nameFile=File.separator+subDirs[subDirs.length-1];
			return path.replaceAll(nameFile,"");
	}
	/**
	 * abro el projecto 
	 */
	public void doOpenProject() {
		
		String path = DiagramFileHandler.chooseFileName();
		if(path==null){
			return;
		}
		this.closeDiagrams();
		XmlGenerator generator=new XmlGenerator();
		ProjectEntity projectEntity=null;
		ProjectContext context=null;
		try {
			projectEntity=generator.loadProjectXML(path);
			projectEntity.setParentDir(parsePath(path));
			context=generator.loadDiagramXML(projectEntity.getParentDir()+File.separator+projectEntity.getNameComponent());
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, Constants.TITLE_ERROR_OPEN,Constants.MESSAGE_ERROR_OPEN,
				    JOptionPane.ERROR_MESSAGE);
		}
		AppContext.getInstance().setProjectContext(context);
		AppContext.getInstance().setProjectEntity(projectEntity);
		if(!projectEntity.getListDiagrams().isEmpty()){
		   this.doOpen(projectEntity.getListDiagrams().get(0));
		}
        AppContext.getInstance().incrementCount();
        AppContext.getInstance().incrementCount();
		this.gui.activarBotones();
		this.gui.actualizarArbol();
		
	}

	/**
	 * creo el nuevo projecto 
	 * @throws UnsupportedEncodingException 
	 * @throws FileNotFoundException 
	 */
	public void doNewProject(){
		ProjectContext context= new ProjectContext();
		ProjectEntity projectEntity= new ProjectEntity();
		AppContext.getInstance().setProjectContext(context);
		AppContext.getInstance().setProjectEntity(projectEntity);
		List<String> folder = DiagramFileHandler.chooseFolderName();
		if(folder.isEmpty()){
			return;
		}
		this.closeDiagrams();
		String nameProject=folder.get(1);
		File file= new File(folder.get(0)+File.separator+nameProject+File.separator+Constants.DATOS);
		projectEntity.setName(nameProject);
		projectEntity.setParentDir(file.getPath());
		projectEntity.setNameComponent(nameProject+Program.COMP+"."+Program.EXTENSION);
		if(file.mkdirs()){
			XmlGenerator generator= new XmlGenerator();
			try {
				generator.saveDiagramXml(projectEntity,file.getPath()+File.separator+nameProject+"."+Program.EXTENSION);
				generator.saveDiagramXml(context,file.getPath()+File.separator+projectEntity.getNameComponent());
			} catch (IOException e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, Constants.TITLE_ERROR_CREATE,Constants.MESSAGE_ERROR_CREATE,
					    JOptionPane.ERROR_MESSAGE);
			}
		}
        AppContext.getInstance().incrementCount();
		this.doNew();
		this.gui.activarBotones();
		this.gui.actualizarArbol();
	}

	
	private void doNewHelper() {
		if (lastTabIsEmpty()) return; // If the last tab is empty do nothing (it's already new)
		DiagramHandler diagram = new DiagramHandler(null);
		this.diagrams.add(diagram);
		this.gui.open(diagram);
		if (this.diagrams.size() == 1) this.setPropertyPanelToGridElement(null);
	}

	public void doOpen() {
		String fn = DiagramFileHandler.chooseFileName();
		if (fn != null) this.doOpen(fn);
	}

	public void doOpen(final String filename) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				doOpenHelper(filename);
			}
		});
	}

	private void closeDiagrams(){
		boolean exits=this.diagrams.isEmpty();
		while (!exits) {
            DiagramHandler diagrams = this.diagrams.get(0);
			diagrams.doClose();
			exits=this.diagrams.isEmpty();;
		}
		this.doNewHelper();
			
	}
	
	private void doOpenHelper(String filename) {
		if (lastTabIsEmpty()) (diagrams.get(diagrams.size() - 1)).doClose(); // If the last tab is empty close it (the opened diagram replaces the new one)
		File file = new File(filename);
		if (!file.exists()) return;
		DiagramHandler diagram = new DiagramHandler(file);
		this.diagrams.add(diagram);
		this.gui.open(diagram);
		if (this.diagrams.size() == 1) this.setPropertyPanelToGridElement(null);
		if (recentFiles.contains(filename)) recentFiles.remove(filename);
		recentFiles.add(0, filename);
		int maxRecentFiles = 10;
		if (recentFiles.size() > maxRecentFiles) recentFiles.remove(maxRecentFiles);
		MenuFactorySwing.getInstance().updateDiagramEntities();	
	}

	/**
	 * If the last diagram tab and it's undo history (=controller) is empty return true, else return false
	 */
	private boolean lastTabIsEmpty() {
		if (!this.diagrams.isEmpty()) {
			DiagramHandler lastDiagram = this.diagrams.get(diagrams.size() - 1);
			if (lastDiagram.getController().isEmpty() && lastDiagram.getDrawPanel().getAllEntities().isEmpty()) { return true; }
		}
		return false;
	}

	// ask for save for all diagrams (if main is closed)
	public boolean askSaveIfDirty() {
		boolean ok = true;
		for (DiagramHandler d : this.getDiagrams()) {
			if (!d.askSaveIfDirty()) ok = false;
		}

		if (!this.getGUI().getCurrentCustomHandler().closeEntity()) ok = false;
		return ok;
	}

	// called by UI when main is closed
	public void closeProgram() {
		Config.saveConfig();
		if (file_created) {
			timer.cancel();
			(new File(Path.temp() + tmp_file)).delete();
		}
	}

	public Hashtable<String, PaletteHandler> getPalettes() {
		if (this.palettes == null) {
			this.palettes = new Hashtable<String, PaletteHandler>();
			// scan palettes
			List<File> palettes = this.scanForPalettes();
			for (File palette : palettes)
				this.palettes.put(palette.getName(), new PaletteHandler(palette));
		}
		return this.palettes;
	}

	public List<File> scanForPalettes() {
		// scan palettes directory...
		FileSystemView fileSystemView = FileSystemView.getFileSystemView();
		File[] paletteFiles = fileSystemView.getFiles(new File(Path.homeProgram() + "palettes/"), false);
		List<File> palettes = new ArrayList<File>();
		for (File palette : paletteFiles) {
			if (palette.getName().endsWith("." + Program.EXTENSION)) palettes.add(palette);
		}
		return palettes;
	}

	public List<String> getPaletteNames() {
		return this.getPaletteNames(this.getPalettes());
	}

	public List<String> getPaletteNames(Hashtable<String, PaletteHandler> palettes) {
		List<String> palettenames = new ArrayList<String>();
		for (String palette : palettes.keySet())
			palettenames.add(palette.substring(0, palette.length() - 4));
		Collections.sort(palettenames, new PaletteSorter());
		return palettenames;
	}

	public List<String> getTemplateNames() {
		ArrayList<String> templates = new ArrayList<String>();
		// scan palettes directory...
		FileSystemView fileSystemView = FileSystemView.getFileSystemView();
		File[] templateFiles = fileSystemView.getFiles(new File(Path.customElements()), false);
		for (File template : templateFiles) {
			if (template.getName().endsWith(".java")) templates.add(template.getName().substring(0, template.getName().length() - 5));
		}
		Collections.sort(templates, new TemplateSorter());
		return templates;
	}

	public List<DiagramHandler> getDiagrams() {
		return this.diagrams;
	}
	public DiagramHandler getDiagramHandler(String file) {
		for (Iterator<DiagramHandler> iterator =  this.diagrams.iterator(); iterator.hasNext();) {
			DiagramHandler type = (DiagramHandler) iterator.next();
			if(type.getFileHandler().getFileName().equals(file)){
				return type;
			}
		}
		return null;
	}
	public BaseGUI getGUI() {
		return this.gui;
	}

	public GridElement getEditedGridElement() {
		return editedGridElement;
	}

	public String getPropertyString() {
		return this.gui.getPropertyPanelText();
	}

	public PaletteHandler getPalette() {
		String name = this.gui.getSelectedPalette();
		if (name != null) return this.getPalettes().get(name);
		return null;
	}

	public DrawPanel getPalettePanel() {
		return this.getPalette().getDrawPanel();
	}

	// returns the current diagramhandler the user works with - may be a diagramhandler of a palette too
	public DiagramHandler getDiagramHandler() {
		return this.currentDiagramHandler;
	}

	public List<String> getRecentFiles() {
		return recentFiles;
	}

	

}
