package F28DA_CW2;

import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.*;

import javax.swing.table.TableModel;

import dnl.utils.text.table.TextTable;

//CHANGE METHODS BACK TO PRIVATE

public class FlyingPlannerMainPartBC {
	
	private static Scanner input = new Scanner(System.in);
	
	
	
// --------------------------------------------------- printJourney METHOD ---------------------------------------------------
	public static void printJourney(Journey journey, FlyingPlanner fi) throws ParseException {
		List<String> stops = journey.getStops();
		List<String> jFlights = journey.getFlights();
		
		String startCode = stops.get(0); 
		Airport start = fi.airport(startCode);
		String startCity = start.getCity();
		
		String endCode = stops.get(stops.size()-1); 
		Airport end = fi.airport(endCode);
		String endCity = end.getCity();
		
		String welcome = String.format("Journey for %s (%s) to %s (%s)" , startCity, startCode, endCity, endCode);
		System.out.println(welcome);
		
		String[] columnNames = {"Leg", "Leave", "At", "On", "Arrive", "At"};
		Object[][] data = new Object[jFlights.size()][columnNames.length];
		for(int i=0 ; i<jFlights.size() ; i++) {
			Flight flight = fi.flight(jFlights.get(i));
			Airport departure = flight.getFrom();
			Airport arrival = flight.getTo();
			String depTime = flight.getFromGMTime();
			String arrTime = flight.getToGMTime();
			
			data[i][0] = i+1; //Leg
			data[i][1] = departure.toString(); //Leave
			data[i][2] = depTime; //Leave At
			data[i][3] = jFlights.get(i); //On
			data[i][4] = arrival.toString(); //Arrive
			data[i][5] = arrTime; //Arrive At
		}
		TextTable table = new TextTable(columnNames, data);
		table.printTable();
		
		int cost = journey.totalCost();
		String totalCost = String.format("Total Journey Cost = £%d", cost);
		System.out.println(totalCost);
		
		int miles = journey.totalAirmiles();
		String totalMiles = String.format("Total miles earned = %d miles", miles);
		System.out.println(totalMiles);
	}
	
	public static void printAirport(Set<Airport> airportSet) throws ParseException {
		List<Airport> airportList = new ArrayList<>();
		for(Airport airport : airportSet) {
			airportList.add(airport);
		}
		
		String[] columnNames = {"#", "Airport Code", "City", "Name"};
		Object[][] data = new Object[airportList.size()][columnNames.length];
		for(int i=0 ; i<airportList.size() ; i++) {
			String aCode = airportList.get(i).getCode(); 
			String aCity = airportList.get(i).getCity();
			String aName = airportList.get(i).getName();
			
			data[i][0] = i+1; //#
			data[i][1] = aCode; //Code
			data[i][2] = aCity; //City
			data[i][3] = aName; //Name
		}
		TextTable table = new TextTable(columnNames, data);
		table.printTable();
	}


	
// --------------------------------------------------- checker METHOD ---------------------------------------------------
	private static String checker(String option1, String option2) {
		return checker(option1, option2, null);
	}
	
	private static String checker(String option1, String option2, String option3) {
		while(true) {
			String entry = input.nextLine().strip().toLowerCase();
			
			if(entry.equals(option1) == false) {
				if(entry.equals(option2) == false) {
					if(entry.equals(option3) == false || option3 == null) {
						System.err.println("\nPlease enter a valid input");
						continue;
					} else { return option3; }
				} else { return option2; }
			} else { return option1; }

		}
	}
	
	
	
// --------------------------------------------------- flightInput METHOD ---------------------------------------------------
	public static Airport flightInput(char mode, FlyingPlanner fi) {
		if(mode == 'd')
			System.err.println("\nEnter Departure Airport:");
		else if(mode == 'a')
			System.err.println("\nEnter Destination Airport:");
		else if(mode == 'o')
			System.err.println("\nEnter Airport:");
		
		String entry = input.nextLine().toUpperCase();
		Airport airport = fi.airport(entry);
		
		while(fi.getGraph().containsVertex(airport) == false) {
			System.err.println("Please enter a valid Airport Code");
			entry = input.nextLine();
			airport = fi.airport(entry);
		}
		
		return airport;
	}

	
	
// --------------------------------------------------- excludeInput METHOD ---------------------------------------------------
	public static List<String> excludeInput(){
		System.err.println("\nWould you like to exclude any Airports from your journey?");
		System.err.println("Type 'Yes' or 'No': ");
		String decision = checker("yes", "no");
		
		if(decision == "yes") {
			System.err.println(
					"\nPlease enter the Airports you want to exclude. \n" + 
					"Enter the codes one by one and press ENTER when done."
			);
			List<String> exclude = new ArrayList<>();
			
			String excl = input.nextLine();
			while(excl != "") {
				exclude.add(excl);
				excl = input.nextLine();
			}
			return exclude;
		} else {
			return null;
		}
	}
	
	
	
// --------------------------------------------------- journeyPartB METHOD ---------------------------------------------------	
	public static void journeyPartB(FlyingPlanner fi) throws FlyingPlannerException, ParseException {
		
		String depCode = flightInput('d', fi).getCode();

		String arrCode = flightInput('a', fi).getCode();
		
		System.err.println("\nWould you like the... \n1: Cheapest available flight \n2: Shortest available flight");
		System.err.println("Please select 1 or 2");
		String decision = checker("1", "2");

		List<String> exclude = excludeInput();
		
		if(decision == "1") {
			Journey cheapestJourney = fi.leastCost(depCode, arrCode, exclude);
			System.err.println("\nSearching for cheapest Flight...");
			printJourney(cheapestJourney, fi);
			System.out.println("Air Time: " + cheapestJourney.airTime());
			System.out.println("Connecting Time: " + cheapestJourney.connectingTime());
			System.out.println("Total Time: " + cheapestJourney.totalTime());
		} else {
			Journey shortestJourney = fi.leastHop(depCode, arrCode, exclude);
			System.err.println("\nSearching for shortest Flight with minimum connections...");
			printJourney(shortestJourney, fi);
		}
	}
	
	
	
// --------------------------------------------------- journeyPartC METHOD ---------------------------------------------------	
	private static void journeyPartC(FlyingPlanner fi) throws FlyingPlannerException, ParseException {
		
		System.err.println("\nWould you like to perform a... \n1: Directly Connected search \n2: Meet Up search?");
		System.err.println("Please select 1 or 2");
		String decision = checker("1", "2");
		
		if(decision == "1") {
			Airport airport = flightInput('o', fi);
			Set<Airport> dirCon = fi.directlyConnected(airport);
			String dirConCount = String.format("There are %d airports directly connected to %s", dirCon.size(), airport.getName());
			System.out.println(dirConCount);
			
			System.err.println("\nWould you like to view the list of airports reachable from the above airport that have strictly more direct connections?");
			System.err.println("Please enter 'Yes' or 'No'");
			String dcDecision = checker("yes", "no");
			
			if(dcDecision == "yes") {
				fi.setDirectlyConnected();
				fi.setDirectlyConnectedOrder();
				Set<Airport> betterCon = fi.getBetterConnectedInOrder(airport);
				printAirport(betterCon);
			}
		} else {
			String airport1 = flightInput('o', fi).getCode();
			String airport2 = flightInput('o', fi).getCode();
			
			System.err.println("\nWould you like to search for the...");
				System.err.println("\n1: Cheapest Meet Up (i.e. Least Cost");
				System.err.println("\n2: Meet Up with the least number of connections (i.e. Least Connections");
				System.err.println("\n3: Shortest Meet Up (i.e. Least time (incl. Connection Times))");
			String muDecision = checker("1", "2", "3");
			
			if(muDecision == "1") {
				String meetup = fi.leastCostMeetUp(airport1, airport2);
				Airport meetupA = fi.airport(meetup);
				System.out.println(String.format("The cheapest meet up point is at %s (%s)", meetupA.getName(), meetupA.getCode()));
			}
			else if(muDecision == "2") {
				String meetup = fi.leastHopMeetUp(airport1, airport2);
				Airport meetupA = fi.airport(meetup);
				System.out.println(String.format("The meet up point with the least connections is at %s (%s)", meetupA.getName(), meetupA.getCode()));
			}
			else {
				String time = null;
				do {
					System.err.println("\nPlease enter a starting time in 24-hour format (HHmm)");
					time = input.nextLine();
				} while(time.length() != 4);
				
				String meetup = fi.leastTimeMeetUp(airport1, airport2, time);
				Airport meetupA = fi.airport(meetup);
				System.out.println(String.format("The shortest meetup point is at %s (%s)", meetupA.getName(), meetupA.getCode()));
					
				
			}
		}
	}
	
	
	
// --------------------------------------------------- main METHOD ---------------------------------------------------	
	public static void main(String[] args) {

		// Your implementation should be in FlyingPlanner.java, this class is only to
		// run the user interface of your programme.

		FlyingPlanner fi;
		fi = new FlyingPlanner();
		try {
			fi.populate(new FlightsReader());
			
			System.err.println(" ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
			System.err.println("| Welcome to the Flight Planner |");
			System.err.println(" ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n");
			
			System.err.println("Would you like to...\n1: Cheapest or Shortest Flight Search\n2: Meet-up or Directly Connected Search");
			System.err.println("\nPlease select 1 or 2");
			String decision = checker("1", "2");
			
			if(decision == "1")
				journeyPartB(fi);
			else
				journeyPartC(fi);
			
		} catch (FileNotFoundException | FlyingPlannerException e) {
			e.printStackTrace();
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
	}

}
