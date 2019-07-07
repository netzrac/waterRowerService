package waterRowerService;

public class WaterRowerService {

	public WaterRowerService() {
		// TODO Auto-generated constructor stub
	}
	
	private static Object semaphore=new Object();

	public static void main(String[] args) throws DataConnectorException {

		class SimpleDataNotifier implements DataNotifier {

			@Override
			public void readEvent(DataEvent e) {
				System.out.println( e.getEventType()+": "+e.getRawData());
			}
			
		}

		DataNotifier notifier=new SimpleDataNotifier();

		SerialDataConnector sdc=new SerialDataConnector();
		int waitfor=200;
		sdc.register(notifier);
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
