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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        txt = (TextView) findViewById(R.id.splashText);
        if (!isOnline()) {
            txt.setText("Checking aborted\nNo intenet access");
            enterMain();
        }
        dp = new DataProvider(getApplicationContext());
        final int localRev = dp.getLocalRev();
        dp.requestData("getrev", new DataProvider.OnDataReceived() {
            @Override
            public void onReceive(String result) {
                final int newestRev = Integer.parseInt(result);
                if (localRev < newestRev) new DataProvider(getApplicationContext()).requestData("getAll", new DataProvider.OnDataReceived() {
                    @Override
                    public void onReceive(String result) {
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
    }

    void enterMain() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(splash.this, MainActivity.class));
                finish();
            }
        }, 2000);
    }

    boolean isOnline() {
        ConnectivityManager cmgr = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo info = cmgr.getActiveNetworkInfo();
        return ((info != null)&&(info.isConnectedOrConnecting()));
    }
}
