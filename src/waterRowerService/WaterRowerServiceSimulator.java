package waterRowerService;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WaterRowerServiceSimulator implements WaterRowerService {

	private SimulatorDataConnector sdc;
	private DataNotifier serviceNotifier;
	private Thread thread;
	private String heartrate="000";

	public WaterRowerServiceSimulator() throws DataConnectorException {

		System.out.println(this.getClass().getName());
		String replayFile=SimulatorConfig.getStringOptionValue("file");
		if( replayFile==null ) {
			sdc=new StandardSimulatorDataConnector();
		} else {
			sdc=new ReplayDataConnector(replayFile);
		}

		this.serviceNotifier=new ServiceDataNotifier(this);
		registerNotifier(serviceNotifier);

		thread=new Thread(sdc);
		thread.start();
		
		reset();
	}
	
	public void reset() throws DataConnectorException {

		System.out.println(this.getClass().getName()+"::reset()");
		this.sdc.write("R");

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

		SimulatorConfig.setConfigOptions(args);
		
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
