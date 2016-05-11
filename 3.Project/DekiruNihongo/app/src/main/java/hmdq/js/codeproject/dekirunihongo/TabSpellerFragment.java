package hmdq.js.codeproject.dekirunihongo;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import hmdq.js.codeproject.dekirunihongo.Lesson.LearnVocabulary;
import hmdq.js.codeproject.dekirunihongo.Lesson.RandomInt;
import hmdq.js.codeproject.dekirunihongo.Lesson.STrim;

public class TabSpellerFragment extends Fragment implements TextToSpeech.OnInitListener{
    private View view;
    private String[] sTu;
    private String[] sNghia;
    private int MY_DATA_CHECK_CODE = 0;
    private TextToSpeech myTTS;
    private RandomInt rdSpell;
    private Button btnStartOverSpell;
    private Button btnSpeakSpell;
    private TextView tvNumFinish;
    private TextView tvCheckInOrCorSpell;
    private TextView tvNghiaSpell;
    private TextView tvWriteTuSpell;
    private TextView tvNextSpell;
    private EditText edtTuSpell;
    private int indexSpell;
    private int indexFinish;
    private int sTuLength;
    private boolean checkEditSpell;
    private boolean checkLearn;
    private String sAnswerSpell;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tab_speller, container, false);
        getFromWidget();
        LearnVocabulary activity = (LearnVocabulary) getActivity();
        sTu = activity.getsTu();
        sNghia = activity.getsNghia();
        sTuLength = sTu.length;
        if (sNghia.length > 0) {
            setSpell();
        }
        Intent checkTTSIntent = new Intent();
        checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkTTSIntent, MY_DATA_CHECK_CODE);

        return view;
    }

    private void getFromWidget() {
        btnStartOverSpell = (Button) view.findViewById(R.id.btnStartOverSpell);
        btnSpeakSpell = (Button) view.findViewById(R.id.btnSpeakSpell);
        tvNumFinish = (TextView) view.findViewById(R.id.tvNumFinish);
        tvCheckInOrCorSpell = (TextView) view.findViewById(R.id.tvCheckInOrCorSpell);
        tvNghiaSpell = (TextView) view.findViewById(R.id.tvNghiaSpell);
        tvWriteTuSpell = (TextView) view.findViewById(R.id.tvWriteTuSpell);
        tvNextSpell = (TextView) view.findViewById(R.id.tvNextSpell);
        edtTuSpell = (EditText) view.findViewById(R.id.edtTuSpell);

    }

    private void setSpell() {
        resetSpell();
        tvNextSpell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                indexSpell = rdSpell.Random();
                if (indexSpell > -1) {
                    speakWords(sTu[indexSpell]);
                    nextSpell();
                } else {
                    Toast.makeText(getContext(), "Bạn đã hoàn thành", Toast.LENGTH_SHORT).show();
                }
            }
        });
        edtTuSpell.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    if (checkEditSpell) {
                        checkEditSpell = false;
                        if (checkLearn) {
                            sAnswerSpell = edtTuSpell.getText().toString();
                            STrim sSpellTrim = new STrim(sAnswerSpell);
                            sAnswerSpell = sSpellTrim.trim();
                            tvNumFinish.setText(indexFinish + "");
                            if (sAnswerSpell.equals(sTu[indexSpell])) {
                                tvCheckInOrCorSpell.setTextColor(getResources().getColor(R.color.colorGreenCorrect));
                                setTextAnswerSpell(getString(R.string.CORRECT), "");
                                indexFinish++;
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    public void run() {
                                        indexSpell = rdSpell.Random();
                                        if (indexSpell > -1) {
                                            speakWords(sTu[indexSpell]);
                                            nextSpell();
                                        } else {
                                            Toast.makeText(getContext(), "Bạn đã hoàn thành", Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                }, 2000);
                            } else {
                                tvNextSpell.setText("Tiếp tục");
                                rdSpell.remove(indexFinish % sTuLength);
                                tvCheckInOrCorSpell.setTextColor(getResources().getColor(R.color.colorRedIncor));
                                setTextAnswerSpell(getString(R.string.INCORRECT), "Correct: " + sTu[indexSpell]);
                            }
                            checkLearn = false;
                        }
                    } else edtTuSpell.setText(sAnswerSpell);
                }
                return false;
            }
        });
        btnSpeakSpell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speakWords(sTu[indexSpell]);
            }
        });
        btnStartOverSpell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetSpell();
                speakWords(sTu[indexSpell]);
            }
        });

    }
    private void setTextAnswerSpell(String inCorOrCor, String correct) {
        tvNghiaSpell.setText(sNghia[indexSpell]);
        tvCheckInOrCorSpell.setText(inCorOrCor);
        tvWriteTuSpell.setText(correct);
    }

    private void resetSpell() {
        rdSpell = new RandomInt(sTuLength);
        indexFinish = 0;
        tvNumFinish.setText(indexFinish + "");
        indexSpell = rdSpell.Random();
        nextSpell();
    }

    private void nextSpell() {
        checkEditSpell = true;
        tvNextSpell.setText("");
        tvCheckInOrCorSpell.setText("");
        tvNghiaSpell.setText("");
        edtTuSpell.setText("");
        checkLearn = true;
        tvWriteTuSpell.setText("");

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
