package RdmSeedNet;

import java.util.ArrayList;

import RdmSeedNet.layerRd.typeDiffusion;

public class run extends framework {

	static double  f = 0.039, k=0.058, Da = 0.2, Db = 0.1 ;
	
	public static void main(String[] args) {

		lRd = new layerRd(1.0, 1.0, 50, 50);
		
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
