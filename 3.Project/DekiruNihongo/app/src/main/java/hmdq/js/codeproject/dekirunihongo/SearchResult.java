package hmdq.js.codeproject.dekirunihongo;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.TreeMap;

import hmdq.js.codeproject.dekirunihongo.Vocabulary.ArrayAdapterVocabulary;
import hmdq.js.codeproject.dekirunihongo.Vocabulary.Employee;

public class SearchResult extends AppCompatActivity implements SearchView.OnQueryTextListener, TextToSpeech.OnInitListener {
    private SearchView searchView;
    private TextView tVToolbarSearch;
    private ListView listViewSearch;
    private TextToSpeech myTTS;
    //status check code
    private int MY_DATA_CHECK_CODE = 0;
    private DataProvider dp = null;
    private String[] sTu;
    private String[] sNghia;
    private TreeMap<String, String> mapSearch = null;
    private int indexMax;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        tVToolbarSearch = (TextView) findViewById(R.id.tVToolbarSearch);
        setToolbar();
        Bundle bd = getIntent().getExtras();
        tVToolbarSearch.setText(bd.getString("sSearch"));
        searchTu(bd.getString("sSearch"));
    }
    private void searchTu(String query) {
        Toast.makeText(SearchResult.this,query,Toast.LENGTH_SHORT).show();
        listViewSearch = (ListView) findViewById(R.id.listViewSearch);
        CommonData cd = CommonData.getInstance();
        ArrayList<Employee> arrayListVocabulary = new ArrayList<>();
        // getdata
        dp = new DataProvider(this);
        if (dp != null) {
            //Toast.makeText(SearchResult.this,cd.noBook+"",Toast.LENGTH_SHORT).show();
            mapSearch = dp.find(query, cd.noBook);
            indexMax = mapSearch.size();
            if (indexMax == 0) {
                Toast.makeText(SearchResult.this,"Từ không được tìm thấy",Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(SearchResult.this,"Từ đã được tìm thấy",Toast.LENGTH_SHORT).show();
                sTu = new String[indexMax];
                sNghia = new String[indexMax];
                mapSearch.keySet().toArray(sTu);
                mapSearch.values().toArray(sNghia);
                // nhâp dữ liệu ở đây
                for (int i = 0; i < indexMax; i++) {
                    Employee emp = new Employee();
                    emp.setTu(sTu[i]);
                    emp.setNghia(sNghia[i]);
                    arrayListVocabulary.add(emp);
                }
                ArrayAdapterVocabulary adapter = new ArrayAdapterVocabulary(this, R.layout.item_listview_vocabulary, arrayListVocabulary);
                listViewSearch.setAdapter(adapter);
                // sự kiện text to speech
                listViewSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        TextView tvListTu = (TextView) view.findViewById(R.id.tvListTu);
                        String stringTu = tvListTu.getText().toString();
                        speakWords(stringTu);
                    }
                });
            }
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
        tVToolbarSearch.setText(query);
        searchTu(query);
        return false;
    }


    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }


}
