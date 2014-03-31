package scheduleGenerator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Random;
import java.util.TreeMap;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * Used to store predicted days and generate new days.
 * 
 * @author schneimd. Created Oct 18, 2012.
 */
public class Schedule extends Thread implements Serializable {

	private ArrayList<Worker> workers;
	private ArrayList<Day> days;
	private TreeMap<String, TreeMap<String, Worker>> schedule;
	private GregorianCalendar cal;
	private HashMap<Integer, ArrayList<Worker>> workerIndices;
	private boolean workerForEveryJob = true;

	/**
	 * Used to construct an initial schedule, used if one does not exist.
	 * 
	 * @param daySlots
	 * @param wrks
	 */
	public Schedule(ArrayList<Day> daySlots, ArrayList<Worker> wrks) {
		this.workers = wrks;
		this.days = daySlots;
		this.workerIndices = new HashMap<Integer, ArrayList<Worker>>();
		for (int i = 1; i <= 7; i++) {
			this.workerIndices.put(i, new ArrayList<Worker>());
		}
		this.generateIndices();

		// Key is year/month/day format and item is a hashmap with key nameOfJob
		// and item Worker
		this.schedule = new TreeMap<String, TreeMap<String, Worker>>();

		this.cal = new GregorianCalendar();

		this.calculateNextMonth();
	}

	@Override
	public void run() {
		this.calculateNextMonth();
	}

	/**
	 * returns workers in schedule.
	 * 
	 * @return workers
	 */
	public ArrayList<Worker> getWorkers() {
		return this.workers;
	}

	private void generateIndices() {
		for (int i = 0; i < this.workers.size(); i++) {
			for (Day day : this.workers.get(i).getDays()) {
				int numDay = this.numForName(day.getNameOfDay());
				this.workerIndices.get(numDay).add(this.workers.get(i));
			}
		}
	}

	/**
	 * Calculates another month of schedule based on workers availability.
	 * 
	 */
	private synchronized void calculateNextMonth() {

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

		// Used to see if month changes
		int currentMonth = this.cal.get(Calendar.MONTH);

		int daysInMonth = 0;
		ArrayList<Integer> numOfJobs = new ArrayList<Integer>();

		// While still in the current month generate a schedule for each day
		while (currentMonth == this.cal.get(Calendar.MONTH)) {

			for (Day day : this.days) {

				if (this.cal.get(Calendar.DAY_OF_WEEK) == this.numForName(day
						.getNameOfDay())) {

					TreeMap<String, Worker> jobsWithWorker = new TreeMap<String, Worker>();
					ArrayList<String> jobsInOrder = day.getJobs();
					daysInMonth++;
					numOfJobs.add(jobsInOrder.size());
					jobsInOrderLoop(jobsInOrder, day, jobsWithWorker);

					String date = this.cal.get(Calendar.YEAR)
							+ "/"
							+ String.format("%02d",
									(this.cal.get(Calendar.MONTH) + 1))
							+ "/"
							+ String.format("%02d",
									this.cal.get(Calendar.DAY_OF_MONTH));
					this.schedule.put(date, jobsWithWorker);
					break; // Breaks so it doesn't check the other days
				}
			}
			this.cal.add(Calendar.DATE, 1);
		}
		HTMLGenerator.makeTable(daysInMonth, numOfJobs);
		// Calls itself if there aren't many days generated
		// For instance if the date it was created is the last day of the
		// month it would only makes one day of schedule.
		if (this.schedule.size() - initialSize < 2 && !this.workerForEveryJob) {
			this.calculateNextMonth();
		}

		Main.dumpConfigFile();
	}

	/**
	 * SWAP1 TEAM 01 QUALITY CHANGE REPLACED BIG SEGMENT OF CODE WITH THIS NEW
	 * METHOD METHOD
	 */
	// SWAP1 Team01 SMELL: Temporary Field (workersWorking): Doesn't
	// always get used. Make a home for this orphaned variable.
    // SWAP 1, TEAM 2
    // REFACTORING FOR ENHANCEMENT FROM BAD SMELL.
    // workersWorking is now used unless there is an empty job
	public void jobsInOrderLoop(ArrayList<String> jobsInOrder, Day day,
			TreeMap<String, Worker> jobsWithWorker) {
		ArrayList<String> workersWorking = new ArrayList<String>();
		for (String job : jobsInOrder) {
			ArrayList<Worker> workersForJob = new ArrayList<Worker>();
			ArrayList<Worker> workersForJobForced = new ArrayList<Worker>();

            populateIfNoWorkersWorking(day, workersWorking, job, workersForJob, workersForJobForced);

            if (workersForJob.size() > 0) {
                PrepareWorkersForJob(jobsWithWorker, workersWorking, job, workersForJob);
                continue;
            }
            PrepareForcedWorkersForJob(day, jobsWithWorker, workersWorking, job, workersForJobForced);
		}
	}

    // SWAP 1, TEAM 2
    // REFACTORING FOR ENHANCEMENT FROM BAD SMELL.
    // Refactored out this method
    private void PrepareForcedWorkersForJob(Day day, TreeMap<String, Worker> jobsWithWorker, ArrayList<String> workersWorking, String job, ArrayList<Worker> workersForJobForced) {
        //SWAP1 Team01 ADDITIONAL FEATURE: Schedule now "forces"
        //a non-working person to an empty job if possible.
        //Never leave a job undone.
        if (workersForJobForced.size() > 0) {
            Worker workerForJobForced = workersForJobForced.get(new Random()
                .nextInt(workersForJobForced.size()));
            jobsWithWorker.put(job, workerForJobForced);
            workersWorking.add(workerForJobForced.getName());
        } else {
            jobsWithWorker.put(job, new Worker("Empty",
                    new ArrayList<Day>()));
            JOptionPane.showMessageDialog(new JFrame(),
                    "No workers are able to work as a(n) " + job + " on "
                            + day.getNameOfDay());
            this.workerForEveryJob = false;
        }
    }

    // SWAP 1, TEAM 2
    // REFACTORING FOR ENHANCEMENT FROM BAD SMELL.
    // Refactored out this method
    private void PrepareWorkersForJob(TreeMap<String, Worker> jobsWithWorker, ArrayList<String> workersWorking, String job, ArrayList<Worker> workersForJob) {
        Worker workerForJob = workersForJob.get(new Random()
            .nextInt(workersForJob.size()));
        //SWAP1 Team01 BONUS FEATURE: Ability to schedule each person
        //no more than once for a particular role before everybody who desires
        //to perform that role is scheduled to do so.
        //
        //Compares each worker's stored numWorkedFor information and will
        //switch workers if someone else has worked the job less.
        for (Worker w : workersForJob) {
            if (w.numWorkedForJob(job) < workerForJob
                    .numWorkedForJob(job)) {
                workerForJob = w;
            }
        }
        jobsWithWorker.put(job, workerForJob);
        workersWorking.add(workerForJob.getName());
        workerForJob.addWorkedJob(job);
    }

    // SWAP 1, TEAM 2
    // REFACTORING FOR ENHANCEMENT FROM BAD SMELL.
    // workersWorking is now used unless there is an empty job
    private void populateIfNoWorkersWorking(Day day, ArrayList<String> workersWorking, String job, ArrayList<Worker> workersForJob, ArrayList<Worker> workersForJobForced) {
        for (Worker worker : this.workerIndices.get(this.numForName(day
                .getNameOfDay()))) {
            Day workerDay = worker.getDayWithName(day.getNameOfDay());
            //SWAP1 Team01 ADDITIONAL FEATURE: Populated additional worker list
            //here of workers that aren't doing anything.
            if (!workersWorking.contains(worker.getName())) {
                workersForJobForced.add(worker);
                if (workerDay.getJobs().contains(job)) {
                    workersForJob.add(worker);
                }
            }
        }
    }

    private int numForName(String nameOfDay) {
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

	// SWAP1 Team01 SMELL: Speculative Generality. Clogs up code with big
	// comments.

    // SWAP2 Team2
    // REFACTORING FOR ENHANCEMENT FROM BAD SMELL.
    // Removed the comments that were unneeded.

	/**
	 * Returns the schedule.
	 * 
	 * @return HashMap schedule
	 */
	public TreeMap<String, TreeMap<String, Worker>> getSchedule() {
		return this.schedule;
	}

}
