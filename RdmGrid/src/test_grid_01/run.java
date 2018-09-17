package test_grid_01;

public class run {
		
	private static double  f = 05, 
		k = 0.4,
		Da = 0.1, 
		Db = 0.2;
	
	public static layerRd rd = new layerRd(50, 50, 1, 1);
	
	public static void main(String[] args) {
		
		rd.initializeRdCost("a", 0);
		rd.initializeRdCost("b", 0);
		
		rd.setGsParams(f, k, Da, Db);
		rd.test();
		
	}
		



}
