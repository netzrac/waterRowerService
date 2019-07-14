package waterRowerService;

public abstract class SimulatorDataConnector implements DataConnector, Runnable {

	@Override
	abstract public void register(DataNotifier notifier);

	@Override
	abstract public void unregister(DataNotifier notifier);

	@Override
	abstract public void write(String data) throws DataConnectorException;
	
	@Override
	abstract public void run();
	
}
