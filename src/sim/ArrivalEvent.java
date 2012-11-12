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

import rand.*;

/**
 * An arrival event in a queue system.
 *
 * @author martimy
 */
public class ArrivalEvent extends SimEvent {

    public static RandomNumber en;

    /**
     * Constructor of the arrival event
     */
    public ArrivalEvent(Time t, Object o) {
        super(t, o);
    }

    public ArrivalEvent(Time t) {
        super(t, null);
    }

    /**
     * Sets the distribution function of the arrival rate
     */
    public static void arrivalRate(RandomNumber r) {
        en = r;
    }

    /**
     * Upon execution of this event,
     * Schedule another arrival event based on current arrival rate.
     * If server is not busy, increase the number of customers served by one and schedule a departure event;
     * otherwise, increase the number of customers in the queue and add the event to the system's queue.
     *
     */
    public void run() {
        Scheduler sc = Scheduler.instance();
        SimQueue qs = SimQueue.instance();
        qs.update(time.minus(sc.getLastEventTime()));
        qs.totalCustomers++;

        // Generate subsequent arrival event
        try {
            sc.addEvent(new ArrivalEvent(/*id+1,*/time.plus(en.getNumber())));
        } catch (SIMException ex) {
            System.err.println("Exception : " + ex);
            System.exit(0);
        }

        // if server is free then the departure event can be secheduled immediately
        // otherwise, this arrival event is queued or dropped depending on the space
        // left in the queue
        if (!qs.busyServer) {
            //sc.totalQueueDelay += 0;
            qs.numCustomersServed++;
            qs.busyServer = true;

            try {
                sc.addEvent(new DepartureEvent(/*id,*/time.plus(DepartureEvent.eo.getNumber())));
            } catch (SIMException ex) {
                System.err.println("Exception : " + ex);
                System.exit(0);
            }

        } else if (!qs.full()) {
            qs.numInQueue++;
            qs.queue.addLast(this);
        } else {
            qs.numCustomersDropped++;
        }
    }
}
