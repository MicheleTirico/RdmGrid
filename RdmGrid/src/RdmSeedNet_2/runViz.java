package RdmSeedNet_2;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import RdmSeedNet_2.framework.morphogen;
import RdmSeedNet_2.layerRd.typeDiffusion;

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
	static double  f = 0.030, k=0.062, Da = 0.2, Db = 0.1 ;	
	static double g = 1, alfa = 2 , Ds = 1	, r = 5;
	
	private static layerRd lRd = new layerRd(1.0, 0, 150, 150, true , ty);
	protected static layerSeed lSeed = new layerSeed();

	protected static layerNet lNet = new layerNet() ;
	protected static bucketSet bks = new bucketSet() ;
	
	  public LifePanel() {
		setPreferredSize(new Dimension(150, 150));
	  	setBackground(Color.white);
		
	  	bks = new bucketSet(1, 1, 150, 150);
		bks.initializeBukets();	
		lRd.initializeCostVal(1,0);	
		lRd.setValueOfCellAround(1, 1, 75, 75, 1);		//		lRd.setValueOfCell(1, 1, 25, 25)
		lRd.setGsParameters(f, k, Da, Db, typeDiffusion.mooreWeigthed);
		
		lNet = new layerNet() ;
		lSeed = new layerSeed(g, alfa, Ds, r , morphogen.b );
	//	lSeed.initializationSeedCircle(20, 2);
			
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
	
		lRd.updateLayer();
		//	lSeed.updateLayer();
		//	lNet.updateLayer();
			lNet.updateLayerAndSeeds();
		repaint();
		t++;
	}
	
}

