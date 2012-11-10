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
public class EmpiricalRandom extends Random implements RandomNumber {

    protected double[] x;

    /**
     * Constructor of EmpiricalRandom.
     */
    public EmpiricalRandom(long seed, double[] xin) {
        super(seed);

        double[] ac = new double[xin.length];
        double sum = 0;
        for (int i = 0; i < xin.length; i++) {
            sum += xin[i];
            ac[i] = sum;
        }

        this.x = ac;
    }

    /**
     * Returns a random number from an exponential distribution
     */
    public long getNumber() {
        double u = nextDouble();
        int n = x.length;

        while (n>0 && u < x[n-1]) {
            n--;
        }
        
        return new Double(n).longValue();
    }
}
