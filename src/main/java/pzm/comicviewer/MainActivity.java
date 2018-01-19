package pzm.comicviewer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;

import pzm.comicviewer.Comics.CyAndHComic;
import pzm.comicviewer.Comics.DinosaurComic;
import pzm.comicviewer.Comics.IComic;
import pzm.comicviewer.Comics.PhDComic;
import pzm.comicviewer.Comics.PvPComic;
import pzm.comicviewer.Comics.SMBCComic;
import pzm.comicviewer.Comics.chainsawsuitComic;
import pzm.comicviewer.Comics.xkcdComic;
import pzm.comicviewer.Utils.App;
import pzm.comicviewer.Utils.ComicPopulator;
import pzm.comicviewer.Utils.FileUtils;
import pzm.comicviewer.Utils.StaticVars;
import pzm.comicviewer.Utils.ZoomableImageView;

public class MainActivity extends AppCompatActivity {

    public ZoomableImageView ziv;
    String currentPage;
    String currentTitle;
    Document doc;
    public Toast titleToast;
    private SharedPreferences preferences;
    Spinner spinner;
    Bitmap currentImage;
    CheckBox checkBoxSave, checkBoxFav, bookmarkCheckbox;
    public ImageButton buttonLeft, buttonRight, buttonLeftMax, buttonRightMax, buttonRandom;
    CompoundButton.OnCheckedChangeListener checkBoxSaveListener;
    CompoundButton.OnCheckedChangeListener checkBoxFavListener;
    ComicPopulator comicPopulator;

    IComic currentComic, previousComic;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        Intent intent;

        switch (item.getItemId()) {
            case R.id.preferences:
                intent = new Intent(MainActivity.this, AppPreferenceActivity.class);
                startActivity(intent);
                return true;
            case R.id.comicList:
                intent = new Intent(MainActivity.this, ComicListActivity.class);
                startActivity(intent);
                return true;
            case R.id.feedback:
                intent = new Intent(MainActivity.this, FeedbackActivity.class);
                startActivity(intent);
                return true;
            //case R.id.saved:
            //intent = new Intent(MainActivity.this, ComicSavedActivity.class);
            //startActivity(intent);
            //return true;
            case R.id.comics:
                intent = new Intent(MainActivity.this, AppComicsActivity.class);
                startActivity(intent);
                return true;
            case R.id.help:
                intent = new Intent(MainActivity.this, HelpActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateCheckBoxes();
    }

    @Override
    protected void onPause() {

        File lastViewed = new File(getApplicationContext().getFilesDir(), StaticVars.lastViewedFilename);
        FileOutputStream outstream;
        try {
            outstream = new FileOutputStream(lastViewed);
            String xd = currentComic.getSource() + "::" + currentComic.getTitle() + "::" + currentComic.getLocation();
            outstream.write((currentComic.getSource() + "::" + currentComic.getTitle() + "::" + currentComic.getLocation()).getBytes());
            outstream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        spinner = ((Spinner) findViewById(R.id.spinner));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                comicSpinnerChangeListener();
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });
        File folder = new File(getApplicationContext().getFilesDir() + File.separator + StaticVars.comicsDirectory);
        if (!folder.exists()) {
            folder.mkdir();
        }

        buttonLeftMax = (ImageButton) findViewById(R.id.buttonLeftMax);
        buttonLeft = (ImageButton) findViewById(R.id.buttonLeft);
        buttonRandom = (ImageButton) findViewById(R.id.buttonRandom);
        buttonRight = (ImageButton) findViewById(R.id.buttonRight);
        buttonRightMax = (ImageButton) findViewById(R.id.buttonRightMax);

        titleToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);


        File folsder = new File(getApplicationContext().getFilesDir() + File.separator + StaticVars.comicsDirectory);
        for (File f : folsder.listFiles()) {
            //f.delete();
        }


        File favFile = new File(getApplicationContext().getFilesDir() + File.separator + StaticVars.favoriteFilename);
        try {
            //favFile.delete();
            favFile.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
        ziv = (ZoomableImageView) findViewById(R.id.imageView);
        ziv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,
                        currentComic.getTitle(),
                        Toast.LENGTH_LONG).show();
            }
        });

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
        bookmarkCheckbox = (CheckBox) findViewById(R.id.bookmarkCheckbox);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setLogo(R.mipmap.comiclogo);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String filename = extras.getString("file");
            if (filename != null) {
                currentComic = FileUtils.getComicFromUrl(filename.substring(0, filename.length() - 4));
            }
        }


        if (currentComic == null) {
            BufferedReader br;
            File lastViewed = new File(getApplicationContext().getFilesDir(), StaticVars.lastViewedFilename);
            try {
                if (!lastViewed.isFile() && !lastViewed.createNewFile()) {

                } else {
                    br = new BufferedReader(new FileReader(getApplicationContext().getFilesDir() + File.separator + StaticVars.lastViewedFilename));
                    if (preferences.getBoolean("remember_position_preference", true)) {
                        String s = br.readLine();
                        if (s != null) {
                            currentComic = FileUtils.getComicFromUrl(s);
                        }
                    }
                    br.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this, "There was an error retrieving your last viewed comic", Toast.LENGTH_SHORT).show();
            }
        }
        if (currentComic == null) {
            currentComic = new SMBCComic(StaticVars.smbcUrl);
        }

        ziv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTitleToast();
            }
        });

        spinner.setSelection(((ArrayAdapter) spinner.getAdapter()).getPosition(currentComic.getSource()));


        comicPopulator = new ComicPopulator();
        comicPopulator.populateComic(currentComic, MainActivity.this, this);
        setUpButtons();
        updateCheckBoxes();

    }



    protected void goToBookmark(View v) {
        IComic bookmarkedComic = FileUtils.getBookmarkedComic(currentComic);
        if (bookmarkedComic == null) {
            titleToast.setText("No bookmarked comic found");
            titleToast.show();
            return;
        }
        previousComic = currentComic;
        currentComic = bookmarkedComic;
        comicPopulator.populateComic(currentComic, MainActivity.this, this);
    }

    public void bookmarkComic(View v) {
        if (!bookmarkCheckbox.isChecked()) {
            FileUtils.unBookmarkComic(currentComic);
            return;
        }
        FileUtils.bookmarkComic(currentComic);
        bookmarkCheckbox.setChecked(true);

    }

    private void showTitleToast() {
        titleToast.setText(currentComic.getTitle());
        titleToast.show();
    }

    protected void goToPrevious(View v) {
        if (previousComic == null) {
            titleToast.setText("No previous comic");
            titleToast.show();
            return;
        }
        IComic tempComic = previousComic;
        previousComic = currentComic;
        currentComic = tempComic;
        previousComic.setUrl(previousComic.getLocation());
        spinner.setSelection(((ArrayAdapter) spinner.getAdapter()).getPosition(currentComic.getSource()));
        currentComic.setUrl(currentComic.getLocation());
        currentComic = comicPopulator.populateComic(currentComic, MainActivity.this, this);
        setUpButtons();
    }


    private void comicSpinnerChangeListener() {
        String s = spinner.getItemAtPosition(spinner.getSelectedItemPosition()).toString();
        if (s.equals(currentComic.getSource())) {
            return;
        }
        previousComic = currentComic;
        previousComic.setUrl(previousComic.getLocation());

        //TODO: switch to enums
        switch (s) {
            case StaticVars.smbcString:
                currentComic = new SMBCComic(StaticVars.smbcUrl);
                break;
            case StaticVars.xkcdString:
                currentComic = new xkcdComic(StaticVars.xkcdUrl);
                break;
            case StaticVars.pvpString:
                currentComic = new PvPComic(StaticVars.pvpUrl);
                break;
            case StaticVars.phdString:
                currentComic = new PhDComic(StaticVars.phdUrl);
                break;
            case StaticVars.DinosaurString:
                currentComic = new DinosaurComic(StaticVars.DinosaurUrl);
                break;
            case StaticVars.chainsawsuitString:
                currentComic = new chainsawsuitComic(StaticVars.chainsawsuitUrl);
                break;
            case StaticVars.cyAndHString:
                currentComic = new CyAndHComic(StaticVars.cyAndHUrl);
                break;
        }
        setUpButtons();
        comicPopulator.populateComic(currentComic, MainActivity.this, this);
    }

    protected void setUpButtons() {
        if (currentComic instanceof PvPComic || currentComic instanceof PhDComic || currentComic instanceof DinosaurComic) {
            buttonRandom.setEnabled(false);
        } else {
            buttonRandom.setEnabled(true);
        }

    }


    public void updateCheckBoxes() {
        if(!currentComic.isLoaded()) {
            checkBoxSave.setEnabled(false);
            checkBoxFav.setEnabled(false);
            bookmarkCheckbox.setEnabled(false);
        } else {
            checkBoxFav.setEnabled(true);
            checkBoxSave.setEnabled(true);
            bookmarkCheckbox.setEnabled(true);
        }
        checkBoxSave.setOnCheckedChangeListener(null);
        checkBoxFav.setOnCheckedChangeListener(null);

        if (FileUtils.isBookmarked(currentComic)) {
            bookmarkCheckbox.setChecked(true);
        } else {
            bookmarkCheckbox.setChecked(false);
        }

        if (FileUtils.isSaved(currentComic)) {
            checkBoxSave.setChecked(true);
        } else {
            checkBoxSave.setChecked(false);
        }

        if (FileUtils.isFavorite(currentComic)) {
            checkBoxFav.setChecked(true);
        } else {
            checkBoxFav.setChecked(false);
        }

        checkBoxSave.setOnCheckedChangeListener(checkBoxSaveListener);
        checkBoxFav.setOnCheckedChangeListener(checkBoxFavListener);
    }


    public void onClickRight(View view) {
        if (currentComic.equals(currentComic.getRight())) {
            showToastLastComic();
            return;
        }
        previousComic = currentComic;
        currentComic = comicPopulator.populateComic(currentComic.getRight(), MainActivity.this, this);
    }

    public void onClickLeft(View view) {
        if (currentComic.equals(currentComic.getLeft())) {
            showToastFirstComic();
            return;
        }
        previousComic = currentComic;
        currentComic = comicPopulator.populateComic(currentComic.getLeft(), MainActivity.this, this);
    }

    public void onClickRightMax(View view) {
        if (currentComic.equals(currentComic.getRight())) {
            showToastLastComic();
            return;
        }
        previousComic = currentComic;
        currentComic = comicPopulator.populateComic(currentComic.getRightMax(), MainActivity.this, this);
    }

    public void onClickLeftMax(View view) {
        if (currentComic.equals(currentComic.getLeft())) {
            showToastFirstComic();
            return;
        }
        previousComic = currentComic;
        currentComic = comicPopulator.populateComic(currentComic.getLeftMax(), MainActivity.this, this);
    }

    public void onClickRandom(View view) {
        previousComic = currentComic;
        currentComic = comicPopulator.populateComic(currentComic.getRandom(), MainActivity.this, this);
    }

    public void checkBoxSaveChange() { //listener
        if (checkBoxSave.isChecked() && !FileUtils.isSaved(currentComic)) {
            FileUtils.saveComic(currentComic);
        } else {
            FileUtils.unSaveComic(currentComic);
        }
    }

    public void checkBoxFavChange() { //listener
        if (checkBoxFav.isChecked()) {
            FileUtils.favoriteComic(currentComic);
            if (preferences.getBoolean("autosave_checkbox_preference", true)) {
                checkBoxSave.setChecked(true);
            }
        } else {
            FileUtils.unFavoriteComic(currentComic);
        }
    }

    protected void showToastLastComic() {
        titleToast.setText("This is the latest comic");
        titleToast.show();
    }

    protected void showToastFirstComic() {
        titleToast.setText("This is the first comic");
        titleToast.show();
    }


}
