/**
 * 
 */
package com.umlet.element;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Vector;

import com.baselet.element.GridElement;

/**
 * @author oscar
 *
 */
public class Identifiers extends Relation {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2441474102245596715L;
	Vector<String> _strings;
	@Override
	public void setPanelAttributes(String state) {
		this.setStrings(null);
		super.setPanelAttributes("lt=<(*)(*)>");
		this.getStrings().add(state);
		this.panelAttributes=state;
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
		if (this.getStrings() != null) {
			if (this.getStrings().size() > 0) {
				Point start = this.getCenterOfLine();
				int yPos = start.y - (int) this.getHandler().getFontHandler().getDistanceBetweenTexts(); // B. Buckl
				// added
				// -this.getHandler().getDistTextToText()
				int xPos = start.x;
				for (int i = 0; i < getStrings().size(); i++) {
					String s = this.getStrings().elementAt(i);
					this.getHandler().getFontHandler().writeText(g2, s, xPos, yPos, true);
					yPos += (int) this.getHandler().getFontHandler().getFontSize();
					yPos += this.getHandler().getFontHandler().getDistanceBetweenTexts();
				}
			}
		}
	
	}
	
	public Identifiers() {
		super();
	}
	@Override
	public GridElement CloneFromMe() {
		Identifiers c = new Identifiers();
         System.out.println(this.getPanelAttributes());
		c.setPanelAttributes(this.getPanelAttributes());
		c.setAdditionalAttributes(this.getAdditionalAttributes());

		c.setVisible(true);
		c.setBounds(this.getBounds());
		c.setHandler(this.getHandler());

		return c;
	}

}
