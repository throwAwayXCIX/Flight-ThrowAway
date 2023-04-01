package F28DA_CW2;

import java.util.*;

public class StopCostClass {
	
	private String aCode;
	private int numStop;
	private int totalCost;
	
	public StopCostClass(String code, int stops, int cost) {
		this.aCode = code;
		this.numStop = stops;
		this.totalCost = cost;
	}
	
	public String getCode() {
		return this.aCode;
	}
	
	public int getAmount(char mode) {
		if(mode == 's')
			return this.numStop;
		else
			return this.totalCost;
	}
	
	public void sortClass(List<StopCostClass> list) {
		
	}

	
}
