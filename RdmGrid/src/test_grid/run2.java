package test_grid;

public class run2 {
		
	
	public static layerRd rd = new layerRd(50, 50, 1, 1);
	
	public static void main(String[] args) {
		
		rd.initializeRdCost("a", 0);
		rd.initializeRdCost("b", 0);
		
		rd.test();
		
	}
		



}
