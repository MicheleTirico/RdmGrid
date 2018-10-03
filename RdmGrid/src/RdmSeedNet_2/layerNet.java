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
	
	
	public void test ( ) {
		for ( Node n : graph.getEachNode()) {
			bks.putNode(n);
		}
		System.out.println(bks.getListBucketNotEmpty().size() ) ;
		
	}
	
	public void updateLayers_01 ( typeVectorField typeVectorField ) { System.out.println("numberNodes "+ graph.getNodeCount() +"\n"+"numberSeeds "+ lSeed.getListSeeds().size());	
	
	ArrayList<seed> listSeedsToRemove  = new ArrayList<seed> (); 
	ArrayList <Node> listNodeF = new ArrayList<Node>() ;
	
	for ( seed s : lSeed.getListSeeds() ) {
		
		// get old node
		Node nodeS = s.getNode();	
		
		// get cooord of potential node
		idNode = Integer.toString(idNodeInt)  ;
		graph.addNode( idNode ) ; 
		Node nodeF = graph.getNode(idNode ) ;
		
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
		
		bks.putNode(nodeF);
		listNodeF.add(nodeF);


	}
	listSeedsToRemove.stream().forEach(s-> lSeed.removeSeed(s));
}

	public void updateLayers_02 ( typeVectorField typeVectorField , double distTest, int depthMax ) { System.out.println("numberNodes "+ graph.getNodeCount() +"\n"+"numberSeeds "+ lSeed.getListSeeds().size());	
	
		Dijkstra dijkstra = new Dijkstra(Element.EDGE, "length", "length") ; 
		ArrayList<seed> listSeedsToRemove  = new ArrayList<seed> (); 
		
		for ( seed s : lSeed.getListSeeds() ) {
			
			// get old node
			Node nodeS = s.getNode();	
			
			// get cooord of potential node
			idNode = Integer.toString(idNodeInt)  ;
			graph.addNode( idNode ) ; 
			Node nodeF = graph.getNode(idNode ) ;
			
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
			
			bks.putNode(nodeF);
		
		
		//	handleNear_01(nodeF, listSeedsToRemove, depth, distTest, s);
			handleNear_04(nodeF, listSeedsToRemove, depthMax , distTest, s, dijkstra);
		}
		listSeedsToRemove.stream().forEach(s-> lSeed.removeSeed(s));
	}

	public void updateLayers_03 ( typeVectorField typeVectorField , double distTest, int depthMax ) { System.out.println("numberNodes "+ graph.getNodeCount() +"\n"+"numberSeeds "+ lSeed.getListSeeds().size());	
	
	Dijkstra dijkstra = new Dijkstra(Element.EDGE, "length", "length") ; 
	ArrayList<seed> listSeedsToRemove  = new ArrayList<seed> (); 
	
	for ( seed s : lSeed.getListSeeds() ) {
		Node nodeF = null , nodeS = null ;
		// get old node
		nodeS = s.getNode();			
		double[] 	vec = lSeed.getVector(s , typeVectorField ) ;
		
		if ( s.getIntenVec() > 0.001 ) {
			
			// get cooord of potential node
			idNode = Integer.toString(idNodeInt)  ;
			graph.addNode( idNode ) ; 
			nodeF = graph.getNode(idNode ) ;
			
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
			
			bks.putNode(nodeF);
		}	
		else {
			nodeS.setAttribute("xyz", s.getX()+ vec[0], s.getY() + vec[1] , 0);
			s.setCoords(s.getX() + vec[0] , s.getY() + vec[1]);	
			nodeF = nodeS ;
		}

		dijkstra.setSource(nodeF);
		dijkstra.init(graph);
		dijkstra.compute();
		
		ArrayList <Node> listNodeInBks = new ArrayList<Node>(bks.getNodesInRadius(nodeF, 1)) ;	

		double distGeomTest = 100 ;
		int distTopo  = 0 ;
		Node nodeWin = null ;
		for ( Node near : listNodeInBks ) {
			double distGeom = getDistGeom(near, nodeF);
			if ( distGeom < distTest && distGeom < distGeomTest && distGeom > 0.1 ) {
				distGeomTest = distGeom ;
				distTopo = dijkstra.getPath(near).size() - 2 ;
				if ( distTopo > depthMax || distTopo == -2  ) {
					nodeWin = near ;
				}
			}
		}
		if ( nodeWin !=null ) {// 		System.out.println(nodeF + " " + nodeWin +" " + getDistGeom(nodeWin, nodeF) + " " + ( dijkstra.getPath(nodeWin).size() - 2 ) + " " + depthMax);
		
			try {
				idEdge = Integer.toString(idEdgeInt+1 );
				Edge ed = graph.addEdge(idEdge, nodeWin , nodeF) ;
				ed.addAttribute("length", getDistGeom(nodeWin,nodeF));
				listSeedsToRemove.add(s);
		 		idEdgeInt++;	 		
			 }
			 catch (EdgeRejectedException e) {	 }			
		}		
	}			
	listSeedsToRemove.stream().forEach(s-> lSeed.removeSeed(s));
}
	
	public void updateLayers_04 ( typeVectorField typeVectorField ,  int depthMax  , boolean createSeed , double minDistSeed  ) { System.out.println("numberNodes "+ graph.getNodeCount() +"\n"+"numberSeeds "+ lSeed.getListSeeds().size());	
	
		Dijkstra dijkstra = new Dijkstra(Element.EDGE, "length", "length") ; 
		ArrayList<seed> listSeedsToRemove  = new ArrayList<seed> (); 
		
		if (createSeed) 
			createSeed_01(minDistSeed);
		
		for ( seed s : lSeed.getListSeeds() ) {
			Node nodeS , nodeF ;
			// get old node
			nodeS = s.getNode();	
			
			// get cooord of potential node
			idNode = Integer.toString(idNodeInt)  ;
			graph.addNode( idNode ) ; 
			nodeF = graph.getNode(idNode ) ;
			
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
			
			bks.putNode(nodeF);
			
			double inten = getDistGeom(nodeS, nodeF);
			ArrayList <Node> listNodeInBks = new ArrayList<Node>(bks.getNodesInRadius(nodeF, 1)) ;	
			Node nodeWin = null ;
			
			for( Node n : listNodeInBks) 
				if ( getDistGeom(n, nodeF ) < inten && !n.equals(nodeS) ) {
					nodeWin = n ;
					break ;	
				}	
			
			if( nodeWin != null && nodeWin != nodeS ) {
				dijkstra.setSource(nodeF);
				
				dijkstra.init(graph);
				dijkstra.compute();
				try {
					idEdge = Integer.toString(idEdgeInt+1 );
					Edge ed = graph.addEdge(idEdge, nodeWin , nodeF) ;
					ed.addAttribute("length", getDistGeom(nodeWin,nodeF));			
					int distTopo = dijkstra.getPath(nodeWin).size() - 2 ;
					idEdgeInt++;	 
					
					if ( distTopo > depthMax || distTopo == - 2 ) 
						listSeedsToRemove.add(s);								
				 }
				 catch (EdgeRejectedException ex) {	 }			
			}
		}
		listSeedsToRemove.stream().forEach(s-> lSeed.removeSeed(s));
	}
	
	private void createSeed_01(double minDistSeed) {  
		System.out.println("numberMaxLo " + lRd.getNumberCellMaxLocal());
		ArrayList <cell> listCellToRemove = new ArrayList<cell>(); 
		for ( cell c : lRd.getListMaxLocal() ) {
			double[] coordC = new double[] { c.getX() , c.getY()} ;
		//	System.out.println(c.getX() +" " + c.getY());
			Node nearest = getNearestNode(coordC, bks.getNodesInRadius(coordC, 1) ) ;
		
			if ( nearest != null ) {
			//	System.out.println(nearest);
				double[] coordNearest = GraphPosLengthUtils.nodePosition(nearest) ;
				
				if ( getDistGeom(coordNearest, coordC) < minDistSeed ) {
//					System.out.println("peppe");
					lSeed.createSeed(coordNearest[0], coordNearest[1], nearest);
					
					listCellToRemove.add(c);
				}
			}
		}
		listCellToRemove.stream().forEach(c->lRd.removeMaxLocal(c));
	}
	


	private void handleNewNode_01 (Node nodeF , Node nodeS , seed s , typeVectorField typeVectorField ) {
		// get old node
		nodeS = s.getNode();	
		
		// get cooord of potential node
		idNode = Integer.toString(idNodeInt)  ;
		graph.addNode( idNode ) ; 
		nodeF = graph.getNode(idNode ) ;
		
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
		
		bks.putNode(nodeF);
		
	}
	
	private void handleNewNode_02 (Node nodeF , Node nodeS , seed s , typeVectorField typeVectorField ) {
		// get old node
		nodeS = s.getNode();			
		double[] 	vec = lSeed.getVector(s , typeVectorField ) ;
		
		if ( s.getIntenVec() > 0.001 ) {
			
			// get cooord of potential node
			idNode = Integer.toString(idNodeInt)  ;
			graph.addNode( idNode ) ; 
			nodeF = graph.getNode(idNode ) ;
			
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
			
			bks.putNode(nodeF);
		}	
		else {
			nodeS.setAttribute("xyz", s.getX()+ vec[0], s.getY() + vec[1] , 0);
			s.setCoords(s.getX() + vec[0] , s.getY() + vec[1]);	
			nodeF = nodeS ;
		}
	}
	
	private void handleNear_03 (Node nodeF , ArrayList<seed> listSeedsToRemove , int depthMax , double distTest, seed s  ,  Dijkstra dijkstra) {
		ArrayList <Node> listNearestNodes = new ArrayList<Node>(bks.getNodesInRadius(nodeF, 1)) ;	 
		ArrayList <Node> listNodeToConnect = new ArrayList<Node>(listNearestNodes) ;		
		Map<Node,Double> mapNodeDist = new HashMap<Node,Double> (getMapNodeDist(nodeF, listNearestNodes, listNearestNodes.size()) );		 
		ArrayList <Node> nearestNodesDist = new ArrayList <Node> (mapNodeDist.keySet() );
		
		int depth = 0 , pos = 0 ;
		double dist = 0 ;
		Node next = null ;
		dijkstra.setSource(nodeF); 
		dijkstra.init(graph); 
		dijkstra.compute();
		 
		Iterator<? extends Node> iter = nodeF.getBreadthFirstIterator() ; 
		while ( iter.hasNext() 
				&& depth < depthMax 
				|| dist < distTest
				&& pos < nearestNodesDist.size() 
				&& nearestNodesDist.contains(next)
				) {	
			next = iter.next();
			depth = dijkstra.getPath(next).size() - 2 ;
			dist = getDistGeom(next, nodeF);
		//	System.out.println(next +" " + depth + " " + dist);
			listNodeToConnect.remove(next);
			pos++ ;
		}
		listNodeToConnect.remove(nodeF);
		if (! listNodeToConnect.isEmpty() ) {
			Node near = getNearestNode (nodeF, listNodeToConnect);
			try {
				idEdge = Integer.toString(idEdgeInt+1 );
				Edge ed = graph.addEdge(idEdge, near , nodeF) ;
				ed.addAttribute("length", getDistGeom(near,nodeF));
				listSeedsToRemove.add(s);
		 		idEdgeInt++;
			 }
			 catch (EdgeRejectedException e) { e.printStackTrace();
				// TODO: handle exception
			}			
		}
	}

	private void handleNear_04 (Node nodeF , ArrayList<seed> listSeedsToRemove , int depthMax , double distTest, seed s  ,  Dijkstra dijkstra) {

		ArrayList <Node> oldList = new ArrayList<Node>(bks.getNodesInRadius(nodeF, 1)) ;	
		ArrayList <Node> listNearestNodes = new ArrayList<Node>(oldList) ;
//		for ( Node n : oldList ) 			if ( getDistGeom(n, nodeF) >= 0.2 ) 		listNearestNodes.remove(n);

		dijkstra.setSource(nodeF);
		dijkstra.init(graph);
		dijkstra.compute();
		int depth = 0 ; 
		for ( Node near : listNearestNodes ) {
			depth = dijkstra.getPath(near).size() - 2 ;
			if ( depth >= depthMax || depth == -2 ) {
				if ( getDistGeom(near , nodeF) <= distTest )
				try {
					idEdge = Integer.toString(idEdgeInt+1 );
					Edge ed = graph.addEdge(idEdge, near , nodeF) ;
					ed.addAttribute("length", getDistGeom(near,nodeF));
					listSeedsToRemove.add(s);
			 		idEdgeInt++;
			 		break ;
				 }
				 catch (EdgeRejectedException e) { e.printStackTrace();
					// TODO: handle exception
				}
			}
			
		}
		
	 
	}
	
	private void handleNear_02 (Node nodeF , ArrayList<seed> listSeedsToRemove , int depthMax , double distTest, seed s  ,  Dijkstra dijkstra) {
		 ArrayList <Node> listNearestNodes = new ArrayList<Node>(bks.getNodesInRadius(nodeF, 1)) ;
		 Map<Node,Double> mapNodeDist = new HashMap<Node,Double> (getMapNodeDist(nodeF, listNearestNodes, listNearestNodes.size()) );
		 ArrayList <Node> nearestNodesDist = new ArrayList <Node> (mapNodeDist.keySet() );
	
//		 System.out.println(listNearestNodes);
//		 System.out.println(mapNodeDist);	 
//		 System.out.println(nearestNodesDist);
		 
		 dijkstra.setSource(nodeF);
		 dijkstra.init(graph);
		 dijkstra.compute();
		 Node near = null ;
		 int  pos = 0 , depth = 0 ;
		 double distGeom = 0 ;
		/*
		 while (
//				 distGeom > 0.1 &&  
//				 depth > 10 &&
				 pos < nearestNodesDist.size()  ) {
			near =  nearestNodesDist.get(pos);
			depth = dijkstra.getPath(near).size() - 2 ;
			
			distGeom = mapNodeDist.get(near);

			if (  distGeom > 0.1 &&  depth > 10  ) {
				nodeTest.add(near);
				break;
			}
			pos++;
		 }
		 */
		 while ( pos < nearestNodesDist.size()  ) {
			near =  nearestNodesDist.get(pos);	
			distGeom = mapNodeDist.get(near);
			if (  distGeom > distTest ) {
			
				depth = dijkstra.getPath(near).size() - 2 ;
				if (  depth > depthMax  ) {		
					System.out.println(near + " " + depth + " "+ distGeom + depth ); 
					 try {
						idEdge = Integer.toString(idEdgeInt+1 );
						Edge ed = graph.addEdge(idEdge, near , nodeF) ;
						ed.addAttribute("length", getDistGeom(near,nodeF));
						listSeedsToRemove.add(s);
				 		idEdgeInt++;
					 }
					 catch (EdgeRejectedException e) { e.printStackTrace();
						// TODO: handle exception
					}
				}
				break ;
			}
			pos++;
		 } 
	}
	
	private void handleNear_01 (Node nodeF , ArrayList<seed> listSeedsToRemove , int depth, double distTest, seed s  ) {
		  ArrayList <Node> listNearestNodes = new ArrayList<Node>(bks.getNodesInRadius(nodeF, 1)) ,
	        		listNodeInDepth = new ArrayList<Node>(getListNodesInDepth(nodeF , depth )) ,
	        		listNodesNear = new ArrayList<Node>(listNearestNodes) ;
	    
		  listNodesNear.removeAll(listNodeInDepth);
		    
		  Node near = getNearestNode(nodeF, listNodesNear) ;
		    
		  if ( near != null && getDistGeom(near, nodeF) < distTest ) {
			idEdge = Integer.toString(idEdgeInt+1 );
			Edge ed = graph.addEdge(idEdge, near , nodeF) ;
			ed.addAttribute("length", getDistGeom(near,nodeF));
			listSeedsToRemove.add(s);
	 		idEdgeInt++;		
		  }
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
	
private Node getNearestNode ( double[] coord , ArrayList<Node> listNodes ) {
		
		
		
		Node nearest = null;
		double dist = 10 ;	
		for ( Node n : listNodes) {
			double[] coordN = GraphPosLengthUtils.nodePosition(n);
			double test = getDistGeom(coord , coordN);
			if ( test < dist ) {
				nearest = n ;
				dist = test ;
			}
		}	
		return nearest ;
	}
	
	private ArrayList<Node> getListNodesInDepth(Node node ,int depthMax ) {
		
		ArrayList<Node> list = new ArrayList<Node> () ;
		Dijkstra dijkstra = new Dijkstra(Element.EDGE, "length", "length") ; 
		Iterator<? extends Node> iter = node.getBreadthFirstIterator() ; 
		
		dijkstra.setSource(node);
		dijkstra.init(graph);
		dijkstra.compute();
		int depth = 0 ;
		
		while ( iter.hasNext() && depth < depthMax  ) {
			Node next = iter.next();		
			ArrayList<Node> listPathNode = new ArrayList<Node> ((Collection<? extends Node>) dijkstra.getPath(next).getEachNode());
			for ( Node n : listPathNode)
				if ( ! list.contains(n))
					list.add(n) ;
			depth = listPathNode.size() - 1 ;
		}	
		list.remove(node) ;
		return list;
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
