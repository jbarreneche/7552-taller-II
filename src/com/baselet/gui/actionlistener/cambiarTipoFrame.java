package com.baselet.gui.actionlistener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;

import ar.uba.fi.taller2.tp.control.context.AppContext;
import ar.uba.fi.taller2.tp.model.Attribute;
import ar.uba.fi.taller2.tp.model.Entity;
import ar.uba.fi.taller2.tp.model.Identifiers;
import ar.uba.fi.taller2.tp.model.TypeEntity;

import com.baselet.control.Main;
import com.baselet.element.GridElement;
import com.baselet.gui.MenuFactorySwing;

public class cambiarTipoFrame extends JFrame implements ActionListener{

	/* Instanciarla de este modo
    IdFrame frame = new IdFrame(); 
    frame.setVisible(true);
    */
	
	private JComboBox tiposCB;
	private TypeEntity tipoNuevo;
    
	private static final long serialVersionUID = 1L;
	private JButton aceptarButton;
	private Entity miEntidad = null;
	private GridElement miGridElement;
	
	
	private List<Attribute> attrs;
	private List<Entity> entidades;

	public cambiarTipoFrame() {

		setTitle("Cambiar tipo de entidad");
		setLayout(null);
		setBounds(200, 200, 240, 110);
		
		/* Obtengo la entidad actual*/
		miGridElement =  Main.getInstance().getDiagramHandler().getDrawPanel().getSelector().getSelectedEntities().get(0);
		Long id = miGridElement.getId();
		miEntidad  = AppContext.getInstance().getProjectContext().getEntity(id);
		
		tiposCB = new JComboBox();
		tiposCB.setBounds(20,20,200,30);
		tiposCB.addItem("Maestra Cosa");
		tiposCB.addItem("Maestra Dominio"); 
		tiposCB.addItem("Transaccional Historica"); 
		tiposCB.addItem("Transaccional Programada");
		add(tiposCB);

		aceptarButton		 	= new JButton("Aceptar");
		aceptarButton.addActionListener(this);
		aceptarButton.setBounds(20, 60, 200, 30);
		add(aceptarButton);
	}

	public void actionPerformed(ActionEvent e) {

		if (e.getSource()==aceptarButton) {
			String color = "blue";
			TypeEntity tipo = TypeEntity.MAESTRA_COSA;
			if (tiposCB.getSelectedIndex() == 2){
				tipo = TypeEntity.TRANSACCIONAL_HISTORICA;
				color = "green";
			}else if (tiposCB.getSelectedIndex() == 1)	{
				tipo = TypeEntity.MAESTRA_DOMINIO;
				color = "cyan";
			}else if (tiposCB.getSelectedIndex() == 3)	{
				tipo = TypeEntity.TRANSACCIONAL_PROGRAMADA;
				color = "magenta";
			}
			
			tipoNuevo = tipo;
			miEntidad.setTypeEntity(tipo);
			//miGridElement.setColor(color,false);
			MenuFactorySwing.getInstance().updateDiagramEntities();
			this.dispose();
		}
	}
	
	public TypeEntity getNuevoTipo() {
		return tipoNuevo;
	}
	
	public void setAceptarButtonActionListener(ActionListener listener){
		this.aceptarButton.addActionListener(listener);
	}

}