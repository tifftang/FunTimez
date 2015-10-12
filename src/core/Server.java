package core;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;



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
					while(true){
					
					BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					String read = input.readLine();
					Log.i("Thread", read);
					Message msg = Message.obtain(null, ChatroomActivity.MSG_SEND, 0, 0); 
					Bundle bundle = new Bundle();
					bundle.putString("msg", read);
					msg.setData(bundle);
						mMessenger.send(msg);
					}

				} catch (IOException e) {
					e.printStackTrace();
				}  catch (RemoteException e) {
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