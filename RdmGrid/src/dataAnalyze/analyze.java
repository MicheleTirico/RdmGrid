package dataAnalyze;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import RdmSeedNet_2.framework;

public class analyze extends framework {

	private static FileWriter fw1, fw2 ,fw3 ;
	public enum indicator {
		seedCount(""),
		gammaIndex("") , 
		averageDegree("") ;
		
		private String id;
		private String path ;
		private FileWriter fw ;
		
		private indicator ( String id ) {
			this.id = id ;
		
		}
		public FileWriter getFw ( ) {
			return fw ;
		}
		public String getId ( ) {
			return id ;
		}
		public void setId ( String id ) {
			this.id = id ;
		}
		
		public void setFw ( String path )throws IOException {
			fw = new FileWriter(path,  true) ;
			this.path = path ;
		}
	}
	
	
	
	protected ArrayList<indicator> listIndicators = new ArrayList<indicator>();
	private indicator indicator ;

}
