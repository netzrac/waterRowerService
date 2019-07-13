package waterRowerService;

import java.util.ArrayList;

public class SimulatorDataConnector implements DataConnector, Runnable {

	private ArrayList<DataNotifier> dnMap=new ArrayList<DataNotifier>();
	
	public SimulatorDataConnector() {
	}
	
	@Override
	public void register(DataNotifier notifier) {
		dnMap.add(notifier);
	}

	@Override
	public void unregister(DataNotifier notifier) {
		dnMap.remove(notifier);
	}

	@Override
	public void write(String data) throws DataConnectorException {
		// input for simulator ignored
	}

	@Override
	public void run() {
		try {
			int min=0;
			int sec=0;
			int dist=0;
			while( true) {
				try {
					Thread.sleep(3000); // wait for 3s
				} catch (InterruptedException e1) {
				} 
				String rawData; // e.g. A8000200005110314021048046502
				/*
				 * * 03-04 Total Minutes 05-06 Total Seconds 07-11 Total Distance 12 ? 13-14
				 * Minutes to 500m 15-16 Seconds to 500m 17-19 SPM 20-22 Watt 23-26 Cal/h 27-28
				 * Level
				 */			
				dist+=3;
				min=dist/60;
				sec=dist%60;
				if( dist>9999) dist=0;
				rawData = "A80"
				+ String.format("%02d%02d%04d", min, sec, dist)
				+"110314021048046502";
				DataEvent e=new DataEvent(rawData);
				for( DataNotifier dn:dnMap) {
					dn.readEvent(e);
				}
			}
		} catch (DataConnectorException e) {
			System.err.println( "Exception caught in simulation loop: "+e.getLocalizedMessage());
		} 
	}

}
