package RdmSeedNet_2;

import java.util.ArrayList;


public class layerSeed extends framework {
	
	private ArrayList<seed> seeds = new ArrayList<seed>();
	private double alfa, dist, g , Ds , r ;
	private morphogen m ;
	
	public enum typeInitializationSeed {centerCellThMorp, test }
	private static typeInitializationSeed typeInitializationSeed ;
	
	public layerSeed () {
		this(0,0,0,0);	
	}

	public layerSeed( double g , double alfa , double Ds, double r ) {
		this.g = g;
		this.alfa = alfa ;
		this.Ds = Ds ;
		this.r = r ;
		
	}
	
// INITIALIZATION SEED SET --------------------------------------------------------------------------------------------------------------------------
	public void initializationSeedThMorp ( morphogen m , double minTh, double maxTh) {
	
		this.m = m ; 
		for ( cell c : listCell ) {
			double val = lRd.getValMorp(c, m, true) ;
			if (val >= minTh && val <= maxTh)
				createSeed(c.getX(), c.getY());
		}
	//	System.out.println(seeds);
		
	}
	
	// create one seed
	public void createSeed ( double X , double Y ) {
		seeds.add( new seed(X, Y, 0, 0) ) ;
	}
	
// UPDATE LAYER SEED --------------------------------------------------------------------------------------------------------------------------------
	
	public void updateLayer() {
		for (seed s : seeds) {
			double sX = s.getX() , sY = s.getY() , vecX = 0 , vecY = 0;
			for ( int x = (int) Math.floor(s.getX() -r ) ; x <= (int) Math.ceil(s.getX() + r ); x++ )
				for ( int y = (int) Math.floor(s.getY() -r ) ; y <= (int) Math.ceil(s.getY() + r ); y++ ) {
					cell c = lRd.getCell(x,y);
					double val = lRd.getValMorp(c, m, true) ;
					vecX = vecX + ceckPositionVector(c.getX(),s.getX(),val) ;
					vecY = vecY + ceckPositionVector(c.getY(),s.getY(),val) ;
				}
			vecX = checkValueVector(vecX, 1) ;
			vecY = checkValueVector(vecY, 1) ;
			
			s.setVec(vecX, vecY);	
			s.setCoords(sX + vecX , sY + vecY);	
		}
	}
		
	public void updateLayer3() {
		
		for ( seed s : seeds ) {
			double sX = s.getX() , sY = s.getY() , vecX = 0 , vecY = 0;
		
			System.out.println(s.getX() + " " + s.getY());
			
			double[] vec = new double[2] ;
			
			ArrayList<cell> listCell = new ArrayList<cell>(getListCellsInRadius(s));
			
			for ( cell c : listCell) {
		//		System.out.println(c.getX() + " " + c.getY());
				
				double 	val = lRd.getValMorp(c, m, false) ;
		
				if ( val > 1 )
					val = 1 ; 
				
				double v = ceckPositionVector(c.getX(),s.getX(),val);
		//		System.out.println(v);
				vecX = vecX + ceckPositionVector(c.getX(),s.getX(),val) ;
				vecY = vecY + ceckPositionVector(c.getY(),s.getY(),val) ;
	//			System.out.println(getVector(c.getX(), sX, val));
			}
			

			if (vecX > 1 )				vecX = 1 ;
			
			if (vecY > 1 )				vecY = 1 ;
			
			if (vecX < - 1 )				vecX = - 1 ;
			
			if (vecY < - 1 )				vecY = - 1 ;
			s.setVec(vecX, vecY);
	
			//s.setCoords(sX + vecX , sY + vecY);
			s.setX( sX + vecX );
			s.setY( sY + vecY );
		
		}
	}
	
	private double checkValueVector (double vec , double valMax) { 
		if (vec > valMax )				
			return  valMax ;
		else if (vec < - valMax )				
			return - valMax ;
		else 
			return vec;
	}
	
	private double ceckPositionVector (double posCell , double posSeed, double val) {
		double v = Ds * g * val / Math.pow(posCell - posSeed, alfa);
		if ( posCell == posSeed)
			return 0 ;
		if ( posCell < posSeed)
			return -v ;
		else return v ;
	}
	
	public void updateLayer2 ( ) {

	seed s= seeds.get(0);
	//		System.out.println(s);
	//		System.out.println(s.getX() + " " + s.getY());
			double[] vec = computeVec(s, m);

			s.setVec(vec[0], vec[1]);
	
			s.setX(s.getX()+vec[0]);
			s.setY(s.getY()+vec[1]);

	//		System.out.println(vec[0]+ " " + vec[1]);
	//		System.out.println(vec[0]+s.getVecX());
	//		s.setX(8);
	//		s.setY(8);
	//		System.out.println(vec[0]+s.getVecX());
		
	}
	

	// compute vector 
	private double[] computeVec (seed s, morphogen m ) {

		double vecX = 0 , vecY = 0, sX = s.getX(), sY = s.getY() ;	

		for ( cell c : getListCellsInRadius(s) ) {
			System.out.println(c.getX());
		}
		
		for ( int x = (int) Math.floor(s.getX() - r ) ; x <= (int) Math.ceil(s.getX() + r ); x++ )
			for ( int y = (int) Math.floor(s.getY() - r ) ; y <= (int) Math.ceil(s.getY() + r ); y++ ) {
			//	System.out.println( s.getX() );

				double 	val = lRd.getValMorp(lRd.getCell(x, y), m, false) ;
				if ( val > 1 )
					val = 1 ;
				
				vecX = vecX + getVector(x, sX, val);
				vecY = vecY + getVector(y, sY, val) ;
		
				if ( vecX > 1) 
					System.out.println(vecX);
				
		//		System.out.println(getVector(x, sX, val));
			}
	
		double[] vec = new double[2];
		vec[0] = vecX;
		vec[1] = vecY;
		return vec; 
	}
	
	private  ArrayList<cell> getListCellsInRadius ( seed s  ) {
		
		ArrayList<cell> list = new ArrayList<cell> () ;
		for ( int x = (int) Math.floor(s.getX() -r ) ; x <= (int) Math.ceil(s.getX() + r ); x++ )
			for ( int y = (int) Math.floor(s.getY() -r ) ; y <= (int) Math.ceil(s.getY() + r ); y++ ) 
				list.add(lRd.getCell(x,y));
		
		return list;
	}
		
	private double getVector (double posCell, double posSeed, double val ) {
		
		double sign = getSignVec(posCell , posSeed ) ;	
		if ( sign == 0 ) 
			return 0 ;
		else return sign * ( g * Ds * val ) / Math.pow(posCell - posSeed, alfa) ;		
	}
	
	private double getSignVec ( double pos1 , double pos2 ) {
		
		if (pos1 == pos2)
			return 0 ; 
		else if ( pos1 - pos2 >= 0 )
			return 1;
		else 
			return -1 ;
	}
	
	public ArrayList<seed> getListSeeds () {
		return seeds;
		
	}
 }
