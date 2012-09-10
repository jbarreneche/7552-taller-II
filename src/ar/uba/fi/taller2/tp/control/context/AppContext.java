package ar.uba.fi.taller2.tp.control.context;

import ar.uba.fi.taller2.tp.model.Entity;
import ar.uba.fi.taller2.tp.model.Relation;
import ar.uba.fi.taller2.tp.model.TypeEntity;
import ar.uba.fi.taller2.tp.model.TypeRelation;


public class AppContext {
	private static AppContext context=null;
	private ProjectContext projectContext=new ProjectContext();
	private ProjectEntity projectEntity= new ProjectEntity();
	private Entity entidadEntity = new Entity(null,null);
	private Relation relacionRelation = new Relation(null,null);
	
	private Long countNewProject;

	public Long getCountNewProject() {
		return countNewProject;
	}
	private AppContext(){
		countNewProject=Long.valueOf(0);
	}
	
	public void incrementCount(){
		++countNewProject;
	}
	public static AppContext getInstance() {
		if(context==null)
			context= new AppContext();
		return context;
	}
	public ProjectContext getProjectContext() {
		return projectContext;
	}
	public void setProjectContext(ProjectContext projectContext) {
		this.projectContext = projectContext;
	}
	public Entity getEntity(Long id) {
		return this.projectContext.getEntity(id);
		
	}
	public Relation getRelation(Long id) {
		return this.projectContext.getRelation(id);
		
	}
	public ProjectEntity getProjectEntity() {
		return projectEntity;
	}
	public void setProjectEntity(ProjectEntity projectEntity) {
		this.projectEntity = projectEntity;
	}
	
	public void setEntidadEntity(String name, TypeEntity tipo){
		this.entidadEntity.setName(name);
		this.entidadEntity.setTypeEntity(tipo);
	}
	public String getRelacionRelationName(){
		return this.relacionRelation.getName();
	}
	public TypeRelation getRelacionRelationType(){
		return this.relacionRelation.getTypeRelation();
	}

	public void setRelacionRelation(String name, TypeRelation tipo){
		this.relacionRelation.setName(name);
		this.relacionRelation.setTypeRelation(tipo);
	}
	public String getEntidadEntityName(){
		return this.entidadEntity.getName();
	}
	public TypeEntity getEntidadEntityType(){
		return this.entidadEntity.getTypeEntity();
	}
}
