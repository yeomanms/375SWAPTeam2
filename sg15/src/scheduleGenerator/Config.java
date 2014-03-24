/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scheduleGenerator;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.*;

/**
 *
 * @author schneimd
 */
public class Config extends javax.swing.JFrame {

    private boolean firstSelection = true;
    @SuppressWarnings("rawtypes")
	private DefaultListModel[] models;
    
    
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
            // SWAP 1, TEAM 2
            this.WeekCheck[day.getDayOfWeek()].doClick();
            ArrayList<String> jobs = day.getJobs();
            for(String job: jobs) {
                this.models[0].addElement(job);
                this.WeekJobList[day.getDayOfWeek()].setModel(this.models[0]);
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

        // SWAP 1, TEAM 2
    	//SMELL: Data Clump - There are many areas in this class where data in the format below is "clumped" together.
    	//These would be hard to turn into an object because they are GUI related but there may be a better way to clean this
    	//clump up.
        this.WeekJobList = new JList[7];
        this.WeekScrollPane = new JScrollPane[7];
        this.WeekJobName = new JTextField[7];
        this.WeekLabel = new JLabel[7];
        this.WeekAddJob = new JButton[7];
        this.WeekDeleteJob = new JButton[7];
        this.WeekTab = new JPanel[7];
        for(int i = 0; i< WeekJobList.length; i++){
            this.WeekJobList[i] = new JList();
            this.WeekScrollPane[i] = new JScrollPane();
            this.WeekScrollPane[i].setPreferredSize(new Dimension(185,150));
            this.WeekJobName[i] = new JTextField();
            this.WeekLabel[i] = new JLabel();
            this.WeekAddJob[i] = new JButton();
            this.WeekDeleteJob[i] = new JButton();
            this.WeekTab[i] = new JPanel();
        }
    }

    // SWAP 1, TEAM 2
    // With additional refactoring, the initialization could be reduced in size
    // much like
    //SMELL: Shotgun Surgery - Whenever small changes were made to refactor this class, many more changes were demanded in order for
    // the project to work in a future state.
    private void initComponents() {
        this.WeekCheck = new JCheckBox[7];
        for(int i = 0; i< WeekCheck.length; i++){
            WeekCheck[i] = new JCheckBox();
            WeekCheck[i].setText(Day.DayOfTheWeek(i+1));
            WeekCheck[i].setName(Day.DayOfTheWeek(i+1) + "Check"); // NOI18N
            WeekCheck[i].addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    weekCheckActionPerformed(evt);
                }
            });
        }

    	this.jPanel1 = new javax.swing.JPanel();
        this.jLabel1 = new javax.swing.JLabel();
        this.nextButton = new javax.swing.JButton();
        this.dayTabs = new javax.swing.JTabbedPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Configuration");
        setPreferredSize(new java.awt.Dimension(801, 87));
        setResizable(false);

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
                .addComponent(this.WeekCheck[0])
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(this.WeekCheck[1], javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(this.WeekCheck[2])
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(this.WeekCheck[3], javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(this.WeekCheck[4])
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(this.WeekCheck[5], javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(this.WeekCheck[6], javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(this.nextButton)
                .addGap(78, 78, 78))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(this.WeekCheck[0], javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(this.WeekCheck[5], javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(this.WeekCheck[6], javax.swing.GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE)
                        .addComponent(this.nextButton))
                    .addComponent(this.WeekCheck[3], javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(this.WeekCheck[2], javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(this.jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(this.WeekCheck[4], javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(this.WeekCheck[1], javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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

    
    /**
	 * @param evt  
	 */
    @SuppressWarnings("unchecked")
    // SWAP 1, TEAM 2
	private void weekCheckActionPerformed(java.awt.event.ActionEvent evt) {
        for(int i = 0; i < this.WeekCheck.length; i++){
            if(WeekCheck[i].isSelected()){
                if(this.firstSelection) {
                    stretch();
                }
                this.models[0] = new DefaultListModel<Object>();
                this.WeekJobList[i].setModel(this.models[0]);
                this.WeekScrollPane[i].setViewportView(this.WeekJobList[i]);
                this.WeekJobName[i].setColumns(20);
                this.WeekLabel[i].setText("Job Name:");
                this.WeekAddJob[i].setText("Add Job");
                final int k = i;
                this.WeekAddJob[i].addActionListener(new java.awt.event.ActionListener() {
                    @Override
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        if(!Config.this.WeekJobName[k].getText().isEmpty()) {
                            Config.this.models[0].addElement(Config.this.WeekJobName[k].getText());
                            Config.this.WeekJobList[k].setModel(Config.this.models[0]);
                            Config.this.WeekJobName[k].setText("");
                        }
                    }
                });

                this.WeekDeleteJob[i].setText("Delete Job");
                this.WeekDeleteJob[i].addActionListener(new java.awt.event.ActionListener() {
                    @Override
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        while(!Config.this.WeekJobList[k].isSelectionEmpty()) {
                            int n = Config.this.WeekJobList[k].getSelectedIndex();
                            Config.this.models[0].remove(n);
                        }
                    }
                });

                javax.swing.GroupLayout weekTabLayout = new javax.swing.GroupLayout(this.WeekTab[i]);
                this.WeekTab[i].setLayout(weekTabLayout);
                weekTabLayout.setHorizontalGroup(
                        weekTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(weekTabLayout.createSequentialGroup()
                                        .addContainerGap()
                                        .addComponent(this.WeekScrollPane[i], javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addGroup(weekTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(weekTabLayout.createSequentialGroup()
                                                        .addComponent(this.WeekLabel[i])
                                                        .addGroup(weekTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                .addGroup(weekTabLayout.createSequentialGroup()
                                                                        .addGap(14, 14, 14)
                                                                        .addComponent(this.WeekAddJob[i]))
                                                                .addGroup(weekTabLayout.createSequentialGroup()
                                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                        .addComponent(this.WeekJobName[i], javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                                .addComponent(this.WeekDeleteJob[i]))
                                        .addContainerGap(431, Short.MAX_VALUE))
                );
                weekTabLayout.setVerticalGroup(
                        weekTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(weekTabLayout.createSequentialGroup()
                                        .addContainerGap()
                                        .addGroup(weekTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addGroup(weekTabLayout.createSequentialGroup()
                                                        .addGroup(weekTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                                .addComponent(this.WeekJobName[i], javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addComponent(this.WeekLabel[i]))
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(this.WeekAddJob[i])
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(this.WeekDeleteJob[i]))
                                                .addComponent(this.WeekScrollPane[i], javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addContainerGap(25, Short.MAX_VALUE))
                );
                this.dayTabs.addTab(Day.DayOfTheWeek(i+1), this.WeekTab[i]);
            } else {
                stretch();
                this.dayTabs.remove(this.WeekTab[i]);
            }
        }
    }

    /**
	 * @param evt  
	 */
    private void nextButtonActionPerformed(java.awt.event.ActionEvent evt) {
    	ArrayList<Day> days = new ArrayList<Day>();
        // SWAP 1 Team 2
        for(int i = 0; i < this.WeekCheck.length; i++){
            if(this.WeekCheck[i].isSelected()){
                ArrayList<Object> day = new ArrayList<Object>();
                List<Object> jobs = Arrays.asList(this.models[0].toArray());
                day.addAll(jobs);
                days.add(new Day(Day.DayOfTheWeek(i+1),day));
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
        if(numSelected() > 0) {
            this.setSize(801, 290);
            this.firstSelection = false;
        } else {
            this.setSize(801, 87);
            this.firstSelection = true;
        }
    }

    // SWAP 1 Team 2
    private int numSelected(){
        int j = 0;
        for(int i = 0; i < WeekCheck.length; i++){
            if(WeekCheck[i].isSelected()) j++;
        }
        return j;
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

    // SWAP 1 Team 2
    private javax.swing.JList[] WeekJobList;
    private javax.swing.JCheckBox[] WeekCheck;
    private javax.swing.JScrollPane[] WeekScrollPane;
    private javax.swing.JButton[] WeekAddJob;
    private javax.swing.JButton[] WeekDeleteJob;
    private javax.swing.JTextField[] WeekJobName;
    private javax.swing.JLabel[] WeekLabel;
    private javax.swing.JPanel[] WeekTab;

    
    private javax.swing.JTabbedPane dayTabs;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JButton nextButton;
}
