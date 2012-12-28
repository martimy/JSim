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

import java.io.PrintStream;
import java.util.*;
import rand.RandomNumber;

public class SimQueue {

    public RandomNumber en;  // arrival rate
    public RandomNumber eo;  // service rate
    public int INFINITE_SIZE = -1;
    public int maxQueueSize;
    public int numCustomersServed, numCustomersDropped; //, totalServers;
    public Time totalQueueDelay;
    public long areaServer, areaNumServer;
    //boolean busyServer;
    public LinkedList[] queue;  // LinkedList is needed for the queue functions
    private int numQueue, numServers, busyServers;
    public long[] areaQueue;
    public int totalCustomers;
    private SimOutput out;

    public SimQueue(int q, int s) {
        q = Math.max(q, 1);
        s = Math.max(s, 1);
        totalQueueDelay = new Time(0);
        maxQueueSize = INFINITE_SIZE;
        queue = new LinkedList[q];
        for (int i = 0; i < q; i++) {
            queue[i] = new LinkedList();
        }
        //numInQueue = new int[q];
        numQueue = q;
        numServers = s;
        areaQueue = new long[q];
        busyServers = 0;
    }

    public SimQueue() {
        this(1, 1);
    }

    /**
     * Sets the maximum size of the queue
     */
    public void setQueueSize(int k) {
        maxQueueSize = k;
    }

    /**
     * Sets the distribution function of the arrival rate
     */
    public void arrivalRate(RandomNumber r) {
        en = r;
    }

    public void serviceRate(RandomNumber r) {
        eo = r;
    }

    /**
     * Should by called by the SimEvent's run() method to update the system's
     * stats.
     *
     */
//    public void update(Time deltaT) {
//    }
    void update(Time time, Time lastEventTime) {
        Time deltaT = time.minus(lastEventTime);
        long sum = 0;
        for (int q = 0; q < numQueue; q++) {
            areaQueue[q] += queue[q].size() * deltaT.getTime();
            sum += areaQueue[q];
        }
        //int b = busyServers() ? 1 : 0;
        areaServer += busyServers * deltaT.getTime();
        //areaNumServer += totalServers * deltaT.getTime();
        if (out != null) {
            out.trace(String.format("%6d, %6d, %6d\n", time.getTime(), sum, areaServer));
        }
    }

    /**
     * Returns true if all queues in the system are full.
     *
     * @return boolean
     */
    public boolean full() {
        if (maxQueueSize != INFINITE_SIZE) {
            for (int q = 0; q < numQueue; q++) {
                if (queue[q].size() < maxQueueSize) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns true if there is at least one customer in any queue
     *
     * @return boolean
     */
    public boolean occupied() {
        for (int q = 0; q < numQueue; q++) {
            if (queue[q].size() > 0) {
                return true;
            }
        }
        return false;
    }

//    void log(Time time) {
//        System.out.format("%s\t%6d\t%6d\t%6d\t%6d\n", time, numInQueue, numCustomersServed, totalCustomers, numCustomersDropped);
//    }
    public void addCustomer() {
        totalCustomers++;
    }

    /**
     * return an empty queue based on some criteria
     * or -1 if no empty queue is found
     */
    public int getFreeQueue() {
        // get the most empty queue, if two queus are equally empty,
        // return the one with smallest id

        long num = Long.MAX_VALUE;
        int id = -1;
        for (int i = 0; i < numQueue; i++) {
            //System.err.println(i + " " + queue[i].size());
            if (num > queue[i].size()) {
                num = queue[i].size();
                id = i;
            }
        }
        if ((maxQueueSize != INFINITE_SIZE) && (queue[id].size() >= maxQueueSize)) {
            id = -1;
        }
        return id;
    }
    private int nextQueue = -1;

    /**
     * return an occupied queue based on some criteria
     * or -1 if no occupied queue is found
     */
    public int getBusyQueue() {
        // get the next queue in round rabon fashion
        // skip an empty queue
        int id = -1;
        for (int i = 0; i < numQueue; i++) {
            nextQueue = ++nextQueue % numQueue;
            if (queue[nextQueue].size() > 0) {
                id = nextQueue;
                break;
            }
            //System.err.println("nextQueue = " + nextQueue);
        }
        return id;
    }

    /**
     * return a queue of ID q
     * 
     * @param q
     * @return LinkedList
     */
    public LinkedList getQueue(int q) {
        return queue[q];
    }

    /**
     * Returns the sum of areas under queues
     * @return
     */
    public long getAreaQueue() {
        long sum = 0;
        for (int i = 0; i < numQueue; i++) {
            sum += areaQueue[i];
        }
        return sum;
    }

    /**
     * Get total number of customers in the queues
     * @return
     */
    public int getCustomersInQueue() {
        int sum = 0;
        for (int i = 0; i < numQueue; i++) {
            sum += queue[i].size();
        }
        return sum;
    }

    public boolean busyServers() {
        return (busyServers == numServers);
    }

    public void incBusyServers() {
        busyServers = Math.min(++busyServers, numServers);
    }

    public void decBusyServers() {
        busyServers = Math.max(--busyServers, 0);
    }

    public void setTrace(SimOutput out) {
        this.out = out;
    }
}
