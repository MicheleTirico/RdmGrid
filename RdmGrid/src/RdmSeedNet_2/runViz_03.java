package RdmSeedNet_2;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import RdmSeedNet_2.framework.morphogen;
import RdmSeedNet_2.framework.typeRadius;
import RdmSeedNet_2.layerRd.typeDiffusion;


public class runViz_03 extends JApplet{
	public static void main(String s[]) {
	    JFrame frame = new JFrame();
	    frame.setTitle("gs sim");
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.setSize(600,600);
	    
	    JApplet applet = new runViz_03();
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

	static double  f = 0.014, k=0.054, Da = 0.2, Db = 0.1 ;	
	private static layerRd lRd = new layerRd(1.0, 0, 300, 300,true, typeRadius.circle);
	
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
		  System.out.println(lRd.getValueMorphogen(150, 150, morphogen.a));
		  
		  Timer timer = new Timer(10, this);
		  timer.start();	  
	  }
	  
	  @Override 
	  public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D graphics2d = (Graphics2D) g;
		
		for ( cell c : lRd.getListCell() ) {
		g.setColor(Color.RED);
			if ( c.getVal2()>0.15  ) {
				g.drawRect(c.getX(), c.getY(), 0, 0);
			}
		}
			
	  }
	
	
	  

	

	@Override
	public void actionPerformed(ActionEvent e) {
		
		System.out.println(t);
//		System.out.println(lRd.getValueMorphogen(150, 150, morphogen.b));
		t++;
		lRd.updateLayer();
		repaint();
	}
	/*
	// set RD start values to use in similtion ( gsAlgo )
				protected static  void setRdType ( RdmType type ) {
					
					switch ( type ) {
						case holes: 				{ f = 0.039 ; k = 0.058 ; } 
													break ;
						case solitions :			{ f = 0.030 ; k = 0.062 ; } 
													break ; 
						case mazes : 				{ f = 0.029 ; k = 0.057 ; } 
													break ;
						case movingSpots :			{ f = 0.014 ; k = 0.054 ; } 
													break ;
						case pulsatingSolitions :	{ f = 0.025 ; k = 0.060 ; } 
													break ;
						case U_SkateWorld :			{ f = 0.062 ; k = 0.061 ; } 
													break ;
						case f055_k062 :			{ f = 0.055 ; k = 0.062 ; } 
													break ;
						case chaos :				{ f = 0.026 ; k = 0.051 ; } 
													break ;
						case spotsAndLoops :		{ f = 0.018 ; k = 0.051 ; } 
													break ;
						case worms :				{ f = 0.078 ; k = 0.061 ; } 
													break ;
						case waves :				{ f = 0.014 ; k = 0.045 ; } 
													break ;		
					}
				}
		
	*/
}

