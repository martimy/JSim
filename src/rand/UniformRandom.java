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
 * A random number generator based on exponential distribution.
 *
 * @author martimy
 */
public class UniformRandom extends Random implements RandomNumber {

    protected long min, max;

    /**
     * Constructor of UniformRandom
     *
     * @param seed, mean
     */
    public UniformRandom(long seed, long min, long max) {
        super(seed);
        this.min = min;
        this.max = max;
    }

    /**
     * Returns a random number from an exponential distribution
     */
    public long getNumber() {
        long x = max - min;
        return new Double(x * nextDouble() + min).longValue();
    }
}
