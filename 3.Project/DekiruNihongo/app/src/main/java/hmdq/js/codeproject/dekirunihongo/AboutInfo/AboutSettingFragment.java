package hmdq.js.codeproject.dekirunihongo.AboutInfo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import hmdq.js.codeproject.dekirunihongo.R;


public class AboutSettingFragment extends Fragment {
    private View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_about_setting, container, false);
        // Inflate the layout for this fragment
        return view;
    }

}
