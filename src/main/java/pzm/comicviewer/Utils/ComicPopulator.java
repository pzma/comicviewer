package pzm.comicviewer.Utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import java.net.URL;

import pzm.comicviewer.ArchivedComicActivity;
import pzm.comicviewer.ComicListActivity;
import pzm.comicviewer.Comics.CyAndHComic;
import pzm.comicviewer.Comics.DinosaurComic;
import pzm.comicviewer.Comics.IComic;
import pzm.comicviewer.Comics.PhDComic;
import pzm.comicviewer.Comics.PvPComic;
import pzm.comicviewer.Comics.SMBCComic;
import pzm.comicviewer.Comics.chainsawsuitComic;
import pzm.comicviewer.Comics.xkcdComic;
import pzm.comicviewer.MainActivity;
import pzm.comicviewer.R;

/**
 * Created by pat on 9/9/2016.
 */
public class ComicPopulator {

    private Context context;
    private IComic comic, comicCopy;
    private Activity activity;
    private Document document;

    public ComicPopulator() {

    }

    public IComic populateComic(IComic comic, Context ctx, Activity activity) {
        this.context = ctx;
        this.comic = comic;
        this.comicCopy = comic;
        comicCopy.setLoaded(false);

        if (comic.getUrl().endsWith(StaticVars.PNG)) {
            comic.setUrl(comic.getUrl().substring(0, comic.getUrl().length() - 4));
        }
        this.activity = activity;
        if (comic instanceof SMBCComic) {
            AsyncTask x = new RetrieveSMBCTask().execute(comic.getUrl());
        } else if (comic instanceof xkcdComic) {
            AsyncTask x = new RetrievexkcdTask().execute(comic.getUrl());
        } else if (comic instanceof PvPComic) {
            AsyncTask x = new RetrievePvPTask().execute(comic.getUrl());
        } else if (comic instanceof PhDComic) {
            AsyncTask x = new RetrievePhDTask().execute(comic.getUrl());
        } else if (comic instanceof DinosaurComic) {
            AsyncTask x = new RetrieveDinosaurTask().execute(comic.getUrl());
        } else if (comic instanceof chainsawsuitComic) {
            AsyncTask x = new RetrievechainsawsuitTask().execute(comic.getUrl());
        } else if (comic instanceof CyAndHComic) {
            AsyncTask x = new RetrieveCyAndHTask().execute(comic.getUrl());
        }
        return this.comic;
    }

    private abstract class ComicAsyncTask extends AsyncTask<String, Void, Bitmap> {
        ProgressDialog progDailog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progDailog = new ProgressDialog(context);
            progDailog.setMessage("Loading...");
            progDailog.setIndeterminate(false);
            progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progDailog.setCancelable(false);
            progDailog.show();
        }


        @Override
        protected void onPostExecute(Bitmap bmp) {
            super.onPostExecute(bmp);
            if (activity instanceof MainActivity || activity instanceof ArchivedComicActivity) {
                try {
                    ZoomableImageView ziv = (ZoomableImageView) ((Activity) context).findViewById(R.id.imageView);
                    ziv.setImageBitmap(bmp);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (activity instanceof MainActivity) {
                    ((MainActivity) activity).updateCheckBoxes();
                } else {
                    ((ArchivedComicActivity) activity).updateCheckBoxes();
                }
            }
            progDailog.dismiss();
            if (activity instanceof ComicListActivity) {
                FileUtils.saveComic(comic);
            }
        }
    }

    private class RetrieveCyAndHTask extends ComicAsyncTask {
        protected Bitmap doInBackground(String... urls) {

            try {
                comic.setDoc(Jsoup.connect(urls[0]).get());
                Element image = comic.getDoc().select("img#main-comic").first();
                if (image == null) {
                    image = comic.getDoc().select("img#featured-comic").first();
                }

                comic.setTitle("Cy & H");
                URL url = new URL(urlify("http:" + image.attr("src")));
                Bitmap bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                bitmap = getCorrectlySizedBitmap(bitmap);

                comic.setBitmap(bitmap);
                comic.setLoaded(true);
                return bitmap;
            } catch (Exception e) {
                e.printStackTrace();
                Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.noimage);
                comic = comicCopy;
                return bmp;
            }
        }
    }

    private class RetrieveDinosaurTask extends ComicAsyncTask {
        protected Bitmap doInBackground(String... urls) {

            try {
                comic.setDoc(Jsoup.connect(urls[0]).get());
                Elements image = comic.getDoc().select("[name=twitter:image:src]");
                Elements title = comic.getDoc().select("[name=twitter:description]");

                comic.setTitle(title.attr("content"));

                URL url = new URL(urlify(image.attr("content")));
                Bitmap bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                bitmap = getCorrectlySizedBitmap(bitmap);

                comic.setBitmap(bitmap);
                comic.setLoaded(true);
                return bitmap;
            } catch (Exception e) {
                e.printStackTrace();
                Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.noimage);
                comic = comicCopy;
                return bmp;
            }
        }
    }


    private class RetrievechainsawsuitTask extends ComicAsyncTask {
        protected Bitmap doInBackground(String... urls) {

            try {
                comic.setDoc(Jsoup.connect(urls[0]).get());
                Element image = comic.getDoc().select("div#comic img").first();

                comic.setTitle(image.attr("title"));


                URL url = new URL(urlify(image.attr("src")));
                Bitmap bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                bitmap = getCorrectlySizedBitmap(bitmap);

                comic.setBitmap(bitmap);
                comic.setLoaded(true);
                return bitmap;
            } catch (Exception e) {
                e.printStackTrace();
                Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.noimage);
                comic = comicCopy;
                return bmp;
            }
        }
    }


    private class RetrievePhDTask extends ComicAsyncTask {
        protected Bitmap doInBackground(String... urls) {

            try {
                comic.setDoc(Jsoup.connect(urls[0]).get());
                Elements image = comic.getDoc().select("#comic");

                Element title = comic.getDoc().select("[name=twitter:title]").first();
                comic.setTitle(title.attr("content"));

                URL url = new URL(urlify(image.attr("src")));
                Bitmap bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                bitmap = getCorrectlySizedBitmap(bitmap);
                comic.setBitmap(bitmap);
                comic.setLoaded(true);
                return bitmap;
            } catch (Exception e) {
                e.printStackTrace();
                Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.noimage);
                comic = comicCopy;
                return bmp;
            }
        }
    }

    private class RetrievePvPTask extends ComicAsyncTask {
        protected Bitmap doInBackground(String... urls) {
            try {
                comic.setDoc(Jsoup.connect(urls[0]).get());
                Elements image = comic.getDoc().select(".comic-art img");

                comic.setTitle(image.attr("alt"));

                URL url = new URL(urlify(image.attr("src")));
                Bitmap bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                bitmap = getCorrectlySizedBitmap(bitmap);
                comic.setBitmap(bitmap);
                comic.setLoaded(true);
                return bitmap;
            } catch (Exception e) {
                e.printStackTrace();
                Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.noimage);
                comic = comicCopy;
                return bmp;
            }
        }

    }


    private class RetrievexkcdTask extends ComicAsyncTask {

        protected Bitmap doInBackground(String... urls) {
            try {
                comic.setDoc(Jsoup.connect(urls[0]).get());
                Element image = comic.getDoc().select("div#comic").first();

                Node node = image.childNode(1);

                comic.setTitle(node.attr("alt")+" *" + node.attr("title") +"*");

                URL url = new URL(urlify("http:"+node.attr("src")));
                Bitmap bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                bitmap = getCorrectlySizedBitmap(bitmap);
                comic.setBitmap(bitmap);
                comic.setLoaded(true);
                return bitmap;
            } catch (Exception e) {
                e.printStackTrace();
                Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.noimage);
                comic = comicCopy;
                return bmp;
            }
        }

    }


    private class RetrieveSMBCTask extends ComicAsyncTask {


        protected Bitmap doInBackground(String... urls) {
            try {
                comic.setDoc(Jsoup.connect(urls[0]).get());
                Elements image = comic.getDoc().select("#cc-comic");

                comic.setTitle(image.attr("title"));

                URL url = new URL(StaticVars.smbcUrl + urlify(image.attr("src")));
                Bitmap bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                bitmap = getCorrectlySizedBitmap(bitmap);

                comic.setBitmap(bitmap);
                comic.setLoaded(true);
                return bitmap;
            } catch (Exception e) {
                e.printStackTrace();
                Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.noimage);
                return bmp;
            }
        }

    }

    private String urlify(String s) {
        return s.replaceAll(" ", "%20");
    }

    private Bitmap getCorrectlySizedBitmap(Bitmap bitmap) {
        if ((bitmap.getHeight() > FileUtils.getMaxTextureSize()) || (bitmap.getWidth() > FileUtils.getMaxTextureSize())) {
            int maxDim = FileUtils.getMaxTextureSize();
            int max = Math.max(bitmap.getHeight(), bitmap.getWidth());
            float aspect_ratio = ((float) maxDim / max);
            Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, (int) (bitmap.getWidth() * aspect_ratio), (int) (bitmap.getHeight() * aspect_ratio), false);
            comic.setBitmap(resizedBitmap);
            return resizedBitmap;
        } else {
            return bitmap;
        }

    }
}
