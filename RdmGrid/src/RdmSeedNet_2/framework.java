package RdmSeedNet_2;

import java.util.ArrayList;

public abstract class framework  {

	protected static layerSeed lSeed = new layerSeed();
	protected static layerRd lRd = new layerRd();
	protected static layerNet lNet = new layerNet() ;
	protected static bucketSet bks = new bucketSet() ;
	
	protected static int idNodeInt  ;
	protected static int idEdgeInt ;
	protected static String idNode ;
	protected static String idEdge;
	
	protected static ArrayList<cell> listCell = new ArrayList<cell> ();
	protected static ArrayList<bucket> listBucket = new ArrayList<bucket>();
	
	protected  enum morphogen { a , b }		
	
	
	
}
