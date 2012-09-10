package com.baselet.gui.standalone;

import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import com.baselet.control.Path;

public class CustomIconRenderer extends DefaultTreeCellRenderer {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ImageIcon entityIcon;
    ImageIcon relationIcon;
    ImageIcon diagramIcon;
    ImageIcon folderIcon;

    public CustomIconRenderer() {
	  	entityIcon =  new ImageIcon(Path.homeProgram() + "img/"+"entidad icon.gif");
	  	relationIcon =  new ImageIcon(Path.homeProgram() + "img/"+"relacion_icon.gif");
	  	diagramIcon =  new ImageIcon(Path.homeProgram() + "img/"+"diagram.png");
		folderIcon =  new ImageIcon(Path.homeProgram() + "img/"+"Folder.png");
    }

    public Component getTreeCellRendererComponent(JTree tree,Object value,boolean sel,boolean expanded,boolean leaf,
                                                    int row,boolean hasFocus) {

        super.getTreeCellRendererComponent(tree, value, sel,expanded, leaf, row, hasFocus);

        Object nodeObj = ((DefaultMutableTreeNode)value).getUserObject();
        if(nodeObj==null)
        	return this;
        if (nodeObj.getClass().getSimpleName().equals("Relation")){
            setIcon(relationIcon);
        } else if (nodeObj.getClass().getSimpleName().equals("Entity")){
            setIcon(entityIcon);
        }else if (nodeObj.getClass().getSimpleName().equals("DiagramTree")){
            setIcon(diagramIcon);
        }
        else{
        	setIcon(folderIcon);
        }
       
        return this;
    }
}
