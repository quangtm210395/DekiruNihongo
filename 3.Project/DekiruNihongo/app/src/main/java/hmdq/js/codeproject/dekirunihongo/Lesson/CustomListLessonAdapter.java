package hmdq.js.codeproject.dekirunihongo.Lesson;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import hmdq.js.codeproject.dekirunihongo.R;

/**
 * Created by Phan M Duong on 5/7/2016.
 */
public class CustomListLessonAdapter extends ArrayAdapter<String> {
    private final Activity context;
    private final String[] itemname;
    private final Integer[] imgid;

    public CustomListLessonAdapter(Activity context, String[] itemname, Integer[] imgid) {
        super(context, R.layout.simple_list_item, itemname);
        // TODO Auto-generated constructor stub

        this.context = context;
        this.itemname = itemname;
        this.imgid = imgid;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.simple_list_item, null, true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.tvListLesson);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.imageIcon);

        txtTitle.setText(itemname[position]);
        imageView.setImageResource(imgid[position]);
        return rowView;
    }
}
