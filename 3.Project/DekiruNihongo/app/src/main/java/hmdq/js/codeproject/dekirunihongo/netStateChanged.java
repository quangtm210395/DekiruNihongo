package hmdq.js.codeproject.dekirunihongo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class netStateChanged extends BroadcastReceiver {
    DataProvider dp;
    Context ct;

    @Override
    public void onReceive(Context context, Intent intent) {
        ct = context;
        if(isOnline(ct)) {
            dp = new DataProvider(ct);
            final int localRev = dp.getLocalRev();
            dp.requestData("getrev", new DataProvider.OnDataReceived() {
                @Override
                public void onReceive(String result) {
                    final int newestRev = Integer.parseInt(result);
                    if (localRev < newestRev) new DataProvider(ct).requestData("getAll", new DataProvider.OnDataReceived() {
                        @Override
                        public void onReceive(String result) {
                            dp.updateData(String.valueOf(newestRev), result);
                            String noti = "Đã Update lên revision " + String.valueOf(newestRev) + "\nRestart lại App để làm mới";
                            Toast.makeText(ct, noti, Toast.LENGTH_LONG).show();
                        }
                    }); else Toast.makeText(ct, "No Update", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    boolean isOnline(Context context) {
        ConnectivityManager cmgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cmgr.getActiveNetworkInfo();
        return ((info != null)&&(info.isConnectedOrConnecting()));
    }
}
