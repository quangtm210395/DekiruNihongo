package hmdq.js.codeproject.dekirunihongo;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import hmdq.js.codeproject.dekirunihongo.About.About;
import hmdq.js.codeproject.dekirunihongo.Lesson.ListLesson;

public class MainActivity extends AppCompatActivity{
    private static final int TIME_DELAY = 2000;
    private static long back_pressed;
    private TextToSpeech myTTS;
    //status check code
    private int MY_DATA_CHECK_CODE = 0;
    ImageButton iBtnBook1, iBtnBook2, iBtnBook3,iBtnAbout;
    TextView tVBook1,tVBook2,tVBook3,tVAbout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // không cho màn hình xoay ngang
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getFromWidget();
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

    private void getFromWidget() {
        iBtnBook1 = (ImageButton) findViewById(R.id.iBtnBook1);
        iBtnBook2 = (ImageButton) findViewById(R.id.iBtnBook2);
        iBtnBook3 = (ImageButton) findViewById(R.id.iBtnBook3);
        iBtnAbout = (ImageButton) findViewById(R.id.iBtnAbout);
        tVBook1 = (TextView) findViewById(R.id.tVBook1);
        tVBook2 = (TextView) findViewById(R.id.tVBook2);
        tVBook3 = (TextView) findViewById(R.id.tVBook3);
        tVAbout = (TextView) findViewById(R.id.tVAbout);
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
