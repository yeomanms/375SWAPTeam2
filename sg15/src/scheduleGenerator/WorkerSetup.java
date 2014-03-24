package scheduleGenerator;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * 
 * @author schneimd
 */

//SMELL: Large class - This class is quite large and the project as a whole would probably be improved if this classes method were
//refactored.
public class WorkerSetup extends javax.swing.JFrame {

	private ArrayList<Day> days;
	private ArrayList<JPanel> workerTabs;
    private ArrayList<ArrayList<Date>> calendars;

	/**
	 * Allows for editing of already made workers.
	 * 
	 * @param workers
	 */
	public WorkerSetup(ArrayList<Worker> workers) {
		this.setPreferredSize(new Dimension(425, 450));
		this.workerTabs = new ArrayList<JPanel>();
        this.calendars = new ArrayList<ArrayList<Date>>();
		initComponents();
		for (int c = 0; c < workers.size(); c++) {
			this.addWorker();
		}

		for (int c = 0; c < workers.size(); c++) {
			JTextField nameArea = (JTextField) this.workerTabs.get(c)
					.getComponent(2);
			nameArea.setText(workers.get(c).getName());
			JTabbedPane daysPane = (JTabbedPane) this.workerTabs.get(c)
					.getComponents()[0];
			for (int i = 0; i < daysPane.getTabCount(); i++) {
				for (int n = 0; n < workers.get(c).getDays().size(); n++) {
					if (daysPane.getTitleAt(i).equals(
							workers.get(c).getDays().get(n).getNameOfDay())) {

						JPanel day = (JPanel) daysPane.getComponent(i);
						JScrollPane pane = (JScrollPane) day.getComponent(0);
						JViewport view = (JViewport) pane.getComponent(0);
						JPanel p = (JPanel) view.getComponent(0);

						for (Component job : p.getComponents()) {
							for (String workerJob : workers.get(c).getDays().get(n)
									.getJobs()) {
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
        this.calendars = new ArrayList<ArrayList<Date>>();
		initComponents();
		addWorker();
	}

	private void addWorker() {
		this.days = Main.getDays();
		javax.swing.JTabbedPane tempWorkerDays = new javax.swing.JTabbedPane();
		javax.swing.JTextField tempWorkerName = new javax.swing.JTextField();
		javax.swing.JPanel tempWorkerTab = new javax.swing.JPanel();

        this.calendars.add(new ArrayList<Date>());

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

			// Create a tab Panel for the Worker Tab and add the inputs.

			JPanel dayTab = new JPanel();

			// Set veritcal and horizontal layouts.
			javax.swing.GroupLayout sundayTab1Layout = new javax.swing.GroupLayout(
					dayTab);
			dayTab.setLayout(sundayTab1Layout);
			sundayTab1Layout
					.setHorizontalGroup(sundayTab1Layout
							.createParallelGroup(
									javax.swing.GroupLayout.Alignment.LEADING)
							.addGroup(
									sundayTab1Layout
											.createSequentialGroup()
											.addGap(63, 63, 63)
											.addGroup(
													sundayTab1Layout
															.createParallelGroup(
																	javax.swing.GroupLayout.Alignment.LEADING)
															.addComponent(
																	tempDayJobPane,
																	javax.swing.GroupLayout.PREFERRED_SIZE,
																	198,
																	javax.swing.GroupLayout.PREFERRED_SIZE)
															.addComponent(
																	jobLabel))
											.addContainerGap(73,
													Short.MAX_VALUE)));

			sundayTab1Layout
					.setVerticalGroup(sundayTab1Layout
							.createParallelGroup(
									javax.swing.GroupLayout.Alignment.LEADING)
							.addGroup(
									sundayTab1Layout
											.createSequentialGroup()
											.addContainerGap()
											.addComponent(jobLabel)
											.addPreferredGap(
													javax.swing.LayoutStyle.ComponentPlacement.RELATED)
											.addComponent(
													tempDayJobPane,
													javax.swing.GroupLayout.DEFAULT_SIZE,
													179, Short.MAX_VALUE)
											.addContainerGap()));

			tempWorkerDays.addTab(day.getNameOfDay(), dayTab);

		}

		// Add a section for the worker's name
		JLabel workerNameLabel = new JLabel("Worker's Name:");

		javax.swing.GroupLayout workerTab1Layout = new javax.swing.GroupLayout(
				tempWorkerTab);
		tempWorkerTab.setLayout(workerTab1Layout);
		//: Duplicated Code - <explanation>
		workerTab1Layout
				.setHorizontalGroup(workerTab1Layout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								workerTab1Layout
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												workerTab1Layout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING)
														.addComponent(
																tempWorkerDays)
														.addGroup(
																javax.swing.GroupLayout.Alignment.TRAILING,
																workerTab1Layout
																		.createSequentialGroup()
																		.addGap(0,
																				0,
																				Short.MAX_VALUE)
																		.addComponent(
																				workerNameLabel)
																		.addPreferredGap(
																				javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
																		.addComponent(
																				tempWorkerName,
																				javax.swing.GroupLayout.PREFERRED_SIZE,
																				150,
																				javax.swing.GroupLayout.PREFERRED_SIZE)
																		.addGap(49,
																				49,
																				49)))
										.addContainerGap()));

		// Adds text area and label for name then tab area for days.
		workerTab1Layout
				.setVerticalGroup(workerTab1Layout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								workerTab1Layout
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												workerTab1Layout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(
																workerNameLabel)
														.addComponent(
																tempWorkerName,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(
												tempWorkerDays,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												249,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addContainerGap(
												javax.swing.GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)));
		// Prevents a nullPointer
		if (this.workerTabs.size() == 0) {
			this.workerTabs.add(tempWorkerTab);
			this.workerTabPanel.addTab("Worker 1", null, tempWorkerTab, "");
		} else {
			this.workerTabs.add(tempWorkerTab);
			this.workerTabPanel.addTab(
					"Worker " + String.valueOf(this.workerTabs.size()), null,
					tempWorkerTab, "");
		}
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 */
	//SMELL: Long Method - This very long method is begging to be refactored.   It is quite long and would be improved if it was
	// broken into a couple smaller methods.
	private void initComponents() {

		this.workerTabPanel = new javax.swing.JTabbedPane();
		this.addButton = new javax.swing.JButton();
		this.removeButton = new javax.swing.JButton();
		this.nextButton = new javax.swing.JButton();
		this.backButton = new javax.swing.JButton();
        this.calButton = new JButton();

        this.calButton.setText("Select Calendar");
        this.calButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                fileSelector(evt);
            }
        });

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setTitle("Worker Setup");

		this.addButton.setText("Add Worker");
		this.addButton.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				addButtonActionPerformed(evt);
			}
		});

		this.removeButton.setText("Remove Worker");
		this.removeButton
				.addActionListener(new java.awt.event.ActionListener() {
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

		JScrollPane outside = new JScrollPane();
		outside.setViewportView(this.workerTabPanel);

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(
				getContentPane());
		getContentPane().setLayout(layout);
		
		layout.setHorizontalGroup(layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(
						layout.createSequentialGroup()
								.addGap(106, 106, 106)
								.addComponent(this.backButton,
										javax.swing.GroupLayout.PREFERRED_SIZE,
										65,
										javax.swing.GroupLayout.PREFERRED_SIZE)
								.addGap(18, 18, 18)
								.addComponent(this.nextButton,
										javax.swing.GroupLayout.PREFERRED_SIZE,
										65,
										javax.swing.GroupLayout.PREFERRED_SIZE)
								.addContainerGap(
										javax.swing.GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE))
				.addGroup(
						layout.createSequentialGroup()
								.addContainerGap()
								.addGroup(
										layout.createParallelGroup(
												javax.swing.GroupLayout.Alignment.LEADING)
												.addGroup(
														layout.createSequentialGroup()
																.addComponent(
																		this.addButton,
																		javax.swing.GroupLayout.PREFERRED_SIZE,
																		136,
																		javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(
                                                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED,
                                                                        2,
                                                                        Short.MAX_VALUE)
																.addComponent(
                                                                        this.calButton,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                        80,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(
                                                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED,
                                                                        2,
                                                                        Short.MAX_VALUE)
																.addComponent(
                                                                        this.removeButton,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                        136,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE))
												.addComponent(
														outside,
														javax.swing.GroupLayout.PREFERRED_SIZE,
														0, Short.MAX_VALUE))
								.addContainerGap()));
		layout.setVerticalGroup(layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(
                        layout.createSequentialGroup()
                                .addComponent(outside,
                                        javax.swing.GroupLayout.PREFERRED_SIZE,
                                        330,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(
                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(
                                        layout.createParallelGroup(
                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(this.addButton)
                                                .addComponent(this.calButton)
                                                .addComponent(this.removeButton))
                                .addGap(18, 18, 18)
                                .addGroup(
                                        layout.createParallelGroup(
                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(this.nextButton)
                                                .addComponent(this.backButton))
                                .addGap(0, 8, Short.MAX_VALUE)));

		pack();
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
				JOptionPane.showMessageDialog(this,
						"You have not entered a name for every worker.");
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
			workers.add(new Worker(nameArea.getText(), workerDays, calendars.get(getSelectedUser())));
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
        this.calendars.remove(getSelectedUser());
		this.workerTabs.remove(this.workerTabPanel.getSelectedComponent());
		this.workerTabPanel.remove(this.workerTabPanel.getSelectedIndex());
	}

    // SWAP 1, TEAM 2
    private void fileSelector(ActionEvent evt){
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "iCal", "ics");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(null);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            addCalToUser(chooser.getSelectedFile());
        }
    }

    // SWAP 1, TEAM 2
    private void addCalToUser(File selectedFile){
        String[] data = readFileAsString(selectedFile.getAbsolutePath()).split("\n");

        for(int line = 0; line < data.length; line++){
            if(data[line].contains("BEGIN:VEVENT")){
                Date d = processVEvent(line, data);
                if(d != null) calendars.get(getSelectedUser()).add(d);
            }
        }
    }

    // SWAP 1, TEAM 2
    private Date processVEvent(int line, String[] data) {
        for(; line < data.length; line++){
            if(data[line].contains("END:VEVENT")){
                return null;
            } else if(data[line].contains("DTSTART") || data[line].contains("DTEND")){
                SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
                try {
                    return formatter.parse(data[line].split(":")[1].split("T")[0]);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    // SWAP 1, TEAM 2
    private int getSelectedUser(){
        return this.workerTabPanel.getSelectedIndex();
    }

    /**
     * Reads a file into a String
     *
     * @author Frank Roetker
     *
     * @param filename
     *            name of the file
     * @return contents of the file as a String
     */
    public static String readFileAsString(String filename) {
        StringBuffer fileData = new StringBuffer(1000);
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            char[] buf = new char[1024];
            int numRead = 0;
            while ((numRead = reader.read(buf)) != -1) {
                String readData = String.valueOf(buf, 0, numRead);
                fileData.append(readData);
                buf = new char[1024];
            }
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileData.toString();
    }

	private javax.swing.JButton addButton;
	private javax.swing.JButton backButton;
	private javax.swing.JButton nextButton;
	private javax.swing.JButton removeButton;
    private JButton calButton;
	private javax.swing.JTabbedPane workerTabPanel;
}
