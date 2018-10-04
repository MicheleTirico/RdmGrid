package RdmSeedNet_2;

import java.io.IOException;

import org.graphstream.graph.Graph;
import org.graphstream.stream.file.FileSource;
import org.graphstream.stream.file.FileSourceDGS;
import org.graphstream.stream.file.FileSourceFactory;
import org.graphstream.ui.swingViewer.ViewPanel;
import org.graphstream.ui.view.Viewer;

import RdmGsaNetViz.handleVizStype;
import RdmGsaNetViz.handleVizStype.stylesheet;
import RdmSeedNet_2.layerRd.typeDiffusion;
import RdmSeedNet_2.layerRd.typeInitializationMaxLocal;

public class runAndViz extends framework {

	private static double  Da = 0.2, Db = 0.1 ;	
	private static double g = 1, alfa = 2 , Ds = .1 , r = 5 ;
	
	// viz 
	protected static FileSource fs ;
	private static ViewPanel view ;
	private static Viewer viewer ;
	
	public static void main(String[] args) throws  InterruptedException {		
	
		bks = new bucketSet(1, 1, 200, 200);
		bks.initializeBukets();
		
		lRd = new layerRd(1, 1, 200, 200, true);		
		lRd.initializeCostVal(1,0);	
		lRd.setInitMaxLocal(typeInitializationMaxLocal.singlePoint , morphogen.b, false);
			
		setRdType ( RdmType.f055_k062 ) ;	//	System.out.println(f + " " + k);
		lRd.setGsParameters(f, k, Da, Db, typeDiffusion.mooreWeigthed );
	
		lNet = new layerNet("net") ;
		Graph netGraph = lNet.getGraph();
	
		lSeed = new layerSeed(g, alfa, Ds, r , morphogen.b );

		initMultiCircle(1, 1, 20 , 100 ,100 , 2 , 4 );
		lNet.setLengthEdges("length" , true );

		// set file Source for file step
		viewer = netGraph.display(false);	

		// setup viz netGraph
		handleVizStype netViz = new handleVizStype( netGraph ,stylesheet.manual , "seed", 1) ;
		netViz.setupIdViz(false , netGraph, 20 , "black");
		netViz.setupDefaultParam (netGraph, "black", "black", 1 , 0.5 );
		netViz.setupVizBooleanAtr(true, netGraph, "black", "red" , false , false ) ;
		netViz.setupFixScaleManual( true , netGraph, 200 , 0);
	
		// only for viz
		for ( seed s : lSeed.getListSeeds()) 	
			s.getNode().setAttribute("seed", 1);	
		
		int t = 0 ;
		while ( t < 100  ) {
			if ( ! lSeed.getListSeeds().isEmpty() ) {
				System.out.println("------------- step " +t);
				lRd.updateLayer();
				lRd.computeMaxLocal();
				lNet.updateLayers_04(typeVectorField.gravity , 4 , true , 1 );
		//		Thread.sleep(1);
				t++;
			}
			
		}
	


		
	
	}

	
		
}
