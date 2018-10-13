package dataAnalyze;

import java.io.IOException;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.stream.file.FileSinkDGS;

public class storeNetwork extends analyze {
	
	private boolean run ;
	private Graph graph = new SingleGraph("grToAn");	
	private handleFolder hF ;
	private String 	header , nameFile  , path , pathNetStore ,
		nameFileStart , nameFileStep ,
					pathStartDSG , pathStepDSG ;
	
	private static FileSinkDGS fsd = new FileSinkDGS();
	
	public storeNetwork ( ) throws IOException {
		this(false ,null, null, null, null);
	}

	public storeNetwork (boolean run , Graph graph, String path00 , String nameFolder , String nameFile ) throws IOException {
		this.run = run ;
		this.graph = graph ;
		System.out.println(path00);
		this.path = path00 +"\\"+ nameFolder + "\\";
		System.out.println(path);
		this.nameFile = nameFile;
		hF = new handleFolder(path) ;
		pathNetStore = hF.createNewGenericFolder(nameFolder);
		nameFileStart = nameFile + "Start.dsg";
		nameFileStep = nameFile + "Step.dsg";
		pathStartDSG = path + nameFileStart ;
		pathStepDSG = path + nameFileStep ;
	}
	
	public void initStore ( ) throws IOException {
	
		if ( run ) {
			graph.write(fsd, pathStartDSG);
			graph.addSink(fsd);
			fsd.begin(pathStepDSG);
		}
	}
	
	public void storeDSGStep ( int t ) {
		if ( run )
			graph.stepBegins(t);		
	}
	
	public void closeStore ( ) throws IOException {
		if ( run )
			fsd.end();
	}
}
