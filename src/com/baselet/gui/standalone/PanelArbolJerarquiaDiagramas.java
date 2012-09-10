package com.baselet.gui.standalone;

import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.Iterator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import ar.uba.fi.taller2.tp.control.context.AppContext;
import ar.uba.fi.taller2.tp.model.DiagramTree;
import ar.uba.fi.taller2.tp.model.Entity;
import ar.uba.fi.taller2.tp.model.Relation;

import com.baselet.control.Main;

public class PanelArbolJerarquiaDiagramas 	extends JPanel 		/*implements TreeSelectionListener*/{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTree miArbol;
	private DefaultMutableTreeNode raiz=null;
	
	public PanelArbolJerarquiaDiagramas() {
		super(new GridLayout(1,0));
		crearArbol();
	}
    
     public void crearArbol(){
    	 if(AppContext.getInstance().getCountNewProject()>1){
    		 this.remove(0);
    	 }
    	
    	 raiz = new DefaultMutableTreeNode( "Principal" );
         rellenarNodos();
         miArbol = new JTree(raiz);
         miArbol.setBorder(BorderFactory.createEtchedBorder());
         miArbol.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
         CustomIconRenderer iconRenderer= new CustomIconRenderer();
         miArbol.setCellRenderer(iconRenderer);
         JScrollPane treeView = new JScrollPane(miArbol);
         miArbol.setShowsRootHandles(true);
         miArbol.putClientProperty("JTree.lineStyle", "Horizontal");
         if(AppContext.getInstance().getCountNewProject()!=1){
        	 add(treeView);
    	 }
        
         
         MouseListener ml = new MouseAdapter() {
             public void mousePressed(MouseEvent e) {
                 int selRow = miArbol.getRowForLocation(e.getX(), e.getY());
                 TreePath selPath = miArbol.getPathForLocation(e.getX(), e.getY());
                 if(selRow != -1) {
                     if(e.getClickCount() == 2) {
                     	 DefaultMutableTreeNode selectedNode =(DefaultMutableTreeNode) selPath.getLastPathComponent();
             		    if (selectedNode.getUserObject().getClass().getSimpleName().equals("DiagramTree")){
         		    	DiagramTree diagramTree=(DiagramTree)selectedNode.getUserObject();
         		    	String filename=AppContext.getInstance().getProjectEntity().getParentDir()+File.separator+diagramTree.getNombre()+"_rep.xml";
         		    	System.out.println(filename);
         		    	Main.getInstance().doOpen(filename);
         		       }

                     }
                 }
             }
         };
         miArbol.addMouseListener(ml);
     }
	
	public void agregarDiagramaAlArbol( DiagramTree diagr ) {
		
		DefaultMutableTreeNode diagrama= null;
        DefaultMutableTreeNode elemento = null;
        
        diagrama = new DefaultMutableTreeNode( diagr );
        raiz.add(diagrama);
        
        if ( diagr.getEntities() != null ) {
            Iterator<Entity> iter = diagr.getEntities().iterator();
            while( iter.hasNext() ) {
                elemento = new DefaultMutableTreeNode( iter.next() );
                diagrama.add(elemento);
            }
        }
        if ( diagr.getRelations() != null ) {
            Iterator<Relation> iter2 = diagr.getRelations().iterator();
            while( iter2.hasNext() ) {
                elemento = new DefaultMutableTreeNode( iter2.next() );
                diagrama.add(elemento);
            }
        }

        
	}
	
	private void rellenarNodos() {
        List<DiagramTree> list= AppContext.getInstance().getProjectEntity().getDiagramas();
        
        if ( list == null ) {
        	// el proyecto no tiene diagramas..
        } else {
            Iterator<DiagramTree> iter = list.iterator();
            
            while( iter.hasNext() ) {
            	agregarDiagramaAlArbol( iter.next() );
            }
        }

	}
	
}
