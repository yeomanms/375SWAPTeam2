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
	private HashMap<String, Integer> lastWorkerToPerformJob;
	
	/*
	 * Removing magic numbers from the numForName method.
	 */
	private int final SUNDAY = 1;
	private int final MONDAY = 2;
	private int final TUESDAY = 3;
	private int final WEDNESDAY = 4;
	private int final THURSDAY = 5;
	private int final FRIDAY = 6;
	private int final SATURDAY = 7;

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

		
		// SWAP 1, TEAM 9
		// BONUS FEATURE
		// This sets up the alternating workers hashmap. It creates a list of all
		// jobs, and assumes that some null worker worked before, so that when we
		// begin iterating through, we will setup the first, second, third, etc.
		// workers.
		this.lastWorkerToPerformJob = new HashMap<String, Integer>();
		// Add Jobs with Null previous workers
		for(Day currentDay : daySlots){
			for(String job : currentDay.getJobs()){
				lastWorkerToPerformJob.put(job, -1);
			}
		}
		
		
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

	// SWAP 1, TEAM 9
	// QUALITY CHANGES
	// This code is the portion that parses the date and fast-forwards it into the month variable
	// we will need for other processing. 
	private void recalculateSchedule(){
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
	}
	
	// SWAP 1, TEAM 9
	// QUALITY CHANGES
	// The day schedule generation has slightly more complex logic and requires two return values, so this simple inner class holds that data.
	private class GenerateDayScheduleReturn{
		public boolean shouldBreak;
		public int daysInMonth;
		
		public GenerateDayScheduleReturn(boolean shouldBreak, int daysInMonth){
			this.shouldBreak = shouldBreak;
			this.daysInMonth = daysInMonth;
		}
	}
	
	// SWAP 1, TEAM 9
	// QUALITY CHANGES
	// This function pulls out a massive amount of looping code from the main function. This piece
	// allows us to modify the schedule generation that happens for each day.
	private GenerateDayScheduleReturn generateDaySchedule(Day day, int daysInMonth, int currentMonth, ArrayList<Integer> numOfJobs){
		if (this.cal.get(Calendar.DAY_OF_WEEK) == this.numForName(day.getNameOfDay())) {

			TreeMap<String, Worker> jobsWithWorker = new TreeMap<String, Worker>();
			ArrayList<String> workersWorking = new ArrayList<String>();

			ArrayList<String> jobsInOrder = day.getJobs();

			// Used for html later

			daysInMonth++;
			numOfJobs.add(jobsInOrder.size());

			//asdfasdfadsfasdfasdfasdfasdfasdfasdfadsf
			fillJobs(day, jobsInOrder, workersWorking, jobsWithWorker);

			String date = this.cal.get(Calendar.YEAR)+ "/" + String.format("%02d", (this.cal.get(Calendar.MONTH) + 1)) + "/" + String.format("%02d", this.cal.get(Calendar.DAY_OF_MONTH));
			this.schedule.put(date, jobsWithWorker);
			
			return new GenerateDayScheduleReturn(true, daysInMonth);
		}
		return new GenerateDayScheduleReturn(false, daysInMonth);
	}
	
	// SWAP 1, TEAM 9
	// QUALITY CHANGES
	// This code is the part that actually fills in who takes what job and when.
	// Having it all in its own function is very useful! We can actually allow for
	// some sort of preferences system here, for example.
	private void fillJobs(Day day, ArrayList<String> jobsInOrder, ArrayList<String> workersWorking, TreeMap<String, Worker> jobsWithWorker){
		for (String job : jobsInOrder) {

			ArrayList<Worker> workersForJob = new ArrayList<Worker>();

			
			
			for (Worker worker : this.workerIndices.get(this.numForName(day.getNameOfDay()))) {
				Day workerDay = worker.getDayWithName(day.getNameOfDay());
				if (workerDay.getJobs().contains(job)&& !workersWorking.contains(worker.getName())) {
					workersForJob.add(worker);
				}
			}
			if (workersForJob.size() > 0) {
				
				// SWAP 1, TEAM 9
				// BONUS FEATURE
				// The below code allows us to cycle through workers for all the jobs
				// which ensures that no worker works more than one week in a row
				// at a time, if there are multiple workers for that job.
				//Worker workerForJob = workersForJob.get(new Random().nextInt(workersForJob.size()));
				Integer nextWorker = lastWorkerToPerformJob.get(job) + 1;
				if (nextWorker > workersForJob.size()){
					nextWorker = 0;
				}
				
				Worker workerForJob = workersForJob.get(nextWorker);
				
				
				for (Worker w : workersForJob) {
					if (w.numWorkedForJob(job) < workerForJob.numWorkedForJob(job)) {
						workerForJob = w;
					}
				}
				jobsWithWorker.put(job, workerForJob);
				workersWorking.add(workerForJob.getName());
				workerForJob.addWorkedJob(job);
			} else {
				jobsWithWorker.put(job, new Worker("Empty", new ArrayList<Day>()));
				JOptionPane.showMessageDialog(new JFrame(),"No workers are able to work as a(n) "+ job + " on "+ day.getNameOfDay());
				this.workerForEveryJob = false;
				break;
			}
		}
	}
	
	/**
	 * Calculates another month of schedule based on workers availability.
	 * 
	 */
	public void calculateNextMonth() {

		int initialSize = this.schedule.size();

		// If the schedule has already been generated
		// SWAP 1, TEAM 9
		// QUALITY CHANGES
		// Cleared out the code that resets the the calendar and sets up the month.
		// With this modification, we can more easily create a new calendar and run with it.
		recalculateSchedule();

		// Used to see if month changes
		int currentMonth = this.cal.get(Calendar.MONTH);

		int daysInMonth = 0;
		ArrayList<Integer> numOfJobs = new ArrayList<Integer>();

		// While still in the current month generate a schedule for each day
		while (currentMonth == this.cal.get(Calendar.MONTH)) {

			for (Day day : this.days) {
				// SWAP 1, TEAM 9
				// QUALITY CHANGES
				// Cleared out the contents of this massive for loop to allow us to more easily modify it.
				// New functionality, such as a new alternating algorithm for choosing who goes on what day, could be much more
				// easily implemented.
				GenerateDayScheduleReturn daySchedule = generateDaySchedule(day, daysInMonth, currentMonth, numOfJobs);
				if (daySchedule.shouldBreak){
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

	private int numForName(String nameOfDay) {

		int dayNum = 0;
		if (nameOfDay.equals("Sunday")) {
			dayNum = SUNDAY;
		} else if (nameOfDay.equals("Monday")) {
			dayNum = MONDAY;
		} else if (nameOfDay.equals("Tuesday")) {
			dayNum = TUESDAY;
		} else if (nameOfDay.equals("Wednesday")) {
			dayNum = WEDNESDAY;
		} else if (nameOfDay.equals("Thursday")) {
			dayNum = THURSDAY;
		} else if (nameOfDay.equals("Friday")) {
			dayNum = FRIDAY;
		} else if (nameOfDay.equals("Saturday")) {
			dayNum = SATURDAY;
		}
		return dayNum;
	}

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
