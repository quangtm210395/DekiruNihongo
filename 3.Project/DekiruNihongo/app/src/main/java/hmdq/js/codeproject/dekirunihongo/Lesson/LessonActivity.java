package hmdq.js.codeproject.dekirunihongo.Lesson;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;
import java.util.TreeMap;

import hmdq.js.codeproject.dekirunihongo.CommonData;
import hmdq.js.codeproject.dekirunihongo.DataProvider;
import hmdq.js.codeproject.dekirunihongo.Grammar.GrammarActivity;
import hmdq.js.codeproject.dekirunihongo.Quiz.ArrayQuiz;
import hmdq.js.codeproject.dekirunihongo.R;
import hmdq.js.codeproject.dekirunihongo.SearchResult;
import hmdq.js.codeproject.dekirunihongo.Vocabulary.ArrayAdapterVocabulary;
import hmdq.js.codeproject.dekirunihongo.Vocabulary.Employee;

public class LessonActivity extends AppCompatActivity implements TextToSpeech.OnInitListener, SearchView.OnQueryTextListener {

    private SearchView searchView;
    TabHost mTabHost;
    ListView listViewVocabulary;
    TextView tVToolbarLesson;
    TextView tvQuestion;
    TextView tvNumIncorTest, tvNumCorTest, tvNumRemainTest;
    RadioButton rBAnswer1, rBAnswer2, rBAnswer3, rBAnswer4;
    RadioGroup rGGram;
    Button btnLearn;
    Button btnStartOverQuiz;
    Button btnNextQuiz;
    DataProvider dp = null;
    TreeMap<String, String> mapVocab;
    TreeMap<String, String> mapGram;
    ListView listViewGrammar;
    // 2 mang sau đây là để test
//    private String[] sTu = {"わたし", "なまえ", "くに", "にほん", "かんこく", "ちゅうごく", "アメリカ", "イタリア", "オーストラリア", "ロシア", "タイ"};
//    private String[] sNghia = {"Tôi", "Tên", "Đất nước", "Nhật Bản", "Hàn Quốc", "Trung Quốc", "Mỹ", "Ý", "Úc", "Nga", "Thái Lan"};
    private String[] sTu;
    private String[] sNghia;
    private String[] sNameGram;
    // = {"Ngữ pháp 1", "Ngữ pháp 2", "Ngữ pháp 3", "Ngữ pháp 4", "Ngữ pháp 5", "Ngữ pháp 6", "Ngữ pháp 7", "Ngữ pháp 8", "Ngữ pháp 9", "Ngữ pháp 10"};
//    ;
    private String[] sGram;
    //    = {"Giải nghĩa 1", "Giải nghĩa 2", "Giải nghĩa 3", "Giải nghĩa 4", "Giải nghĩa 5", "Giải nghĩa 6", "Giải nghĩa 7", "Giải nghĩa 8", "Giải nghĩa 9", "Giải nghĩa 10"};
//    ;
    private String[] sQuestion = {"cau hoi thu nhat", "cau hoi thu hai"};
    private TextToSpeech myTTS;
    //status check code
    private int MY_DATA_CHECK_CODE = 0;
    private String lesson, book, lessonName;
    private String answerQuiz;
    private int indexMax;
    private int indexMaxQuiz;
    private int indexQuiz;
    private ArrayList<ArrayQuiz> arrayListQuiz = new ArrayList<>();
    private RandomInt rdQuiz;
    private int checkAnswer = 0;
    private int numCor, numIncor;
    private int idRBAnswer = -1;
    private int countQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson);
        mTabHost = (TabHost) findViewById(R.id.tabHostLesson);
        mTabHost.setup();
        // toolbar
        setToolbar();
        //lưu dữ liệu vị trí bài hiện tại
        CommonData cd = CommonData.getInstance();
        cd.noLesson = lesson;
        // tabs
        tabVocabulary();
        tabGrammar();
//        tabListen();
        tabQuiz();
        tabKanji();
        //check for TTS data TextToSpeech;
        Intent checkTTSIntent = new Intent();
        checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkTTSIntent, MY_DATA_CHECK_CODE);
    }

    private void tabVocabulary() {
        //create tabVocabulary
        TabHost.TabSpec tab;
        tab = mTabHost.newTabSpec("vocab");
        tab.setContent(R.id.tabVocabulary);
        tab.setIndicator("Từ vựng");
        mTabHost.addTab(tab);
        // TODO
        // listView for vocabulary
        setLisview();
        // lean vocabulary
        btnLearn = (Button) findViewById(R.id.btnLearn);
        btnLearn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intenLeanVocab = new Intent(LessonActivity.this, LearnVocabulary.class);
                intenLeanVocab.putExtra("sTuLength", indexMax);
                intenLeanVocab.putExtra("lesson", lesson);
                intenLeanVocab.putExtra("Tu", sTu);
                intenLeanVocab.putExtra("Nghia", sNghia);
                startActivity(intenLeanVocab);
            }
        });
    }

    private void tabGrammar() {
        //create tabGrammar
        TabHost.TabSpec tab;
        tab = mTabHost.newTabSpec("gram");
        tab.setContent(R.id.tabGrammar);
        tab.setIndicator("Ngữ pháp");
        mTabHost.addTab(tab);
        // TODO
        listViewGrammar = (ListView) findViewById(R.id.listViewGrammar);
        int indexListGram = 10;
        Bundle bd = getIntent().getExtras();
        if (bd != null) {
            lesson = bd.getString("lesson");
            book = bd.getString("book");
        }


        dp = new DataProvider(this);
        if (dp != null) {
            mapGram = dp.getData(book, "gra", lesson);
            indexListGram = mapGram.size();
            sNameGram = new String[indexListGram];
            sGram = new String[indexListGram];
            mapGram.keySet().toArray(sNameGram);
            mapGram.values().toArray(sGram);
        }

        ArrayList<String> arrayListGram = new ArrayList<>();
        for (int i = 0; i < indexListGram; i++) {
            arrayListGram.add(sNameGram[i]);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                LessonActivity.this,
                android.R.layout.simple_list_item_1,
                arrayListGram
        );
        listViewGrammar.setAdapter(adapter);
        listViewGrammar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intentGram = new Intent(LessonActivity.this, GrammarActivity.class);
                intentGram.putExtra("lesson", lesson);
                intentGram.putExtra("namegram", sNameGram[position]);
                intentGram.putExtra("gram", sGram[position]);
                startActivity(intentGram);
            }
        });
    }

//    private void tabListen() {
//        //create tabListen
//        TabHost.TabSpec tab;
//        tab = mTabHost.newTabSpec("listen");
//        tab.setContent(R.id.tabListen);
//        tab.setIndicator("Nghe");
//        mTabHost.addTab(tab);
//        // TODO
//
//    }

    private void tabQuiz() {
        //create tabQuiz
        TabHost.TabSpec tab;
        tab = mTabHost.newTabSpec("Quiz");
        tab.setContent(R.id.tabQuiz);
        tab.setIndicator("Quiz");
        mTabHost.addTab(tab);
        // TODO
        // add data
        tvNumRemainTest = (TextView) findViewById(R.id.tvNumRemainTest);
        tvNumCorTest = (TextView) findViewById(R.id.tvNumCorTest);
        tvNumIncorTest = (TextView) findViewById(R.id.tvNumIncorTest);
        tvQuestion = (TextView) findViewById(R.id.tvQuestion);
        rBAnswer1 = (RadioButton) findViewById(R.id.rBAnswer1);
        rBAnswer2 = (RadioButton) findViewById(R.id.rBAnswer2);
        rBAnswer3 = (RadioButton) findViewById(R.id.rBAnswer3);
        rBAnswer4 = (RadioButton) findViewById(R.id.rBAnswer4);
        btnStartOverQuiz = (Button) findViewById(R.id.btnStartOverQuiz);
        rGGram = (RadioGroup) findViewById(R.id.rGGram);
        btnNextQuiz = (Button) findViewById(R.id.btnNextQuiz);
        indexMaxQuiz = 2;
        for (int i = 0; i < indexMaxQuiz; i++) {
            ArrayQuiz arrayQuiz = new ArrayQuiz();
            arrayQuiz.setQuestion(sQuestion[i]);
            String[] answer = {"1", "2", "3", "4", (i + 1) + ""};
            arrayQuiz.setAnswer(answer);
            arrayListQuiz.add(arrayQuiz);
        }
        resetQuiz();
        setQuestion();
        btnStartOverQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (idRBAnswer != -1) {
                    RadioButton radio = (RadioButton) findViewById(idRBAnswer);
                    radio.setChecked(false);
                }
                checkAnswer = 0;
                resetQuiz();
                setQuestion();
            }
        });
        btnNextQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                indexQuiz = rdQuiz.Random();
                if (indexQuiz > -1) {
                    if (idRBAnswer != -1) {
                        RadioButton radio = (RadioButton) findViewById(idRBAnswer);
                        radio.setChecked(false);
                    }
                    checkAnswer = 0;
                    setQuestion();
                } else {
                    tvNumRemainTest.setText(indexMaxQuiz - countQuestion + "");
                    Toast.makeText(LessonActivity.this, "Bạn đã hoàn thành", Toast.LENGTH_SHORT).show();
                }

            }
        });
        rGGram.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkAnswer == 1) {
                    checkAnswer++;
                    if (answerQuiz.equals(rBAnswer1.getText().toString())) {
                        rBAnswer1.setTextColor(Color.parseColor("#00FB36"));
                    } else if (answerQuiz.equals(rBAnswer2.getText().toString())) {
                        rBAnswer2.setTextColor(Color.parseColor("#00FB36"));
                    } else if (answerQuiz.equals(rBAnswer3.getText().toString())) {
                        rBAnswer3.setTextColor(Color.parseColor("#00FB36"));
                    } else if (answerQuiz.equals(rBAnswer4.getText().toString())) {
                        rBAnswer4.setTextColor(Color.parseColor("#00FB36"));
                    }
                    RadioButton radioAnswer = (RadioButton) findViewById(checkedId);
                    String sAnswer = radioAnswer.getText().toString();
                    idRBAnswer = checkedId;
                    if (sAnswer.equals(answerQuiz)) {
                        numCor++;
                        tvNumCorTest.setText(numCor + "");
                    } else {
                        numIncor++;
                        tvNumIncorTest.setText(numIncor + "");
                        radioAnswer.setTextColor(Color.parseColor("#FF0000"));
                    }
                } else if (checkAnswer == 2) {
                    RadioButton radio = (RadioButton) findViewById(idRBAnswer);
                    radio.setChecked(true);
                }
            }
        });

    }

    private void resetQuiz() {
        rdQuiz = new RandomInt(indexMaxQuiz);
        indexQuiz = rdQuiz.Random();
        countQuestion = 0;
        numCor = 0;
        numIncor = 0;
        tvNumIncorTest.setText(numIncor + "");
        tvNumCorTest.setText(numCor + "");
        tvNumRemainTest.setText(indexMaxQuiz + "");
    }

    private void setQuestion() {
        tvNumRemainTest.setText(indexMaxQuiz - countQuestion + "");
        countQuestion++;
        rBAnswer1.setText(arrayListQuiz.size() + "");
        ArrayQuiz arrayQuiz = new ArrayQuiz();
        arrayQuiz = arrayListQuiz.get(indexQuiz);
        RandomInt rdAnsQuiz = new RandomInt(4);
        tvQuestion.setText("Câu hỏi"+" "+countQuestion +": " + arrayQuiz.getQuestion());
        rBAnswer1.setText(arrayQuiz.getAnswer(rdAnsQuiz.Random()));
        rBAnswer1.setChecked(false);
        rBAnswer1.setTextColor(Color.parseColor("#000000"));
        rBAnswer2.setChecked(false);
        rBAnswer2.setText(arrayQuiz.getAnswer(rdAnsQuiz.Random()));
        rBAnswer2.setTextColor(Color.parseColor("#000000"));
        rBAnswer3.setChecked(false);
        rBAnswer3.setText(arrayQuiz.getAnswer(rdAnsQuiz.Random()));
        rBAnswer3.setTextColor(Color.parseColor("#000000"));
        rBAnswer4.setChecked(false);
        rBAnswer4.setText(arrayQuiz.getAnswer(rdAnsQuiz.Random()));
        rBAnswer4.setTextColor(Color.parseColor("#000000"));
        answerQuiz = arrayQuiz.getAnswer(4).toString();
        checkAnswer++;
    }

    private void tabKanji() {
        //create Quiz
        TabHost.TabSpec tab;
        tab = mTabHost.newTabSpec("Kanji");
        tab.setContent(R.id.tabKanji);
        tab.setIndicator("Kanji");
        mTabHost.addTab(tab);
        // TODO

    }


    private void setLisview() {
        listViewVocabulary = (ListView) findViewById(R.id.listViewVocabulary);
        ArrayList<Employee> arrayListVocabulary = new ArrayList<>();
        tVToolbarLesson = (TextView) findViewById(R.id.tVToolbarLesson);
        Bundle bd = getIntent().getExtras();
        if (bd != null) {
            lesson = bd.getString("lesson");
            book = bd.getString("book");
            lessonName = bd.getString("lessonName");
        }
        // set tên bài lên toolbar;
        if (lessonName != null) {
            tVToolbarLesson.setText(getString(R.string.lesson) + " " + lesson + ": " + lessonName);
        } else tVToolbarLesson.setText(getString(R.string.lesson) + " " + lesson);
        // getdata
        dp = new DataProvider(this);
        if (dp != null) {
            mapVocab = dp.getData(book, "vocab", lesson);
            indexMax = mapVocab.size();
            sTu = new String[indexMax];
            sNghia = new String[indexMax];
            mapVocab.keySet().toArray(sTu);
            mapVocab.values().toArray(sNghia);
            // nhâp dữ liệu ở đây
            for (int i = 0; i < indexMax; i++) {
                Employee emp = new Employee();
                emp.setTu(sTu[i]);
                emp.setNghia(sNghia[i]);
                arrayListVocabulary.add(emp);
            }
            ArrayAdapterVocabulary adapter = new ArrayAdapterVocabulary(this, R.layout.item_listview_vocabulary, arrayListVocabulary);
            listViewVocabulary.setAdapter(adapter);
            // sự kiện text to speech
            listViewVocabulary.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    TextView tvListTu = (TextView) view.findViewById(R.id.tvListTu);
                    String stringTu = tvListTu.getText().toString();
                    speakWords(stringTu);
                }
            });
        }
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
        Intent intentSearch = new Intent(LessonActivity.this, SearchResult.class);
        intentSearch.putExtra("sSearch",query);
        startActivity(intentSearch);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}
