package hmdq.js.codeproject.dekirunihongo.Lesson;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
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
import java.util.TreeMap;

import hmdq.js.codeproject.dekirunihongo.R;
import hmdq.js.codeproject.dekirunihongo.SearchResult;

public class LearnVocabulary extends AppCompatActivity implements TextToSpeech.OnInitListener, SearchView.OnQueryTextListener {
    private SearchView searchView;
    private TextView tvNextSpell;
    TabHost mTabHostLearn;
    Button btnBack, btnNext;
    Button btnStartOverLearn, btnNextLearn, btnStartOverSpell;
    TextView tVToolbarLearn;
    TextView tvFlash, tvPhan;
    TextView tvNumRemain, tvNumInCor, tvNumCor, tvCheckInOrCorLearn, tvNghiaLearn, tvWriteTuLearn, tvNumFinish, tvCheckInOrCorSpell, tvNghiaSpell, tvWriteTuSpell;
    EditText edtTuLearn, edtTuSpell;
    ImageButton iBtnSpeakFlash, iBtnSpeakSpell;
    boolean checkFlash, checkLearn;
    private TextToSpeech myTTS;
    //status check code
    private int MY_DATA_CHECK_CODE = 0;
    private String[] sTu, sTutmp;
    private String[] sNghia, sNghiatmp;
    private int indexFCards, indexRemain, indexLearn, indexInCor, indexCor, indexFinish, indexSpell, sTuLength;
    private String lesson = null;
    private RandomInt rdLearn, rdSpell;
    private String sAnswerLearn, sAnswerSpell;
    private boolean checkEditLearn, checkEditSpell;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lean_vocabulary);
        // không cho màn hình xoay ngang
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
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
            tVToolbarLearn.setText(getString(R.string.lesson) + " " + lesson);
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
        btnStartOverLearn = (Button) findViewById(R.id.btnStartOverLearn);
        btnNextLearn = (Button) findViewById(R.id.btnNextLearn);
        tvNumRemain = (TextView) findViewById(R.id.tvNumRemain);
        tvNumInCor = (TextView) findViewById(R.id.tvNumIncor);
        tvNumCor = (TextView) findViewById(R.id.tvNumCor);
        tvCheckInOrCorLearn = (TextView) findViewById(R.id.tvCheckInOrCorLearn);
        tvNghiaLearn = (TextView) findViewById(R.id.tvNghiaLearn);
        tvWriteTuLearn = (TextView) findViewById(R.id.tvWriteTuLearn);
        edtTuLearn = (EditText) findViewById(R.id.edtTuLearn);
        // setFromWidget layout Speller
        btnStartOverSpell = (Button) findViewById(R.id.btnStartOverSpell);
        tvNumFinish = (TextView) findViewById(R.id.tvNumFinish);
        tvCheckInOrCorSpell = (TextView) findViewById(R.id.tvCheckInOrCorSpell);
        tvNghiaSpell = (TextView) findViewById(R.id.tvNghiaSpell);
        tvWriteTuSpell = (TextView) findViewById(R.id.tvWriteTuSpell);
        edtTuSpell = (EditText) findViewById(R.id.edtTuSpell);
        iBtnSpeakSpell = (ImageButton) findViewById(R.id.iBtnSpeakSpell);
        tvNextSpell = (TextView) findViewById(R.id.tvNextSpell);

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
        final TreeMap<String, String> sSai = new TreeMap<>();
        resetLearn(sTuLength, sTu, sNghia);
        edtTuLearn.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    if (checkEditLearn) {
                        sAnswerLearn = edtTuLearn.getText().toString();
                        checkEditLearn = false;
                        STrim sLearnTrim = new STrim(sAnswerLearn);
                        sAnswerLearn = sLearnTrim.trim();
                        if (sAnswerLearn.equals(sTutmp[indexLearn])) {
                            indexCor++;
                            setTextAnswerLearn(getString(R.string.CORRECT), "");
                            tvNumCor.setText(indexCor + "");
                        } else {
                            indexInCor++;
                            tvNumInCor.setText(indexInCor + "");
                            setTextAnswerLearn(getString(R.string.INCORRECT), getString(R.string.correct) + ": " + sTutmp[indexLearn]);
                            sSai.put(sTutmp[indexLearn], sNghiatmp[indexLearn]);
                        }
                    } else edtTuLearn.setText(sAnswerLearn);
                    return true;
                }
                return false;
            }
        });
        btnStartOverLearn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetLearn(sTuLength, sTu, sNghia);
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
                    if (sSai.size() != 0) {
                        sTutmp = new String[sSai.size()];
                        sNghiatmp = new String[sSai.size()];
                        sSai.keySet().toArray(sTutmp);
                        sSai.values().toArray(sNghiatmp);
                        resetLearn(sSai.size(), sTutmp, sNghiatmp);
                    } else
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
        resetSpell();
        tvNextSpell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                indexSpell = rdSpell.Random();
                if (indexSpell > -1) {
                    speakWords(sTu[indexSpell]);
                    nextSpell();
                } else {
                    Toast.makeText(LearnVocabulary.this, "Bạn đã hoàn thành", Toast.LENGTH_SHORT).show();
                }
            }
        });
        edtTuSpell.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    if (checkEditSpell) {
                        checkEditSpell = false;
                        if (checkLearn) {
                            sAnswerSpell = edtTuSpell.getText().toString();
                            STrim sSpellTrim = new STrim(sAnswerSpell);
                            sAnswerSpell = sSpellTrim.trim();
                            tvNumFinish.setText(indexFinish + "");
                            if (sAnswerSpell.equals(sTu[indexSpell])) {
                                tvCheckInOrCorSpell.setTextColor(Color.parseColor("#00FB36"));
                                setTextAnswerSpell("CORRECT", "");
                                indexFinish++;
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    public void run() {
                                        indexSpell = rdSpell.Random();
                                        if (indexSpell > -1) {
                                            speakWords(sTu[indexSpell]);
                                            nextSpell();
                                        } else {
                                            Toast.makeText(LearnVocabulary.this, "Bạn đã hoàn thành", Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                }, 2000);
                            } else {
                                tvNextSpell.setText("Tiếp tục");
                                rdSpell.remove(indexFinish % sTuLength);
                                tvCheckInOrCorSpell.setTextColor(Color.parseColor("#FF0000"));
                                setTextAnswerSpell("INCORRECT", "Correct: " + sTu[indexSpell]);
                            }
                            checkLearn = false;
                        }
                    } else edtTuSpell.setText(sAnswerSpell);
                }
                return false;
            }
        });
        iBtnSpeakSpell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speakWords(sTu[indexSpell]);
            }
        });
        btnStartOverSpell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetSpell();
                speakWords(sTu[indexSpell]);
            }
        });

    }

    private void setTextAnswerSpell(String inCorOrCor, String correct) {
        tvNghiaSpell.setText(sNghia[indexSpell]);
        tvCheckInOrCorSpell.setText(inCorOrCor);
        tvWriteTuSpell.setText(correct);
    }

    private void resetSpell() {
        rdSpell = new RandomInt(sTuLength);
        indexFinish = 0;
        tvNumFinish.setText(indexFinish + "");
        indexSpell = rdSpell.Random();
        nextSpell();
    }

    private void nextSpell() {
        checkEditSpell = true;
        tvNextSpell.setText("");
        tvCheckInOrCorSpell.setText("");
        tvNghiaSpell.setText("");
        edtTuSpell.setText("");
        checkLearn = true;
        tvWriteTuSpell.setText("");

    }

    //TABLEARN
    // setText khi chọn buttun Answer
    private void setTextAnswerLearn(String inCorOrCor, String correct) {
        tvCheckInOrCorLearn.setText(inCorOrCor);
        tvWriteTuLearn.setText(correct);
    }

    // reset lại các biến trong tablearn;
    private void resetLearn(int sTuLength, String[] sTu, String[] sNghia) {
        sTutmp = new String[sTuLength];
        sNghiatmp = new String[sTuLength];
        sTutmp = sTu;
        sNghia = sNghia;
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
        checkEditLearn = true;
        indexLearn = rdLearn.Random();
        tvNumRemain.setText(indexRemain + "");
        tvNghiaLearn.setText(sNghia[indexLearn]);
        edtTuLearn.setText("");
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
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem itemSearch = menu.findItem(R.id.mnSearch);
        searchView = (SearchView) itemSearch.getActionView();
        //set OnQueryTextListener cho search view để thực hiện search theo text
        searchView.setOnQueryTextListener(this);
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

    @Override
    public boolean onQueryTextSubmit(String query) {
        Intent intentSearch = new Intent(LearnVocabulary.this, SearchResult.class);
        intentSearch.putExtra("sSearch", query);
        startActivity(intentSearch);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

}
