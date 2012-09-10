package ar.uba.fi.taller2.tp.control.context;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import ar.uba.fi.taller2.tp.model.DiagramTree;
import ar.uba.fi.taller2.tp.model.Entity;
import ar.uba.fi.taller2.tp.model.Relation;

import com.baselet.control.Main;
import com.baselet.diagram.DiagramHandler;
import com.baselet.diagram.command.RemoveElement;
import com.baselet.element.GridElement;

public class ProjectEntity {


	private String name;
	private String parentDir;

	private String nameComponent;
	private List<String> listDiagrams;

	public ProjectEntity() {
		super();
		listDiagrams= new ArrayList<String>();
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNameComponent() {
		return nameComponent;
	}
	public void setNameComponent(String nameComponent) {
		this.nameComponent = nameComponent;
	}
	public String getParentDir() {
		return parentDir;
	}
	public void setParentDir(String parentDir) {
		this.parentDir = parentDir;
	}
	public List<String> getListDiagrams() {
		return listDiagrams;
	}
	public void setListDiagrams(List<String> listDiagrams) {
		this.listDiagrams = listDiagrams;
	}
	public void addDiagram(String diagram){
		if(!this.listDiagrams.contains(diagram)){
			this.listDiagrams.add(diagram);
		}
	}

	public List<DiagramTree> getDiagramas() {
		List<DiagramTree> diagramTrees= new ArrayList<DiagramTree>();
		for (Iterator<String> iterator = this.listDiagrams.iterator(); iterator.hasNext();) {
			String diagram = (String) iterator.next();
			File file = new File(diagram);

			List<Entity> entities= new ArrayList<Entity>();
	         List<Relation> relations= new ArrayList<Relation>();
	         DiagramTree diagramTree= new DiagramTree();
	         diagramTree.setEntities(entities);
	         diagramTree.setRelations(relations);
	         diagramTree.setNombre(file.getName().replace("_rep.xml", ""));
			DiagramHandler diagramH = new DiagramHandler(file);
			 java.awt.Component[] comp= diagramH.getDrawPanel().getComponents();
			  for (int i = 0; i < comp.length; i++) {
				  GridElement co=(GridElement) comp[i];
				  if(co.getClass().getSimpleName().equals("EER_Entity")){
				  Entity entity=AppContext.getInstance().getEntity(co.getId());
				  entities.add(entity);
				  }
				  if(co.getClass().getSimpleName().equals("EER_Rel_Diamond")){
				  Relation relation=AppContext.getInstance().getRelation(co.getId());
				   relations.add(relation);
				  }
				  
				  
				  
			}
			  diagramTrees.add(diagramTree);
		}

		return diagramTrees;
	}
	
	
	public Vector<GridElement> getComponet(	String diagram,Long id){
		Vector<GridElement> elements= new Vector<GridElement>();
		File file = new File(diagram);
		DiagramHandler diagramH = new DiagramHandler(file);
		java.awt.Component[] comp= diagramH.getDrawPanel().getComponents();
		for (int i = 0; i < comp.length; i++) {

			GridElement co=(GridElement) comp[i];
			if(co.getIdPrimary()!=null){
				if(co.getIdPrimary().equals(id)||co.getId().equals(id)){
					elements.add(co);
				}
			}else 	if(co.getId().equals(id)){
				elements.add(co);
			} 

		}
		return elements;
	}
		public Vector<GridElement> getEER_Entity(Long id){
         Map<Integer, Vector<GridElement> > map= new HashMap<Integer, Vector<GridElement>>();
			for (Iterator<String> iterator = this.listDiagrams.iterator(); iterator.hasNext();) {
				String diagram = (String) iterator.next();
				Vector<GridElement> elements=getComponet(diagram, id);
				map.put(elements.size(), elements);
			}
			List<Integer> list=new ArrayList<Integer>(map.keySet());
			Collections.sort(list);
			return map.get(list.get(list.size()-1));
		}
		public Vector<GridElement> getAll(Long id){
			 Vector<GridElement> elementsAll= new Vector<GridElement>();
				for (Iterator<String> iterator = this.listDiagrams.iterator(); iterator.hasNext();) {
					String diagram = (String) iterator.next();
					Vector<GridElement> elements=getComponet(diagram, id);
					elementsAll.addAll(elements);
				}
				return elementsAll;
			}
		public void removeOthersEntities(String fileName, Long id) {
			for (Iterator<String> iterator = this.listDiagrams.iterator(); iterator.hasNext();) {
				String diagram = (String) iterator.next();
				if(!diagram.equals(fileName)){
					Vector<GridElement> elements= new Vector<GridElement>();
					File file = new File(diagram);
					DiagramHandler diagramH =Main.getInstance().getDiagramHandler(file.getName());
					java.awt.Component[] comp= diagramH.getDrawPanel().getComponents();
					for (int i = 0; i < comp.length; i++) {
						GridElement co=(GridElement) comp[i];
						if(co.getIdPrimary()!=null){
							if(co.getIdPrimary().equals(id)||co.getId().equals(id)){
								elements.add(co);
							}
						}else 	if(co.getId().equals(id)){
							elements.add(co);
						} 
					}
					diagramH.getController().executeCommand(new RemoveElement(elements));
					diagramH.doSave();
					
					
				}
			}
		}


}
