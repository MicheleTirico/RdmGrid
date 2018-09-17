package test_grid_01;

import test_grid_01.grid.typeInitialization;

public class layerRd {
	
	private int numberCellX, numberCellY;
	private double sizeCellX, sizeCellY;

	private  double f ,  k ,  Da,  Db ;
	private grid g1 , g2 ;
	
	// constructor
	public layerRd (
			int numberCellX, int numberCellY,
			double sizeCellX, double sizeCellY) {
		
		this.numberCellX = numberCellX;
		this.numberCellY = numberCellY;
		this.sizeCellX = sizeCellX;
		this.sizeCellY = sizeCellY;
		
		g1 = new grid("a",sizeCellX, sizeCellY, numberCellX, numberCellY);
		g2 = new grid("b",sizeCellX, sizeCellY, numberCellX, numberCellY);		
	}
	
// INITIALIZATION GRID ------------------------------------------------------------------------------------------------------------------------------
	
	//initialize grid with val cost
	public void initializeRdCost (String idGrid , double val) {	
		grid g = getGrid(idGrid);		
		g.setValCost(10);
		g.initializeGrid(typeInitialization.cost);
	}

	//initialize grid with val random
	public void initializeRdRandom (String idGrid , int seedRd ,  double minRd , double maxRd) {	
		grid g = getGrid(idGrid);		
		g.setValRandom(seedRd, minRd, maxRd);
		g.initializeGrid(typeInitialization.random);
	}
	
	
	public void updateGs ( ) {
		// get list cell actives
	
		
		
		
	}

// RULES --------------------------------------------------------------------------------------------------------------------------------------------
	
// Gray Scott classic model -------------------------------------------------------------------------------------------------------------------------
	
	// set initial parameters of gray scott model
	public void setGsParams ( double f , double k , double Da, double Db) {
		this.k = k ;
		this.f = f ;
		this.Da = Da ;
		this.Db = Db ;
	}
	
	
	
// GET METHODS --------------------------------------------------------------------------------------------------------------------------------------
	// get grid from id
	private grid getGrid ( String idGrid ) {
		grid g = null ;
		if ( idGrid.equals(g1.getId())) 
			g= g1;	
		else if( idGrid.equals(g2.getId())) 
			g= g2;	
		else  
			System.out.println("grid not difined");		
		return g;
	}
	
	public void test () {
		
	}
	
// PRINT METHODS ------------------------------------------------------------------------------------------------------------------------------------
	// print row
	public void printRow ( String idGrid, int row ) {
		getGrid(idGrid).printValRow(row);
	}

	// print grid
	public void printGrid ( String idGrid) {
		getGrid(idGrid).printGrid();
	}
	

}
