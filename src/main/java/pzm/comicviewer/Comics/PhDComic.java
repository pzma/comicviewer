package pzm.comicviewer.Comics;

import android.graphics.Bitmap;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import pzm.comicviewer.Utils.ComicEnum;
import pzm.comicviewer.Utils.StaticVars;

/**
 * Created by pat on 9/7/2016.
 */
public class PhDComic extends IComic {

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

    public PhDComic(String url) {
        this.comicEnum = ComicEnum.PHD;
        this.setUrl(url);
        this.source = StaticVars.phdString;
    }

    public PhDComic(String url, String title) {
        this.comicEnum = ComicEnum.PHD;
        this.setUrl(url);
        this.setTitle(title);
        this.source = StaticVars.phdString;
    }

    @Override
    public ComicEnum getComicEnum() {
        return comicEnum;
    }

    @Override
    public String getLocation() {
        Elements docUrl = doc.select("[href*=archive_print.php?comicid=]");

        String link = new String("http://www.phdcomics.com/comics/archive.php?comicid=" + docUrl.first().attr("href").replaceAll("\\D+", ""));
        return link;
    }

    @Override
    public PhDComic getLeft() {
        try {
            String loc = getLocation();
            if (loc.equals("http://www.phdcomics.com/comics/archive.php?comicid=1")) {
                return this;
            }
            int num = getTrailingInteger(loc);
            num--;
            PhDComic phc = new PhDComic("http://www.phdcomics.com/comics/archive.php?comicid=" + num);
            String s = "ds";
            return phc;
        } catch (Exception e) {
            e.printStackTrace();
            return this;
        }
    }

    @Override
    public PhDComic getLeftMax() {
        try {
            if (getLocation().equals("http://www.phdcomics.com/comics/archive.php?comicid=1")) {
                return this;
            }
            return new PhDComic("http://www.phdcomics.com/comics/archive.php?comicid=1");
        } catch (Exception e) {
            return this;
        }
    }

    @Override
    public PhDComic getRightMax() {
        try {
            Elements goRight = getDoc().select("td:contains(next)");
            if (goRight.size() == 0) {
                return this;
            }
            return new PhDComic(StaticVars.phdUrl);
        } catch (Exception e) {
            return this;
        }
    }

    @Override
    public PhDComic getRight() {
        try {
            Elements goRight = getDoc().select("td:contains(next)");
            if (goRight.size() == 0) {
                return this;
            } else {
                int num = getTrailingInteger(getLocation());
                num++;
                return new PhDComic("http://www.phdcomics.com/comics/archive.php?comicid=" + num);
            }
        } catch (Exception e) {
            return this;
        }

    }

    @Override
    public PhDComic getRandom() {
        try {
            Elements goRandom = getDoc().select(".navaux");
            return new PhDComic(goRandom.attr("href"));
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

