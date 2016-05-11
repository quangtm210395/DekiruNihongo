package hmdq.js.codeproject.dekirunihongo;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import java.util.Locale;

import hmdq.js.codeproject.dekirunihongo.Lesson.LearnVocabulary;


public class TabFlashcardsFragment extends Fragment implements TextToSpeech.OnInitListener{
    private int MY_DATA_CHECK_CODE = 0;
    private TextToSpeech myTTS;
    private View view;
    private TextView tvFlash;
    private TextView tvPhan;
    private Button btnBack;
    private Button btnNext;
    private Button btnSpeakFlash;
    private int indexFCards;
    private String[] sTu;
    private String[] sNghia;
    private int sTuLength;
    private boolean checkFlash;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tab_flashcards, container, false);
        // Inflate the layout for this fragment
        setFromWidget();
        LearnVocabulary activity = (LearnVocabulary) getActivity();
        sTu = activity.getsTu();
        sNghia = activity.getsNghia();
        sTuLength = sTu.length;
        if (sTuLength > 0) {
            setFlashcards();
        }
        Intent checkTTSIntent = new Intent();
        checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkTTSIntent, MY_DATA_CHECK_CODE);
        return view;
    }

    private void setFlashcards() {
        indexFCards = 0;
        tvFlash.setText(sNghia[indexFCards]);
        tvPhan.setText((indexFCards + 1) + " / " + sTuLength);
        checkFlash = false;
        tvFlash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkFlash) {
                    tvFlash.setText(sNghia[indexFCards]);
                    checkFlash = false;
                } else {
                    tvFlash.setText(sTu[indexFCards]);
                    speakWords(sTu[indexFCards]);
                    checkFlash = true;
                }
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (indexFCards < sTuLength - 1) {
                    indexFCards = indexFCards + 1;
                    tvFlash.setText(sNghia[indexFCards]);
                    tvPhan.setText((indexFCards + 1) + " / " + sTuLength);
                    checkFlash = false;
                }
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (indexFCards > 0) {
                    indexFCards = indexFCards - 1;
                    tvFlash.setText(sNghia[indexFCards]);
                    tvPhan.setText((indexFCards + 1) + " / " + sTuLength);
                    checkFlash = false;
                }
            }
        });
        btnSpeakFlash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkFlash) {
                    speakWords(sTu[indexFCards]);

                }
                Toast.makeText(getContext(),sTu[indexFCards],Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setFromWidget() {
        btnBack = (Button) view.findViewById(R.id.btnBack);
        btnNext = (Button) view.findViewById(R.id.btnNext);
        tvFlash = (TextView) view.findViewById(R.id.tvFlash);
        tvPhan = (TextView) view.findViewById(R.id.tvPhan);
        btnSpeakFlash = (Button) view.findViewById(R.id.btnSpeakFlash);
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
