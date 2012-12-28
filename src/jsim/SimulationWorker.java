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

import java.io.FileNotFoundException;
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
    public int numQueues;                       // number of queues
    public int numServers;                       // number of servers
    public double[] A = null;                          // stats array
    public String outFile;                       // file name

    @Override
    protected String doInBackground() throws Exception {
        Scheduler sc;
        SimQueue qs;
        SimOutput out = new SimOutput(numQueues, N);

        // Repeat the simulation N times
        for (int i = 0; i < N; i++) {
            //RandomNumber ta = new ExpoRandom(seed * (i + 1), lambda);
            //RandomNumber ts = new ExpoRandom(seed * (i + 5), mu);
            //RandomNumber ts = new NormalRandom(456*(i+1), mu, 9);

            qs = new SimQueue(numQueues, numServers);
            qs.arrivalRate(new ExpoRandom(seed * (i + 1), lambda));
            qs.serviceRate(new ExpoRandom(seed * (i + 5), mu));
            if (K > 0) {
                qs.setQueueSize(K);
            }

            // send trace data to the SimOut
            qs.setTrace(out);

            sc = Scheduler.instance();
            try {
                sc.addEvent(new ArrivalEvent(new Time(0), qs));      // starts the simulation by arrival at time 0
                sc.addEvent(new EndEvent(new Time(simTime), qs));    // ends the simulation at after 1000s
            } catch (SIMException ex) {
                System.err.println("Exception : " + ex);
                System.exit(0);
            }
            sc.run();

            out.update(qs, sc.getLastEventTime().getTime());

            setProgress((i + 1) * 100 / N);
            Scheduler.reset();
        }

        out.printTrace(outFile);

        return out.getAnalyticalResults(lambda, mu) + out.getSimResults();
    }
}
