package hmdq.js.codeproject.dekirunihongo;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

public class splash extends AppCompatActivity {
    DataProvider dp;
    TextView txt;
    int newestRev;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        txt = (TextView) findViewById(R.id.splashText);
        dp = new DataProvider(getApplicationContext());
        if (isOnline()) {
            final int localRev = dp.getLocalRev();
            dp.requestData("getrev", new DataProvider.OnDataReceived() {
                @Override
                public void onReceive(String result) {
                    if (result.equals("")) {
                        txt.setText("Chekcing aborted");
                        enterMain();
                        return;
                    } else newestRev = Integer.parseInt(result);
                    if (localRev < newestRev) new DataProvider(getApplicationContext()).requestData("getAll", new DataProvider.OnDataReceived() {
                        @Override
                        public void onReceive(String result) {
                            if (result.equals("")) {
                                txt.setText("Failed to get data\nNo change made");
                                enterMain();
                                return;
                            }
                            dp.updateData(String.valueOf(newestRev), result);
                            txt.setText("Updated revision " + String.valueOf(newestRev));
                            enterMain();
                        }
                    }); else {
                        txt.setText("No update");
                        enterMain();
                    }
                }
            });
        } else {
            txt.setText("Checking aborted");
            enterMain();
        }
    }

    void enterMain() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        startActivity(new Intent(splash.this, MainActivity.class));
    }

    boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        return (cm.getActiveNetworkInfo() != null)&&(cm.getActiveNetworkInfo().isConnected());
    }
}
