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

import javax.swing.SwingWorker;
import rand.*;
import sim.*;

/**
 *
 * @author martimy
 */
public class SimulationWorker extends SwingWorker<String, Void> {

    public long seed;                           // this has no effect for now
    public int N;                                 // iterations
    public long lambda;                         // mean arrival time
    public long mu;                              // mean service time
    public int K;                               // queue size
    public int simTime;                        // simulation time
    public double[] A = null;                          // stats array


    @Override
    protected String doInBackground() throws Exception {
        Scheduler sc;
        SimQueue qs;

        // to collect stats
        A = new double[8];
        for (double a : A) {
            a = 0;
        }

        // Repeat the simulation N times
        for (int i = 0; i < N; i++) {
            RandomNumber ta = new ExpoRandom(123 * (i + 1), lambda);
            RandomNumber ts = new ExpoRandom(456 * (i + 1), mu);
            //RandomNumber ts = new NormalRandom(456*(i+1), mu, 9);

            ArrivalEvent.arrivalRate(ta);
            DepartureEvent.serviceRate(ts);

            qs = SimQueue.instance();
            qs.setQueueSize(K);

            sc = Scheduler.instance();
            try {
                sc.addEvent(new ArrivalEvent(new Time(0)));      // starts the simulation by arrival at time 0
                sc.addEvent(new EndEvent(new Time(simTime)));    // ends the simulation at after 1000s
            } catch (SIMException ex) {
                System.err.println("Exception : " + ex);
                System.exit(0);
            }
            sc.run();

            A[0] += qs.numCustomersServed;
            A[1] += (double) qs.totalQueueDelay.getTime() / qs.numCustomersServed;
            A[2] += (double) qs.areaQueue / sc.getLastEventTime().getTime();
            A[3] += (double) qs.areaServer / sc.getLastEventTime().getTime();
            A[4] += qs.totalQueueDelay.getTime();
            A[5] += qs.totalCustomers;
            A[6] += qs.numCustomersDropped;
            A[7] += qs.numInQueue;

            setProgress((i + 1) * 100 / N);
            SimQueue.reset();
            Scheduler.reset();
        }

        // Theoretical results
        double rho = (double) mu / lambda;
        double L = rho / (1 - rho);
        double Lq = rho * rho / (1 - rho);
        double W = mu / (1 - rho);
        double Wq = rho * mu / (1 - rho);

        StringBuilder st = new StringBuilder();
        st.append(String.format("Total Customers Served = %4.2f\n", A[0] / N));
        st.append(String.format("Average Queue Delay = %4.2f (%4.2f)\n", A[1] / N, Wq));
        st.append(String.format("Average Queue size = %4.2f : (%4.2f)\n", A[2] / N, Lq));
        st.append(String.format("Average utilization = %4.2f : (%4.2f)\n", A[3] / N, rho));
        st.append(String.format("Total Queue Delay = %4.2f\n", A[4] / N));
        st.append("Sanity check = " + A[0] + " + " + A[6] + " + " + A[7] + " = " + A[5] + " " + (A[0] + A[6] + A[7] == A[5]));

        System.out.println(st);
        return st.toString();
    }
}
