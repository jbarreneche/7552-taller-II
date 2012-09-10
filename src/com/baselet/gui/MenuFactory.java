package com.baselet.gui;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import ar.uba.fi.taller2.tp.control.context.AppContext;
import ar.uba.fi.taller2.tp.model.Attribute;
import ar.uba.fi.taller2.tp.model.Component;
import ar.uba.fi.taller2.tp.model.DiagramTree;
import ar.uba.fi.taller2.tp.model.Entity;
import ar.uba.fi.taller2.tp.model.EntityRelation;
import ar.uba.fi.taller2.tp.model.Identifiers;
import ar.uba.fi.taller2.tp.model.TypeAttribute;

import com.baselet.control.Constants.Program;
import com.baselet.control.Main;
import com.baselet.diagram.DiagramHandler;
import com.baselet.diagram.DrawPanel;
import com.baselet.diagram.PaletteHandler;
import com.baselet.diagram.Selector;
import com.baselet.diagram.command.AddElement;
import com.baselet.diagram.command.Align;
import com.baselet.diagram.command.ChangeColor;
import com.baselet.diagram.command.Copy;
import com.baselet.diagram.command.CreateGroup;
import com.baselet.diagram.command.Cut;
import com.baselet.diagram.command.Move;
import com.baselet.diagram.command.Paste;
import com.baselet.diagram.command.RemoveElement;
import com.baselet.diagram.command.UnGroup;
import com.baselet.element.GridElement;
import com.baselet.element.Group;
import com.baselet.gui.actionlistener.AttributeFrame;
import com.baselet.gui.actionlistener.EntityRelationFrame;
import com.baselet.gui.actionlistener.IdFrame;
import com.baselet.gui.actionlistener.cambiarTipoFrame;
import com.baselet.gui.standalone.StandaloneGUI;
import com.umlet.element.AttributePK;
import com.umlet.element.Relation;
import com.umlet.element.RelationEntity;
import com.umlet.element.Specialization;
import com.umlet.element.UseCase;
import com.umlet.element.custom.EER_Entity;
import com.umlet.element.custom.EER_Rel_Diamond;

public class MenuFactory {
	

	//FILE
	public static final String FILE = "Archivos";
	protected static final String NEW = "Nuevo Diagrama";
	protected static final String NEW_PRO= "Nuevo Projecto";
	protected static final String OPEN_PRO= "Abrir Projecto";
	protected static final String OPEN = "Abrir...";
	protected static final String RECENT_FILES = "Archivos recientes";
	protected static final String SAVE = "Guardar";
	protected static final String SAVE_AS = "Guardar como...";
	protected static final String EXPORT_AS = "Exportar como...";
//	protected static final String MAIL_TO = "Mail to...";
	protected static final String EDIT_CURRENT_PALETTE = "Editar diagrama Actual";
	protected static final String OPTIONS = "Opciones...";
	protected static final String PRINT = "Imprimir...";
	protected static final String EXIT = "Cerrar";

	//EDIT
	public static final String EDIT = "Editar";
	protected static final String UNDO = "Deshacer";
	protected static final String REDO = "Rehacer";
	protected static final String DELETE = "Borrar";
	protected static final String SELECT_ALL = "Selecionar Todo";
	protected static final String GROUP = "Agrupar";
	protected static final String UNGROUP = "Desagrupar";
	protected static final String CUT = "Cortar";
	protected static final String COPY = "Copiar";
	protected static final String PASTE = "Pegar";

	// HELP
	public static final String HELP = "About";
	protected static final String ABOUT_PROGRAM = "Tp Taller2 - 1er Cuatrimetre 2012";

	// CONTEXT ON ELEMENT
	protected static final String SET_FOREGROUND_COLOR = "Set foreground color";
	protected static final String SET_BACKGROUND_COLOR = "Set background color";
	protected static final String ALIGN = "Align";
	
	// ADD ON ELEMENT
	protected static final String ADD_ATTRIBUTE = "Agregar atributo";
	protected static final String ADD_COMPOSED_ATTRIBUTE = "Agregar atributo compuesto";
	protected static final String ADD_ID = "Agregar identificador simple";
	protected static final String ADD_COMPOSED_ID = "Agregar identificador compuesto";
	protected static final String ADD_ENTITY_TO_RELATION_UP = "Unir entidad a extremo superior";
	protected static final String ADD_ENTITY_TO_RELATION_DOWN = "Unir entidad a extremo inferior";
	
	// GENERALIZE
	protected static final String GENERALIZATION = "Generalizar...";
	protected static final String CAMBIAR_TIPO = "Cambiar tipo";
	protected static final String ADD_ENTITY_TO_GENERALIZATION = "Agregar entidad a generalización";
	
	//ATTRIBUTE EDITION
	protected static final String EDIT_ATTRIBUTE = "Editar atributo";
	//ATTRIBUTE EDITION
	protected static final String EDIT_RELATION_ENTITY = "Editar Relación-Entidad";

	// OTHERS
	protected static final String SEARCH = "Buscar";
	protected static final String ZOOM = "Zoom";

	protected void doAction(final String menuItem, final Object param) {
		//AB: Hopefully this will resolve threading issues and work for eclipse AND standalone
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {			
				Main main = Main.getInstance();
				BaseGUI gui = main.getGUI();
				DiagramHandler diagramHandler = gui.getCurrentDiagram().getHandler();
				DiagramHandler actualHandler = main.getDiagramHandler();
				PaletteHandler paletteHandler = main.getPalette();//getPalettes().get("EER.xml");
				Selector actualSelector = actualHandler == null ? null : actualHandler.getDrawPanel().getSelector();
				
				if (menuItem.equals(NEW_PRO)) {
					main.doNewProject();
				}
				else if (menuItem.equals(OPEN_PRO)) {
					main.doOpenProject();
				}
				else if (menuItem.equals(NEW)) {
					main.doNew();
				}
				else if (menuItem.equals(OPEN)) {
					main.doOpen();
				}
				else if (menuItem.equals(RECENT_FILES)) {
					main.doOpen((String) param);
				}
				else if (menuItem.equals(SAVE) && diagramHandler != null) {
					diagramHandler.doSave();
					AppContext.getInstance().incrementCount();
					Main.getInstance().getGUI().actualizarArbol();
				}
				else if (menuItem.equals(SAVE_AS) && diagramHandler != null) {
					diagramHandler.doSaveAs(Program.EXTENSION);
				}
				else if (menuItem.equals(EXPORT_AS) && diagramHandler != null) {
					diagramHandler.doSaveAs((String) param);
				}
				else if (menuItem.equals(EDIT_CURRENT_PALETTE)) {
					main.doOpen(main.getPalette().getFileHandler().getFullPathName());
				}
				else if (menuItem.equals(OPTIONS)) {
					OptionPanel.getInstance().showOptionPanel();
				}
				else if (menuItem.equals(PRINT) && diagramHandler != null) {
					diagramHandler.doPrint();
				}
				else if (menuItem.equals(EXIT)) {
					main.getGUI().closeWindow();
				}
				else if (menuItem.equals(UNDO) && (actualHandler != null) && (actualSelector != null)) {
					actualHandler.getController().undo();
					if (gui instanceof StandaloneGUI) ((StandaloneGUI) gui).updateGrayedOutMenuItems(actualHandler);
				}
				else if (menuItem.equals(REDO) && (actualHandler != null)) {
					actualHandler.getController().redo();
					if (gui instanceof StandaloneGUI) ((StandaloneGUI) gui).updateGrayedOutMenuItems(actualHandler);
				}
				else if (menuItem.equals(DELETE) && (actualHandler != null) && (actualSelector != null)) {
					GridElement v = actualSelector.getSelectedEntities().get(0);
					if(AppContext.getInstance().getProjectContext().getEntities().isEmpty()){
						actualHandler.getController().executeCommand(new RemoveElement(v));
						return;
					}
					
					if(v.getClass().getSimpleName().equals("Specialization")){
						Long id2=v.getId();
						Entity entity=AppContext.getInstance().getEntity(v.getIdPrimary());
						ar.uba.fi.taller2.tp.model.Specialization specialization=entity.getGeneralization(v.getId());
						entity.getGeneralizations().remove(specialization);
						actualHandler.getController().executeCommand(new RemoveElement(v));
						GridElement x = buscarUnGridElement( id2 , actualHandler );
						if(x!=null)
						actualHandler.getController().executeCommand(new RemoveElement(x));
					}else if(v.getClass().getSimpleName().equals("EER_Entity")){
						Entity entity= AppContext.getInstance().getEntity(v.getId());
						AppContext.getInstance().getProjectContext().getEntities().remove(entity);
						Vector<GridElement> vector=new Vector<GridElement>();
						vector.add(v);
						
						com.umlet.element.Identifiers idsActGraf = null;
						java.awt.Component[] comps = actualHandler.getDrawPanel().getComponents();
						for (int i = 0; i < comps.length; i++) {
							GridElement unGridElement = (GridElement) comps[i];
							Long primary = unGridElement.getIdPrimary();
							Long id_entidad = entity.getId();
							if( (primary != null) && (primary.equals(id_entidad)) ) {
								vector.add( unGridElement );
							}
						}
						AppContext.getInstance().getProjectEntity().removeOthersEntities(actualHandler.getFileHandler().getFullPathName(),v.getId());
						actualHandler.getController().executeCommand(new RemoveElement(vector));
					}else if(v.getClass().getSimpleName().equals("EER_Rel_Diamond")){
						ar.uba.fi.taller2.tp.model.Relation entity= AppContext.getInstance().getRelation(v.getId());
						AppContext.getInstance().getProjectContext().getRelations().remove(entity);
						Vector<GridElement> vector=new Vector<GridElement>();
						vector.add(v);
						
						com.umlet.element.Identifiers idsActGraf = null;
						java.awt.Component[] comps = actualHandler.getDrawPanel().getComponents();
						for (int i = 0; i < comps.length; i++) {
							GridElement unGridElement = (GridElement) comps[i];
							Long primary = unGridElement.getIdPrimary();
							Long id_entidad = entity.getId();
							if( (primary != null) && (primary.equals(id_entidad)) ) {
								vector.add( unGridElement );
							}
						}
						
						actualHandler.getController().executeCommand(new RemoveElement(vector));
					}
					else if(v.getClass().getSimpleName().equals("Attribute")) {
						eliminarAtributoSimple(v, actualHandler );
					}
					else if(v.getClass().getSimpleName().equals("AttributePK")) {
						Long idActual = v.getId();
						DiagramTree miDiagrama = null;			
						String nombreDiagrama = Main.getInstance().getDiagramHandler().getName();
						String[] partes = nombreDiagrama.split("_");
						nombreDiagrama = partes[0];
						List<DiagramTree> diagramas = AppContext.getInstance().getProjectEntity().getDiagramas();
						Iterator<DiagramTree> iterDiag = diagramas.iterator();
						while (iterDiag.hasNext()) {
							DiagramTree unDiag = iterDiag.next();
							if (nombreDiagrama.equals(unDiag.getNombre())) {
								miDiagrama = unDiag;
							}
						}
						List<ar.uba.fi.taller2.tp.model.Entity> entidades = miDiagrama.getEntities();
						Iterator<ar.uba.fi.taller2.tp.model.Entity> iterEnt = entidades.iterator();
						ar.uba.fi.taller2.tp.model.Attribute miAtributo = null;
						ar.uba.fi.taller2.tp.model.Entity miEntidad = null;
						boolean continua = true;
						while( iterEnt.hasNext() && continua ) {
							ar.uba.fi.taller2.tp.model.Entity entActual = iterEnt.next();
							List<ar.uba.fi.taller2.tp.model.Attribute> susAtr = entActual.getAttributes();
							Iterator<ar.uba.fi.taller2.tp.model.Attribute> iterAt = susAtr.iterator();
							while( iterAt.hasNext() && continua ) {
								ar.uba.fi.taller2.tp.model.Attribute actual = iterAt.next();
								long result = idActual - actual.getId();
								if( result == 0 ) {
									miAtributo = actual;
									miEntidad = entActual;
									continua = false;
								}
							}
						}
						// saco al atributo de la lista de atributos de la entidad 
						miEntidad.getAttributes().remove(miAtributo);
						
						// saco al id del atributo de la lista de identificadores simples
						Identifiers unIdentificador = new Identifiers();
						unIdentificador.addIds(idActual);
						Iterator<Identifiers> itIdent = miEntidad.getIdentifiers().iterator();
						Identifiers identActual = null;
						while( itIdent.hasNext() ) {
							Identifiers i = itIdent.next();
							if( i.isEqual(unIdentificador) ) {
								identActual = i;
							}
						}
						miEntidad.getIdentifiers().remove(identActual);
						
						// ahora borra al atributo 
						Vector<GridElement> vector=new Vector<GridElement>();
						vector.add(v);
						actualHandler.getController().executeCommand(new RemoveElement(vector));
					}
					else if(v.getClass().getSimpleName().equals("UseCase")) {
						eliminarAtributoCompuesto( v , actualHandler );
					}else if(v.getClass().getSimpleName().equals("RelationEntity")) {
						eliminarRelationEntity( v , actualHandler );
					}
				}
				else if (menuItem.equals(SELECT_ALL) && (actualHandler != null) && (actualSelector != null)) {
					actualSelector.selectAll();
				}
				else if (menuItem.equals(GROUP) && (actualHandler != null)) {
					Main.getInstance().getDiagramHandler().getController().executeCommand(new CreateGroup());
				}
				else if (menuItem.equals(UNGROUP) && (actualHandler != null) && (actualSelector != null)) {
					Vector<GridElement> gridElements = actualSelector.getSelectedEntities();
					for (GridElement gridElement : gridElements) {
						if (gridElement instanceof Group) actualHandler.getController().executeCommand(new UnGroup((Group) gridElement));
					}
				}
				else if (menuItem.equals(CUT) && (actualHandler != null)) {
					if (!actualHandler.getDrawPanel().getAllEntities().isEmpty()) actualHandler.getController().executeCommand(new Cut());
				}
				else if (menuItem.equals(COPY) && (actualHandler != null)) {
					if (!actualHandler.getDrawPanel().getAllEntities().isEmpty()) actualHandler.getController().executeCommand(new Copy());
				}
				else if (menuItem.equals(PASTE) && (actualHandler != null)) {
					actualHandler.getController().executeCommand(new Paste());
				}
				else if (menuItem.equals(ADD_ATTRIBUTE) || menuItem.equals(ADD_COMPOSED_ATTRIBUTE)) {
					if (actualSelector.getSelectedEntities().size() == 1){
				    	final AttributeFrame frame = new AttributeFrame(); 
					    frame.setVisible(true);
   
					    frame.setAceptarButtonActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								processComposedAttribute(frame);
								
							}
					    });	
						
					}
					
				}
				else if (menuItem.equals(EDIT_ATTRIBUTE)) {
					GridElement element = actualSelector.getSelectedEntities().get(0);
                    final Attribute component = (Attribute) AppContext.getInstance().getProjectContext().getComponent(element.getId());
					String nombre = component.getName();
					String card = component.getCardinality();
					if (actualSelector.getSelectedEntities().size() == 1){
				    	final AttributeFrame frame1 = new AttributeFrame(nombre, card);
					    frame1.setVisible(true);
					    frame1.setAceptarButtonActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								//processEditAttribute(frame1);
								component.setName(frame1.getNombre());
								component.setCardinality(frame1.getCardinalidad());
							}
					    });	
				}
				}
//				else if (menuItem.equals(ADD_COMPOSED_ATTRIBUTE)) {
//					Relation r = (Relation)paletteHandler.getDrawPanel().getAllEntities().get(8).CloneFromMe();
//					UseCase oval = (UseCase)paletteHandler.getDrawPanel().getAllEntities().get(7).CloneFromMe();
//					if (actualSelector.getSelectedEntities().size() == 1){
//						GridElement element = actualSelector.getSelectedEntities().get(0);
//						Long id=AppContext.getInstance().getProjectContext().getIdIncrement();
//						Component component = AppContext.getInstance().getProjectContext().getComponent(element.getId());
//						
//						final Attribute attributeModel=new Attribute(id, r.getPanelAttributes(), TypeAttribute.COMPOSED);
//						component.getAttributes().add(attributeModel);
//						r.setId(id);
//						r.setIdPrimary(element.getId());
//						oval.setId(id);
//						oval.setIdPrimary(component.getId());
//						oval.setAttribute(attributeModel);
//						// Agrego Id al componente
//						onAdd(r, diagramHandler, element, 1);
//						
//						String nombre = attributeModel.getName();
//						String card = attributeModel.getCardinality();
//						if (actualSelector.getSelectedEntities().size() == 1){
//					    	final AttributeFrame frame1 = new AttributeFrame(nombre, card);
//						    frame1.setVisible(true);
//						    frame1.setAceptarButtonActionListener(new ActionListener() {
//								public void actionPerformed(ActionEvent e) {
//									//processEditAttribute(frame1);
//									attributeModel.setName(frame1.getNombre());
//									attributeModel.setCardinality(frame1.getCardinalidad());
//								}
//						    });	
//						}
//						
//						Point p1 = new Point(r.getX()+ r.getWidth(), r.getY());
//						diagramHandler.getController().executeCommand(new AddElement(oval, 10, 10));
//						diagramHandler.getController().executeCommand(new Move(oval, p1.x+40, p1.y));
//					}
//					
//				}
				else if (menuItem.equals(ADD_ID)) {
					AttributePK r = (AttributePK)paletteHandler.getDrawPanel().getAllEntities().get(5).CloneFromMe();
					if (actualSelector.getSelectedEntities().size() == 1){
						GridElement element = actualSelector.getSelectedEntities().get(0);
						Long id=AppContext.getInstance().getProjectContext().getIdIncrement();
						Entity component = AppContext.getInstance().getProjectContext().getEntity(element.getId());
						Attribute attributeModel=new Attribute(id, r.getPanelAttributes(), TypeAttribute.SIMPLE);
						component.getAttributes().add(attributeModel);
						r.setId(id);
						r.setAttribute(attributeModel);
						r.setIdPrimary(element.getId());
						Identifiers iden= new Identifiers();
						iden.addIds(attributeModel.getId());
						component.getIdentifiers().add(iden);
						onAdd(r, diagramHandler, element, 1);
					}
				}
				else if (menuItem.equals(ADD_COMPOSED_ID)) {
					AttributePK r = (AttributePK)paletteHandler.getDrawPanel().getAllEntities().get(5).CloneFromMe();
					if (actualSelector.getSelectedEntities().size() == 1){
						final IdFrame frame = new IdFrame(); 
					    frame.setVisible(true);
					    
					    frame.setAceptarButtonActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								System.out.println("Componentes elegidos para el ID compuesto: " + frame.getIds_componentes());
								processComposedID(frame);
								
							}
					    });	
					    
					}
				}
				else if (menuItem.equals(ADD_ENTITY_TO_RELATION_UP)) {
						final EntityRelationFrame frame1 = new EntityRelationFrame();
					    frame1.setVisible(true);
					    frame1.setAceptarButtonActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								processEntityRelationFrameUp(frame1);
							}
					    });
							
				}
				else if (menuItem.equals(ADD_ENTITY_TO_RELATION_DOWN)) {
					final EntityRelationFrame frame1 = new EntityRelationFrame();
				    frame1.setVisible(true);
				    frame1.setAceptarButtonActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							processEntityRelationFrameDown(frame1);
						}
				    });
				}
				else if (menuItem.equals(EDIT_RELATION_ENTITY)) {
					 GridElement element = actualSelector.getSelectedEntities().get(0);
					EntityRelation component=AppContext.getInstance().getProjectContext().getRelation(element.getIdPrimary()).getEntityRelation(element.getId());
					String role = component.getRole();
					String card = component.getCardinality();
					if (actualSelector.getSelectedEntities().size() == 1){
					final EntityRelationFrame frame1 = new EntityRelationFrame(role,card);
				    frame1.setVisible(true);
				    frame1.setAceptarButtonActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							changeAttributes(frame1);
							
						}

						
				    });
					}
				}				
				
				else if (menuItem.equals(GENERALIZATION)) {
					Specialization r = (Specialization)paletteHandler.getDrawPanel().getAllEntities().get(6).CloneFromMe();
					if (actualSelector.getSelectedEntities().size() == 1){
						String seleccion = JOptionPane.showInputDialog(Main.getInstance().getGUI(),"Especialización Ejm:(T,E)","Ingrese especialización",JOptionPane.QUESTION_MESSAGE);  
						GridElement element = actualSelector.getSelectedEntities().get(0);
						r.setPanelAttributes(seleccion);
						r.setIdPrimary(element.getId());
						onAdd(r, diagramHandler, element, 1);
						int width = 80;
						r.getLinePoints().get(0).setLocation(r.getLinePoints().get(1).getX() - element.getWidth()/2,
								r.getLinePoints().get(1).getY()+element.getHeight());
						
						r.getLinePoints().get(1).setLocation(r.getLinePoints().get(0).getX(),
								r.getLinePoints().get(0).getY()+width);
						
					}
				}
				else if (menuItem.equals(CAMBIAR_TIPO)) {
					if (actualSelector.getSelectedEntities().size() == 1){
						GridElement element = actualSelector.getSelectedEntities().get(0);
						Entity entidadSeleccionada = (Entity) AppContext.getInstance().getProjectContext().getComponent(element.getIdPrimary());
						
						final cambiarTipoFrame frame = new cambiarTipoFrame(); 
					    frame.setVisible(true);
					}
				}
				else if (menuItem.equals(ADD_ENTITY_TO_GENERALIZATION)) {
					Vector<GridElement> v = diagramHandler.getDrawPanel().getAllEntities();
					GridElement to = null;
					Long id = (Long)param;
					for(GridElement aux : v){
						if (aux.getId() == id){
							to = aux;
							break;
						}
					}
					Relation r = (Relation)paletteHandler.getDrawPanel().getAllEntities().get(8).CloneFromMe();
					if (actualSelector.getSelectedEntities().size() == 1 && to != null){
						Specialization element = (Specialization)actualSelector.getSelectedEntities().get(0);
						Long idnew=AppContext.getInstance().getProjectContext().getIdIncrement();
						r.setId(idnew);
						
						element.setId(idnew);
//						// Interaccion con el modelo
						Entity entity = AppContext.getInstance().getProjectContext().getEntity(id);
						ar.uba.fi.taller2.tp.model.Specialization generalization= 
						new ar.uba.fi.taller2.tp.model.Specialization(idnew,element.getIdPrimary(), element.getPanelAttributes());
//						relation.getEntitys().add(new EntityRelation(id.longValue(), Component.CARDINALIDAD_0_N));
						r.setIdPrimary(id);
						element.setIdPrimary(id);
						element.setGeneralization(generalization);
						entity.getGeneralizations().add(generalization);
						diagramHandler.getController().executeCommand(new AddElement(r,0, 0));
						
						r.getLinePoints().get(0).setLocation(element.getLinePoints().get(1).getLocation().getX() + element.getX(),
								element.getLinePoints().get(1).getLocation().getY() + element.getY());
						r.getLinePoints().get(1).setLocation(to.getStickingPoint(1).x + to.getWidth()/2 - r.getX(),
								to.getStickingPoint(1).y - r.getY());
						
					}
				}
				
				else if (menuItem.equals(ABOUT_PROGRAM)) {
				}
				else if (menuItem.equals(SET_FOREGROUND_COLOR) && (actualHandler != null)) {
					actualHandler.getController().executeCommand(new ChangeColor((String) param, true));
				}
				else if (menuItem.equals(SET_BACKGROUND_COLOR) && (actualHandler != null)) {
					actualHandler.getController().executeCommand(new ChangeColor((String) param, false));
				}
				else if (menuItem.equals(ALIGN) && (actualHandler != null) && (actualSelector != null)) {
					Vector<GridElement> v = actualSelector.getSelectedEntities();
					if (v.size() > 0) {
						actualHandler.getController().executeCommand(new Align(v, actualSelector.getDominantEntity(), (String) param));
					}
				}
			}
						
			private void eliminarAtributoSimple(GridElement v, DiagramHandler actualHandler) {
				
				
				Long idActual = v.getId();
				DiagramTree miDiagrama = null;			
				String nombreDiagrama = Main.getInstance().getDiagramHandler().getName();
				String[] partes = nombreDiagrama.split("_");
				nombreDiagrama = partes[0];
				List<DiagramTree> diagramas = AppContext.getInstance().getProjectEntity().getDiagramas();
				Iterator<DiagramTree> iterDiag = diagramas.iterator();
				while (iterDiag.hasNext()) {
					DiagramTree unDiag = iterDiag.next();
					if (nombreDiagrama.equals(unDiag.getNombre())) {
						miDiagrama = unDiag;
					}
				}
				if(miDiagrama==null){
					actualHandler.getController().executeCommand(new RemoveElement(v));
					return;
				}
				List<ar.uba.fi.taller2.tp.model.Entity> entidades = miDiagrama.getEntities();
				Iterator<ar.uba.fi.taller2.tp.model.Entity> iterEnt = entidades.iterator();
				ar.uba.fi.taller2.tp.model.Attribute miAtributo = null;
				ar.uba.fi.taller2.tp.model.Entity miEntidad = null;
				boolean encontrado = false;
				boolean continua = true;
				while( iterEnt.hasNext() && continua ) {
					ar.uba.fi.taller2.tp.model.Entity entActual = iterEnt.next();
					List<ar.uba.fi.taller2.tp.model.Attribute> susAtr = entActual.getAttributes();
					Iterator<ar.uba.fi.taller2.tp.model.Attribute> iterAt = susAtr.iterator();
					while( iterAt.hasNext() && continua ) {
						ar.uba.fi.taller2.tp.model.Attribute actual = iterAt.next();
						long result = idActual - actual.getId();
						if( result == 0 ) {
							miAtributo = actual;
							miEntidad = entActual;
							continua = false;
						}
						else if ( actual.getType().equals( TypeAttribute.COMPOSED ) ) {
							Iterator<ar.uba.fi.taller2.tp.model.Attribute> iter2 = actual.getAttributes().iterator();
							while( iter2.hasNext() ) {
								ar.uba.fi.taller2.tp.model.Attribute prox = iter2.next();
								if( idActual.equals( prox.getId() ) ) {
									miAtributo = prox;
									miEntidad = entActual;
									continua = false;
									encontrado = true;
								}
							}
						}
					}
				}
				
				if (!encontrado) {
					eliminarAtributoSimpleDeRelacion(v,actualHandler,miDiagrama);
					return;
				}
				
				// actualHandler.getDrawPanel().getComponents();
				Vector<GridElement> vector=new Vector<GridElement>();
				vector.add(v);
				
				// ahora saco al atributo de la lista de atributos de la entidad
				eliminarAtributoDeEntidad(miEntidad, miAtributo);
				
				// elimina todos los ids compuestos (por las dudas)
				miEntidad.eliminarIdentificadoresCompuestos();
				
				com.umlet.element.Identifiers idsActGraf = null;
				java.awt.Component[] comps = actualHandler.getDrawPanel().getComponents();
				for (int i = 0; i < comps.length; i++) {
					GridElement unGridElement = (GridElement) comps[i];
					Long primary = unGridElement.getIdPrimary();
					Long id_entidad = miEntidad.getId();
					if( (primary != null) && (primary.equals(id_entidad)) ) {
						if( unGridElement instanceof com.umlet.element.Identifiers ) {
							vector.add( unGridElement );
						}
					}
				}
				
				// ahora borra al atributo */
				actualHandler.getController().executeCommand(new RemoveElement(vector));
				
			}

			
			private void eliminarAtributoSimpleDeRelacion( GridElement v, DiagramHandler actualHandler , DiagramTree miDiagrama ) {
				Long idActual = v.getId();
				List<ar.uba.fi.taller2.tp.model.Relation> entidades = miDiagrama.getRelations();
				Iterator<ar.uba.fi.taller2.tp.model.Relation> iterEnt = entidades.iterator();
				ar.uba.fi.taller2.tp.model.Attribute miAtributo = null;
				ar.uba.fi.taller2.tp.model.Relation miEntidad = null;
				boolean encontrado = false;
				boolean continua = true;
				while( iterEnt.hasNext() && continua ) {
					ar.uba.fi.taller2.tp.model.Relation entActual = iterEnt.next();
					List<ar.uba.fi.taller2.tp.model.Attribute> susAtr = entActual.getAttributes();
					Iterator<ar.uba.fi.taller2.tp.model.Attribute> iterAt = susAtr.iterator();
					while( iterAt.hasNext() && continua ) {
						ar.uba.fi.taller2.tp.model.Attribute actual = iterAt.next();
						long result = idActual - actual.getId();
						if( result == 0 ) {
							miAtributo = actual;
							miEntidad = entActual;
							continua = false;
						}
						else if ( actual.getType().equals( TypeAttribute.COMPOSED ) ) {
							Iterator<ar.uba.fi.taller2.tp.model.Attribute> iter2 = actual.getAttributes().iterator();
							while( iter2.hasNext() ) {
								ar.uba.fi.taller2.tp.model.Attribute prox = iter2.next();
								if( idActual.equals( prox.getId() ) ) {
									miAtributo = prox;
									miEntidad = entActual;
									continua = false;
									encontrado = true;
								}
							}
						}
					}
				}
				if(miEntidad==null){
					actualHandler.getController().executeCommand(new RemoveElement(v));
					return;
				}
				// actualHandler.getDrawPanel().getComponents();
				Vector<GridElement> vector=new Vector<GridElement>();
				vector.add(v);
				
				// ahora saco al atributo de la lista de atributos de la entidad
				eliminarAtributoDeEntidad(miEntidad, miAtributo);
				
				com.umlet.element.Identifiers idsActGraf = null;
				java.awt.Component[] comps = actualHandler.getDrawPanel().getComponents();
				for (int i = 0; i < comps.length; i++) {
					GridElement unGridElement = (GridElement) comps[i];
					Long primary = unGridElement.getIdPrimary();
					Long id_entidad = miEntidad.getId();
					if( (primary != null) && (primary.equals(id_entidad)) ) {
						if( unGridElement instanceof com.umlet.element.Identifiers ) {
							vector.add( unGridElement );
						}
					}
				}
				
				// ahora borra al atributo */
				actualHandler.getController().executeCommand(new RemoveElement(vector));
			}

			private void eliminarAtributoDeEntidad(Component miEntidad, Attribute miAtributo) {
				
				if( miEntidad.getAttributes().contains(miAtributo) ) {
					miEntidad.getAttributes().remove(miAtributo);
				} else {
					Iterator<Attribute> iter = miEntidad.getAttributes().iterator();
					while( iter.hasNext() ) {
						Attribute actual = iter.next();
						if( actual.getType().equals( TypeAttribute.COMPOSED ) ) {
							eliminarPosibleAtributoDeCompuesto( actual , miAtributo );
						}
					}
				}
			}

			private void eliminarPosibleAtributoDeCompuesto(Attribute compuesto, Attribute miAtributo) {
				if( compuesto.getAttributes().contains(miAtributo) ) {
					compuesto.getAttributes().remove(miAtributo);
				} else {
					Iterator<Attribute> iter = compuesto.getAttributes().iterator();
					while( iter.hasNext() ) {
						Attribute actual = iter.next();
						if( actual.getType().equals( TypeAttribute.COMPOSED ) ) {
							eliminarPosibleAtributoDeCompuesto( actual , miAtributo );
						}
					}
				}
				return;
			}


			private void onAdd(GridElement r, DiagramHandler diagramHandler, GridElement element, int direction){
				Point p1 = element.getStickingPoint();
				diagramHandler.getController().executeCommand(new AddElement(r, 10, 10));
				if (element instanceof EER_Entity)
					diagramHandler.getController().executeCommand(new Move(r, p1.x-60, p1.y-40));
				if (element instanceof EER_Rel_Diamond && direction == 1)
					diagramHandler.getController().executeCommand(new Move(r, p1.x-element.getWidth()/2-40, p1.y-60));
				if (element instanceof EER_Rel_Diamond && direction == 2)
					diagramHandler.getController().executeCommand(new Move(r, p1.x-element.getWidth()/2-40, p1.y-60 + element.getHeight()));
				if (element instanceof UseCase)
					diagramHandler.getController().executeCommand(new Move(r, p1.x-60, p1.y-40));
				
			}
			
			private void processComposedAttribute(AttributeFrame frame){
				Main main = Main.getInstance();
				BaseGUI gui = main.getGUI();
				DiagramHandler diagramHandler = gui.getCurrentDiagram().getHandler();
				DiagramHandler actualHandler = main.getDiagramHandler();
				PaletteHandler paletteHandler = main.getPalette();//getPalettes().get("EER.xml");
				Selector actualSelector = actualHandler == null ? null : actualHandler.getDrawPanel().getSelector();
				GridElement element = actualSelector.getSelectedEntities().get(0);				
				Long id=AppContext.getInstance().getProjectContext().getIdIncrement();
				if(menuItem.equals(ADD_COMPOSED_ATTRIBUTE)){
//					Component component= AppContext.getInstance().getProjectContext().getComponent(element.getIdPrimary());
//					Attribute attributeModel=new Attribute(id, r.getPanelAttributes(), TypeAttribute.SIMPLE);
//					component.getAttributes().add(attributeModel);
//					attributeModel.setCardinality(frame.getCardinalidad());
//					attributeModel.setName(frame.getNombre());
//					r.setId(id);
//					r.setAttribute(attributeModel);
//					r.setIdPrimary(element.getId());
//					onAdd(r, diagramHandler, element, 1);
					
					Relation r = (Relation)paletteHandler.getDrawPanel().getAllEntities().get(8).CloneFromMe();
					UseCase oval = (UseCase)paletteHandler.getDrawPanel().getAllEntities().get(7).CloneFromMe();
					if (actualSelector.getSelectedEntities().size() == 1){
						Component component = AppContext.getInstance().getProjectContext().getComponent(element.getId());
						
						final Attribute attributeModel=new Attribute(id, r.getPanelAttributes(), TypeAttribute.COMPOSED);
						component.getAttributes().add(attributeModel);
						r.setId(id);
						r.setIdPrimary(component.getId());
						oval.setId(id);
						oval.setIdPrimary(component.getId());
						oval.setAttribute(attributeModel);
						// Agrego Id al componente
						onAdd(r, diagramHandler, element, 1);
						
						attributeModel.setName(frame.getNombre());
						attributeModel.setCardinality(frame.getCardinalidad());
						
						Point p1 = new Point(r.getX()+ r.getWidth(), r.getY());
						diagramHandler.getController().executeCommand(new AddElement(oval, 10, 10));
						diagramHandler.getController().executeCommand(new Move(oval, p1.x+40, p1.y));
					}
					
					
				}else{
					com.umlet.element.Attribute r = (com.umlet.element.Attribute)paletteHandler.getDrawPanel().getAllEntities().get(4).CloneFromMe();
					Component component = AppContext.getInstance().getProjectContext().getComponent(element.getId());
					Attribute attributeModel=new Attribute(id, r.getPanelAttributes(), TypeAttribute.SIMPLE);
					component.getAttributes().add(attributeModel);
					r.setId(id);
					r.setAttribute(attributeModel);
					if(element.getClass().getSimpleName().equals("UseCase")){
						r.setIdPrimary(element.getIdPrimary());
					}else{
						r.setIdPrimary(element.getId());
					}
						
					attributeModel.setCardinality(frame.getCardinalidad());
					attributeModel.setName(frame.getNombre());
					onAdd(r, diagramHandler, element, 1);
				}
				// Interaccion con el modelo
				
			}

			private void changeAttributes(EntityRelationFrame frame1) {
				Main main = Main.getInstance();
				DiagramHandler actualHandler = main.getDiagramHandler();
				Selector actualSelector = actualHandler == null ? null : actualHandler.getDrawPanel().getSelector();
				 GridElement element = actualSelector.getSelectedEntities().get(0);
				EntityRelation component=AppContext.getInstance().getProjectContext().getRelation(element.getIdPrimary()).getEntityRelation(element.getId());
				component.setRole(frame1.getNombre());
				component.setCardinality(frame1.getCardinalidad());
				element.setPanelAttributes(getAtributtes(frame1));
				
			}
			private void processEntityRelationFrameUp(EntityRelationFrame frame){
				Main main = Main.getInstance();
				BaseGUI gui = main.getGUI();
				DiagramHandler diagramHandler = gui.getCurrentDiagram().getHandler();
				DiagramHandler actualHandler = main.getDiagramHandler();
				PaletteHandler paletteHandler = main.getPalette();
				Vector<GridElement> v = diagramHandler.getDrawPanel().getAllEntities();
				Selector actualSelector = actualHandler == null ? null : actualHandler.getDrawPanel().getSelector();
				GridElement to = null;
				Long id = (Long)param;
				for(GridElement aux : v){
					if (aux.getId() == id){
						to = aux;
						break;
					}
				}
				RelationEntity r = (RelationEntity)paletteHandler.getDrawPanel().getAllEntities().get(1).CloneFromMe();
				if (actualSelector.getSelectedEntities().size() == 1 && to != null){
					GridElement element = actualSelector.getSelectedEntities().get(0);
				Long idnew=AppContext.getInstance().getProjectContext().getIdIncrement();
				// Interaccion con el modelo
				ar.uba.fi.taller2.tp.model.Relation relation = AppContext.getInstance().getProjectContext().getRelation(element.getId());
				EntityRelation relation2=new EntityRelation(idnew,frame.getCardinalidad(), id,frame.getNombre());
				relation.getEntitys().add(relation2);
				r.setId(idnew);
				r.setEntityRelation(relation2);
				r.setIdPrimary(element.getId());
				r.setPanelAttributes(getAtributtes(frame));
				onAdd(r, diagramHandler, element, 1);
				r.getLinePoints().get(1).setLocation(to.getStickingPoint(1).x - r.getX(),
						to.getStickingPoint(1).y - r.getY());
				}
			}
		    private String getAtributtes(EntityRelationFrame frame){
		    	return frame.getNombre()+"\nm1="+frame.getCardinalidad();
		    }
			private void processEntityRelationFrameDown(EntityRelationFrame frame){
				Main main = Main.getInstance();
				BaseGUI gui = main.getGUI();
				DiagramHandler diagramHandler = gui.getCurrentDiagram().getHandler();
				DiagramHandler actualHandler = main.getDiagramHandler();
				PaletteHandler paletteHandler = main.getPalette();
				Vector<GridElement> v = diagramHandler.getDrawPanel().getAllEntities();
				Selector actualSelector = actualHandler == null ? null : actualHandler.getDrawPanel().getSelector();
				GridElement to = null;
				Long id = (Long)param;
				for(GridElement aux : v){
					if (aux.getId() == id){
						to = aux;
						break;
					}
				}
				RelationEntity r = (RelationEntity)paletteHandler.getDrawPanel().getAllEntities().get(1).CloneFromMe();
				if (actualSelector.getSelectedEntities().size() == 1 && to != null){
					GridElement element = actualSelector.getSelectedEntities().get(0);
				Long idnew=AppContext.getInstance().getProjectContext().getIdIncrement();
				// Interaccion con el modelo
				ar.uba.fi.taller2.tp.model.Relation relation = AppContext.getInstance().getProjectContext().getRelation(element.getId());
				EntityRelation relation2=new EntityRelation(idnew, frame.getCardinalidad(),id,frame.getNombre());
				relation.getEntitys().add(relation2);
				r.setId(idnew);
				r.setEntityRelation(relation2);
				r.setIdPrimary(element.getId());
				r.setPanelAttributes(getAtributtes(frame));
				onAdd(r, diagramHandler, element, 2);
				r.getLinePoints().get(1).setLocation(to.getStickingPoint(1).x - r.getX(),
						to.getStickingPoint(1).y - r.getY());
				}
			}
			
			private void processComposedID(IdFrame frame){
				Main main = Main.getInstance();
				BaseGUI gui = main.getGUI();
				DiagramHandler diagramHandler = gui.getCurrentDiagram().getHandler();
				DiagramHandler actualHandler = main.getDiagramHandler();
				PaletteHandler paletteHandler = main.getPalette();//getPalettes().get("EER.xml");
				Selector actualSelector = actualHandler == null ? null : actualHandler.getDrawPanel().getSelector();
				if (actualSelector.getSelectedEntities().size() == 1){
					// Interaccion con el modelo
					Identifiers identifier= new Identifiers();
					for (Long id : frame.getIds_componentes())
						identifier.addIds(id);
					GridElement element = actualSelector.getSelectedEntities().get(0);
					Entity component = AppContext.getInstance().getProjectContext().getEntity(element.getId());
					component.getIdentifiers().add(identifier);
					
					// Graficar componente
					for (int i = 0; i < frame.getIds_componentes().size()-1;i++) {
						com.umlet.element.Identifiers r = (com.umlet.element.Identifiers)paletteHandler.getDrawPanel().getAllEntities().get(2).CloneFromMe();
//						r.setId(element.getId());
//						r.setAttribute(attributeModel);
						r.setIdPrimary(element.getId());
						Point p1 = element.getStickingPoint();
						diagramHandler.getController().executeCommand(new AddElement(r, p1.x + 30 + 20 * i -30, p1.y - 30 - 20 * i -30));
					}
				}
				
			}
			
		});
	}
	
	protected void eliminarRelationEntity(GridElement v,DiagramHandler actualHandler) {
	 EntityRelation entityRelation=  AppContext.getInstance().getRelation(v.getIdPrimary()).getEntityRelation(v.getId());
	 AppContext.getInstance().getRelation(v.getIdPrimary()).getEntitys().remove(entityRelation);
	 actualHandler.getController().executeCommand(new RemoveElement(v));
	}

	// These components should only be enabled if the drawpanel is not empty
	protected List<EER_Entity> diagramEntities = new ArrayList<EER_Entity>();
	public void updateDiagramEntities() {
		DrawPanel currentDiagram = Main.getInstance().getGUI().getCurrentDiagram();
		if (currentDiagram == null) return; //Possible if method is called at loading a palette
		DiagramHandler handler = currentDiagram.getHandler();
		diagramEntities.clear();
		for (GridElement component : handler.getDrawPanel().getAllEntities()) {
			if (component instanceof EER_Entity)
				diagramEntities.add((EER_Entity)component);
		}
	}

	
	// These components should only be enabled if the drawpanel is not empty
	protected List<JComponent> diagramDependendComponents = new ArrayList<JComponent>();
	public void updateDiagramDependendComponents() {
		DrawPanel currentDiagram = Main.getInstance().getGUI().getCurrentDiagram();
		if (currentDiagram == null) return; //Possible if method is called at loading a palette
		DiagramHandler handler = currentDiagram.getHandler();
		boolean enable = !((handler == null) || handler.getDrawPanel().getAllEntities().isEmpty());
		for (JComponent component : diagramDependendComponents) {
			component.setEnabled(enable);
		}

	}
	
	protected void eliminarAtributoCompuesto( GridElement v , DiagramHandler actualHandler ) {
		
		Long idActual = v.getId();
		DiagramTree miDiagrama = null;			
		String nombreDiagrama = Main.getInstance().getDiagramHandler().getName();
		String[] partes = nombreDiagrama.split("_");
		nombreDiagrama = partes[0];
		List<DiagramTree> diagramas = AppContext.getInstance().getProjectEntity().getDiagramas();
		Iterator<DiagramTree> iterDiag = diagramas.iterator();
		while (iterDiag.hasNext()) {
			DiagramTree unDiag = iterDiag.next();
			if (nombreDiagrama.equals(unDiag.getNombre())) {
				miDiagrama = unDiag;
			}
		}
		List<ar.uba.fi.taller2.tp.model.Entity> entidades = miDiagrama.getEntities();
		Iterator<ar.uba.fi.taller2.tp.model.Entity> iterEnt = entidades.iterator();
		ar.uba.fi.taller2.tp.model.Attribute miAtributo = null;
		ar.uba.fi.taller2.tp.model.Entity miEntidad = null;
		boolean encontrado = false;
		boolean continua = true;
		while( iterEnt.hasNext() && continua ) {
			ar.uba.fi.taller2.tp.model.Entity entActual = iterEnt.next();
			List<ar.uba.fi.taller2.tp.model.Attribute> susAtr = entActual.getAttributes();
			Iterator<ar.uba.fi.taller2.tp.model.Attribute> iterAt = susAtr.iterator();
			while( iterAt.hasNext() && continua ) {
				ar.uba.fi.taller2.tp.model.Attribute actual = iterAt.next();
				long result = idActual - actual.getId();
				if( result == 0 ) {
					miAtributo = actual;
					miEntidad = entActual;
					continua = false;
					encontrado = true;
				}
			}
		}
		
		if( !encontrado ) {
			eliminarAtributoCompuestoDeRelacion( v , actualHandler , miDiagrama );
			return;
		}
		
		Set<Long> setDeSimples = new HashSet<Long>();
		Iterator<Attribute> subAtributos = miAtributo.getAttributes().iterator();
		while( subAtributos.hasNext() ) {
			Attribute proximo = subAtributos.next();
			if( proximo.getType().equals( TypeAttribute.COMPOSED ) ) {
				GridElement x = buscarUnGridElement( proximo.getId() , actualHandler );
				if( x == null ) { System.out.println("TODO MAAAAAAAAAAAAAAAAAAAL!"); }
				eliminarAtributoCompuesto(x, actualHandler );
			} else {
				setDeSimples.add( proximo.getId() );
			}
		}
		
		// actualHandler.getDrawPanel().getComponents();
		Vector<GridElement> vector=new Vector<GridElement>();
		vector.add(v);
		
		// ahora saco al atributo de la lista de atributos de la entidad */
		miEntidad.getAttributes().remove(miAtributo);
		
		// elimina todos los ids compuestos (por las dudas)
//		miEntidad.eliminarIdentificadoresCompuestos();
		
		com.umlet.element.Identifiers idsActGraf = null;
		java.awt.Component[] comps = actualHandler.getDrawPanel().getComponents();
		for (int i = 0; i < comps.length; i++) {
			GridElement unGridElement = (GridElement) comps[i];
			Long id_elemento = unGridElement.getId();
			if( setDeSimples.contains( id_elemento ) ) {
				vector.add( unGridElement );
			}
			else {
				if( id_elemento.equals( miAtributo.getId() ) && unGridElement instanceof Relation ) {
					vector.add( unGridElement );
				}
			}
		}
				
		// ahora borra al atributo */
		actualHandler.getController().executeCommand(new RemoveElement(vector));
	}

	private void eliminarAtributoCompuestoDeRelacion(GridElement v,
			DiagramHandler actualHandler, DiagramTree miDiagrama) {
		
		Long idActual = v.getId();
		List<ar.uba.fi.taller2.tp.model.Relation> entidades = miDiagrama.getRelations();
		Iterator<ar.uba.fi.taller2.tp.model.Relation> iterEnt = entidades.iterator();
		ar.uba.fi.taller2.tp.model.Attribute miAtributo = null;
		ar.uba.fi.taller2.tp.model.Relation miEntidad = null;
		boolean encontrado = false;
		boolean continua = true;
		while( iterEnt.hasNext() && continua ) {
			ar.uba.fi.taller2.tp.model.Relation entActual = iterEnt.next();
			List<ar.uba.fi.taller2.tp.model.Attribute> susAtr = entActual.getAttributes();
			Iterator<ar.uba.fi.taller2.tp.model.Attribute> iterAt = susAtr.iterator();
			while( iterAt.hasNext() && continua ) {
				ar.uba.fi.taller2.tp.model.Attribute actual = iterAt.next();
				long result = idActual - actual.getId();
				if( result == 0 ) {
					miAtributo = actual;
					miEntidad = entActual;
					continua = false;
					encontrado = true;
				}
			}
		}
		if(miAtributo==null){
			Long id2=v.getId();
			actualHandler.getController().executeCommand(new RemoveElement(v));
			GridElement x = buscarUnGridElement( id2 , actualHandler );
			actualHandler.getController().executeCommand(new RemoveElement(x));
			return;
		}
			
		Set<Long> setDeSimples = new HashSet<Long>();
		Iterator<Attribute> subAtributos = miAtributo.getAttributes().iterator();
		while( subAtributos.hasNext() ) {
			Attribute proximo = subAtributos.next();
			if( proximo.getType().equals( TypeAttribute.COMPOSED ) ) {
				GridElement x = buscarUnGridElement( proximo.getId() , actualHandler );
				if( x == null ) { System.out.println("TODO MAAAAAAAAAAAAAAAAAAAL!"); }
				eliminarAtributoCompuesto(x, actualHandler );
			} else {
				setDeSimples.add( proximo.getId() );
			}
		}
		
		// actualHandler.getDrawPanel().getComponents();
		Vector<GridElement> vector=new  Vector<GridElement>();
		vector.add(v);
		
		// ahora saco al atributo de la lista de atributos de la entidad */
		miEntidad.getAttributes().remove(miAtributo);
		
		com.umlet.element.Identifiers idsActGraf = null;
		java.awt.Component[] comps = actualHandler.getDrawPanel().getComponents();
		for (int i = 0; i < comps.length; i++) {
			GridElement unGridElement = (GridElement) comps[i];
			Long id_elemento = unGridElement.getId();
			if( setDeSimples.contains( id_elemento ) ) {
				vector.add( unGridElement );
			}
			else {
				if( id_elemento.equals( miAtributo.getId() ) && unGridElement instanceof Relation ) {
					vector.add( unGridElement );
				}
			}
		}
				
		// ahora borra al atributo */
		actualHandler.getController().executeCommand(new RemoveElement(vector));
		
	}

	private GridElement buscarUnGridElement(Long idActual, DiagramHandler actualHandler) {
		
		System.out.println("\n\nDEBUG: está buscando un grid element...\n\n");
		
		java.awt.Component[] comps = actualHandler.getDrawPanel().getComponents();
		for (int i = 0; i < comps.length; i++) {
			GridElement unGridElement = (GridElement) comps[i];
			Long id_elemento = unGridElement.getId();
			if( idActual.equals( id_elemento ) ) {
				return unGridElement;
			}
		}
		
		return null;
	}

}

