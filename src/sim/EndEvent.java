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
 * This event ends the simulation of the queue system by stopping the scheduler.
 *
 * @author martimy
 */
public class EndEvent extends SimEvent {

    /**
     * Constructor of the end event.
     */
    public EndEvent(Time t, Object o) {
        super(t, o);
    }

    public EndEvent(Time t) {
        super(t, null);
    }

    /**
     * Executes the end-simulation event. The scheduler ignores all subsequent
     * events in the event queue.
     */
    void run() {
        Scheduler sc = Scheduler.instance();
        SimQueue qs = SimQueue.instance();
        qs.update(time.minus(sc.getLastEventTime()));
        sc.finish();
        //System.out.println("End ="+time);
    }
}
