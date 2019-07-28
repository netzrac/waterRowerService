package waterRowerService;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class ReplayDataConnector extends SimulatorDataConnector {

	private ArrayList<DataNotifier> dnMap=new ArrayList<DataNotifier>();
	private String replayFile;
	private int factor;
	
	public ReplayDataConnector(String replayFile) {
		this.replayFile=replayFile;
		this.factor=SimulatorConfig.getIntOptionValue("timefactor", 1000);
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
			// wait n secs
			System.out.println("Waiting before starting...");
			try {
				Thread.sleep(18*1000);
			} catch (InterruptedException e2) {
			}
			System.out.println("Starting replay...");
			// open file
			Scanner scanner=new Scanner( new File(replayFile));
			String currData=null;
			String nextData=null;
			DataRecord currDataRecord=null;
			DataRecord nextDataRecord=null;
			if( scanner.hasNextLine()) {
				currData=getNextDataLine(scanner);
			}
			if( scanner.hasNextLine()) {
				nextData=getNextDataLine(scanner);
				if( nextData!=null) {
					if(nextData.length()>29) {
						nextData=nextData.substring(0, 29);
					}
					nextDataRecord=new DataRecord(nextData);
				}
			}
			while( currData!=null) {
				currDataRecord=new DataRecord(currData);
				// trigger read event
				DataEvent e=new DataEvent(currData);
				for( DataNotifier dn:dnMap) {
					dn.readEvent(e);
				}
				try {
					// wait n secs
					if( nextData!=null&&currData!=null) {
						Thread.sleep((nextDataRecord.getTotalSeconds()-currDataRecord.getTotalSeconds())*factor);
					}
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				// read next input
				currData=nextData;
				if( scanner.hasNextLine()) {
					nextData=getNextDataLine(scanner);
					if(nextData.length()>29) {
						nextData=nextData.substring(0, 29);
					}
					if( nextData!=null) {
						nextDataRecord=new DataRecord(nextData);
					} else {
						nextDataRecord=null;
					}
				} else {
					nextData=null;
				}
			}
			System.out.println("Replay finished.");
			scanner.close();
			System.exit(0);
		} catch (DataConnectorException | FileNotFoundException | DataRecordException e) {
			System.err.println( "Exception caught in simulation loop: "+e.getLocalizedMessage());
			System.exit( -1);
		} 
	}
	
	private String getNextDataLine( Scanner scanner) {
		String data=null;
		while( scanner.hasNextLine()) {
			data=scanner.nextLine();
			if( DataRecord.dataId.equals(data.substring(0, data.length()<3?data.length():3))) {
				return data;
			} 
		}
		return null;
	}

	@Override
	public void register(DataNotifier notifier) {
		dnMap.add(notifier);
	}

	@Override
	public void unregister(DataNotifier notifier) {
		dnMap.remove(notifier);
	}

}
