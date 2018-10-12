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
import RdmSeedNet_2.layerMaxLoc.typeComp;
import RdmSeedNet_2.layerMaxLoc.typeInit;
import RdmSeedNet_2.layerNet.typeSetupLayer;
import RdmSeedNet_2.layerRd.typeComputeMaxLocal;
import RdmSeedNet_2.layerRd.typeDiffusion;
import RdmSeedNet_2.layerRd.typeInitializationMaxLocal;

public class run extends framework {

	static double Da = 0.2 , Db = 0.1 ;	
	static double g = 1, alfa = 2 , Ds = .1	, r = 2 ;
	private static Viewer viewerNet , viewerLocMax ;
	
	public static void main(String[] args) {		
		
		isFeedBackModel(false , typeFeedbackModel.booleanCombinedImpact);
		
		bks = new bucketSet(1, 1, 600, 600);
		bks.initializeBukets();
		
		lRd = new layerRd(1, 1, 600, 600, true, typeRadius.circle);		
		lRd.initializeCostVal(1,0);	
			
		setRdType ( RdmType.pulsatingSolitions) ;	//	System.out.println(f + " " + k);
		lRd.setGsParameters(f, k, Da, Db, typeDiffusion.mooreWeigthed );
		
		double eps =  - 0.05;
		lRd.setFeedBackParameters(0.35 , 0.05 );
		lRd.setFeedBackParameters(	Da - eps, Da + eps, 
									Db + eps, Db - eps);
		
		lMl = new layerMaxLoc(true,true, typeInit.test, typeComp.wholeGrid, morphogen.b);
		lMl.initializeLayer();
		
		lNet = new layerNet("net") ;
		Graph netGraph = lNet.getGraph();
		Graph graphLoc = lMl.getGraph() ;
		
		lSeed = new layerSeed(g, alfa, Ds, r , morphogen.b );

		initMultiCircle(1, 1, 50 , 300 ,300 , 2 , 4 );		
//		initMultiCircle(1, 1, 50 , 320 ,320 , 2 , 5 );		
//		initMultiCircle(1, 1, 50 , 320 ,280  , 2 , 3 );
//		initMultiCircle(1, 1, 50 , 280  ,320 , 3 , 5 );		
//		initMultiCircle(1, 1, 50 , 280  ,280  , 1 , 5 );	

		lNet.setLengthEdges("length" , true );

		// set file Source for file step		
		viewerLocMax = graphLoc.display(false);
		viewerNet = netGraph.display(false);	
		
		// setup viz netGraph
		handleVizStype netViz = new handleVizStype( netGraph ,stylesheet.manual , "seed", 1) ;
		netViz.setupIdViz(false , netGraph, 20 , "black");
		netViz.setupDefaultParam (netGraph, "black", "black", 1 , 0.5 );
		netViz.setupVizBooleanAtr(true, netGraph, "black", "red" , false , false ) ;
		netViz.setupFixScaleManual( true , netGraph, 600 , 0);
		
		// setup viz max Loc Graph
		handleVizStype maxLocViz = new handleVizStype( graphLoc ,stylesheet.booleanAtr , "seed", 1) ;
		maxLocViz.setupDefaultParam (graphLoc, "black", "black", 6 , 0.5 );
		maxLocViz.setupVizBooleanAtr(true, graphLoc, "black", "red" , false , false ) ;
	
		
		int t = 0 ;
		while ( t < 10000 && ! lSeed.getListSeeds().isEmpty()  ) {	
			System.out.println("------------- step " +t);
			maxLocViz.setupFixScaleManual( false , graphLoc, 200 , 0);
			lRd.updateLayer();
			lMl.updateLayer();
			lNet.updateLayers_05(typeVectorField.slopeDistanceRadius , 0 , true , 1 );
		//	Thread.sleep(1);
			t++;
		}
		
	
		
		// only for viz
		for ( seed s : lSeed.getListSeeds()) 	
			s.getNode().setAttribute("seed", 1);	
	
		/*
		for ( seed s : lSeed.getListSeeds()) {
			System.out.println (s.getX() + " " + s.getY());
			cell c = lRd.getCell(s);
		//	System.out.println(c.getX() + " " + c.getY());
//			if ( c.hasSeed() )
//				System.out.println("hasSeed " + c.hasSeed());
			if ( c.hasNode() )
				System.out.println("hasNode " + c.hasNode() ) ;
		}
		
		for ( Node n : netGraph.getEachNode()) {
			double [] position = GraphPosLengthUtils.nodePosition(n);
			try {
				cell c = lRd.getCell(position);
				if ( c.hasNode() )
					System.out.println(c.getX() + " " + c.getY() + " hasNode " + c.hasNode() ) ;
			} catch (NullPointerException e) {
				// TODO: handle exception
			}
		}
	*/
	}

	
		
		
}