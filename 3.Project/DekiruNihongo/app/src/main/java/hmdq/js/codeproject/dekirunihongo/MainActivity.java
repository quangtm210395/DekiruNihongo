package hmdq.js.codeproject.dekirunihongo;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import java.util.List;

import hmdq.js.codeproject.dekirunihongo.Lesson.ListLesson;

public class MainActivity extends AppCompatActivity {

    ImageButton iBtn1,iBtn2,iBtn3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // không cho màn hình xoay ngang
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        iBtn1 = (ImageButton)findViewById(R.id.iBtn1);
        iBtn2 = (ImageButton)findViewById(R.id.iBtn2);
        iBtn3 = (ImageButton)findViewById(R.id.iBtn3);

        iBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mh2 = new Intent(MainActivity.this, ListLesson.class);
                mh2.putExtra("book", "book1");
                startActivity(mh2);
            }
        });

        iBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mh2 = new Intent(MainActivity.this, ListLesson.class);
                mh2.putExtra("book", "book2");
                startActivity(mh2);
            }
        });

        iBtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mh2 = new Intent(MainActivity.this, ListLesson.class);
                mh2.putExtra("book","book3");
                startActivity(mh2);

            }
        });
    }

}
