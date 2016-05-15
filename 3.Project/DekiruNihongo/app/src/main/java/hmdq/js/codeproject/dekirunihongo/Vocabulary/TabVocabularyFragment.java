package hmdq.js.codeproject.dekirunihongo.Vocabulary;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

import hmdq.js.codeproject.dekirunihongo.LearnVocab.LearnVocabulary;
import hmdq.js.codeproject.dekirunihongo.Lesson.LessonActivity;
import hmdq.js.codeproject.dekirunihongo.R;

public class TabVocabularyFragment extends Fragment implements TextToSpeech.OnInitListener{
    private String[] sTu;
    private String[] sNghia;
    private ListView listViewVocabulary;
    private View view;
    private int indexMax;
    private Button btnLearn;
    private String lesson;
    private int MY_DATA_CHECK_CODE = 0;
    private TextToSpeech myTTS;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tab_vocabulary, container, false);
        LessonActivity activity = (LessonActivity) getActivity();
        sTu = activity.getsTu();
        sNghia = activity.getsNghia();
        lesson = activity.getLesson();
        indexMax = sTu.length;
        if (sNghia.length > 0) {
            setLisview();
            learnVocab();
        }
        Intent checkTTSIntent = new Intent();
        checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkTTSIntent, MY_DATA_CHECK_CODE);
        // Inflate the layout for this fragment
        return view;
    }

    private void learnVocab() {
        // lean vocabulary
        btnLearn = (Button) view.findViewById(R.id.btnLearn);
        btnLearn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (indexMax > 0) {
                    Intent intenLeanVocab = new Intent(getActivity(), LearnVocabulary.class);
                    intenLeanVocab.putExtra("sTuLength", indexMax);
                    intenLeanVocab.putExtra("lesson", lesson);
                    intenLeanVocab.putExtra("Tu", sTu);
                    intenLeanVocab.putExtra("Nghia", sNghia);
                    startActivity(intenLeanVocab);
                }
            }
        });
    }

    private void setLisview() {
        listViewVocabulary = (ListView) view.findViewById(R.id.listViewVocabulary);
        ArrayList<Employee> arrayListVocabulary = new ArrayList<>();
        for (int i = 0; i < indexMax; i++) {
            Employee emp = new Employee();
            emp.setTu(sTu[i]);
            emp.setNghia(sNghia[i]);
            arrayListVocabulary.add(emp);
        }
        ArrayAdapterVocabulary adapter = new ArrayAdapterVocabulary(getActivity(), R.layout.item_listview_vocabulary, arrayListVocabulary);
        listViewVocabulary.setAdapter(adapter);
        // sự kiện text to speech
        listViewVocabulary.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tvListTu = (TextView) view.findViewById(R.id.tvListTu);
                String stringTu = tvListTu.getText().toString();
                    speakWords(stringTu);
            }
        });
    }
    // TEXT TO SPEECH
    //speak text
    private void speakWords(String speech) {

        //speak straight away
        if (myTTS != null){
            myTTS.speak(speech, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    //kiểm tra kết quả của Text To Speech

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == MY_DATA_CHECK_CODE) {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                //the user has the necessary data - create the TTS
                myTTS = new TextToSpeech(getActivity(), this);
            } else {
                //no data - install it now
                Intent installTTSIntent = new Intent();
                installTTSIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installTTSIntent);
            }
        }
    }

    //thiết lập Text To Speech
    public void onInit(int initStatus) {

        //check for successful instantiation
        if (initStatus == TextToSpeech.SUCCESS) {
            if (myTTS.isLanguageAvailable(Locale.JAPANESE) == TextToSpeech.LANG_AVAILABLE)
                myTTS.setLanguage(Locale.JAPANESE);
        } else if (initStatus == TextToSpeech.ERROR) {
            Toast.makeText(getActivity(), "Sorry! Text To Speech failed...", Toast.LENGTH_LONG).show();
        }
    }
}
