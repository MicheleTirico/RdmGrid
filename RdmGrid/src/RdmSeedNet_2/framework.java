package RdmSeedNet_2;

import java.util.ArrayList;

import javax.swing.JPanel;

import org.graphstream.graph.Node;
import org.graphstream.ui.graphicGraph.GraphPosLengthUtils;

import RdmSeedNet_2.run.RdmType;

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
	
	protected enum morphogen { a , b }		
	protected enum typeVectorField { gravity , slope , slopeDistance , slopeRadius , slopeDistanceRadius } 
	
	
	// get spatial distance from 2 nodes 
		public static double getDistGeom ( Node n1 , Node n2 ) {	
			
			double [] 	coordN1 = GraphPosLengthUtils.nodePosition(n1) , 
						coordN2 = GraphPosLengthUtils.nodePosition(n2); 
			
			return  Math.pow(Math.pow( coordN1[0] - coordN2[0] , 2 ) + Math.pow( coordN1[1] - coordN2[1] , 2 ), 0.5 )  ;
		}
		
		public static double getDistGeom ( double [] coordN1 , double [] coordN2 ) {			
			return  Math.pow(Math.pow( coordN1[0] - coordN2[0] , 2 ) + Math.pow( coordN1[1] - coordN2[1] , 2 ), 0.5 )  ;
		}

	
	
	
	
}
