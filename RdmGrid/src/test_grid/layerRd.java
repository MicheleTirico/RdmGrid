package test_grid;

import javax.swing.text.GapContent;

import test_grid.grid.typeInitialization;

public class layerRd{
	
	private int numberCellX, numberCellY;
	private double sizeCellX, sizeCellY;

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
	
// initialization grid ------------------------------------------------------------------------------------------------------------------------------
	public void initializeRdCost (String idGrid , double val) {	
		grid g = getGrid(idGrid);		
		g.setValCost(10);
		g.initializeGrid(typeInitialization.cost);
	}
	
	public void initializeRdRandom (String idGrid , int seedRd ,  double minRd , double maxRd) {	
		grid g = getGrid(idGrid);		
		g.setValRandom(seedRd, minRd, maxRd);
		g.initializeGrid(typeInitialization.random);
	}
	
// GET METHODS --------------------------------------------------------------------------------------------------------------------------------------
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
	

	
}
