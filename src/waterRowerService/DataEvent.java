package waterRowerService;

public interface DataEvent {
	
	public enum EventType { DATA};
	
	EventType getEventType();
	String getRawData();
	
}
