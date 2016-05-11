package hmdq.js.codeproject.dekirunihongo.Lesson;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.TreeMap;

import hmdq.js.codeproject.dekirunihongo.R;
import hmdq.js.codeproject.dekirunihongo.SearchResult;
import hmdq.js.codeproject.dekirunihongo.TabFlashcardsFragment;
import hmdq.js.codeproject.dekirunihongo.TabLearnFragment;
import hmdq.js.codeproject.dekirunihongo.TabSpellerFragment;

public class LearnVocabulary extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private SearchView searchView;
    private TabLayout tabLearnVocab;
    private ViewPager viewPagerLearnVocab;
    private TextView tVToolbarLearn;
    private String[] sTu;
    private String[] sNghia;
    private String lesson;
    private int sTuLength;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn_vocabulary);
        // không cho màn hình xoay ngang
        // toolbar
        setToolbar();
        // getdata
        getData();
        tVToolbarLearn = (TextView) findViewById(R.id.tVToolbarLearn);
        viewPagerLearnVocab = (ViewPager) findViewById(R.id.viewPagerLearnVocab);
        tabLearnVocab = (TabLayout) findViewById(R.id.tabsLearnVocab);
        setupViewPager(viewPagerLearnVocab);
        tabLearnVocab.setupWithViewPager(viewPagerLearnVocab);
        setupTabText();

    }

    public String[] getsTu() {
        return sTu;
    }

    public String[] getsNghia() {
        return sNghia;
    }

    private void getData() {
        Bundle bd = getIntent().getExtras();
        if (bd != null) {
            sTuLength = bd.getInt("sTuLength");
            sTu = new String[sTuLength];
            sNghia = new String[sTuLength];
            lesson = bd.getString("lesson");
            sTu = bd.getStringArray("Tu");
            sNghia = bd.getStringArray("Nghia");
        }
        // set tên bài lên toolbar;
        tVToolbarLearn.setText(getString(R.string.lesson) + " " + lesson);
        // set tên bài lên toolbar;
    }

    private void setupTabText() {
        TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabOne.setText(getString(R.string.flashcards));
        tabLearnVocab.getTabAt(0).setCustomView(tabOne);

        TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabTwo.setText(getString(R.string.learn));
        tabLearnVocab.getTabAt(1).setCustomView(tabTwo);

        TextView tabThree = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabThree.setText(getString(R.string.speller));
        tabLearnVocab.getTabAt(2).setCustomView(tabThree);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new TabFlashcardsFragment(), getString(R.string.flashcards));
        adapter.addFragment(new TabLearnFragment(), getString(R.string.learn));
        adapter.addFragment(new TabSpellerFragment(), getString(R.string.speller));
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Intent intentSearch = new Intent(LearnVocabulary.this, SearchResult.class);
        intentSearch.putExtra("sSearch", query);
        startActivity(intentSearch);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
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
