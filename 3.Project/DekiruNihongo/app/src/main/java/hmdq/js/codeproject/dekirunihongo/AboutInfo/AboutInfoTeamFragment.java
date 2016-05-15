package hmdq.js.codeproject.dekirunihongo.AboutInfo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import hmdq.js.codeproject.dekirunihongo.R;


public class AboutInfoTeamFragment extends Fragment {
    private View view;
    private TextView tvInfoTeam;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_about_info_team, container, false);
        tvInfoTeam = (TextView) view.findViewById(R.id.tvInfoTeam);
        tvInfoTeam.setText(Html.fromHtml(getString(R.string.about_info_team)));
        // Inflate the layout for this fragment
        return view;
    }
}
