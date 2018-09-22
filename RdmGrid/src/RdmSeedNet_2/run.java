package RdmSeedNet_2;

import java.util.ArrayList;

import org.graphstream.graph.Graph;

import RdmGsaNetViz.handleVizStype;
import RdmGsaNetViz.handleVizStype.stylesheet;
import RdmSeedNet_2.layerNet.typeSetupLayer;
import RdmSeedNet_2.layerRd.typeDiffusion;

public class run extends framework {

	static double  f , k  , Da = 0.2, Db = 0.1 ;	
	static double g = 1, alfa = 2 , Ds = 1	, r = 5;
	
	public static enum RdmType { holes , solitions , movingSpots , pulsatingSolitions , mazes , U_SkateWorld , f055_k062 , chaos , spotsAndLoops , worms , waves }
	private static RdmType type ;
	
	public static void main(String[] args) {

		bks = new bucketSet(1, 1, 200, 200);
		bks.initializeBukets();
		
		lRd = new layerRd(1, 1, 200, 200);		
		lRd.initializeCostVal(1,0);	
		lRd.setValueOfCellAround(1, 1, 100, 100, 2);		//		lRd.setValueOfCell(1, 1, 25, 25)
		
		setRdType ( RdmType.f055_k062) ;
		lRd.setGsParameters(f, k, Da, Db, typeDiffusion.mooreCost );
	
		lNet = new layerNet(10) ;
		lSeed = new layerSeed(g, alfa, Ds, r , morphogen.b );
		lSeed.initializationSeedCircle(20, 4);
		
		for ( int t = 0 ; t < 100 ; t++) {			
			
			System.out.println("------------- step " +t);
			lRd.updateLayer();
		//	lNet.updateLayerAndSeeds2();
			lNet.updateLayerAndSeeds7();
			if ( lSeed.getListSeeds().isEmpty())
				break;
		}
		
		Graph gra = lNet.getGraph();
			
		// setup viz netGraph
		handleVizStype netViz = new handleVizStype( gra ,stylesheet.manual , "merge", 1) ;
		netViz.setupIdViz(false , gra, 20 , "black");
		netViz.setupDefaultParam (gra, "black", "black", 5 , 0.5 );
		netViz.setupVizBooleanAtr(true, gra, "black", "red" , false , false ) ;
		netViz.setupFixScaleManual( false  , gra, 200 , 0);
	
		gra.display(false) ;
		
		
	}

	// set RD start values to use in similtion ( gsAlgo )
		private static  void setRdType ( RdmType type ) {
			
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
		
}
