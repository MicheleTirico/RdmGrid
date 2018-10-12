package dataAnalyze;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;

import RdmGsaNetExport.expCsv;
import RdmSeedNet_2.layerNet;
import RdmSeedNet_2.layerSeed;
import scala.Array;

public class analyzeNetwork extends analyze {
	
	private Graph graph = new SingleGraph("grToAn");	
	private String 	header , nameFile , nameFolder , path  ;
	FileWriter fileWriter ;
	
	private handleFolder hF ;
	private String pathNetAn;
	public analyzeNetwork () throws IOException {
		this(null, null, null, null);
	}

	
	public analyzeNetwork(Graph graph, String path , String nameFolder , String nameFile ) throws IOException {
		this.graph = graph ;
		this.path = path;
		this.nameFolder = nameFolder ;
		this.nameFile = nameFile;
		hF = new handleFolder(path) ;
		pathNetAn = hF.createNewGenericFolder(nameFolder);
		
		System.out.println(path);
		fileWriter = new FileWriter( path + nameFile + ".csv" , true );		
		expCsv.addCsv_header( fileWriter, header ) ;
	}
	
	
	public void computeSeedCount (int step) throws IOException {
		header = "testHeader";
		int val = lSeed.getNumSeeds();
		System.out.println(Arrays.asList( Double.toString(step) , Double.toString(val)));
		expCsv.writeLine(fileWriter, Arrays.asList( Double.toString(step) , Double.toString(val) ) , ';' ) ;
	}
	
	public void closeFileWriter () throws IOException {
		fileWriter.close();
	}
	
	private void createCsv ( ) {
		
	}
	
	public void computeIndicators ( ) {
		for ( indicator in : listIndicators ) {
			
			computeIn( in ) ;
			
		}
		
	}
	
	public void computeIn ( indicator in ) {
		
		switch (in) {
		case seedCount:
			
			break;

		default:
			break;
		}
	}
	
// SET METHODS --------------------------------------------------------------------------------------------------------------------------------------
	public void setIndicators (indicator indicator ){	
		listIndicators.add(indicator);
	}
	
	public void setIndicators (ArrayList<indicator> list ) {
		listIndicators.addAll(list) ;
	}

}
