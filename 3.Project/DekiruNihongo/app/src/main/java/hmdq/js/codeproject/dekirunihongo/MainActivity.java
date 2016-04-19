package hmdq.js.codeproject.dekirunihongo;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.Locale;

import hmdq.js.codeproject.dekirunihongo.Lesson.ListLesson;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener{
    private TextToSpeech myTTS;
    //status check code
    private int MY_DATA_CHECK_CODE = 0;
    String speech = "にほん";
    ImageButton iBtnBook1, iBtnBook2, iBtnBook3;
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
//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            public void run() {
//                    speakWords("こんにちは!　わたしは　できるにほんごです、いっしょに　べんきょうを　はじめましょう。");
//            }
//        }, 1000);
        iBtnBook1 = (ImageButton) findViewById(R.id.iBtnBook1);
        iBtnBook2 = (ImageButton) findViewById(R.id.iBtnBook2);
        iBtnBook3 = (ImageButton) findViewById(R.id.iBtnBook3);

        iBtnBook1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent mh2 = new Intent(MainActivity.this, ListLesson.class);
                mh2.putExtra("book", "1");
                startActivity(mh2);
            }
        });

        iBtnBook2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mh2 = new Intent(MainActivity.this, ListLesson.class);
                mh2.putExtra("book", "2");
                startActivity(mh2);
            }
        });

        iBtnBook3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mh2 = new Intent(MainActivity.this, ListLesson.class);
                mh2.putExtra("book","3");
                startActivity(mh2);

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

}
