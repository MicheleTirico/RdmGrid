package RdmSeedNet_2;

import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.ui.graphicGraph.GraphPosLengthUtils;
import org.graphstream.ui.view.Viewer;

import RdmGsaNetAlgo.graphGenerator;
import RdmGsaNetViz.handleVizStype;
import RdmGsaNetViz.handleVizStype.stylesheet;
import RdmSeedNet_2.framework.RdmType;
import RdmSeedNet_2.framework.morphogen;
import RdmSeedNet_2.framework.typeVectorField;
import RdmSeedNet_2.layerNet.typeSetupLayer;
import RdmSeedNet_2.layerRd.typeDiffusion;
import RdmSeedNet_2.layerRd.typeInitializationMaxLocal;

public class run extends framework {

	static double Da = 0.15, Db = 0.15 ;	
	static double g = 1, alfa = 2 , Ds = .01	, r = 1 ;
	private static Viewer viewer ;
	
	public static void main(String[] args) {		
		
		isFeedBackModel(true);
		
		bks = new bucketSet(1, 1, 200, 200);
		bks.initializeBukets();
		
		lRd = new layerRd(1, 1, 200, 200, true);		
		lRd.initializeCostVal(1,0);	
		lRd.setInitMaxLocal(typeInitializationMaxLocal.singlePoint , morphogen.b, false);
			
		setRdType ( RdmType.movingSpots ) ;	//	System.out.println(f + " " + k);
		lRd.setGsParameters(f, k, Da, Db, typeDiffusion.mooreWeigthed );
		lRd.setFeedBackParameters(0.3, 0.1);
		
		lNet = new layerNet("net") ;
		Graph netGraph = lNet.getGraph();
	
		lSeed = new layerSeed(g, alfa, Ds, r , morphogen.b );

		initMultiCircle(1, 1, 40 , 100 ,100 , 2 , 4 );
		lNet.setLengthEdges("length" , true );

		// set file Source for file step
		viewer = netGraph.display(false);	

		// setup viz netGraph
		handleVizStype netViz = new handleVizStype( netGraph ,stylesheet.manual , "seed", 1) ;
		netViz.setupIdViz(false , netGraph, 20 , "black");
		netViz.setupDefaultParam (netGraph, "black", "black", 1 , 0.5 );
		netViz.setupVizBooleanAtr(true, netGraph, "black", "red" , false , false ) ;
		netViz.setupFixScaleManual( false , netGraph, 200 , 0);
	
		// only for viz
		for ( seed s : lSeed.getListSeeds()) 	
			s.getNode().setAttribute("seed", 1);	
		
		int t = 0 ;
		while ( t < 5000 && ! lSeed.getListSeeds().isEmpty()  ) {	
			System.out.println("------------- step " +t);
			lRd.updateLayer();
			lRd.computeMaxLocal();
			lNet.updateLayers_04(typeVectorField.slopeDistanceRadius , 4 , false , 1 );
		//	Thread.sleep(1);
			t++;
	
		}
	}
	

	
		
		
}