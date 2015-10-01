package core;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/* 
 * Used to check if the connectivity changed (ie: data to wifi)
 */
public class NetworkReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent intent) {
     
        String status = ConnectionHelper.getConnectivityStatus(context);
        //TODO: wat we do once we find out connection changes?

    }
}
