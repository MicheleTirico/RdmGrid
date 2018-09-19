package RdmSeedNet_2;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.graphicGraph.GraphPosLengthUtils;

public class layerNet extends framework {

	private int numNodes , a ;
	private double radius;
	
	public enum typeSetupLayer { circle , test }
	private static typeSetupLayer typeSetupLayer ;
	
	private Graph graph = new SingleGraph ( "net" );
	public layerNet () {
		this( 0 ) ;
	}
	
	public layerNet( int a ) {
		this.a = a ;
	}
	
	public void setParametersCircle ( int numNodes, double radius ) {
		this.numNodes = numNodes ;
		this.radius = radius ;
	}
	
	
	public void initLayer ( typeSetupLayer  typeSetupLayer ) {
		layerNet.typeSetupLayer = typeSetupLayer ;
		
		switch (typeSetupLayer) {
			case circle:	
				createNetCircle();				
				break;
			default:
				break;	
		}
	}
	
	public void updateLayer () {
	//	graph.display(false);
		System.out.println("numberNodes "+ graph.getNodeCount());
		System.out.println("numberSeeds "+ lSeed.getListSeeds().size());
		
		for ( seed s : lSeed.getListSeeds() ) {
			
			Node oldNode = s.getNode();	
			
			idNode = Integer.toString(idNodeInt)  ;
			graph.addNode(idNode ) ; 
			Node newNode = graph.getNode(idNode ) ;
			
	//		System.out.println("node " + pos[0] + " " + pos[1]);
	//		System.out.println("seed " + s.getX() + " " + s.getY());
			newNode.setAttribute("xyz", s.getX()+ s.getVecX(), s.getY()+s.getVecY() , 0);
		
			idEdge = Integer.toString(idEdgeInt+1 );
			graph.addEdge(idEdge, oldNode, newNode) ;
			
			s.setNode(newNode);
			idNodeInt++;
			idEdgeInt++;
		}
	}
	
	public void updateLayerAndSeeds ( ) {
		
		System.out.println("numberNodes "+ graph.getNodeCount());
		System.out.println("numberSeeds "+ lSeed.getListSeeds().size());
		
		for ( seed s : lSeed.getListSeeds() ) {
			
			Node oldNode = s.getNode();	
			
			idNode = Integer.toString(idNodeInt)  ;
			graph.addNode(idNode ) ; 
			Node newNode = graph.getNode(idNode ) ;
			
			double[] vec = lSeed.getVector(s);
			
			s.setVec(vec[0], vec[1]);	
			s.setCoords(s.getX() + vec[0] , s.getY() + vec[1]);	
			
			newNode.setAttribute("xyz", s.getX()+ vec[0], s.getY() + vec[1] , 0);
			idEdge = Integer.toString(idEdgeInt+1 );
			graph.addEdge(idEdge, oldNode, newNode) ;
			
			s.setNode(newNode);
			idNodeInt++;
			idEdgeInt++;
		}
	}
	
	
	private void addNode( seed s ) {
		idNode =  Integer.toString(idNodeInt) ;
		graph.addNode(idNode);
		graph.getNode(idNode).addAttribute("xyz", s.getX(), s.getY() , 0 );
		idNodeInt++;	
	}

	public void removeNode () {
		
	}
	
	public void addEdge () {
		
	}
	
	public void removeEdge () {
		
	}
	
	
// SETUP LAYER --------------------------------------------------------------------------------------------------------------------------------------
	private void createNetCircle () {
		
		double[] centerLayerRd = lRd.getCenter () ;
		double 	centerX = centerLayerRd[0] , 
				centerY = centerLayerRd[1] ,
				angle = 2 * Math.PI / numNodes ;		
		for ( idNodeInt = 0 ; idNodeInt < numNodes ; idNodeInt++ ) {
			
			double 	coordX = radius * Math.cos( idNodeInt * angle ) ,
					coordY = radius * Math.sin( idNodeInt * angle ) ;
					
			idNode =  Integer.toString(idNodeInt) ;
			graph.addNode(idNode) ;
			graph.getNode(idNode).addAttribute("xyz", centerX + coordX ,  centerY + coordY , 0 );
		}
		
		for ( idEdgeInt = 0 ; idEdgeInt < numNodes ; idEdgeInt++ ) {
			String idEdge = Integer.toString(idEdgeInt);
			try {		
				graph.addEdge(idEdge,Integer.toString(idEdgeInt) , Integer.toString(idEdgeInt+1) ) ;		
			}
			catch (org.graphstream.graph.ElementNotFoundException e) {
				graph.addEdge(idEdge,Integer.toString(idEdgeInt) , Integer.toString(0) ) ;
				break ; 
			}
		}
	}
	
	public Graph getGraph () {
		return graph;
	}
	
	public void display ( boolean display ) {
		if ( display)
			graph.display(false) ;
	}
	
	
	

	
}
