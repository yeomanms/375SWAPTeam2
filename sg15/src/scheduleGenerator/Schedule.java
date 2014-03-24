package scheduleGenerator;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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
    private final Worker EmptyWorker = new Worker("Empty", new ArrayList<Day>(), null);

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
                ChooseWorker(day, job, jobsWithWorker, workersWorking, workersForJob);
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
        jobsWithWorker.put(job, EmptyWorker);
        JOptionPane.showMessageDialog(
                new JFrame(),
                "No workers are able to work as a(n) "
                        + job + " on "
                        + day.getNameOfDay());
        this.workerForEveryJob = false;
    }

    // SWAP 1, TEAM 2
    private void ChooseWorker(Day day, String job, TreeMap<String, Worker> jobsWithWorker, ArrayList<String> workersWorking, ArrayList<Worker> workersForJob){
        Worker workerForJob = getRandomFreeWorker(workersForJob, getDate());
        if(workerForJob.equals(EmptyWorker)){
            EmptyWorker(day, job, jobsWithWorker);
            return;
        }
        for (Worker w : workersForJob) {
            if (!w.isBusy(getDate())
                && w.numWorkedForJob(job) < workerForJob.numWorkedForJob(job)) {
                workerForJob = w;
            }
        }
        jobsWithWorker.put(job, workerForJob);
        workersWorking.add(workerForJob.getName());
        workerForJob.addWorkedJob(job);
    }

    private Worker getRandomFreeWorker(ArrayList<Worker> workers, Date date){
        ArrayList<Worker> possibleWorkers = new ArrayList<Worker>();
        for(Worker w : workers){
            if(!w.isBusy(date))
                possibleWorkers.add(w);
        }
        return possibleWorkers.size() > 0
                ? possibleWorkers.get(new Random().nextInt(possibleWorkers.size()))
                : EmptyWorker;
    }

    private Date getDate(){
        Calendar c = this.cal;
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
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

	//SMELL: Duplicate Code - we fixed this almost exact same method in the CalendarGUI class, each of these if-else cases are nearly 
	//the exact same and the size of this method could be cut in half by refactoring it.  Could be improved by making this simple method
	//callable gloabally.
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
