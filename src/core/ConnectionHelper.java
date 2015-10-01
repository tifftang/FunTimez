package core;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
 
public class ConnectionHelper {
 
    @SuppressWarnings("unused")
	private Context context;
    private ConnectivityManager cm;
    private NetworkInfo activeNetwork;
 
    public ConnectionHelper(Context context){
        this.context = context;
    	this.cm =
    	        (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
    	 
    	this.activeNetwork = cm.getActiveNetworkInfo();
    }
 
    public enum Connection
    {
      WIFI, ETHERNET, DATA
    }
    
    /**
     * Check if user is connected to the internet through data, wifi, etc
     * **/
    public boolean hasConnection(){
    	return this.activeNetwork != null && this.activeNetwork.isConnectedOrConnecting();
    }
 
    /**
     * Check if user is connected via wifi
     * **/
    public boolean isWifi(){
    	return this.activeNetwork.getType() == ConnectivityManager.TYPE_WIFI;
    }

    /**
     * Check if user is connected via data
     * **/
    public boolean isData(){
    	return this.activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE;
    }

    /**
     * Check if user is connected via ethernet
     * **/
    public boolean isEthernet(){
    	return this.activeNetwork.getType() == ConnectivityManager.TYPE_ETHERNET;
    }

    public static String getConnectivityStatus(Context context) {
    	ConnectivityManager cm =
    	        (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
    	 
    	NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
    	if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI){
    		return "WIFI";
    	}
    	if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE){
    		return "DATA";
    	}
    	if (activeNetwork.getType() == ConnectivityManager.TYPE_ETHERNET){
    		return "ETHERNET";
    	}
    	return "NOT CONNECTED";
    }


}