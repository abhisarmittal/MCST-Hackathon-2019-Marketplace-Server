import java.io.IOException;

public class Main {
	
	public static void main(String[] args) {
		try {
			Server.startServer();
			Server.listenForConncetions();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
//	public static void addShutdownHook() {
//		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//				try {
//					for(int i = 0; i < Server.clientSockets.size(); i++) {
//						if(Server.clientSockets.get(i).isConnected()) {
//							break;
//						}
//					}
//					Server.closeServer();
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//		}));
//	}

}
