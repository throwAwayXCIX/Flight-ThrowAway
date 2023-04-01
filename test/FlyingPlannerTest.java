import static org.junit.Assert.*;
import F28DA_CW2.*;

import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.HashSet;
import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;

public class FlyingPlannerTest {

	FlyingPlanner fi;

	@Before
	public void initialize() {
		fi = new FlyingPlanner();
		try {
			fi.populate(new FlightsReader());
		} catch (FileNotFoundException | FlyingPlannerException e) {
			e.printStackTrace();
		}
	}

	// Example test cases for Part B
	
	@Test
	public void leastCostTest() {
		try {
			Journey i = fi.leastCost("EDI", "DXB");
			assertEquals(3, i.totalHop());
			assertEquals(374, i.totalCost());
		} catch (FlyingPlannerException e) {
			fail();
		}
	}

	@Test
	public void leastHopTest() {
		try {
			Journey i = fi.leastHop("EDI", "DXB");
			assertEquals(2, i.totalHop());
		} catch (FlyingPlannerException e) {
			fail();
		}
	}
	
	@Test
	public void leastHopCustomTest() {
		FlyingPlanner fp = new FlyingPlanner();
		HashSet<String[]> airports = new HashSet<String[]>();
		String[] a1= {"A1","City1","AirportName1"}; airports.add(a1);
		String[] a2= {"A2","City2","AirportName2"}; airports.add(a2);
		String[] a3= {"A3","City2","AirportName3"}; airports.add(a3);
		HashSet<String[]>  flights = new HashSet<String[]>();
		String[] f1= {"F1","A1","1000","A2","1100","500"}; flights.add(f1);
		String[] f2= {"F2","A1","1000","A3","1100","50"}; flights.add(f2);
		String[] f3= {"F3","A3","1000","A2","1100","50"}; flights.add(f3);
		fp.populate(airports, flights);
		try {
			Journey lc = fp.leastHop("A1", "A2");
			
			assertEquals(500,lc.totalCost());
			assertEquals(1,lc.totalHop());
			assertEquals(900, lc.totalAirmiles());
			assertEquals(60, lc.airTime());
			assertEquals(60, lc.totalTime());
		} catch (FlyingPlannerException e) {
			e.printStackTrace();
			fail();
		}
	}

}
