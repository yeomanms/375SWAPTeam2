package scheduleGenerator;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.*;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * 
 * @author schneimd
 */
public class WorkerSetup extends javax.swing.JFrame {

    private ArrayList<Day> days;
    private ArrayList<JPanel> workerTabs;

    /**
     * Allows for editing of already made workers.
     * 
     * @param workers
     */
    public WorkerSetup(ArrayList<Worker> workers) {
        this.setPreferredSize(new Dimension(425, 450));
        this.workerTabs = new ArrayList<JPanel>();
        initComponents();
        for (int c = 0; c < workers.size(); c++) {
            this.addWorker();
        }

        for (int c = 0; c < workers.size(); c++) {
            JTextField nameArea = (JTextField) this.workerTabs.get(c).getComponent(2);
            nameArea.setText(workers.get(c).getName());
            JTabbedPane daysPane = (JTabbedPane) this.workerTabs.get(c).getComponents()[0];
            for (int i = 0; i < daysPane.getTabCount(); i++) {
                for (int n = 0; n < workers.get(c).getDays().size(); n++) {
                    if (daysPane.getTitleAt(i).equals(workers.get(c).getDays().get(n).getNameOfDay())) {

                        JPanel day = (JPanel) daysPane.getComponent(i);
                        JScrollPane pane = (JScrollPane) day.getComponent(0);
                        JViewport view = (JViewport) pane.getComponent(0);
                        JPanel p = (JPanel) view.getComponent(0);

                        for (Component job : p.getComponents()) {
                            for (String workerJob : workers.get(c).getDays().get(n).getJobs()) {
                                if (((JCheckBox) job).getText().equals(workerJob)) {
                                    ((JCheckBox) job).setSelected(true);
                                }
                            }
                        }
                    }
                }
            }
        }

    }

    /**
     * Creates new form WorkerSetup
     */
    public WorkerSetup() {
        this.setPreferredSize(new Dimension(425, 450));
        this.workerTabs = new ArrayList<JPanel>();
        initComponents();
        addWorker();
    }

    private void addWorker() {

        // SWAP 1, TEAM 9
        // SMELL: Comments - The number of comments in the code below indicates
        // that they are serving
        // as dividers, in a way that would indicate we should extract methods
        // from this function.
        // This would vastly improve the code quality and readability here,
        // allowing us to actually
        // extend functionality in adding workers.
        // SWAP 2, TEAM 10
        // Refactor: Moved out the large chunks of code that would do UI formatting
        // things so that this method is clearer to read. Much of the actual work for the
        // formatting was pretty one-off for this part, and not nicely extendable to the other
        // parts that use similar code. Perhaps if the UI were redesigned, this could
        // result in a much more formulaic way of calling these methods.

        this.days = Main.getDays();
        javax.swing.JTabbedPane tempWorkerDays = new javax.swing.JTabbedPane();
        javax.swing.JTextField tempWorkerName = new javax.swing.JTextField();
        javax.swing.JPanel tempWorkerTab = new javax.swing.JPanel();

        this.makeDayTabs(tempWorkerDays);

        this.addWorkerSection(tempWorkerDays, tempWorkerTab, tempWorkerName);

        // Prevents a nullPointer
        if (this.workerTabs.size() == 0) {
            this.workerTabs.add(tempWorkerTab);
            this.workerTabPanel.addTab("Worker 1", null, tempWorkerTab, "");
        } else {
            this.workerTabs.add(tempWorkerTab);
            this.workerTabPanel.addTab("Worker " + String.valueOf(this.workerTabs.size()), null, tempWorkerTab, "");
        }
    }

    private void addWorkerSection(javax.swing.JTabbedPane tempWorkerDays, javax.swing.JPanel tempWorkerTab,
            javax.swing.JTextField tempWorkerName) {
        // Add a section for the worker's name
        JLabel workerNameLabel = new JLabel("Worker's Name:");

        javax.swing.GroupLayout workerTabLayout = new javax.swing.GroupLayout(tempWorkerTab);
        tempWorkerTab.setLayout(workerTabLayout);
        workerTabLayout.setHorizontalGroup(workerTabLayout.createParallelGroup(
                javax.swing.GroupLayout.Alignment.LEADING).addGroup(
                workerTabLayout
                        .createSequentialGroup()
                        .addContainerGap()
                        .addGroup(
                                workerTabLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(tempWorkerDays)
                                        .addGroup(
                                                javax.swing.GroupLayout.Alignment.TRAILING,
                                                workerTabLayout
                                                        .createSequentialGroup()
                                                        .addGap(0, 0, Short.MAX_VALUE)
                                                        .addComponent(workerNameLabel)
                                                        .addPreferredGap(
                                                                javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                        .addComponent(tempWorkerName,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE, 150,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addGap(49, 49, 49))).addContainerGap()));

        // Adds text area and label for name then tab area for days.
        workerTabLayout.setVerticalGroup(workerTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(
                        workerTabLayout
                                .createSequentialGroup()
                                .addContainerGap()
                                .addGroup(
                                        workerTabLayout
                                                .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(workerNameLabel)
                                                .addComponent(tempWorkerName, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tempWorkerDays, javax.swing.GroupLayout.PREFERRED_SIZE, 249,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

    }

    private void makeDayTabs(javax.swing.JTabbedPane tempWorkerDays) {
        // Makes a tab for each day and a check box for each job.
        for (Day day : this.days) {
            JCheckBox[] jobs = new JCheckBox[day.getJobs().size()];
            for (int i = 0; i < day.getJobs().size(); i++) {
                jobs[i] = new JCheckBox(day.getJobs().get(i));
            }

            // Put Check Boxes in a scrollPane for dynamics
            JScrollPane tempDayJobPane = new JScrollPane();
            JPanel tempPanel = new JPanel();
            tempPanel.setLayout(new GridLayout(jobs.length, 1));

            for (JCheckBox job : jobs) {
                tempPanel.add(job);
            }
            tempDayJobPane.setViewportView(tempPanel);

            // Label the Pane
            JLabel jobLabel = new JLabel("Preferred Jobs:");

            JPanel dayTab = new JPanel();

            // Set vertical and horizontal layouts.
            javax.swing.GroupLayout tabLayout = new javax.swing.GroupLayout(dayTab);

            tabLayout.setHorizontalGroup(tabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(
                            tabLayout
                                    .createSequentialGroup()
                                    .addGap(63, 63, 63)
                                    .addGroup(
                                            tabLayout
                                                    .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(tempDayJobPane,
                                                            javax.swing.GroupLayout.PREFERRED_SIZE, 198,
                                                            javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(jobLabel)).addContainerGap(73, Short.MAX_VALUE)));

            tabLayout.setVerticalGroup(tabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(
                            tabLayout
                                    .createSequentialGroup()
                                    .addContainerGap()
                                    .addComponent(jobLabel)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(tempDayJobPane, javax.swing.GroupLayout.DEFAULT_SIZE, 179,
                                            Short.MAX_VALUE).addContainerGap()));

            dayTab.setLayout(tabLayout);
            tempWorkerDays.addTab(day.getNameOfDay(), dayTab);

        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     */
    private void initComponents() {

        // SWAP 1, TEAM 9
        // SMELL: Long Method - This method below clearly is much longer than
        // should be in one function.
        // If we broke out this function into multiple sub-functions, we would
        // be able to modify the
        // worker setup menu much more easily, and would be able to add more
        // functionality to it.
        // SWAP 2, TEAM 10
        // Refactor: Moved out the parts where the buttons are initialized, and the
        // content pane's layout is made. Similar to some of the earlier sections
        // with the groups, since much of it is used to get a certain layout, there
        // isn't going to be a nice way to move the components of the code around 
        // without large amounts of trial and error when trying to make it reusable.
        this.workerTabPanel = new javax.swing.JTabbedPane();
        this.addButton = new javax.swing.JButton();
        this.removeButton = new javax.swing.JButton();
        this.nextButton = new javax.swing.JButton();
        this.backButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Worker Setup");

        this.initButtons();

        getContentPane().setLayout(this.getContentPaneLayout());
        pack();
    }

    private javax.swing.GroupLayout getContentPaneLayout(){
        JScrollPane outside = new JScrollPane();
        outside.setViewportView(this.workerTabPanel);
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        layout.setHorizontalGroup(layout
                .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(
                        layout.createSequentialGroup()
                                .addGap(106, 106, 106)
                                .addComponent(this.backButton, javax.swing.GroupLayout.PREFERRED_SIZE, 65,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(this.nextButton, javax.swing.GroupLayout.PREFERRED_SIZE, 65,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(
                        layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(
                                        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(
                                                        layout.createSequentialGroup()
                                                                .addComponent(this.addButton,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE, 136,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(
                                                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED,
                                                                        82, Short.MAX_VALUE)
                                                                .addComponent(this.removeButton,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE, 136,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addComponent(outside, javax.swing.GroupLayout.PREFERRED_SIZE, 0,
                                                        Short.MAX_VALUE)).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
                layout.createSequentialGroup()
                        .addComponent(outside, javax.swing.GroupLayout.PREFERRED_SIZE, 330,
                                javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(
                                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(this.addButton).addComponent(this.removeButton))
                        .addGap(18, 18, 18)
                        .addGroup(
                                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(this.nextButton).addComponent(this.backButton))
                        .addGap(0, 8, Short.MAX_VALUE)));

        return layout;
    }
    
    /**
     * @param evt
     */
    private void nextButtonActionPerformed(java.awt.event.ActionEvent evt) {
        ArrayList<Worker> workers = new ArrayList<Worker>();
        boolean allGood = true;
        for (JPanel tab : this.workerTabs) {
            ArrayList<Day> workerDays = new ArrayList<Day>();
            JTextField nameArea = (JTextField) tab.getComponent(2);
            if (nameArea.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "You have not entered a name for every worker.");
                allGood = false;
                break;
            }
            JTabbedPane daysPane = (JTabbedPane) tab.getComponents()[0];
            for (int i = 0; i < daysPane.getTabCount(); i++) {

                JPanel day = (JPanel) daysPane.getComponent(i);

                JScrollPane pane = (JScrollPane) day.getComponent(0);

                JViewport view = (JViewport) pane.getComponent(0);

                JPanel p = (JPanel) view.getComponent(0);

                ArrayList<Object> jobNames = new ArrayList<Object>();

                for (Component job : p.getComponents()) {
                    if (((JCheckBox) job).isSelected()) {
                        jobNames.add(((JCheckBox) job).getText());
                    }
                }
                workerDays.add(new Day(daysPane.getTitleAt(i), jobNames));
            }
            workers.add(new Worker(nameArea.getText(), workerDays));
        }
        if (allGood) {
            HTMLGenerator.reset();
            Main.setWorkers(workers);
            Main.setSchedule(new Schedule(Main.getDays(), Main.getWorkers()));
            Main.dumpConfigFile();
            Main.cal = new CalendarGUI(Main.getSchedule());
            Main.toggleCalendar();
            Main.toggleWorkerSetup();
        }
    }
    
    private void initButtons(){
        this.addButton.setText("Add Worker");
        this.addButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });

        this.removeButton.setText("Remove Worker");
        this.removeButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeButtonActionPerformed(evt);
            }
        });

        this.nextButton.setText("Next");
        this.nextButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nextButtonActionPerformed(evt);
            }
        });

        this.backButton.setText("Back");
        this.backButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backButtonActionPerformed(evt);
            }
        });
    }

    /**
     * @param evt
     */
    private void backButtonActionPerformed(java.awt.event.ActionEvent evt) {
        Main.toggleConfig();
        Main.toggleWorkerSetup();
    }

    /**
     * @param evt
     */
    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {
        this.addWorker();
    }

    /**
     * @param evt
     */
    private void removeButtonActionPerformed(java.awt.event.ActionEvent evt) {
        this.workerTabs.remove(this.workerTabPanel.getSelectedComponent());
        this.workerTabPanel.remove(this.workerTabPanel.getSelectedIndex());
    }

    private javax.swing.JButton addButton;
    private javax.swing.JButton backButton;
    private javax.swing.JButton nextButton;
    private javax.swing.JButton removeButton;
    private javax.swing.JTabbedPane workerTabPanel;
}
