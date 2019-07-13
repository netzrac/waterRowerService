package waterRowerService;

public interface WaterRowerService {
	void registerNotifier( DataNotifier dn);
	void unregisterNotifier( DataNotifier dn);
	void reset() throws DataConnectorException;
}
