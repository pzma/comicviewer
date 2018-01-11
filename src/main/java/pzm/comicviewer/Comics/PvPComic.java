package pzm.comicviewer.Comics;

import android.graphics.Bitmap;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import pzm.comicviewer.Utils.ComicEnum;
import pzm.comicviewer.Utils.StaticVars;

/**
 * Created by pat on 9/7/2016.
 */
public class PvPComic extends IComic {

    @Override
    public boolean isSection() {
        return false;
    }

    ComicEnum comicEnum;
    private String source;
    private Document doc;
    private String url;
    private String title;
    private boolean fav;
    private boolean saved;
    private Bitmap bitmap;

    public PvPComic(String url) {
        this.comicEnum = ComicEnum.PVP;
        this.setUrl(url);
        this.source = StaticVars.pvpString;
    }

    public PvPComic(String url, String title) {
        this.comicEnum = ComicEnum.PVP;
        this.setUrl(url);
        this.setTitle(title);
        this.source = StaticVars.pvpString;
    }

    @Override
    public ComicEnum getComicEnum() {
        return comicEnum;
    }

    @Override
    public String getLocation() {
        return getDoc().location();
    }

    @Override
    public PvPComic getLeft() {

        try {
            Elements goLeft = doc.select("div.comic-nav.left");
            Elements ele = goLeft.select(".left .divider");
            if (doc.location().equals("http://pvponline.com/comic/mon-may-04")) {
                return this;
            } else {
                return new PvPComic(StaticVars.pvpUrl + ele.attr("href"));
            }
        } catch (Exception e) {
            return this;
        }
    }

    @Override
    public PvPComic getLeftMax() {
        try {
            if (doc.location().equals("http://pvponline.com/comic/mon-may-04")) {
                return this;
            } else {
                return new PvPComic("http://pvponline.com/comic/mon-may-04");
            }
        } catch (Exception e) {
            return this;
        }
    }

    @Override
    public PvPComic getRightMax() {
        try {
            Elements goLeft = doc.select("div.comic-nav.left");
            Elements ele = goLeft.select(".left a:contains(Next)");
            if (ele.size() == 0) {
                return this;
            } else {
                return new PvPComic(StaticVars.pvpUrl);
            }
        } catch (Exception e) {
            return this;
        }
    }

    @Override
    public PvPComic getRight() {
        try {
            Elements goLeft = doc.select("div.comic-nav.left");
            Elements ele = goLeft.select(".left a:contains(Next)");

            if (ele.size() == 0) {
                return this;
            } else {
                return new PvPComic(StaticVars.pvpUrl + ele.attr("href"));
            }
        } catch (Exception e) {
            return this;
        }
    }

    @Override
    public PvPComic getRandom() {
        return this;
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

