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
import dataAnalyze.analyze.indicator;

public class runAndAnalyze extends framework {

	static double Da = 0.2 , Db = 0.1 ;	
	static double g = 1, alfa = 2 , Ds = .1	, r = 2 ;
	
	public static void main(String[] args) throws IOException {	
		
		bks = new bucketSet(1, 1, 200, 200);
		bks.initializeBukets();

		lRd = new layerRd(1, 1, 200, 200, true, typeRadius.circle);		
		lRd.initializeCostVal(1,0);	
			
		setRdType ( RdmType.solitions) ;	
		lRd.setGsParameters(f, k, Da, Db, typeDiffusion.mooreWeigthed );
		
		lMl = new layerMaxLoc(true,true, typeInit.test, typeComp.wholeGrid, morphogen.b);
		lMl.initializeLayer();
		
		lNet = new layerNet("net") ;
		Graph netGraph = lNet.getGraph();
		Graph graphLoc = lMl.getGraph() ;
		
		String path = "D:\\ownCloud\\RdmGrid_exp" ;
		analyzeNetwork aN = new analyzeNetwork(netGraph, path, "analyzeNet", "netIndicators") ;		
		aN.setIndicators(new ArrayList<indicator> ( Arrays.asList(indicator.averageDegree,indicator.gammaIndex, indicator.seedCount)));
		
		lSeed = new layerSeed(g, alfa, Ds, r , morphogen.b );

		initMultiCircle(1, 1, 50 , 100 ,100 , 2 , 4 );		
		
		lNet.setLengthEdges("length" , true );
			
		int t = 0 ;
		while ( t < 5 && ! lSeed.getListSeeds().isEmpty()  ) {	
			System.out.println("------------- step " +t);
			lRd.updateLayer();
			lMl.updateLayer();
			lNet.updateLayers_05(typeVectorField.slopeDistanceRadius , 0 , true , 1 );

//			aN.computeIndicators();
			aN.computeSeedCount(t);
			
		
			t++;
		}
		
		aN.closeFileWriter();
		
		// only for viz
		for ( seed s : lSeed.getListSeeds()) 	
			s.getNode().setAttribute("seed", 1);	
	
	}

	
		
		
}