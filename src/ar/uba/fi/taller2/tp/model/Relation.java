package ar.uba.fi.taller2.tp.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class Relation extends Component{
	private TypeRelation typeRelation; 
	
	public TypeRelation getTypeRelation() {
		return typeRelation;
	}
	public void setTypeRelation(TypeRelation typeRelation) {
		this.typeRelation = typeRelation;
	}

	private List<EntityRelation> entitys;
	
	public List<EntityRelation> getEntitys() {
		return entitys;
	}

	public void setEntitys(List<EntityRelation> entitys) {
		this.entitys = entitys;
	}

	public Relation(Long id, String name) {
		super(id, name);
		this.entitys= new ArrayList<EntityRelation>();
	}

	public EntityRelation getEntityRelation(Long numAttribute) {
	    	
	    	for (Iterator<EntityRelation> iterator = entitys.iterator(); iterator.hasNext();) {
	    		EntityRelation attribute = (EntityRelation) iterator.next();
				if(attribute.getId().equals(numAttribute))
					return attribute;
			}
			return null;
	}
	
}
