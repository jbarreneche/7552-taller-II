package com.umlet.element.custom;

// Some import to have access to more Java features
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.util.HashMap;
import java.util.Vector;

import ar.uba.fi.taller2.tp.control.context.AppContext;
import ar.uba.fi.taller2.tp.model.Relation;
import ar.uba.fi.taller2.tp.model.TypeRelation;

import com.baselet.control.Constants;
import com.baselet.control.Utils;
import com.baselet.element.GridElement;


@SuppressWarnings("serial")
public class EER_Rel_Diamond extends com.baselet.element.GridElement {

	private Relation relation=null;
	public static final HashMap<TypeRelation ,Color> colorMap = new HashMap<TypeRelation, Color>();
	static {
		colorMap.put(TypeRelation.SIMPLE, Color.WHITE);
		colorMap.put(TypeRelation.COMPUESTA, Color.LIGHT_GRAY);
	}
	
	// Change this method if you want to edit the graphical
	// representation of your custom element.
	@Override
	public void paintEntity(Graphics g) {
		fgColor=Constants.DEFAULT_FOREGROUND_COLOR;
		if(this.relation==null){
			this.relation=AppContext.getInstance().getRelation(this.getId());
		}
		if (this.relation!=null){
			bgColor=EER_Rel_Diamond.colorMap.get(this.relation.getTypeRelation());
		}
//		float zoom = getHandler().getZoomFactor();
		
		Graphics2D g2 = (Graphics2D) g;
		g2.setFont(this.getHandler().getFontHandler().getFont());
		Composite old = g2.getComposite();
		AlphaComposite alpha = AlphaComposite.getInstance(AlphaComposite.SRC_OVER,  Constants.ALPHA_MIDDLE_TRANSPARENCY);
		Composite composites[] = { old, alpha };
		// Some unimportant initialization stuff; setting color, font
		// quality, etc. You should not have to change this.
		g2.setColor(fgColor);
		

		// Finally, change other graphical attributes using
		// drawLine, getWidth, getHeight..

		// Define the elements outline using a polygon, rectangle, oval, etc.
		Polygon poly = new Polygon();
		poly.addPoint(this.getWidth() / 2, 0);
		poly.addPoint(this.getWidth() - 1, this.getHeight() / 2);
		poly.addPoint(this.getWidth() / 2, this.getHeight() - 1);
		poly.addPoint(0, this.getHeight() / 2);

		g2.setComposite(composites[1]); // set aplha composite for drawing the background color
		g2.setColor(bgColor);
		g2.fillPolygon(poly); // fill the background
		g2.setComposite(composites[0]); // reset composite settings
		if (isSelected) g2.setColor(fgColor);
		else g2.setColor(fgColorBase);

		// It's getting interesting here:
		// First, the strings you type in the element editor are read and
		// split into lines. Then, by default, they are printed out on the
		// element, aligned to the left. Change this to modify this default
		// text printing and to react to special strings (like the "--" string
		// in the UML class elements which draw a line).
		Vector<String> tmp = Utils.decomposeStrings(this.getPanelAttributes());
		int yPos = this.getHeight() / 2 - (((int) this.getHandler().getFontHandler().getDistanceBetweenTexts() + (int) this.getHandler().getFontHandler().getFontSize()) * tmp.size()) / 2;
		for (int i = 0; i < tmp.size(); i++) {
			String s = tmp.elementAt(i);
			yPos += (int) this.getHandler().getFontHandler().getFontSize();
			this.getHandler().getFontHandler().writeText(g2, s, this.getWidth() / 2, yPos, true);
			yPos += this.getHandler().getFontHandler().getDistanceBetweenTexts();
		}
		

		// Draw the elements outline
		g2.drawPolygon(poly);
	}

	// Change this method if you want to set the resize-attributes of
	// your custom element
	@Override
	public int getPossibleResizeDirections() {
		// Remove from this list the borders you don't want to be resizeable.
		return Constants.RESIZE_TOP | Constants.RESIZE_LEFT | Constants.RESIZE_BOTTOM | Constants.RESIZE_RIGHT;
	}
	@Override
	public void setPanelAttributes(String panelAttributes) {
		if(this.getId().equals(Long.valueOf(-1))){
			this.panelAttributes =panelAttributes;
			return;
		}
		if(this.relation==null){
		this.relation=AppContext.getInstance().getRelation(this.getId());
		}
		if(!panelAttributes.isEmpty()){
			this.relation.setName(panelAttributes);
			this.panelAttributes =panelAttributes;
		}
	}
	@Override
	public String getPanelAttributes() {
		if(this.relation!=null)
		   return this.relation.getName();  
		return panelAttributes;
	}
	@Override
	public GridElement CloneFromMe() {
			EER_Rel_Diamond c =new EER_Rel_Diamond();
			c.setBounds(this.getBounds());
			c.setHandler(this.getHandler());
			c.relation=AppContext.getInstance().getProjectContext().getNewRelation(this.getName());
			c.setId(c.relation.getId());
			c.setPanelAttributes(this.getPanelAttributes()); // copy states
			c.relation.setName(this.getName());
			return c;
		
	}

}
