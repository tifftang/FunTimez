package com.funtimez;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ActionBar.LayoutParams;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.text.InputType;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import core.FileHelper;
import core.Server;

public class ChatroomActivity extends Activity {
    /** Messenger for communicating with the service. */
    Server mService = null;
    //Server mService;
    /** Flag indicating whether we have called bind on the service. */
    boolean mBound;
	public static final int SERVERPORT = 6000;
	private static final String LOG_TAG = null;
	private Socket socket;
	private Button bSend, bAdd;
	private FunTimezApp app;
	private String username;
	private String m_Text = "";
	private FileHelper fileHelper = new FileHelper();

    /**
     * Class for interacting with the main interface of the service.
     */
    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            // This is called when the connection with the service has been
            // established, giving us the object we can use to
            // interact with the service.  We are communicating with the
            // service using a Messenger, so here we get a client-side
            // representation of that from the raw IBinder object.
            mService = ((Server.MyLocalBinder) service).getService();
            mService.setHandler(mHandler);
            mBound = true;
            new Thread(new ClientThread()).start();
        }

        public void onServiceDisconnected(ComponentName className) {
            // This is called when the connection with the service has been
            // unexpectedly disconnected -- that is, its process crashed.
            mService = null;
            mBound = false;
        }
    };
	private TextView text;
	private Handler updateConversationHandler;
	private Button bSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatroom);
        startService(new Intent(this, Server.class));
        // Bind to the service
        bindService(new Intent(this, Server.class), mConnection,Context.BIND_AUTO_CREATE);

    }

    @Override
    protected void onStart() {
        super.onStart();

		text = (TextView) findViewById(R.id.display_text);
		app = ((FunTimezApp)getApplicationContext());
		//username = app.getUser().getUsername();
		final EditText send_text = (EditText) findViewById(R.id.send_text);
		text.setMovementMethod(new ScrollingMovementMethod());
		updateConversationHandler = new Handler();

		
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
				        int pixels = m_Text.length() * 80;
				        // TODO: Create button
				         final Button myButton = new Button(ChatroomActivity.this);
		                 myButton.setText(m_Text);
		                 final LinearLayout ll = (LinearLayout)findViewById(R.id.button_layout);
		                 LayoutParams lp = new LayoutParams(pixels, LayoutParams.MATCH_PARENT);
		                 Toast.makeText(getApplicationContext(), m_Text, Toast.LENGTH_SHORT).show();	
		                 ll.addView(myButton, lp);
		                /* LayoutParams params = new LayoutParams(10, LayoutParams.MATCH_PARENT);
		                 myButton.setLayoutParams(params);*/
		                 myButton.setOnClickListener(new OnClickListener() {
		                	    public void onClick(View v)
		                	    {
		                	        CharSequence shortcut = myButton.getText();
		                	        send_text.append(shortcut);
		                	        send_text.setSelection(send_text.getText().length());
		                	        
		                	    } 
		                	});
		                 myButton.setOnLongClickListener(new View.OnLongClickListener() {

		                     @Override
		                     public boolean onLongClick(View v) {
		         				AlertDialog.Builder builder = new AlertDialog.Builder(ChatroomActivity.this);
		        				builder.setTitle("Edit new shortcut button");
		        				// Set up the input
		        				final EditText input = new EditText(ChatroomActivity.this);
		        				// Set input type
		        				input.setInputType(InputType.TYPE_CLASS_TEXT);
		        				builder.setView(input);
		        				builder.setPositiveButton("OK", new DialogInterface.OnClickListener() { 
		        				    @Override
		        				    public void onClick(DialogInterface dialog, int which) {
		        				        m_Text = input.getText().toString();
		        				        int pixels = m_Text.length() * 80;
		        				        ViewGroup.LayoutParams lp =  myButton.getLayoutParams();
		        				        lp.width = pixels;
		        				        myButton.setLayoutParams(lp);
		        		                myButton.setText(m_Text);
		        				    }
		        				});
		        				builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		        				    @Override
		        				    public void onClick(DialogInterface dialog, int which) {
		        				        dialog.cancel();
		        				    }
		        				});
		        				builder.setNeutralButton("Delete",  new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog, int which) {
										ll.removeView(myButton);
									}
		        					
		        				});
		        				builder.show();
		                         return true;
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
		bSave = (Button) findViewById(R.id.save);
		bSave.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String textToStore = text.getText().toString();
				String[] lines = textToStore.split( "\n" );
			    try {
			    	File dir = fileHelper.getChatStorageDir("test");
			    	FileOutputStream writer = new FileOutputStream(new File(dir + "/test.html"));
			    	writer.write("<html>".getBytes());
			    	writer.write("<head>".getBytes());
			    	writer.write("<title>Chat History</title>".getBytes());
			    	writer.write("<body>".getBytes());
			    	writer.write("<table width=\"100%%\" cellpadding=\"1\" cellspacing=\"0\">".getBytes());

			    	for(String l: lines){
			    		String msg = "<tr><td class=\"msg\" width=\"100%\"><FONT face=\"Arial\" size=\"2\" color=\"#000000\">" + l + "</FONT></td></tr>";
			    		writer.write(msg.getBytes());
			    	}
			    	writer.write("</body>".getBytes());
			    	writer.write("</body>".getBytes());
			    	writer.write("</html>".getBytes());
					writer.flush();
					writer.close();
					Toast.makeText(getApplicationContext(), "Saving", Toast.LENGTH_SHORT).show();
			    	
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}


				

			}
		});
        
    }
	class ClientThread implements Runnable {

		@Override
		public void run() {
			Log.i("Thread", "client thread running");
			try {
				Log.i("Thread", getLocalIpAddress());
				socket = new Socket(getLocalIpAddress(), SERVERPORT);
			
			} catch (UnknownHostException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
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

    @Override
    protected void onStop() {
        super.onStop();
        // Unbind from the service
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
    }
    public static final int MSG_SEND = 1;
    /**
     * Handler of incoming messages from clients.
     */
    public class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_SEND:
                	Bundle bundle = msg.getData();
                	updateConversationHandler.post(new updateUIThread(bundle.getString("msg")));
                	break;
                default:
                	super.handleMessage(msg);
            }
        }
    }

    /**
     * Target we publish for clients to send messages to IncomingHandler.
     */
    final IncomingHandler mHandler = new IncomingHandler();
    final Messenger mMessenger = new Messenger(new IncomingHandler());


    
}