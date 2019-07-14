package waterRowerService;

import java.util.ArrayList;

public class ReplayDataConnector extends SimulatorDataConnector {

	private ArrayList<DataNotifier> dnMap=new ArrayList<DataNotifier>();
	private int min=0;
	private int sec=0;
	private int dist=0;
	private String replayFile;
	private boolean restart=false;
	
	public ReplayDataConnector(String replayFile) {
		this.replayFile=replayFile;
	}
	

	@Override
	public void write(String data) throws DataConnectorException {
		if( "R".contentEquals(data)) {
			restart=true;
		}
	}

	@Override
	public void run() {
		try {
			while( true) {
				// TODO open file
				// read input
				String rawData=null;
				DataEvent e=new DataEvent(rawData);
				for( DataNotifier dn:dnMap) {
					dn.readEvent(e);
				}
				// wait n secs
			}
		} catch (DataConnectorException e) {
			System.err.println( "Exception caught in simulation loop: "+e.getLocalizedMessage());
		} 
	}

}
