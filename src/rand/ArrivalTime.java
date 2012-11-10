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
 * 
 * @author martimy
 */
public class ArrivalTime implements RandomNumber{
	protected Random r1, r2;
	protected double mean, std, row;
	
	public ArrivalTime(long seed, double mean, double std, double row) {
		r1 = new Random(seed);
		r2 = new Random(seed + 500);
		this.mean = mean;
		this.std = std;
		this.row = row;
	}

        /**
         * Returns arrival time based on ? distribution
         */
	public long getNumber() {
		double v1, v2, sum;
		v1 = r1.nextGaussian() * std + mean;
		v2 = r2.nextGaussian() * std + mean;
		sum = Math.abs(v1-v2);
		//System.out.print(sum+",");
		return ( new Double( 3600.0 / (row * sum) ).longValue() );
	}
}
