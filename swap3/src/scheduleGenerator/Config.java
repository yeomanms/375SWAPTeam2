/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scheduleGenerator;

import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.*;

// SWAP 1, TEAM 9
// SMELL: Large Class - This class is absolutely enormous, which would indicate
// to us
// that there is clearly a large amount of fluff or duplicate code inside that
// does not
// belong in this class. Splitting it up into smaller classes would allow for
// better
// modularity in our configuration menu.
// SWAP 2, TEAM 10
// Refactor: We greatly reduced the size of this large class. We did more than
// cut in half the number of lines and greatly reduced the interdependence

public class Config extends javax.swing.JFrame {

	private boolean firstSelection = true;
	private int numSelected = 0;
	@SuppressWarnings("rawtypes")
	private DefaultListModel[] models;

	/**
	 * Used to edit days.
	 * 
	 * @param daysi
	 */
	@SuppressWarnings("unchecked")
	public Config(ArrayList<Day> days) {
		this.models = new DefaultListModel[7];
		initDyn();
		initComponents();

		for (Day day : days) {

			// SWAP 1, TEAM 9
			// SMELL: Switch Statements - The massive list of if statements
			// below would indicate
			// to us that there are variables being repeatedly rediscovered in
			// this statement where
			// they should be defined in a class variable of a new or existing
			// class so that they
			// do not have to be rediscovered in the day value.

			// SWAP 2, TEAM 2
			// Refactor: Now there is a method within Day that without a
			// case statment determines the day number from its associated
			// string. Additionally, the different components that couldn't be
			// accessed by an id now can due to the change for "duplicate code"
			// so now the entire case statement can be removed. This also
			// removed the need for the method that was just supposed to help
			// with the internals of the case statement
			int dayIndex = Day.getNumForName(day.getNameOfDay());
			this.checkList[dayIndex].doClick();
			ArrayList<String> jobs = day.getJobs();
			for (String job : jobs) {
				this.models[dayIndex].addElement(job);
				this.jobList[dayIndex].setModel(this.models[dayIndex]);
			}
		}
	}

	/**
	 * Creates new form.
	 */
	public Config() {
		this.models = new DefaultListModel[7];
		initDyn();

		initComponents();
	}

	@SuppressWarnings("rawtypes")
	private void initDyn() {
		// SWAP 1, TEAM 9
		// SMELL: Duplicated Code - Each of these objects for every day has
		// exactly the same set of components.
		// Clearly, they could all be combined into a single UI object for each
		// day. This would greatly reduce
		// the number of objects in our UI, which would greatly improve the
		// understandability of the project.
		// SWAP 2, TEAM 10
		// Refactor: Removed the duplication by moving each of these items into
		// lists indexed by day. Allowed there to be several hundred lines of
		// duplicated code to be removed
		for (int i = 0; i < 7; i++) {
			this.scrollPaneList[i] = new javax.swing.JScrollPane();
			this.scrollPaneList[i].setPreferredSize(new Dimension(185, 150));
			this.jobList[i] = new javax.swing.JList();
			this.jobName[i] = new javax.swing.JTextField();
			this.dayLabel[i] = new javax.swing.JLabel();
			this.addJobButton[i] = new javax.swing.JButton();
			this.deleteJobButton[i] = new javax.swing.JButton();
			this.dayTab[i] = new javax.swing.JPanel();
		}
	}

	private void initComponents() {

		JPanel jPanel1 = new javax.swing.JPanel();
		this.jLabel1 = new javax.swing.JLabel();
		this.nextButton = new javax.swing.JButton();
		this.dayTabs = new javax.swing.JTabbedPane();

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setTitle("Configuration");
		setPreferredSize(new java.awt.Dimension(801, 87));
		setResizable(false);

		for (int i = 0; i < 7; i++) {
			this.checkList[i] = new JCheckBox();
			this.checkList[i].setText(Day.getNameforNum(i + 1));
			this.checkList[i].setName(Day.getNameforNum(i + 1)
					.toLowerCase() + "Check"); // NOI18N
			this.checkList[i].addItemListener(new DayHandler(this, i));
		}

		this.jLabel1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
		this.jLabel1.setText("Days:");

		this.nextButton.setText("Next");
		this.nextButton.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				nextButtonActionPerformed(evt);
			}
		});

		javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(
				jPanel1);
		jPanel1.setLayout(jPanel1Layout);
		jPanel1Layout
				.setHorizontalGroup(jPanel1Layout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								javax.swing.GroupLayout.Alignment.TRAILING,
								jPanel1Layout
										.createSequentialGroup()
										.addContainerGap(
												javax.swing.GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)
										.addComponent(this.jLabel1)
										.addGap(18, 18, 18)
										.addComponent(this.checkList[0])
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(
												this.checkList[1],
												javax.swing.GroupLayout.PREFERRED_SIZE,
												71,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
										.addComponent(this.checkList[2])
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(
												this.checkList[3],
												javax.swing.GroupLayout.PREFERRED_SIZE,
												100,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
										.addComponent(this.checkList[4])
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(
												this.checkList[5],
												javax.swing.GroupLayout.PREFERRED_SIZE,
												65,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(
												this.checkList[6],
												javax.swing.GroupLayout.PREFERRED_SIZE,
												100,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addGap(18, 18, 18)
										.addComponent(this.nextButton)
										.addGap(78, 78, 78)));
		jPanel1Layout
				.setVerticalGroup(jPanel1Layout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								jPanel1Layout
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												jPanel1Layout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING)
														.addComponent(
																this.checkList[0],
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																Short.MAX_VALUE)
														.addGroup(
																jPanel1Layout
																		.createParallelGroup(
																				javax.swing.GroupLayout.Alignment.BASELINE)
																		.addComponent(
																				this.checkList[5],
																				javax.swing.GroupLayout.DEFAULT_SIZE,
																				javax.swing.GroupLayout.DEFAULT_SIZE,
																				Short.MAX_VALUE)
																		.addComponent(
																				this.checkList[6],
																				javax.swing.GroupLayout.DEFAULT_SIZE,
																				33,
																				Short.MAX_VALUE)
																		.addComponent(
																				this.nextButton))
														.addComponent(
																this.checkList[3],
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																Short.MAX_VALUE)
														.addComponent(
																this.checkList[2],
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																Short.MAX_VALUE)
														.addGroup(
																jPanel1Layout
																		.createSequentialGroup()
																		.addComponent(
																				this.jLabel1)
																		.addGap(0,
																				0,
																				Short.MAX_VALUE))
														.addComponent(
																this.checkList[4],
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																Short.MAX_VALUE)
														.addComponent(
																this.checkList[1],
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																Short.MAX_VALUE))
										.addContainerGap()));

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(
				getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(
						layout.createSequentialGroup()
								.addComponent(jPanel1,
										javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.PREFERRED_SIZE)
								.addGap(0, 18, Short.MAX_VALUE))
				.addComponent(this.dayTabs));
		layout.setVerticalGroup(layout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				layout.createSequentialGroup()
						.addComponent(jPanel1,
								javax.swing.GroupLayout.PREFERRED_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE,
								javax.swing.GroupLayout.PREFERRED_SIZE)
						.addGap(18, 18, 18)
						.addComponent(this.dayTabs,
								javax.swing.GroupLayout.DEFAULT_SIZE, 196,
								Short.MAX_VALUE)));

		this.dayTabs.getAccessibleContext().setAccessibleName("Days Tab");

		pack();
	}// </editor-fold>


	// SWAP 1, TEAM 9
	// SMELL: Shotgun Surgery - If we chose to modify what happens when someone
	// performs an action on a day checkbox,
	// we would have to change all seven of the below functions in the same way
	// to accommodate the new functionality.
	// Changing this to be in a single function for all seven days would allow
	// us to make changes to the checkboxes
	// far more rapidly.
	// SWAP 2, TEAM 10
	// Refactor: Removed the shotgun surgery by replacing the seven different
	// methods instead with just one. Now if you want to change the gui layout
	// for configurations, add a new required field, etc. it could be done at a
	// single place
	private class DayHandler implements ItemListener {
		Config c;
		int day;

		public DayHandler(Config c, int day) {
			this.c = c;
			this.day = day;
		}

		@Override
		public void itemStateChanged(ItemEvent e) {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				c.numSelected++;
				if (c.firstSelection) {
					stretch();
				}
				c.models[day] = new DefaultListModel<Object>();
				c.jobList[day].setModel(c.models[day]);
				c.scrollPaneList[day].setViewportView(c.jobList[day]);

				c.jobName[day].setColumns(20);

				c.dayLabel[day].setText("Job Name:");

				c.addJobButton[day].setText("Add Job");
				c.addJobButton[day]
						.addActionListener(new java.awt.event.ActionListener() {
							@Override
							public void actionPerformed(
									java.awt.event.ActionEvent evt) {
								if (!c.jobName[day].getText().isEmpty()) {
									c.models[day].addElement(c.jobName[day]
											.getText());
									c.jobList[day].setModel(c.models[day]);
									c.jobName[day].setText("");
								}
							}
						});

				c.deleteJobButton[day].setText("Delete Job");
				c.deleteJobButton[day]
						.addActionListener(new java.awt.event.ActionListener() {
							@Override
							public void actionPerformed(
									java.awt.event.ActionEvent evt) {
								while (!c.jobList[day].isSelectionEmpty()) {
									int n = c.jobList[day].getSelectedIndex();
									c.models[day].remove(n);
								}

							}
						});

				javax.swing.GroupLayout tabLayout = new javax.swing.GroupLayout(
						c.dayTab[day]);
				c.dayTab[day].setLayout(tabLayout);
				tabLayout
						.setHorizontalGroup(tabLayout
								.createParallelGroup(
										javax.swing.GroupLayout.Alignment.LEADING)
								.addGroup(
										tabLayout
												.createSequentialGroup()
												.addContainerGap()
												.addComponent(
														c.scrollPaneList[day],
														javax.swing.GroupLayout.PREFERRED_SIZE,
														182,
														javax.swing.GroupLayout.PREFERRED_SIZE)
												.addGap(18, 18, 18)
												.addGroup(
														tabLayout
																.createParallelGroup(
																		javax.swing.GroupLayout.Alignment.LEADING)
																.addGroup(
																		tabLayout
																				.createSequentialGroup()
																				.addComponent(
																						c.dayLabel[day])
																				.addGroup(
																						tabLayout
																								.createParallelGroup(
																										javax.swing.GroupLayout.Alignment.LEADING)
																								.addGroup(
																										tabLayout
																												.createSequentialGroup()
																												.addGap(14,
																														14,
																														14)
																												.addComponent(
																														c.addJobButton[day]))
																								.addGroup(
																										tabLayout
																												.createSequentialGroup()
																												.addPreferredGap(
																														javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																												.addComponent(
																														c.jobName[day],
																														javax.swing.GroupLayout.PREFERRED_SIZE,
																														100,
																														javax.swing.GroupLayout.PREFERRED_SIZE))))
																.addComponent(
																		c.deleteJobButton[day]))
												.addContainerGap(431,
														Short.MAX_VALUE)));
				tabLayout
						.setVerticalGroup(tabLayout
								.createParallelGroup(
										javax.swing.GroupLayout.Alignment.LEADING)
								.addGroup(
										tabLayout
												.createSequentialGroup()
												.addContainerGap()
												.addGroup(
														tabLayout
																.createParallelGroup(
																		javax.swing.GroupLayout.Alignment.LEADING,
																		false)
																.addGroup(
																		tabLayout
																				.createSequentialGroup()
																				.addGroup(
																						tabLayout
																								.createParallelGroup(
																										javax.swing.GroupLayout.Alignment.BASELINE)
																								.addComponent(
																										c.jobName[day],
																										javax.swing.GroupLayout.PREFERRED_SIZE,
																										javax.swing.GroupLayout.DEFAULT_SIZE,
																										javax.swing.GroupLayout.PREFERRED_SIZE)
																								.addComponent(
																										c.dayLabel[day]))
																				.addPreferredGap(
																						javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																				.addComponent(
																						c.addJobButton[day])
																				.addPreferredGap(
																						javax.swing.LayoutStyle.ComponentPlacement.RELATED,
																						javax.swing.GroupLayout.DEFAULT_SIZE,
																						Short.MAX_VALUE)
																				.addComponent(
																						c.deleteJobButton[day]))
																.addComponent(
																		c.scrollPaneList[day],
																		javax.swing.GroupLayout.PREFERRED_SIZE,
																		javax.swing.GroupLayout.DEFAULT_SIZE,
																		javax.swing.GroupLayout.PREFERRED_SIZE))
												.addContainerGap(25,
														Short.MAX_VALUE)));
				c.dayTabs.addTab(Day.getNameforNum(day + 1),
						c.dayTab[day]);
			} else {
				c.numSelected--;
				stretch();
				c.dayTabs.remove(c.dayTab[day]);
			}
		}
	}

	/**
	 * @param evt
	 */
	private void nextButtonActionPerformed(java.awt.event.ActionEvent evt) {
		ArrayList<Day> days = new ArrayList<Day>();
		for (int i = 0; i < 7; i++) {
			if (this.checkList[i].isSelected()) {
				days.add(new Day(Day.getNameforNum(i + 1),
						new ArrayList<Object>(Arrays.asList(this.models[i]
								.toArray()))));
			}
		}

		if (days.size() > 0) {
			boolean hasJobs = true;
			int i = 0;
			while (hasJobs && i < days.size()) {
				if (days.get(i).getJobs().size() == 0) {
					hasJobs = false;
				}
				i++;
			}
			if (hasJobs) {
				Main.setDays(days);
				Main.wSet = new WorkerSetup();
				Main.toggleWorkerSetup();
				Main.config = this;
				Main.toggleConfig();
			} else {
				JOptionPane.showMessageDialog(this,
						"You must have at least one job each day.");
			}
		} else {
			JOptionPane.showMessageDialog(this, "You have not added any days.");
		}
	}

    // SWAP 3, TEAM 2
    // removed duplicated code from the stretch function.
	private void stretch() {
		if (this.firstSelection = this.numSelected > 0) {
			this.setSize(801, 290);
            return;
        }
		this.setSize(801, 87);
	}

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String args[]) {

		try {
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager
					.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
        // SWAP 3, TEAM 2
        // Removed a bunch of duplicate catch statments.
        // This reduces the size of the file, like the last team
        // wanted.
		} catch (Exception ex) {
			java.util.logging.Logger.getLogger(Config.class.getName()).log(
					java.util.logging.Level.SEVERE, null, ex);
		}
		// </editor-fold>

		/* Create and display the form */
		java.awt.EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				new Config().setVisible(true);
			}
		});
	}

	// SWAP 1, TEAM 9
	// QUALITY CHANGES
	// This code breaks out the job-adding lines from the
	// nextButtonActionPerformed
	// function, so there is a great deal less code duplication.
	public void nextButtonActionAddJobsToDay(int modelIndex, String dayName,
			ArrayList<Day> days) {
		ArrayList<Object> currentDay = new ArrayList<Object>();
		List<Object> jobs = Arrays.asList(this.models[modelIndex].toArray());
		currentDay.addAll(jobs);
		days.add(new Day(dayName, currentDay));
	}


	private JScrollPane[] scrollPaneList = new JScrollPane[7];
	private JButton[] addJobButton = new JButton[7];
	private JButton[] deleteJobButton = new JButton[7];
	private JList[] jobList = new JList[7];
	private JTextField[] jobName = new JTextField[7];
	private JLabel[] dayLabel = new JLabel[7];
	private JPanel[] dayTab = new JPanel[7];
	private JCheckBox[] checkList = new JCheckBox[7];


    // SWAP 3, TEAM 2
    // We should remove the property jPanel1, becasue it
    // is only used within one method.
	private javax.swing.JTabbedPane dayTabs;
	private javax.swing.JLabel jLabel1;
//	private javax.swing.JPanel jPanel1;
	private javax.swing.JButton nextButton;
}
