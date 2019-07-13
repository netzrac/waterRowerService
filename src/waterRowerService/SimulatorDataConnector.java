package waterRowerService;

import java.util.ArrayList;

public class SimulatorDataConnector implements DataConnector, Runnable {

	private ArrayList<DataNotifier> dnMap=new ArrayList<DataNotifier>();
	private int min=0;
	private int sec=0;
	private int dist=0;
	private Object semaphore=new Object();
	
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
		if( "R".contentEquals(data)) {
			synchronized( semaphore) {
				dist=0;
			}
		}
	}

	@Override
	public void run() {
		try {
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
				int dist=0;
				synchronized( semaphore) {
					this.dist+=3;
					if( this.dist>9999) this.dist=0;
					dist=this.dist;
				}
				min=dist/60;
				sec=dist%60;
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
