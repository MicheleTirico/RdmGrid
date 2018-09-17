package test_grid_02;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import test_grid_02.layerRd.morphogen;
import test_grid_02.layerRd.typeDiffusion;

import java.awt.geom.*;

public class runViz extends JApplet {
  public static void main(String s[]) {
    JFrame frame = new JFrame();
    frame.setTitle("gs sim");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(600,600);
    
    JApplet applet = new runViz();
    applet.init();
    frame.getContentPane().add(applet);
    frame.pack();
    frame.setVisible(true);
  }
  
  public void init() {
    JPanel panel = new LifePanel();
    getContentPane().add(panel);
  }
}

class LifePanel extends JPanel implements ActionListener{
	
	int t;

	static double  f = 0.039, k=0.058, Da = 0.2, Db = 0.1 ;	
	private static layerRd lRd = new layerRd(1.0, 0, 300, 300);
	
	  public LifePanel() {
		  setPreferredSize(new Dimension(300, 300));
		  setBackground(Color.white);
		
		  lRd.initializeCostVal(1,0);
		  lRd.setValueOfCell(1, 1, 150, 150);
		  lRd.setValueOfCell(1, 1, 151, 150);
		  lRd.setValueOfCell(1, 1, 149, 150);
		  lRd.setValueOfCell(1, 1, 150, 151);
		  lRd.setValueOfCell(1, 1, 150, 149);
		  
		  lRd.setGsParameters(f, k, Da, Db, typeDiffusion.mooreCost);
		  System.out.println(lRd.getValueMorphogen(100, 150, morphogen.a));
		  
		  Timer timer = new Timer(100, this);
		  timer.start();	  
	  }
	  
	  @Override 
	  public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D graphics2d = (Graphics2D) g;
		
	
		
		g.setColor(Color.RED);
		
		for ( cell c : lRd.getListCellActive() ) {
			if ( c.getVal2()> .1 ) {
				g.drawRect(c.getX(), c.getY(), 0, 0);

			}
		}
		
	  }
	
	
	  

	

	@Override
	public void actionPerformed(ActionEvent e) {
		
		System.out.println(t);
		t++;
		lRd.updateValues();
		repaint();
	}
	
}

