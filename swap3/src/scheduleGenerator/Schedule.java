package scheduleGenerator;

import scheduleGenerator.AbstractSchedule;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Random;
import java.util.TreeMap;

import javax.swing.JFrame;
import javax.swing.JOptionPane;


/*
 * BONUS FEATURE
 * Here, I wanted to make the make a schedule that would do similar things as here, but did scheduling things differently
 * To allow for this, I took much of the functionality that needed to be in both this and the new schedule and put it into
 * an abstract class. Other than extracting some of the stuff that was here, no real changes were made.
 * The main code smell we needed to overcome was Shotgun surgery, as many things needed the schedule, and so any
 * change is likely to affect many other class. 
 */

/**
 * Used to store predicted days and generate new days.
 * 
 * @author schneimd. Created Oct 18, 2012.
 */
public class Schedule extends AbstractSchedule {

    protected HashMap<Integer, ArrayList<Worker>> workerIndices;
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
	protected void calculateNextMonth() {

		int initialSize = this.schedule.size();
		
		// If the schedule has already been generated
		loadPreviousMonths();
		
		int currentMonth = this.cal.get(Calendar.MONTH);

		int daysInMonth = 0;
		ArrayList<Integer> numOfJobs = new ArrayList<Integer>();

		// While still in the current month generate a schedule for each day
		while (currentMonth == this.cal.get(Calendar.MONTH)) {

			for (Day day : this.days) {

				if (this.cal.get(Calendar.DAY_OF_WEEK) == this.numForName(day
						.getNameOfDay())) {

					TreeMap<String, Worker> jobsWithWorker = new TreeMap<String, Worker>();
					ArrayList<String> workersWorking = new ArrayList<String>();

					ArrayList<String> jobsInOrder = day.getJobs();

					// Used for html later
					daysInMonth++;
					numOfJobs.add(jobsInOrder.size());

					for (String job : jobsInOrder) {

						ArrayList<Worker> workersForJob = availableWorkersForJob(
								day, workersWorking, job);
						selectWorker(day, jobsWithWorker, workersWorking, job,
								workersForJob);

					}
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
	

	private void loadPreviousMonths() {
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

	//SWAP 1, Team 10
	//QUALITY CHANGE
	//Separating out this method would allow you to determine the available workers for any different day.  
	//This makes it easier to locate code relating to availability and would allow for reuse
	private ArrayList<Worker> availableWorkersForJob(Day day,
			ArrayList<String> workersWorking, String job) {
		ArrayList<Worker> workersForJob = new ArrayList<Worker>();

		for (Worker worker : this.workerIndices.get(this
				.numForName(day.getNameOfDay()))) {
			Day workerDay = worker.getDayWithName(day
					.getNameOfDay());
			if (workerDay.getJobs().contains(job)
					&& !workersWorking.contains(worker
							.getName())) {
				workersForJob.add(worker);

			}
		}
		return workersForJob;
	}
	
	//This encapsulates the worker selection given the potential workers for the job.  This could easily enable 
	//multiple ways of selecting or allow it to be used elsewhere.  Additionally, it helps to improve readibility.
	private void selectWorker(Day day, TreeMap<String, Worker> jobsWithWorker,
			ArrayList<String> workersWorking, String job,
			ArrayList<Worker> workersForJob) {
		if (workersForJob.size() > 0) {
			Worker workerForJob = workersForJob
					.get(new Random().nextInt(workersForJob
							.size()));
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
			jobsWithWorker.put(job, new Worker("Empty",
					new ArrayList<Day>()));
			JOptionPane
					.showMessageDialog(
							new JFrame(),
							"No workers are able to work as a(n) "
									+ job + " on "
									+ day.getNameOfDay());
			this.workerForEveryJob = false;
		}
	}
}
