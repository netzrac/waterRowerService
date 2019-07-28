package waterRowerService;

public interface WaterRowerService {
	void registerNotifier( DataNotifier dn);
	void unregisterNotifier( DataNotifier dn);
	void reset() throws DataConnectorException;
	void setHeartrate(String substring);
	String getHeartrate();
}
