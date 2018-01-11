package pzm.comicviewer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import pzm.comicviewer.Comics.IComic;
import pzm.comicviewer.ListActivity.ComicListViewArrayAdapter;
import pzm.comicviewer.ListActivity.CustomDialogInterfaceListener;
import pzm.comicviewer.ListActivity.GroupItem;
import pzm.comicviewer.Utils.AnimatedExpandableListView;
import pzm.comicviewer.Utils.ComicPopulator;
import pzm.comicviewer.Utils.FileUtils;

public class ComicListActivity extends Activity {

    private AnimatedExpandableListView listView;
    private ComicListViewArrayAdapter adapter;
    SharedPreferences preferences;
    CheckBox savedOnlyMode;

    @Override
    protected void onResume() {
        super.onResume();
        populateList();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comic_list);
        populateList();
    }

    private void populateList() {
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        savedOnlyMode = (CheckBox) findViewById(R.id.checkBoxOfflineMode);

        if (preferences.getBoolean("offline_viewing", true)) {
            savedOnlyMode.setChecked(true);
        } else {
            savedOnlyMode.setChecked(false);
        }
        savedOnlyMode.setEnabled(true);


        List<IComic> comicList = FileUtils.getSavedOrFavoriteComics();
        Collections.sort(comicList);

        List<GroupItem> items = new ArrayList<>();
        for (int x = 0; x < comicList.size(); x++) {

            List<IComic> groupList = new ArrayList<>();
            groupList.add(comicList.get(x));
            GroupItem item = new GroupItem();
            item.title = comicList.get(x).getSource();

            for (int y = x + 1; y < comicList.size(); y++) {
                if (comicList.get(y).getSource().equals(comicList.get(x).getSource())) {
                    groupList.add(comicList.get(y));
                } else {
                    break;
                }
                x = y;
            }
            item.items = groupList;
            items.add(item);
        }


        adapter = new ComicListViewArrayAdapter(this);
        adapter.setData(items);

        listView = (AnimatedExpandableListView) findViewById(R.id.list);
        listView.setAdapter(adapter);

        listView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {

                if (listView.isGroupExpanded(groupPosition)) {
                    listView.collapseGroupWithAnimation(groupPosition);
                } else {
                    listView.expandGroupWithAnimation(groupPosition);
                }
                return true;
            }
        });
    }

    public void openComic(View v) {
        IComic comic = (IComic) v.getTag();
        if (savedOnlyMode.isChecked() && !comic.isSaved()) {
            Toast.makeText(this, "Offline Viewing only allows viewing of saved comics.", Toast.LENGTH_SHORT).show();
            return;
        }
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("offline_viewing", savedOnlyMode.isChecked());
        editor.commit();

        Intent intent = new Intent(ComicListActivity.this, ArchivedComicActivity.class);
        intent.putExtra("file", FileUtils.getFileName(comic));
        intent.putExtra("isFav", comic.isFav());
        intent.putExtra("isSaved", comic.isSaved());
        startActivity(intent);
    }


    public void deleteSingleSavedComic(View v) {
        IComic comic = (IComic) v.getTag();
        if (comic.isFav()) {
            comic.setFav(false);
            FileUtils.unFavoriteComic(comic);
        }
        if (comic.isSaved()) {
            comic.setSaved(false);
            FileUtils.unSaveComic(comic);
        }
        adapter.notifyDataSetChanged();
    }

    public void favComicUpdateStatus(View v) {
        IComic comic = (IComic) v.getTag();
        if (((CheckBox) v).isChecked()) {
            comic.setFav(true);
            FileUtils.favoriteComic(comic);
        } else {
            comic.setFav(false);
            FileUtils.unFavoriteComic(comic);
        }
    }

    public void saveComicUpdateStatus(View v) {
        IComic comic = (IComic) v.getTag();

        if (((CheckBox) v).isChecked()) {
            comic.setSaved(true);
            ComicPopulator cp = new ComicPopulator();
            cp.populateComic(comic, ComicListActivity.this, this);
        } else {
            comic.setSaved(false);
            FileUtils.unSaveComic(comic);
        }
    }


    private AlertDialog AskOptionDeleteAll(View v) {
        AlertDialog.Builder test = new AlertDialog.Builder(this);
        test.setTitle("Delete");
        test.setMessage("Delete ALL Saved Conversations");
        test.setIcon(R.drawable.deletedialog);
        DialogInterface.OnClickListener x = new CustomDialogInterfaceListener(v) {
            public void onClick(DialogInterface dialog, int whichButton) {
                deleteAllConvos();
                dialog.dismiss();
            }
        };
        test.setPositiveButton("Delete", x);
        test.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();

            }
        });
        return test.create();
    }

    private void deleteAllConvos() {
        FileUtils.deleteAllSavedComics();
        FileUtils.deleteAllFavoriteComics();
        adapter.setData(new ArrayList<GroupItem>());
        adapter.notifyDataSetChanged();
    }


    public void deleteAll(View v) {
        AlertDialog diaBox = AskOptionDeleteAll(v);
        diaBox.show();
    }

    private AlertDialog AskOptionDeleteNonFav(View v) {

        AlertDialog.Builder test = new AlertDialog.Builder(this);
        test.setTitle("Delete");
        test.setMessage("Delete all Non-Favorite Saved Comics");
        test.setIcon(R.drawable.deletedialog);
        DialogInterface.OnClickListener x = new CustomDialogInterfaceListener(v) {
            public void onClick(DialogInterface dialog, int whichButton) {
                deleteNonFavConvos();
                dialog.dismiss();
            }
        };
        test.setPositiveButton("Delete", x);
        test.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();

            }
        });
        return test.create();
    }


    private void deleteNonFavConvos() {
        FileUtils.deleteAllNonFavoriteComics();
        populateList();
        adapter.notifyDataSetChanged();
    }

    public void deleteNonFav(View v) {
        AlertDialog diaBox = AskOptionDeleteNonFav(v);
        diaBox.show();
    }


}
