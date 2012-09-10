package ar.uba.fi.taller2.tp.control.context;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ar.uba.fi.taller2.tp.model.Attribute;
import ar.uba.fi.taller2.tp.model.Component;
import ar.uba.fi.taller2.tp.model.Entity;
import ar.uba.fi.taller2.tp.model.Relation;
import ar.uba.fi.taller2.tp.model.TypeAttribute;

public class ProjectContext {
	
	private List<Entity> entities;
	private List<Relation> relations;
	private Long idIncrement;
    public Long getIdIncrement() {
		return ++idIncrement;
	}
	public ProjectContext() {
		super();
		entities= new  ArrayList<Entity>();
		relations= new  ArrayList<Relation>();
		idIncrement=Long.valueOf(0);
	}
	public List<Relation> getRelations() {
		return relations;
	}
	public void setRelations(List<Relation> relations) {
		this.relations = relations;
	}
	public List<Entity> getEntities() {
		return entities;
	}
	public void setEntities(List<Entity> entities) {
		this.entities = entities;
	}
	public Entity getNewEntity(){
		Entity entity= new Entity(++idIncrement, "Entidad");
		this.getEntities().add(entity);
		return entity;
	}
	
	public Entity getNewEntity(String name){
		Entity entity= new Entity(++idIncrement, name);
		this.getEntities().add(entity);
		return entity;
	}
	public Entity getEntity(Long id){
		return	(Entity) getComponent(id,entities);
	}
	public Relation getNewRelation(){
		Relation relation= new Relation(++idIncrement, "Relacion");
		this.getRelations().add(relation);
		return relation;
	}
	public Relation getNewRelation(String nombre){
		Relation relation= new Relation(++idIncrement, nombre);
		this.getRelations().add(relation);
		return relation;
	}
	public Relation getRelation(Long id){
		return	(Relation) getComponent(id,relations);
	}
	
	public Component getComponent(Long id){
		List<Component> aux = new ArrayList<Component>();
		for (Iterator<Entity> iterator = this.entities.iterator(); iterator.hasNext();) {
			Entity type = (Entity) iterator.next();
			for (Iterator<Attribute> iterator2 = type.getAttributes().iterator(); iterator2.hasNext();) {
				Attribute component = (Attribute) iterator2.next();
				aux.add(component);
				if(component.getType().equals(TypeAttribute.COMPOSED)){
					for (Iterator<Attribute> iterator3 = component.getAttributes().iterator(); iterator3
							.hasNext();) {
						Attribute attribute = (Attribute) iterator3.next();
						aux.add(attribute);
					}
				}
			}
		}
		for (Iterator<Relation> iterator = this.relations.iterator(); iterator.hasNext();) {
			Relation relation = (Relation) iterator.next();
			aux.add(relation);
			for (Iterator<Attribute> iterator2 = relation.getAttributes().iterator(); iterator2.hasNext();) {
				Attribute component = (Attribute) iterator2.next();
				aux.add(component);
				if(component.getType().equals(TypeAttribute.COMPOSED)){
					for (Iterator<Attribute> iterator3 = component.getAttributes().iterator(); iterator3
							.hasNext();) {
						Attribute attribute = (Attribute) iterator3.next();
						aux.add(attribute);
					}
				}
			}
		}
		aux.addAll(relations);
		aux.addAll(entities);
		return	getComponent(id,aux);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Component getComponent(Long id,List components){
		for (Iterator<Component> iterator = components.iterator(); iterator.hasNext();) {
			Component component = (Component) iterator.next();
			if(component.getId().equals(id))
				return component;
			
		}
		return null;
	}
	public void setIdIncrement(Long idIncrement) {
		this.idIncrement = idIncrement;
	}
}
