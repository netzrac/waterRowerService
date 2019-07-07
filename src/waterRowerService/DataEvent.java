package waterRowerService;

public class DataEvent {
	
	public enum EventType { UNKNOWN, DATA, C_REPL, F_REPL, V_REPL, LEVEL, H_REPL, R_REPL, WARN, QUIT, T_REPL}
	private String rawData;
	
	public DataEvent( String rawData) {
		this.rawData=rawData;
	}
	
	EventType getEventType() {
		EventType et=EventType.UNKNOWN;
		switch( rawData.charAt(0)) {
		case 'A':
			et=EventType.DATA;
			break;
		case 'C':
			et=EventType.C_REPL;
			break;
		case 'V':
			et=EventType.V_REPL;
			break;
		case 'L':
			et=EventType.LEVEL;
			break;
		case 'H':
			et=EventType.H_REPL;
			break;
		case 'R':
			et=EventType.R_REPL;
			break;
		case 'T':
			et=EventType.T_REPL;
			break;
		case 'W':
			et=EventType.WARN;
			break;
		case 'X':
			et=EventType.QUIT;
			break;
		case 'F':
			et=EventType.F_REPL;
			break;
		}
		return et;
	}
	String getRawData() {
		return rawData;
	}
	
}
