package hmdq.js.codeproject.dekirunihongo;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import hmdq.js.codeproject.dekirunihongo.AboutInfo.*;
import hmdq.js.codeproject.dekirunihongo.Lesson.ListLesson;

public class MainActivity extends AppCompatActivity{
    private static final int TIME_DELAY = 2000;
    private static long back_pressed;
    private Typeface tfLato;
    private Typeface tfKoz;
    private Button btnAbout;
    private TextView tvWelcome;
    private TextView tvWelcomeVN;
    private String[] sNameBook = {"初級", "初中級", "中級"};
    private String[] sNameBookVN = {"Sơ cấp","Sơ trung cấp", "Trung cấp"};
    private Integer[] imgIconid = {R.drawable.image_book_1,R.drawable.image_book_2,R.drawable.image_book_3};
    private ListView listViewBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // không cho màn hình xoay ngang
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getFromWidget();
        tfLato = Typeface.createFromAsset(getAssets(),"fonts/lato.ttf");
        tfKoz = Typeface.createFromAsset(getAssets(),"fonts/kozB.otf");
        final Intent mh2 = new Intent(MainActivity.this, ListLesson.class);
        CustomListBookAdapter adapter = new CustomListBookAdapter(MainActivity.this, sNameBook,sNameBookVN, imgIconid);
        listViewBook.setAdapter(adapter);
        listViewBook.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) mh2.putExtra("book", "1");
                if (position == 1) mh2.putExtra("book", "2");
                if (position == 2) mh2.putExtra("book", "3");
                startActivity(mh2);
            }
        });
        btnAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, About.class));
            }
        });
        tvWelcome.setTypeface(tfKoz);
        tvWelcomeVN.setTypeface(tfLato);
        btnAbout.setTypeface(tfLato);
    }

    private void getFromWidget() {
        tvWelcome = (TextView) findViewById(R.id.tvWelcome);
        tvWelcomeVN = (TextView) findViewById(R.id.tvWelcomeVN);
        listViewBook = (ListView) findViewById(R.id.listViewBook);
        btnAbout = (Button) findViewById(R.id.btnAbout);
    }

    @Override
    public void onBackPressed() {
        if (back_pressed + TIME_DELAY > System.currentTimeMillis()) {
            super.onBackPressed();
        } else {
            Toast.makeText(getBaseContext(), "Nhấn back thêm 1 lần nữa để thoát",
                    Toast.LENGTH_SHORT).show();
        }
        back_pressed = System.currentTimeMillis();
    }
}
