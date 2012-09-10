package com.umlet.element;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Vector;

import ar.uba.fi.taller2.tp.control.context.AppContext;

import com.baselet.element.GridElement;

public class Specialization extends Relation {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Vector<String> _strings;
	private ar.uba.fi.taller2.tp.model.Specialization generalization;
	public ar.uba.fi.taller2.tp.model.Specialization getGeneralization() {
		return generalization;
	}
	public void setGeneralization(ar.uba.fi.taller2.tp.model.Specialization generalization) {
		this.generalization = generalization;
	}
	@Override
	public String getPanelAttributes() {
		if(this.generalization!=null)
		   return this.generalization.getCardinality();
		return panelAttributes;
	}
	@Override
	public void setPanelAttributes(String state) {
		this.setStrings(null);
		super.setPanelAttributes("lt=<<<<<-");
		if(this.getId().equals(Long.valueOf(-1))){
			this.panelAttributes=state;
			return;
		}
		if(this.generalization==null){
			this.generalization=AppContext.getInstance().getProjectContext().getEntity(this.getIdPrimary()).getGeneralization(this.getId());
			this.panelAttributes=this.generalization.getCardinality();
		}
		if(!state.isEmpty()){
			if(this.generalization!=null)
			this.generalization.setCardinality(state);
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
	
	
	public Specialization() {
		super();
	}
	@Override
	public GridElement CloneFromMe() {
		Specialization c = new Specialization();
		c.setPanelAttributes(this.getPanelAttributes());
		c.setAdditionalAttributes(this.getAdditionalAttributes());

		c.setVisible(true);
		c.setBounds(this.getBounds());
		c.setHandler(this.getHandler());

		return c;
	}

}
