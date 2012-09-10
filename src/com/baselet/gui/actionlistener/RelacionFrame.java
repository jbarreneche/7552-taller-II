package com.baselet.gui.actionlistener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import ar.uba.fi.taller2.tp.control.context.AppContext;
import ar.uba.fi.taller2.tp.model.Relation;
import ar.uba.fi.taller2.tp.model.TypeRelation;

import com.baselet.control.Main;
import com.baselet.element.GridElement;
import com.baselet.gui.MenuFactorySwing;
import com.baselet.gui.listener.GridElementListener;
public class RelacionFrame extends JFrame implements ActionListener{

	private static final long serialVersionUID = 1L;
	private JTextField textfield1;
	private JLabel label1, label2;
	private JButton boton1;
	private JComboBox combo1;
	
	public RelacionFrame() {
		setTitle("Nueva Relacion");
		setLayout(null);
        setBounds(200, 200, 250, 150);
		label1=new JLabel("Nombre:");
		label1.setBounds(10,10,70,30);
		add(label1);
		textfield1=new JTextField();
		textfield1.setBounds(70,10,170,30);
		add(textfield1);
		 label2 = new JLabel("Tipo:");
        label2.setBounds(10,50,50,30);
        add(label2);
		combo1=new JComboBox();
        combo1.setBounds(70,50,170,30);
        combo1.addItem("Simple");
        combo1.addItem("Compuesta"); 
        add(combo1);
		boton1=new JButton("Aceptar");
		boton1.setBounds(75, 90, 100, 30);
		boton1.addActionListener(this);
		add(boton1);
	}

	public String getText(){
		return textfield1.getText();
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource()==boton1 && textfield1.getText().length()!=0 ) {
			String color = "white";
			TypeRelation tipo = TypeRelation.SIMPLE;
			if (combo1.getSelectedIndex() == 1){
				tipo = TypeRelation.COMPUESTA;
				color = "light_gray";
			}

			//AppContext.getInstance().setRelacionRelation(this.getText(), tipo);
			java.awt.Component[] comp = Main.getInstance().getPalettes().get("EER.xml")
					.getDrawPanel().getComponents();
			GridElement e1 = (GridElement) comp[3];
			GridElementListener grid = Main.getInstance().getPalette().getEntityListener(e1);
			e1.addMouseListener(grid);
			e1.setHandler(Main.getInstance().getCurrentInfoDiagramHandler());
			e1.setName(textfield1.getText());
//			e1.setColor(color, false);
			Long id = grid.mouseDoubleClicked(e1);
			Relation relacion  = AppContext.getInstance().getProjectContext().getRelation(id);
			relacion.setTypeRelation(tipo);
			this.dispose();
		}
	}

}