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
 * This is a class that has a RandomNumber interface but always returns a fixed number.
 *
 * @author martimy
 */
public class NoRandom extends Random implements RandomNumber {

    protected long mean;

    /**
     * Constructor of NoRandom.
     * @param seed, mean
     */
    public NoRandom(long mean) {
        super(0);
        this.mean = mean;
    }

    /**
     * Returns a fixed number
     */
    public long getNumber() {
        return mean;
    }
}
