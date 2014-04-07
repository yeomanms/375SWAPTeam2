package scheduleGenerator;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.NavigableSet;
import java.util.Set;
import java.util.TreeMap;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 * 
 * @author schneimd
 */

// SWAP 1, TEAM 9
// SMELL: Divergent Change - This class has many responsibilities, and if we would like
// to change something in the underlying code, it is closely entangled with the gui code.
// This class thus requires us to change a great deal of gui code every single time we 
// want to change a piece of functionality.
public class CalendarGUI extends javax.swing.JFrame {

	private Schedule schedule;
	private GregorianCalendar cal;
	private TreeMap<String, TreeMap<String, Worker>> scheduleMap;
	private int currentMonth;
	private String monthName;
	@SuppressWarnings("unused")
	private int earliestYear, earliestMonth, earliestDay;
	private int monthsAhead = 0;
	private int yearsAhead = 0;

	/**
	 * Creates new form Calendar
	 * 
	 * @param schd
	 */
	public CalendarGUI(Schedule schd) {
		this.schedule = schd;
		this.scheduleMap = this.schedule.getSchedule();
		String[] earliest = this.scheduleMap.firstKey().split("/");
		this.earliestYear = Integer.parseInt(earliest[0]);
		this.earliestMonth = Integer.parseInt(earliest[1]);
		this.earliestDay = Integer.parseInt(earliest[2]);
		this.cal = new GregorianCalendar();
		initComponents();
		this.fillTableForThisMonth();
	}

	private void setTitleMonth(int n, int year) {

		// SWAP 1, TEAM 9
		// QUALITY CHANGES
		// This small piece of code has more error checking than the previous
		// iteration and performs the same action in far less code, 
		// with no code duplication. We did not consider this minor addition of
		// error checking to be a wholesale change.
		String monthName;
		
		if (!(n >= 1 && n <= 12)){
			// Fail	Gracefully
			monthName = "Invalid";
		}
		else{
			monthName = new DateFormatSymbols().getMonths()[n-1];
		}
		
		this.monthTitle.setText(monthName + " " + year);
		this.monthName = monthName + " " + year;
	}

	/**
	 * Displays the calendar for the current month based on the computers month.
	 * 
	 */
	public void fillTableForThisMonth() {
		int currentYear = new GregorianCalendar().get(Calendar.YEAR);
		this.currentMonth = new GregorianCalendar().get(Calendar.MONTH) + 1;
		this.setTitleMonth(this.currentMonth, currentYear);
		this.monthsAhead = 0;
		this.yearsAhead = 0;

		String keyStart = currentYear + "/"
				+ String.format("%02d", this.currentMonth);
		String currentKey = "";

		// Generates calendar for current month if none exists
		while (currentKey.equals("")) {
			Set<String> keys = this.scheduleMap.keySet();
			for (String key : keys) {
				if (key.startsWith(keyStart)) {
					currentKey = key;
					break;
				}
			}
			if (currentKey.equals("")) {
				//Thread t = new Thread(this.schedule);
				//t.start();
				this.schedule.calculateNextMonth();
			}
		}

		DefaultTableModel table = new DefaultTableModel(new Object[0][0],
				new String[0][0]);

		this.cal = new GregorianCalendar(currentYear, this.currentMonth - 1, 1);

		while (this.currentMonth == this.cal.get(Calendar.MONTH) + 1) {
			String tempKey = this.cal.get(Calendar.YEAR)
					+ "/"
					+ String.format("%02d", (this.cal.get(Calendar.MONTH) + 1))
					+ "/"
					+ String.format("%02d", this.cal.get(Calendar.DAY_OF_MONTH));
			if (this.scheduleMap.containsKey(tempKey)) {

				int numOfJobs = this.scheduleMap.get(tempKey).size();
				String[] colData = new String[numOfJobs];
				int i = 0;

				for (String key : this.scheduleMap.get(tempKey).keySet()) {
					colData[i] = key + ": "
							+ this.scheduleMap.get(tempKey).get(key).getName();
					i++;
				}

				String numDate = (this.cal.get(Calendar.MONTH) + 1)
						+ "/"
						+ String.format("%02d",
								this.cal.get(Calendar.DAY_OF_MONTH)) + "/"
						+ String.format("%02d", this.cal.get(Calendar.YEAR));
				String colTitle = Day.getNameforNum(this.cal
						.get(Calendar.DAY_OF_WEEK)) + " (" + numDate + ")";
				table.addColumn(colTitle, colData);

			}
			this.cal.add(Calendar.DATE, 1);
		}

		HTMLGenerator.addMonth(this.monthName, table);
		this.scheduleTable.setModel(table);
	}

	/**
	 * Displays the next month from current month.
	 * 
	 */
	public void fillTableMonthAhead() {
		int currentYear = new GregorianCalendar().get(Calendar.YEAR);
		this.monthsAhead++;
		int showMonth = new GregorianCalendar().get(Calendar.MONTH)
				+ this.monthsAhead + 1;
		this.yearsAhead = 0;
		while (showMonth > 12) {
			currentYear++;
			showMonth -= 12;
			this.yearsAhead++;
		}
		this.setTitleMonth(showMonth, currentYear);

		String keyStart = currentYear + "/" + String.format("%02d", showMonth);
		String currentKey = "";

		// Generates calendar for current month if none exists
		while (currentKey.equals("")) {
			Set<String> keys = this.scheduleMap.keySet();
			for (String key : keys) {
				if (key.startsWith(keyStart)) {
					currentKey = key;
					break;
				}
			}
			if (currentKey.equals("")) {
				//Thread t = new Thread(this.schedule);
				//t.start();
				this.schedule.calculateNextMonth();
			}
		}

		DefaultTableModel table = new DefaultTableModel(new Object[0][0],
				new String[0][0]);
		this.cal = new GregorianCalendar(currentYear, showMonth - 1, 1);

		while (showMonth == this.cal.get(Calendar.MONTH) + 1) {
			String tempKey = this.cal.get(Calendar.YEAR)
					+ "/"
					+ String.format("%02d", (this.cal.get(Calendar.MONTH) + 1))
					+ "/"
					+ String.format("%02d", this.cal.get(Calendar.DAY_OF_MONTH));
			if (this.scheduleMap.containsKey(tempKey)) {

				int numOfJobs = this.scheduleMap.get(tempKey).size();
				String[] colData = new String[numOfJobs];
				int i = 0;

				for (String key : this.scheduleMap.get(tempKey).keySet()) {
					colData[i] = key + ": "
							+ this.scheduleMap.get(tempKey).get(key).getName();
					i++;
				}

				String numDate = String.format("%02d",
						(this.cal.get(Calendar.MONTH) + 1))
						+ "/"
						+ String.format("%02d",
								this.cal.get(Calendar.DAY_OF_MONTH))
						+ "/"
						+ this.cal.get(Calendar.YEAR);
				String colTitle = Day.getNameforNum(this.cal
						.get(Calendar.DAY_OF_WEEK)) + " (" + numDate + ")";
				table.addColumn(colTitle, colData);

			}
			this.cal.add(Calendar.DATE, 1);

		}
		HTMLGenerator.addMonth(this.monthName, table);
		this.scheduleTable.setModel(table);
	}

	/**
	 * Displays the last months from current month.
	 * 
	 */
	public void fillTableMonthBack() {
		int tempMonths = this.monthsAhead;
		if ((new GregorianCalendar().get(Calendar.MONTH) + tempMonths) % 12 == 0) {
			this.yearsAhead--;
		}
		int currentYear = new GregorianCalendar().get(Calendar.YEAR)
				+ this.yearsAhead;
		this.monthsAhead--;
		int monthsToAdd = this.monthsAhead;
		while (monthsToAdd < -11) {
			monthsToAdd += 12;
			currentYear--;
			this.yearsAhead--;
		}
		int showMonth = new GregorianCalendar().get(Calendar.MONTH)
				+ monthsToAdd + 1;

		while (showMonth > 12) {
			showMonth -= 12;
		}

		if (currentYear < this.earliestYear
				|| (currentYear == this.earliestYear && showMonth < this.earliestMonth)) {
			this.monthsAhead++;

		} else {
			this.setTitleMonth(showMonth, currentYear);

			String keyStart = currentYear + "/"
					+ String.format("%02d", showMonth);
			String currentKey = "";

			// Generates calendar for current month if none exists
			while (currentKey.equals("")) {
				Set<String> keys = this.scheduleMap.keySet();
				for (String key : keys) {
					if (key.startsWith(keyStart)) {
						currentKey = key;
						break;
					}
				}
				if (currentKey.equals("")) {
					//Thread t = new Thread(this.schedule);
					//t.start();
					this.schedule.calculateNextMonth();
				}
			}

			DefaultTableModel table = new DefaultTableModel(new Object[0][0],
					new String[0][0]);
			this.cal = new GregorianCalendar(currentYear, showMonth - 1, 1);

			while (showMonth == this.cal.get(Calendar.MONTH) + 1) {
				String tempKey = this.cal.get(Calendar.YEAR)
						+ "/"
						+ String.format("%02d",
								(this.cal.get(Calendar.MONTH) + 1))
						+ "/"
						+ String.format("%02d",
								this.cal.get(Calendar.DAY_OF_MONTH));
				if (this.scheduleMap.containsKey(tempKey)) {

					int numOfJobs = this.scheduleMap.get(tempKey).size();
					String[] colData = new String[numOfJobs];
					int i = 0;

					for (String key : this.scheduleMap.get(tempKey).keySet()) {
						colData[i] = key
								+ ": "
								+ this.scheduleMap.get(tempKey).get(key)
										.getName();
						i++;
					}

					String numDate = String.format("%02d",
							(this.cal.get(Calendar.MONTH) + 1))
							+ "/"
							+ String.format("%02d",
									this.cal.get(Calendar.DAY_OF_MONTH))
							+ "/"
							+ this.cal.get(Calendar.YEAR);
					String colTitle = Day.getNameforNum(this.cal
							.get(Calendar.DAY_OF_WEEK)) + " (" + numDate + ")";
					table.addColumn(colTitle, colData);

				}
				this.cal.add(Calendar.DATE, 1);
			}

			this.scheduleTable.setModel(table);
			HTMLGenerator.addMonth(this.monthName, table);
		}

	}

	private void initComponents() {

		this.monthTitle = new javax.swing.JLabel();
        JButton previousMonthButton = new javax.swing.JButton();
        JButton nextMonthButton = new javax.swing.JButton();
        JScrollPane jScrollPane1 = new javax.swing.JScrollPane();
        JTable scheduleTable = new javax.swing.JTable();
        JPopupMenu popup = new javax.swing.JPopupMenu();
        JMenuBar menuBar = new javax.swing.JMenuBar();
        JMenu fileMenu = new javax.swing.JMenu();
        JMenuItem saveChanges = new javax.swing.JMenuItem();
        JMenu editMenu = new javax.swing.JMenu();
        JMenuItem editWorkers = new javax.swing.JMenuItem();
        JMenuItem editDays = new javax.swing.JMenuItem();
        JMenu generateMenu = new javax.swing.JMenu();
        JMenuItem genHtml = new javax.swing.JMenuItem();
        JMenuItem generateText = new javax.swing.JMenuItem();
        JMenu fontMenu = new javax.swing.JMenu();
        JMenuItem comicMenuItem = new javax.swing.JMenuItem();
		JMenuItem timesMenuItem = new javax.swing.JMenuItem();

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setTitle("Calendar");

		this.monthTitle.setFont(new java.awt.Font("Tahoma", 1, 24));
		this.monthTitle.setText("Month Name Here");
		
		// SWAP 1 TEAM 9
		// ADDITIONAL FEATURE
		// The Month title is also centered in between the previous and next
		// month buttons
		this.monthTitle.setHorizontalAlignment(SwingConstants.CENTER);
		previousMonthButton.setText("<");
		previousMonthButton
				.addActionListener(new java.awt.event.ActionListener() {
					@Override
					public void actionPerformed(java.awt.event.ActionEvent evt) {
						previousMonthActionPerformed(evt);
					}
				});

		
		nextMonthButton.setText(">");
		nextMonthButton
				.addActionListener(new java.awt.event.ActionListener() {
					@Override
					public void actionPerformed(java.awt.event.ActionEvent evt) {
						nextMonthActionPerformed(evt);
					}
				});

		this.scheduleTable.setModel(new javax.swing.table.DefaultTableModel(
				new Object[][] { { null, null, null }, { null, null, null },
						{ null, null, null }, { null, null, null } },
				new String[] { "Monday (10/22/2012)", "Wednesday (10/24/12)",
						"Thursday (10/26/12)" }));
		this.scheduleTable.setColumnSelectionAllowed(true);
		this.scheduleTable.getTableHeader().setReorderingAllowed(false);
		
		for(Worker i:this.schedule.getWorkers())
		{
			final Worker input = i;
			popup.add(new JMenuItem(input.getName())).addActionListener(new java.awt.event.ActionListener() {
				@Override
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					editCell(input);
				}
			});
		}
		this.scheduleTable.setComponentPopupMenu(popup);
		
		jScrollPane1.setViewportView(this.scheduleTable);

		fileMenu.setText("File");
		
		// SWAP 1, TEAM 9
		// QUALITY CHANGES
		// Applied the AcceleratorText function to this code.
		/*
		this.saveChanges.setAccelerator(javax.swing.KeyStroke.getKeyStroke(
				java.awt.event.KeyEvent.VK_S,
				java.awt.event.InputEvent.CTRL_MASK));
		this.saveChanges.setText("Save Changes");
		*/
		acceleratorText(saveChanges, java.awt.event.KeyEvent.VK_S, "Save Changes");
		saveChanges.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				saveChangesActionPerformed(evt);
			}
		});
		fileMenu.add(saveChanges);
		
		menuBar.add(fileMenu);

		editMenu.setText("Edit");
		
		// SWAP 1, TEAM 9
		// QUALITY CHANGES
		// Applied the AcceleratorText function to this code.
		/*
		this.editWorkers.setAccelerator(javax.swing.KeyStroke.getKeyStroke(
				java.awt.event.KeyEvent.VK_W,
				java.awt.event.InputEvent.CTRL_MASK));
		this.editWorkers.setText("Edit Workers");
		 */
		acceleratorText(editWorkers, java.awt.event.KeyEvent.VK_W, "Edit Workers");
		editWorkers.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				editWorkersActionPerformed(evt);
			}
		});


		editMenu.add(editWorkers);

		// SWAP 1, TEAM 9
		// QUALITY CHANGES
		// Applied the AcceleratorText function to this code.
		/*
		this.editDays.setAccelerator(javax.swing.KeyStroke.getKeyStroke(
				java.awt.event.KeyEvent.VK_D,
				java.awt.event.InputEvent.CTRL_MASK));
		this.editDays.setText("Edit Days");
		*/
		acceleratorText(editDays, java.awt.event.KeyEvent.VK_D, "Edit Days");
		editDays.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				editDaysActionPerformed(evt);
			}
		});
		editMenu.add(editDays);

		menuBar.add(editMenu);

		generateMenu.setText("Generate");

		// SWAP 1, TEAM 9
		// QUALITY CHANGES
		// Applied the AcceleratorText function to this code.
		/*
		this.genHtml.setAccelerator(javax.swing.KeyStroke.getKeyStroke(
				java.awt.event.KeyEvent.VK_H,
				java.awt.event.InputEvent.CTRL_MASK));
		this.genHtml.setText("Generate Web Page");
		*/
		acceleratorText(genHtml, java.awt.event.KeyEvent.VK_H, "Generate Web Page");
		genHtml.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				genHtmlActionPerformed(evt);
			}
		});
		generateMenu.add(genHtml);

		// SWAP 1, TEAM 9
		// QUALITY CHANGES
		// Applied the AcceleratorText function to this code.
		/*
		this.generateText.setAccelerator(javax.swing.KeyStroke.getKeyStroke(
				java.awt.event.KeyEvent.VK_T,
				java.awt.event.InputEvent.CTRL_MASK));
		this.generateText.setText("Generate Text");
		*/
		acceleratorText(generateText, java.awt.event.KeyEvent.VK_T, "Generate Text");
		generateText
				.addActionListener(new java.awt.event.ActionListener() {
					@Override
					public void actionPerformed(java.awt.event.ActionEvent evt) {
						generateTextActionPerformed(evt);
					}
				});
		generateMenu.add(generateText);

		menuBar.add(generateMenu);
		
		fontMenu.setText("Font Options");

        // SWAP 3, TEAM 2
        // improved font size to make it not as impossible to read
		comicMenuItem.addActionListener(new java.awt.event.ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                setUIFont(new javax.swing.plaf.FontUIResource(new Font("Comic Sans MS",Font.PLAIN, 14)));
                Main.toggleCalendar();
                Main.cal = new CalendarGUI(schedule);
                Main.toggleCalendar();
            }
		});
		comicMenuItem.setText("Comic Sans");
		
		fontMenu.add(comicMenuItem);

        // SWAP 3, TEAM 2
        // improved font size to make it not as impossible to read
		timesMenuItem.addActionListener(new java.awt.event.ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                setUIFont(new javax.swing.plaf.FontUIResource(new Font("Times New Roman",Font.PLAIN, 14)));
                Main.toggleCalendar();
                Main.cal = new CalendarGUI(schedule);
                Main.toggleCalendar();
            }
        });
		
		timesMenuItem.setText("Times New Roman");
		
		fontMenu.add(timesMenuItem);
		menuBar.add(fontMenu);

		setJMenuBar(menuBar);

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(
				getContentPane());
		getContentPane().setLayout(layout);
		
		// SWAP 1, TEAM 9
		// SMELL: Message Chains - the below mass of code is actually a few giant, broken lines.
		// There a tremendous number of nested calls to subfunctions of subfunctions, which is
		// something that creates high coupling and is therefore very difficult to change in the
		// future. Making only a small change to the calendar's gui would be exceedingly difficult.
		layout.setHorizontalGroup(layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addComponent(jScrollPane1,
						javax.swing.GroupLayout.DEFAULT_SIZE, 1002,
						Short.MAX_VALUE)
				.addGroup(
						layout.createSequentialGroup()
								.addContainerGap()
								.addComponent(previousMonthButton)
								.addPreferredGap(
										javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								// SWAP 1, TEAM 9
								// ADDITIONAL FEATURE
								// I fixed the next month button in a single place! Hurrah!
								.addComponent(this.monthTitle, 200, 200, 200)
								.addPreferredGap(
										javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(nextMonthButton)
								.addGap(0, 0, Short.MAX_VALUE)));
		layout.setVerticalGroup(layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(
						layout.createSequentialGroup()
								.addContainerGap(18, Short.MAX_VALUE)
								.addGroup(
										layout.createParallelGroup(
												javax.swing.GroupLayout.Alignment.LEADING,
												false)
												.addComponent(
														this.monthTitle,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														Short.MAX_VALUE)
												.addComponent(
														previousMonthButton,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														Short.MAX_VALUE)
												.addComponent(
														nextMonthButton,
														javax.swing.GroupLayout.PREFERRED_SIZE,
														29,
														javax.swing.GroupLayout.PREFERRED_SIZE))
								.addPreferredGap(
										javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
								.addComponent(jScrollPane1,
										javax.swing.GroupLayout.PREFERRED_SIZE,
										265,
										javax.swing.GroupLayout.PREFERRED_SIZE)));

		pack();
	}

	private void editWorkersActionPerformed(java.awt.event.ActionEvent evt) {
		Main.wSet = new WorkerSetup(this.schedule.getWorkers());
		Main.toggleWorkerSetup();
		Main.toggleCalendar();
	}

	private void editDaysActionPerformed(java.awt.event.ActionEvent evt) {
		Main.config = new Config(Main.getDays());
		Main.toggleConfig();
		Main.toggleCalendar();
	}

	/**
	 * @param evt
	 */
	private void previousMonthActionPerformed(java.awt.event.ActionEvent evt) {
		this.fillTableMonthBack();
	}

	/**
	 * @param evt
	 */
	private void nextMonthActionPerformed(java.awt.event.ActionEvent evt) {
		this.fillTableMonthAhead();
	}

	/**
	 * @param evt
	 */
	private void genHtmlActionPerformed(java.awt.event.ActionEvent evt) {
		HTMLGenerator.writeHtml();
	}

	/**
	 * @param evt
	 */
	private void generateTextActionPerformed(java.awt.event.ActionEvent evt) {
		NavigableSet<String> keySet = this.scheduleMap.navigableKeySet();
		String textOutput = new String();
		File readout = new File("Calendar.txt");
		ArrayList<String> dutyRows = new ArrayList<String>();

		int column = 1;
		for (String i : keySet) {
			textOutput += String.format("%-30s", "|" + i);
			NavigableSet<String> valueSet = this.scheduleMap.get(i)
					.navigableKeySet();
			int row = 0;
			for (String j : valueSet) {
				if (dutyRows.size() <= row)
					dutyRows.add("");
				String newCol = dutyRows.get(row) + "|" + j + ": "
						+ this.scheduleMap.get(i).get(j).getName();

				dutyRows.set(row,
						String.format("%-" + 30 * column + "s", newCol));
				row += 1;
			}
			column += 1;
		}

		for (String i : dutyRows) {
			textOutput += "\n" + i;
		}
		
		char[] letterOutput = textOutput.toCharArray();

		try {
			readout.createNewFile();

			FileWriter outFile = new FileWriter(readout);
			for(char i:letterOutput)
				outFile.write(i);
			outFile.close();
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}

	/**
	 * @param evt
	 */
	private void saveChangesActionPerformed(java.awt.event.ActionEvent evt) {
		Main.dumpConfigFile();
	}

	// SWAP 1, TEAM 9
	// SMELL: Speculative Generality - This piece of code below is for the undo button, but the
	// undo button does not appear to be implemented. The author apppears to have put in this code 
	// in the hope of implementing the undo button at some point. This code should simply be deleted 
	// to avoid confusing any people who may decide to utilize this codebase. 
	// SWAP 2, TEAM 10
	// Refactor: Removed the unuseful code as well as the unused pieces that reference it
	
	private void editCell(Worker input)
	{
		int i = this.scheduleTable.getSelectedRow();
		int j = this.scheduleTable.getSelectedColumn();
		if(this.scheduleTable.getValueAt(i,j) != null)
		{
			System.out.println(this.scheduleTable.getColumnName(j));
			String job = this.scheduleTable.getValueAt(i,j).toString().split(":")[0];
			String date = this.scheduleTable.getColumnName(j).split(" ")[1];
			date = date.substring(1,date.length()-1);
			String[] dateNums = date.split("/");
			date = dateNums[2] + "/" + dateNums[0] + "/" + dateNums[1];
			System.out.println(date);
			this.scheduleMap.get(date).put(job,input);
			this.scheduleTable.setValueAt(job + ": " + input.getName(),i,j);
		}
	}
	
	// SWAP 1, TEAM 9\
	// QUALITY CHANGES
	// This function combines the two long function calls repeatedly copy-pasted
	// in the above initComponents function into a single function with only
	// three inputs
	private void acceleratorText(JMenuItem input, int keyEvent, String text){
		input.setAccelerator(javax.swing.KeyStroke.getKeyStroke(
				keyEvent,
				java.awt.event.InputEvent.CTRL_MASK));
		input.setText(text);
	}
	
	/*
	 * FURTHER ELABORATION:
	 * To continue with the idea of UI improvements, I added the ability to change the font
	 * of the application during run time, should appeal to people now. 
	 */

    // SWAP 3, TEAM 2
    // Apparently, the Devon team added the "FURTHER ELABORATION" section but forgot
    // to comment on that section. Our team changed the way that the menu items were
    // handled, because they were added quite unwisely. Also, it should be noted that
    // the font sizes make the new fonts impossible to read. I guess this is a "feature".
    // Our team then changed the font size to make it more appropriate.
	private static void setUIFont(javax.swing.plaf.FontUIResource f)
	{
	    java.util.Enumeration<Object> keys = UIManager.getDefaults().keys();
	    while (keys.hasMoreElements())
	    {
	        Object key = keys.nextElement();
	        Object value = UIManager.get(key);
	        if (value instanceof javax.swing.plaf.FontUIResource)
	        {
	            UIManager.put(key, f);
	        }
	    }
	}


    // SWAP 3, TEAM 2
    // Improved the layout of the swing objects.
    // of all of these objects, there are only two that
    // are actually used out of one function, so we
    // removed these as properties of the class.
    private javax.swing.JTable scheduleTable;
    private javax.swing.JLabel monthTitle;
//	private javax.swing.JMenuItem editDays;
//	private javax.swing.JMenu editMenu;
//	private javax.swing.JMenuItem editWorkers;
//	private javax.swing.JMenu fileMenu;
//	private javax.swing.JMenuItem genHtml;
//	private javax.swing.JMenu generateMenu;
//	private javax.swing.JMenuItem generateText;
//	private javax.swing.JScrollPane jScrollPane1;
//	private javax.swing.JMenuBar menuBar;
//	private javax.swing.JButton nextMonthButton;
//	private javax.swing.JPopupMenu popup;
//	private javax.swing.JButton previousMonthButton;
//	private javax.swing.JMenuItem saveChanges;
//	private javax.swing.JMenu fontMenu;
}