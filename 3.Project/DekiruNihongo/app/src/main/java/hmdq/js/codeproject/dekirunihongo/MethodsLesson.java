package hmdq.js.codeproject.dekirunihongo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MethodsLesson extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // add actionbar;
        setContentView(R.layout.activity_methods_lesson);
        Bundle bd = getIntent().getExtras();
        String lesson = null;
        if (bd != null){
            lesson = bd.getString("lesson");
        }
        if (lesson != null) {
            Toast.makeText(
                    MethodsLesson.this,
                    lesson,
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
