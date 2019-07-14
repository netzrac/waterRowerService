package waterRowerService;

import java.util.ArrayList;

public abstract class SimulatorDataConnector implements DataConnector, Runnable {

	private ArrayList<DataNotifier> dnMap=new ArrayList<DataNotifier>();
		
	@Override
	public void register(DataNotifier notifier) {
		dnMap.add(notifier);
	}

	@Override
	public void unregister(DataNotifier notifier) {
		dnMap.remove(notifier);
	}

	@Override
	abstract public void write(String data) throws DataConnectorException;
	
	@Override
	abstract public void run();
	
}
