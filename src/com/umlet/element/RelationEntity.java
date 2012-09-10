/**
 * 
 */
package com.umlet.element;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Vector;

import ar.uba.fi.taller2.tp.control.context.AppContext;
import ar.uba.fi.taller2.tp.model.EntityRelation;

import com.baselet.element.GridElement;

/**
 * @author oscar
 *
 */
public class RelationEntity extends Relation {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2607432264140146101L;
	Vector<String> _strings;
	private EntityRelation entityRelation;
	public EntityRelation getEntityRelation() {
		return entityRelation;
	}
	public void setEntityRelation(EntityRelation entityRelation) {
		this.entityRelation = entityRelation;
	}
	@Override
	public void setPanelAttributes(String state) {
//		super.setPanelAttributes("lt=-");
	
		if(this.getId().equals(Long.valueOf(-1))){
			this.panelAttributes=state;
			return;
		}
		if(this.entityRelation==null){
			if( AppContext.getInstance().getProjectContext().getRelation(this.getIdPrimary()) != null ) {
				this.entityRelation=AppContext.getInstance().getProjectContext().getRelation(this.getIdPrimary()).getEntityRelation(this.getId());
				this.panelAttributes=this.entityRelation.getRole()+"\nm1="+this.entityRelation.getCardinality();
			} else {
				this.entityRelation = null;
				this.panelAttributes = "nada para destacar";
			}
		}
	
		if(!state.isEmpty()){
			if(this.entityRelation!=null)
			this.panelAttributes =state;
		}
		super.setPanelAttributes(this.panelAttributes);
	}
	@Override
	public String getPanelAttributes() {
		return panelAttributes;
	}
	

	public  RelationEntity() {
		super();
	}
	@Override
	public GridElement CloneFromMe() {
		RelationEntity c = new RelationEntity();
		c.setPanelAttributes(this.getPanelAttributes());
		c.setAdditionalAttributes(this.getAdditionalAttributes());

		c.setVisible(true);
		c.setBounds(this.getBounds());
		c.setHandler(this.getHandler());

		return c;
	}
}
