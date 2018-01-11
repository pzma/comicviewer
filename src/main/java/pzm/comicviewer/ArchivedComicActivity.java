package pzm.comicviewer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import pzm.comicviewer.Comics.IComic;
import pzm.comicviewer.ListActivity.CustomDialogInterfaceListener;
import pzm.comicviewer.Utils.App;
import pzm.comicviewer.Utils.ComicPopulator;
import pzm.comicviewer.Utils.FileUtils;
import pzm.comicviewer.Utils.StaticVars;
import pzm.comicviewer.Utils.ZoomableImageView;

public class ArchivedComicActivity extends AppCompatActivity {

    IComic currentComic;
    ZoomableImageView ziv;
    public Toast titleToast;

    List<IComic> comicList;
    CheckBox checkBoxSave, checkBoxFav, checkBoxOpenInViewer;
    CompoundButton.OnCheckedChangeListener checkBoxSaveListener;
    CompoundButton.OnCheckedChangeListener checkBoxFavListener;
    int currentIndex;
    boolean savedOnlyMode;
    ComicPopulator comicPopulator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archived_comic);
        Bundle extras = getIntent().getExtras();
        String filename = extras.getString("file");
        boolean isFav = extras.getBoolean("isFav");
        boolean isSaved = extras.getBoolean("isSaved");
        currentComic = FileUtils.getComicFromUrl(filename);
        currentComic.setFav(isFav);
        currentComic.setSaved(isSaved);
        letsgo();
    }

    private void letsgo() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        CheckBox checkBox = (CheckBox) findViewById(R.id.checkBoxOfflineMode);
        if (preferences.getBoolean("offline_viewing", true)) {
            comicList = FileUtils.getSavedComics();
            checkBox.setChecked(true);
            savedOnlyMode = true;
        } else {
            savedOnlyMode = false;
            comicList = FileUtils.getSavedOrFavoriteComics();
            checkBox.setChecked(false);
        }
        Collections.sort(comicList);
        currentIndex = comicList.indexOf(currentComic);
        checkBox.setEnabled(false);
        titleToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);


        checkBoxOpenInViewer = (CheckBox) findViewById(R.id.checkBoxViewer);

        checkBoxSave = (CheckBox) findViewById(R.id.checkBoxSave);
        checkBoxSaveListener = new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkBoxSaveChange();
            }
        };
        checkBoxSave.setOnCheckedChangeListener(checkBoxSaveListener);

        checkBoxFav = (CheckBox) findViewById(R.id.checkBoxFav);
        checkBoxFavListener = new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkBoxFavChange();
            }
        };
        checkBoxFav.setOnCheckedChangeListener(checkBoxFavListener);
        comicPopulator = new ComicPopulator();


        ziv = (ZoomableImageView) findViewById(R.id.imageView);
        ziv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTitleToast();
            }
        });

//        File folder = new File(App.getContext().getFilesDir() + File.separator + StaticVars.comicsDirectory);
//        File[] fileList = folder.listFiles();
//        Bitmap[] bitmaps = new Bitmap[fileList.length];
//        BitmapFactory.Options options;
//        options = new BitmapFactory.Options();
//        options.inSampleSize = 2;
//        for(int i = 0; i<fileList.length; i++) {
//            System.out.println(i);
//            File thisfile = fileList[i];
//            bitmaps[i] = BitmapFactory.decodeFile(thisfile.getAbsolutePath(), options);
//            int x = fileList.length;
//            System.out.println(i);
//        }
        String s = FileUtils.getFileName(currentComic);

//        File f = new File(App.getContext().getFilesDir() + File.separator + StaticVars.comicsDirectory + File.separator + FileUtils.getFileName(currentComic));
//        boolean yxc = false;
//        String xt = "ds";
//        if(f.isFile()) {
//            yxc = true;
//        }
//
//        Bitmap bitmap = BitmapFactory.decodeFile(f.getAbsolutePath());
//        ziv.setImageBitmap(bitmap);
        updateImageView();

        updateCheckBoxes();
    }

    private void updateImageView() {
        //currentcomic is already updated at this point
        if (currentComic.isSaved()) {
            File f = new File(App.getContext().getFilesDir() + File.separator + StaticVars.comicsDirectory + File.separator + FileUtils.getFileName(currentComic));
            Bitmap bitmap = BitmapFactory.decodeFile(f.getAbsolutePath());
            ziv.setImageBitmap(bitmap);
        } else { //not saved, need to pull from web
            IComic tempComic = FileUtils.getComicFromUrl(FileUtils.getFileName(currentComic));
            tempComic.setFav(currentComic.isFav());
            tempComic.setSaved(currentComic.isSaved());
            currentComic = tempComic;
            comicPopulator.populateComic(currentComic, ArchivedComicActivity.this, this);
        }
        updateCheckBoxes();
    }

    public void updateCheckBoxes() {
        checkBoxSave.setOnCheckedChangeListener(null);
        checkBoxFav.setOnCheckedChangeListener(null);
        checkBoxOpenInViewer.setChecked(false);
        if (currentComic.isSaved()) {
            checkBoxSave.setChecked(true);
        } else {
            checkBoxSave.setChecked(false);
        }

        if (currentComic.isFav()) {
            checkBoxFav.setChecked(true);
        } else {
            checkBoxFav.setChecked(false);
        }

        checkBoxSave.setOnCheckedChangeListener(checkBoxSaveListener);
        checkBoxFav.setOnCheckedChangeListener(checkBoxFavListener);
    }

    public void checkBoxOpenInViewerChange(View v) {
        if (checkBoxOpenInViewer.isChecked()) {
            AlertDialog diaBox = AskOptionDeleteAll(v);
            diaBox.show();
        }
    }

    private AlertDialog AskOptionDeleteAll(View v) {
        AlertDialog.Builder test = new AlertDialog.Builder(this);
        test.setTitle("Open");
        test.setMessage("Open Comic in Main Viewer?");
        test.setIcon(R.drawable.backdialog);
        DialogInterface.OnClickListener x = new CustomDialogInterfaceListener(v) {
            public void onClick(DialogInterface dialog, int whichButton) {
                checkBoxOpenInViewer.setChecked(false);
                openComicInMainView();
                dialog.dismiss();
            }
        };
        test.setPositiveButton("Open", x);
        test.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                checkBoxOpenInViewer.setChecked(false);
                dialog.dismiss();
            }
        });
        return test.create();
    }

    private void openComicInMainView() {
        Intent intent = new Intent(ArchivedComicActivity.this, MainActivity.class);
        intent.putExtra("file", FileUtils.getFileName(currentComic));
        startActivity(intent);
    }

    public void checkBoxSaveChange() { //listener
        if (checkBoxSave.isChecked()) {
            FileUtils.saveComic(currentComic);
            currentComic.setSaved(true);
        } else {
            FileUtils.unSaveComic(currentComic);
            currentComic.setSaved(false);
        }
        comicList.set(currentIndex, currentComic);

    }

    public void checkBoxFavChange() { //listener
        if (checkBoxFav.isChecked()) {
            FileUtils.favoriteComic(currentComic);
            currentComic.setFav(true);
        } else {
            FileUtils.unFavoriteComic(currentComic);
            currentComic.setFav(false);
        }
        comicList.set(currentIndex, currentComic);
    }

    private void showTitleToast() {
        titleToast.setText(currentComic.getTitle());
        titleToast.show();
    }

    protected void showToastLastComic() {
        titleToast.setText("This is the latest comic");
        titleToast.show();
    }

    protected void showToastFirstComic() {
        titleToast.setText("This is the first comic");
        titleToast.show();
    }

    public boolean updateSaveList() {
        if (savedOnlyMode) {
            if (!checkBoxSave.isChecked()) {
                comicList.remove(comicList.indexOf(currentComic));
                return true;
            }
        }
        return false;
    }


    public void onClickRight(View view) {
        if (currentIndex == comicList.size() - 1) {
            showToastLastComic();
            return;
        }
        if (!updateSaveList()) {
            currentIndex++;
        }
        currentComic = comicList.get(currentIndex);
        updateImageView();
    }

    public void onClickLeft(View view) {
        if (currentIndex == 0) {
            showToastFirstComic();
            return;
        }
        updateSaveList();
        currentIndex--;
        currentComic = comicList.get(currentIndex);
        updateImageView();
    }

    public void onClickRightMax(View view) {
        if (currentIndex == comicList.size() - 1) {
            showToastLastComic();
            return;
        }
        updateSaveList();
        currentIndex = comicList.size() - 1;
        currentComic = comicList.get(currentIndex);
        updateImageView();
    }

    public void onClickLeftMax(View view) {
        if (currentIndex == 0) {
            showToastFirstComic();
            return;
        }
        updateSaveList();
        currentIndex = 0;
        currentComic = comicList.get(currentIndex);
        updateImageView();
    }

    public void onClickRandom(View view) {

        int random;
        if (comicList.size() <= 1) {
            titleToast.setText("There is only one comic saved or favorited");
            titleToast.show();
            return;
        }
        updateSaveList();
        random = new Random().nextInt(comicList.size());
        while (random == currentIndex) {
            random = new Random().nextInt(comicList.size());
        }
        currentIndex = random;
        currentComic = comicList.get(currentIndex);
        updateImageView();
    }


}
