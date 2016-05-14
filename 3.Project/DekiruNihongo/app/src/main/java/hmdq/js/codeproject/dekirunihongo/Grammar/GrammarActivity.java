package hmdq.js.codeproject.dekirunihongo.Grammar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import hmdq.js.codeproject.dekirunihongo.R;

public class GrammarActivity extends AppCompatActivity {
    TextView tvNameGram,tvGram,tVToolbarGram;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grammar);
        setToolbar();
        tVToolbarGram = (TextView) findViewById(R.id.tVToolbarGram);
        tvNameGram = (TextView) findViewById(R.id.tvNameGram);
        tvGram = (TextView) findViewById(R.id.tvGram);
        Bundle bd = getIntent().getExtras();
        if (bd != null){
            String sGram = bd.getString("gram").replaceAll("@","'");
            tvGram.setText(Html.fromHtml(sGram));
            tvNameGram.setText(bd.getString("namegram"));
            tVToolbarGram.setText(bd.getString("lesson"));
        }

    }
    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarGram);
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
            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
