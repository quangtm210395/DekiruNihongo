package hmdq.js.codeproject.dekirunihongo.AboutInfo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import hmdq.js.codeproject.dekirunihongo.R;


public class AboutSettingFragment extends Fragment {
    private View view;
    private TextView tvLinkTTS;
    private TextView tvLinkInput;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_about_setting, container, false);
        tvLinkTTS = (TextView) view.findViewById(R.id.tvLinkTTS);
        tvLinkInput = (TextView) view.findViewById(R.id.tvLinkKeyboard);
        tvLinkTTS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = getString(R.string.linktts);
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
        tvLinkInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = getString(R.string.link_input);
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
        // Inflate the layout for this fragment
        return view;
    }

}
