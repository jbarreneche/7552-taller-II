package com.baselet.gui.actionlistener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.baselet.control.Main;

public class RelacionActionListener implements ActionListener{
	private Main main;
	public RelacionActionListener(Main main) {
		this.main = main;
	}
	
	public class FrameRunnable implements Runnable
	{
		RelacionFrame frame;
	    public void run()
	    {
	        frame = new RelacionFrame(); 
	        frame.setVisible(true);
	    }
	    
	    public String getName(){
	    	return frame.getText();
	    }
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		FrameRunnable ventana = new FrameRunnable();
		new Thread(ventana).start();
	}

}
