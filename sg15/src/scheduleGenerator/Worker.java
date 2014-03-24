package scheduleGenerator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * A worker contains days available to work with jobs.
 *
 * @author schneimd.
 *         Created Oct 15, 2012.
 */

//SMELL: Lazy Class - This class does do entirely different work than the worker setup class.  This class may be collapsable back into
// the worker setup class.
public class Worker implements Serializable{

	private String name;
    // SWAP 1, TEAM 2
    // QUALITY CHANGES
	private ArrayList<Day> days = new ArrayList<Day>();
    private ArrayList<Date> busy = new ArrayList<Date>();
	private HashMap<String, Integer> timesWorked;
	
	/**
	 * Builds a worker with available days.
	 * @param name 
	 * @param days
	 *
	 */
	public Worker(String name, ArrayList<Day> days, ArrayList<Date> busy)
	{
		this.name = name;
		this.days = days;
        this.busy = busy;
		this.timesWorked = new HashMap<String, Integer>();
		for(Day day: days) {
			for(String job:day.getJobs()) {
				this.timesWorked.put(job, 0);
			}
		}
	}
	
	/**
	 * Gives the name of the worker.
	 *
	 * @return name of worker
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Increments the time a job is worked by one.
	 *
	 * @param jobName
	 */
	public void addWorkedJob(String jobName) {
		this.timesWorked.put(jobName, this.timesWorked.get(jobName).intValue() + 1);
	}
	
	/**
	 * Returns the number of times a job has been worked.
	 *
	 * @param jobName
	 * @return number of tims job has been worked.
	 */
	public int numWorkedForJob(String jobName) {
		return this.timesWorked.get(jobName);
	}
	
	/**
	 * Returns the workers day based on name.
	 *
	 * @param name
	 * @return day with same name
	 */
	public Day getDayWithName(String name) {
		for(Day d: this.days) {
			if(d.getNameOfDay().equals(name)) {
				return d;
			}
		}
		return null;
	}
	
	/**
	 * Returns the worker's days.
	 *
	 * @return days
	 */
	public ArrayList<Day> getDays() {
		return this.days;
	}
	
	/**
	 * Adds a day to the worker.
	 *
	 * @param d
	 */
	public void addDay(Day d) {
		this.days.add(d);
	}

    // SWAP 1, TEAM 2
    // QUALITY CHANGES
    public boolean isBusy(Date d){
        return busy.contains(d);
    }
	
}
