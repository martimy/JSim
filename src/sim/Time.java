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
 * Simulation time object.
 * 
 * @author martimy
 */
public class Time implements Comparable<Time> {

    private long time;

    /** Creates a new instance of Time */
    public Time(long ms) {
        time = ms;
    }

    /**
     * Sets the time in milliseconds
     */
    public void setTime(long ms) {
        time = ms;
    }

    /**
     * Return a long representing time in milliseconds
     */
    public long getTime() {
        return time;
    }

    /**
     * Increments the time value by the amount in the parameter
     */
    public void add(long ms) {
        time += ms;
    }

    /**
     * Increments the time value by the amount in the parameter
     */
    public void add(Time t) {
        time += t.time;
    }

    /**
     * Increments the time value by the amount in the parameter
     */
    public void subtract(Time t) {
        time -= t.time;
    }

    /**
     * Returns a Time object with the sum value
     */
    public Time plus(long ms) {
        return (new Time(time + ms));
    }

    /**
     * Returns a Time object with the sum value
     */
    public Time plus(Time t) {
        return (new Time(time + t.time));
    }

    /**
     * Returns a Time object with the difference value
     */
    public Time minus(Time t) {
        return (new Time(time - t.time));
    }

    public int compareTo(Time o) {
        long t = o.time;
        if (time < t) {
            return -1;
        } else if (time > t) {
            return 1;
        }
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Time) {
            return (time == ((Time) o).time);
        }
        return false;
    }

    @Override
    public int hashCode() { // added automatically by IDE
        int hash = 7;
        hash = 67 * hash + (int) (this.time ^ (this.time >>> 32));
        return hash;
    }

    @Override
    public String toString() {
        //return String.format( "%02d:%02d:%02d", hour, minute, second );
        return String.format("%d", time);
    }
}
