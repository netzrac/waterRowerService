/**
 *
 * Copyright 2019 Carsten Pratsch
 *
 * This file is part of waterRowerService.
 *
 * waterRowerService is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * waterRowerService is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with waterRowerService.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package waterRowerService;

import com.sun.javafx.binding.StringFormatter;

/**
 * 
 * @author cp
 *
 *
 * Byte	Description
 * 00-02	? (fixed to 0xA 0x8 0x0)
 * 03-04	Total Minutes
 * 05-06	Total Seconds
 * 07-11	Total Distance
 * 12	?
 * 13-14	Minutes to 500m
 * 15-16	Seconds to 500m
 * 17-19	SPM
 * 20-22	Watt
 * 23-26	Cal/h
 * 27-28	Level
 * 29	\n
 *
 */
public class DataRecord {

	public String getStr0to2() {
		return str0to2;
	}

	public void setStr0to2(String str0to2) {
		this.str0to2 = str0to2;
	}

	public int getTotalSeconds() {
		return totalSeconds;
	}

	public void setTotalSeconds(int totalSeconds) {
		this.totalSeconds = totalSeconds;
	}

	public Integer getTotalDistance() {
		return totalDistance;
	}

	public void setTotalDistance(Integer totalDistance) {
		this.totalDistance = totalDistance;
	}

	public int getCurr500mSeconds() {
		return curr500mSeconds;
	}

	public void setCurr500mSeconds(int curr500mSeconds) {
		this.curr500mSeconds = curr500mSeconds;
	}

	public Integer getSpm() {
		return spm;
	}

	public void setSpm(Integer spm) {
		this.spm = spm;
	}

	public Integer getWatt() {
		return watt;
	}

	public void setWatt(Integer watt) {
		this.watt = watt;
	}

	public Integer getCalPerHr() {
		return calPerHr;
	}

	public void setCalPerHr(Integer calPerHr) {
		this.calPerHr = calPerHr;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	private String str0to2;

	private int totalSeconds;

	private int totalDistance;

	private int curr500mSeconds;

	private int spm;

	private int watt;

	private int calPerHr;

	private int level;
	
	public static String dataId="A80"; 

	DataRecord( String rawData) throws DataRecordException {
		String id=rawData.substring(0,3);
		if( rawData.length()!=29 || !dataId.equals(id)) {
			throw new DataRecordInvalidDataException();
		}
		setTotalSeconds(Integer.parseInt( rawData.substring(3,5))*60
				+ Integer.parseInt( rawData.substring(5,7)));
		setTotalDistance(Integer.parseInt( 
				rawData.substring(7,12)));
		setCurr500mSeconds(Integer.parseInt( rawData.substring(13,15))*60
				+ Integer.parseInt( rawData.substring(15,17)));
		setSpm(Integer.parseInt( 
				rawData.substring(17,20)));
		setWatt(Integer.parseInt( 
				rawData.substring(20,23)));
		setCalPerHr(Integer.parseInt( 
				rawData.substring(23,27)));
		setLevel(Integer.parseInt( 
				rawData.substring(27,29)));		
		
	}
	
	public String toString() {
		return StringFormatter.format( "%d %d\n", 
				getTotalSeconds(), 
				getTotalDistance())
				.getValue();
	}
	
}
