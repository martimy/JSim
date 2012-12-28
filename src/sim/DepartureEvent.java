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

/**
 * A departure event in queue system.
 *
 * @author martimy
 */
public class DepartureEvent extends SimEvent {

    private SimQueue qs;

    /** Creates a new instance of DepartureEvent */
    public DepartureEvent(Time t, SimQueue o) {
        super(t, o);
        qs = o;
    }

    /**
     * If there is any occupied queue, schedule a departure event for the first customer
     * in the queue; otherwise, do nothing.
     */
    public void run() {
        Scheduler sc = Scheduler.instance();
        qs.update(time, sc.getLastEventTime());

        if (qs.occupied()){
            int qID = qs.getBusyQueue();
            //System.err.println("Exit "+qID);
            ArrivalEvent ae = (ArrivalEvent) qs.getQueue(qID).removeFirst();
            Time delay = time.minus(ae.time);
            qs.totalQueueDelay.add(delay);
            qs.numCustomersServed++;
            try {
                sc.addEvent(new DepartureEvent(time.plus(qs.eo.getNumber()), qs));
            } catch (SIMException ex) {
                System.err.println("Exception : " + ex);
                System.exit(0);
            }

        } else {
            //System.err.println("Exiting ");
            qs.decBusyServers();
        }
    }
}
