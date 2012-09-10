package com.umlet.element;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Vector;

import ar.uba.fi.taller2.tp.control.context.AppContext;
import ar.uba.fi.taller2.tp.model.Attribute;

import com.baselet.element.GridElement;

public class AttributePK extends Relation {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Vector<String> _strings;
	private Attribute attribute;
	public Attribute getAttribute() {
		return attribute;
	}
	public void setAttribute(Attribute attribute) {
		this.attribute = attribute;
	}
	@Override
	public String getPanelAttributes() {
		if(this.attribute!=null)
		   return this.attribute.getName();  
		return panelAttributes;
	}
	@Override
	public void setPanelAttributes(String state) {
		super.setPanelAttributes("lt=<(*)");
		if(this.getId().equals(Long.valueOf(-1))){
			this.panelAttributes=state;
			this.getStrings().add(this.panelAttributes);
			return;
		}
		if(this.attribute==null){
			this.attribute=(ar.uba.fi.taller2.tp.model.Attribute) AppContext.getInstance().getProjectContext().getComponent(this.getId());
			if(this.attribute!=null) {
				this.panelAttributes=this.attribute.getName();
			} else {
				this.panelAttributes="Este atributoPK fue borrado";
			}
				
		}
		if(!state.isEmpty()){
			if(this.attribute!=null)
			this.attribute.setName(state);
			this.panelAttributes =state;
		}
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
	
	
	public AttributePK() {
		super();
	}
	@Override
	public GridElement CloneFromMe() {
		AttributePK c = new AttributePK();
         System.out.println(this.getPanelAttributes());
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
