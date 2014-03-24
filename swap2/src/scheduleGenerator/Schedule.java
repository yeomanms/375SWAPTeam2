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
					//SWAP1 Team01 ADDITIONAL FEATURE: Removed field workersWorking,
					//moved into the jobsInOrderLoop method.

					ArrayList<String> jobsInOrder = day.getJobs();

					// Used for html later

					daysInMonth++;
					numOfJobs.add(jobsInOrder.size());

					//
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
	 * 
	 * @param jobsInOrder
	 * @param day
	 * @param workersWorking
	 * @param workersForJob
	 * @param jobsWithWorker
	 */
	// SWAP1 Team01 SMELL: Temporary Field (workersWorking): Doesn't
	// always get used. Make a home for this orphaned variable.
	public void jobsInOrderLoop(ArrayList<String> jobsInOrder, Day day,
			TreeMap<String, Worker> jobsWithWorker) {
		//SWAP1 Team01 ADDITIONAL FEATURE: field workersWorking now
		//a variable of the jobsInOrderLoop method.
		ArrayList<String> workersWorking = new ArrayList<String>();
		for (String job : jobsInOrder) {
			ArrayList<Worker> workersForJob = new ArrayList<Worker>();
			ArrayList<Worker> workersForJobForced = new ArrayList<Worker>();

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
			
			if (workersForJob.size() > 0) {
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
			} else {
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
	// /**
	// * Returns the month/day/year of next date with the name of day.
	// *
	// * @param nameOfDay
	// * @return string of year/month/day format
	// */
	// private String getNextDate(String nameOfDay) {
	// int dayNum = numForName(nameOfDay);
	// GregorianCalendar tempCal = (GregorianCalendar) this.cal.clone();
	//
	// tempCal.add(Calendar.DATE, 1);
	// while (tempCal.get(Calendar.DAY_OF_WEEK) != dayNum) {
	// tempCal.add(Calendar.DATE, 1);
	// }
	// return String.valueOf(tempCal.get(Calendar.YEAR)) + "/" +
	// String.valueOf(tempCal.get(Calendar.MONTH)) + "/"
	// + String.valueOf(tempCal.get(Calendar.DAY_OF_MONTH));
	// }

	/**
	 * Returns the schedule.
	 * 
	 * @return HashMap schedule
	 */
	public TreeMap<String, TreeMap<String, Worker>> getSchedule() {
		return this.schedule;
	}

}
