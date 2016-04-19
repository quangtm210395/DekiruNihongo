package hmdq.js.codeproject.dekirunihongo.Lesson;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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

import hmdq.js.codeproject.dekirunihongo.R;

public class LearnVocabulary extends AppCompatActivity implements TextToSpeech.OnInitListener {
    TabHost mTabHostLearn;
    Button btnBack, btnNext;
    Button btnAnswerLearn, btnStartOverLearn, btnNextLearn, btnAnswerSpel, btnStartOverSpel;
    TextView tVToolbarLearn;
    TextView tvFlash, tvPhan;
    TextView tvNumRemain, tvNumInCor, tvNumCor, tvCheckInOrCorLearn, tvLearnNghia, tvWriteTuLearn, tvNumFinish, tvCheckInOrCorSpel, tvSpelNghia, tvWriteTuSpel;
    EditText edtLearnTu, edtSpelTu;
    ImageButton iBtnSpeakFlash, iBtnSpeakSpel;
    boolean checkFlash, checkLearn;
    private TextToSpeech myTTS;
    //status check code
    private int MY_DATA_CHECK_CODE = 0;
    private String[] sTu;
    private String[] sNghia;
    private int indexFCards, indexRemain, indexLearn, indexInCor, indexCor, indexFinish, indexSpel,sTuLength;
    private String lesson = null;
    private RandomInt rdLearn, rdSpel;

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
            sTuLength = bd.getInt("sTuLength");
            sTu = new String[sTuLength];
            sNghia = new String[sTuLength];
            lesson = bd.getString("lesson");
            sTu = bd.getStringArray("Tu");
            sNghia = bd.getStringArray("Nghia");
        }
        // set tên bài lên toolbar;
        if (lesson != null) {
            tVToolbarLearn.setText("Lesson " + lesson);
        }
    }

    private void setFromWidget() {
        mTabHostLearn = (TabHost) findViewById(R.id.tabHostLearn);
        mTabHostLearn.setup();
        // setFromWidget layout flashcards
        btnBack = (Button) findViewById(R.id.btnBack);
        btnNext = (Button) findViewById(R.id.btnNext);
        tvFlash = (TextView) findViewById(R.id.tvFlash);
        tvPhan = (TextView) findViewById(R.id.tvPhan);
        tVToolbarLearn = (TextView) findViewById(R.id.tVToolbarLearn);
        iBtnSpeakFlash = (ImageButton) findViewById(R.id.iBtnSpeakFlash);
        // setFromWidget layout Learn
        btnAnswerLearn = (Button) findViewById(R.id.btnAnswerLearn);
        btnStartOverLearn = (Button) findViewById(R.id.btnStartOverLearn);
        btnNextLearn = (Button) findViewById(R.id.btnNextLearn);
        tvNumRemain = (TextView) findViewById(R.id.tvNumRemain);
        tvNumInCor = (TextView) findViewById(R.id.tvNumIncor);
        tvNumCor = (TextView) findViewById(R.id.tvNumCor);
        tvCheckInOrCorLearn = (TextView) findViewById(R.id.tvCheckInOrCorLearn);
        tvLearnNghia = (TextView) findViewById(R.id.tvLearnNghia);
        tvWriteTuLearn = (TextView) findViewById(R.id.tvWriteTuLearn);
        edtLearnTu = (EditText) findViewById(R.id.edtLearnTu);
        // setFromWidget layout Speller
        btnAnswerSpel = (Button) findViewById(R.id.btnAnswerSpel);
        btnStartOverSpel = (Button) findViewById(R.id.btnStartOverSpel);
        tvNumFinish = (TextView) findViewById(R.id.tvNumFinish);
        tvCheckInOrCorSpel = (TextView) findViewById(R.id.tvCheckInOrCorSpel);
        tvSpelNghia = (TextView) findViewById(R.id.tvSpelNghia);
        tvWriteTuSpel = (TextView) findViewById(R.id.tvWriteTuSpel);
        edtSpelTu = (EditText) findViewById(R.id.edtSpelTu);
        iBtnSpeakSpel = (ImageButton) findViewById(R.id.iBtnSpeakSpel);

    }

    private void tabFlashCards() {
        //create tabFlashCards
        TabHost.TabSpec tab;
        tab = mTabHostLearn.newTabSpec("flashcards");
        tab.setContent(R.id.tabFlashCards);
        tab.setIndicator("Flashcards");
        mTabHostLearn.addTab(tab);
        // TODO
        indexFCards = 0;
        tvFlash.setText(sNghia[indexFCards]);
        tvPhan.setText((indexFCards + 1) + " / " + sTuLength);
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
                if (indexFCards < sTuLength - 1) {
                    indexFCards = indexFCards + 1;
                    tvFlash.setText(sNghia[indexFCards]);
                    tvPhan.setText((indexFCards + 1) + " / " + sTuLength);
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
                    tvPhan.setText((indexFCards + 1) + " / " + sTuLength);
                    checkFlash = false;
                }
            }
        });
        iBtnSpeakFlash.setOnClickListener(new View.OnClickListener() {
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
        tab = mTabHostLearn.newTabSpec("learn");
        tab.setContent(R.id.tabLearn);
        tab.setIndicator("Learn");
        mTabHostLearn.addTab(tab);
        // TODO
        resetLearn();
        btnAnswerLearn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sAnswer = edtLearnTu.getText().toString();
                STrim sLearnTrim = new STrim(sAnswer);
                sAnswer = sLearnTrim.trim();
                if (sAnswer.equals(sTu[indexLearn])) {
                    indexCor++;
                    setTextAnswerLearn("CORRECT", "");
                } else {
                    indexInCor++;
                    setTextAnswerLearn("INCORRECT", "Correct: " + sTu[indexLearn]);
                }
            }
        });

        btnStartOverLearn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetLearn();
            }
        });

        btnNextLearn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (indexRemain > 0) {
                    if (!tvCheckInOrCorLearn.getText().equals("")) {
                        setTextLearn();
                    } else {
                        Toast.makeText(LearnVocabulary.this, "Bạn hãy viết câu trả lời trước khi qua câu tiếp theo", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    tvNumRemain.setText(indexRemain + "");
                    Toast.makeText(LearnVocabulary.this, "Bạn đã hoàn thành", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // thiết lập chức năng speller
    private void tabSpeller() {
        //create tabSpeller
        TabHost.TabSpec tab;
        tab = mTabHostLearn.newTabSpec("speller");
        tab.setContent(R.id.tabSpeller);
        tab.setIndicator("Speller");
        mTabHostLearn.addTab(tab);
        // TODO
        resetSpel();
        iBtnSpeakSpel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speakWords(sTu[indexSpel]);
            }
        });
        btnAnswerSpel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkLearn) {
                    String sAnswer = edtSpelTu.getText().toString();
                    STrim sSpelTrim = new STrim(sAnswer);
                    sAnswer = sSpelTrim.trim();
                    if (sAnswer.equals(sTu[indexSpel])) {
                        setTextAnswerSpel("CORRECT", "");
                        indexFinish++;
                        tvNumFinish.setText(indexFinish + "");
                    } else {
                        rdSpel.remove(indexFinish % sTuLength);
                        setTextAnswerSpel("INCORRECT", "Correct: " + sTu[indexLearn]);
                    }
                    checkLearn = false;
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            indexSpel = rdSpel.Random();
                            if (indexSpel > -1) {
                                speakWords(sTu[indexSpel]);
                                nextSpel();
                            } else {
                                Toast.makeText(LearnVocabulary.this, "Bạn đã hoàn thành", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }, 2000);
                }
            }
        });
        btnStartOverSpel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetSpel();
                speakWords(sTu[indexSpel]);
            }
        });

    }

    private void setTextAnswerSpel(String inCorOrCor, String correct) {
        tvSpelNghia.setText(sNghia[indexSpel]);
        tvCheckInOrCorSpel.setText(inCorOrCor);
        tvWriteTuSpel.setText(correct);
    }

    private void resetSpel() {
        rdSpel = new RandomInt(sTuLength);
        indexFinish = 0;
        tvNumFinish.setText(indexFinish + "");
        indexSpel = rdSpel.Random();
        nextSpel();
    }

    private void nextSpel() {
        tvCheckInOrCorSpel.setText("");
        tvSpelNghia.setText("");
        edtSpelTu.setText("");
        checkLearn = true;
        tvWriteTuSpel.setText("");

    }

    //TABLEARN
    // setText khi chọn buttun Answer
    private void setTextAnswerLearn(String inCorOrCor, String correct) {
        tvNumInCor.setText(indexInCor + "");
        tvCheckInOrCorLearn.setText(inCorOrCor);
        tvWriteTuLearn.setText(correct);
    }

    // reset lại các biến trong tablearn;
    private void resetLearn() {
        indexRemain = sTuLength;
        rdLearn = new RandomInt(indexRemain);
        indexCor = 0;
        indexInCor = 0;
        tvNumCor.setText("" + indexCor);
        tvNumInCor.setText("" + indexInCor);
        setTextLearn();

    }

    //tạo ra setText toàn bộ learn
    private void setTextLearn() {
        indexLearn = rdLearn.Random();
        tvNumRemain.setText(indexRemain + "");
        tvLearnNghia.setText(sNghia[indexLearn]);
        edtLearnTu.setText("");
        indexRemain--;
        tvCheckInOrCorLearn.setText("");
        tvWriteTuLearn.setText("");
    }
    //TABLEARN

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

    // TEXT TO SPEECH
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
