package hmdq.js.codeproject.dekirunihongo;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import hmdq.js.codeproject.dekirunihongo.Lesson.ListLesson;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener{
    private static final int TIME_DELAY = 2000;
    private static long back_pressed;
    private TextToSpeech myTTS;
    //status check code
    private int MY_DATA_CHECK_CODE = 0;
    String speech = "にほん";
    ImageButton iBtnBook1, iBtnBook2, iBtnBook3,iBtnAbout;
    TextView tVBook1,tVBook2,tVBook3,tVAbout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // không cho màn hình xoay ngang
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //check for TTS data TextToSpeech;
        Intent checkTTSIntent = new Intent();
        checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkTTSIntent, MY_DATA_CHECK_CODE);
        // speak name project
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                    speakWords("こんにちは!　いっしょに　べんきょうしましょう。");
            }
        }, 1000);
        iBtnBook1 = (ImageButton) findViewById(R.id.iBtnBook1);
        iBtnBook2 = (ImageButton) findViewById(R.id.iBtnBook2);
        iBtnBook3 = (ImageButton) findViewById(R.id.iBtnBook3);
        iBtnAbout = (ImageButton) findViewById(R.id.iBtnAbout);
        tVBook1 = (TextView) findViewById(R.id.tVBook1);
        tVBook2 = (TextView) findViewById(R.id.tVBook2);
        tVBook3 = (TextView) findViewById(R.id.tVBook3);
        tVAbout = (TextView) findViewById(R.id.tVAbout);
        final Intent mh2 = new Intent(MainActivity.this, ListLesson.class);
        iBtnBook1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mh2.putExtra("book", "1");
                startActivity(mh2);
            }
        });

        iBtnBook2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mh2.putExtra("book", "2");
                startActivity(mh2);
            }
        });
        iBtnBook3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mh2.putExtra("book","3");
                startActivity(mh2);

            }
        });
        iBtnAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,About.class));
            }
        });
        tVBook1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mh2.putExtra("book", "1");
                startActivity(mh2);
            }
        });

        tVBook2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mh2.putExtra("book", "2");
                startActivity(mh2);
            }
        });

        tVBook3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mh2.putExtra("book","3");
                startActivity(mh2);

            }
        });
        tVAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,About.class));
            }
        });
    }
    // TEXT TO SPEECH
    //speak text
    private void speakWords(String speech) {

        //speak straight away
        myTTS.speak(speech, TextToSpeech.QUEUE_FLUSH, null);
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
    @Override
    public void onBackPressed() {
        if (back_pressed + TIME_DELAY > System.currentTimeMillis()) {
            super.onBackPressed();
        } else {
            Toast.makeText(getBaseContext(), "Nhấn back thêm 1 lần nữa để thoát",
                    Toast.LENGTH_SHORT).show();
        }
        back_pressed = System.currentTimeMillis();
    }

}
