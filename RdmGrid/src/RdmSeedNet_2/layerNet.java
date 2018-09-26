package RdmSeedNet_2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.graphstream.algorithm.Dijkstra;
import org.graphstream.algorithm.Dijkstra.Element;
import org.graphstream.graph.Edge;
import org.graphstream.graph.EdgeRejectedException;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.Path;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.graphicGraph.GraphPosLengthUtils;


public class layerNet extends framework {

	private boolean setLengthEdges ;
	private int numNodes  ;
	private double radius;
	private String id ; 
	public enum typeSetupLayer { circle , test }
	private static typeSetupLayer typeSetupLayer ;
	
	private Graph graph = new SingleGraph ( "net" );
	public layerNet () {
		this( null ) ;
	}
	
	public layerNet( String id) {
		this.id = id ;
	}
	
	public void setLengthEdges ( String attr , boolean setLengthEdges) {
		this.setLengthEdges = setLengthEdges ;
		if ( setLengthEdges ) 
			for ( Edge e : graph.getEachEdge() ) 
				e.addAttribute("length", getLength(e));	
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
	
	public void updateLayer () {	System.out.println("numberNodes "+ graph.getNodeCount() +"\n"+"numberSeeds "+ lSeed.getListSeeds().size());
		
		for ( seed s : lSeed.getListSeeds() ) {	
			Node oldNode = s.getNode();			
			idNode = Integer.toString(idNodeInt)  ;
			graph.addNode(idNode ) ; 
			Node newNode = graph.getNode(idNode ) ;
			newNode.setAttribute("xyz", s.getX()+ s.getVecX(), s.getY()+s.getVecY() , 0);
			idEdge = Integer.toString(idEdgeInt+1 );
			graph.addEdge(idEdge, oldNode, newNode) ;
			s.setNode(newNode);
			idNodeInt++;
			idEdgeInt++;
		}
	}
	
	public void test ( ) {
		for ( Node n : graph.getEachNode()) {
			bks.putNode(n);
		}
		System.out.println(bks.getListBucketNotEmpty().size() ) ;
		
	}
	
	public void updateLayerAndSeeds3 () {	System.out.println("numberNodes "+ graph.getNodeCount() +"\n"+"numberSeeds "+ lSeed.getListSeeds().size());
	
		ArrayList<seed> listSeedsToRemove  = new ArrayList<seed> (); 
		ArrayList<Edge> listNewEdges = new ArrayList<Edge>() ;
				
		for ( seed s : lSeed.getListSeeds() ) {
			
			// get node seed
			Node nodeS = s.getNode();	
			double[] coordNodeS = GraphPosLengthUtils.nodePosition(nodeS) ;
						
			// get cooord of future node
			double[] vec = lSeed.getVector(s, typeVectorField.gravity);	
			double intenVec = s.getIntenVec();
			double[] coordNodeF = new double[] {s.getX()+ vec[0], s.getY() + vec[1]};
			
			// check edge X with graph
			ArrayList<Node> listNearestNodesNs = bks.getNodesInRadius(nodeS, 1);
			ArrayList<Edge> listEdges = getListEdgewhitNodeInList( listNearestNodesNs );	
			ArrayList<Edge> listXEdges = getListEdgeXInList(coordNodeS,coordNodeF, listEdges) ;
		
			if ( listXEdges.isEmpty() ) {
				// create node if no X Edges
			//	System.out.println(listXEdges);
				idNode = Integer.toString(idNodeInt)  ;
				graph.addNode( idNode ) ; 
				Node nodeF = graph.getNode(idNode ) ;
				
				s.setVec(vec[0], vec[1]);	
				s.setCoords(s.getX() + vec[0] , s.getY() + vec[1]);	
				
				// set coordinates of new node
				nodeF.setAttribute("xyz", s.getX()+ vec[0], s.getY() + vec[1] , 0);
				
				// create edge
				idEdge = Integer.toString(idEdgeInt+1 );
				Edge e = graph.addEdge(idEdge, nodeS, nodeF) ;
				listNewEdges.add(e);
				// link seed to node
				s.setNode(nodeF);
				idNodeInt++;
				idEdgeInt++;
			}
			else {	//	System.out.println("not empty");
				System.out.println(listXEdges);
				
				listSeedsToRemove.add(s);
				ArrayList <Node> nearestNodesDist = new ArrayList <Node> (getMapNodeDist(nodeS, listNearestNodesNs, 5).keySet() );
				for ( Node nearest : nearestNodesDist ) {
					
					double[] coordNearest = GraphPosLengthUtils.nodePosition(nearest);
					ArrayList<Edge> listXEdges2 = getListEdgeXInList(coordNodeS,coordNearest, listEdges) ;//	System.out.println(listEdges);
					if (listXEdges2.isEmpty()) {
						try {
							// create edge
							idEdge = Integer.toString(idEdgeInt+1 );
							Edge e = graph.addEdge(idEdge, nodeS, nearest) ;
							listNewEdges.add(e);
							idEdgeInt++;
							break ;
						}
						catch (EdgeRejectedException e) {
							// TODO: handle exception
						}
					} 
				}
			}
			
		
		}
		
		if ( ! listSeedsToRemove.isEmpty() )
			System.out.println(listSeedsToRemove);
		for ( seed s :listSeedsToRemove)
			lSeed.removeSeed(s);
		
		for (Edge e : listNewEdges ) {
			Node n0 = e.getNode0(), n1 = e.getNode1();
			
			ArrayList<Edge> listEdgeNeig = new ArrayList<Edge>(getListEdgeNeighbor(n0, .1)) ;
			listEdgeNeig.addAll( getListEdgeNeighbor(n1, .1));

			ArrayList<Edge> listXEdges3 = getListEdgeXInList( e, listEdgeNeig) ;
			if ( ! listXEdges3.isEmpty() ) {
		//		graph.removeEdge(e);
			}
		}	
		
	
//			System.out.println(listEdgeNeig);
//			ArrayList<Edge> listXEdges3 = getListEdgeXInList( e, listEdgeNeig) ;
//			for ( Edge ed : listXEdges3 )
//				graph.removeEdge(ed);		
		
	}
	
	public void updateLayerAndSeeds4 () {	System.out.println("numberNodes "+ graph.getNodeCount() +"\n"+"numberSeeds "+ lSeed.getListSeeds().size());
	
		ArrayList<seed> listSeedsToRemove  = new ArrayList<seed> (); 
		
		for ( seed s : lSeed.getListSeeds()  ) {
			
			// get old node
			Node nodeS = s.getNode();
			double[] coordNodeS = GraphPosLengthUtils.nodePosition(nodeS) ;
			
			// compute future node
			double[] vec = lSeed.getVector(s, typeVectorField.gravity);		//	System.out.println(vec[0] + " " + vec[1] );
			double[] coordNodeF = new double[]{s.getX()+ vec[0], s.getY() + vec[1] };
			s.setVec(vec[0], vec[1]);	
			
			// check edge X with graph
			ArrayList<Node> listNearestNodes = bks.getNodesInRadius(nodeS, 1);
			ArrayList<Edge> listEdges = getListEdgewhitNodeInList( listNearestNodes );	
			ArrayList<Edge> listXEdges = getListEdgeXInList(coordNodeS,coordNodeF, listEdges) ;//	System.out.println(listEdges);
			
			if ( ! listXEdges.isEmpty() ) {
				System.out.println(getMapNodeDist(nodeS , listNearestNodes, 5 ));
			//	System.out.println(nodeS + " " + getMapNodeDist(nodeS , listNearestNodes, 5 ).keySet());
				
				for (Node nearest :  getMapNodeDist(nodeS , listNearestNodes, 3 ).keySet()) {
					double[] coordNearest = GraphPosLengthUtils.nodePosition(nearest) ;

					
					ArrayList<Edge> listXEdges2 = getListEdgeXInList(coordNodeS,coordNearest, listEdges) ;//	System.out.println(listEdges);
					System.out.println(listXEdges2);
					if( listXEdges2.isEmpty()) {
						try {
							idEdge = Integer.toString(idEdgeInt + 1 );
							graph.addEdge(idEdge, nodeS, nearest) ;
							
							if( !  listSeedsToRemove.contains(s) )
								listSeedsToRemove.add(s); 
							
							System.out.println(idEdge +" " + nearest + " " + nodeS);
							idEdgeInt++;
							break ;
						}
						catch (EdgeRejectedException e) {
							continue;
						}
					}				
				}		
			}
			
			// create new node
			idNode = Integer.toString(idNodeInt)  ;
			graph.addNode( idNode ) ; 
			Node nodeF = graph.getNode(idNode ) ;			
			nodeF.setAttribute("xyz", s.getX()+ vec[0], s.getY() + vec[1] , 0);
			s.setCoords(s.getX() + vec[0] , s.getY() + vec[1]);	
			bks.putNode(nodeF);
			idNodeInt++;
				
			// create edge
			idEdge = Integer.toString(idEdgeInt +1 );
			graph.addEdge(idEdge, nodeS , nodeF) ;
			s.setNode(nodeF);
			idEdgeInt++;
	
			// get radius
			double intenVec = s.getIntenVec() ;
			listNearestNodes = bks.getNodesInRadius(nodeF, intenVec);	//		System.out.println(intenVec);
			/*
			// check nodes in radius
			if ( ! listNearestNodes.isEmpty()) {
				for ( Node nearest : listNearestNodes) {
					try {		//	System.out.println(nearest +" " + nodeF +  " "+ listNearestNodes);
						
						double[] coordNodeNearest = GraphPosLengthUtils.nodePosition(nodeS) ;
						listEdges = getListEdgeNeighbor( nodeF , intenVec);
						listXEdges = getListEdgeXInList(coordNodeF , coordNodeNearest, listEdges) ;//	System.out.println(listEdges);
					
						if ( ! listXEdges.isEmpty()) {
							continue;
						}
						
						idEdge = Integer.toString(idEdgeInt + 1 );
						graph.addEdge(idEdge, nodeF, nearest) ;
						if( !  listSeedsToRemove.contains(s) )
							listSeedsToRemove.add(s);
					//	listSeeds = lSeed.getListSeeds()  ; 
						idEdgeInt++;
					} catch (EdgeRejectedException e) { 	//	e.printStackTrace();
						continue ;
					}
				}
			}			
			 */
		}
		
		for ( seed s : listSeedsToRemove) {
			lSeed.removeSeed(s);
		}
	}
	
	public void updateLayerAndSeeds6 () {	System.out.println("numberNodes "+ graph.getNodeCount() +"\n"+"numberSeeds "+ lSeed.getListSeeds().size());
	
	ArrayList<seed> listSeedsToRemove  = new ArrayList<seed> (); 
	
	for ( seed s : lSeed.getListSeeds()  ) {	//		System.out.println(s.getX() + " " + s.getY() + " " + s.getVecX() + " " + s.getVecY() + " " + s.getIntenVec() + " " +s.getNode());
		// get old node
		Node nodeS = s.getNode();
		double[] coordNodeS = GraphPosLengthUtils.nodePosition(nodeS) ;
		double[] vec = lSeed.getVector(s, typeVectorField.gravity);		
		s.setVec(vec[0], vec[1]);
		
		double[] coordNodeF = new double[]{s.getX()+ vec[0], s.getY() + vec[1] };
		
		double intenVec = s.getIntenVec() ;
		Node nodeF = null ;
		
		if ( intenVec > 0.05 ) {
			// check edge X with graph
		//	ArrayList<Node> listNearestNodes = bks.getNodesInRadius(new double[] {s.getX()+vec[0]/2 , s.getVecY()+ vec[1]/2 }, s.getIntenVec() / 2  );
			ArrayList<Node> listNearestNodes = bks.getNodesInRadius(nodeS, 5 ) ;
			ArrayList<Edge> listEdges = getListEdgewhitNodeInList(listNearestNodes);
			ArrayList<Edge> listXEdges = getListEdgeXInList(coordNodeS,coordNodeF, listEdges) ;//	System.out.println(listEdges);
			
			Edge e = null;	
			if ( ! listXEdges.isEmpty() ) {
				ArrayList <Node> nearestNodesDist = new ArrayList <Node> (getMapNodeDist(nodeS, bks.getNodesInRadius(nodeS, 1), 5).keySet() );			
				for (Node nearest : nearestNodesDist ) {
					ArrayList<Node> listNearestNodes2 = bks.getNodesInRadius(nodeS, 1 );
					ArrayList<Edge> listEdges2 = getListEdgewhitNodeInList(listNearestNodes2);
					ArrayList<Edge> listXEdges2 = getListEdgeXInList(nearest, nodeS, listEdges2) ;//	System.out.println(listEdges);
					
					if ( listXEdges2.isEmpty() ) {
						try {
							idEdge = Integer.toString(idEdgeInt +1 );
							e = graph.addEdge(idEdge, nodeS, nearest) ;
							listSeedsToRemove.add(s);
							idEdgeInt++;
							break ;
						} catch (EdgeRejectedException exception) {
				
						}
					}
				}
			}
			
			if ( e != null  ) {	//		System.out.println(getLength(e) + " " + s.getIntenVec());
				listSeedsToRemove.add(s);				
				continue ;
			}
			
		
			// create new node
			idNode = Integer.toString(idNodeInt)  ;
			nodeF = graph.addNode( idNode ) ; 		
			nodeF.setAttribute("xyz", s.getX()+ vec[0], s.getY() + vec[1] , 0);
			s.setCoords(s.getX() + vec[0] , s.getY() + vec[1]);	
			s.setNode(nodeF);
			bks.putNode(nodeF);
			idNodeInt++;
				
			// create edge
			idEdge = Integer.toString( idEdgeInt+1 );
			graph.addEdge(idEdge, nodeS , nodeF) ;
			s.setNode(nodeF);
			idEdgeInt++;
		}
		else {
			System.out.println(s.getIntenVec());
			nodeS.setAttribute("xyz", s.getX()+ vec[0], s.getY() + vec[1] , 0); 
			s.setCoords(s.getX() + vec[0] , s.getY() + vec[1]);	
			
			ArrayList<Node> listNearestNodes = bks.getNodesInRadius(nodeS, 5 ) ;
			ArrayList<Edge> listEdges = getListEdgewhitNodeInList(listNearestNodes);
			ArrayList<Edge> listXEdges = getListEdgeXInList(coordNodeS,coordNodeF, listEdges) ;//	System.out.println(listEdges);
			
			Edge e = null;	
			if ( ! listXEdges.isEmpty() ) {
				ArrayList <Node> nearestNodesDist = new ArrayList <Node> (getMapNodeDist(nodeS, bks.getNodesInRadius(nodeS, 1), 5).keySet() );			
				for (Node nearest : nearestNodesDist ) {
					ArrayList<Node> listNearestNodes2 = bks.getNodesInRadius(nodeS, 1 );
					ArrayList<Edge> listEdges2 = getListEdgewhitNodeInList(listNearestNodes2);
					ArrayList<Edge> listXEdges2 = getListEdgeXInList(nearest, nodeS, listEdges2) ;//	System.out.println(listEdges);
					
					if ( listXEdges2.isEmpty() ) {
						try {
							idEdge = Integer.toString(idEdgeInt +1 );
							e = graph.addEdge(idEdge, nodeS, nearest) ;
							listSeedsToRemove.add(s);
							idEdgeInt++;
							break ;
						} catch (EdgeRejectedException exception) {
				
						}
					}
				}
			}
			
			if ( e != null  ) {	//		System.out.println(getLength(e) + " " + s.getIntenVec());
				listSeedsToRemove.add(s);				
				continue ;
			}
			
		}
	}
	for ( seed s :listSeedsToRemove)
		lSeed.removeSeed(s);
	
}

	public void updateLayerAndSeeds5 () {	System.out.println("numberNodes "+ graph.getNodeCount() +"\n"+"numberSeeds "+ lSeed.getListSeeds().size());
	
		ArrayList<seed> listSeedsToRemove  = new ArrayList<seed> (); 
		
		for ( seed s : lSeed.getListSeeds()  ) {	//		System.out.println(s.getX() + " " + s.getY() + " " + s.getVecX() + " " + s.getVecY() + " " + s.getIntenVec() + " " +s.getNode());
			// get old node
			Node nodeS = s.getNode();
			double[] coordNodeS = GraphPosLengthUtils.nodePosition(nodeS) ;
			double[] vec = lSeed.getVector(s, typeVectorField.gravity);		
			s.setVec(vec[0], vec[1]);
			
			double[] coordNodeF = new double[]{s.getX()+ vec[0], s.getY() + vec[1] };
			
			// check edge X with graph
		//	ArrayList<Node> listNearestNodes = bks.getNodesInRadius(new double[] {s.getX()+vec[0]/2 , s.getVecY()+ vec[1]/2 }, s.getIntenVec() / 2  );
			ArrayList<Node> listNearestNodes = bks.getNodesInRadius(nodeS, 5 ) ;
			ArrayList<Edge> listEdges = getListEdgewhitNodeInList(listNearestNodes);
			ArrayList<Edge> listXEdges = getListEdgeXInList(coordNodeS,coordNodeF, listEdges) ;//	System.out.println(listEdges);
			
			Edge e = null;	
			if ( ! listXEdges.isEmpty() ) {
				ArrayList <Node> nearestNodesDist = new ArrayList <Node> (getMapNodeDist(nodeS, bks.getNodesInRadius(nodeS, 1), 5).keySet() );			
				for (Node nearest : nearestNodesDist ) {
					ArrayList<Node> listNearestNodes2 = bks.getNodesInRadius(nodeS, 1 );
					ArrayList<Edge> listEdges2 = getListEdgewhitNodeInList(listNearestNodes2);
					ArrayList<Edge> listXEdges2 = getListEdgeXInList(nearest, nodeS, listEdges2) ;//	System.out.println(listEdges);
					
					if ( listXEdges2.isEmpty() ) {
						try {
							idEdge = Integer.toString(idEdgeInt +1 );
							e = graph.addEdge(idEdge, nodeS, nearest) ;
		//					if ( s.getIntenVec() > .1) 
//								System.out.println(s.getIntenVec());
								listSeedsToRemove.add(s);
							idEdgeInt++;
							break ;
						} catch (EdgeRejectedException exception) {
					//		System.out.println(e + " " + nearest + " " + nodeS );
					//		System.out.println(nodeS + " " +nodeS.getEdgeSet());
					//		System.out.println(nearest + " " + nearest.getEdgeSet());
							
					//		exception.printStackTrace();
	//						break ;
						}
					}
				}
			}
			
			if ( e != null  ) {			
				System.out.println(getLength(e) + " " + s.getIntenVec());
				// System.out.println(e + " " + listXEdges);				
			//	if ( s.getIntenVec() > .1) 
					listSeedsToRemove.add(s);				
				continue ;
			}
			
			// create new node
			idNode = Integer.toString(idNodeInt)  ;
			Node nodeF = graph.addNode( idNode ) ; 		
			nodeF.setAttribute("xyz", s.getX()+ vec[0], s.getY() + vec[1] , 0);
			s.setCoords(s.getX() + vec[0] , s.getY() + vec[1]);	
			s.setNode(nodeF);
			bks.putNode(nodeF);
			idNodeInt++;
				
			// create edge
			idEdge = Integer.toString( idEdgeInt+1 );
			graph.addEdge(idEdge, nodeS , nodeF) ;
			s.setNode(nodeF);
			idEdgeInt++;
		}
		for ( seed s :listSeedsToRemove)
			lSeed.removeSeed(s);
		
	}

	public void updateLayerAndSeeds7 () {	System.out.println("numberNodes "+ graph.getNodeCount() +"\n"+"numberSeeds "+ lSeed.getListSeeds().size());
	
	ArrayList<seed> listSeedsToRemove  = new ArrayList<seed> (); 
	
	for ( seed s : lSeed.getListSeeds()  ) {	//		System.out.println(s.getX() + " " + s.getY() + " " + s.getVecX() + " " + s.getVecY() + " " + s.getIntenVec() + " " +s.getNode());
		// get old node
		Node nodeS = s.getNode();
		double[] coordNodeS = GraphPosLengthUtils.nodePosition(nodeS) ;
		double[] vec = lSeed.getVector(s, typeVectorField.gravity);		
		s.setVec(vec[0], vec[1]);
		
		double[] coordNodeF = new double[]{s.getX()+ vec[0], s.getY() + vec[1] };
		
		// check edge X with graph
		ArrayList<Node> listNearestNodes = bks.getNodesInRadius(nodeS, 1 ) ;
		ArrayList<Edge> listEdges = getListEdgewhitNodeInList(listNearestNodes);
		ArrayList<Edge> listXEdges = getListEdgeXInList(coordNodeS,coordNodeF, listEdges) ;//	System.out.println(listEdges);
		double testDist = 0 ;
		Edge e = null;	
		if ( ! listXEdges.isEmpty() ) {
			ArrayList <Node> nearestNodesDist = new ArrayList <Node> (getMapNodeDist(nodeS, bks.getNodesInRadius(nodeS, 1), 5).keySet() );			
			for (Node nearest : nearestNodesDist ) {
				ArrayList<Node> listNearestNodes2 = bks.getNodesInRadius(nodeS, 1 );
				ArrayList<Edge> listEdges2 = getListEdgewhitNodeInList(listNearestNodes);
				ArrayList<Edge> listXEdges2 = getListEdgeXInList(nearest, nodeS, listEdges2) ;//	System.out.println(listEdges);
				
				if ( listXEdges2.isEmpty() ) {
					try {
						idEdge = Integer.toString(idEdgeInt +1 );
						e = graph.addEdge(idEdge, nodeS, nearest) ;
					//	listSeedsToRemove.add(s);
						testDist = getDistGeom(nodeS, getNearestNode(nodeS, listNearestNodes));
						idEdgeInt++;
						break ;
					} catch (EdgeRejectedException exception) {
					}
				}
			}
		}
		
		if ( e != null && getLength(e) >= testDist ) {			
			listSeedsToRemove.add(s);				
			continue ;
		}
		
		// create new node
		ArrayList<Node> listNeig = new ArrayList<Node>(getListNeighbors(nodeS));
		boolean test = false ;
		for ( Node n0 : listNeig) {
			double dist_n0_nodeS = getDistGeom(nodeS, n0) ;
			if ( dist_n0_nodeS < Math.min(getDistGeom(coordNodeF, coordNodeS) , .5 ) ) {
				nodeS.setAttribute("xyz", s.getX()+ vec[0], s.getY() + vec[1] , 0);
				test = true ;
				break ;
			}
		}
//		if (test == false ) {
			idNode = Integer.toString(idNodeInt)  ;
			Node nodeF = graph.addNode( idNode ) ; 		
			nodeF.setAttribute("xyz", s.getX()+ vec[0], s.getY() + vec[1] , 0);
			s.setCoords(s.getX() + vec[0] , s.getY() + vec[1]);	
			s.setNode(nodeF);
			bks.putNode(nodeF);
			idNodeInt++;
				
			// create edge
			idEdge = Integer.toString( idEdgeInt+1 );
			graph.addEdge(idEdge, nodeS , nodeF) ;
			s.setNode(nodeF);
			idEdgeInt++;
			
//		}
		
		/*			if ( s.getIntenVec() < 0.1) {
		ArrayList<Node> listNeig = new ArrayList<Node>(getListNeighbors(nodeS));
		
		listNeig.remove(nodeF);
		for (Node n0 : listNeig) {
			if ( getDistGeom(n0, nodeS) < 0.05) {
				for ( Node n1 : new ArrayList<Node> (getListNeighbors(n0) ) ) {
					if ( ! n1.equals(nodeS))
					try {
						idEdge = Integer.toString( idEdgeInt+1 );
						graph.addEdge(idEdge, n1 , nodeS) ;
						graph.removeNode(n0);
						idEdgeInt++ ;
					}
					catch (EdgeRejectedException | ArrayIndexOutOfBoundsException ex) {
						// TODO: handle exception
						
					}
				}
			}
		}
		/*	for (Node no : getListNeighbors(n))
				if (!no.equals(nodeF) ) {
					try {
						idEdge = Integer.toString( idEdgeInt+1 );
						graph.addEdge(idEdge, no , nodeF) ;
						graph.removeNode(n);
						idEdgeInt++ ;
					} catch (EdgeRejectedException ex) {
						// TODO: handle exception
					}
					
			*/		
				
	}
	
	for ( seed s :listSeedsToRemove)
		lSeed.removeSeed(s);
	
}
    
	public void updateLayerAndSeeds8 () {	System.out.println("numberNodes "+ graph.getNodeCount() +"\n"+"numberSeeds "+ lSeed.getListSeeds().size());
	
		ArrayList<seed> listSeedsToRemove  = new ArrayList<seed> (); 
		
	
		Dijkstra dijkstra = new Dijkstra(Element.EDGE, "length", "length") ; 
	
		for ( seed s : lSeed.getListSeeds()  ) {	//		System.out.println(s.getX() + " " + s.getY() + " " + s.getVecX() + " " + s.getVecY() + " " + s.getIntenVec() + " " +s.getNode());
			// get old node
			Node nodeS = s.getNode();
			double[] coordNodeS = GraphPosLengthUtils.nodePosition(nodeS) ;
			double[] vec = lSeed.getVector(s, typeVectorField.gravity);		
			s.setVec(vec[0], vec[1]);
			
			double[] coordNodeF = new double[]{s.getX()+ vec[0], s.getY() + vec[1] };
			
			// check edge X with graph
			ArrayList<Node> listNearestNodes = bks.getNodesInRadius(nodeS, 5 ) ;
			ArrayList<Edge> listEdges = getListEdgewhitNodeInList(listNearestNodes);
			ArrayList<Edge> listXEdges = getListEdgeXInList(coordNodeS,coordNodeF, listEdges) ;//	System.out.println(listEdges);
			double testDist = 0 ;
			Edge e = null;	
			if ( ! listXEdges.isEmpty() ) {
				ArrayList <Node> nearestNodesDist = new ArrayList <Node> (getMapNodeDist(nodeS, bks.getNodesInRadius(nodeS, 1), 5).keySet() );			
				for (Node nearest : nearestNodesDist ) {
					ArrayList<Node> listNearestNodes2 = bks.getNodesInRadius(nodeS, 2 );
					ArrayList<Edge> listEdges2 = getListEdgewhitNodeInList(listNearestNodes2);
					ArrayList<Edge> listXEdges2 = getListEdgeXInList(nearest, nodeS, listEdges2) ;//	System.out.println(listEdges);
					
					if ( listXEdges2.isEmpty() ) {
						try {							
							dijkstra.setSource(nodeS);
							dijkstra.init(graph);
					 		dijkstra.compute();
//							System.out.println(nodeS + " " + nearest + " " + dijkstra.getPathLength(nearest));
//							System.out.println(getDistGeom(nodeS, nearest));
							
							double dj = dijkstra.getPathLength(nearest);
					//		if ( getDistGeom(nodeS, nearest) > dj ) {
								idEdge = Integer.toString(idEdgeInt +1 );
								e = graph.addEdge(idEdge, nodeS, nearest) ;
								e.addAttribute("length", getLength(e));
								listSeedsToRemove.add(s);	
								idEdgeInt++;
								break ;
						//	} else {	
								/*
								for ( Node n : dijkstra.getPath(nearest).getEachNode() ) {
									for ( Node neig: getListNeighbors(n)) {
										idEdge = Integer.toString(idEdgeInt +1 );
										e = graph.addEdge(idEdge, nodeS, neig) ;
										e.addAttribute("length", getLength(e));
										graph.removeNode(n) ;
										listSeedsToRemove.add(s);	
										
										idNodeInt++ ;
										
									}
				
								}

								*/	
							
						} catch (EdgeRejectedException exception) {
						}
					}
				}
			}
			
			if ( e != null && getLength(e) >= 0.0 ) {			
		//		listSeedsToRemove.add(s);				
		//		continue ;
			}
			
			// create new node
			ArrayList<Node> listNeig = new ArrayList<Node>(getListNeighbors(nodeS));
			boolean test = false ;
			for ( Node n0 : listNeig) {
				double dist_n0_nodeS = getDistGeom(nodeS, n0) ;
				if ( dist_n0_nodeS < Math.min(getDistGeom(coordNodeF, coordNodeS) , .1 ) ) {
					nodeS.setAttribute("xyz", s.getX()+ vec[0], s.getY() + vec[1] , 0);
					test = true ;
					break ;
				}
			}
			if (test == false ) {
				idNode = Integer.toString(idNodeInt)  ;
				Node nodeF = graph.addNode( idNode ) ; 		
				nodeF.setAttribute("xyz", s.getX()+ vec[0], s.getY() + vec[1] , 0);
				s.setCoords(s.getX() + vec[0] , s.getY() + vec[1]);	
				s.setNode(nodeF);
				bks.putNode(nodeF);
				idNodeInt++;
					
				// create edge
				idEdge = Integer.toString( idEdgeInt+1 );
				e = graph.addEdge(idEdge, nodeS , nodeF) ;
				e.addAttribute("length", getLength(e));
				s.setNode(nodeF);
				idEdgeInt++;
				
 			}
		}
		
		for ( seed s :listSeedsToRemove)
			lSeed.removeSeed(s);
		
	}
	
	public void updateLayerAndSeeds10 (typeVectorField typeVectorField) {	System.out.println("numberNodes "+ graph.getNodeCount() +"\n"+"numberSeeds "+ lSeed.getListSeeds().size());
		
		ArrayList<seed> listSeedsToRemove  = new ArrayList<seed> (); 
		Dijkstra dijkstra = new Dijkstra(Element.EDGE, "length", "length") ; 
		ArrayList <Node> listNodeWithSeed = new ArrayList<Node>(lSeed.getListNodeWithSeed());
		
		for ( seed s : lSeed.getListSeeds()  ) {	//		System.out.println(s.getX() + " " + s.getY() + " " + s.getVecX() + " " + s.getVecY() + " " + s.getIntenVec() + " " +s.getNode());
			// get old node
			Node nodeS = s.getNode();
			double[] coordNodeS = GraphPosLengthUtils.nodePosition(nodeS) ;
			
			// compute future node
			double[] vec = lSeed.getVector(s, typeVectorField);		
			double[] coordNodeF = new double[]{s.getX()+ vec[0], s.getY() + vec[1] };
			s.setVec(vec[0], vec[1]);
			
			// check edge X with graph
			ArrayList<Node> listNearestNodes = bks.getNodesInRadius(nodeS, 1 );
			ArrayList<Edge> listEdges = getListEdgewhitNodeInList(listNearestNodes);
			ArrayList<Edge> listXEdges = getListEdgeXInList(coordNodeS,coordNodeF, listEdges) ;//	System.out.println(listEdges);
			
			Edge e = null ;
			if ( listXEdges.isEmpty()) {
				Node nodeF;	
		//		System.out.println("max");
				idNode = Integer.toString(idNodeInt)  ;
				nodeF = graph.addNode( idNode ) ; 		
				nodeF.setAttribute("xyz", s.getX()+ vec[0], s.getY() + vec[1] , 0);
				s.setCoords(s.getX() + vec[0] , s.getY() + vec[1]);	
				s.setNode(nodeF);
				bks.putNode(nodeF);
				idNodeInt++;
					
				// create edge
				idEdge = Integer.toString( idEdgeInt+1 );
				e = graph.addEdge(idEdge, nodeS , nodeF) ;
				e.addAttribute("length", getLength(e));
				s.setNode(nodeF);
				idEdgeInt++;
			}
			else {
		//		System.out.println(listXEdges);
				Node nearest = getNearestNode(nodeS, listNearestNodes) ;
		//		ArrayList <Node> nearestNodesDist = new ArrayList <Node> (getMapNodeDist(nodeS, listNearestNodes, 5 ).keySet() );
		//		for ( Node nearest : nearestNodesDist)   {
					
					
					
					try {	
						idEdge = Integer.toString(idEdgeInt +1 );
						e = graph.addEdge(idEdge, nodeS, nearest) ;				
						e.setAttribute("length", getLength(e));	
						
						if( ! listSeedsToRemove.contains(s) 
								&& !listNodeWithSeed.contains(nearest)  
								) 
							listSeedsToRemove.add(s);				
						
						idEdgeInt++;
						break ;
	
					
					} catch (EdgeRejectedException ex) { //			 e.printStackTrace();
						s.setNode(nodeS);
						s.setVec(vec[0], vec[1]);
			//		}
				}
			} 
			if ( e == null ) {

				
			}
			
		
				
				/*
			// get radius
			double intenVec = s.getIntenVec() ;	
			listNearestNodes = bks.getNodesInRadius(nodeF, intenVec);	//		System.out.println(intenVec);
				
			// check nodes in radius
			if ( ! listNearestNodes.isEmpty()) {
				for ( Node nearest : listNearestNodes) {
					try {		//	System.out.println(nearest +" " + nodeF +  " "+ listNearestNodes);
						
						double[] coordNodeNearest = GraphPosLengthUtils.nodePosition(nearest) ;
						listEdges = getListEdgeNeighbor( nodeF , intenVec);
						listXEdges = getListEdgeXInList(coordNodeF , coordNodeNearest, listEdges) ;//	System.out.println(listEdges);
					
						if ( ! listXEdges.isEmpty()) 
							continue;
						
						idEdge = Integer.toString(idEdgeInt + 1 );
						Edge e = graph.addEdge(idEdge, nodeF, nearest) ;
						if( !  listSeedsToRemove.contains(s) && getLength(e) <= 0.001 )
							listSeedsToRemove.add(s);
					//	listSeeds = lSeed.getListSeeds()  ; 
						idEdgeInt++;
					} catch (EdgeRejectedException e) { 	//	e.printStackTrace();
						continue ;
					}
				}
			}			
			 */
		}
		
		for ( seed s : listSeedsToRemove) 
			lSeed.removeSeed(s);
	}
	
	public void updateLayerAndSeeds9 () {	System.out.println("numberNodes "+ graph.getNodeCount() +"\n"+"numberSeeds "+ lSeed.getListSeeds().size());
	
	ArrayList<seed> listSeedsToRemove  = new ArrayList<seed> (); 
	Dijkstra dijkstra = new Dijkstra(Element.EDGE, "length", "length") ; 
	
	for ( seed s : lSeed.getListSeeds()  ) {	//		System.out.println(s.getX() + " " + s.getY() + " " + s.getVecX() + " " + s.getVecY() + " " + s.getIntenVec() + " " +s.getNode());
		// get old node
		Node nodeS = s.getNode();
		double[] coordNodeS = GraphPosLengthUtils.nodePosition(nodeS) ;
		
		// compute future node
		double[] vec = lSeed.getVector(s, typeVectorField.gravity);		
		double[] coordNodeF = new double[]{s.getX()+ vec[0], s.getY() + vec[1] };
		
		// check edge X with graph
		ArrayList<Node> listNearestNodes = bks.getNodesInRadius(nodeS, 1 );
		ArrayList<Edge> listEdges = getListEdgewhitNodeInList(listNearestNodes);
		ArrayList<Edge> listXEdges = getListEdgeXInList(coordNodeS,coordNodeF, listEdges) ;//	System.out.println(listEdges);
		System.out.println(listXEdges);
		if ( ! listXEdges.isEmpty()) {
			Node nearest = getNearestNode(nodeS, listNearestNodes) ;
		//	s.setNode(nodeS);
			
			try {
				dijkstra.setSource(nodeS);
				dijkstra.init(graph);
				dijkstra.compute();
			
			//	double dj = dijkstra.getPathLength(nearest);			
				Path p = dijkstra.getPath(nearest) ;
				if ( p.size() >= 3 && p.size() <= 6 ) {	
					System.out.println(nodeS + " " + nearest + " " + p + " " + p.size());
					ArrayList<Node> listNodes = new ArrayList<Node>(p.getNodeSet());
					listNodes.removeAll(Arrays.asList(nodeS, nearest));
					for ( Node n :listNodes ) 
						graph.removeNode(n);	
					continue ;
				}
				
				idEdge = Integer.toString(idEdgeInt +1 );
				Edge e = graph.addEdge(idEdge, nodeS, nearest) ;				
				e.setAttribute("length", getLength(e));
				
				
				if( ! listSeedsToRemove.contains(s) ) 
					listSeedsToRemove.add(s);				
				
				idEdgeInt++;
				continue;
				
			 } catch (EdgeRejectedException e) { //			 e.printStackTrace();
			}
		} 
		
		Node nodeF;
	
		if ( getDistGeom(coordNodeS, coordNodeF) < 0.05 ) {
		//	System.out.println(s.getX() + " " + vec[0]);
		//	System.out.println("min" + GraphPosLengthUtils.nodePosition(nodeS)[0]);
			nodeS.setAttribute("xyz", s.getX()+ vec[0], s.getY() + vec[1] , 0);	
			s.setVec(vec[0], vec[1]);
		//	System.out.println("min" + GraphPosLengthUtils.nodePosition(nodeS)[0]);
		//	s.setNode(nodeS);
		// s.setCoords(s.getX()+ vec[0], s.getY() + vec[1] );
		}
		else {
			System.out.println("max");
			idNode = Integer.toString(idNodeInt)  ;
			nodeF = graph.addNode( idNode ) ; 		
			nodeF.setAttribute("xyz", s.getX()+ vec[0], s.getY() + vec[1] , 0);
			s.setCoords(s.getX() + vec[0] , s.getY() + vec[1]);	
			s.setNode(nodeF);
			bks.putNode(nodeF);
			idNodeInt++;
				
			// create edge
			idEdge = Integer.toString( idEdgeInt+1 );
			Edge e = graph.addEdge(idEdge, nodeS , nodeF) ;
			e.addAttribute("length", getLength(e));
			s.setNode(nodeF);
			idEdgeInt++;
		}		
			/*
		// get radius
		double intenVec = s.getIntenVec() ;	
		listNearestNodes = bks.getNodesInRadius(nodeF, intenVec);	//		System.out.println(intenVec);
			
		// check nodes in radius
		if ( ! listNearestNodes.isEmpty()) {
			for ( Node nearest : listNearestNodes) {
				try {		//	System.out.println(nearest +" " + nodeF +  " "+ listNearestNodes);
					
					double[] coordNodeNearest = GraphPosLengthUtils.nodePosition(nearest) ;
					listEdges = getListEdgeNeighbor( nodeF , intenVec);
					listXEdges = getListEdgeXInList(coordNodeF , coordNodeNearest, listEdges) ;//	System.out.println(listEdges);
				
					if ( ! listXEdges.isEmpty()) 
						continue;
					
					idEdge = Integer.toString(idEdgeInt + 1 );
					Edge e = graph.addEdge(idEdge, nodeF, nearest) ;
					if( !  listSeedsToRemove.contains(s) && getLength(e) <= 0.001 )
						listSeedsToRemove.add(s);
				//	listSeeds = lSeed.getListSeeds()  ; 
					idEdgeInt++;
				} catch (EdgeRejectedException e) { 	//	e.printStackTrace();
					continue ;
				}
			}
		}			
		 */
	}
	
	for ( seed s : listSeedsToRemove) 
		lSeed.removeSeed(s);
}
	
	public void updateLayerAndSeeds () {	System.out.println("numberNodes "+ graph.getNodeCount() +"\n"+"numberSeeds "+ lSeed.getListSeeds().size());
		
		ArrayList<seed> listSeedsToRemove  = new ArrayList<seed> (); 
		
		for ( seed s : lSeed.getListSeeds()  ) {	//		System.out.println(s.getX() + " " + s.getY() + " " + s.getVecX() + " " + s.getVecY() + " " + s.getIntenVec() + " " +s.getNode());
			// get old node
			Node nodeS = s.getNode();
			double[] coordNodeS = GraphPosLengthUtils.nodePosition(nodeS) ;
			
			// compute future node
			double[] vec = lSeed.getVector(s, typeVectorField.gravity);		
			if ( s.getIntenVec() <= 0.01)
				continue ;

		//	System.out.println(vec[0] + " " + vec[1] );
			double[] coordNodeF = new double[]{s.getX()+ vec[0], s.getY() + vec[1] };
			
			// check edge X with graph
			ArrayList<Node> listNearestNodes = bks.getNodesInRadius(nodeS, 1 );
			ArrayList<Edge> listEdges = getListEdgewhitNodeInList(listNearestNodes);
			ArrayList<Edge> listXEdges = getListEdgeXInList(coordNodeS,coordNodeF, listEdges) ;//	System.out.println(listEdges);
			
			if ( ! listXEdges.isEmpty()) {
				try {
					Node nearest = getNearestNode(nodeS, listNearestNodes) ;	
					idEdge = Integer.toString(idEdgeInt +1 );
					Edge e = graph.addEdge(idEdge, nodeS, nearest) ;				
					double l = getLength(e);
					s.setNode(nodeS);
					
					if( ! listSeedsToRemove.contains(s) ) {
						listSeedsToRemove.add(s);
						System.out.println(l);		
					}
					
					idEdgeInt++;
					continue;
				 } catch (EdgeRejectedException e) { //			 e.printStackTrace();
					
				}
			}
					
			// create new node
			idNode = Integer.toString(idNodeInt)  ;
			graph.addNode( idNode ) ; 
			Node nodeF = graph.getNode(idNode ) ;			
			nodeF.setAttribute("xyz", s.getX()+ vec[0], s.getY() + vec[1] , 0);
			s.setCoords(s.getX() + vec[0] , s.getY() + vec[1]);	
			bks.putNode(nodeF);
			idNodeInt++;
				
			// create edge
			idEdge = Integer.toString( idEdgeInt+1 );
			graph.addEdge(idEdge, nodeS , nodeF) ;
			s.setNode(nodeF);
			idEdgeInt++;
	
			
			/*	
			// get radius
			double intenVec = s.getIntenVec() ;	
			listNearestNodes = bks.getNodesInRadius(nodeF, intenVec);	//		System.out.println(intenVec);
				
			// check nodes in radius
			if ( ! listNearestNodes.isEmpty()) {
				for ( Node nearest : listNearestNodes) {
					try {		//	System.out.println(nearest +" " + nodeF +  " "+ listNearestNodes);
						
						double[] coordNodeNearest = GraphPosLengthUtils.nodePosition(nearest) ;
						listEdges = getListEdgeNeighbor( nodeF , intenVec);
						listXEdges = getListEdgeXInList(coordNodeF , coordNodeNearest, listEdges) ;//	System.out.println(listEdges);
					
						if ( ! listXEdges.isEmpty()) 
							continue;
						
						idEdge = Integer.toString(idEdgeInt + 1 );
						Edge e = graph.addEdge(idEdge, nodeF, nearest) ;
						if( !  listSeedsToRemove.contains(s) && getLength(e) <= 0.001 )
							listSeedsToRemove.add(s);
					//	listSeeds = lSeed.getListSeeds()  ; 
						idEdgeInt++;
					} catch (EdgeRejectedException e) { 	//	e.printStackTrace();
						continue ;
					}
				}
			}			
			 */
		}
		
		for ( seed s : listSeedsToRemove) 
			lSeed.removeSeed(s);
	}
	
	public void updateLayerAndSeeds2 ( ) {
		
		System.out.println("numberNodes "+ graph.getNodeCount());
		System.out.println("numberSeeds "+ lSeed.getListSeeds().size());
		
		for ( seed s : lSeed.getListSeeds() ) {
			
			// get old node
			Node oldNode = s.getNode();	
			
			// get cooord of potential node
			idNode = Integer.toString(idNodeInt)  ;
			graph.addNode( idNode ) ; 
			Node newNode = graph.getNode(idNode ) ;
			
			// compute vector
			double[] vec = lSeed.getVector(s, typeVectorField.gravity);	
	//		s.setVec(vec[0], vec[1]);	
			s.setCoords(s.getX() + vec[0] , s.getY() + vec[1]);	
			
			// set coordinates of new node
			newNode.setAttribute("xyz", s.getX()+ vec[0], s.getY() + vec[1] , 0);
			
			// create edge
			idEdge = Integer.toString(idEdgeInt+1 );
			graph.addEdge(idEdge, oldNode, newNode) ;
			
			// link seed to node
			s.setNode(newNode);
			idNodeInt++;
			idEdgeInt++;
		}
	}
	
	public void updateLayerAndSeeds ( typeVectorField typeVectorField ) {
		
		System.out.println("numberNodes "+ graph.getNodeCount());
		System.out.println("numberSeeds "+ lSeed.getListSeeds().size());
		
		for ( seed s : lSeed.getListSeeds() ) {
			
			// get old node
			Node oldNode = s.getNode();	
			
			// get cooord of potential node
			idNode = Integer.toString(idNodeInt)  ;
			graph.addNode( idNode ) ; 
			Node newNode = graph.getNode(idNode ) ;
			
			// compute vector
			double[] vec = lSeed.getVector(s , typeVectorField );	
	//		s.setVec(vec[0], vec[1]);	
			s.setCoords(s.getX() + vec[0] , s.getY() + vec[1]);	
			
			// set coordinates of new node
			newNode.setAttribute("xyz", s.getX()+ vec[0], s.getY() + vec[1] , 0);
			
			// create edge
			idEdge = Integer.toString(idEdgeInt+1 );
			graph.addEdge(idEdge, oldNode, newNode) ;
			
			// link seed to node
			s.setNode(newNode);
			idNodeInt++;
			idEdgeInt++;
		}
	}

	public void updateLayers_01 ( typeVectorField typeVectorField ) { System.out.println("numberNodes "+ graph.getNodeCount() +"\n"+"numberSeeds "+ lSeed.getListSeeds().size());	
		
		ArrayList<seed> listSeedsToRemove  = new ArrayList<seed> (); 
		Dijkstra dijkstra = new Dijkstra(Element.EDGE, "length", "length") ; 
		for ( seed s : lSeed.getListSeeds() ) {
			
			// get old node
			Node nodeS = s.getNode();	
			
			// get cooord of potential node
			idNode = Integer.toString(idNodeInt)  ;
			graph.addNode( idNode ) ; 
			Node nodeF = graph.getNode(idNode ) ;
			bks.putNode(nodeF);
			// compute vector
			double[] vec = lSeed.getVector(s , typeVectorField );	
			s.setCoords(s.getX() + vec[0] , s.getY() + vec[1]);	
			
			// set coordinates of new node
			nodeF.setAttribute("xyz", s.getX()+ vec[0], s.getY() + vec[1] , 0);
			
			// create edge
			idEdge = Integer.toString(idEdgeInt+1 );
			Edge e = graph.addEdge(idEdge, nodeS , nodeF) ;
			e.addAttribute("length", getDistGeom(nodeS,nodeF));
		
			// link seed to node
			s.setNode(nodeF);
			idNodeInt++;
			idEdgeInt++;
			
			handleRadius_02(s, dijkstra, listSeedsToRemove, nodeF, nodeS);
			
		}
		for ( seed s : listSeedsToRemove) 
			lSeed.removeSeed(s);
	}
	
	private void handleRadius_02 ( seed s , Dijkstra dijkstra , ArrayList<seed> listSeedsToRemove, Node nodeF , Node nodeS ) {
		
		Node source = nodeF ;
        Iterator<? extends Node> iter = source.getDepthFirstIterator() ;
        ArrayList <Node> listNodeToRemove = new ArrayList<Node>();
        ArrayList <Node> listNodeToConnect = new ArrayList<Node>();
        ArrayList <Node> listNodes = new ArrayList<Node>(bks.getNodesInRadius(nodeF, .1));
        
		dijkstra.setSource(nodeF);
		dijkstra.init(graph);
		dijkstra.compute();
		
        while (iter.hasNext() && ! listNodes.isEmpty() ) {
            Node next = iter.next();
            int lenTopo = dijkstra.getPath(next).size() - 2; 	
            listNodes.remove(next);
            if ( lenTopo >= 2 && listNodes.contains(next) ) {
            	listNodeToConnect.add(next); 
            } else {
            	listNodeToRemove.add(next);
            	listNodeToConnect.addAll(getListNeighbors(next));
            }
            
           
          /*
            if ( lenTopo <=  2 && ! listNodeToConnect.contains(next) &&  listNodes.contains(next)  ) {
            	listNodeToRemove.add(next);
            	listNodeToConnect.addAll(getListNeighbors(next));
            	
            } else {
            	listNodeToConnect.add(next);        
            	listSeedsToRemove.add(s) ;
          //  	listNodes.remove(next);
            }
            listNodes.remove(next);
           */
        }
        
        for ( Node n : listNodeToRemove )    	graph.removeNode(n);
        
        for (Node n : listNodeToConnect ) {
        	try {
	        	idEdge = Integer.toString(idEdgeInt+1 );
				Edge ed = graph.addEdge(idEdge, n , nodeF) ;
				ed.addAttribute("length", getDistGeom(n,nodeF));
		 		idEdgeInt++;
        	} catch (EdgeRejectedException e) {
				// TODO: handle exception
			}
        } 
	}
	
	private void handleRadius ( seed s , Dijkstra dijkstra , ArrayList<seed> listSeedsToRemove, Node nodeF , Node nodeS ) {
		// get radius
		double distCeck = s.getIntenVec() ;
	
		ArrayList <Node> listNearest = new ArrayList<Node> ( bks.getNodesInRadius(nodeF, distCeck));	//	System.out.println(nodeF + " " + nodeS + " " +listNearest);
		listNearest.remove(nodeS) ;
		Map < Node , Double> mapNodeDist = getMapNodeDist(nodeF, listNearest, 20);
		if ( ! listNearest.isEmpty()  ) { // System.out.println(nearest + " " + listNearest);
			
			for ( Node nearest : mapNodeDist.keySet()) { 
		
				dijkstra.setSource(nodeF);
				dijkstra.init(graph);
				dijkstra.compute();
				int lenTopo = dijkstra.getPath(nearest).size() - 2;		//		System.out.println(nearest + " " + nodeF + dijkstra.getPath(nearest));
				
				// create edge
				if ( lenTopo > 2 ) {
					System.out.println(nearest + " " + nodeF + dijkstra.getPath(nearest));
					idEdge = Integer.toString(idEdgeInt+1 );
					Edge ed = graph.addEdge(idEdge, nearest , nodeF) ;
					ed.addAttribute("length", getDistGeom(nearest,nodeF));
			 		idEdgeInt++;
					listSeedsToRemove.add(s);
//								break ;
				}
			}
		}	
	}

	private void addNode( seed s ) {
		idNode =  Integer.toString(idNodeInt) ;
		graph.addNode(idNode);
		graph.getNode(idNode).addAttribute("xyz", s.getX(), s.getY() , 0 );
		idNodeInt++;	
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
			Node n = graph.getNode(idNode) ;
			n.addAttribute("xyz", centerX + coordX ,  centerY + coordY , 0 );
			bks.putNode(n);
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

// GET NODES ----------------------------------------------------------------------------------------------------------------------------------------
	// get nearest node
	private Node getNearestNode ( Node node , ArrayList<Node> listNodes ) {
		
		listNodes.remove(node);
		
		Node nearest = null;
		double dist = 10 ;	
		for ( Node n : listNodes) {
			double test = getDistGeom(n , node);
			if ( test < dist ) {
				nearest = n ;
				dist = test ;
			}
		}	
		return nearest ;
	}
	
		
// GET EDGES ----------------------------------------------------------------------------------------------------------------------------------------

	// get length edge
	public static double getLength ( Edge e ) {		
		double[] 	coordN0 = GraphPosLengthUtils.nodePosition(e.getNode0() ) , 
					coordN1 = GraphPosLengthUtils.nodePosition(e.getNode1() ) ; 
		return Math.pow( Math.pow(coordN0[0] - coordN1[0], 2) + Math.pow(coordN0[1] - coordN1[1], 2) , 0.5 ) ;	
	}
	
	private ArrayList<Edge> getListEdgewhitNodeInList ( ArrayList<Node> listNodes ){	
		ArrayList<Edge> listEdge = new ArrayList<Edge>  ( ) ;
		for (Node n : listNodes ) {
			for ( Edge e : n.getEdgeSet()) 
				if ( ! listEdge.contains(e))
					listEdge.add(e);
		}
		return listEdge;
	}
	
	private ArrayList<Edge> getListEdgeNeighbor ( Node node , double radius ) {
		
		ArrayList<Edge> listEdge = new ArrayList<Edge> ( ) ;	//	System.out.println(bks.getNodesInRadius( node, radius));
		for ( Node n : bks.getNodesInRadius( node, radius) ) 
			for ( Edge e : n.getEdgeSet() ) 
				if ( ! listEdge.contains(e))
					listEdge.add(e) ;
		
		return listEdge ;
	}
	
	private static ArrayList<Edge> getListEdgeXInList ( double[] coords0 , double[] coords1 , ArrayList<Edge> listEdgeInRadius ) {
		
		ArrayList<Edge> list = new ArrayList<Edge> () ;
		for ( Edge e : listEdgeInRadius ) {
			Node n0 = e.getNode0(),	n1 = e.getNode1();
			
			double [] 	n0Coord = GraphPosLengthUtils.nodePosition(n0) , 
						n1Coord = GraphPosLengthUtils.nodePosition(n1) ,
						
						intersectionCoord = getCoordIntersectionLine(coords0[0], coords0[1], coords1 [0], coords1 [1], n0Coord[0], n0Coord[1], n1Coord[0], n1Coord[1]) ;
				
			if ( intersectionCoord[0] >= Math.min(coords0[0], coords1[0]) && intersectionCoord[0] <=  Math.max(coords0[0], coords1[0]) )
				if ( intersectionCoord[1] >= Math.min(coords0[1], coords1[1]) && intersectionCoord[1] <=  Math.max(coords0[1], coords1[1]) )

				list.add(e) ; 			
		}
		return list ;
	}
	
	private static ArrayList<Edge> getListEdgeXInList ( Node nEdge0 , Node nEdge1, ArrayList<Edge> listEdgeInRadius ) {
		
		ArrayList<Edge> list = new ArrayList<Edge> () ;
		double[] 	coords0 = GraphPosLengthUtils.nodePosition(nEdge0),
					coords1 = GraphPosLengthUtils.nodePosition(nEdge1);
		
		for ( Edge e : listEdgeInRadius ) {
			Node n0 = e.getNode0(),	n1 = e.getNode1();
			double [] 	n0Coord = GraphPosLengthUtils.nodePosition(n0) , 
						n1Coord = GraphPosLengthUtils.nodePosition(n1) ,
						
						intersectionCoord = getCoordIntersectionLine(coords0[0], coords0[1], coords1 [0], coords1 [1], n0Coord[0], n0Coord[1], n1Coord[0], n1Coord[1]) ;
				
			if ( intersectionCoord[0] >= Math.min(coords0[0], coords1[0]) && intersectionCoord[0] <=  Math.max(coords0[0], coords1[0]) )
				if ( intersectionCoord[1] >= Math.min(coords0[1], coords1[1]) && intersectionCoord[1] <=  Math.max(coords0[1], coords1[1]) )

				list.add(e) ; 			
		}
		return list ;
	}
	
	private static ArrayList<Edge> getListEdgeXInList (  Edge edge , ArrayList<Edge> listEdgeInRadius ) {
		
		double [] 	coords0 = GraphPosLengthUtils.nodePosition(edge.getNode0()) , 
				coords1 = GraphPosLengthUtils.nodePosition(edge.getNode1()) ;
				
		ArrayList<Edge> list = new ArrayList<Edge> () ;
		for ( Edge e : listEdgeInRadius ) {
			Node n0 = e.getNode0(),	n1 = e.getNode1();
			
			double [] 	n0Coord = GraphPosLengthUtils.nodePosition(n0) , 
						n1Coord = GraphPosLengthUtils.nodePosition(n1) ,
						
						intersectionCoord = getCoordIntersectionLine(coords0[0], coords0[1], coords1 [0], coords1 [1], n0Coord[0], n0Coord[1], n1Coord[0], n1Coord[1]) ;
				
			if ( intersectionCoord[0] > Math.min(coords0[0], coords1[0]) && intersectionCoord[0] <  Math.max(coords0[0], coords1[0]) )
				if ( intersectionCoord[1] >= Math.min(coords0[1], coords1[1]) && intersectionCoord[1] <=  Math.max(coords0[1], coords1[1]) )

				list.add(e) ; 			
		}
		return list ;
	}
	
	public static double[] getCoordIntersectionLine ( double x1 , double y1 , double x2 , double y2 , double x3 , double y3 , double x4 , double y4  ) {
		double 	xi = 0 , yi =  0 ,
				m1 , m2 , q1 , q2 ;
		
		double[] coordInter = new double[2] ;
		
		m1 = ( y1 - y2 ) / ( x1 - x2 ) ;
		m2 = ( y3 - y4 ) / ( x3 - x4 ) ;
		
		q1 = y1 - m1 * x1 ;
		q2 = y3 - m2 * x3 ;
		
		xi = ( q2 - q1 ) / ( m1 - m2 ) ;
		yi = m1 * xi  + q1 ;
	
		
		coordInter[0] = xi ; coordInter [1] = yi ;
	//	System.out.println( m2 *xi + q2 );
		return coordInter ;
	}

	// method to return sorted map ( min -> max ) by values 
	public static Map getMapTopValues ( Map <Node , Double> map , int limit ) {
				
		
		return  map.entrySet().stream()
			       .sorted(Map.Entry.comparingByValue(Comparator.naturalOrder()))
			       .limit(limit)
			       .collect(Collectors.toMap(
			       Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
	}
	
	// method to return sorted map ( min -> max ) by values 
	public static Map<Node, Double> getMapNodeDist ( Node node, ArrayList<Node> listNode , int limit ) {
		Map<Node,Double> map = new HashMap() ;
		for ( Node n: listNode ) 
			map.put(n, getDistGeom(node, n));		
		
		return  map.entrySet().stream()
			       .sorted(Map.Entry.comparingByValue(Comparator.naturalOrder()))
			       .limit(limit)
			       .collect(Collectors.toMap(
			       Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
	}
	
	// not works
	private ArrayList<Edge> getListEdgeXInListNotWork ( double[] coords0 , double[] coords1 , ArrayList<Edge> listEdgeInRadius ) {
		
		ArrayList<Edge> list = new ArrayList<Edge> () ;
		for ( Edge e : listEdgeInRadius ) {
			
			Node n0 = e.getNode0(),	n1 = e.getNode1();
			
			double [] 	n0Coord = GraphPosLengthUtils.nodePosition(n0) , 
						n1Coord = GraphPosLengthUtils.nodePosition(n1) ,
						
						intersectionCoord = getCoordIntersectionLineNotWork(coords0[0], coords0[1], coords1 [0], coords1 [1], n0Coord[0], n0Coord[1], n1Coord[0], n1Coord[1]) ;
							
			 double minX = Math.min(n0Coord[0], n1Coord[0] ) ,
					maxX = Math.max(n0Coord[0], n1Coord[0] ) ; 
			
			 double minY = Math.min(n0Coord[1], n1Coord[1] ) ,
					maxY = Math.max(n0Coord[1], n1Coord[1] ) ; 
				
			if ( intersectionCoord[0] >= minX && intersectionCoord[0] <= maxX && intersectionCoord[1] >= minY && intersectionCoord[1] <= maxY ) {
				list.add(e) ; 
			}			
		}
		return list ;
	}
	
	// not works
	private ArrayList<Edge> getListEdgeXInListNotWork ( Edge edge , ArrayList<Edge> listEdgeInRadius ) {
		
		ArrayList<Edge> list = new ArrayList<Edge> () ;
		
		double [] 	coords0 = GraphPosLengthUtils.nodePosition(edge.getNode0()) ,
					coords1 = GraphPosLengthUtils.nodePosition(edge.getNode1());
		
		
		for ( Edge e : listEdgeInRadius ) {
			
			Node n0 = e.getNode0(),	n1 = e.getNode1();
			
			double [] 	n0Coord = GraphPosLengthUtils.nodePosition(n0) , 
						n1Coord = GraphPosLengthUtils.nodePosition(n1) ,
						
						intersectionCoord = getCoordIntersectionLineNotWork(coords0[0], coords0[1], coords1 [0], coords1 [1], n0Coord[0], n0Coord[1], n1Coord[0], n1Coord[1]) ;
							
			 double minX = Math.min(n0Coord[0], n1Coord[0] ) ,
					maxX = Math.max(n0Coord[0], n1Coord[0] ) ; 
			
			 double minY = Math.min(n0Coord[1], n1Coord[1] ) ,
					maxY = Math.max(n0Coord[1], n1Coord[1] ) ; 
				
			if ( intersectionCoord[0] >= minX && intersectionCoord[0] <= maxX && intersectionCoord[1] >= minY && intersectionCoord[1] <= maxY ) {
				list.add(e) ; 
			}			
		}
		return list ;
	}

	// not works
	public static double[] getCoordIntersectionLineNotWork ( double x1 , double y1 , double x2 , double y2 , double x3 , double y3 , double x4 , double y4  ) {
		double 	xi = 0 , yi =  0 ,
				m1 , m2 , q1 , q2 ;
		double[] coordInter = new double[2] ;
		
		m1 = ( y1 - y2 ) / ( x1 - x2 ) ;
		m2 = ( y3 - y4 ) / ( x3 - x4 ) ;
		
		q1 = ( x2 * y1 - x1 * y2 ) / ( x2 - x1 ) ;
		q2 = ( x4 * y3 - x3 * y4 ) / ( x4 - x3 ) ;
		
		xi = ( q2 - q1 ) / ( m1 - m2 ) ;
		yi = m1 * xi  + q1 ;
		
		coordInter[0] = xi ; coordInter [1] = yi ;
		return coordInter ;
	}
	
	// not works
	public static double[] getCoordIntersectionLineNotWorks ( double x1 , double y1 , double x2 , double y2 , double x3 , double y3 , double x4 , double y4  ) {
		double 	a , b , c , d,  xi = 0 , yi = 0;
		double[] coordInter = new double[2] ;
		
		a = (y2-y1)/(x2-x1);
		b = (y4-y3)/(x4-x3);

		xi = ( y1 + x1 * a - x3 * b - y3 ) / ( a-b) ;
		yi = a * xi- x1 * a - y1 ;
		
	//	System.out.println(xi + " " + y1 );
		coordInter[0] = xi ; coordInter [1] = yi ;

		return coordInter ;
	}
	
	// get list of neighbor String
	public ArrayList<Node> getListNeighbors ( Node node ) { 
		
		ArrayList<Node> listNeig = new ArrayList<Node>();
		
		Iterator<Node> iter = node.getNeighborNodeIterator() ;	
		while (iter.hasNext()) {		 
			Node neig = iter.next() ;		//		System.out.println(neig.getId() + neig.getAttributeKeySet());
			if ( !listNeig.contains(neig) )
				listNeig.add(neig);
		}
		listNeig.remove(node) ;
		return listNeig ;
	}
	
	
}
