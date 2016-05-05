package hmdq.js.codeproject.dekirunihongo.Lesson;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import hmdq.js.codeproject.dekirunihongo.CommonData;
import hmdq.js.codeproject.dekirunihongo.DataProvider;
import hmdq.js.codeproject.dekirunihongo.R;
import hmdq.js.codeproject.dekirunihongo.SearchResult;

public class ListLesson extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private SearchView searchView;
    ListView listViewLesson;
    TextView tVToolbarBook;
    String book;
    DataProvider dp;
    List<String> listLesson;
    String[] sLesson;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_lesson);
        // làm toolbar
        setToolbar();
        tVToolbarBook = (TextView) findViewById(R.id.tVToolbarBook);

        listViewLesson = (ListView)findViewById(R.id.listViewLesson);

        Bundle bd = getIntent().getExtras();
        book = "";
        int indexMax;
        if (bd != null){
            book = bd.getString("book");
        }

        // lưu dữ liệu vị trí quyển sách hiện tại
        CommonData cd = CommonData.getInstance();
        cd.noBook = book;

        // lấy danh sách tên bài
        dp = new DataProvider(this);
        if (dp != null){
            listLesson = dp.getListLesson(book,"vocab");
            sLesson = new String[listLesson.size()];
            if (listLesson != null) sLesson = listLesson.toArray(sLesson);
        }

        // set tên sách lên toolbar;
        if (book != null) {
            if (book.equals("1")){
                tVToolbarBook.setText(getString(R.string.book) + " "+ book + ": しょきゅう");
            } else if (book.equals("2")){
                tVToolbarBook.setText(getString(R.string.book) + " "+ book + ": しょちゅうきゅう");
            } else if (book.equals("3")){
                tVToolbarBook.setText(getString(R.string.book) + " "+ book + ": ちゅうきゅう");
            }

        }
        if (book != null && (book.equals("1") || book.equals("2"))) indexMax = 15;
        else indexMax = 20;
        ArrayList<String> arrayListLesson = new ArrayList<>();
        //set tên bài vào danh sách bài
        if (sLesson.length != 0){
            for (int i = 1; i <= indexMax; i ++){
                arrayListLesson.add(getString(R.string.lesson) + " "+ (i) + ": " + sLesson[i-1]);
            }
        } else {
            for (int i = 1; i <= indexMax; i ++){
                arrayListLesson.add(getString(R.string.lesson) + " "+ (i));
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                ListLesson.this,
                R.layout.simple_list_item,
                arrayListLesson
        );
        listViewLesson.setAdapter(adapter);

        for (int i = 1; i <= indexMax; i++){
            listViewLesson.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent mh3 = new Intent(ListLesson.this, LessonActivity.class);
                    mh3.putExtra("lesson", "" + (position + 1));
                    mh3.putExtra("book", book);
                    startActivity(mh3);
                }
            });
        }

    }

    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarListLesson);
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
        Intent intentSearch = new Intent(ListLesson.this, SearchResult.class);
        intentSearch.putExtra("sSearch",query);
        startActivity(intentSearch);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}
