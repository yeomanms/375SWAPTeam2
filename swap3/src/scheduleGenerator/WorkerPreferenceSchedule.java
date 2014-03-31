package scheduleGenerator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.TreeMap;

import javax.swing.JFrame;
import javax.swing.JOptionPane;


/*
 * BONUS FEATURE
 * Here, the schedule operates very similarly to the Schedule, except for a priority queue is used for the scheduling to
 * make sure that people are assigned in an order that makes sure each person who wants a certain job can get it.
 */

/**
 * Used to store predicted days and generate new days.
 * 
 * @author Devon Timaeus
 */
public class WorkerPreferenceSchedule extends AbstractSchedule {

    protected HashMap<String, HashMap<String, PriorityQueue<Worker>>> workerQueues;

    /**
     * Used to construct an initial schedule, used if one does not exist.
     * 
     * @param daySlots
     * @param wrks
     */
    public WorkerPreferenceSchedule(ArrayList<Day> daySlots, ArrayList<Worker> wrks) {
        this.workers = wrks;
        this.days = daySlots;
        this.workerQueues = new HashMap<String, HashMap<String, PriorityQueue<Worker>>>();
        this.createNewWorkerQueues();

        // Key is year/month/day format and item is a hashmap with key nameOfJob
        // and item Worker
        this.schedule = new TreeMap<String, TreeMap<String, Worker>>();

        this.cal = new GregorianCalendar();

        this.calculateNextMonth();
    }

    /**
     * Calculates another month of schedule based on workers availability.
     * 
     */
    protected void calculateNextMonth() {

        int initialSize = this.schedule.size();

        // If the schedule has already been generated
        if (this.schedule.size() > 0) {
            String lastDateMade = this.schedule.lastKey();
            String[] parts = lastDateMade.split("/");
            int year = Integer.parseInt(parts[0]);
            int month = Integer.parseInt(parts[1]) - 1;
            int day = Integer.parseInt(parts[2]);
            this.cal = new GregorianCalendar(year, month, day);
            int tempNum = this.cal.get(Calendar.MONTH);
            while (tempNum == this.cal.get(Calendar.MONTH)) {
                this.cal.add(Calendar.DATE, 1);
            }
        }

        this.makeNewSchedule();

        // Calls itself if there aren't many days generated
        // For instance if the date it was created is the last day of the
        // month it would only makes one day of schedule.
        if (this.schedule.size() - initialSize < 2 && !this.workerForEveryJob) {
            this.calculateNextMonth();
        }
    }

    private void makeNewSchedule() {
        // Used to see if month changes
        int daysInMonth = 0;
        int currentMonth = this.cal.get(Calendar.MONTH);
        ArrayList<Integer> numOfJobs = new ArrayList<Integer>();

        // While still in the current month generate a schedule for each day
        while (currentMonth == this.cal.get(Calendar.MONTH)) {
            for (Day day : this.days) {
                if (this.cal.get(Calendar.DAY_OF_WEEK) == this.numForName(day.getNameOfDay())) {

                    TreeMap<String, Worker> jobsWithWorker = new TreeMap<String, Worker>();
                    ArrayList<String> workersWorking = new ArrayList<String>();

                    // Used for html later
                    daysInMonth++;
                    numOfJobs.add(day.getJobs().size());

                    for (String job : day.getJobs()) {
                        //need to make sure same person isn't assigned to 2 jobs
                        ArrayList<Worker> workersToReAdd = new ArrayList<Worker>();
                        Worker workerForJob = this.workerQueues.get(day.getNameOfDay()).get(job).poll();
                        boolean workerFound = false;
                        while(workerForJob != null && !workerFound){
                            if(workersWorking.contains(workerForJob.getName())){
                                workersToReAdd.add(workerForJob);
                                workerForJob = this.workerQueues.get(day.getNameOfDay()).get(job).poll();
                            } else{
                                workersWorking.add(workerForJob.getName());
                                workerFound = true;
                                break;
                            }
                        }
                        
                        if (workerFound) {
                            workerForJob.addWorkedJob(job);
                            jobsWithWorker.put(job, workerForJob);
                            this.workerQueues.get(day.getNameOfDay()).get(job).add(workerForJob);
                            this.workerQueues.get(day.getNameOfDay()).get(job).addAll(workersToReAdd);
                            this.createNewWorkerQueues();
                        } else {
                            jobsWithWorker.put(job, new Worker("Empty", new ArrayList<Day>()));
                            JOptionPane.showMessageDialog(new JFrame(), "No workers are able to work as a(n) " + job
                                    + " on " + day.getNameOfDay());
                            this.workerForEveryJob = false;
                            break;
                        }

                    }
                    String date = this.cal.get(Calendar.YEAR) + "/"
                            + String.format("%02d", (this.cal.get(Calendar.MONTH) + 1)) + "/"
                            + String.format("%02d", this.cal.get(Calendar.DAY_OF_MONTH));
                    this.schedule.put(date, jobsWithWorker);
                    break; // Breaks so it doesn't check the other days
                }
            }
            this.cal.add(Calendar.DATE, 1);
        }
        HTMLGenerator.makeTable(daysInMonth, numOfJobs);
    }
    
    private void createNewWorkerQueues(){
        for (Day day : this.days) {
            this.workerQueues.put(day.getNameOfDay(), new HashMap<String, PriorityQueue<Worker>>());
            for (final String job : day.getJobs()) {
                this.workerQueues.get(day.getNameOfDay()).put(job,
                        new PriorityQueue<Worker>(10, new WorkerComparator(job)));
            }
        }
        
        for (Worker worker : this.workers) {
            for (Day day : worker.getDays()) {
                for (String job : day.getJobs()) {
                    this.workerQueues.get(day.getNameOfDay()).get(job).add(worker);
                }
            }
        }
    }

}
