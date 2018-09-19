package RdmSeedNet_2;

import java.util.ArrayList;

import org.graphstream.graph.Graph;

import RdmGsaNetViz.handleVizStype;
import RdmGsaNetViz.handleVizStype.stylesheet;
import RdmSeedNet_2.layerNet.typeSetupLayer;
import RdmSeedNet_2.layerRd.typeDiffusion;

public class run extends framework {

	static double  f = 0.030, k=0.062, Da = 0.2, Db = 0.1 ;
	
	static double g = 1, alfa = 2 , Ds = 1	, r = 10;
	
	public static void main(String[] args) {

		lRd = new layerRd(1.0, 1.0, 150, 150);
	
		lRd.initializeCostVal(1,0);	
		lRd.setValueOfCellAround(1, 1, 75, 75, 1);		//		lRd.setValueOfCell(1, 1, 25, 25)
		lRd.setGsParameters(f, k, Da, Db, typeDiffusion.mooreWeigthed);
	
		
		lNet = new layerNet(10) ;
//		lNet.setParametersCircle (8 , 2);
	//	lNet.initLayer(typeSetupLayer.circle);

		lSeed = new layerSeed(g, alfa, Ds, r , morphogen.b );
	//	lSeed.initializationEachNode();
		lSeed.initializationSeedCircle(20, 3);
		
	
	
		for ( int t = 0 ; t < 200 ; t++) {			
			
			System.out.println("------------- step " +t);
			lRd.updateLayer();
		//	lSeed.updateLayer();
		//	lNet.updateLayer();
			lNet.updateLayerAndSeeds();
			
		}
		
	//	lNet.display(true);

		 Graph gra = lNet.getGraph();
			
		// setup viz netGraph
		handleVizStype netViz = new handleVizStype( gra ,stylesheet.manual , "merge", 1) ;
		netViz.setupIdViz(false , gra, 10 , "black");
		netViz.setupDefaultParam (gra, "black", "black", 5 , 0.5 );
		netViz.setupVizBooleanAtr(true, gra, "black", "red" , false , false ) ;
		netViz.setupFixScaleManual( true  , gra, 150 , 0);
	
		gra.display(false) ;
		
		
	}

}
