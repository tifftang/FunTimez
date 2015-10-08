package com.funtimez;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

import com.funtimez.R;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import core.User;

public class ChatroomActivity extends Activity {

	private ServerSocket serverSocket;
	Handler updateConversationHandler;
	Thread serverThread = null;
	private TextView text;
	public static final int SERVERPORT = 6000;
	private Socket socket;
	private static final String LOG_TAG = null;
	private Button bSend, bAdd;
	private boolean isServer = true;
	private FunTimezApp app;
	private String username;
	private String m_Text = "";
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chatroom);

		text = (TextView) findViewById(R.id.display_text);
		app = ((FunTimezApp)getApplicationContext());
		username = app.getUser().getUsername();
		updateConversationHandler = new Handler();
		final EditText send_text = (EditText) findViewById(R.id.send_text);
		text.setMovementMethod(new ScrollingMovementMethod());
		if(isServer){
			this.serverThread = new Thread(new ServerThread());
			this.serverThread.start();
		}
		new Thread(new ClientThread()).start();
		bSend = (Button) findViewById(R.id.send);
		bSend.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					
					if(socket.isConnected()){
						PrintWriter out = new PrintWriter(new BufferedWriter(
								new OutputStreamWriter(socket.getOutputStream())),
								true);
						out.println(send_text.getText().toString());
					}else{
						
						Toast.makeText(getApplicationContext(), "Not Connected", Toast.LENGTH_SHORT).show();	
					}

				} catch (UnknownHostException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		bAdd = (Button) findViewById(R.id.add);
		bAdd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(ChatroomActivity.this);
				builder.setTitle("Add new shortcut button");
				// Set up the input
				final EditText input = new EditText(ChatroomActivity.this);
				// Set input type
				input.setInputType(InputType.TYPE_CLASS_TEXT);
				builder.setView(input);
				// Set up the buttons
				builder.setPositiveButton("OK", new DialogInterface.OnClickListener() { 
				    @Override
				    public void onClick(DialogInterface dialog, int which) {
				        m_Text = input.getText().toString();
				        // TODO: Create button
				         final Button myButton = new Button(ChatroomActivity.this);
		                 myButton.setText(m_Text);
		                 LinearLayout ll = (LinearLayout)findViewById(R.id.button_layout);
		                 LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		                 Toast.makeText(getApplicationContext(), m_Text, Toast.LENGTH_SHORT).show();	
		                 ll.addView(myButton, lp);
		                 myButton.setOnClickListener(new OnClickListener() {
		                	    public void onClick(View v)
		                	    {
		                	        CharSequence shortcut = myButton.getText();
		                	        send_text.append(shortcut);
		                	        send_text.setSelection(send_text.getText().length());
		                	        
		                	    } 
		                	});
				    }
				});
				builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				    @Override
				    public void onClick(DialogInterface dialog, int which) {
				        dialog.cancel();
				    }
				});
				builder.show();
			}
		});

	}

	@Override
	protected void onStop() {
		super.onStop();
		try {
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

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

				try {

					socket = serverSocket.accept();

					CommunicationThread commThread = new CommunicationThread(socket);
					new Thread(commThread).start();

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

				try {

					String read = input.readLine();

					updateConversationHandler.post(new updateUIThread(read));

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	class updateUIThread implements Runnable {
		private String msg;

		public updateUIThread(String str) {
			this.msg = str;
		}

		@Override
		public void run() {
			text.setText(text.getText().toString()+ username + "says: "+ msg + "\n");
		}
	}

	class ClientThread implements Runnable {

		@Override
		public void run() {
			Log.i("Thread", "client thread running");
			try {
				socket = new Socket(getLocalIpAddress(), SERVERPORT);
			
			} catch (UnknownHostException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}

		}

	}
	/* 
	 * Lifted from stack overflow, grabs the ip address of this device
	 */
	public String getLocalIpAddress() {
	    try {
	        for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
	            NetworkInterface intf = en.nextElement();
	            for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
	                InetAddress inetAddress = enumIpAddr.nextElement();
	                if (!inetAddress.isLoopbackAddress()) {
	                    return inetAddress.getHostAddress().toString();
	                }
	            }
	        }
	    } catch (SocketException ex) {
	        Log.e(LOG_TAG, ex.toString());
	    }
	    return null;
	}
}