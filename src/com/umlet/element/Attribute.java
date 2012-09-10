package com.umlet.element;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Vector;

import ar.uba.fi.taller2.tp.control.context.AppContext;
import ar.uba.fi.taller2.tp.model.Component;

import com.baselet.element.GridElement;

public class Attribute extends Relation {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Vector<String> _strings;

	private ar.uba.fi.taller2.tp.model.Attribute attribute;
	public ar.uba.fi.taller2.tp.model.Attribute getAttribute() {
		return attribute;
	}
	public void setAttribute(ar.uba.fi.taller2.tp.model.Attribute attribute) {
		this.attribute = attribute;
	}
	@Override
	public String getPanelAttributes() {
		if(this.attribute!=null){
		   String str =this.attribute.getName();
			if (!this.attribute.getCardinality().equals(Component.CARDINALIDAD_1_1) )
				str+="\n" + this.attribute.getCardinality();
			return str;
		  }  
		return panelAttributes;
	}
	@Override
	public void setPanelAttributes(String state) {
//		this.setStrings(null);
		super.setPanelAttributes("lt=<()");
	
		if(this.getId().equals(Long.valueOf(-1))){
			this.panelAttributes=state;
			this.getStrings().add(this.panelAttributes);
			return;
		}
		if(this.attribute==null){
			this.attribute=(ar.uba.fi.taller2.tp.model.Attribute) AppContext.getInstance().getProjectContext().getComponent(this.getId());
			if( this.attribute!=null ) {
				String str =this.attribute.getName();
				if (this.attribute.getCardinality()!=null&&!this.attribute.getCardinality().equals(Component.CARDINALIDAD_1_1) )
					str+="\n" + this.attribute.getCardinality();
				this.panelAttributes=str;	
			} else {
				//TODO para los que acabamos de borrar...
				this.panelAttributes="Nombre generico";
			}
			
		}
		if(!state.isEmpty()){
			if(this.attribute!=null)
			this.attribute.setName(state);
			this.panelAttributes =state;
		}
//		this.getStrings().add(this.panelAttributes);
	}
	private Vector<String> getStrings() {
	if (_strings == null) {
		_strings = new Vector<String>();
	}
	return _strings;
}
	private void setStrings(Vector<String> v) {
		_strings = v;
	}

	@Override
	public void paintEntity(Graphics g) {
		super.paintEntity(g);
		Graphics2D g2 = (Graphics2D) g;
				Point start = this.getCenterOfLine();
				int yPos = start.y - (int) this.getHandler().getFontHandler().getDistanceBetweenTexts(); // B. Buckl
				int xPos = start.x;
					this.getHandler().getFontHandler().writeText(g2, this.getPanelAttributes(), xPos, yPos, true);
					yPos += (int) this.getHandler().getFontHandler().getFontSize();
					yPos += this.getHandler().getFontHandler().getDistanceBetweenTexts();
	}
	
	public Attribute() {
		super();
	}
	@Override
	public GridElement CloneFromMe() {
		Attribute c = new Attribute();
		c.setPanelAttributes(this.getPanelAttributes());
		c.setAdditionalAttributes(this.getAdditionalAttributes());

		c.setVisible(true);
		c.setBounds(this.getBounds());
		c.setHandler(this.getHandler());
		if(this.getId()!=null)
		c.setId(this.getId());
		if(this.getIdPrimary()!=null)
		c.setIdPrimary(this.getIdPrimary());
		return c;
	}

}
