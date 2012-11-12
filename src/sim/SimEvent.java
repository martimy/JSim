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
 * The superclass of all simulation events.
 *
 * @author martimy
 */
public abstract class SimEvent {

    Time time;
    Object obj;
    //int id;

    public SimEvent(Time t, Object o) {
        //id = n;
        time = t;
        obj = o;
    }

    @Override
    public String toString() {
        return (this.getClass().getName() + " @ " + time);
    }

    /**
     * Must be overridden to describe how the event should be executed.
     */
    abstract void run();
}
