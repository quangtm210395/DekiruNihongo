package hmdq.js.codeproject.dekirunihongo.Lesson;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.Toast;

import java.util.ArrayList;

import hmdq.js.codeproject.dekirunihongo.R;
import hmdq.js.codeproject.dekirunihongo.Vocabulary.ArrayAdapterVocabulary;
import hmdq.js.codeproject.dekirunihongo.Vocabulary.Employee;

public class LessonActivity extends AppCompatActivity {

    TabHost mTabHost;
    ListView listViewVocabulary;
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
    }

    private void setLisview() {
        listViewVocabulary = (ListView)findViewById(R.id.listViewVocabulary);
        ArrayList<Employee> arrayListVocabulary = new ArrayList<Employee>();
        Bundle bd = getIntent().getExtras();
        String lesson = null;
        if (bd!= null){
            lesson = bd.getString("lesson");
        }
        if (lesson != null) {
            Toast.makeText(
                    LessonActivity.this,
                    lesson,
                    Toast.LENGTH_SHORT).show();
        }
        int indexMax;
        indexMax = 20;
        // nhâp dữ kiệu ở đây
        for (int i = 1; i <= indexMax; i ++){
            Employee emp = new Employee();
            emp.setTu("Tu " + (i));
            emp.setNghia("Nghia " + (i));
            arrayListVocabulary.add(emp);
        }
        ArrayAdapterVocabulary adapter = new ArrayAdapterVocabulary(this,R.layout.item_listview_vocabulary,arrayListVocabulary);
        listViewVocabulary.setAdapter(adapter);

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
