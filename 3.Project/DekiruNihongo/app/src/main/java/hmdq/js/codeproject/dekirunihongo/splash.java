package hmdq.js.codeproject.dekirunihongo;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;

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
    }

    @Override
    protected void onStart() {
        super.onStart();
        dp = new DataProvider(this);
        beginLoading();
    }

    void beginLoading() {
        super.onStart();
        netChecker checker = new netChecker(this);
        checker.execute(new netChecker.OnCheckingDone() {
            @Override
            public void onDone(String result) {
                if (result.equals("1")) {
                    final int localRev = dp.getLocalRev();
                    dp.requestData("getrev", new DataProvider.OnDataReceived() {
                        @Override
                        public void onReceive(String result) {
                            Log.v("JSDK",result);
                            try {
                                newestRev = Integer.parseInt(result);
                            } catch (Exception e) {
                                e.printStackTrace();
                                Log.v("JSDK","Failed to update database");
                                enterMain();
                                return;
                            }
                            if (localRev != newestRev) {
                                if (localRev == 0) txt.setText("Lần khởi động đầu tiên sẽ khá lâu\nXin vui lòng đợi trong giây lát");
                                else txt.setText("Phát hiện cập nhật mới");
                                Log.v("JSDK","New update found");
                                new DataProvider(getApplicationContext()).requestData("getAll", new DataProvider.OnDataReceived() {
                                    @Override
                                    public void onReceive(String result) {
                                        if (result.equals("")) {
                                            Log.v("JSDK", "Failed to update");
                                            enterMain();
                                            return;
                                        }
                                        dp.updateData(String.valueOf(newestRev), result);
                                        Log.v("JSDK","Update succeeded");
                                        enterMain();
                                    }
                                });
                            } else {
                                Log.v("JSDK","No new update");
                                enterMain();
                            }
                        }
                    });
                }
                else {
                    txt.setText("Không thể kết nối mạng");
                    enterMain();
                }
            }
        });
    }

    void enterMain() {
        if (dp.getLocalRev() == 0) {
            AlertDialog.Builder ab = new AlertDialog.Builder(this);
            ab.setTitle("Vui lòng kiểm tra lại kết nối mạng và thử lại")
                    .setMessage("App không thể lấy được dữ liệu trong lần khởi chạy đầu tiên\n" +
                            "Vì vậy hiện tại ứng dụng không có dữ liệu để chạy")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            System.exit(0);
                        }
                    });
            ab.create().show();
        } else {
            Handler h = new Handler();
            h.postDelayed(new Runnable() {
                @Override
                public void run() {;
                    startActivity(new Intent(splash.this, MainActivity.class));
                    finish();
                }
            }, 3000);
        }
    }
}
