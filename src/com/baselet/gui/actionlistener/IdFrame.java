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

import com.baselet.control.Main;

public class IdFrame extends JFrame implements ActionListener{

	/* Instanciarla de este modo
    IdFrame frame = new IdFrame(); 
    frame.setVisible(true);
    */
    
	private static final long serialVersionUID = 1L;
	private JButton aceptarButton, removeButton,addAtributoButton,addEntidadButton;
	private DefaultListModel model;
	private JList list;
	private ArrayList<Long> ids_componentes;
	private JLabel label1, label2, label3;
	private JComboBox addAtributoCombo,addEntidadCombo;
	private JScrollPane pane;
	
	private List<Attribute> attrs;
	private List<Entity> entidades;

	public IdFrame() {

		setTitle("Indentificador Compuesto");
		setLayout(null);
		setBounds(200, 200, 400,400);

		label1 	= new JLabel("Componentes");
		label2 	= new JLabel("+ Atributo Propio");
		label3 	= new JLabel("+ Entidad");
		model 	= new DefaultListModel();
		list 			= new JList(model);
		pane 		= new JScrollPane(list);
		ids_componentes	= new ArrayList<Long>();
		addAtributoButton 	= new JButton("Agregar");
		addEntidadButton 	= new JButton("Agregar");
		aceptarButton		 	= new JButton("Aceptar");
		addAtributoCombo 	= new JComboBox();
		addEntidadCombo 	= new JComboBox();
		removeButton 			= new JButton("- Borrar Componente");
		
		/*Lista de Atributos de la Entidad Seleccionada*/
		Long id = Main.getInstance().getDiagramHandler().getDrawPanel().getSelector().getSelectedEntities().get(0).getId();
		Entity entity  = AppContext.getInstance().getProjectContext().getEntity(id);
		attrs = entity.getAttributes();
				
		for (int i=0; i<attrs.size(); i++)
		{
				addAtributoCombo.addItem(attrs.get(i).getName());
		}
		
		/*Lista de Entidades en el Proyecto*/
		entidades = AppContext.getInstance().getProjectContext().getEntities();
		for (int i=0; i<entidades.size(); i++)
			addEntidadCombo.addItem(entidades.get(i).getName());

		/*Agrega a la lista una Entidad*/
		addEntidadButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.addElement(addEntidadCombo.getSelectedItem().toString());
				ids_componentes.add( entidades.get(addEntidadCombo.getSelectedIndex()).getId() );
			}
		});
		
		/*Agrega a la lista un Atributo*/
		addAtributoButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.addElement(addAtributoCombo.getSelectedItem().toString());
				Long id_agregar = attrs.get(addAtributoCombo.getSelectedIndex()).getId();
				ids_componentes.add( id_agregar );
			}
		});
		
		/*Saca de la lista*/
		removeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int index = list.getSelectedIndex();
				if (model.getSize() > 0 && index>-1)
				{
					model.removeElementAt(index);
					ids_componentes.remove(index);
				}
			}
		});

		aceptarButton.addActionListener(this);

		/*Posiciones*/
		pane.setBounds(10, 50, 380, 150);
		addAtributoButton.setBounds(300, 210, 80, 30);
		addEntidadButton.setBounds(300, 250, 80, 30);
		addAtributoCombo.setBounds(140, 210, 150, 30);
		addEntidadCombo.setBounds(140, 250, 150, 30);
		removeButton.setBounds(10, 290, 380, 30);
		aceptarButton.setBounds(10, 330, 380, 30);
		label1.setBounds(10,10,120,30);
		label2.setBounds(10, 210, 130, 30);
		label3.setBounds( 10, 250, 130, 30);
		add(pane);
		add(addAtributoButton);
		add(addEntidadButton);
		add(addAtributoCombo);
		add(addEntidadCombo);
		add(removeButton);
		add(label1);
		add(label2);
		add(label3);
		add(aceptarButton);
	}

	public void actionPerformed(ActionEvent e) {

		if (e.getSource()==aceptarButton) {
			this.dispose();
		}
	}
	
	
	public void setAceptarButtonActionListener(ActionListener listener){
		this.aceptarButton.addActionListener(listener);
	}

	public ArrayList<Long> getIds_componentes() {
		return ids_componentes;
	}

	public void setIds_componentes(ArrayList<Long> ids_componentes) {
		this.ids_componentes = ids_componentes;
	}
	
	

}