package scheduleGenerator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.TreeMap;

/*
 * BONUS FEATURE
 * This code is virtually the same as what was in Schedule.java, just extracted here.
 */

public abstract class AbstractSchedule extends Thread implements Serializable {
	
    private static final long serialVersionUID = 6595204764383243072L;
    protected ArrayList<Worker> workers;
	protected ArrayList<Day> days;
	protected TreeMap<String, TreeMap<String, Worker>> schedule;
	protected GregorianCalendar cal;
	protected boolean workerForEveryJob = true;

	/**
	 * Returns the schedule.
	 * 
	 * @return HashMap schedule
	 */
	public TreeMap<String, TreeMap<String, Worker>> getSchedule(){
		return schedule;
	}
	
	/**
	 * returns workers in schedule.
	 * 
	 * @return workers
	 */
	public ArrayList<Worker> getWorkers(){
		return workers;
	}
	
	//SWAP 1, Team 10
	//SMELL: Feature Envy - This is functionality that is reused regularly about days but is in what seems to be the wrong class.  
	//It should be moved to the day class
	protected int numForName(String nameOfDay) {
		//SWAP 1, Team 10
		//SMELL: Switch-Statement - this is essentially just a long case statement for each of the different days.  
		//By using some built in java functionality with days, you could do this automatically.
		int dayNum = 0;
		if (nameOfDay.equals("Sunday")) {
			dayNum = 1;
		} else if (nameOfDay.equals("Monday")) {
			dayNum = 2;
		} else if (nameOfDay.equals("Tuesday")) {
			dayNum = 3;
		} else if (nameOfDay.equals("Wednesday")) {
			dayNum = 4;
		} else if (nameOfDay.equals("Thursday")) {
			dayNum = 5;
		} else if (nameOfDay.equals("Friday")) {
			dayNum = 6;
		} else if (nameOfDay.equals("Saturday")) {
			dayNum = 7;
		}
		return dayNum;
	}
	
	/**
	 * Calculates another month of schedule based on workers availability.
	 * 
	 */
	protected abstract void calculateNextMonth();
	
	public void run(){
		calculateNextMonth();
	}
}
