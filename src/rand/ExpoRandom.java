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
 * A random number generator based on an Exponential distribution.
 *
 * @author martimy
 */
public class ExpoRandom extends Random implements RandomNumber {

    protected long mean;

    /**
     * Constructor of ExpoRandom
     * 
     * @param seed, mean
     */
    public ExpoRandom(long seed, long mean) {
        super(seed);
        this.mean = mean;
    }

    /**
     * Returns a random number from an exponential distribution
     */
    public long getNumber() {
        return (new Double(-Math.log(nextDouble()) * mean)).longValue();
    }
}
