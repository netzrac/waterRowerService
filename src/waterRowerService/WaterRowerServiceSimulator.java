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

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WaterRowerServiceSimulator implements WaterRowerService {

	private SimulatorDataConnector sdc;
	private DataNotifier serviceNotifier;
	private Thread thread;
	private List<HeartrateNotifier> heartrateNotifiers=new ArrayList<HeartrateNotifier>();

	public WaterRowerServiceSimulator() throws DataConnectorException {

		System.out.println(this.getClass().getName());
		String replayFile=SimulatorConfig.getStringOptionValue("file");
		if( replayFile==null ) {
			sdc=new StandardSimulatorDataConnector();
		} else {
			sdc=new ReplayDataConnector(replayFile);
		}

		this.serviceNotifier=new ServiceDataNotifier(this);
		registerNotifier(serviceNotifier);

		thread=new Thread(sdc);
		thread.start();
		
		reset();
	}
	
	public void reset() throws DataConnectorException {

		System.out.println(this.getClass().getName()+"::reset()");
		this.sdc.write("R");

	}
	
	@Override
	public void registerNotifier(DataNotifier notifier) {
		System.out.println(this.getClass().getName()+"::registerNotifier()");
		sdc.register(notifier);
	}
	
	public void unregisterNotifier( DataNotifier notifier) {
		System.out.println(this.getClass().getName()+"::unregisterNotifier()");
		sdc.unregister(notifier);
	}
	
	

	public static void main(String[] args) throws DataConnectorException, IOException {

		SimulatorConfig.setConfigOptions(args);
		
		WaterRowerServiceSimulator wrs=new WaterRowerServiceSimulator();
		
		ServerSocket listener = new ServerSocket(1963);
		try {
            System.out.println("The water rower simulation server is running...");
            ExecutorService pool = Executors.newFixedThreadPool(9);
            while (true) {
                pool.execute(new Client(wrs, listener.accept()));
            }
        } catch( IOException e) {
        	System.err.println( "Error while listening: "+e.getLocalizedMessage());
        }
		
		wrs.close();
		listener.close();
		
	}
	
	private void close() {
		unregisterNotifier( serviceNotifier);
	}

	@Override
	public void registerHeartrateNotifier( HeartrateNotifier heartrateNotifier) {
		heartrateNotifiers.add(heartrateNotifier);
	}

	public void unregisterHeartrateNotifier( HeartrateNotifier heartrateNotifier) {
		heartrateNotifiers.remove(heartrateNotifier);
	}
	
	public void setHeartrate(String heartrateString) {
		for( HeartrateNotifier hn:heartrateNotifiers) {
			hn.heartrateEvent( Integer.parseInt( heartrateString));
		}
	}

}
