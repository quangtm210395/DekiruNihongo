package hmdq.js.codeproject.dekirunihongo;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

public class netStateChanged extends BroadcastReceiver {
    DataProvider dp;
    Context ct;
    int newestRev;
    NotificationManager manager;
    NotificationCompat.Builder builder;

    @Override
    public void onReceive(Context context, Intent intent) {
        ct = context;
        dp = new DataProvider(ct);
        manager = (NotificationManager) ct.getSystemService(Context.NOTIFICATION_SERVICE);
        builder = new NotificationCompat.Builder(ct);
        builder.setSmallIcon(R.drawable.start_logo)
                .setOngoing(true)
                .setContentTitle("Dekiru Nihongo")
                .setContentText("Đã phát hiện kết nối mạng\nĐang kiểm tra cập  nhật");
        manager.notify(111, builder.build());

        netChecker checker = new netChecker(context);
        checker.execute(new netChecker.OnCheckingDone() {
            @Override
            public void onDone(String result) {
                if (result.equals("1")) {
                    final int localRev = dp.getLocalRev();
                    dp.requestData("getrev", new DataProvider.OnDataReceived() {
                        @Override
                        public void onReceive(String result) {
                            try {
                                Log.v("TAG", result);
                                newestRev = Integer.parseInt(result);
                            } catch (Exception e) {
                                e.printStackTrace();
                                manager.cancel(111);
                                //Checking aborted
                                return;
                            }
                            if (localRev < newestRev) {
                                //Update found
                                builder.setContentText("Đã tìm thấy cập nhật\nĐang tải về");
                                manager.notify(111, builder.build());
                                new DataProvider(ct).requestData("getAll", new DataProvider.OnDataReceived() {
                                    @Override
                                    public void onReceive(String result) {
                                        if (result.equals("")) {
                                            //Update found but failed to get data
                                            manager.cancel(111);
                                            return;
                                        }
                                        dp.updateData(String.valueOf(newestRev), result);
                                        //Update successfully
                                        builder.setContentText("Cập nhật dữ liệu thành công\nNhấn vào đây đẻ khởi động lại App");
                                        Intent t = new Intent(ct, MainActivity.class);
                                        PendingIntent p = PendingIntent.getActivity(ct, 1, t, 0);
                                        builder.setContentIntent(p)
                                                .setAutoCancel(true)
                                                .setOngoing(false)
                                                .setPriority(NotificationCompat.PRIORITY_HIGH);
                                        manager.notify(111, builder.build());
                                    }
                                });
                            } else {
                                //No update
                                manager.cancel(111);
                            }
                        }
                    });
                } else manager.cancel(111);
            }
        });
    }

    boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        return (cm.getActiveNetworkInfo() != null)&&(cm.getActiveNetworkInfo().isConnected());
    }
}
