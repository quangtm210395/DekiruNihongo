package hmdq.js.codeproject.dekirunihongo.Lesson;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;
import java.util.Random;
import java.util.Vector;

import hmdq.js.codeproject.dekirunihongo.R;

public class LearnVocabulary extends AppCompatActivity implements  TextToSpeech.OnInitListener {
    private TextToSpeech myTTS;
    //status check code
    private int MY_DATA_CHECK_CODE = 0;
    TabHost mTabH;
    Button btnBack, btnNext;
    Button btnAnswer, btnStartOver, btnNextLearn;
    TextView tVToolbarLearn;
    TextView tvFlash, tvPhan;
    TextView tvNumRemain, tvNumInCor, tvNumCor, tvCheckInOrCor, tvLearnNghia, tvWriteTu;
    EditText edtLearnTu;
    ImageButton iBtnSpeak;
    private String[] sTu = new String[20];
    private String[] sNghia = new String[20];
    boolean checkFlash;
    private int indexFCards, indexRemain, indexLearn, indexInCor, indexCor;
    private String lesson = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lean_vocabulary);
        setFromWidget();
        // toolbar
        setToolbar();
        // getdata
        getData();
        // tabs
        tabFlashCards();
        tabLearn();
        tabSpeller();
        //check for TTS data TextToSpeech;
        Intent checkTTSIntent = new Intent();
        checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkTTSIntent, MY_DATA_CHECK_CODE);
    }

    private void getData() {
        Bundle bd = getIntent().getExtras();
        if (bd != null) {
            lesson = bd.getString("lesson");
            sTu = bd.getStringArray("Tu");
            sNghia = bd.getStringArray("Nghia");
        }
        // set tên bài lên toolbar;
        if (lesson != null) {
            tVToolbarLearn.setText(lesson);
        }
    }

    private void setFromWidget() {
        mTabH = (TabHost) findViewById(R.id.tabHostLearn);
        mTabH.setup();
        // setFromWidget layout flashcards
        btnBack = (Button) findViewById(R.id.btnBack);
        btnNext = (Button) findViewById(R.id.btnNext);
        tvFlash = (TextView) findViewById(R.id.tvFlash);
        tvPhan = (TextView) findViewById(R.id.tvPhan);
        tVToolbarLearn = (TextView) findViewById(R.id.tVToolbarLearn);
        iBtnSpeak = (ImageButton) findViewById(R.id.iBtnSpeak);
        // setFromWidget layout Learn
        btnAnswer = (Button) findViewById(R.id.btnAnswer);
        btnStartOver = (Button) findViewById(R.id.btnStartOver);
        btnNextLearn = (Button) findViewById(R.id.btnNextLearn);
        tvNumRemain = (TextView) findViewById(R.id.tvNumRemain);
        tvNumInCor = (TextView) findViewById(R.id.tvNumIncor);
        tvNumCor = (TextView) findViewById(R.id.tvNumCor);
        tvCheckInOrCor = (TextView) findViewById(R.id.tvCheckInOrCor);
        tvLearnNghia = (TextView) findViewById(R.id.tvLearnNghia);
        tvWriteTu = (TextView) findViewById(R.id.tvWriteTu);
        edtLearnTu = (EditText) findViewById(R.id.edtLearnTu);
        // setFromWidget layout Speller

    }

    private void tabFlashCards() {
        //create tabFlashCards
        TabHost.TabSpec tab;
        tab = mTabH.newTabSpec("flashcards");
        tab.setContent(R.id.tabFlashCards);
        tab.setIndicator("Flashcards");
        mTabH.addTab(tab);
        // TODO
        indexFCards = 0;
        tvFlash.setText(sNghia[indexFCards]);
        tvPhan.setText((indexFCards + 1) + " / " + sNghia.length);
        checkFlash = false;
        tvFlash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkFlash) {
                    tvFlash.setText(sNghia[indexFCards]);
                    checkFlash = false;
                } else {
                    tvFlash.setText(sTu[indexFCards]);
                    speakWords(sTu[indexFCards]);
                    checkFlash = true;
                }
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (indexFCards < sNghia.length - 1) {
                    indexFCards = indexFCards + 1;
                    tvFlash.setText(sNghia[indexFCards]);
                    tvPhan.setText((indexFCards + 1) + " / " + sNghia.length);
                    checkFlash = false;
                }
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (indexFCards > 0) {
                    indexFCards = indexFCards - 1;
                    tvFlash.setText(sNghia[indexFCards]);
                    tvPhan.setText((indexFCards + 1) + " / " + sNghia.length);
                    checkFlash = false;
                }
            }
        });
        iBtnSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkFlash) {
                    speakWords(sTu[indexFCards]);
                }
            }
        });
    }

    private void tabLearn() {
        //create tabLearn
        TabHost.TabSpec tab;
        tab = mTabH.newTabSpec("learn");
        tab.setContent(R.id.tabLearn);
        tab.setIndicator("Learn");
        mTabH.addTab(tab);
        // TODO
        resetLearn();
        btnAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sAnswer = edtLearnTu.getText().toString();
                sAnswer = trim(sAnswer);
                if (sAnswer.equals(sTu[indexLearn])) {
                    indexCor++;
                    setTextAnswer("CORRECT");
                } else {
                    indexInCor++;
                    setTextAnswer("INCORRECT");
                }
            }
        });
        btnStartOver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetLearn();
            }
        });
        btnNextLearn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvNumRemain.setText(indexRemain + "");
                if (indexRemain > 0) {
                    if (!tvCheckInOrCor.getText().equals("")) {
//                        randomLearn();
                    } else {
                        Toast.makeText(LearnVocabulary.this, "Bạn hãy viết câu trả lời trước khi qua câu tiếp theo", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LearnVocabulary.this, "Bạn đã hoàn thành", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    // setText khi chọn buttun Answer
    private void setTextAnswer(String inCorOrCor) {
        tvNumInCor.setText(indexInCor + "");
        tvCheckInOrCor.setText(inCorOrCor);
        tvWriteTu.setText("Correct: " + sTu[indexLearn]);
    }

    private void resetLearn() {
        indexRemain = sNghia.length;
        indexCor = 0;
        indexInCor = 0;
        tvNumCor.setText("" + indexCor);
        tvNumInCor.setText("" + indexInCor);
//        randomLearn();

    }

    //tạo ra
//    private void randomLearn() {
//        for (; indexRemain > 0; ) {
//            indexLearn = rd.nextInt(sNghia.length);
//            if (checkRadomLearn[indexLearn]) {
//                checkRadomLearn[indexLearn] = false;
//                tvNumRemain.setText(indexRemain + "");
//                tvLearnNghia.setText(sNghia[indexLearn]);
//                edtLearnTu.setText("");
//                indexRemain--;
//                tvCheckInOrCor.setText("");
//                tvWriteTu.setText("");
//                break;
//            }
//        }
//    }

    // xóa kí tự trắng đầu, cuối của từ
    private String trim(String s) {
        int i;
        for (i = 0; i < s.length(); i++) {
            if (s.charAt(i) != '　') break;
        }
        s = s.substring(i);
        for (i = s.length() - 1; i > 0; i--) {
            if (s.charAt(i) != '　') break;
        }
        s = s.substring(0, i + 1);
        return s;
    }

    // thiết lập chức năng speller
    private void tabSpeller() {
        //create tabSpeller
        TabHost.TabSpec tab;
        tab = mTabH.newTabSpec("speller");
        tab.setContent(R.id.tabSpeller);
        tab.setIndicator("Speller");
        mTabH.addTab(tab);
        // TODO

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

    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarLesson);
        setSupportActionBar(toolbar);
        //Không hiện tiêu đề
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //Hiện nút back
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.mnSearch:
                Toast.makeText(this, "Search", Toast.LENGTH_SHORT).show();
                break;
            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

}
