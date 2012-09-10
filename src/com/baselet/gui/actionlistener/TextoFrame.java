package com.baselet.gui.actionlistener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import ar.uba.fi.taller2.tp.control.context.AppContext;
import ar.uba.fi.taller2.tp.model.Relation;
import ar.uba.fi.taller2.tp.model.TypeRelation;

import com.baselet.control.Main;
import com.baselet.element.GridElement;
import com.baselet.gui.listener.GridElementListener;

public class TextoFrame extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	private JTextArea label1;
	private JScrollPane pane;
	
	public TextoFrame( String texto) {
		setTitle("Resultado");
		setLayout(null);
        setBounds(200, 200, 420, 500);
		label1=new JTextArea();
		label1.setBounds(10,10,400,450);
		label1.setText(texto);
		label1.setEditable(false);
		pane = new JScrollPane(label1);
		pane.setBounds(10,10,400,450);
		add(pane);
	}

	public void actionPerformed(ActionEvent e) {

	}
}
