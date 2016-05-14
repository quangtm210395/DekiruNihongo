package hmdq.js.codeproject.dekirunihongo.LearnVocab;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.TreeMap;

import hmdq.js.codeproject.dekirunihongo.Lesson.RandomInt;
import hmdq.js.codeproject.dekirunihongo.Lesson.STrim;
import hmdq.js.codeproject.dekirunihongo.R;

public class TabLearnFragment extends Fragment {
    private View view;
    private String[] sTu;
    private String[] sNghia;
    private String[] sTutmp;
    private String[] sNghiatmp;
    private String sAnswerLearn;
    private int sTuLength;
    private int indexLearn;
    private int indexCor;
    private int indexInCor;
    private int indexRemain;
    private Button btnStartOverLearn;
    private Button btnNextLearn;
    private TextView tvNumRemain;
    private TextView tvNumInCor;
    private TextView tvNumCor;
    private TextView tvCheckInOrCorLearn;
    private TextView tvNghiaLearn;
    private TextView tvWriteTuLearn;
    private EditText edtTuLearn;
    private boolean checkEditLearn;
    private RandomInt rdLearn;
    private Toast toast;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tab_learn, container, false);
        // Inflate the layout for this fragment
        setFromWidget();
        LearnVocabulary activity = (LearnVocabulary) getActivity();
        sTu = activity.getsTu();
        sNghia = activity.getsNghia();
        sTuLength = sTu.length;
        if (sTuLength > 0) {
            setLearn();
        }
        return view;
    }

    private void setFromWidget() {
        btnStartOverLearn = (Button) view.findViewById(R.id.btnStartOverLearn);
        btnNextLearn = (Button) view.findViewById(R.id.btnNextLearn);
        tvNumRemain = (TextView) view.findViewById(R.id.tvNumRemain);
        tvNumInCor = (TextView) view.findViewById(R.id.tvNumIncor);
        tvNumCor = (TextView) view.findViewById(R.id.tvNumCor);
        tvCheckInOrCorLearn = (TextView) view.findViewById(R.id.tvCheckInOrCorLearn);
        tvNghiaLearn = (TextView) view.findViewById(R.id.tvNghiaLearn);
        tvWriteTuLearn = (TextView) view.findViewById(R.id.tvWriteTuLearn);
        edtTuLearn = (EditText) view.findViewById(R.id.edtTuLearn);
    }

    private void setLearn() {
        final TreeMap<String, String> sSai = new TreeMap<>();
        resetLearn(sTuLength, sTu, sNghia);
        edtTuLearn.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    STrim sTrim = new STrim(edtTuLearn.getText().toString());
                    if (!sTrim.trim().equals("")) {
                        if (checkEditLearn) {
                            sAnswerLearn = edtTuLearn.getText().toString();
                            checkEditLearn = false;
                            STrim sLearnTrim = new STrim(sAnswerLearn);
                            sAnswerLearn = sLearnTrim.trim();
                            if (sAnswerLearn.equals(sTutmp[indexLearn])) {
                                indexCor++;
                                tvCheckInOrCorLearn.setTextColor(getResources().getColor(R.color.colorGreenCorrect));
                                setTextAnswerLearn(getString(R.string.CORRECT), "");
                                tvNumCor.setText(indexCor + "");
                            } else {
                                indexInCor++;
                                tvNumInCor.setText(indexInCor + "");
                                tvCheckInOrCorLearn.setTextColor(getResources().getColor(R.color.colorRedIncor));
                                setTextAnswerLearn(getString(R.string.INCORRECT), getString(R.string.correct) + ": " + sTutmp[indexLearn]);
                                sSai.put(sTutmp[indexLearn], sNghiatmp[indexLearn]);
                            }
                        } else edtTuLearn.setText(sAnswerLearn);
                        return true;
                    }
                }
                return false;
            }
        });
        btnStartOverLearn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetLearn(sTuLength, sTu, sNghia);
            }
        });

        btnNextLearn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (toast != null)
                    toast.cancel();
                if (indexRemain > 0) {
                    if (!tvCheckInOrCorLearn.getText().toString().equals("")) {
                        indexLearn = rdLearn.Random();
                        setTextLearn();
                    } else {
                        if (indexRemain == 1)
                            toast = Toast.makeText(getContext(), "Bạn hãy viết câu trả lời trước khi kết thúc", Toast.LENGTH_SHORT);
                        else
                            toast = Toast.makeText(getContext(), "Bạn hãy viết câu trả lời trước khi trước khi làm câu tiếp theo", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                } else {
                    if (sSai.size() != 0) {
                        sTutmp = new String[sSai.size()];
                        sNghiatmp = new String[sSai.size()];
                        sSai.keySet().toArray(sTutmp);
                        sSai.values().toArray(sNghiatmp);
                        resetLearn(sSai.size(), sTutmp, sNghiatmp);
                    } else {
                        tvNumRemain.setText(indexRemain + "");
                        toast = Toast.makeText(getContext(), "Bạn đã hoàn thành", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
            }
        });
    }

    // setText khi chọn buttun Answer
    private void setTextAnswerLearn(String inCorOrCor, String correct) {
        tvCheckInOrCorLearn.setText(inCorOrCor);
        tvWriteTuLearn.setText(correct);
    }

    // reset lại các biến trong tablearn;
    private void resetLearn(int sTuLength, String[] sTu, String[] sNghia) {
        sTutmp = new String[sTuLength];
        sNghiatmp = new String[sTuLength];
        sTutmp = sTu;
        sNghiatmp = sNghia;
        indexRemain = sTuLength;
        rdLearn = new RandomInt(indexRemain);
        indexCor = 0;
        indexInCor = 0;
        tvNumCor.setText("" + indexCor);
        tvNumInCor.setText("" + indexInCor);
        setTextLearn();

    }

    //tạo ra setText toàn bộ learn
    private void setTextLearn() {
        checkEditLearn = true;
        indexLearn = rdLearn.Random();
        tvNumRemain.setText(indexRemain + "");
        tvNghiaLearn.setText(sNghiatmp[indexLearn]);
        edtTuLearn.setText("");
        indexRemain--;
        tvCheckInOrCorLearn.setText("");
        tvWriteTuLearn.setText("");
    }

}
