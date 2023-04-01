package F28DA_CW2;

import org.jgrapht.*;
import org.jgrapht.alg.*;
import org.jgrapht.graph.*;
import java.util.List;
import java.util.*;

import org.jgrapht.alg.shortestpath.*;

public class FlyingPlannerMainPartA {
	
	//Method used to print the cheapest path in the desired format.
	public static void printPath(List<String> path) {
		int size = path.size();
		System.out.println("Cheapest Path is:");
		for(int i = 0; i<size-1 ; i++) {
			System.out.println(path.get(i) + " ---> " + path.get(i+1));
		}
	}
	
	public static void main(String[] args) {
		//Creation of Graphs
		SimpleDirectedWeightedGraph<String, DefaultWeightedEdge> graph = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		BidirectionalDijkstraShortestPath<String, DefaultWeightedEdge> dijkGraph = new BidirectionalDijkstraShortestPath<>(graph);

        //Variables used for vertices
		String EDI = "Edinburgh";
        String LHR = "Heathrow";
        String DXB = "Dubai";
        String SYD = "Sydney";
        String KUL = "Kuala Lumpur";

        //Add the vertices
        graph.addVertex(EDI);
        graph.addVertex(LHR);
        graph.addVertex(DXB);
        graph.addVertex(SYD);
        graph.addVertex(KUL);

        //Add edges to create a circuit and add weight to the edges
        DefaultWeightedEdge e1 = graph.addEdge(EDI, LHR); graph.setEdgeWeight(e1, 80);
        DefaultWeightedEdge e1Rev = graph.addEdge(LHR, EDI); graph.setEdgeWeight(e1Rev, 80);
        
        DefaultWeightedEdge e2 = graph.addEdge(LHR, DXB); graph.setEdgeWeight(e2, 130);
        DefaultWeightedEdge e2Rev = graph.addEdge(DXB, LHR); graph.setEdgeWeight(e2Rev, 130);
        
        DefaultWeightedEdge e3 = graph.addEdge(LHR, SYD); graph.setEdgeWeight(e3, 570);
        DefaultWeightedEdge e3Rev = graph.addEdge(SYD, LHR); graph.setEdgeWeight(e3Rev, 570);
        
        DefaultWeightedEdge e4 = graph.addEdge(EDI, DXB); graph.setEdgeWeight(e4, 150);
        DefaultWeightedEdge e4Rev = graph.addEdge(DXB, EDI); graph.setEdgeWeight(e4Rev, 150);
        
        DefaultWeightedEdge e5 = graph.addEdge(DXB, KUL); graph.setEdgeWeight(e5, 170);
        DefaultWeightedEdge e5Rev = graph.addEdge(KUL, DXB); graph.setEdgeWeight(e5Rev, 170);
        
        DefaultWeightedEdge e6 = graph.addEdge(KUL, SYD); graph.setEdgeWeight(e6, 150);
        DefaultWeightedEdge e6Rev = graph.addEdge(SYD, KUL); graph.setEdgeWeight(e6Rev, 150);
        
        
        //Display graph
        System.out.println("Graph Output:");
        for(DefaultWeightedEdge e : graph.edgeSet()) {
        	System.out.println(graph.getEdgeSource(e) + " <----> " + graph.getEdgeTarget(e) + " | Weight: " + graph.getEdgeWeight(e));
        }
        
        System.err.println("---------------------------------------------------");
        
        //Find shortest path from departure to destination
        Scanner input = new Scanner(System.in);
        String departure, destination;
        System.err.println("Enter Departure City: "); departure = input.nextLine();
        System.err.println("Enter Destination City: "); destination = input.nextLine();
        System.err.println("---------------------------------------------------");
        
        GraphPath<String, DefaultWeightedEdge> path = dijkGraph.getPath(departure, destination);
        printPath(path.getVertexList());
        System.out.println("Cost of above path is: " + dijkGraph.getPathWeight(departure, destination));
	}

}
