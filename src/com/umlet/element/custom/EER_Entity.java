package com.umlet.element.custom;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.util.HashMap;
import java.util.Vector;

import ar.uba.fi.taller2.tp.control.context.AppContext;
import ar.uba.fi.taller2.tp.model.Entity;
import ar.uba.fi.taller2.tp.model.TypeEntity;

import com.baselet.control.Constants;
import com.baselet.control.Utils;
import com.baselet.element.GridElement;


@SuppressWarnings("serial")
public class EER_Entity extends GridElement {
	int ySave = 0;
	boolean hasAttributes = false;
    private Entity entity=null;
	private Vector<String> getStringVector() {
		Vector<String> ret = Utils.decomposeStrings(this.getPanelAttributes());
		return ret;
	}
	@Override
	public void setPanelAttributes(String panelAttributes) {
		if(this.getId().equals(Long.valueOf(-1))){
			this.panelAttributes =panelAttributes;
			return;
		}
		if(entity==null){
		this.entity=AppContext.getInstance().getEntity(this.getId());
		}
		if(!panelAttributes.isEmpty()){
			this.entity.setName(panelAttributes);
			this.panelAttributes =panelAttributes;
		}
		
		
	}
	@Override
	public String getPanelAttributes() {
		if(this.entity!=null)
		   return this.entity.getName();  
		return panelAttributes;
	}
	
	public static final HashMap<TypeEntity, Color> colorMap = new HashMap<TypeEntity, Color>();
	static {
		colorMap.put(TypeEntity.MAESTRA_COSA, Color.BLUE);
		colorMap.put(TypeEntity.MAESTRA_DOMINIO, Color.CYAN);
		colorMap.put(TypeEntity.TRANSACCIONAL_HISTORICA, Color.GREEN);
		colorMap.put(TypeEntity.TRANSACCIONAL_PROGRAMADA, Color.MAGENTA);
	}
	
	
	@Override
	public void paintEntity(Graphics g) {
		fgColor=Constants.DEFAULT_FOREGROUND_COLOR;
		if(this.entity==null){
			this.entity=AppContext.getInstance().getEntity(this.getId());
		}
		if (this.entity!=null){
			bgColor=EER_Entity.colorMap.get(this.entity.getTypeEntity());
		}
		float zoom = getHandler().getZoomFactor();
		
		Graphics2D g2 = (Graphics2D) g;
		g2.setFont(this.getHandler().getFontHandler().getFont());
		Composite old = g2.getComposite();
		AlphaComposite alpha = AlphaComposite.getInstance(AlphaComposite.SRC_OVER,  Constants.ALPHA_MIDDLE_TRANSPARENCY);
		Composite composites[] = { old, alpha };
		g2.setColor(fgColor);

		Vector<String> tmp = getStringVector();
		boolean CENTER = true;
		boolean drawInnerRect = false;
		int yPos = (int) this.getHandler().getFontHandler().getDistanceBetweenTexts() * 2;

		// ### draw rectangles and lines (some duplicated code)
		Polygon poly = new Polygon();
		for (int i = 0; i < tmp.size(); i++) {
			String s = tmp.elementAt(i);
			if (s.equals("--")) {
				CENTER = false;
				ySave = yPos + (int) this.getHandler().getFontHandler().getDistanceBetweenTexts() * 2;
				yPos += (int) this.getHandler().getFontHandler().getDistanceBetweenTexts() * 3;
			}
			else {
				yPos += (int) this.getHandler().getFontHandler().getFontSize();
				if (CENTER && s.startsWith("##")) drawInnerRect = true;
				yPos += this.getHandler().getFontHandler().getDistanceBetweenTexts();
				if (CENTER) ySave = yPos;
			}
		}
		poly.addPoint(0, 0);
		poly.addPoint(this.getWidth() - 1, 0);
		if (CENTER) {
			hasAttributes = false; // see getStickingBorder()
			ySave = this.getHeight();
			poly.addPoint(this.getWidth() - 1, this.getHeight() - 1);
			poly.addPoint(0, this.getHeight() - 1);
		}
		else {
			hasAttributes = true; // see getStickingBorder()
			g.drawLine((int) (10 * zoom), ySave, (int) (10 * zoom), yPos + (int) this.getHandler().getFontHandler().getDistanceBetweenTexts() - (int) this.getHandler().getFontHandler().getDistanceBetweenTexts());
			poly.addPoint(this.getWidth() - 1, ySave);
			poly.addPoint(0, ySave);
		}

		g2.setComposite(composites[1]);
		g2.setColor(bgColor);
		g2.fillPolygon(poly);
		g2.setComposite(composites[0]);
		if (isSelected) g2.setColor(fgColor);
		else g2.setColor(fgColorBase);
		g2.drawPolygon(poly);

		if (drawInnerRect) {
			if (CENTER) g.drawRect((int) (3 * zoom), (int) (3 * zoom), this.getWidth() - (int) (7 * zoom), this.getHeight() - (int) (7 * zoom));
			else g.drawRect((int) (3 * zoom), (int) (3 * zoom), this.getWidth() - (int) (7 * zoom), ySave - (int) (6 * zoom));
		}

		// #### draw text
		CENTER = true;
		yPos = (int) this.getHandler().getFontHandler().getDistanceBetweenTexts() * 2;

		for (int i = 0; i < tmp.size(); i++) {
			String s = tmp.elementAt(i);
			if (s.equals("--")) {
				CENTER = false;
				ySave = yPos + (int) this.getHandler().getFontHandler().getDistanceBetweenTexts() * 2;
				yPos += (int) this.getHandler().getFontHandler().getDistanceBetweenTexts() * 3;
			}
			else {
				yPos += (int) this.getHandler().getFontHandler().getFontSize();
				if (CENTER) {
					String s1 = s;
					if (s.startsWith("##")) {
						drawInnerRect = true;
						s1 = s1.substring(2);
					}
					this.getHandler().getFontHandler().writeText(g2, s1, this.getWidth() / 2, yPos, true);
				}
				else {
					this.getHandler().getFontHandler().writeText(g2, s, (int) this.getHandler().getFontHandler().getFontSize(), yPos, false);
				}
				yPos += this.getHandler().getFontHandler().getDistanceBetweenTexts();
				if (CENTER) ySave = yPos;
			}
		}
	}
	
	@Override
	public GridElement CloneFromMe() {
			EER_Entity c =new EER_Entity();
			c.setBounds(this.getBounds());
			c.setHandler(this.getHandler());
			if(this.getId().equals(Long.valueOf(-1))){
			c.entity=AppContext.getInstance().getProjectContext().getNewEntity(this.getName());
			}else{
				c.entity=this.entity;
			}
			c.setPanelAttributes(this.getPanelAttributes()); // copy states
			c.setId(c.entity.getId());
			return c;
		
	}
}
