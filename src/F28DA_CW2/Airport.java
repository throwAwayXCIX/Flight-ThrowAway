package F28DA_CW2;

import java.io.FileNotFoundException;
import java.util.*;

public class Airport implements IAirportPartB, IAirportPartC {
	
	//DECLARATIONS
	private String code, city, name;
	private Set<Airport> directlyConnectedSet;
	private int order;
	
	//CONSTRUCTOR
	public Airport(String[] airports) {
		this.code = airports[0];
		this.city = airports[1];
		this.name = airports[2];
	}
	
	
	
// --------------------------------------------------- METHODS ---------------------------------------------------
	@Override
	public String getCode() {
		return code;
	}

	@Override
	public String getName() {
		return name;
	}
	
	public String getCity() {
		return city;
	}

	@Override
	public void setDicrectlyConnected(Set<Airport> dicrectlyConnected) {
		this.directlyConnectedSet = dicrectlyConnected;

	}

	@Override
	public Set<Airport> getDicrectlyConnected() {
		return directlyConnectedSet;
	}


	@Override
	public void setDicrectlyConnectedOrder(int order) {
		this.order = order;

	}

	@Override
	public int getDirectlyConnectedOrder() {
		return this.order;
	}
	
	@Override
	public String toString() {
		return this.city + " (" + this.code + ") ";
	}

}
