package RdmSeedNet_2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.ui.graphicGraph.GraphPosLengthUtils;

public class layerSeed extends framework {
	
	private ArrayList<seed> seeds = new ArrayList<seed>();
	private double alfa, dist, g , Ds , r ;
	private morphogen m ;
	
	public enum typeInitializationSeed {centerCellThMorp, test }
	private static typeInitializationSeed typeInitializationSeed ;
	
	public layerSeed () {
		this(0,0,0,0, null);	
	}

	public layerSeed( double g , double alfa , double Ds, double r , morphogen m) {
		this.g = g;
		this.alfa = alfa ;
		this.Ds = Ds ;
		this.r = r ;		
		this.m = m ; 
	}
	
// INITIALIZATION SEED SET --------------------------------------------------------------------------------------------------------------------------
	public void initializationSeedThMorp ( morphogen m , double minTh, double maxTh) {
		for ( cell c : listCell ) {
			double val = lRd.getValMorp(c, m, true) ;
			if (val >= minTh && val <= maxTh)
				createSeed(c.getX(), c.getY());
		}
	}
	
	public void initializationEachNode () {
		for ( Node n : lNet.getGraph().getEachNode() ) {
			double[] pos = GraphPosLengthUtils.nodePosition(n ) ;
			createSeed(pos[0], pos[1]);
	//		seed s = lSeed.getSeed
		}
	}
	
	// remove seed 
	public void removeSeed ( seed s ) {
		seeds.remove(s) ;
	}
	
	// create one seed
	public void createSeed ( double X , double Y ) {
		seeds.add( new seed(X, Y, 0, 0 , null) ) ;
	}
	
	public void createSeed ( double X , double Y , Node n ) {
		n.addAttribute("xyz", X , Y  , 0 );
		seeds.add( new seed(X, Y, 0, 0 , n) ) ;
	}
	
	public void initializationSeedCircle ( int numNodes , double radius ) {
		
		Graph graph = lNet.getGraph() ;
		double[] centerLayerRd = lRd.getCenter () ;
		double 	centerX = centerLayerRd[0] , 
				centerY = centerLayerRd[1] ,
				angle = 2 * Math.PI / numNodes ;		
		for ( idNodeInt = 0 ; idNodeInt < numNodes ; idNodeInt++ ) {
			
			double 	coordX = radius * Math.cos( idNodeInt * angle ) ,
					coordY = radius * Math.sin( idNodeInt * angle ) ;
					
			idNode =  Integer.toString(idNodeInt) ;
			graph.addNode(idNode) ;
			Node n = graph.getNode(idNode);
			
			n.addAttribute("xyz", centerX + coordX ,  centerY + coordY , 0 );
			
			lSeed.createSeed(centerX + coordX, centerY + coordY , n);
		}
		
		for ( idEdgeInt = 0 ; idEdgeInt < numNodes ; idEdgeInt++ ) {
			String idEdge = Integer.toString(idEdgeInt);
			try {		
				graph.addEdge(idEdge,Integer.toString(idEdgeInt) , Integer.toString(idEdgeInt+1) ) ;		
			}
			catch (org.graphstream.graph.ElementNotFoundException e) {
				graph.addEdge(idEdge,Integer.toString(idEdgeInt) , Integer.toString(0) ) ;
				break ; 
			}
		}
		for ( Node n : graph.getEachNode()) {
			bks.putNode(n);
		}

	}
	
// UPDATE LAYER SEED --------------------------------------------------------------------------------------------------------------------------------	
	public void updateLayer() {
		for (seed s : seeds) {
			double sX = s.getX() , sY = s.getY() , vecX = 0 , vecY = 0;
			for ( int x = (int) Math.floor(s.getX() -r ) ; x <= (int) Math.ceil(s.getX() + r ); x++ )
				for ( int y = (int) Math.floor(s.getY() -r ) ; y <= (int) Math.ceil(s.getY() + r ); y++ ) {
					try {
						cell c = lRd.getCell(x,y);
						double val = lRd.getValMorp(c, m, true) ;
						vecX = vecX + ceckPositionVectorGravity(c.getX(),s.getX(),val) ;
						vecY = vecY + ceckPositionVectorGravity(c.getY(),s.getY(),val) ;
					} catch (ArrayIndexOutOfBoundsException e) {
						continue ;
					}
				
				}
			vecX = checkValueVector(vecX, 1) ;
			vecY = checkValueVector(vecY, 1) ;
			
			s.setVec(-vecX, -vecY);	
			s.setCoords(sX - vecX , sY - vecY);	
		}
	}

	protected double[] getVector (seed s , typeVectorField typeVectorField ) {
		
		double[] vector = new double[2];
		switch ( typeVectorField) {
			case gravity:
				vector = getVectorGravity(s);
				break;
			case slope :
				vector = getVectorSlope(s);
				break;
		}
		return vector ;
	}
	
	// get vector slope
	private double[] getVectorSlope ( seed s ) {
		double sX = s.getX() , sY = s.getY() ;

		cell 	c00 = lRd.getCell( (int) Math.floor(sX),(int) Math.floor(sY)), 
				c11 = lRd.getCell( (int) Math.ceil(sX),(int) Math.ceil(sY)),
				c01 = lRd.getCell( (int) Math.floor(sX),(int) Math.ceil(sY)),
				c10 = lRd.getCell( (int) Math.ceil(sX),(int) Math.floor(sY));
		
		double 	val00 = lRd.getValMorp(c00, m, false) , 
				val11 = lRd.getValMorp(c11, m, false) ,
				val01 = lRd.getValMorp(c01, m, false) ,
				val10 = lRd.getValMorp(c10, m, false) ,
				distXfloor = Math.pow(sY - Math.floor(sY), 1), 
				distXceil = Math.pow(sY - Math.ceil(sY), 1), 
				
				distYfloor = Math.pow(sX - Math.floor(sX), 1), 
				distYceil = Math.pow(sX - Math.ceil(sX), 1); 
				
		//		System.out.println(distXceil + " " + distXfloor);
				double 	vecX = (-val00 + val10) - (val10 - val11) ,
						vecY = (-val00 + val01) + (val01 - val11) ;
					
		if ( Double.isNaN(vecX))			vecX = 0 ;
		if ( Double.isNaN(vecY))			vecY = 0 ;
		
		vecX = checkValueVector(vecX, 1.0) ;
		vecY = checkValueVector(vecY, 1.0) ;
		
//		System.out.println(vecX);		
//		System.out.println(vecY);	

		s.setVec(-vecX, -vecY);
		return new double[] {-vecX ,-vecY} ;
	}
	
	//get vector gravity
	private double[] getVectorGravity ( seed s ) {
		double vecX = 0 , vecY = 0;
		for ( int x = (int) Math.floor(s.getX() -r ) ; x <= (int) Math.ceil(s.getX() + r ); x++ )
			for ( int y = (int) Math.floor(s.getY() -r ) ; y <= (int) Math.ceil(s.getY() + r ); y++ ) {
				try {
					cell c = lRd.getCell(x,y);
					double val = lRd.getValMorp(c, m, true) ;
					vecX = vecX + ceckPositionVectorGravity(c.getX(),s.getX(),val) ;
					vecY = vecY + ceckPositionVectorGravity(c.getY(),s.getY(),val) ;
				} catch (ArrayIndexOutOfBoundsException e) {
					continue ;
				}
			}
		vecX = checkValueVector(vecX, 1.0) ;
		vecY = checkValueVector(vecY, 1.0) ;
		
		s.setVec(-vecX, -vecY);
		return new double[] {-vecX ,-vecY} ;
	}
	
	private void setSeedNewCoords ( seed s , double vecX ,double vecY ) {
		s.setCoords(s.getX() + vecX, s.getY() + vecY);
	}
	
	// check max value of vector 
	private double checkValueVector (double vec , double valMax) { 
		if (vec > valMax )				
			return  valMax ;
		else if (vec < - valMax )				
			return - valMax ;
		else 
			return vec;
	}
	
	// not used
	private double ceckPositionVector (double vector ,double posCell , double posSeed, double val) {
		vector = vector / Math.pow(posCell - posSeed, alfa) ;	
		if ( posCell == posSeed)
			return 0 ;
		if ( posCell < posSeed)
			return -vector ;
		else return vector ;
	}
	
	// check distance between seed and cell
	private double ceckPositionVectorGravity (double posCell , double posSeed, double val) {
		double v = Ds * g * val / Math.pow(posCell - posSeed, alfa);
		if ( posCell == posSeed)
			return 0 ;
		if ( posCell < posSeed)
			return -v ;
		else return v ;
	}
	

	// old method
	public void updateLayer3() {
		for ( seed s : seeds ) {
			double sX = s.getX() , sY = s.getY() , vecX = 0 , vecY = 0;		
			ArrayList<cell> listCell = new ArrayList<cell>(getListCellsInRadius(s));
			for ( cell c : listCell) {
				double 	val = lRd.getValMorp(c, m, false) ;
				if ( val > 1 )				val = 1 ; 
				vecX = vecX + ceckPositionVectorGravity(c.getX(),s.getX(),val) ;
				vecY = vecY + ceckPositionVectorGravity(c.getY(),s.getY(),val) ;
			}
			if (vecX > 1 )				vecX = 1 ;		
			if (vecY > 1 )				vecY = 1 ;		
			if (vecX < - 1 )			vecX = - 1 ;		
			if (vecY < - 1 )			vecY = - 1 ;
			
			s.setVec(vecX, vecY);
			s.setX( sX + vecX );
			s.setY( sY + vecY );	
		}
	}
	
	// not used
	private double[] computeVec (seed s, morphogen m ) {
		double vecX = 0 , vecY = 0, sX = s.getX(), sY = s.getY() ;	
		for ( int x = (int) Math.floor(s.getX() - r ) ; x <= (int) Math.ceil(s.getX() + r ); x++ )
			for ( int y = (int) Math.floor(s.getY() - r ) ; y <= (int) Math.ceil(s.getY() + r ); y++ ) {
				double 	val = lRd.getValMorp(lRd.getCell(x, y), m, false) ;
				if ( val > 1 )
					val = 1 ;
				
				vecX = vecX + getVector(x, sX, val);
				vecY = vecY + getVector(y, sY, val) ;			
			}
		double[] vec = new double[2];
		vec[0] = vecX;
		vec[1] = vecY;
		return vec; 
	}
	
	// not used
	private  ArrayList<cell> getListCellsInRadius ( seed s  ) {	
		ArrayList<cell> list = new ArrayList<cell> () ;
		for ( int x = (int) Math.floor(s.getX() -r ) ; x <= (int) Math.ceil(s.getX() + r ); x++ )
			for ( int y = (int) Math.floor(s.getY() -r ) ; y <= (int) Math.ceil(s.getY() + r ); y++ ) 
				list.add(lRd.getCell(x,y));		
		return list;
	}
	
	// not used	
	private double getVector (double posCell, double posSeed, double val ) {	
		double sign = getSignVec(posCell , posSeed ) ;	
		if ( sign == 0 ) 
			return 0 ;
		else return sign * ( g * Ds * val ) / Math.pow(posCell - posSeed, alfa) ;		
	}
	
	// not used
	private double getSignVec ( double pos1 , double pos2 ) {	
		if (pos1 == pos2)
			return 0 ; 
		else if ( pos1 < pos2   )
			return -1;
		else 
			return 1 ;
	}
	
// GET METHODS --------------------------------------------------------------------------------------------------------------------------------------
	public ArrayList<seed> getListSeeds () {
		return seeds;		
	}
	
	public ArrayList<Node> getListNodeWithSeed ( ) {
		ArrayList<Node> list = new ArrayList<Node> ();
		for ( seed s : seeds) {
			Node n = s.getNode();
			if ( ! list.contains(n))
				list.add(s.getNode());
		}
		return list;
	}
	
	public Map<cell ,Double> getValAroundSeed ( seed s , morphogen m , double r ) {
		Map<cell ,Double> map = new HashMap<cell ,Double> ();
		for ( int x = (int) Math.floor(s.getX() -r ) ; x <= (int) Math.ceil(s.getX() + r ); x++ )
			for ( int y = (int) Math.floor(s.getY() - r ) ; y <= (int) Math.ceil(s.getY() + r ); y++ ) {
					cell c = lRd.getCell(x,y);
					double val = lRd.getValMorp(c, m, true) ;
					map.put(c, val);
				}
	
		return map;
		
		
	}
}
