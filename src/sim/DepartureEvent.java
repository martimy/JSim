//    JSim is a discrete event simulator of an M/M/1 queue system.
//    Copyright (C) 2007  Maen Artimy
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

import rand.*;

/**
 * A departure event.
 *
 * @author martimy
 */
public class DepartureEvent extends SimEvent {

    public static RandomNumber eo;

    /** Creates a new instance of DepartureEvent */
    public DepartureEvent(Time t, Object o) {
        super(t, o);
    }
    public DepartureEvent(Time t) {
        super(t, null);
    }

    public static void serviceRate(RandomNumber r) {
        eo = r;
    }

    /**
     * If the queue is not empty, schedule a departure event for the first customer
     * in the queue; otherwise, do nothing.
     */
    public void run() {
        Scheduler sc = Scheduler.instance();
        SimQueue qs = SimQueue.instance();
        qs.update(time.minus(sc.getLastEventTime()));

        if (qs.numInQueue > 0) {
            ArrivalEvent ae = (ArrivalEvent) qs.queue.removeFirst();
            Time delay = time.minus(ae.time);
            qs.totalQueueDelay.add(delay);
            qs.numInQueue--;
            qs.numCustomersServed++;
            try {
                sc.addEvent(new DepartureEvent(/*ae.id,*/time.plus(eo.getNumber())));
            } catch (SIMException ex) {
                System.err.println("Exception : " + ex);
                System.exit(0);
            }

        } else {
            qs.busyServer = false;
        }
    }
}
