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
 * An arrival event in a queue system.
 *
 * @author martimy
 */
public class ArrivalEvent extends SimEvent {

    
    private SimQueue qs;

    /**
     * Constructor of the arrival event
     */
    public ArrivalEvent(Time t, SimQueue o) {
        super(t, o);
        qs = o;
    }

    /**
     * Upon execution of this event,
     *
     * select a queue to enter (based on some criteria)
     * is there an empty space in the queue?
     *  if yes:
     *      is there a free server?
     *          if yes:
     *              increment number of busy servers
     *          if no:
     *              increase number of customers in queue
     *  if no:
     *          drop customer
     *  Schedule another arrival event based on current arrival rate.
     *
     */
    public void run() {
        Scheduler sc = Scheduler.instance();
        int qID = qs.getFreeQueue();
        //System.err.println("Enter "+qID);
        qs.update(time, sc.getLastEventTime());   // update all queue curves
        qs.addCustomer();                              // to the system

        // if there is a free server then the departure event can be secheduled immediately
        // otherwise, this arrival event is queued or dropped depending on the space
        // left in the queue(s)
        if (!qs.busyServers()) {
            qs.numCustomersServed++;
            qs.incBusyServers();

            try {
                sc.addEvent(new DepartureEvent(time.plus(qs.eo.getNumber()), qs));
            } catch (SIMException ex) {
                System.err.println("Exception : " + ex);
                System.exit(0);
            }
        } else if (!qs.full()) {
            qs.getQueue(qID).addLast(this);
        } else {
            qs.numCustomersDropped++;
        }

        // Generate subsequent arrival event
        try {
            sc.addEvent(new ArrivalEvent(time.plus(qs.en.getNumber()), qs));
        } catch (SIMException ex) {
            System.err.println("Exception : " + ex);
            System.exit(0);
        }

    }
}
