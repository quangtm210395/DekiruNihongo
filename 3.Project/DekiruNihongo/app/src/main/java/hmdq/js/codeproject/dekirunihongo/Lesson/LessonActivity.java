package hmdq.js.codeproject.dekirunihongo.Lesson;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import hmdq.js.codeproject.dekirunihongo.GetData;
import hmdq.js.codeproject.dekirunihongo.R;
import hmdq.js.codeproject.dekirunihongo.SearchResult;
import hmdq.js.codeproject.dekirunihongo.Grammar.TabGrammarFragment;
import hmdq.js.codeproject.dekirunihongo.Kanji.TabKanjiFragment;
import hmdq.js.codeproject.dekirunihongo.Quiz.TabQuizFragment;
import hmdq.js.codeproject.dekirunihongo.Vocabulary.TabVocabularyFragment;

public class LessonActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private SearchView searchView;
    private String lesson;
    private String book;
    private TabLayout tabLesson;
    private ViewPager viewPagerLesson;
    private GetData gd;
    private TextView tVToolBarLesson;
    private String lessonName;
    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson);
        setToolbar();

        // không cho màn hình xoay ngang
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        tVToolBarLesson = (TextView) findViewById(R.id.tVToolbarLesson);
        viewPagerLesson = (ViewPager) findViewById(R.id.viewPagerLesson);
        tabLesson = (TabLayout) findViewById(R.id.tabsLesson);
        setupViewPager(viewPagerLesson);
        tabLesson.setupWithViewPager(viewPagerLesson);
        setupTabText();
        //getData
        getData();
        viewPagerLesson.setCurrentItem(0);
        viewPagerLesson.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (toast != null)
                    toast.cancel();
                if (position == 0) {

                }
                if (position == 3) {
                    toast = Toast.makeText(LessonActivity.this, "Trả lời câu hỏi với đáp án đúng nhất", Toast.LENGTH_LONG);
                    toast.show();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void getData() {
        Bundle bd = getIntent().getExtras();
        if (bd != null) {
            lesson = bd.getString("lesson");
            book = bd.getString("book");
            lessonName = bd.getString("lessonName");
        }
        tVToolBarLesson.setText(getString(R.string.lesson) + " " + lesson + ": " + lessonName);
        gd = new GetData(this, book, lesson);
    }

    public String[] getsTu() {
        return gd.getsTu();
    }

    public String[] getsNghia() {
        return gd.getsNghia();
    }

    public String[] getsNameGram() {
        return gd.getsNameGram();
    }

    public String[] getsGram() {
        return gd.getsGram();
    }

    public String[] getsNameKanji() {
        return gd.getsNameKanji();
    }

    public String[] getsKanji() {
        return gd.getsKanji();
    }

    public String[] getsQuestion() {
        return gd.getsQuestion();
    }

    public String[] getsAnswer() {
        return gd.getsAnswer();
    }

    public String getLesson() {
        return getString(R.string.lesson) + " " + lesson + ": " + lessonName;
    }

    private void setupTabText() {
        TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabOne.setText(getString(R.string.vocabulary));
        tabLesson.getTabAt(0).setCustomView(tabOne);

        TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabTwo.setText(getString(R.string.grammar));
        tabLesson.getTabAt(1).setCustomView(tabTwo);

        TextView tabThree = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabThree.setText(getString(R.string.kanji));
        tabLesson.getTabAt(2).setCustomView(tabThree);

        TextView tabFour = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabFour.setText(getString(R.string.quiz));
        tabLesson.getTabAt(3).setCustomView(tabFour);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new TabVocabularyFragment(), getString(R.string.vocabulary));
        adapter.addFragment(new TabGrammarFragment(), getString(R.string.grammar));
        adapter.addFragment(new TabKanjiFragment(), getString(R.string.kanji));
        adapter.addFragment(new TabQuizFragment(), getString(R.string.quiz));
        viewPager.setAdapter(adapter);
    }


    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final ArrayList<Fragment> mFragmentList = new ArrayList<>();
        private final ArrayList<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public long getItemId(int position) {

            return super.getItemId(position);
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
    public boolean onQueryTextSubmit(String query) {
        Intent intentSearch = new Intent(LessonActivity.this, SearchResult.class);
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
