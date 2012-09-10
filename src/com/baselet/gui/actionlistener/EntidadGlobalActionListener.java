package com.baselet.gui.actionlistener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import ar.uba.fi.taller2.tp.control.context.AppContext;
import ar.uba.fi.taller2.tp.model.Entity;

import com.baselet.control.ClipBoard;
import com.baselet.control.Main;
import com.baselet.control.Path;
import com.baselet.diagram.DiagramHandler;
import com.baselet.diagram.command.Paste;
import com.baselet.element.GridElement;
import com.baselet.gui.actionlistener.EntidadActionListener.FrameRunnable;
import com.baselet.gui.standalone.CustomIconRenderer;

public class EntidadGlobalActionListener implements ActionListener{
	private Main main;
	public EntidadGlobalActionListener(Main main) {
		this.main = main;
	}
	public class FrameRunnable implements Runnable
	{
		EntidadGlobalFrame frame;
	    public void run()
	    {
	        frame = new EntidadGlobalFrame(); 
	        frame.setVisible(true);
	    }
	    
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		FrameRunnable ventana = new FrameRunnable();
		new Thread(ventana).start();
		 
	}
	

}
