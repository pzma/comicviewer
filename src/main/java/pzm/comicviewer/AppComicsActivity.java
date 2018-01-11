package pzm.comicviewer;

import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import pzm.comicviewer.ListActivity.AppComicsListAdapter;
import pzm.comicviewer.Utils.ComicEnum;

public class AppComicsActivity extends ListActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setListAdapter(new AppComicsListAdapter(this, ComicEnum.urls()));
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        String selectedValue = (String) getListAdapter().getItem(position);
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(selectedValue));
        startActivity(browserIntent);
    }
}
