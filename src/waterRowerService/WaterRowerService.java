package waterRowerService;

import waterRowerService.DataEvent.EventType;

public class WaterRowerService {

	private SerialDataConnector sdc;

	public WaterRowerService() throws DataConnectorException {
		
		class SimpleDataNotifier implements DataNotifier {

			private WaterRowerService waterRowerService;

			public SimpleDataNotifier(WaterRowerService waterRowerService) {
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

		DataNotifier notifier=new SimpleDataNotifier(this);

		sdc=new SerialDataConnector();
		sdc.register(notifier);
		
		reset();
		
	}
	
	public void reset() throws DataConnectorException {
		int waitfor=50;
		
		synchronized (sdc) {
			sdc.write( "C\n");
			sleep(waitfor);
			sdc.write( "T\n");
			sleep(waitfor);
			sdc.write( "V\n");
			sleep(waitfor);
			sdc.write( "L\n");
			sleep(waitfor);
			sdc.write( "H\n");
			sleep(waitfor);
			sdc.write( "R\n");		
		}

	}

	private static Object semaphore=new Object();

	public static void main(String[] args) throws DataConnectorException {

		new WaterRowerService();
		sleep();
	}

	private static void sleep() {
		sleep(0);
	}
	
	private static void sleep(int i) {
		try {
			synchronized (semaphore) {
				if( i==0) {
					semaphore.wait();
				} else {
					semaphore.wait(i);
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
