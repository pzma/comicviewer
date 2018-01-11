package pzm.comicviewer.Comics;

import android.graphics.Bitmap;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pzm.comicviewer.Utils.ComicEnum;
import pzm.comicviewer.Utils.StaticVars;

/**
 * Created by pat on 9/7/2016.
 */
public class xkcdComic extends IComic {


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

    public xkcdComic(String url) {
        this.comicEnum = ComicEnum.XKCD;
        this.setUrl(url);
        this.source = StaticVars.xkcdString;
    }

    public xkcdComic(String url, String title) {
        this.comicEnum = ComicEnum.XKCD;
        this.setUrl(url);
        this.setTitle(title);
        this.source = StaticVars.xkcdString;
    }

    @Override
    public ComicEnum getComicEnum() {
        return comicEnum;
    }

    @Override
    public String getLocation() {
        Element getUrl = doc.select("#middleContainer").first();
        String link = getUrl.text();
        Pattern pattern = Pattern.compile("https?://xkcd.com/\\d+/");
        Matcher matcher = pattern.matcher(link);
        if (matcher.find()) {
            return matcher.group();
        }
        return link;
    }

    @Override
    public xkcdComic getLeft() {
        try {
            Element goLeft = doc.select(".comicNav").select("ul li a").get(1);
            if (doc.location().equals("http://xkcd.com//1/")) {
                return this;
            }
            return new xkcdComic(StaticVars.xkcdUrl + goLeft.attr("href"));
        } catch (Exception e) {
            return this;
        }
    }

    @Override
    public xkcdComic getLeftMax() {
        try {
            Element goLeft = doc.select(".comicNav").select("ul li a").get(0);
            if (doc.location().equals("http://xkcd.com//1/")) {
                return this;
            }
            return new xkcdComic(StaticVars.xkcdUrl + goLeft.attr("href"));
        } catch (Exception e) {
            return this;
        }
    }

    @Override
    public xkcdComic getRightMax() {
        try {
            Element goLeft = doc.select(".comicNav").select("ul li a").get(4);
            if (doc.location().equals("http://xkcd.com//") || doc.location().equals("http://xkcd.com/")) {
                return this;
            }
            return new xkcdComic(StaticVars.xkcdUrl + goLeft.attr("href"));
        } catch (Exception e) {
            return this;
        }
    }

    @Override
    public xkcdComic getRight() {
        try {
            Element goLeft = doc.select(".comicNav").select("ul li a").get(3);
            if (goLeft.attr("href").equals("#")) {
                return this;
            }
            return new xkcdComic(StaticVars.xkcdUrl + goLeft.attr("href"));
        } catch (Exception e) {
            return this;
        }
    }

    @Override
    public xkcdComic getRandom() {
        try {
            Element goLeft = doc.select(".comicNav").select("ul li a").get(2);
            return new xkcdComic("http:" + goLeft.attr("href"));
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

