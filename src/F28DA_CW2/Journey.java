package F28DA_CW2;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

import org.jgrapht.*;

public class Journey implements IJourneyPartB<Airport, Flight>, IJourneyPartC<Airport, Flight> {
	
	//DECLARATIONS
	private List<Airport> airportsJourney;
	private List<Flight> flightsJourney;
	
// --------------------------------------------------- CONSTRUCTORS ---------------------------------------------------
	public Journey(List<Airport> airports , List<Flight> flights) {
		this.airportsJourney = airports;
		this.flightsJourney = flights;
	}
	
	public Journey(GraphPath<Airport, Flight> path) {
		this.airportsJourney = path.getVertexList();
		this.flightsJourney = path.getEdgeList();
	}
	
	
	
// --------------------------------------------------- get METHODS ---------------------------------------------------
	//GET all the airports within a journey
	@Override
	public List<String> getStops() {
		List<String> airportList = new ArrayList<>();
		
		for(Airport airport : airportsJourney)
			airportList.add(airport.getCode());
		
		return airportList;
	}

	//GET all the flights within a journey
	@Override
	public List<String> getFlights() {
		List<String> flightList = new ArrayList<>();
		
		for(Flight flight : flightsJourney)
			flightList.add(flight.getFlightCode());
		
		return flightList;
	}

	
	
// --------------------------------------------------- total METHODS ---------------------------------------------------
	//Number of connections
	@Override
	public int totalHop() {
		return flightsJourney.size();
	}

	//Total cost of journey
	@Override
	public int totalCost() {
		int total = 0;
		
		for(Flight flight : flightsJourney)
			total += flight.getCost();
		
		return total;
	}


	
// --------------------------------------------------- time METHODS ---------------------------------------------------
	//Calculates total time (in minutes) spent flying in a journey
	@Override
	public int airTime() {
		int totalAirTime = 0;
		
		for(Flight flight : this.flightsJourney) {
			String leave = flight.getFromGMTime();
			String arrive = flight.getToGMTime();
			
			int airTime = FlyingPlanner.timeDuration(leave, arrive);
			totalAirTime += airTime;
		}
		return totalAirTime;
	}

	//Calculates total time (in minutes) in between flights in a journey
	@Override
	public int connectingTime() {
		int totalConnectingTime = 0;
		
		for(int count=0 ; count<flightsJourney.size()-1 ; count++) {
			Flight flight1 = flightsJourney.get(count);
			Flight flight2 = flightsJourney.get(count+1);
			
			String flight1Arrival = flight1.getToGMTime();
			String flight2Departure = flight2.getFromGMTime();
			
			int duration = FlyingPlanner.timeDuration(flight1Arrival, flight2Departure);
			totalConnectingTime += duration;
		}
		
		return totalConnectingTime;
	}

	//Calculates total journey time (Air Time + Connection Time)
	@Override
	public int totalTime() {
		return this.airTime() + this.connectingTime();
	}


	
	
// --------------------------------------------------- Air Miles Scheme METHOD ---------------------------------------------------	
	//Calculates total miles earned for the journey - FORMULA -> (Cost of Journey * 0.03) * Total Air Time of Journey
	@Override
	public int totalAirmiles() {
		double multiplier = this.totalCost() * 0.03;
		return (int) (this.airTime() * multiplier);
	}

}
