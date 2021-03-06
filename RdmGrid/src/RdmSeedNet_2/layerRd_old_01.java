package RdmSeedNet_2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.graphicGraph.GraphPosLengthUtils;

import RdmSeedNet_2.framework.morphogen;

public class layerRd_old_01 extends framework   {

	private double sizeX, sizeY;
	private int numCellX, numCellY , idPointMaxLocInt  = 0 ;
	private String idPointMaxLoc ;
	private cell[][] cells ;
	private ArrayList<cell> listMaxLocal = new ArrayList<cell>() ;
	private morphogen m ;
	private Graph graphMaxLoc = new SingleGraph ("maxLoc");
	
	private ArrayList <int[]> listCoordsMaxLoc = new ArrayList<int[]>();
	private ArrayList <cell> listCellMaxLoc = new ArrayList<cell>();
	private  double f ,  k ,  Da,  Db ;
	
	// feedback parameters
	private double DaMax , DbMin ;
	private double  DaN ,  DaS , DaNS , DbN ,  DbS , DbNS ;
	
	private boolean computeMaxLocal , vizMaxLocLayer ;
	
	public enum typeInitializationMaxLocal{ test , singlePoint , allPointActive , noInizialization }
	public enum typeComputeMaxLocal{ test , movingMaxPoint , wholeGrid }
	
	public enum typeNeighbourhood { moore, vonNewmann , m_vn }	
	public enum typeDiffusion { mooreCost, mooreWeigthed , vonNewmannCost }
	
	private typeDiffusion typeDiffusion ;
	private typeInitializationMaxLocal typeInitializationMaxLocal ;
	private typeComputeMaxLocal typeComputeMaxLocal  ;
	public layerRd_old_01 ( ) {
		this(0,0,0,0,false , null );	
	}

	public layerRd_old_01(double sizeX, double sizeY , int numCellX, int numCellY , boolean computeMaxLocal  , typeRadius   typeRadius  ) {
		this.sizeX = sizeX ;
		this.sizeY = sizeY ;
		this.numCellX = numCellX ;
		this.numCellY = numCellY ;
		this.computeMaxLocal = computeMaxLocal ;
		this.typeRadius  =  typeRadius ;
		cells = new cell[numCellX][numCellY];
	}

// INITIALIZATION GRID ------------------------------------------------------------------------------------------------------------------------------
	public void initializeCostVal ( double val1 , double val2 ) {
		for (int x = 0; x<numCellX; x++)
			for (int y = 0; y<numCellY; y++) {
				cell c = new cell(x,y,val1, val2, false , false , false);
				cells[x][y] = c ;
				putCellInList(c);			
			}
	}
	
	public void initializeRandomVal ( int seedRd1, int seedRd2, double minVal1, double minVal2 , double maxVal1 , double maxVal2) {
		for (int x = 0; x<numCellX; x++)
			for (int y = 0; y<numCellY; y++) {
				cell c = new cell(x,y,getValRd(seedRd1, minVal1, maxVal1), getValRd(seedRd2, minVal2, maxVal2), false , false , false);
				cells[x][y] = c ;
				putCellInList(c);			
			}	
	}	
	
// compute max local --------------------------------------------------------------------------------------------------------------------------------	
	
	public void setInitMaxLocal ( typeInitializationMaxLocal typeInitializationMaxLocal, typeComputeMaxLocal typeComputeMaxLocal,  morphogen m , boolean vizMaxLocLayer   ) {
		this.typeInitializationMaxLocal = typeInitializationMaxLocal  ;
		this.typeComputeMaxLocal = typeComputeMaxLocal  ;
		this.m = m ;
		this.vizMaxLocLayer = vizMaxLocLayer ;		
	}
	
	private void initializeMaxLocal ( int posX , int posY ) {		
		listMaxLocal.add(cells[posX][posY]);
		cell c = cells[posX][posY] ;
		c.setMaxLocal(true);	
		if ( vizMaxLocLayer ) {
			listCellMaxLoc.add(c);
			listCoordsMaxLoc.add(new int[] { posX, posY });
		}
	}
	
	public void computeMaxLocal ( ) {	
		
		ArrayList<cell>  listCellToAdd = new ArrayList<cell>() ,
				listCellToRemove = new ArrayList<cell>() ;
				
		switch (typeComputeMaxLocal ) {
		case movingMaxPoint: {
			for ( cell cellMax : listMaxLocal ) {
				ArrayList<cell> listNeig = getListNeighbors(typeNeighbourhood.moore, cellMax.getX(), cellMax.getY()) ;
				listNeig.add(cellMax);
				for ( cell c : listNeig ) {
					int posX = c.getX(), posY = c.getY();
					double val = getValMorp(c, m, false);
					if (  ! listCellToAdd.contains(c) && ! listCellToRemove.contains(c) && ! listMaxLocal.contains(c) ) 
						if ( val >= getValMorp(getCell(posX+1, posY), m, true) && val >= getValMorp(getCell(posX-1, posY), m, true)) 
							if ( val >= getValMorp(getCell(posX, posY+1), m, true) && val >= getValMorp(getCell(posX, posY-1), m, true)) {
								listCellToAdd.add(c);		
								listCellToRemove.add(cellMax);		
							}			
				}
			}
			
		}	break;
		case wholeGrid : {
//			listCellMaxLoc.clear();
//			for ( cell c : listCell ) {
//				c.setMaxLocal(false);
//				listCellMaxLoc.remove(c);
//				double val = getValMorp(c, m, false);
//				double valTest = 0 ;
//				int ind = 0;
//				ArrayList<cell> listNeig = getListNeighbors(typeNeighbourhood.moore, c.getX(), c.getY()) ;
//				while( valTest < val && ind < listNeig.size()) {
//					valTest = getValMorp(listNeig.get(ind), m, false);
//					ind++;	
//				}
//				if ( ind == listNeig.size() && ! listCellToAdd.contains(c) && !listCellToRemove.contains(c)) 	
//					listCellToAdd.add(c);		
//				
//			
//				
//			}
			} break ;
	
		}
		
		listCellToAdd.stream().forEach(c -> addMaxLocal(c, vizMaxLocLayer));
//		listCellToRemove.stream().forEach(c -> removeMaxLocal(c, vizMaxLocLayer));
		
		if ( vizMaxLocLayer) {	
			for ( Node n : graphMaxLoc.getEachNode()) {
				try {
					 n.getAttribute("viz");
				} catch (NullPointerException e) {
					continue ;
				}
				graphMaxLoc.removeNode(n);
			}
			
			for ( cell c : listMaxLocal) {		
				idPointMaxLoc = Integer.toString(idPointMaxLocInt++);
				Node n = graphMaxLoc.addNode(idPointMaxLoc);
				n.addAttribute("xyz", c.getX(), c.getY() , 0);	
			}
		}
	} 
		
	protected void addMaxLocal (cell c) {
		listMaxLocal.add(c);
		cells[c.getX()][c.getY()].setMaxLocal(true);		
	}
		
	protected void addMaxLocal (cell c , boolean vizMaxLocLayer ) {
		listMaxLocal.add(c);
		int posX = c.getX(), 
				posY = c.getY();
		cells[posX][posY].setMaxLocal(true);		
		if ( vizMaxLocLayer) {	
			listCellMaxLoc.add(c);
		}
	}
	
	protected void removeMaxLocal (cell c ) {
		listMaxLocal.remove(c);
		cells[c.getX()][c.getY()].setMaxLocal(false);				
	}
		
	protected void removeMaxLocal (cell c , boolean vizMaxLocLayer ) {
		listMaxLocal.remove(c);
		int posX = c.getX(), 
				posY = c.getY();
		cells[posX][posY].setMaxLocal(false);			
		if ( vizMaxLocLayer) {
			listCellMaxLoc.remove(c);
		}
	}
	
// FEEDBACK -----------------------------------------------------------------------------------------------------------------------------------------
	public void setFeedBackParameters ( double DaS , double DbN ) {
		this.DaS = DaS ;
		this.DbN = DbN ;
	}
	
	public void setFeedBackParameters ( double DaN , double DaS ,double DaNS ,double DbN , double DbS ,double DbNS ) {
		this.DaN = DaN ;
		this.DaS = DaS ;
		this.DaNS = DaNS ; 
		this.DbN = DbN ;
		this.DbS = DbS ;
		this.DbNS = DbNS ;	
	}

// RULES --------------------------------------------------------------------------------------------------------------------------------------------	
	// Gray Scott classic model 
		
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
	
	
	public void setValueOfCellAround  ( double valA , double valB , int cellX, int cellY, int radius ) {
		for ( int x = (int) Math.floor(cellX - radius) ; x <= (int) Math.ceil(cellX + radius ) ; x++  )
			for ( int y = (int) Math.floor(cellY - radius ) ; y <= (int) Math.ceil(cellY + radius ) ; y++  ) {
				cells[x][y].setVals(valA, valB);			
				if ( computeMaxLocal && typeInitializationMaxLocal.equals(typeInitializationMaxLocal.allPointActive)) 
					initializeMaxLocal( x , y );			
			}
		if ( computeMaxLocal && typeInitializationMaxLocal.equals(typeInitializationMaxLocal.singlePoint)) 
			initializeMaxLocal( cellX,  cellY ) ;
		
	}
	
	// update cells 


	public void updateLayer (  ) {
		
		for ( cell c : listCell ) {
			double 	valA = getValMorp(c, morphogen.a, false),
					valB = getValMorp(c, morphogen.b, false);
			
			morphogen a = morphogen.a ;
			morphogen b = morphogen.b ;
				
			double coefDiffA = 0 , coefDiffB = 0;
			
			if ( isFeedBackModel ) { // booleanSingleImpact , booleanCombinedImpact
				switch (typeFeedbackModel) {
				case booleanSingleImpact:
					coefDiffA = getCoeffDifSinImp (a, c) ; 
					coefDiffB = getCoeffDifSinImp (b, c);
					break;
				case booleanCombinedImpact :
					coefDiffA = getCoeffDifCombImp (a, c) ; 
					coefDiffB = getCoeffDifCombImp (b, c);
				}
				
			}
			else {
				coefDiffA = getCoefDiff(a) ; 
				coefDiffB = getCoefDiff(b);
			}
			
			double 	diffA = coefDiffA * getDiffusion(typeDiffusion, c, a) ,
					diffB = coefDiffB * getDiffusion(typeDiffusion, c, b) ,
			
					react = valA * valB * valB ,
			
					extA = f * ( 1 - valA ) ,
					extB = ( f + k ) * valB ;
	
			double	newValA =  valA + diffA - react + extA,
					newValB =  valB + diffB + react - extB;
			c.setVals(newValA, newValB);
		}
	}
	
	public void updateLayer ( boolean computeMaxLoc ) {
		
		ArrayList<cell>  listCellToAdd = new ArrayList<cell>() ,
				listCellToRemove = new ArrayList<cell>() ;
			
			listCellMaxLoc.clear();	
		for ( cell c : listCell ) {
			double 	valA = getValMorp(c, morphogen.a, false),
					valB = getValMorp(c, morphogen.b, false);
			
			morphogen a = morphogen.a ;
			morphogen b = morphogen.b ;
				
			double coefDiffA = 0 , coefDiffB = 0;
			
			if ( isFeedBackModel ) { // booleanSingleImpact , booleanCombinedImpact
				switch (typeFeedbackModel) {
				case booleanSingleImpact:
					coefDiffA = getCoeffDifSinImp (a, c) ; 
					coefDiffB = getCoeffDifSinImp (b, c);
					break;
				case booleanCombinedImpact :
					coefDiffA = getCoeffDifCombImp (a, c) ; 
					coefDiffB = getCoeffDifCombImp (b, c);
				}
				
			}
			else {
				coefDiffA = getCoefDiff(a) ; 
				coefDiffB = getCoefDiff(b);
			}
			
			double 	diffA = coefDiffA * getDiffusion(typeDiffusion, c, a) ,
					diffB = coefDiffB * getDiffusion(typeDiffusion, c, b) ,
			
					react = valA * valB * valB ,
			
					extA = f * ( 1 - valA ) ,
					extB = ( f + k ) * valB ;
	
			double	newValA =  valA + diffA - react + extA,
					newValB =  valB + diffB + react - extB;
			c.setVals(newValA, newValB);
			
			
			// computeMax
			c.setMaxLocal(false);
			if ( computeMaxLoc) {
				double val = getValMorp(c, m, false);
				double valTest = 0 ;
				int ind = 0;
				ArrayList<cell> listNeig = getListNeighbors(typeNeighbourhood.moore, c.getX(), c.getY()) ;
				while( valTest < val && ind < listNeig.size()) {
					valTest = getValMorp(listNeig.get(ind), m, false);
					ind++;	
				}
				if ( ind == listNeig.size()) 
					listCellToAdd.add(c);		
				else 
					listCellToRemove.add(c);	
			}
	}
	listCellToAdd.stream().forEach(c -> addMaxLocal(c, vizMaxLocLayer));
	listCellToRemove.stream().forEach(c -> removeMaxLocal(c,vizMaxLocLayer));
		
	}
	
	
	// get Fick's diffusion 
	private double getDiffusion ( typeDiffusion typeDiffusion, cell c , morphogen m ) {
		double 	diff = 0 , 
				val = getValMorp(c, m, false) ,
				valNeig = 0,
				valNeigS = 0 ,	// sum of values of side neighbors 
				valNeigC = 0;	// sum of values of corner neighbors	
		ArrayList<cell> listNeig = new ArrayList<cell>();
		
		switch (typeDiffusion) {
			case mooreCost: {
				listNeig = getListNeighbors(typeNeighbourhood.moore, c.getX(),c.getY()) ;
				
				for ( cell neig : listNeig) 
					 valNeig = valNeig + getValMorp(neig, m, false);

				diff = -  val + valNeig / listNeig.size()  ;
			}	break;
			
			case vonNewmannCost: {
				listNeig = getListNeighbors(typeNeighbourhood.vonNewmann, c.getX(),c.getY()) ;
				
				for ( cell neig : listNeig) 
					 valNeig = valNeig + getValMorp(neig, m, false);

				diff = -  val + valNeig / listNeig.size()  ;
			}	break;
			
			case mooreWeigthed : {
				ArrayList<cell> listNeigS = new ArrayList<cell>(getListNeighbors(typeNeighbourhood.vonNewmann, c.getX(), c.getY()));
				ArrayList<cell> listNeigC = new ArrayList<cell>(getListNeighbors(typeNeighbourhood.m_vn, c.getX(), c.getY()));
				
				for ( cell neigS : listNeigS ) 
					valNeigS = valNeigS + getValMorp(neigS, m, false);

				for ( cell neigC : listNeigC ) 
					 valNeigC = valNeigC + getValMorp(neigC, m, false);
			
				diff = - val + 0.2 * valNeigS + 0.05 * valNeigC ; 				
			}	break ;
		}
		return diff ; 
	}
		
	private double getDiffusionCost ( typeDiffusion typeDiffusion, cell c , morphogen m ) {
		double 	diff = 0 , 
				val = getValMorp(c, m, false) ,
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
			 valNeig = valNeig + getValMorp(neig, m, false);

		diff = listNeig.size() * val - valNeig ;

		return diff ; 
	}
	
	private double getDiffusionWeigth ( typeDiffusion typeDiffusion , cell c , morphogen m ) {
		double diff = 0 , 
		val = getValMorp(c, m, false) ,
		valNeigS = 0 ,	// sum of values of side neighbors 
		valNeigC = 0;	// sum of values of corner neighbors
		
		ArrayList<cell> listNeigS = new ArrayList<cell>(getListNeighbors(typeNeighbourhood.vonNewmann, c.getX(), c.getY()));
		ArrayList<cell> listNeigC = new ArrayList<cell>(getListNeighbors(typeNeighbourhood.m_vn, c.getX(), c.getY()));
		
		for ( cell neigS : listNeigS ) 
			valNeigS = valNeigS + getValMorp(neigS, m, false);

		for ( cell neigC : listNeigC ) 
			 valNeigC = valNeigC + getValMorp(neigC, m, false);

		diff = ( listNeigS.size() + listNeigC.size() ) * val - 0.8 * valNeigS - 0.2 * valNeigC ; 
				 
		return diff; 
	}

// GET METHODS --------------------------------------------------------------------------------------------------------------------------------------
	public double getValueMorphogen ( int cellX, int cellY, morphogen m) {		
		if ( m.equals(morphogen.a))
			return cells[cellX][cellY].getVal1();
		else
			return cells[cellX][cellY].getVal2();
	}
	
	protected double[] getCenter () {		
		return new double[] { numCellX * sizeX / 2 , numCellY * sizeY / 2} ;
	}
	
	protected ArrayList<cell> getListMaxLocal () {
		return listMaxLocal ;
	}
	
	protected int getNumberCellMaxLocal () {
		return listMaxLocal.size();
	}

	
	protected Graph getGraph ( ) {
		return graphMaxLoc ;
	}
	
// private methods ----------------------------------------------------------------------------------------------------------------------------------
	

	private double getValRd ( int seedRd, double minRd , double maxRd ) {
		Random rd = new Random( seedRd );
		return minRd + (maxRd - minRd) * rd.nextDouble(); 
	}
	

	// get value of morphogen 
	protected double getValMorp ( cell c, morphogen m , boolean checkVal ) {		
		double val = 0 ;
		if ( m.equals(morphogen.a))
			val = c.getVal1();
		else
			val =  c.getVal2();
	
		if ( checkVal )
			if (val>1 )
				return 1;
			else if (val < 0)
				return 0 ;
		return val;
	}
	
	// get coefficient diffusion 
	

	private double getCoefDiff ( morphogen m) {		
		if ( m.equals(morphogen.a))
			return Da;
		else
			return Db;
	}
	
	
	// get coefficient diffusion for feedback model 
	private double getCoeffDifSinImp (morphogen m , cell c ) {
		if ( m.equals(morphogen.a))
			if ( c.hasSeed() )
				return DaS;
			else 
				return Da;
		else {
			if ( c.hasNode() )
				return DbN ;
			else
				return Db ;		
		}					
	}
	
	private double getCoeffDifCombImp  ( morphogen m , cell c ) {
		
		if ( m.equals(morphogen.a)) {		
			if ( c.hasSeed() && c.hasNode())
				return DaNS;
			else if  ( c.hasSeed() && ! c.hasNode() )
				return DaS;
			else if  ( ! c.hasSeed() &&  c.hasNode() )
				return DaN ;
			else
				return Da ;			
		} else {
			if ( c.hasSeed() && c.hasNode())
				return DbNS;
			else if  ( c.hasSeed() && ! c.hasNode() )
				return DbS;
			else if  ( ! c.hasSeed() &&  c.hasNode() )
				return DbN ;
			else
				return Db ;			
		}
	}
// GET NEIGHBORS ------------------------------------------------------------------------------------------------------------------------------------	 
	protected ArrayList<cell> getListNeighbors ( typeNeighbourhood typeNeighbourhood , int cellX , int cellY ) {
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
			case m_vn : {
				list.addAll(Arrays.asList(		    			
						cells[checkCell(cellX+1,numCellX)][checkCell(cellY-1,numCellY)],				
						cells[checkCell(cellX+1,numCellX)][checkCell(cellY+1,numCellY)],							
						cells[checkCell(cellX-1,numCellX)][checkCell(cellY-1,numCellY)],
						cells[checkCell(cellX-1,numCellX)][checkCell(cellY+1,numCellY)]
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
		if ( !listCell.contains(c))
			listCell.add(c);
	}
 
	public ArrayList<cell> getListCell () {
		return listCell;		
	}
	
	public cell getCell (int X , int Y) {
		return  cells[X][Y]; 
	}
	
	public cell getCell ( double[] coords) {
		try {
			return cells[(int) Math.floor(coords[0])][(int) Math.floor(coords[1])] ;
		} catch (ArrayIndexOutOfBoundsException e) {
			e.printStackTrace();
			return null ;
		}
		}
	
	public cell getCell ( Node n ) {
		double[] coords = GraphPosLengthUtils.nodePosition(n);	
		return cells[(int) Math.floor(coords[0])][(int) Math.floor(coords[1])] ;
	}

	public cell getCell ( seed s ) {
		return cells[(int) Math.floor(s.getX() )][(int) Math.floor(s.getY() )] ;
	}
		
	public double getFeed ( ) {
		return f;
	}
	
	public double getKill ( ) {
		return k;
	}
	
	
}

