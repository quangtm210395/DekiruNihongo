package hmdq.js.codeproject.dekirunihongo.Lesson;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

import hmdq.js.codeproject.dekirunihongo.CommonData;
import hmdq.js.codeproject.dekirunihongo.R;

public class ListLesson extends AppCompatActivity {
    ListView listViewLesson;
    TextView tVToolbarBook;
    String book;
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
        CommonData.noBook = book;

        // set tên sách lên toolbar;
        if (book != null) {
            tVToolbarBook.setText(getString(R.string.book) + " "+ book );
        }
        if (book != null && (book.equals("1") || book.equals("2"))) indexMax = 15;
        else indexMax = 20;
        ArrayList<String> arrayListLesson = new ArrayList<>();
        for (int i = 1; i <= indexMax; i ++){
            arrayListLesson.add(getString(R.string.lesson) + " "+ (i));
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
