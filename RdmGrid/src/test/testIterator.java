package test;

import java.util.Iterator;

import org.graphstream.algorithm.Dijkstra;
import org.graphstream.algorithm.Dijkstra.Element;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.DefaultGraph;
import org.graphstream.graph.implementations.SingleGraph;

import RdmGsaNetAlgo.graphGenerator;
import RdmGsaNetAlgo.graphGenerator.spanningTreeAlgo;
import RdmSeedNet_2.layerNet;

public class testIterator {
	
	static Graph graph = new SingleGraph(" ") ;
	public static void main(String[] args) {
	
//		graphGenerator.createGraphRandom(graph, 100);
//		graphGenerator.createSpaningTree(graph, spanningTreeAlgo.krustal);
		graphGenerator.createCompleteGraph(graph, 100, new double[] {5, 5},10 ,20);
		graphGenerator.createSpaningTree(graph, spanningTreeAlgo.krustal);

		graph.display(false) ;
		
		Node n = graph.getNode(10);
		
		for ( Edge e : graph.getEachEdge() ) 
			e.setAttribute("w", 5);	

		Dijkstra dijkstra = new Dijkstra(Element.EDGE, "w", "w") ; 
				dijkstra.setSource(n);
				
		dijkstra.init(graph);
 		dijkstra.compute();
 		
 		System.out.println(dijkstra.getPathLength(graph.getNode(11)) );

		
	}

}
