package core;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;
import java.util.concurrent.LinkedBlockingQueue;

import com.funtimez.ChatroomActivity;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;


public class Server extends Service {
	private final IBinder myBinder = new MyLocalBinder();
	private Handler mHandler;
	Messenger mMessenger;
	private Vector<Socket> clients = new Vector<Socket>();
	private LinkedBlockingQueue<String> messages = new LinkedBlockingQueue<String>();

	public class MyLocalBinder extends Binder {
        public Server getService() {
            return Server.this;
        }
	}
	public void setHandler(Handler handler)
	{ 
	   mHandler = handler;
	   mMessenger = new Messenger(mHandler);
	}
	private ServerSocket serverSocket;
	private Thread serverThread;
	public static final int SERVERPORT = 6000;

	class ServerThread implements Runnable {

		public void run() {
			Socket socket = null;
			Log.i("Thread", "server thread running");
			try {
				serverSocket = new ServerSocket(SERVERPORT);
			} catch (IOException e) {
				e.printStackTrace();
			}
			while (!Thread.currentThread().isInterrupted()) {
				//Log.i("Thread", "got message");
				try {
					
					socket = serverSocket.accept();
					clients.add(socket);
					CommunicationThread commThread = new CommunicationThread(socket);
					new Thread(commThread).start();
					new Thread(new MessageThread()).start();
					

				} catch (IOException e) {
					e.printStackTrace();
				}  
			}
		}
	}
	class CommunicationThread implements Runnable {
		private Socket clientSocket;

		private BufferedReader input;

		public CommunicationThread(Socket clientSocket) {

			this.clientSocket = clientSocket;

			try {

				this.input = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		public void run() {

			while (!Thread.currentThread().isInterrupted()) {
				//Log.i("Thread", "got message");
				try {
					String read = input.readLine();
					messages.put(read);


				} catch (IOException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}  
			}
		}
	}
	
	class MessageThread implements Runnable {

		public void run() {

			while (!Thread.currentThread().isInterrupted()) {
				//Log.i("Thread", "got message");
				try {
					String message = (String) messages.take();
					for(Socket s: clients){
						OutputStream os = s.getOutputStream();
						PrintWriter pw = new PrintWriter(os, true);
						pw.println(message);
						pw.flush();					
					}

				} catch (IOException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}  
			}
		}
	}

    @Override
    public void onCreate() {
		this.serverThread = new Thread(new ServerThread());
		this.serverThread.start();
		
    }

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return myBinder;
	}
}