package pzm.comicviewer.Comics;

import android.graphics.Bitmap;

import org.jsoup.nodes.Document;

import pzm.comicviewer.Utils.ComicEnum;
import pzm.comicviewer.Utils.FileUtils;

/**
 * Created by pat on 9/9/2016.
 */
public abstract class IComic implements Comparable<IComic> {

    public boolean loaded = true;

    public boolean isLoaded(){
        return this.loaded;
    }

    public void setLoaded(boolean n) {
        this.loaded = n;
    }

    abstract public ComicEnum getComicEnum();

    abstract public boolean isSection();

    abstract public String getLocation();

    abstract public IComic getLeft();

    abstract public IComic getLeftMax();

    abstract public IComic getRightMax();

    abstract public IComic getRight();

    abstract public IComic getRandom();

    abstract public String getSource();

    abstract public void setSource(String source);

    abstract public String getUrl();

    abstract public void setUrl(String url);

    abstract public String getTitle();

    abstract public void setTitle(String title);

    abstract public boolean isFav();

    abstract public void setFav(boolean fav);

    abstract public boolean isSaved();

    abstract public void setSaved(boolean saved);

    abstract public Document getDoc();

    abstract public void setDoc(Document doc);

    abstract public Bitmap getBitmap();

    abstract public void setBitmap(Bitmap bitmap);

    public int compareTo(IComic i) {
        if (this instanceof xkcdComic && i instanceof xkcdComic && this.getUrl() != null && i.getUrl() != null) {
            return Integer.valueOf(this.getUrl().replaceAll("\\D+", "")).compareTo(Integer.valueOf(i.getUrl().replaceAll("\\D+", "")));
        }
        if (this instanceof CyAndHComic && i instanceof CyAndHComic && this.getUrl() != null && i.getUrl() != null) {
            return Integer.valueOf(this.getUrl().replaceAll("\\D+", "")).compareTo(Integer.valueOf(i.getUrl().replaceAll("\\D+", "")));
        }
        if (this instanceof DinosaurComic && i instanceof DinosaurComic && this.getUrl() != null && i.getUrl() != null) {
            return Integer.valueOf(this.getUrl().replaceAll("\\D+", "")).compareTo(Integer.valueOf(i.getUrl().replaceAll("\\D+", "")));
        }
        return FileUtils.getFileName(this).compareTo(FileUtils.getFileName(i));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!IComic.class.isAssignableFrom(obj.getClass())) {
            return false;
        }
        final IComic other = (IComic) obj;
        if(!this.isLoaded() || !other.isLoaded()) {
            return false;
        }
        if (this.getSource().equals(other.getSource()) && this.getUrl().equals(other.getUrl())) {
            return true;
        }
        return false;
    }

    protected int getTrailingInteger(String str) {
        int positionOfLastDigit = getPositionOfLastDigit(str);
        if (positionOfLastDigit == str.length()) {
            // string does not end in digits
            return -1;
        }
        return Integer.parseInt(str.substring(positionOfLastDigit));
    }

    protected int getPositionOfLastDigit(String str) {
        int pos;
        for (pos = str.length() - 1; pos >= 0; --pos) {
            char c = str.charAt(pos);
            if (!Character.isDigit(c)) break;
        }
        return pos + 1;
    }
}
