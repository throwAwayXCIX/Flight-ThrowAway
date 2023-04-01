package F28DA_CW2;

import java.util.*;

public class StopCostClassComparator implements Comparator<StopCostClass> {
	
	@Override
	public int compare(StopCostClass first, StopCostClass second) {
		int hopComp = 0;
		int hop1 = first.getAmount('h');
		int hop2 = second.getAmount('h');
		if(hop1 == hop2)
			hopComp = 0;
		else if(hop1 > hop2)
			hopComp = 1;
		else
			hopComp = -1;
		
		int costComp = 0;
		int cost1 = first.getAmount('c');
		int cost2 = second.getAmount('c');
		if(cost1 == cost2)
			costComp = 0;
		else if(cost1 > cost2)
			costComp = 1;
		else
			costComp = -1;
		
		return (hopComp == 0) ? costComp : hopComp;
	}

}
