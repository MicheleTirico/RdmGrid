package RdmSeedNet;

import java.util.ArrayList;

import RdmSeedNet.layerRd.typeNeighbourhood;

public class layerSeed extends framework {
	
	private ArrayList<seed> seeds = new ArrayList<seed>();
	private double alfa, dist, g , Ds , r ;
	
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
	
			for ( cell c : listCell ) {
				double val = lRd.getValMorp(c, m) ;
				if (val >= minTh && val <= maxTh)
					createSeed(c.getX(), c.getY());
		}
	}
	
	// create one seed
	public void createSeed ( double X , double Y ) {
		seeds.add( new seed(X, Y, 0, 0) ) ;
	}
	
// UPDATE LAYER SEED --------------------------------------------------------------------------------------------------------------------------------
	
	// compute vector 
	private void computeVector ( seed s ) {
		
		
		getCellsInRadius(s, r);
		
	}
	
	private ArrayList<cell> getCellsInRadius ( seed s , double r ) {
		ArrayList<cell> list = new ArrayList<cell>() ;
		int x =  (int) (s.getX()- r ) ;
		Math.rou
		for ( int x = Math.round(s.getX()-r))  
		
		return list;
	}
}
