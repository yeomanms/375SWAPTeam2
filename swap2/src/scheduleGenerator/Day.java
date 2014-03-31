package scheduleGenerator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
//SWAP 1 TEAM01 SMELL: Data class. Abstract some usages and put them in here.
// SWAP 1, TEAM 2
// REFACTORING FOR ENHANCEMENT FROM BAD SMELL.
// Added some much needed methods to this class.
/**
 * Day is used to store jobs for a given day.
 *
 * @author schneimd.
 *         Created Oct 15, 2012.
 */
public class Day implements Serializable{

    private String dayOfWeek;
    private ArrayList<String> jobs = new ArrayList<String>();

    /**
     * Construct a day with a name and jobs.
     *
     * @param name
     *
     * @param jobs
     */
    public Day(String name, ArrayList<Object> jobs)
    {
        this.dayOfWeek = name;
        for(Object i:jobs)
        {
            this.jobs.add((String) i);
        }
    }

    /**
     * Add one jobName.
     *
     * @param jobName
     */
    public void addJob(String jobName) {
        this.jobs.add(jobName);
    }

    /**
     * Set jobs to new jobs.
     *
     * @param jobNames
     */
    public void setJobs(ArrayList<String> jobNames) {
        this.jobs = jobNames;
    }

    /**
     * return current jobs.
     *
     * @return jobs
     */
    public ArrayList<String> getJobs() {
        return this.jobs;
    }

    /**
     * Gives the name of this day.
     *
     * @return day of week
     */
    public String getNameOfDay() {
        return this.dayOfWeek;
    }

    // SWAP 1, TEAM 2
    // REFACTORING FOR ENHANCEMENT FROM BAD SMELL.
    // Added some much needed methods to this class.
    public int getDayOfWeek(){
        return DayOfTheWeekN(this.dayOfWeek);
    }

    // SWAP 1, TEAM 2
    // REFACTORING FOR ENHANCEMENT FROM BAD SMELL.
    // Added some much needed methods to this class.
    public static String DayOfTheWeek(int n) {
        return new String[]{
                "Sunday", "Monday", "Tuesday", "Wednesday",
                "Thursday", "Friday", "Saturday"
        }[n-1];
    }

    // SWAP 1, TEAM 2
    // REFACTORING FOR ENHANCEMENT FROM BAD SMELL.
    // Added some much needed methods to this class.
    public static int DayOfTheWeekN(String s) {
        return Arrays.asList(
                "Sunday", "Monday", "Tuesday", "Wednesday",
                "Thursday", "Friday", "Saturday").indexOf(s);
    }
}
