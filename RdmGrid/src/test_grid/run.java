package test_grid;

import test_grid.grid.typeInitialization;

public class run {
	
	public static grid g1 = new grid ("1", 1,1,50,50);
	
	public static grid g2 = new grid ("2", 1,1,50,50);
	
	public static void main(String[] args) {
	
		g1.setValCost(0.5);
		g1.initializeGrid(typeInitialization.cost);
		
		g2.setValRandom(15, 0, 1);
		g2.initializeGrid(typeInitialization.random);
		
		g1.printValRow(1);
		g2.printValRow(1);		
	
	}
	


}
