package hmdq.js.codeproject.dekirunihongo;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IntegerRes;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;

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
                    try {
                        Log.v("TAG", result);
                        newestRev = Integer.parseInt(result);
                    } catch (Exception e) {
                        e.printStackTrace();
                        enterMain();
                        return;
                    }
                    if (localRev < newestRev) {
                        txt.setText("Update found");
                        new DataProvider(getApplicationContext()).requestData("getAll", new DataProvider.OnDataReceived() {
                            @Override
                            public void onReceive(String result) {
                                if (result.equals("")) {
                                    enterMain();
                                    return;
                                }
                                result = dp.rebuild(result);
                                dp.updateData(String.valueOf(newestRev), result);
                                txt.setText("Cập nhật dữ liệu thành công");
                                enterMain();
                            }
                        });
                    } else {
                        enterMain();
                    }
                }
            });
        } else {
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
        finish();
    }

    boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        return (cm.getActiveNetworkInfo() != null)&&(cm.getActiveNetworkInfo().isConnected());
    }
}
