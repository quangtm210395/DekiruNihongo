package hmdq.js.codeproject.dekirunihongo.Quiz;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import hmdq.js.codeproject.dekirunihongo.Lesson.LessonActivity;
import hmdq.js.codeproject.dekirunihongo.Lesson.RandomInt;
import hmdq.js.codeproject.dekirunihongo.R;

public class TabQuizFragment extends Fragment {
    private View view;
    private TextView tvNumCorTest;
    private TextView tvNumRemainTest;
    private TextView tvNumIncorTest;
    private TextView tvQuestion;
    private RadioButton rBAnswer1;
    private RadioButton rBAnswer2;
    private RadioButton rBAnswer3;
    private RadioButton rBAnswer4;
    private RadioGroup rGGram;
    private Button btnStartOverQuiz;
    private Button btnNextQuiz;
    private String[] sQuestion;
    private String[] sAnswer;
    private String lesson;
    private String answerQuiz;
    private int indexMax;
    private int indexQuiz;
    private ArrayList<ArrayQuiz> arrayListQuiz = new ArrayList<>();
    private int checkAnswer = 0;
    private int numCor;
    private int numIncor;
    private int idRBAnswer = -1;
    private int countQuestion;
    private RandomInt rdQuiz;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tab_quiz, container, false);
        setFormWidget();
        LessonActivity activity = (LessonActivity) getActivity();
        sQuestion = activity.getsQuestion();
        sAnswer = activity.getsAnswer();
        lesson = activity.getLesson();
        setQuiz();
        // Inflate the layout for this fragment
        return view;
    }

    private void setFormWidget() {
        tvNumRemainTest = (TextView) view.findViewById(R.id.tvNumRemainTest);
        tvNumCorTest = (TextView) view.findViewById(R.id.tvNumCorTest);
        tvNumIncorTest = (TextView) view.findViewById(R.id.tvNumIncorTest);
        tvQuestion = (TextView) view.findViewById(R.id.tvQuestion);
        rBAnswer1 = (RadioButton) view.findViewById(R.id.rBAnswer1);
        rBAnswer2 = (RadioButton) view.findViewById(R.id.rBAnswer2);
        rBAnswer3 = (RadioButton) view.findViewById(R.id.rBAnswer3);
        rBAnswer4 = (RadioButton) view.findViewById(R.id.rBAnswer4);
        btnStartOverQuiz = (Button) view.findViewById(R.id.btnStartOverQuiz);
        rGGram = (RadioGroup) view.findViewById(R.id.rGGram);
        btnNextQuiz = (Button) view.findViewById(R.id.btnNextQuiz);
    }

    private void setQuiz() {
        indexMax = sQuestion.length;
        for (int i = 0; i < indexMax; i++) {
            ArrayQuiz arrayQuiz = new ArrayQuiz();
            arrayQuiz.setQuestion(sQuestion[i]);
            String[] answer = convertData(sAnswer[i]);
            arrayQuiz.setAnswer(answer);
            arrayListQuiz.add(arrayQuiz);
        }
        resetQuiz();
        setQuestion();
        btnStartOverQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (idRBAnswer != -1) {
                    RadioButton radio = (RadioButton) view.findViewById(idRBAnswer);
                    radio.setChecked(false);
                }
                checkAnswer = 0;
                resetQuiz();
                setQuestion();
            }
        });
        btnNextQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                indexQuiz = rdQuiz.Random();
                if (indexQuiz > -1) {
                    if (idRBAnswer != -1) {
                        RadioButton radio = (RadioButton) view.findViewById(idRBAnswer);
                        radio.setChecked(false);
                    }
                    checkAnswer = 0;
                    setQuestion();
                } else {
                    if (indexMax == 0){
                        tvNumRemainTest.setText(0 + "");
                    } else
                    tvNumRemainTest.setText(indexMax - countQuestion + "");
                    Toast.makeText(getActivity(), "Bạn đã hoàn thành", Toast.LENGTH_SHORT).show();
                }

            }
        });
        rGGram.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkAnswer == 1) {
                    checkAnswer++;
                    if (answerQuiz.equals(rBAnswer1.getText().toString())) {
                        rBAnswer1.setTextColor(Color.parseColor("#00FB36"));
                    } else if (answerQuiz.equals(rBAnswer2.getText().toString())) {
                        rBAnswer2.setTextColor(Color.parseColor("#00FB36"));
                    } else if (answerQuiz.equals(rBAnswer3.getText().toString())) {
                        rBAnswer3.setTextColor(Color.parseColor("#00FB36"));
                    } else if (answerQuiz.equals(rBAnswer4.getText().toString())) {
                        rBAnswer4.setTextColor(Color.parseColor("#00FB36"));
                    }
                    RadioButton radioAnswer = (RadioButton) view.findViewById(checkedId);
                    String sAnswer = radioAnswer.getText().toString();
                    idRBAnswer = checkedId;
                    if (sAnswer.equals(answerQuiz)) {
                        numCor++;
                        tvNumCorTest.setText(numCor + "");
                    } else {
                        numIncor++;
                        tvNumIncorTest.setText(numIncor + "");
                        radioAnswer.setTextColor(Color.parseColor("#FF0000"));
                    }
                } else if (checkAnswer == 2) {
                    RadioButton radio = (RadioButton) view.findViewById(idRBAnswer);
                    radio.setChecked(true);
                }
            }
        });

    }
    private String[] convertData(String s) {
        return s.split("｜");
    }


    private void resetQuiz() {
        rdQuiz = new RandomInt(indexMax);
        indexQuiz = rdQuiz.Random();
        countQuestion = 0;
        numCor = 0;
        numIncor = 0;
        tvNumIncorTest.setText(numIncor + "");
        tvNumCorTest.setText(numCor + "");
        tvNumRemainTest.setText(indexMax + "");
    }

    private void setQuestion() {
        tvNumRemainTest.setText(indexMax - countQuestion + "");
        countQuestion++;
        rBAnswer1.setText(arrayListQuiz.size() + "");
        ArrayQuiz arrayQuiz = new ArrayQuiz();
        if (arrayListQuiz.size() > 0){
            arrayQuiz = arrayListQuiz.get(indexQuiz);
            RandomInt rdAnsQuiz = new RandomInt(4);
            tvQuestion.setText("Câu hỏi" + " " + countQuestion + ": " + arrayQuiz.getQuestion());
            rBAnswer1.setText(arrayQuiz.getAnswer(rdAnsQuiz.Random()));
            rBAnswer1.setChecked(false);
            rBAnswer1.setTextColor(Color.parseColor("#000000"));
            rBAnswer2.setChecked(false);
            rBAnswer2.setText(arrayQuiz.getAnswer(rdAnsQuiz.Random()));
            rBAnswer2.setTextColor(Color.parseColor("#000000"));
            rBAnswer3.setChecked(false);
            rBAnswer3.setText(arrayQuiz.getAnswer(rdAnsQuiz.Random()));
            rBAnswer3.setTextColor(Color.parseColor("#000000"));
            rBAnswer4.setChecked(false);
            rBAnswer4.setText(arrayQuiz.getAnswer(rdAnsQuiz.Random()));
            rBAnswer4.setTextColor(Color.parseColor("#000000"));
            answerQuiz = arrayQuiz.getAnswer(4).toString();
            checkAnswer++;
        }

    }

}
