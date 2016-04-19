package hmdq.js.codeproject.dekirunihongo.Lesson;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import hmdq.js.codeproject.dekirunihongo.DataProvider;
import hmdq.js.codeproject.dekirunihongo.R;
import hmdq.js.codeproject.dekirunihongo.Vocabulary.ArrayAdapterVocabulary;
import hmdq.js.codeproject.dekirunihongo.Vocabulary.Employee;

public class LessonActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    TabHost mTabHost;
    ListView listViewVocabulary;
    TextView tVToolbarLesson;
    Button btnLearn;
    DataProvider dp = null;
    HashMap<String, String> mapVocab;
    // 2 mang sau đây là để test
//    private String[] sTu = {"わたし", "なまえ", "くに", "にほん", "かんこく", "ちゅうごく", "アメリカ", "イタリア", "オーストラリア", "ロシア", "タイ"};
//    private String[] sNghia = {"Tôi", "Tên", "Đất nước", "Nhật Bản", "Hàn Quốc", "Trung Quốc", "Mỹ", "Ý", "Úc", "Nga", "Thái Lan"};
        private String[] sTu = {"わたし"};
    private String[] sNghia = {"Tôi"};
//    private String[] sTu;
//    private String[] sNghia;
    private TextToSpeech myTTS;
    //status check code
    private int MY_DATA_CHECK_CODE = 0;
    private String lesson, book;
    private int indexMax;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson);
        mTabHost = (TabHost) findViewById(R.id.tabHostLesson);
        mTabHost.setup();
        // toolbar
        setToolbar();
        // tabs
        tabVocabulary();
        tabGrammar();
        tabListen();
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
                intenLeanVocab.putExtra("sTuLength",indexMax);
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

    }

    private void tabListen() {
        //create tabListen
        TabHost.TabSpec tab;
        tab = mTabHost.newTabSpec("listen");
        tab.setContent(R.id.tabListen);
        tab.setIndicator("Nghe");
        mTabHost.addTab(tab);
        // TODO

    }

    private void tabQuiz() {
        //create tabQuiz
        TabHost.TabSpec tab;
        tab = mTabHost.newTabSpec("Quiz");
        tab.setContent(R.id.tabQuiz);
        tab.setIndicator("Quiz");
        mTabHost.addTab(tab);
        // TODO

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
        if (bd!= null){
            lesson = bd.getString("lesson");
            book = bd.getString("book");
        }
        // set tên bài lên toolbar;
        if (lesson != null){
            tVToolbarLesson.setText("Lesson " + lesson);
        }
        // getdata
        dp = new DataProvider(this);
        if (dp != null) {
            mapVocab = dp.getData(book, "vocab", lesson);
//            indexMax = mapVocab.size();
            indexMax= sTu.length;
//            sTu = new String[indexMax];
//            sNghia = new String[indexMax];
//            mapVocab.keySet().toArray(sTu);
//            mapVocab.values().toArray(sNghia);
        // nhâp dữ kiệu ở đây
            for (int i = 0; i < indexMax; i++) {
            Employee emp = new Employee();
            emp.setTu(sTu[i]);
            emp.setNghia(sNghia[i]);
            arrayListVocabulary.add(emp);
        }
        ArrayAdapterVocabulary adapter = new ArrayAdapterVocabulary(this,R.layout.item_listview_vocabulary,arrayListVocabulary);
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
