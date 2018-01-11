package pzm.comicviewer.Comics;

import android.graphics.Bitmap;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import pzm.comicviewer.Utils.ComicEnum;
import pzm.comicviewer.Utils.StaticVars;

/**
 * Created by pat on 9/13/2016.
 */
public class CyAndHComic extends IComic {


    @Override
    public boolean isSection() {
        return false;
    }

    private String source;
    private Document doc;
    private String url;
    private String title;
    private boolean fav;
    private boolean saved;
    private Bitmap bitmap;
    private ComicEnum comicEnum;

    public CyAndHComic(String url) {
        this.comicEnum = ComicEnum.CYANDH;
        this.setUrl(url);
        this.source = StaticVars.cyAndHString;
    }

    public CyAndHComic(String url, String title) {
        this.comicEnum = ComicEnum.CYANDH;
        this.setUrl(url);
        this.setTitle(title);
        this.source = StaticVars.cyAndHString;
    }

    @Override
    public ComicEnum getComicEnum() {
        return comicEnum;
    }

    @Override
    public String getLocation() {
        Element getUrl = doc.select("input#permalink").first();
        return getUrl.attr(("value"));
    }

    @Override
    public CyAndHComic getLeft() {
        try {
            Element goLeft = doc.select("a.previous-comic").first();
            if (doc.select("a.previous-comic.disabled").first() != null) {
                return this;
            }
            return new CyAndHComic(StaticVars.cyAndHUrl + goLeft.attr("href"));
        } catch (Exception e) {
            e.printStackTrace();
            return this;
        }
    }

    @Override
    public CyAndHComic getLeftMax() {
        try {
            Element x = doc.select("a.previous-comic .disabled").first();
            if (doc.select("a.previous-comic.disabled").first() != null) {
                return this;
            }
            return new CyAndHComic(StaticVars.cyAndHUrl + "/comics/oldest");
        } catch (Exception e) {
            return this;
        }
    }

    @Override
    public CyAndHComic getRightMax() {
        try {
            Element goRight = doc.select("a.next-comic").first();
            if (doc.select("a.next-comic.disabled").first() != null) {
                return this;
            }
            return new CyAndHComic(StaticVars.cyAndHUrl + "/comics/latest");
        } catch (Exception e) {
            e.printStackTrace();
            return this;
        }
    }

    @Override
    public CyAndHComic getRight() {
        try {
            Element goRight = doc.select("a.next-comic").first();
            if (doc.select("a.next-comic.disabled").first() != null) {
                return this;
            }
            return new CyAndHComic(StaticVars.cyAndHUrl + goRight.attr("href"));
        } catch (Exception e) {
            e.printStackTrace();
            return this;
        }
    }

    @Override
    public CyAndHComic getRandom() {
        try {
            return new CyAndHComic(StaticVars.cyAndHUrl + "/comics/random");
        } catch (Exception e) {
            return this;
        }
    }

    @Override
    public String getSource() {
        return source;
    }

    @Override
    public void setSource(String source) {
        this.source = source;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean isFav() {
        return fav;
    }

    @Override
    public void setFav(boolean fav) {
        this.fav = fav;
    }

    @Override
    public boolean isSaved() {
        return saved;
    }

    @Override
    public void setSaved(boolean saved) {
        this.saved = saved;
    }


    @Override
    public Document getDoc() {
        return doc;
    }

    @Override
    public void setDoc(Document doc) {
        this.doc = doc;
    }

    @Override
    public Bitmap getBitmap() {
        return bitmap;
    }

    @Override
    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
