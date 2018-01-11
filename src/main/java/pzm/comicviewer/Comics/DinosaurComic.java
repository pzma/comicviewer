package pzm.comicviewer.Comics;

import android.graphics.Bitmap;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import pzm.comicviewer.Utils.ComicEnum;
import pzm.comicviewer.Utils.StaticVars;

/**
 * Created by pat on 9/7/2016.
 */
public class DinosaurComic extends IComic {


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

    public DinosaurComic(String url) {
        this.comicEnum = ComicEnum.DINOSAUR;
        this.setUrl(url);
        this.source = StaticVars.DinosaurString;
    }

    public DinosaurComic(String url, String title) {
        this.comicEnum = ComicEnum.DINOSAUR;
        this.setUrl(url);
        this.setTitle(title);
        this.source = StaticVars.DinosaurString;
    }

    @Override
    public ComicEnum getComicEnum() {
        return comicEnum;
    }

    @Override
    public String getLocation() {
        Element getUrl = doc.select("[property=og:url]").first();
        return getUrl.attr("content");
    }

    @Override
    public DinosaurComic getLeft() {
        try {
            String loc = getLocation();
            if (loc.equals("http://www.qwantz.com/index.php?comic=1")) {
                return this;
            }
            int num = getTrailingInteger(loc);
            num--;
            return new DinosaurComic("http://www.qwantz.com/index.php?comic=" + num);
        } catch (Exception e) {
            return this;
        }
    }


    @Override
    public DinosaurComic getLeftMax() {
        try {
            if (getLocation().equals("http://www.qwantz.com/index.php?comic=1")) {
                return this;
            }
            return new DinosaurComic("http://www.qwantz.com/index.php?comic=1");
        } catch (Exception e) {
            return this;
        }
    }

    @Override
    public DinosaurComic getRightMax() {
        try {
            Element element = doc.select("a[rel=next]").first();
            if (element == null) {
                return this;
            }
            return new DinosaurComic(StaticVars.DinosaurUrl);
        } catch (Exception e) {
            return this;
        }
    }

    @Override
    public DinosaurComic getRight() {
        try {
            String loc = getLocation();
            Element element = doc.select("a[rel=next]").first();
            if (element == null) {
                return this;
            }
            int num = getTrailingInteger(loc);
            num++;
            return new DinosaurComic("http://www.qwantz.com/index.php?comic=" + num);
        } catch (Exception e) {
            return this;
        }
    }

    @Override
    public DinosaurComic getRandom() {
        return null;
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

