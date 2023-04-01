package F28DA_CW2;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.AllDirectedPaths;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;

public class Dummy {

	public int timeDuration(String start, String end) throws ParseException {
		
		//VARIABLES
		int dayInMS = 86400000; //Number of milliseconds in a day
		
		//DateTime FORMAT INSTANCE
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");

		//Parses strings to DateTime format (specified in DateTime Format Instance)
		long leaveTime = formatter.parse(start).getTime();
		long arriveTime = formatter.parse(end).getTime();
		
		//For when the air time creeps into the next day - Adds a day in milliseconds to arrival time to account for day change
		if(arriveTime < leaveTime) {
			arriveTime += dayInMS;
		}
		
		//Calculates time between start and end in milliseconds
		long duration = arriveTime - leaveTime;
		//Converts miliseconds to minutes
		long durationMinutes = TimeUnit.MILLISECONDS.toMinutes(duration);
		
		return (int) durationMinutes;
	}
	
	public static void main(String[] args) {
		
		int startH = 16 ; int startM = 40;
		int endH = 16 ; int endM = 50;
		
		
		LocalTime start = LocalTime.of(startH, startM);
		LocalTime end = LocalTime.of(endH, endM);

		
		//11hrs and 43min - 703min
		
		System.out.println("START: " + start);
		System.out.println("END: " + end);
		
		Duration diff = Duration.between(start, end);
		
		if(diff.isNegative())
			diff = diff.plusDays(1);
		
		System.out.println("DIFF: " + diff.toMinutes());
	}

	
	
	/*
	public String leastTimeMeetUp(String at1, String at2, String startTime) throws FlyingPlannerException, ParseException {
		List<String> codeList = codeList(at1, at2);
		
		List<Integer> timeList = new LinkedList<>();
		startTime = Flight.formatTimeString(startTime);
		startTime = startTime.substring(0,2) + ":" + startTime.substring(2);
		for(String code : codeList) {
			Journey j1 = leastTime(at1, code, 4);
			Flight firstFlight1 = flight(j1.getFlights().get(0));
			String ffDepTime1 = firstFlight1.getFromGMTime();
			int time1_1 = j1.timeDuration(startTime, ffDepTime1);
			int time1_2 = j1.totalTime();
			int time1 = time1_1 + time1_2;
			
			Journey j2 = leastTime(at2, code, 4);
			Flight firstFlight2 = flight(j2.getFlights().get(0));
			String ffDepTime2 = firstFlight2.getFromGMTime();
			int time2_1 = j2.timeDuration(startTime, ffDepTime2);
			int time2_2 = j2.totalTime();
			int time2 = time2_1 + time2_2;
			
			if(time1 > time2)
				timeList.add(time1);
			else
				timeList.add(time2);
		}
		
		int minTime = Collections.min(timeList);
		int index = timeList.indexOf(minTime);
		String meetUp = codeList.get(index);
		
		return meetUp;
	}
	*/
	






}
