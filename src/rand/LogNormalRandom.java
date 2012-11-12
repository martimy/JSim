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
 * A random number generator based on LogNormal distribution.
 *
 * @author martimy
 */
public class LogNormalRandom extends Random implements RandomNumber {

    protected long mean, std;

    /**
     * Constructor of LogNormalRandom.
     * @param seed, mean, std
     */
    public LogNormalRandom(long seed, long mean, long std) {
        super(seed);
        this.mean = mean;
        this.std = std;
    }

    /**
     * Returns a random number from a LogNormal distribution
     */
    public long getNumber() {
        // needs testing
        double mean2 = mean * mean;
        double std2 = std * std;
        double mu = Math.log(mean) - 0.5 * Math.log(1 + std2 / mean2);
        double segma = Math.sqrt(Math.log(std2 / mean2 + 1));

        double Y = nextGaussian();
        double X = Math.exp(Y * segma + mu);
        return (new Double(X)).longValue();
    }
}
