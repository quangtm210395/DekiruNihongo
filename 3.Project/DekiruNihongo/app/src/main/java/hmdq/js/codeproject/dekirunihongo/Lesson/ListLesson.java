package hmdq.js.codeproject.dekirunihongo.Lesson;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import hmdq.js.codeproject.dekirunihongo.R;

public class ListLesson extends AppCompatActivity {
    ListView listViewLesson;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_lesson);

        listViewLesson = (ListView)findViewById(R.id.listViewLesson);

        Bundle bd = getIntent().getExtras();
        String book = "";
        int indexMax;
        if (bd != null){
            book = bd.getString("book");
        }
        if ( book != null &&( book.equals("book1") || book.equals("book2"))) indexMax = 15;
        else indexMax = 20;
        ArrayList<String> arrayListLesson = new ArrayList<>();
        for (int i = 1; i <= indexMax; i ++){
            arrayListLesson.add("Lesson " + (i));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                ListLesson.this,
                android.R.layout.simple_list_item_1,
                arrayListLesson
        );
        listViewLesson.setAdapter(adapter);

        for (int i = 1; i <= indexMax; i++){
            listViewLesson.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent mh3 = new Intent(ListLesson.this, LessonActivity.class);
                    mh3.putExtra("lesson", "Lesson " + (position + 1));
                    startActivity(mh3);
                }
            });
        }

    }
}
