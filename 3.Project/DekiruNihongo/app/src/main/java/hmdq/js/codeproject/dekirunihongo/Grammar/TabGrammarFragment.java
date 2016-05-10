package hmdq.js.codeproject.dekirunihongo.Grammar;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import hmdq.js.codeproject.dekirunihongo.Grammar.GrammarActivity;
import hmdq.js.codeproject.dekirunihongo.Lesson.LessonActivity;
import hmdq.js.codeproject.dekirunihongo.R;


public class TabGrammarFragment extends Fragment {
    private ListView listViewGrammar;
    // indexMax là số lượng phần tử của listViewGrammar
    private int indexMax;
    // sNameGram mảng string chứa tên của ngữ pháp
    private String[] sNameGram;
    // sGram mảng string giải nghĩa của ngữ pháp
    private String[] sGram;
    // lesson là tên của bài
    private String lesson;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_tab_grammar, container, false);
        LessonActivity activity = (LessonActivity) getActivity();
        sNameGram = activity.getsNameGram();
        sGram = activity.getsGram();
        lesson = activity.getLesson();
        setListview();
        return view;
    }

    private void setListview() {
        listViewGrammar = (ListView) view.findViewById(R.id.listViewGrammar);
        indexMax = sNameGram.length;
        ArrayList<String> arrayListGram = new ArrayList<>();
        for (int i = 0; i < indexMax; i++) {
            arrayListGram.add(sNameGram[i]);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                arrayListGram
        );
        listViewGrammar.setAdapter(adapter);
        listViewGrammar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intentGram = new Intent(getActivity(), GrammarActivity.class);
                intentGram.putExtra("lesson", lesson);
                intentGram.putExtra("namegram", sNameGram[position]);
                intentGram.putExtra("gram", sGram[position]);
                startActivity(intentGram);
            }
        });
    }

}
