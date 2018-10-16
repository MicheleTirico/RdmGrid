package RdmSeedNet_2;

import java.io.IOException;
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
import dataAnalyze.analyze;
import dataAnalyze.analyzeNetwork;
import dataAnalyze.handleFolder;
import dataAnalyze.storeNetwork;
import dataAnalyze.analyze.indicator;

public class runAndAnalyze extends framework {
 
	private static int sizeGrid = 200 ;
	private static double Da = 0.2 , Db = 0.1 ;	
	private static double g = 1, alfa = 2 , Ds = .1	, r = 2 ;
	private static String  path = "D:\\ownCloud\\RdmGrid_exp" ;

	public static void main(String[] args) throws IOException {	
		
		bks = new bucketSet(1, 1, sizeGrid, sizeGrid);
		bks.initializeBukets();

		lRd = new layerRd(1, 1, sizeGrid, sizeGrid, true, typeRadius.circle);		
		lRd.initializeCostVal(1,0);	
			
		setRdType ( RdmType.mazes) ;	
		lRd.setGsParameters(f, k, Da, Db, typeDiffusion.mooreWeigthed );
		
		lMl = new layerMaxLoc(true,true, typeInit.test, typeComp.wholeGrid, morphogen.b);
		lMl.initializeLayer();
		
		lNet = new layerNet("net") ;
		Graph netGraph = lNet.getGraph();
		Graph graphLoc = lMl.getGraph() ;
		
		analyzeNetwork aN = new analyzeNetwork(false , netGraph, path, "analyzeNet", idPattern) ;		
		aN.setIndicators(new ArrayList<indicator> ( Arrays.asList(	indicator.averageDegree,
																	indicator.gammaIndex, 
																	indicator.seedCount )));

		aN.initAnalysis();
		
		storeNetwork sN = new storeNetwork(false, netGraph, path, "dsg", idPattern +"_net_") ;
		sN.initStore();
	
		staticSympleNetwork sSn = new staticSympleNetwork(netGraph);
		
		lSeed = new layerSeed(g, alfa, Ds, r , morphogen.b );

		initMultiCircle(1, 1, 50 , sizeGrid/2 ,sizeGrid/2, 2 , 4 );		
		
		lNet.setLengthEdges("length" , true );
		// setup viz netGraph
		handleVizStype netViz = new handleVizStype( netGraph ,stylesheet.manual , "seed", 1) ;
		netViz.setupIdViz(false , netGraph, 20 , "black");
		netViz.setupDefaultParam (netGraph, "black", "black", 1 , 0.5 );
		netViz.setupVizBooleanAtr(true, netGraph, "black", "red" , false , false ) ;
		netViz.setupFixScaleManual( true , netGraph, sizeGrid , 0);
		
		netGraph.display(false);	
				
		int t = 0 ;
		while ( t <= 10000 && ! lSeed.getListSeeds().isEmpty()  ) {	
			System.out.println("------------- step " +t);
			try { 
				lRd.updateLayer();
				lMl.updateLayer();
				lNet.updateLayers_05(typeVectorField.slopeDistanceRadius , 0 , true , 1 );
			
				aN.computeIndicators(t);
				sN.storeDSGStep(t);
				t++;
			}
			catch (NullPointerException e) {
				break ;
			}
		}
		
		aN.closeFileWriter();
		sN.closeStore();
		
		sSn.compute();
		
		
		System.out.println( sSn.getGraph().getNodeCount()) ;
		
		// only for viz
		for ( seed s : lSeed.getListSeeds()) 	
			s.getNode().setAttribute("seed", 1);	
	
	}

	
		
		
}