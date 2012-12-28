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
package sim;

import java.io.FileNotFoundException;

/**
 *
 * @author MAEN
 */
public class SimOutput {

    private final int N;
    private final int numQueues;
    private double numCustomersServed;
    private double avgQueueDelay;
    private double[] areaQueue;
    private double areaServer;
    private double totalQueueDelay;
    private double totalCustomers;
    private double numCustomersDropped;
    private double[] queueSize;
    private StringBuilder traceText;

    public SimOutput(int numQueues, int N) {

        this.numQueues = Math.max(numQueues, 1);
        this.N = N;

        areaQueue = new double[numQueues];
        queueSize = new double[numQueues];

        traceText = new StringBuilder();
    }

    public void update(SimQueue qs, long time) {
        numCustomersServed += qs.numCustomersServed;
        avgQueueDelay += (double) qs.totalQueueDelay.getTime() / qs.numCustomersServed;
        areaServer += (double) qs.areaServer / time;
        totalQueueDelay += qs.totalQueueDelay.getTime();
        totalCustomers += qs.totalCustomers;
        numCustomersDropped += qs.numCustomersDropped;
        for (int i = 0; i < numQueues; i++) {
            areaQueue[i] += (double) qs.areaQueue[i] / time;
            queueSize[i] += qs.queue[i].size();
        }

    }

    public String getAnalyticalResults(double lambda, double mu) {
        // Analytical results
        double rho = (double) mu / lambda;                // system utilization *
        double L = rho / (1 - rho);                     // mean number of customers in the system
        double Lq = L * rho;                                  // mean length of queue
        double W = mu / (1 - rho);                     // mean time spent in the system *
        double Wq = mu * L;                               // mean time spent in the queue *

        // Note that
        // W = lambda * L = rho * Wq
        // Wq = lambda * Lq = W / rho

        StringBuilder st = new StringBuilder();
        st.append("Analytical Results\n---\n");
        st.append(String.format("Average Queue Delay = %4.2f\n", Wq));
        st.append(String.format("Average Queue size = %4.2f\n", Lq));
        st.append(String.format("Average utilization = %4.2f\n\n", rho));

        return st.toString();
    }

    public String getSimResults() {

        StringBuilder st = new StringBuilder();
        st.append("Simulation Results\n---\n");
        st.append(String.format("Total customers served = %4.2f\n", numCustomersServed / N));
        st.append(String.format("Average queue delay = %4.2f\n", avgQueueDelay / N));
        double sum = 0.0;
        for (int i = 0; i < numQueues; i++) {
            st.append(String.format("Average queue #%d size = %4.2f\n", (i+1), areaQueue[i] / N));
            sum += areaQueue[i];
        }
        st.append(String.format("Average queue size = %4.2f\n", sum / N));
        st.append(String.format("Average utilization = %4.2f\n", areaServer / N));
        st.append(String.format("Total queue delay = %4.2f\n", totalQueueDelay / N));
        // this works only for M/M/1 for now
        //st.append("Sanity check = " + numCustomersServed + " + " + numCustomersDropped + " + " + queueSize[0] + " = " + totalCustomers + " " + (numCustomersServed + numCustomersDropped + queueSize[0] == totalCustomers) + "\n\n");

        return st.toString();

    }

    /**
     * append the trace file
     * @param format
     */
    public void trace(String s) {
        traceText.append(s);
    }

    public void printTrace(String outFile) throws FileNotFoundException {
        java.io.PrintStream out = new java.io.PrintStream(outFile);
        out.print(traceText.toString());
        out.close();
    }
}
