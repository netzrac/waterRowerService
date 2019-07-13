package waterRowerService;

import java.util.ArrayList;
import java.util.HashMap;

import com.sun.javafx.binding.StringFormatter;

public class SimulatorDataConnector implements DataConnector {

	private ArrayList<DataNotifier> dnMap=new ArrayList<DataNotifier>();
	private Runnable reader=null;
	
	public SimulatorDataConnector() {
		reader=new Runnable() {
			
			@Override
			public void run() {
				try {
					int hrs=0;
					int min=0;
					int dist=0;
					while( true) {
						Thread.sleep(3000); // wait for 3s
						String rawData; // e.g. A8000200005110314021048046502
						/*
						 * * 03-04 Total Minutes 05-06 Total Seconds 07-11 Total Distance 12 ? 13-14
						 * Minutes to 500m 15-16 Seconds to 500m 17-19 SPM 20-22 Watt 23-26 Cal/h 27-28
						 * Level
						 */						
						rawData = "A80"
						+ StringFormatter.format("%02d%02d%04d", hrs, min, dist)
						+"110314021048046502\n";
						DataEvent e=new DataEvent(rawData);
						for( DataNotifier dn:dnMap) {
							dn.readEvent(e);
						}
					}
				} catch (InterruptedException | DataConnectorException e) {
				} 
			}
		};
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

}
