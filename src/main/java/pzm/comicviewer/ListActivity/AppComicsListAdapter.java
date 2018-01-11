package pzm.comicviewer.ListActivity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import pzm.comicviewer.R;
import pzm.comicviewer.Utils.App;
import pzm.comicviewer.Utils.ComicEnum;

/**
 * Created by pat on 9/12/2016.
 */
public class AppComicsListAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final String[] urls;

    public AppComicsListAdapter(Context context, String[] values) {
        super(context, R.layout.app_comics_single_item, values);
        this.context = context;
        this.urls = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        String[] logos = ComicEnum.logos();
        String[] values = ComicEnum.sources();

        View rowView = inflater.inflate(R.layout.app_comics_single_item, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.label);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.logo);
        TextView subTextView = (TextView) rowView.findViewById(R.id.labelsub);
        textView.setText(values[position]);
        subTextView.setText(urls[position]);

        int id = App.getContext().getResources().getIdentifier(logos[position], "drawable", App.getContext().getPackageName());
        imageView.setImageResource(id);

        return rowView;
    }
}