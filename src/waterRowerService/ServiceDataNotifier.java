package waterRowerService;

import waterRowerService.DataEvent.EventType;

public class ServiceDataNotifier implements DataNotifier {

	private WaterRowerService waterRowerService;

	public ServiceDataNotifier(WaterRowerService waterRowerService) {
		this.waterRowerService=waterRowerService;
	}

	@Override
	public void readEvent(DataEvent e) throws DataConnectorException {
		System.out.println( e.getEventType()+": "+e.getRawData());
		if( e.getEventType()==EventType.F_REPL) {
			waterRowerService.reset();
		}
	}
	
}
