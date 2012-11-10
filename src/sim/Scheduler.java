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

import java.util.*;

/**
 * The Scheduler class adds events to the event queue and executes the events in order when its run() method is called.
 *
 * @author martimy
 */
public class Scheduler { //Singular class
    private static Scheduler uniqueInstance;
    private boolean finished;
    private Time currentTime, lastEventTime;
    private SortedSet eventQueue;
    
    
    /**
     * Used by the eventQueue to order events in ascending time order.
     */
    static final Comparator<SimEvent> TIME_ORDER = new Comparator<SimEvent>() {
        public int compare(SimEvent e1, SimEvent e2) {
            int c = e1.time.compareTo(e2.time);
            if(c != 0) return c;
            else if (e1.hashCode() < e2.hashCode()) return -1; // if time is equal, the tie must be broken by the hashcode
            else if (e1.hashCode() > e2.hashCode()) return 1;

            return 0;
        }
    };
    
    /**
     * Constructor of a Scheduler object.
     * The private constructor prevents creating an object of this class outside the class itself following
     * making this class a singular class.
     */
    private Scheduler() {
        eventQueue = new TreeSet(TIME_ORDER);
        currentTime = new Time(0);
        lastEventTime = new Time(0);
        finished = false;
    }
    
    /**
     * Returns an instance of this class.
     */
    public static Scheduler instance() {
        if (uniqueInstance == null) {
            uniqueInstance = new Scheduler();
        }
        return uniqueInstance;
    }
    
    /**
     * Deletes the cuurent uniqueInstance and returns a new one.
     */
    public static Scheduler reset() {
        uniqueInstance = null;
        uniqueInstance = new Scheduler();
        return uniqueInstance;
    }
    
    /**
     * Inserts a new event in the event queue ensuring that the new event is not older than the
     * current time.
     */
    public void addEvent(SimEvent se) throws SIMException {
        if(currentTime.compareTo(se.time) == 1) throw new SIMException("Attempt to add event in the past");
        else if(!eventQueue.add(se)) throw new SIMException("Event cannot be added due to matching time");
    }
    
    /**
     * Terminates the uniqueInstance.
     */
    public void finish() {
        finished = true;
    }
    
    /**
     * Returns the first event in the event queue and update the current time.
     */
    private SimEvent getNextEvent() {
        SimEvent se;
        se = (SimEvent) eventQueue.first();
        lastEventTime = currentTime;
        currentTime = se.time;
        eventQueue.remove(se);
        return se;
    }
    
    /**
     * Goes through the event queue executing each event until the queue is empty
     * or the finish() method is called.
     */
    public void run() {
        SimEvent se;
        
        while(!eventQueue.isEmpty() && !finished) {
            se = getNextEvent();
            se.run();
            //System.out.println(se);
        }
    }
       
    /**
     * Returns current time
     */
    public Time getCurrentTime() {
        return currentTime;
    }

    /**
     * Returns last-event time
     */
    public Time getLastEventTime() {
        return lastEventTime;
    }
}
