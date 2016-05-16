package hmdq.js.codeproject.dekirunihongo;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import hmdq.js.codeproject.dekirunihongo.AboutInfo.*;
import hmdq.js.codeproject.dekirunihongo.Lesson.ListLesson;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener{
    private static final int TIME_DELAY = 2000;
    private static long back_pressed;
    private Typeface tfLato;
    private Typeface tfKoz;
    private Button btnAbout;
    private TextView tvWelcome;
    private TextView tvWelcomeVN;
    private String[] sNameBook = {"初級", "初中級", "中級"};
    private String[] sNameBookVN = {"Sơ cấp","Sơ trung cấp", "Trung cấp"};
    private Integer[] imgIconid = {R.drawable.image_book_1,R.drawable.image_book_2,R.drawable.image_book_3};
    private ListView listViewBook;
    private TextToSpeech myTTS;
    //status check code
    private int MY_DATA_CHECK_CODE = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //check for TTS data TextToSpeech;
        Intent checkTTSIntent = new Intent();
        checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkTTSIntent, MY_DATA_CHECK_CODE);
        // không cho màn hình xoay ngang
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getFromWidget();
        tfLato = Typeface.createFromAsset(getAssets(),"fonts/lato.ttf");
        tfKoz = Typeface.createFromAsset(getAssets(),"fonts/sm.ttf");
        final Intent mh2 = new Intent(MainActivity.this, ListLesson.class);
        CustomListBookAdapter adapter = new CustomListBookAdapter(MainActivity.this, sNameBook,sNameBookVN, imgIconid);
        listViewBook.setAdapter(adapter);
        listViewBook.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) mh2.putExtra("book", "1");
                if (position == 1) mh2.putExtra("book", "2");
                if (position == 2) mh2.putExtra("book", "3");
                startActivity(mh2);
            }
        });
        btnAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, About.class));
            }
        });
        tvWelcome.setTypeface(tfKoz);
        tvWelcomeVN.setTypeface(tfLato);
        btnAbout.setTypeface(tfLato);
        listViewBook.setTextFilterEnabled(true);
        LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(this,R.anim.list_layout_controller_2);
        listViewBook.setLayoutAnimation(controller);
    }

    private void getFromWidget() {
        tvWelcome = (TextView) findViewById(R.id.tvWelcome);
        tvWelcomeVN = (TextView) findViewById(R.id.tvWelcomeVN);
        listViewBook = (ListView) findViewById(R.id.listViewBook);
        btnAbout = (Button) findViewById(R.id.btnAbout);
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
