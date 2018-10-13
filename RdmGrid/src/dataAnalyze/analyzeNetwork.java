package dataAnalyze;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.graphstream.algorithm.Toolkit;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;

import RdmGsaNetExport.expCsv;
import RdmSeedNet_2.layerNet;
import RdmSeedNet_2.layerSeed;
import scala.Array;

public class analyzeNetwork extends analyze {
	
	private Graph graph = new SingleGraph("grToAn");	
	private String 	header , nameFile  , path , pathNetAn ;
	private FileWriter fileWriter ;
	private handleFolder hF ;
	private boolean run ; 
	
	public analyzeNetwork () throws IOException {
		this(false ,null, null, null, null);
	}
	
	public analyzeNetwork(boolean run ,Graph graph, String path , String nameFolder , String nameFile ) throws IOException {
		this.run = run ;
		this.graph = graph ;
		this.path = path +"\\"+ nameFolder + "\\";
		this.nameFile = nameFile;
		hF = new handleFolder(path) ;
		pathNetAn = hF.createNewGenericFolder(nameFolder);
	}
	
	public void test ( ) throws IOException {	
	}
	
	public void initAnalysis ( ) throws IOException {
		if ( run )
			for ( indicator in : listIndicators ) {
				in.setId(in.toString());
				in.setFw(path +nameFile+"_"+ in + ".csv" );
				header = in.toString();
				expCsv.addCsv_header( in.getFw(), header ) ;
			}
	}
		
// COMPUTE SINGLE INDICATOR -------------------------------------------------------------------------------------------------------------------------
	public void computeSeedCount (int step) throws IOException {
		indicator in = indicator.seedCount ;
		fileWriter = in.getFw() ;
		double val = getValIndicator(in) ;
		expCsv.writeLine(fileWriter, Arrays.asList( Double.toString(step) , Double.toString(val) ) , ';' ) ;
	}
	
	public void computeAverageDegree (int step) throws IOException {
		indicator in = indicator.averageDegree ;
		fileWriter = in.getFw() ;
		double val = getValIndicator(in) ;
		expCsv.writeLine(fileWriter, Arrays.asList( Double.toString(step) , Double.toString(val) ) , ';' ) ;
	}
	
	public void computeGammaIndex (int step) throws IOException {
		indicator in = indicator.gammaIndex ;
		fileWriter = in.getFw() ;
		double val = getValIndicator(in) ;
		expCsv.writeLine(fileWriter, Arrays.asList( Double.toString(step) , Double.toString(val) ) , ';' ) ;
	}
		
	// compute all indicators
	public void computeIndicators ( int t) throws IOException {
		if ( run ) 
			for ( indicator in : listIndicators ) {
				fileWriter = in.getFw() ;
				double val = getValIndicator(in) ;
				expCsv.writeLine(fileWriter, Arrays.asList( Double.toString(t) , Double.toString(val) ) , ';' ) ;		
			}		
	}
	
	// close file writer
	public void closeFileWriter () throws IOException {
		if  ( run ) 
			for ( indicator in : listIndicators)
				in.getFw().close();
	}

	public double getValIndicator ( indicator in ) {
		double val = 0;
		switch (in) {
		case seedCount: 
			val = lSeed.getNumSeeds();
			break;
		case averageDegree :
			val = Toolkit.averageDegree(graph); ;
			break ;
		case gammaIndex:
			val = getGammaIndex(graph, true) ;
			break;		
		}
		return val ;
	}
	
// SET METHODS --------------------------------------------------------------------------------------------------------------------------------------
	public void setIndicators (indicator indicator ){	
		listIndicators.add(indicator);
	}
	
	public void setIndicators (ArrayList<indicator> list ) {
		listIndicators.addAll(list) ;
	}
// COMPUTE INDICATORS -------------------------------------------------------------------------------------------------------------------------------
	// gamma index
	private static double getGammaIndex ( Graph graph , boolean isPlanar ) {	
		double  n = graph.getNodeCount() , 
				e = graph.getEdgeCount() ,
				eMax = 0 ;
		
		if ( isPlanar )
			eMax = 3 * n - 6 ; 
		else 
			eMax = ( n - 1 ) * n / 2 ;
		
		if ( eMax == 0 || e == 0)	
			return 0 ;
		else 
			return e / eMax ;
	}
}
