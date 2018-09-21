package RdmSeedNet_2;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.graphstream.graph.Edge;
import org.graphstream.graph.EdgeRejectedException;
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
			double[] vec = lSeed.getVector(s);	
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
			double[] vec = lSeed.getVector(s);		//	System.out.println(vec[0] + " " + vec[1] );
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
	
	public void updateLayerAndSeeds () {
		System.out.println("numberNodes "+ graph.getNodeCount());
		System.out.println("numberSeeds "+ lSeed.getListSeeds().size());

		ArrayList<seed> listSeedsToRemove  = new ArrayList<seed> (); 
		
		for ( seed s : lSeed.getListSeeds()  ) {
	//		System.out.println(s.getX() + " " + s.getY() + " " + s.getVecX() + " " + s.getVecY() + " " + s.getIntenVec() + " " +s.getNode());
			// get old node
			Node nodeS = s.getNode();
			double[] coordNodeS = GraphPosLengthUtils.nodePosition(nodeS) ;
			
			// compute future node
			double[] vec = lSeed.getVector(s);		
			if ( s.getIntenVec() <= 0.01)
				continue ;
			//	
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
	
			// get radius
			double intenVec = s.getIntenVec() ;
//			if (intenVec < 0.01)		continue ;
			
			listNearestNodes = bks.getNodesInRadius(nodeF, intenVec);	//		System.out.println(intenVec);
			/*		
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
			double[] vec = lSeed.getVector(s);	
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
	
	// get spatial distance from 2 nodes 
	public static double getDistGeom ( Node n1 , Node n2 ) {	
		
		double [] 	coordN1 = GraphPosLengthUtils.nodePosition(n1) , 
					coordN2 = GraphPosLengthUtils.nodePosition(n2); 
		
		return  Math.pow(Math.pow( coordN1[0] - coordN2[0] , 2 ) + Math.pow( coordN1[1] - coordN2[1] , 2 ), 0.5 )  ;
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
	
	
	
}
