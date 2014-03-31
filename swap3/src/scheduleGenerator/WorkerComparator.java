package scheduleGenerator;

import java.io.Serializable;
import java.util.Comparator;

/*
 * BONUS FEATURE
 * This is a comparator for the PriorityQueue in the Schedule so that there didn't need to be changes to the Worker class.
 */
public class WorkerComparator implements Serializable, Comparator<Worker> {
    String jobID;

    public WorkerComparator(String jobID) {
        this.jobID = jobID;
    }

    @Override
    public int compare(Worker o1, Worker o2) {
        return o1.numWorkedForJob(this.jobID) - o2.numWorkedForJob(this.jobID);
    }

}
