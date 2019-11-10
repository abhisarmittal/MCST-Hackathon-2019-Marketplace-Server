import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {

	private static ServerSocket serverSocket;
	static ArrayList<Socket> clientSockets;
	private static ArrayList<String> products;

	public static void startServer() throws IOException {
		serverSocket = new ServerSocket(4069);
		System.out.println("Server started!");
		clientSockets = new ArrayList<Socket>();
		products = new ArrayList<String>();
	}

	public static void listenForConncetions() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						System.out.println("Looking for connection on port " + serverSocket.getLocalPort());
						Socket clientSocket = serverSocket.accept();
						clientSockets.add(clientSocket);
//						DataOutputStream clientOS = new DataOutputStream(clientSocket.getOutputStream());
//						for(int i = 0; i < products.size(); i++) {
//							clientOS.writeUTF(products.get(i));
//							clientOS.flush();
//						}
						System.out.println("Established connection");
						listenForIncomingProduct(clientSocket);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

	public static void listenForIncomingProduct(Socket clientSocket) throws IOException {
		DataInputStream clientIS = new DataInputStream(clientSocket.getInputStream());
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				while (true) {
					try {
						System.out.println("Waiting for data from client: " + clientSocket.getInetAddress());
						String incomingProductData = clientIS.readUTF().toString();
						System.out.println("Received data: " + incomingProductData);
						if (!incomingProductData.startsWith("BUY")) {
							synchronized (products) {
								products.add(incomingProductData);
								sendProductDataToAll(incomingProductData, clientSocket);
							}
						}else {
							sendProductDataToAll(incomingProductData, clientSocket);
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

	public static void sendProductDataToAll(String data, Socket socket) throws IOException {
		synchronized (clientSockets) {
			for (int i = 0; i < clientSockets.size(); i++) {
				if (clientSockets.get(i) != socket) {
					DataOutputStream clientOS = new DataOutputStream(clientSockets.get(i).getOutputStream());
					clientOS.writeUTF(data);
					clientOS.flush();
					System.out.println("Data has been sent to all!");
				}
			}
		}
	}

	public static void closeServer() throws IOException {
		for (int i = 0; i < clientSockets.size(); i++) {
			clientSockets.get(i).getInputStream().close();
			clientSockets.get(i).getOutputStream().close();
		}
		serverSocket.close();
	}

}
