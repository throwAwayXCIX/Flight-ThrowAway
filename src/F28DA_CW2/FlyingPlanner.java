package F28DA_CW2;

import java.text.ParseException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import org.jgrapht.*;
import org.jgrapht.alg.shortestpath.AllDirectedPaths;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.*;

public class FlyingPlanner implements IFlyingPlannerPartB<Airport,Flight>, IFlyingPlannerPartC<Airport,Flight> {

	//DECLARATIONS
	private Graph<Airport, Flight> graph = new SimpleDirectedWeightedGraph<>(Flight.class); //Edges weighted with cost for leastCost()
	private Graph<Airport, Flight> daGraph = new DirectedAcyclicGraph<>(Flight.class);
	
	//graph GETTER
	public Graph<Airport, Flight> getGraph() {
		return graph;
	}
		
	
	
	
	/**
	 * The below timeDuration method makes use of Java Date functionality [Everything is calculated in milliseconds]
	 * and calculates the amount of time between two points (start and end)
	 * @param start - Start Time
	 * @param end - End Time
	 * @return - Duration (in minutes) between start and end
	 * @throws ParseException
	 */
// ------------------------------------- timeDuration METHOD -------------------------------------
	public static int timeDuration(String start, String end) {

		int startHour = 0, startMin = 0;
		int endHour = 0, endMin = 0;
		
		startHour = Integer.parseInt(start.substring(0, 2));
		startMin = Integer.parseInt(start.substring(2));
		
		endHour = Integer.parseInt(end.substring(0, 2));
		endMin = Integer.parseInt(end.substring(2));
		
		LocalTime startTime = LocalTime.of(startHour, startMin);
		LocalTime endTime = LocalTime.of(endHour, endMin);
		
		Duration diff = Duration.between(startTime, endTime);
		
		if(diff.isNegative())
			diff = diff.plusDays(1);
		
		return (int) diff.toMinutes();
	}
	
	
		
		
	
	/**
	 * The below populate methods are to populate the defined graphs with Airports as well as the flights between
	 * them. The Airports act as Vertices and the Flights between two Airports act as the Edge between two
	 * Vertices. The weight of each edge is the cost of the flight.
	 * 
	 *  - The first method is to populate the graph from a set of airports and flights.
	 * 
	 *  - The second method makes use of the first by populating a set of airports and a set of flights from a file.
	 * 
	 * Both methods return true when population is successful.
	 */
// --------------------------------------------------- populate METHODS ---------------------------------------------------
	@Override
	public boolean populate(HashSet<String[]> airports, HashSet<String[]> flights) {
		//Iterate through HashSet of airports and add Airport instances to graph
		for(String[] airport : airports) {
			graph.addVertex(new Airport(airport));
		}
		
		//Iterate through HashSet of flights and set edges between two vertices(airports) and set weight of edge as cost of flight
		/**
		 * String[] flight:
		 *  - 0 : Flight Code
		 *  - 1 : Departure Airport Code
		 *  - 2 : Departure Time
		 *  - 3 : Arrival Airport Code
		 *  - 4 : Arrival Time
		 *  - 5 : Flight Cost
		 */
		for(String[] flight : flights) {
			Airport departure = airport(flight[1]);
			Airport arrival = airport(flight[3]);
			graph.addEdge(departure, arrival, new Flight(departure, arrival, flight));
		}
		return true;
	}
	
	@Override
	public boolean populate(FlightsReader fr) {
		HashSet<String[]> airports = fr.getAirports(); //Read Set of airports from file
		HashSet<String[]> flights = fr.getFlights(); //Read set of flights from file
		return populate(airports, flights);
	}

	
	
	
	/**
	 * The below airport and flight methods are used to create an Airport instance and Flight instance from their respective codes.
	 * 
	 * Both methods return null if the airport/flight could not be found in the graph.
	 */
// --------------------------------------------------- airport & flight METHODS ---------------------------------------------------
	@Override
	public Airport airport(String code) {
		//Searches for airport in graph. Returns null if airport cannot be found.
		for(Airport airport : graph.vertexSet()) {
			if(airport.getCode().equals(code)) 
				return airport;
		}
		return null;
	}

	@Override
	//Searches for flight in graph. Returns null if flight cannot be found.
	public Flight flight(String code) {
		for(Flight flight: graph.edgeSet()) {
			if(flight.getFlightCode().equals(code))
				return flight;
		}
		return null;
	}

	
	
	
	/**
	 * The below leastCost methods find the cheapest journey (The path of flights between two airports with the lowest total cost).
	 * The methods makes use of Dijkstra's algorithm with the edges weighted as the cost of each flight.
	 * 
	 *  - The first method returns the cheapest journey without any exclusions.
	 * 
	 *  - The second method returns the cheapest journey excluding any journey that stops at any of the airports specified in 
	 *    the excluding list.
	 */
// --------------------------------------------------- leastCost METHODS ---------------------------------------------------
	@Override
	public Journey leastCost(String from, String to) throws FlyingPlannerException {
		return leastCost(from, to, null); //null represents empty List
	}
	
	@Override
	public Journey leastCost(String from, String to, List<String> excluding) throws FlyingPlannerException {
		//VARIABLES
		Airport fromA = airport(from);
		Airport toA = airport(to);
		
		//REMOVE AIRPORTS from excluding
		if(excluding != null) { //Null check for above method
			for(String aCode : excluding) { //Removes specified vertices from tempGraph
				Airport airport = airport(aCode);
				graph.removeVertex(airport);
			}
		}
		
		for(Flight edge : graph.edgeSet()) {
			graph.setEdgeWeight(edge, edge.getCost());
		}
		
		//FINDING CHEAPEST PATH - using Dijkstra's Algorithm
		GraphPath<Airport, Flight> path = DijkstraShortestPath.findPathBetween(graph, fromA, toA);
		
		//THROW EXCEPTION
		if(path == null) {
			throw new FlyingPlannerException("No available Paths");
		}
		
		Journey journey = new Journey(path);
		return journey;
	}
	
	
	
	
	/**
	 * The below leastHop methods finds the journey with the least number of connections. The methods make use of Dijkstra's 
	 * Algorithm with edges weighted '1d', thus finding the journey with the least number of edges (i.e. connections)
	 * 
	 *  - The first method returns the journey with the least connections without any exclusions.
	 * 
	 *  - The second method returns the journey with the least number of connections excluding any journey that stops at any 
	 *    of the airports specified in the excluding list.
	 */
// --------------------------------------------------- leastHop METHODS ---------------------------------------------------
	@Override
	public Journey leastHop(String from, String to) throws FlyingPlannerException {
		return leastHop(from, to, null); //null represents empty List
	}
	
	@Override
	public Journey leastHop(String from, String to, List<String> excluding) throws FlyingPlannerException {
		//VARIABLES
		Airport fromA = airport(from);
		Airport toA = airport(to);
		
		//REMOVE AIRPORTS from excluding
		if(excluding != null) { //Null check for above method
			for(String aCode : excluding) { //Removes specified vertices from tempGraph
				Airport airport = airport(aCode);
				graph.removeVertex(airport);
			}
		}
		
		for(Flight edge : graph.edgeSet()) {
			graph.setEdgeWeight(edge, 1d);
		}
		
		//FINDING CHEAPEST PATH - using Dijkstra's Algorithm
		GraphPath<Airport, Flight> path = DijkstraShortestPath.findPathBetween(graph, fromA, toA);
		
		//THROW EXCEPTION
		if(path == null) {
			throw new FlyingPlannerException("No available Paths");
		}
		
		Journey journey = new Journey(path);
		return journey;
		
	}
	
	
	
	
	/**
	 * The below directlyConnected methods are Overrides for the methods defined in IFlyingPlannerPartC.java
	 */

// --------------------------------------------------- directlyConnected METHODS ---------------------------------------------------
	/**
	 * This method returns the set of airports that are directly connected to the @param airport.
	 * Two airports are directly connected if there exist two flights connecting them in a single 
	 * hop in both direction.
	 * @param airport - Defined airport Object
	 * @return - A set of airports directly connected to airport defined in @param airport
	 */
	@Override
	public Set<Airport> directlyConnected(Airport airport) {
		Set<Airport> connectedAirports = new HashSet<>();
		
		for(Airport vertex : graph.vertexSet()) {
			if( graph.containsEdge(vertex, airport) && graph.containsEdge(airport, vertex) ) {
				connectedAirports.add(vertex);
			}
		}
		
		return connectedAirports;
	}
	
	/**
	 * 
	 */
	@Override
	public int setDirectlyConnected() {
		int sum = 0;
		
		for(Airport airport : graph.vertexSet()) {
			airport.setDicrectlyConnected(directlyConnected(airport));
			sum += directlyConnected(airport).size();
		}
		
		return sum;
	}
	
	@Override
	public int setDirectlyConnectedOrder() {
		for(Flight edge : graph.edgeSet()) {
			Airport source = graph.getEdgeSource(edge);
			Airport target = graph.getEdgeTarget(edge);
			
			if(source.getDicrectlyConnected().size() < target.getDicrectlyConnected().size()) {
				daGraph.addVertex(source);
				daGraph.addVertex(target);
				daGraph.addEdge(source, target, edge);
			}
		}
		
		return daGraph.edgeSet().size();
	}
		
	@Override	
	public Set<Airport> getBetterConnectedInOrder(Airport airport) {
		Set<Airport> betterConnected = new HashSet<>();
		
		for(Airport destAirport : daGraph.vertexSet()) {
			if(DijkstraShortestPath.findPathBetween(daGraph, airport, destAirport) != null) {
				betterConnected.add(destAirport);
			}
		}
		betterConnected.remove(airport);
		
		return betterConnected;
	}



	/**
	 * 
	 */
// --------------------------------------------------- meetUp METHODS ---------------------------------------------------
	@Override
	public String leastCostMeetUp(String at1, String at2) throws FlyingPlannerException {
		List<String> codeList = codeList(at1, at2);
		
		List<Integer> costList = new LinkedList<>();
		for(String code : codeList) {
			Journey j1 = leastCost(at1, code);
			Journey j2 = leastCost(at2, code);
			int cost = j1.totalCost() + j2.totalCost();
			costList.add(cost);
		}
		
		int minCost = Collections.min(costList);
		int index = costList.indexOf(minCost);
		String meetUp = codeList.get(index);
		
		return meetUp;
	}

	@Override
	public String leastHopMeetUp(String at1, String at2) throws FlyingPlannerException {
		List<String> codeList = codeList(at1, at2);
		
		List<StopCostClass> costStopList = new LinkedList<>();
		for(String code : codeList) {
			Journey j1 = leastHop(at1, code);
			Journey j2 = leastHop(at2, code);
			int cost = j1.totalCost() + j2.totalCost();
			int hops = j1.totalHop() + j2.totalHop();
			costStopList.add(new StopCostClass(code, hops, cost));
		}
		
		costStopList.sort(new StopCostClassComparator());
		String meetUp = costStopList.get(0).getCode();
		
		return meetUp;
	}

	@Override
	public String leastTimeMeetUp(String at1, String at2, String startTime) throws FlyingPlannerException {		
		List<String> codeList = codeList(at1, at2);
		
		int minTime = Integer.MAX_VALUE;
		String minCode = null;
		
		int minTime1 = Integer.MAX_VALUE;
		String minCode1 = null;
		
		int minTime2  =Integer.MAX_VALUE;
		String minCode2 = null;
		
		for(String code : codeList) {
			Journey j1 = leastTime(at1, code);
			Flight firstFlight1 = flight(j1.getFlights().get(0));
			int waitTime1 = timeDuration(startTime, firstFlight1.getFromGMTime());
			int j1Time = j1.totalTime();
			int time1 = waitTime1 + j1Time;
			if(time1 < minTime1) {
				minTime1 = time1;
				minCode1 = code;
			}
			
			Journey j2 = leastTime(at2, code);
			Flight firstFlight2 = flight(j2.getFlights().get(0));
			int waitTime2 = timeDuration(startTime, firstFlight2.getFromGMTime());
			int j2Time = j2.totalTime();
			int time2 = waitTime2 + j2Time;
			if(time2 < minTime2) {
				minTime2 = time2;
				minCode2 = code;
			}
			
			if(minTime1 < minTime2) {
				minCode = minCode1;
			} else {
				minCode = minCode2;
			}
		}
		
		return minCode;
	}
	
	
	private List<String> codeList(String from, String to){
		List<String> codeList = new LinkedList<>();
		int maxNum = 0;
		int hopNum = 0;
		
		do {
			codeList = allPaths(from, to, hopNum);
			maxNum = codeList.size();
			hopNum++;
		} while(maxNum<30 && hopNum<6);
		
		return codeList;
	}
	
	private List<String> allPaths(String from, String to, int maxNum){
		Airport fromA = airport(from);
		Airport toA = airport(to);
		
		AllDirectedPaths<Airport, Flight> allDirPath = new AllDirectedPaths<>(graph);
		List<GraphPath<Airport, Flight>> pathList = allDirPath.getAllPaths(fromA, toA, true, maxNum);
		
		List<String> aCodeList = new LinkedList<>();
		
		for(GraphPath<Airport, Flight> path : pathList) {
			if(path.getEdgeList().size() != 1) {
				List<Airport> airportList = path.getVertexList();
				for(Airport airport : airportList) {
					String aCode = airport.getCode();
					if(!aCode.equalsIgnoreCase(from) && !aCode.equalsIgnoreCase(to) && !aCodeList.contains(aCode))
						aCodeList.add(aCode);
				}
			}
		}
		
		return aCodeList;
	}

	private Journey leastTime(String from, String to) throws FlyingPlannerException {
		Airport fromA = airport(from);
		Airport toA = airport(to);
		
		for(Flight edge : graph.edgeSet()) {
			graph.setEdgeWeight(edge, edge.getFlightDuration());
		}
				
		//FINDING SHORTEST PATH - using Dijkstra's Algorithm
		GraphPath<Airport, Flight> path = DijkstraShortestPath.findPathBetween(graph, fromA, toA);
		
		//THROW EXCEPTION
		if(path == null) {
			throw new FlyingPlannerException("No available Paths");
		}
				
		Journey journey = new Journey(path);
		return journey;
	}
}











