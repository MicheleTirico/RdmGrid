package test;

import java.util.ArrayList;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.graphicGraph.GraphPosLengthUtils;

public class testXEdges {

	static Graph graph = new SingleGraph(" ") ;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Node n0 = graph.addNode("0");
		n0.addAttribute("xyz", 1,1,0);
		
		Node n1 = graph.addNode("1");
		n1.addAttribute("xyz", 2,2,0);
		
		Node n2 = graph.addNode("2");
		n2.addAttribute("xyz", 1,2,0);

		Node n3 = graph.addNode("3");
		n3.addAttribute("xyz", 2.5,1.2,0);

		Edge e0 = graph.addEdge("0", n0, n1);
		Edge e1 = graph.addEdge("1", n2, n3);
		
		ArrayList<Edge> listEdge = new ArrayList<Edge>();
		listEdge.add(e1);
		graph.display(false);
		
		double[] 	coords0 = GraphPosLengthUtils.nodePosition(n0),
					coords1 = GraphPosLengthUtils.nodePosition(n1);
		
		System.out.println(getListEdgeXInList(coords0, coords1, listEdge));
		
		
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
