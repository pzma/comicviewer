package pzm.comicviewer.Comics;

import android.graphics.Bitmap;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import pzm.comicviewer.Utils.ComicEnum;
import pzm.comicviewer.Utils.StaticVars;

/**
 * Created by pat on 9/7/2016.
 */
public class SMBCComic extends IComic {

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

    public SMBCComic(String url) {
        this.comicEnum = ComicEnum.SMBC;
        this.setUrl(url);
        this.source = StaticVars.smbcString;
    }

    public SMBCComic(String url, String title) {
        this.comicEnum = ComicEnum.SMBC;
        this.setUrl(url);
        this.setTitle(title);
        this.source = StaticVars.smbcString;
    }

    @Override
    public ComicEnum getComicEnum() {
        return comicEnum;
    }

    @Override
    public String getLocation() {
        if (getDoc().location().equals(StaticVars.smbcUrl)) {
            Element getUrl = getDoc().select("#twittershare").first();
            String link = getUrl.attr("onclick");
            String link2 = "http://www.sm" + link.substring(link.indexOf("http://sm") + 9, link.indexOf("&text"));
            return link2;
        }
        return getDoc().location();
    }

    @Override
    public SMBCComic getLeft() {

        try {
            Elements goLeft = getDoc().select(".prev");
            if (goLeft.size() == 0) {
                return this;
            } else {
                return new SMBCComic(goLeft.attr("href"));
            }
        } catch (Exception e) {
            return this;
        }
    }

    @Override
    public SMBCComic getLeftMax() {
        try {
            Elements goLeft = getDoc().select(".first");
            if (goLeft.size() == 0) {
                return this;
            } else {
                return new SMBCComic(goLeft.attr("href"));
            }
        } catch (Exception e) {
            return this;
        }
    }

    @Override
    public SMBCComic getRightMax() {
        try {
            Elements goRight = getDoc().select(".last");
            if (goRight.size() == 0) {
                return this;
            } else {
                return new SMBCComic(goRight.attr("href"));
            }
        } catch (Exception e) {
            return this;
        }
    }

    @Override
    public SMBCComic getRight() {
        try {
            Elements goRight = getDoc().select(".next");
            if (goRight.size() == 0) {
                return this;
            } else {
                return new SMBCComic(goRight.attr("href"));
            }
        } catch (Exception e) {
            return this;
        }
    }

    @Override
    public SMBCComic getRandom() {
        try {
            Elements goRandom = getDoc().select(".navaux");
            return new SMBCComic(goRandom.attr("href"));
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

