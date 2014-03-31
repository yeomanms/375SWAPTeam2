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

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

/**
 *
 * @author schneimd
 */
public class Config extends javax.swing.JFrame {

    private boolean firstSelection = true;
    private int numSelected = 0;
    @SuppressWarnings("rawtypes")
	private DefaultListModel[] models;
    
    
    /*
     * CODE SMELL - Code Duplication
     * This is quite a bit of duplication, when it could be pulled into it's own method and just call it with
     * different parameters.
     */
    /**
     * Used to edit days.
     *
     * @param days
     */
    @SuppressWarnings("unchecked")
	public Config(ArrayList<Day> days) {
    	this.models = new DefaultListModel[7];
        initDyn();
        initComponents();
        
    	for(Day day: days) {
    		if(day.getNameOfDay().equals("Sunday")) {
    			this.checkList[0].doClick();
    			ArrayList<String> jobs = day.getJobs();
    			for(String job: jobs) {
    				this.models[0].addElement(job);
    			}
    		} else if(day.getNameOfDay().equals("Monday")) {
    			this.checkList[1].doClick();
    			ArrayList<String> jobs = day.getJobs();
    			for(String job: jobs) {
    				this.models[1].addElement(job);
    			}
    		} else if(day.getNameOfDay().equals("Tuesday")) {
    			this.checkList[2].doClick();
    			ArrayList<String> jobs = day.getJobs();
    			for(String job: jobs) {
    				this.models[2].addElement(job);
    			}
    		} else if(day.getNameOfDay().equals("Wednesday")) {
    			this.checkList[3].doClick();
    			ArrayList<String> jobs = day.getJobs();
    			for(String job: jobs) {
    				this.models[3].addElement(job);
    			}
    		} else if(day.getNameOfDay().equals("Thursday")) {
    			this.checkList[4].doClick();
    			ArrayList<String> jobs = day.getJobs();
    			for(String job: jobs) {
    				this.models[4].addElement(job);
    			}
    		} else if(day.getNameOfDay().equals("Friday")) {
    			this.checkList[5].doClick();
    			ArrayList<String> jobs = day.getJobs();
    			for(String job: jobs) {
    				this.models[5].addElement(job);
    			}
    		} else if(day.getNameOfDay().equals("Saturday")) {
    			this.checkList[6].doClick();
    			ArrayList<String> jobs = day.getJobs();
    			for(String job: jobs) {
    				this.models[6].addElement(job);
    			}
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
    	for (int i = 0; i < 7; i++) {
    		this.scrollPaneList[i] = new javax.swing.JScrollPane();
            this.scrollPaneList[i].setPreferredSize(new Dimension(185,150));
            this.jobList[i] = new javax.swing.JList();
            this.jobName[i] = new javax.swing.JTextField();
            this.dayLabel[i] = new javax.swing.JLabel();
            this.addJobButton[i] = new javax.swing.JButton();
            this.deleteJobButton[i] = new javax.swing.JButton();
            this.dayTab[i] = new javax.swing.JPanel();
    	}
    }

    private void initComponents() {

    	this.jPanel1 = new javax.swing.JPanel();
        this.jLabel1 = new javax.swing.JLabel();
        this.nextButton = new javax.swing.JButton();
        this.dayTabs = new javax.swing.JTabbedPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Configuration");
        setPreferredSize(new java.awt.Dimension(801, 87));
        setResizable(false);
        //SWAP 1, TEAM 10
        //Quality Change.  Created single loop to initialize the check boxes instead of 7 different places
        for(int i = 0; i < 7; i++) {
        	this.checkList[i] = new JCheckBox();
        	this.checkList[i].setText(CalendarGUI.getNameforNum(i + 1));
            this.checkList[i].setName(CalendarGUI.getNameforNum(i + 1).toLowerCase() + "Check"); // NOI18N
            this.checkList[i].addItemListener(new DayHandler(this,i));
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

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(this.jPanel1);
        this.jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(this.jLabel1)
                .addGap(18, 18, 18)
                .addComponent(this.checkList[0])
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(this.checkList[1], javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(this.checkList[2])
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(this.checkList[3], javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(this.checkList[4])
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(this.checkList[5], javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(this.checkList[6], javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(this.nextButton)
                .addGap(78, 78, 78))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(this.checkList[0], javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(this.checkList[5], javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(this.checkList[6], javax.swing.GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE)
                        .addComponent(this.nextButton))
                    .addComponent(this.checkList[3], javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(this.checkList[2], javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(this.jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(this.checkList[4], javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(this.checkList[1], javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(this.jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 18, Short.MAX_VALUE))
            .addComponent(this.dayTabs)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(this.jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(this.dayTabs, javax.swing.GroupLayout.DEFAULT_SIZE, 196, Short.MAX_VALUE))
        );

        this.dayTabs.getAccessibleContext().setAccessibleName("Days Tab");

        pack();
    }// </editor-fold>

    //SWAP 1, TEAM 10
    //Quality Change - this code was duplicated for every day of the week.  Now it's in just one place
    private class DayHandler implements ItemListener {
    	Config c;
    	int day;
    	
    	public DayHandler (Config c, int day) {
    		this.c = c;
    		this.day = day;
    	}

		@Override
		public void itemStateChanged(ItemEvent e) {
			if(e.getStateChange() == ItemEvent.SELECTED) {
                c.numSelected++;
                if(c.firstSelection) {
                    stretch();
                }
                c.models[day] = new DefaultListModel<Object>();
                c.jobList[day].setModel(c.models[day]);
                c.scrollPaneList[day].setViewportView(c.jobList[day]);

                c.jobName[day].setColumns(20);

                c.dayLabel[day].setText("Job Name:");

                c.addJobButton[day].setText("Add Job");
                c.addJobButton[day].addActionListener(new java.awt.event.ActionListener() {
                    @Override
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        if(!c.jobName[day].getText().isEmpty()) {
                            c.models[day].addElement(c.jobName[day].getText());
                            c.jobList[day].setModel(c.models[day]);
                            c.jobName[day].setText("");
                        }
                    }
                });

                c.deleteJobButton[day].setText("Delete Job");
                c.deleteJobButton[day].addActionListener(new java.awt.event.ActionListener() {
                    @Override
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        while(!c.jobList[day].isSelectionEmpty()) {
                            int n = c.jobList[day].getSelectedIndex();
                            c.models[day].remove(n);
                        }
                        
                    }
                });

                javax.swing.GroupLayout sundayTabLayout = new javax.swing.GroupLayout(c.dayTab[day]);
                c.dayTab[day].setLayout(sundayTabLayout);
                sundayTabLayout.setHorizontalGroup(
                    sundayTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(sundayTabLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(c.scrollPaneList[day], javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(sundayTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(sundayTabLayout.createSequentialGroup()
                                .addComponent(c.dayLabel[day])
                                .addGroup(sundayTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(sundayTabLayout.createSequentialGroup()
                                        .addGap(14, 14, 14)
                                        .addComponent(c.addJobButton[day]))
                                    .addGroup(sundayTabLayout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(c.jobName[day], javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addComponent(c.deleteJobButton[day]))
                        .addContainerGap(431, Short.MAX_VALUE))
                );
                sundayTabLayout.setVerticalGroup(
                    sundayTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(sundayTabLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(sundayTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(sundayTabLayout.createSequentialGroup()
                                .addGroup(sundayTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(c.jobName[day], javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(c.dayLabel[day]))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(c.addJobButton[day])
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(c.deleteJobButton[day]))
                            .addComponent(c.scrollPaneList[day], javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(25, Short.MAX_VALUE))
                );
                c.dayTabs.addTab(CalendarGUI.getNameforNum(day + 1), c.dayTab[day]);
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
    	for(int i = 0; i < 7; i++) {
    		if(this.checkList[i].isSelected()) {
            	days.add(new Day(CalendarGUI.getNameforNum(i + 1),new ArrayList<Object>(Arrays.asList(this.models[i].toArray()))));
    		}
    	}
    	
    	if(days.size() > 0) {
    		boolean hasJobs = true;
    		int i = 0;
    		while(hasJobs && i<days.size()) {
    			if(days.get(i).getJobs().size() == 0) {
    				hasJobs = false;
    			}
    			i++;
    		}
    		if(hasJobs) {
		    	Main.setDays(days);
		    	Main.wSet = new WorkerSetup();
		    	Main.toggleWorkerSetup();
		    	Main.config = this;
		    	Main.toggleConfig();
    		} else {
    			JOptionPane.showMessageDialog(this, "You must have at least one job each day.");
    		}
    	} else {
    		JOptionPane.showMessageDialog(this, "You have not added any days.");
    	}
    }
    

    private void stretch() {
        if(this.numSelected > 0) {
            this.setSize(801, 290);
            this.firstSelection = false;
        } else {
            this.setSize(801, 87);
            this.firstSelection = true;
        }
    }
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Config.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Config.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Config.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Config.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
			public void run() {
                new Config().setVisible(true);
            }
        });
    }
    
    //SWAP 1, TEAM 10
    //Quality Change: made a list to hold all of the necessary gui elements and then they can be indexd by day.  Allows for a lot of code reduction
    
    private JScrollPane[] scrollPaneList = new JScrollPane[7];
    private JButton[] addJobButton = new JButton[7];
    private JButton[] deleteJobButton = new JButton[7];
    private JList[] jobList = new JList[7];
    private JTextField[] jobName = new JTextField[7];
    private JLabel[] dayLabel = new JLabel[7];
    private JPanel[] dayTab = new JPanel[7];
    private JCheckBox[] checkList = new JCheckBox[7];
    
    
    private javax.swing.JTabbedPane dayTabs;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JButton nextButton;
}
