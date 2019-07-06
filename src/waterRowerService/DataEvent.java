package waterRowerService;

public class DataEvent {
	
	public enum EventType { DATA}
	private String rawData;
	private EventType type;;
	
	public DataEvent( EventType type, String rawData) {
		this.rawData=rawData;
		this.type=type;
	}
	
	EventType getEventType() {
		return type;
	}
	String getRawData() {
		return rawData;
	}
	
}
