package RdmSeedNet_2;

public class seed {
	private double 	X, Y,
					vecX, vecY;
	
	public seed() {
		this(0,0,0,0);
	}

	public seed(double X, double Y, double vecX, double vecY) {
		this.X = X ;
		this.Y = Y ;
		this.vecX = vecX ;
		this.vecY = vecY ;
	}
// GET METHODS -------------------------------------------------------------------------------------------------------------------------------------- 
    public double getX() {
        return X;
    }
    
    public double getY() {
        return Y;
    }
    
    public double getVecX() {
    	return vecX;
    }
    
    public double getVecY() {
    	return vecY;
    }
   
// SET METHODS --------------------------------------------------------------------------------------------------------------------------------------
    public void setX(double X) {
        this.X = X;
    }
    public void setY(double Y) {
        this.Y = Y;
    }
  
    public void setVecX ( double vecX ) {
    	this.vecX = vecX ;
    }
    
    public void setVecY ( double vecY ) {
    	this.vecY = vecY ;
    }
    
    public void setVec ( double vecX , double vecY ) {
    	this.vecY = vecY ;
    	this.vecX = vecX ;   	
    }  
}
