package waterRowerService;

public interface DataConnector {

	void initialize() throws DataConnectorException;
	void reset() throws DataConnectorException;
	void register( DataNotifier notifier);
	void unregister( DataNotifier notifier);
	void write( String data) throws DataConnectorException;
	
}
