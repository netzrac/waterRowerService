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

import waterRowerService.DataEvent.EventType;

public class ServiceDataNotifier implements DataNotifier {

	private WaterRowerService waterRowerService;

	public ServiceDataNotifier(WaterRowerService waterRowerService) {
		this.waterRowerService=waterRowerService;
	}

	@Override
	public void readEvent(DataEvent e) throws DataConnectorException {
		System.out.println( e.getEventType()+": "+e.getRawData());
		if( e.getEventType()==EventType.F_REPL) {
			waterRowerService.reset();
		}
	}
	
}
