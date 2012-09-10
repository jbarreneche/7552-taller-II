package com.baselet.gui.actionlistener;

import java.awt.Color;
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
import javax.swing.JTextField;

import ar.uba.fi.taller2.tp.control.context.AppContext;
import ar.uba.fi.taller2.tp.model.Attribute;
import ar.uba.fi.taller2.tp.model.Entity;

import com.baselet.control.Main;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.BaseFont;

public class AttributeFrame extends JFrame implements ActionListener{
	private static final long serialVersionUID = 1L;
	private JTextField textfield1, textfield2, textfield3;
	private JLabel label1, label2, label3, label4, label5, label6;
	private JButton aceptar;

	public AttributeFrame() {
			setTitle("Atributo");
			setLayout(null);
			setBounds(200, 200, 250, 160);
			label1=new JLabel("Nombre:");
			label1.setBounds(10,10,70,30);
			add(label1);
			textfield1=new JTextField();
			textfield1.setBounds(70,10,170,30);
			add(textfield1);
			label2 = new JLabel("Cardinalidad:");
			label2.setBounds(10,50,100,30);
			add(label2);
			label3 = new JLabel("(");
			label3.setBounds(100,50,30,30);
			add(label3);
			textfield2=new JTextField();
			textfield2.setBounds(110,50,30,30);
			textfield2.setText("1");
			add(textfield2);
			label4 = new JLabel(",");
			label4.setBounds(140,50,30,30);
			add(label4);
			textfield3=new JTextField();
			textfield3.setBounds(150,50,30,30);
			textfield3.setText("1");
			add(textfield3);
			label5 = new JLabel(")");
			label5.setBounds(180,50,30,30);
			add(label5);
			aceptar=new JButton("Aceptar");
			aceptar.setBounds(75, 90, 100, 30);
			aceptar.addActionListener(this);
			add(aceptar);
			
			label6 = new JLabel("");
			label6.setBounds(70,120,200,30);
			add(label6);
	}

	public AttributeFrame(String nombre, String card) {
        if (card==null || !card.contains(","))
        	card = "(1,1)";
		setTitle("Atributo");
		setLayout(null);
		setBounds(200, 200, 250, 160);
		label1=new JLabel("Nombre:");
		label1.setBounds(10,10,70,30);
		add(label1);
		textfield1=new JTextField();
		textfield1.setBounds(70,10,170,30);
		textfield1.setText(nombre);
		add(textfield1);
		label2 = new JLabel("Cardinalidad:");
		label2.setBounds(10,50,100,30);
		add(label2);
		label3 = new JLabel("(");
		label3.setBounds(100,50,30,30);
		add(label3);
		textfield2=new JTextField();
		textfield2.setBounds(110,50,30,30);
		textfield2.setText(getXCard(card));
		add(textfield2);
		label4 = new JLabel(",");
		label4.setBounds(140,50,30,30);
		add(label4);
		textfield3=new JTextField();
		textfield3.setBounds(150,50,30,30);
		textfield3.setText(getYCard(card));
		add(textfield3);
		label5 = new JLabel(")");
		label5.setBounds(180,50,30,30);
		add(label5);
		aceptar=new JButton("Aceptar");
		aceptar.setBounds(75, 90, 100, 30);
		aceptar.addActionListener(this);
		add(aceptar);
		
		label6 = new JLabel("");
		label6.setBounds(70,120,200,30);
		add(label6);
	}

	public String getNombre(){
		return textfield1.getText();
	}

	public String getCardinalidad(){
		String card = "(" + textfield2.getText() + "," + textfield3.getText() + ")";
		return card;
	}

	public void actionPerformed(ActionEvent e) {

		if (e.getSource()==aceptar) {
			if ((isInteger(textfield2.getText() ) || textfield2.getText().contentEquals("n")|| textfield2.getText().contentEquals("N") || textfield2.getText().contentEquals("*") ) 
					&& ( isInteger(textfield3.getText()) || textfield3.getText().contentEquals("n") || textfield3.getText().contentEquals("N") || textfield3.getText().contentEquals("*")  ))
				this.dispose();
			else
			{
				this.label6.setText("Error en los datos");
				this.label6.setForeground(Color.RED);
				this.validate();
			}
		}
	}


	public void setAceptarButtonActionListener(ActionListener listener){
		this.aceptar.addActionListener(listener);
	}
	
	public boolean isInteger( String input )  
	{  
	   try  
	   {  
	      int i = Integer.parseInt( input );  
	      if (i>=0)
	    	  return true;
	      else return false;
	   }  
	   catch(Exception e)
	   {  
	      return false;  
	   }  
	}  
	
	public String getXCard(String cardinalidad){
		String[] sep = cardinalidad.split(",");
		return sep[0].replace("(", "" );
	}
	
	public String getYCard(String cardinalidad){
		String[] sep = cardinalidad.split(",");
		return sep[1].replace(")", "" );
	}
}
