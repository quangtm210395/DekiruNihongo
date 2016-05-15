package hmdq.js.codeproject.dekirunihongo;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Phan Minh Duong on 5/15/2016.
 */
public class CustomListBookAdapter extends ArrayAdapter<String> {
    private Typeface tfSm;
    private final Activity context;
    private final String[] itemname;
    private final String[] itemnameVN;
    private final Integer[] imgid;

    public CustomListBookAdapter(Activity context, String[] itemname, String[] itemnameVN, Integer[] imgid) {
        super(context, R.layout.simple_list_item_2, itemname);
        // TODO Auto-generated constructor stub
        this.context = context;
        this.itemname = itemname;
        this.itemnameVN = itemnameVN;
        this.imgid = imgid;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.simple_list_item_3, null, true);
        tfSm = Typeface.createFromAsset(context.getAssets(), "fonts/sm.ttf");

        TextView tvListBook = (TextView) rowView.findViewById(R.id.tvListBook);
        TextView tvListBookVN = (TextView) rowView.findViewById(R.id.tvListBookVN);
        ImageView imageIconBook = (ImageView) rowView.findViewById(R.id.imageIconBook);

        tvListBook.setText(itemname[position]);
        tvListBook.setTypeface(tfSm);
        tvListBookVN.setText(itemnameVN[position]);
        tvListBookVN.setTypeface(tfSm);
        imageIconBook.setImageResource(imgid[position]);
        return rowView;
    }
}
