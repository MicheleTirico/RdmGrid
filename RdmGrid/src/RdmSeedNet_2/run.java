package RdmSeedNet_2;

import java.util.ArrayList;

import RdmSeedNet_2.layerRd.typeDiffusion;

public class run extends framework {

	static double  f = 0.039, k=0.058, Da = 0.2, Db = 0.1 ;
	
	static double g = 1, alfa = 2 , Ds = .1 , r = 2;
	
	public static void main(String[] args) {

		lRd = new layerRd(1.0, 1.0, 50, 50);
		lSeed = new layerSeed(g, alfa, Ds, r);
		
		lRd.initializeCostVal(1,0);
		lRd.setValueOfCell(1, 1, 25, 25);
		
		lRd.setValueOfCell(1, 1, 24, 25);
		lRd.setValueOfCell(1, 1, 26, 25);
		lRd.setValueOfCell(1, 1, 25, 24);
		lRd.setValueOfCell(1, 1, 25, 26);
		
		lRd.setGsParameters(f, k, Da, Db, typeDiffusion.mooreWeigthed);
	
		lSeed.initializationSeedThMorp(morphogen.b, 0.5, 1.0);
		
		System.out.println(lSeed.getListSeeds());
		
	//	lSeed.createSeed(25, 25);

		for ( int t = 0 ; t < 30 ; t++) {
			System.out.println(lSeed.getListSeeds());
			
			System.out.println("------------- step " +t);
			lRd.updateLayer();
			lSeed.updateLayer();
		}
	
	

		
		
	}

}
