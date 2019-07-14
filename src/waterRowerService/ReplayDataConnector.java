package waterRowerService;

import java.io.File;
import java.util.ArrayList;

public class ReplayDataConnector extends SimulatorDataConnector {

	private ArrayList<DataNotifier> dnMap=new ArrayList<DataNotifier>();
	private String replayFile;
	
	public ReplayDataConnector(String replayFile) {
		this.replayFile=replayFile;
		if( !new File( this.replayFile).exists()) {
			System.err.println("Replay file does not exist. Exiting application.");
			System.exit(-1);
		} else {
			System.out.println("Replaying file: "+replayFile);
		}
	}
	

	@Override
	public void write(String data) throws DataConnectorException {
		System.out.println("'"+data+"' received to send to device. Will be ignored.");
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
