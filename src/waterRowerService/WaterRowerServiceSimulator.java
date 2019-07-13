package waterRowerService;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WaterRowerServiceSimulator implements WaterRowerService {

	private SimulatorDataConnector sdc;
	private DataNotifier serviceNotifier;
	private Thread thread;

	public WaterRowerServiceSimulator() throws DataConnectorException {

		System.out.println(this.getClass().getName());
		sdc=new SimulatorDataConnector();
		thread=new Thread(sdc);
		thread.start();
		this.serviceNotifier=new ServiceDataNotifier(this);
		
		registerNotifier(serviceNotifier);

		
		reset();
		
	}
	
	public void reset() throws DataConnectorException {

		System.out.println(this.getClass().getName()+"::reset()");

	}
	
	@Override
	public void registerNotifier(DataNotifier notifier) {
		System.out.println(this.getClass().getName()+"::registerNotifier()");
		sdc.register(notifier);
	}
	
	public void unregisterNotifier( DataNotifier notifier) {
		System.out.println(this.getClass().getName()+"::unregisterNotifier()");
		sdc.unregister(notifier);
	}
	
	

	public static void main(String[] args) throws DataConnectorException, IOException {

		WaterRowerServiceSimulator wrs=new WaterRowerServiceSimulator();
		
		ServerSocket listener = new ServerSocket(1963);
		try {
            System.out.println("The water rower simulation server is running...");
            ExecutorService pool = Executors.newFixedThreadPool(9);
            while (true) {
                pool.execute(new Client(wrs, listener.accept()));
            }
        } catch( IOException e) {
        	System.err.println( "Error while listening: "+e.getLocalizedMessage());
        }
		
		wrs.close();
		listener.close();
		
	}
	

	
	private void close() {
		unregisterNotifier( serviceNotifier);
		
	}


}
