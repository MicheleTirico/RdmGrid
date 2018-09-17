package test_grid_02;

import java.util.ArrayList;
import java.util.Arrays;

class cell{
    private int X;
    private int Y;
    private double val1,val2 ;

    public cell() {
        this(0,0,0,0);
    }        
    
    public cell(int X, int Y , double val1 , double val2) {
        this.X = X;
        this.Y = Y;
        this.val1=val1;
        this.val2=val2;
    }
  
 // GET METHODS -------------------------------------------------------------------------------------------------------------------------------------- 
    public int getX() {
        return X;
    }
    
    public int getY() {
        return Y;
    }
    
    public double getVal1() {
    	return val1;
    }
    
    public double getVal2() {
    	return val2;
    }
   
// SET METHODS --------------------------------------------------------------------------------------------------------------------------------------
    public void setX(int X) {
        this.X = X;
    }
    public void setY(int Y) {
        this.Y = Y;
    }
    public void setVals(double val1, double val2) {
    	this.val1 = val1;
    	this.val2 = val2;
    }
}