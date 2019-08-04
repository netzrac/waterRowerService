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

public class DataEvent {
	
	public enum EventType { UNKNOWN, DATA, C_REPL, F_REPL, V_REPL, LEVEL, H_REPL, R_REPL, WARN, QUIT, T_REPL}
	private String rawData;
	
	public DataEvent( String rawData) {
		this.rawData=rawData;
	}
	
	EventType getEventType() {
		EventType et=EventType.UNKNOWN;
		switch( rawData.charAt(0)) {
		case 'A':
			et=EventType.DATA;
			break;
		case 'C':
			et=EventType.C_REPL;
			break;
		case 'V':
			et=EventType.V_REPL;
			break;
		case 'L':
			et=EventType.LEVEL;
			break;
		case 'H':
			et=EventType.H_REPL;
			break;
		case 'R':
			et=EventType.R_REPL;
			break;
		case 'T':
			et=EventType.T_REPL;
			break;
		case 'W':
			et=EventType.WARN;
			break;
		case 'X':
			et=EventType.QUIT;
			break;
		case 'F':
			et=EventType.F_REPL;
			break;
		}
		return et;
	}
	String getRawData() {
		return rawData;
	}
	
}
