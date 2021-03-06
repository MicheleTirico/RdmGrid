package test_grid_02;

import java.util.ArrayList;

import test_grid_02.layerRd.morphogen;
import test_grid_02.layerRd.typeDiffusion;
import test_grid_02.layerRd.typeNeighbourhood;

public class run {

	static double  f = 0.039, k=0.058, Da = 0.2, Db = 0.1 ;
	
	private static layerRd lRd = new layerRd(1.0, 1.0, 50, 50);
	
	public static void main(String[] args) {

		lRd.initializeCostVal(1,0);
		lRd.setValueOfCell(1, 1, 25, 25);
		
		lRd.setValueOfCell(1, 1, 24, 25);
		lRd.setValueOfCell(1, 1, 26, 25);
		lRd.setValueOfCell(1, 1, 25, 24);
		lRd.setValueOfCell(1, 1, 25, 26);
		
		lRd.setGsParameters(f, k, Da, Db, typeDiffusion.mooreWeigthed);
		
		for ( int t = 0 ; t < 2 ; t++)
			lRd.updateValues();
		
	

		
		
	}

}
