package hmdq.js.codeproject.dekirunihongo.Kanji;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import hmdq.js.codeproject.dekirunihongo.Lesson.LessonActivity;
import hmdq.js.codeproject.dekirunihongo.R;


public class TabKanjiFragment extends Fragment {
    private View view;
    private ListView listViewKanji;
    private String[] sNameKanji;
    private String[] sKanji;
    private String lesson;
    private int indexMax;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_tab_kanji, container, false);
        LessonActivity activity = (LessonActivity) getActivity();
        sNameKanji = activity.getsNameKanji();
        sKanji = activity.getsKanji();
        lesson = activity.getLesson();
        if (sKanji.length > 0)
        setListview();
        return view;
    }

    private void setListview() {
        listViewKanji = (ListView) view.findViewById(R.id.listViewKanji);
        indexMax = sNameKanji.length;
        ArrayList<String> arrayListKanji = new ArrayList<>();
        for (int i = 0; i < indexMax; i++) {
            arrayListKanji.add(sNameKanji[i]);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                arrayListKanji
        );
        listViewKanji.setAdapter(adapter);
        listViewKanji.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intentKanji = new Intent(getActivity(), KanjiActivity.class);
                intentKanji.putExtra("lesson", lesson);
                intentKanji.putExtra("namekanji", sNameKanji[position]);
                intentKanji.putExtra("kanji", sKanji[position]);
                startActivity(intentKanji);
            }
        });
    }

}
