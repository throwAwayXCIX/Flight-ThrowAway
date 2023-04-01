package F28DA_CW2;

public class Flight implements IFlight {
	
	//DECLARATIONS
	private String flightCode;
	private Airport from;
	private String fromGMTime;
	private Airport to;
	private String toGMTime;
	private int cost;
	
	//CONSTRUCTOR
	/**
	 * String[] flight:
	 *  - 0 : Flight Code
	 *  - 1 : Departure Airport Code
	 *  - 2 : Departure Time
	 *  - 3 : Arrival Airport Code
	 *  - 4 : Arrival Time
	 *  - 5 : Flight Cost
	 */
	public Flight(Airport departure, Airport arrival, String[] flight) {
		this.flightCode = flight[0];
		this.from = departure;
		this.fromGMTime = formatTimeString(flight[2]);
		this.to = arrival;
		this.toGMTime = formatTimeString(flight[4]);
		this.cost = Integer.parseInt(flight[5]);
	}
	
	
	
	
// --------------------------------------------------- formatTimeString METHOD ---------------------------------------------------
		public static String formatTimeString(String time) {
			if(time.length() == 1) {
				time = ("000" + time);
				return time;
			}
			else if(time.length() == 2) {
				time = ("00" + time);
				return time;
			}
			else if(time.length() == 3) {
				time = ("0" + time);
				return time;
			}
			else {
				return time;
			}
		}
	
// --------------------------------------------------- METHODS ---------------------------------------------------	
	@Override
	public String getFlightCode() {
		return this.flightCode;
	}

	@Override
	public Airport getTo() {
		return this.to;
	}

	@Override
	public Airport getFrom() {
		return this.from;
	}

	@Override
	public String getFromGMTime() {
		return this.fromGMTime;
	}

	@Override
	public String getToGMTime() {
		return this.toGMTime;
	}

	@Override
	public int getCost() {
		return this.cost;
	}
	
	public int getFlightDuration() {
		int duration = FlyingPlanner.timeDuration(this.fromGMTime, this.toGMTime);
		return duration;
	}
}
