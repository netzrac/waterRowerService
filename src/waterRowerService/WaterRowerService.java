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
		sdc.register(notifier);
		sdc.write( "C\n");
		sdc.write( "T\n");
		sdc.write( "V\n");
		sdc.write( "L\n");
		sdc.write( "H\n");
		sdc.write( "R\n");
		
		try {
			synchronized (semaphore) {
				semaphore.wait();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

}
