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

public class SimQueue {

    private static SimQueue uniqueInstance;
    public static final int INFINITE_SIZE = -1;
    LinkedList queue;
    private int maxQueueSize;
    public int numInQueue, numCustomersServed, totalCustomers, numCustomersDropped, totalServers;
    public Time totalQueueDelay;
    public long areaQueue, areaServer, areaNumServer;
    boolean busyServer;

    private SimQueue() {
        totalQueueDelay = new Time(0);
        queue = new LinkedList();
        maxQueueSize = INFINITE_SIZE;
    }

    /**
     * Sets the maximum size of the queue
     */
    public void setQueueSize(int k) {
        maxQueueSize = k;
    }

    /**
     * Returns an instance of this class.
     */
    public static SimQueue instance() {
        if (uniqueInstance == null) {
            uniqueInstance = new SimQueue();
        }
        return uniqueInstance;
    }

    /**
     * Deletes the current SimQueue and returns a new one.
     */
    public static SimQueue reset() {
        uniqueInstance = null;
        uniqueInstance = new SimQueue();
        return uniqueInstance;
    }

    /**
     * Called by the run() method to calculate the average queue size and the average time the server is busy.
     */
    public void update(Time deltaT) {
        areaQueue += numInQueue * deltaT.getTime();
        int b = busyServer ? 1 : 0;
        areaServer += b * deltaT.getTime();
        areaNumServer += totalServers * deltaT.getTime();
    }

    public boolean full() {
        if (maxQueueSize == INFINITE_SIZE) {
            return false;
        } else if (numInQueue <= maxQueueSize) {
            return false;
        }
        return true;
    }

    void log(Time time) {
        System.out.format("%s\t%6d\t%6d\t%6d\t%6d\n", time, numInQueue, numCustomersServed, totalCustomers, numCustomersDropped);
    }
}
