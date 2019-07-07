package waterRowerService;

public interface DataNotifier {

	void readEvent( DataEvent e) throws DataConnectorException;
	
}
