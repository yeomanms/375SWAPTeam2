package scheduleGenerator;

import java.io.Serializable;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Day is used to store jobs for a given day.
 * 
 * @author schneimd. Created Oct 15, 2012.
 */
public class Day implements Serializable {
    // SWAP 1, TEAM 9
    // SMELL: Data Class - This class actually has no responsibilities. It holds
    // two values, and allows
    // other parts of the code to get and set values. The lines in the
    // constructor are particularly odd,
    // given that they simply add from one array list to another. Take this into
    // account, and there
    // are no functional lines of code in this object. We can move extra
    // functionality into this class,
    // which would allow us to move a great deal of code into these functions
    // where it belongs.
    // SWAP 2, TEAM 10
    // Refactor: By adding a function that gets strings from ints, and vice versa
    // this class has a bit more responsibility for knowing about "days" and thus
    // this problem is alleviated a bit. That being said, it's still a bit sparse.
    private String dayOfWeek;
    private ArrayList<String> jobs = new ArrayList<String>();

    /**
     * Construct a day with a name and jobs.
     * 
     * @param name
     * 
     * @param jobs
     */
    public Day(String name, ArrayList<Object> jobs) {
        this.dayOfWeek = name;
        for (Object i : jobs) {
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

    public static String getNameforNum(int n) {
        String[] days = new DateFormatSymbols().getWeekdays();
        return days[n];
    }

    public static int getNumForName(String n) {
        ArrayList<String> days = new ArrayList<String>(Arrays.asList(new DateFormatSymbols().getWeekdays()));
        return days.indexOf(n);
    }

}
