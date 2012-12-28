//    JSim is a discrete event simulator of an M/M/1 queue system.
//    Copyright (C) 2007-2012  Maen Artimy
//
//    This file is part of JSim.
//
//    JSim is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//    JSim is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with JSim.  If not, see <http://www.gnu.org/licenses/>.
package jsim;

import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.*;

/**
 * Simulation of M/M/1 queue system with infinite queue size
 */
public class JSim extends JFrame {

    private Container pane;

    // Declare Input Fields
    private enum field {

        seedText("Random Seed: "),
        lambdaText("Mean Arrival Time: "),
        muText("Mean Service Time: "),
        numQueuesText("Number of Queues: "),
        numServersText("Number of Servers: "),
        qSizeText("Queue Size: "),
        stText("Simulation Time: "),
        iterText("Number of Runs: "),
        outFileText("Output File: ");
        private String label;

        field(String s) {
            this.label = s;
        }
    };
    // Declare the components
    private JTextField[] inField;
    private JTextArea outArea;
    private JButton runButton;
    // Declare the menu
    private JMenuBar mb = new JMenuBar(); // Menubar
    private JMenu mnuFile = new JMenu("File"); // File Entry on Menu bar
    private JMenuItem mnuItemQuit = new JMenuItem("Quit"); // Quit sub item
    private JMenu mnuHelp = new JMenu("Help"); // Help Menu entry
    private JMenuItem mnuItemAbout = new JMenuItem("About"); // About Entry
    private JProgressBar progressBar;

    /**
     * Create and show the GUI.
     */
    private void showGUI() {
        //setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("JSim - Queue Simulator");
        pane = getContentPane();

        //Build Menus
        mnuFile.add(mnuItemQuit);
        mnuHelp.add(mnuItemAbout);
        mb.add(mnuFile);
        mb.add(mnuHelp);

        mnuItemAbout.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                JDialog d = new AboutDialog(JSim.this);
                d.setVisible(true);
            }
        });

        mnuItemQuit.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                terminate();
            }
        });

        // Create an array of input text fields
        inField = new JTextField[field.values().length];
        JPanel gridPanel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(5, 5, 0, 5);  //top, left, bottom, right padding
        c.anchor = GridBagConstraints.PAGE_START;
        c.weightx = 1;
        for (field f : field.values()) {
            JLabel label = new JLabel(f.label, JLabel.TRAILING);
            inField[f.ordinal()] = new JTextField(10);
            label.setLabelFor(inField[f.ordinal()]);
            gridPanel.add(label, c);
            c.gridx++;
            gridPanel.add(inField[f.ordinal()], c);
            c.gridy++;
            c.gridx = 0;
        }

        // Create a run button
        runButton = new JButton("Run");
        runButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonAction(evt);
            }
        });
        c.gridwidth = 2;
        gridPanel.add(runButton, c);

        // Create an output area
        JPanel rightPanel = new JPanel(new BorderLayout());
        outArea = new JTextArea();
        outArea.setRows(10);
        outArea.setColumns(30);
        javax.swing.JScrollPane scroll = new javax.swing.JScrollPane();
        scroll.setViewportView(outArea);
        rightPanel.add(scroll, BorderLayout.CENTER);
        progressBar = new JProgressBar();
        progressBar.setStringPainted(true);
        rightPanel.add(progressBar, BorderLayout.PAGE_END);

        // Put everything togther
        pane.add(mb, BorderLayout.NORTH);
        pane.add(gridPanel, BorderLayout.LINE_START);
        pane.add(rightPanel, BorderLayout.CENTER);
        //pane.add(runButton, BorderLayout.PAGE_END);

        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent we) {
                terminate();
            }
        });

        pack();
        setVisible(true);
        setLocationRelativeTo(null);
    }

    /**
     * React to pressing the button
     * @param evt
     */
    private void buttonAction(ActionEvent evt) {
        final JButton b = (JButton) evt.getSource();
        b.setEnabled(false);

        final SimulationWorker worker = new SimulationWorker() {

            @Override
            protected void done() {
                try {
                    outArea.setText(get());
                } catch (Exception ex) {
                    System.err.println(ex);
                } finally {
                    b.setEnabled(true);
                }
            }
        };

        // A property listener used to update the progress bar
        PropertyChangeListener listener =
                new PropertyChangeListener() {

                    public void propertyChange(PropertyChangeEvent event) {
                        if ("progress".equals(event.getPropertyName())) {
                            progressBar.setValue((Integer) event.getNewValue());
                        }
                    }
                };
        worker.addPropertyChangeListener(listener);

        // Set the simulation parameteres
        worker.seed = Long.parseLong(inField[field.seedText.ordinal()].getText());
        worker.N = Integer.parseInt(inField[field.iterText.ordinal()].getText());
        worker.lambda = Long.parseLong(inField[field.lambdaText.ordinal()].getText());
        worker.mu = Long.parseLong(inField[field.muText.ordinal()].getText());
        worker.K = Integer.parseInt(inField[field.qSizeText.ordinal()].getText());
        worker.numQueues = Integer.parseInt(inField[field.numQueuesText.ordinal()].getText());
        worker.numServers = Integer.parseInt(inField[field.numServersText.ordinal()].getText());
        worker.simTime = Integer.parseInt(inField[field.stText.ordinal()].getText());
        worker.outFile = inField[field.outFileText.ordinal()].getText();
        
        //progressBar.setMaximum(worker.N);
        worker.execute();
    }

    private void initInFields() {
        Settings st = Settings.instance();
        inField[field.seedText.ordinal()].setText("" + st.get("SEED"));
        inField[field.iterText.ordinal()].setText("" + st.get("ITERATIONS"));
        inField[field.lambdaText.ordinal()].setText("" + st.get("ARRIVAL"));
        inField[field.muText.ordinal()].setText("" + st.get("SERVICE"));
        inField[field.qSizeText.ordinal()].setText("" + st.get("QUEUE"));
        inField[field.numQueuesText.ordinal()].setText("" + st.get("NUMQUEUES"));
        inField[field.numServersText.ordinal()].setText("" + st.get("NUMSERVERS"));
        inField[field.stText.ordinal()].setText("" + st.get("TIME"));
        inField[field.outFileText.ordinal()].setText("" + st.get("OUTFILE"));
    }

    private void readInFields() {
        Settings st = Settings.instance();
        st.set("SEED", inField[field.seedText.ordinal()].getText());
        st.set("ITERATIONS", inField[field.iterText.ordinal()].getText());
        st.set("ARRIVAL", inField[field.lambdaText.ordinal()].getText());
        st.set("SERVICE", inField[field.muText.ordinal()].getText());
        st.set("QUEUE", inField[field.qSizeText.ordinal()].getText());
        st.set("NUMQUEUES", inField[field.numQueuesText.ordinal()].getText());
        st.set("NUMSERVERS", inField[field.numServersText.ordinal()].getText());
        st.set("TIME", inField[field.stText.ordinal()].getText());
        st.set("OUTFILE", inField[field.outFileText.ordinal()].getText());
    }

    public void launch() {
        Settings.instance().load();
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                showGUI();
                initInFields();
            }
        });
    }

    public void terminate() {
        readInFields();
        Settings.instance().save();
        System.exit(0);

    }

    /**
     * Launch the application
     * 
     * @param args
     */
    public static void main(String[] args) {
        JSim app = new JSim();
        app.launch();
    }
}
