package hmdq.js.codeproject.dekirunihongo.Grammar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import hmdq.js.codeproject.dekirunihongo.R;
import hmdq.js.codeproject.dekirunihongo.SearchResult;

public class GrammarActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    TextView tvNameGram,tvGram,tVToolbarGram;
    private SearchView searchView;
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
        MenuItem itemSearch = menu.findItem(R.id.mnSearch);
        searchView = (SearchView) itemSearch.getActionView();
        //set OnQueryTextListener cho search view để thực hiện search theo text
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Intent intentSearch = new Intent(GrammarActivity.this, SearchResult.class);
        intentSearch.putExtra("sSearch", query);
        searchView.clearFocus();
        startActivity(intentSearch);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
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
