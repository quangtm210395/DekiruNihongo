package hmdq.js.codeproject.dekirunihongo.Vocabulary;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import hmdq.js.codeproject.dekirunihongo.R;

/**
 * Created by Phan M Duong on 4/7/2016.
 */
public class ArrayAdapterVocabulary extends ArrayAdapter<Employee> {
    Activity context = null;
    ArrayList<Employee> myArray = null;
    int layoutId;

    // contructor
    public ArrayAdapterVocabulary(Activity context, int layoutId, ArrayList<Employee> myArray) {
        super(context, layoutId, myArray);
        this.context = context;
        this.layoutId = layoutId;
        this.myArray = myArray;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // phần này dùng để set ra list
        LayoutInflater inflater = context.getLayoutInflater();
        convertView = inflater.inflate(layoutId, null);
        final TextView tvListTu = (TextView) convertView.findViewById(R.id.tvListTu);
        final Employee emp = myArray.get(position);
        tvListTu.setText(emp.getTu());
        final TextView tvListNghia = (TextView) convertView.findViewById(R.id.tvListNghia);
        tvListNghia.setText(emp.getNghia());
        convertView.setAnimation(AnimationUtils.loadAnimation(context, R.anim.push_in));
        return convertView;
    }
}
