package waterRowerService;

public interface SerialDataEvent {
	
	public enum EventType { DATA};
	
	EventType getEventType();
	String getRawData();
	
}
