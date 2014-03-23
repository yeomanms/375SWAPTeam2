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
    // SWAP 1, TEAM 2
	private synchronized void calculateNextMonth() {
		int initialSize = this.schedule.size();
		generateNewSchedule();

		int daysInMonth = 0;
		ArrayList<Integer> numOfJobs = new ArrayList<Integer>();

        CreatScheduleForMonth(daysInMonth, numOfJobs, this.cal.get(Calendar.MONTH));

		HTMLGenerator.makeTable(daysInMonth, numOfJobs);
		// Calls itself if there aren't many days generated
		// For instance if the date it was created is the last day of the
		// month it would only makes one day of schedule.
		if (this.schedule.size() - initialSize < 2 && !this.workerForEveryJob) {
			this.calculateNextMonth();
		}

		Main.dumpConfigFile();
	}

    // SWAP 1, TEAM 2
    private void CreatScheduleForMonth(int daysInMonth, ArrayList<Integer> numOfJobs, int currentMonth){
        while (currentMonth == this.cal.get(Calendar.MONTH)) {
            for (Day day : this.days) {
                if (this.cal.get(Calendar.DAY_OF_WEEK) == this.numForName(day.getNameOfDay())) {
                    ProcessDay(day, daysInMonth, numOfJobs);
                    break;
                }
            }
            this.cal.add(Calendar.DATE, 1);
        }
    }

    // SWAP 1, TEAM 2
    private void ProcessDay(Day day, int daysInMonth, ArrayList<Integer> numOfJobs){
        TreeMap<String, Worker> jobsWithWorker = new TreeMap<String, Worker>();
        ArrayList<String> workersWorking = new ArrayList<String>();

        daysInMonth++;
        numOfJobs.add(day.getJobs().size());

        for (String job : day.getJobs()) {
            ArrayList<Worker> workersForJob = new ArrayList<Worker>();
            addWorkersToJob(day, job, workersWorking, workersForJob);

            if (workersForJob.size() > 0)
                ChooseWorker(job, jobsWithWorker, workersWorking, workersForJob);
            else {
                EmptyWorker(day, job, jobsWithWorker);
                break;
            }
        }
        String date = this.cal.get(Calendar.YEAR)
                + "/"
                + String.format("%02d",
                (this.cal.get(Calendar.MONTH) + 1))
                + "/"
                + String.format("%02d",
                this.cal.get(Calendar.DAY_OF_MONTH));
        this.schedule.put(date, jobsWithWorker);
    }

    // SWAP 1, TEAM 2
    private void EmptyWorker(Day day, String job, TreeMap<String, Worker> jobsWithWorker) {
        jobsWithWorker.put(job, new Worker("Empty", new ArrayList<Day>()));
        JOptionPane.showMessageDialog(
                new JFrame(),
                "No workers are able to work as a(n) "
                        + job + " on "
                        + day.getNameOfDay());
        this.workerForEveryJob = false;
    }

    // SWAP 1, TEAM 2
    private void ChooseWorker(String job, TreeMap<String, Worker> jobsWithWorker, ArrayList<String> workersWorking, ArrayList<Worker> workersForJob){
        Worker workerForJob = workersForJob.get(new Random().nextInt(workersForJob.size()));
        for (Worker w : workersForJob) {
            if (w.numWorkedForJob(job) < workerForJob.numWorkedForJob(job)) {
                workerForJob = w;
            }
        }
        jobsWithWorker.put(job, workerForJob);
        workersWorking.add(workerForJob.getName());
        workerForJob.addWorkedJob(job);
    }

    // SWAP 1, TEAM 2
    private void addWorkersToJob(Day day, String job, ArrayList<String> workers, ArrayList<Worker> workersForJob){
        for (Worker worker : this.workerIndices.get(numForName(day.getNameOfDay()))) {
            if(worker.getDayWithName(day.getNameOfDay()).getJobs().contains(job) && !workers.contains(worker.getName()))
                workersForJob.add(worker);
        }
    }

    // SWAP 1, TEAM 2
    private void generateNewSchedule(){
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


	//SMELL: Duplicate Code - <explanation>
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
