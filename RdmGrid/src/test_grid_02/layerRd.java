package test_grid_02;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class layerRd {

	private double sizeX, sizeY;
	private int numCellX, numCellY;
	private cell[][] cells ;
	private  double f ,  k ,  Da,  Db ;
	
	public enum typeNeighbourhood { moore, vonNewmann }
	public enum typeDiffusion { mooreCost, mooreWeigthed , vonNewmannCost, vonNewmannWeigthed }
	public enum morphogen { a , b }
	
	private typeDiffusion typeDiffusion ; 
	private ArrayList<cell> listCellActive = new ArrayList<cell> ();
	
	public layerRd ( ) {
		this(0,0,0,0);	
	}

	public layerRd(double sizeX, double sizeY , int numCellX, int numCellY) {
		this.sizeX = sizeX ;
		this.sizeY = sizeY ;
		this.numCellX = numCellX ;
		this.numCellY = numCellY;
		cells = new cell[numCellX][numCellY];
	}

// INITIALIZATION GRID ------------------------------------------------------------------------------------------------------------------------------
	public void initializeCostVal ( double val1 , double val2 ) {
		for (int x = 0; x<numCellX; x++)
			for (int y = 0; y<numCellY; y++) {
				cell c = new cell(x,y,val1, val2);
				cells[x][y] = c ;
				putCellInList(c);			
			}
	}
	
	public void initializeRandomVal ( int seedRd1, int seedRd2, double minVal1, double minVal2 , double maxVal1 , double maxVal2) {
		for (int x = 0; x<numCellX; x++)
			for (int y = 0; y<numCellY; y++) {
				cell c = new cell(x,y,getValRd(seedRd1, minVal1, maxVal1), getValRd(seedRd2, minVal2, maxVal2));
				cells[x][y] = c ;
				putCellInList(c);			
			}	
	}

	
// RULES --------------------------------------------------------------------------------------------------------------------------------------------
	
	// Gray Scott classic model -------------------------------------------------------------------------------------------------------------------------
		
	// set initial parameters of gray scott model
	public void setGsParameters ( double f , double k , double Da, double Db, typeDiffusion typeDiffusion) {
		this.k = k ;
		this.f = f ;
		this.Da = Da ;
		this.Db = Db ;
		this.typeDiffusion = typeDiffusion;
	}
	
	// set perturbation 
	public void setValueOfCell ( double valA , double valB , int cellX, int cellY ) {
		cells[cellX][cellY].setVals(valA, valB);		
	}
	
	// update cells 
	public void updateValues (  ) {
			
		for ( cell c :listCellActive ) {
			double 	valA = getValMorp(c, morphogen.a),
					valB = getValMorp(c, morphogen.b);
			
			morphogen a = morphogen.a ;
			morphogen b = morphogen.b ;
			
	
			double 	diffA = getCoefDiff(a) * getDiffusionCost(typeDiffusion, c, a) ,
					diffB = getCoefDiff(b) * getDiffusionCost(typeDiffusion, c, b) ,
			
					react = valA * Math.pow(valB, 2 ) ,
			
					extA = f * ( 1 - valA ) ,
					extB = ( f + k ) * valB ;
	//		extA = 0 ;
	//		extB = 0 ; 
	
			double	newValA =  valA + diffA - react + extA,
					newValB =  valB + diffB + react - extB;
			
			c.setVals(newValA, newValB);
		}
	}

	// get Fick's diffusion 
	private double getDiffusionCost ( typeDiffusion typeDiffusion, cell c , morphogen m ) {
		double 	diff = 0 , 
				val = getValMorp(c, m) ,
				valNeig = 0;
		ArrayList<cell> listNeig = new ArrayList<cell>();
		
		switch (typeDiffusion) {
			case mooreCost:
				listNeig = getListNeighbors(typeNeighbourhood.moore, c.getX(),c.getY()) ;
				break;
			case vonNewmannCost:
				listNeig = getListNeighbors(typeNeighbourhood.vonNewmann, c.getX(),c.getY()) ;
				break;
			}
		for ( cell neig : listNeig) 
			 valNeig = valNeig + getValMorp(neig, m);

		diff =  ( listNeig.size() * val - valNeig) ;

		return diff ; 
	}

// GET METHODS --------------------------------------------------------------------------------------------------------------------------------------
	public double getValueMorphogen ( int cellX, int cellY, morphogen m) {		
		if ( m.equals(morphogen.a))
			return cells[cellX][cellY].getVal1();
		else
			return cells[cellX][cellY].getVal2();
	}
	
// private methods ----------------------------------------------------------------------------------------------------------------------------------
	private double getValRd ( int seedRd, double minRd , double maxRd ) {
		Random rd = new Random( seedRd );
		return minRd + (maxRd - minRd) * rd.nextDouble(); 
	}

	// get val morphogen in cell 
	private double getValMorp ( cell c, morphogen m) {		
		if ( m.equals(morphogen.a))
			return c.getVal1();
		else
			return c.getVal2();
	}
	
	// get coefficient diffusion 
	private double getCoefDiff ( morphogen m) {		
		if ( m.equals(morphogen.a))
			return Da;
		else
			return Db;
	}
	
// GET NEIGHBORS ------------------------------------------------------------------------------------------------------------------------------------	 
	private ArrayList<cell> getListNeighbors ( typeNeighbourhood typeNeighbourhood , int cellX , int cellY ) {
		ArrayList<cell> list = new  ArrayList<cell> ();		
		switch (typeNeighbourhood) {
			case moore: {
				list.addAll(Arrays.asList(
		    			
						cells[checkCell(cellX+1,numCellX)][checkCell(cellY-1,numCellY)],
						cells[checkCell(cellX+1,numCellX)][checkCell(cellY,numCellY)],
						cells[checkCell(cellX+1,numCellX)][checkCell(cellY+1,numCellY)],
				
						cells[checkCell(cellX,numCellX)][checkCell(cellY+1,numCellY)],
						cells[checkCell(cellX,numCellX)][checkCell(cellY-1,numCellY)],
				
						cells[checkCell(cellX-1,numCellX)][checkCell(cellY-1,numCellY)],
						cells[checkCell(cellX-1,numCellX)][checkCell(cellY,numCellY)],
						cells[checkCell(cellX-1,numCellX)][checkCell(cellY+1,numCellY)]
								));		
			} break ;
		    	 
			case vonNewmann : {
				list.addAll(Arrays.asList(
						cells[checkCell(cellX,numCellX)][checkCell(cellY+1,numCellY)],
						cells[checkCell(cellX,numCellX)][checkCell(cellY-1,numCellY)],
						cells[checkCell(cellX-1,numCellX)][checkCell(cellY,numCellY)],
						cells[checkCell(cellX+1,numCellX)][checkCell(cellY,numCellY)]
								)); 
			} break ;		
		}
		return list; 
	 }	
	
	//check boundary condition
	private int checkCell ( int cell , int maxCell) {
		
		if ( cell > maxCell -1 ) 
			return 0  ;
		if ( cell  <= 0 ) 
			return maxCell-1;
		else return cell;
	}
	
// LIST CELL ACTIVE ---------------------------------------------------------------------------------------------------------------------------------	
	private void putCellInList (cell c) {
		if ( !listCellActive.contains(c))
			listCellActive.add(c);
	}
 
	public ArrayList<cell> getListCellActive () {
		return listCellActive;		
	}
	
}
