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
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

import hmdq.js.codeproject.dekirunihongo.R;
import hmdq.js.codeproject.dekirunihongo.Vocabulary.ArrayAdapterVocabulary;
import hmdq.js.codeproject.dekirunihongo.Vocabulary.Employee;

public class LessonActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private TextToSpeech myTTS;
    //status check code
    private int MY_DATA_CHECK_CODE = 0;
    TabHost mTabHost;
    ListView listViewVocabulary;
    TextView tVToolbarLesson;
    // 2 mang sau đây là để test
    private String[] sTu = {"わたし", "なまえ", "くに", "にほん", "かんこく", "ちゅうごく", "アメリカ", "イタリア", "オーストラリア", "ロシア", "タイ"};
    private String[] sNghia = {"Tôi", "Tên", "Đất nước", "Nhật Bản", "Hàn Quốc", "Trung Quốc", "Mỹ", "Ý", "Úc", "Nga", "Thái Lan"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson);
        // làm toolbar
        setToolbar();
        // làm tabs
        loadTabs();
        // làm listView cho phần vocabulary
        setLisview();
        //check for TTS data TextToSpeech;
        Intent checkTTSIntent = new Intent();
        checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkTTSIntent, MY_DATA_CHECK_CODE);
    }

    private void setLisview() {
        listViewVocabulary = (ListView)findViewById(R.id.listViewVocabulary);
        ArrayList<Employee> arrayListVocabulary = new ArrayList<>();
        tVToolbarLesson = (TextView) findViewById(R.id.tVToolbarLesson);
        Bundle bd = getIntent().getExtras();
        String lesson = null;
        if (bd!= null){
            lesson = bd.getString("lesson");
        }
        // set tên bài lên toolbar;
        if (lesson != null){
            tVToolbarLesson.setText(lesson);
        }

        int indexMax;
        indexMax = 10;
        // nhâp dữ kiệu ở đây
        for (int i = 0; i <= indexMax; i++) {
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

    private void loadTabs() {
        mTabHost = (TabHost)findViewById(R.id.tabHost);
        mTabHost.setup();
        TabHost.TabSpec spec;
        //Tạo tabVocabulary
        spec= mTabHost.newTabSpec("vocab");
        spec.setContent(R.id.tabVocabulary);
        spec.setIndicator("Vocabulary");
        mTabHost.addTab(spec);
        //Tạo tabGrammar
        spec= mTabHost.newTabSpec("gram");
        spec.setContent(R.id.tabGrammar);
        spec.setIndicator("Grammar");
        mTabHost.addTab(spec);
        //Tạo tabListen
        spec= mTabHost.newTabSpec("listen");
        spec.setContent(R.id.tabListen);
        spec.setIndicator("Listen");
        mTabHost.addTab(spec);
        //Tạo tabQuiz
        spec= mTabHost.newTabSpec("Quiz");
        spec.setContent(R.id.tabQuiz);
        spec.setIndicator("Quiz");
        mTabHost.addTab(spec);
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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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
