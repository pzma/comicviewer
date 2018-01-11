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
public class chainsawsuitComic extends IComic {

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

    public chainsawsuitComic(String url) {
        this.comicEnum = ComicEnum.CHAINSAWSUIT;
        this.setUrl(url);
        this.source = StaticVars.chainsawsuitString;
    }

    public chainsawsuitComic(String url, String title) {
        this.comicEnum = ComicEnum.CHAINSAWSUIT;
        this.setUrl(url);
        this.setTitle(title);
        this.source = StaticVars.chainsawsuitString;
    }

    @Override
    public ComicEnum getComicEnum() {
        return comicEnum;
    }

    @Override
    public String getLocation() {
        Element docUrl = doc.select(".addthis_toolbox").first();
        String link = docUrl.attr("addthis:url");


        return link;
    }

    @Override
    public chainsawsuitComic getLeft() {
        try {
            String loc = getLocation();
            if (loc.equals("http://chainsawsuit.com/comic/2008/03/12/strip-338/") || loc.equals("http://chainsawsuit.com/comic/archive/2008/03/12/strip-338/")) {
                return this;
            }
            Elements goLeft = getDoc().select("a[class=hvr-shrink]:has(img[src=/images/css2015-prev.png])");
            return new chainsawsuitComic(goLeft.attr("href"));
        } catch (Exception e) {
            e.printStackTrace();
            return this;
        }
    }

    @Override
    public chainsawsuitComic getLeftMax() {
        try {
            if (getLocation().equals("http://chainsawsuit.com/comic/2008/03/12/strip-338/") || getLocation().equals("http://chainsawsuit.com/comic/archive/2008/03/12/strip-338/")) {
                return this;
            }
            return new chainsawsuitComic("http://chainsawsuit.com/comic/2008/03/12/strip-338/");
        } catch (Exception e) {
            return this;
        }
    }

    @Override
    public chainsawsuitComic getRightMax() {
        try {
            Elements goRight = getDoc().select("a[class=hvr-shrink]:has(img[src=/images/css2015-next.png])");
            if (goRight.attr("href").equals("")) {
                return this;
            } else {
                return new chainsawsuitComic(StaticVars.chainsawsuitUrl);
            }
        } catch (Exception e) {
            return this;
        }
    }

    @Override
    public chainsawsuitComic getRight() {
        try {
            Elements goRight = getDoc().select("a[class=hvr-shrink]:has(img[src=/images/css2015-next.png])");
            if (goRight.attr("href").equals("")) {
                return this;
            } else {
                return new chainsawsuitComic(goRight.attr("href"));
            }
        } catch (Exception e) {
            return this;
        }

    }

    @Override
    public chainsawsuitComic getRandom() {
        try {
            return new chainsawsuitComic("http://chainsawsuit.com/comic/random/?random&nocache=1");
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

