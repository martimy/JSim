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
package rand;

import java.util.Random;

/**
 * This class is not being used anywhere
 * 
 * @author martimy
 */
public class ServiceTime implements RandomNumber {

    protected Random r1;
    protected double mean, std, range;

    public ServiceTime(long seed, double mean, double std, double range) {
        r1 = new Random(seed);
        this.mean = mean;
        this.std = std;
        this.range = range;
    }

    /**
     * Returns service time based on positive Normal distribution
     */
    public long getNumber() {
        double v1;
        v1 = r1.nextGaussian() * std + mean;
        v1 = (v1 >= 0.001) ? v1 : 0.001;
        return (new Double(3600.0 * 2 * range / v1).longValue()); //check correctness
    }
}
