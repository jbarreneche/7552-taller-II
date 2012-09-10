package com.baselet.gui.actionlistener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import ar.uba.fi.taller2.tp.control.context.AppContext;
import ar.uba.fi.taller2.tp.model.Attribute;
import ar.uba.fi.taller2.tp.model.Entity;
import ar.uba.fi.taller2.tp.model.Identifiers;

import com.baselet.control.ClipBoard;
import com.baselet.control.Main;
import com.baselet.control.Path;
import com.baselet.diagram.DiagramHandler;
import com.baselet.diagram.command.Paste;
import com.baselet.element.GridElement;
import com.baselet.gui.standalone.CustomIconRenderer;

public class EntidadGlobalFrame  extends JFrame implements ActionListener {

	/* Instanciarla de este modo
    IdFrame frame = new IdFrame(); 
    frame.setVisible(true);
    */
    
	private static final long serialVersionUID = 1L;
	private JButton aceptarButton;
	private ArrayList<Long> ids_componentes;
	private JLabel label1;
	private JScrollPane pane;
	private List<Entity> entidades;
	private  JTree arbol;
	private ImageIcon leafIcon;

	public EntidadGlobalFrame() {

		entidades = AppContext.getInstance().getProjectContext().getEntities();
		
		DefaultMutableTreeNode raiz = new DefaultMutableTreeNode(
				"Entidades");
		Iterator<Entity> iter = entidades.iterator();
		String buttonPath = Path.homeProgram() + "img/"
				+ "entidad icon.gif";
		leafIcon = new ImageIcon(buttonPath);
		while (iter.hasNext()) {
			DefaultMutableTreeNode elemento = new DefaultMutableTreeNode(
					iter.next());
			raiz.add(elemento);
		}

		arbol = new JTree(raiz);
        CustomIconRenderer renderer = new CustomIconRenderer();
        renderer.setLeafIcon(leafIcon);
        arbol.setCellRenderer(renderer);
        
		setTitle("Lista Entidades");
		setLayout(null);
		setBounds(200, 200, 400,250);

		label1 	= new JLabel("Elija una entidad de las existentes:");
		pane 		= new JScrollPane(arbol);
		
		ids_componentes	= new ArrayList<Long>();
		aceptarButton		 	= new JButton("Aceptar");

		aceptarButton.addActionListener(this);

		/*Posiciones*/
		pane.setBounds(10, 50, 380, 150);
		aceptarButton.setBounds(10, 200, 380, 30);
		label1.setBounds(10,10,250,30);
		add(pane);
		add(label1);
		add(aceptarButton);
	}

	public void actionPerformed(ActionEvent e) {

		if (e.getSource()==aceptarButton) {
			
			int selRow = arbol.getSelectionRows()[0];
			TreePath selPath = arbol.getPathForRow(selRow);
			if (selRow != -1) {
					DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) selPath
							.getLastPathComponent();
					if (selectedNode.getUserObject().getClass().getSimpleName().equals("Entity")) {
						Entity miEntidad = (Entity) selectedNode.getUserObject();
						Vector<GridElement> elements = AppContext.getInstance().getProjectEntity().getEER_Entity(miEntidad.getId());

						DiagramHandler diagramHandler =  Main.getInstance().getGUI().getCurrentDiagram().getHandler();
												
						ClipBoard.getInstance().copy(elements, diagramHandler);
						diagramHandler.getController().executeCommand(new Paste());
				}
			}
			this.dispose();
		}
	}
	
}
