package hmdq.js.codeproject.dekirunihongo.Kanji;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import hmdq.js.codeproject.dekirunihongo.R;

public class KanjiActivity extends AppCompatActivity {
    private TextView tVToolbarKanji;
    private TextView tvNameKanji;
    private TextView tvKanji;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kanji);
        setToolbar();
        tVToolbarKanji = (TextView) findViewById(R.id.tVToolbarKanji);
        tvNameKanji= (TextView) findViewById(R.id.tvNameKanji);
        tvKanji = (TextView) findViewById(R.id.tvKanji);
        Bundle bd = getIntent().getExtras();
        if (bd != null){
            tvKanji.setText(bd.getString("kanji"));
            tvNameKanji.setText(bd.getString("namekanji"));
            tVToolbarKanji.setText(bd.getString("lesson"));
        }

    }
    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarKanji);
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
