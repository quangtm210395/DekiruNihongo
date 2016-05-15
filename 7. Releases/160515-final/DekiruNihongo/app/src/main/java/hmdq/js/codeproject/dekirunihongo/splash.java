package hmdq.js.codeproject.dekirunihongo;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.rahatarmanahmed.cpv.CircularProgressView;

import java.util.Locale;

public class splash extends AppCompatActivity implements TextToSpeech.OnInitListener{
    DataProvider dp;
    TextView txt;
    ImageView bg;
    RelativeLayout lg;
    CircularProgressView cp;
    int newestRev;
    private TextToSpeech myTTS;
    //status check code
    private int MY_DATA_CHECK_CODE = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        //check for TTS data TextToSpeech;
        Intent checkTTSIntent = new Intent();
        checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkTTSIntent, MY_DATA_CHECK_CODE);
        // không cho màn hình xoay ngang
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        txt = (TextView) findViewById(R.id.splashText);
        bg = (ImageView) findViewById(R.id.bg_logo);
        lg = (RelativeLayout) findViewById(R.id.logo);
        cp = (CircularProgressView) findViewById(R.id.cp);
        bg.setAlpha(0f);
    }

    @Override
    protected void onStart() {
        super.onStart();
        lg.animate().setDuration(1500).translationY(-100);
        bg.animate().setDuration(3000).alpha(1.0f);
        txt.setText("Đang tải...");
        dp = new DataProvider(this);
        beginLoading();
    }

    void beginLoading() {
        super.onStart();
        cp.startAnimation();
        cp.setColor(Color.parseColor("#289335"));
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
                                cp.setColor(Color.parseColor("#289335"));
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
            txt.setText("Tải dữ liệu xong");
            cp.setIndeterminate(false);
            cp.setColor(Color.parseColor("#2bae2a"));
            cp.setProgress(100);
            Handler h = new Handler();
            h.postDelayed(new Runnable() {
                @Override
                public void run() {
                    speakWords(getString(R.string.welcome));
                    startActivity(new Intent(splash.this, MainActivity.class));
                    finish();
                }
            }, 2000);
        }
    }
    // TEXT TO SPEECH
    //speak text
    private void speakWords(String speech) {

        //speak straight away
        if (myTTS == null){
            myTTS = new TextToSpeech(this, this);
        }
        if (myTTS != null){
            myTTS.speak(speech, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    //kiểm tra kết quả của Text To Speech

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == MY_DATA_CHECK_CODE) {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                //the user has the necessary data - create the TTS
                myTTS = new TextToSpeech(this, this);
            } else {
                //no data - install it now
                Intent installTTSIntent = new Intent();
                installTTSIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installTTSIntent);
            }
        }
    }

    //thiết lập Text To Speech
    public void onInit(int initStatus) {

        //check for successful instantiation
        if (initStatus == TextToSpeech.SUCCESS) {
            if (myTTS.isLanguageAvailable(Locale.JAPANESE) == TextToSpeech.LANG_AVAILABLE)
                myTTS.setLanguage(Locale.JAPANESE);
        } else if (initStatus == TextToSpeech.ERROR) {
            Toast.makeText(this, "Sorry! Text To Speech failed...", Toast.LENGTH_LONG).show();
        }
    }
}
