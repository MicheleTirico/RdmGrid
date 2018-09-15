package test_grid;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class grid {
	
	private double[][] cells ;
	private  double sizeX, sizeY;
	private  int numberCellsX , numberCellsY ;
	private  String idGrid ;
	public enum typeInitialization { random, cost }

	double valCost ;
	int seedRd ;
	double minRd , maxRd;
		
	// CONSTRUCTOR
	public grid (String idGrid, double sixeX, double sixeY, int numberCellsX , int numberCellsY ) {
		
		cells = new double[numberCellsX][numberCellsY];
		this.idGrid = idGrid ;
		setSizeX(sixeX);
		setSizeY(sixeY);
		setNumberCellsX(numberCellsX);
		setNumberCellsY(numberCellsY);
	}
	
	// initialize methods
	public void initializeGrid (typeInitialization type ) {		
		switch (type) {					
			case cost: {
				for (int x = 0; x<numberCellsX; x++)
					for (int y = 0; y<numberCellsY; y++)
						cells[x][y] = valCost ;
			}	break;
			
			case random:{
				Random rd = new Random( seedRd );
				for (int x = 0; x< numberCellsX; x++)
					for (int y = 0; y< numberCellsY; y++) {
						cells[x][y] = minRd + (maxRd - minRd) * rd.nextDouble() ;
						}		
			}	break;
		}		
	}
	
// RULES --------------------------------------------------------------------------------------------------------------------------------------------
	
	public ArrayList<Double> getVonNeumannNeighbors ( int cellX, int cellY) {	
		return new ArrayList<Double> (Arrays.asList(
				cells[cellX][cellY+1],
				cells[cellX][cellY-1],
				cells[cellX-1][cellY],
				cells[cellX+1][cellY]
						));
	} 
	
	// get list neighbors
	public ArrayList<Double> getMooreNeighbors ( int cellX, int cellY ) {
		return new ArrayList<Double> (Arrays.asList(
		
				cells[cellX+1][cellY-1],
				cells[cellX+1][cellY],
				cells[cellX+1][cellY+1],
		
				cells[cellX][cellY+1],
				cells[cellX][cellY-1],
		
				cells[cellX-1][cellY-1],
				cells[cellX-1][cellY],
				cells[cellX-1][cellY+1]
						));
	}
	
	// get value of cell 
	public double getvalCell (int cellX, int cellY ) {
		if ( cellX > numberCellsX || cellY > numberCellsY) {
			System.out.println("cell out of range");
			return 0 ;
		}
		return 	cells[cellX][cellY];
	}
	
	public String getId () {
		return idGrid;
	}

		
// SET METHODS --------------------------------------------------------------------------------------------------------------------------------------
	
	public void setValCell (double val , int cellX, int cellY ) {
		cells[cellX][cellY] = val ;
	}
	
	private void setSizeX (double x) {
		sizeX = x;
	}
	
	private void setSizeY (double y) {
		sizeY = y;
	}
	
	private void setNumberCellsX (int x) {
		numberCellsX = x;
	}

	private void setNumberCellsY (int y) {
		numberCellsY = y;
	}
	
	public void setValCost(double val) {
		valCost = val;
	}
	
	public void setValRandom(int seedRd, double minRd , double maxRd ) {
		this.seedRd = seedRd ;
		this.minRd = minRd;
		this.maxRd = maxRd;
	}
	
// PRINT METHODS ------------------------------------------------------------------------------------------------------------------------------------	
	public void printValRow ( int row ) {
		System.out.println("\n"+"row: " + row );
		for (int x = 0 ; x < numberCellsX ; x++) {
			System.out.print("("+x+")"+" "+ cells[row][x]+", "); 
		}		
	}
	
	public void printGrid () {
		System.out.println();
		for (int y = 0 ; y < numberCellsY ; y++) {
			printValRow(y);
		System.out.println();	
		}
	}
}
