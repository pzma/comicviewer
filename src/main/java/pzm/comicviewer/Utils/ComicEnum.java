package pzm.comicviewer.Utils;

import java.util.NoSuchElementException;

/**
 * Created by pat on 9/10/2016.
 */
public enum ComicEnum {
    PVP("PvP", "http://pvponline.com/comic", "logopvp"),
    SMBC("SMBC", "http://www.smbc-comics.com/", "logosmbc"),
    XKCD("xkcd", "http://xkcd.com/", "logoxkcd"),
    PHD("PhD", "http://www.phdcomics.com/comics.php", "logophd"),
    DINOSAUR("Dinosaur", "http://www.qwantz.com/", "logodinosaur"),
    CHAINSAWSUIT("chainsawsuit", "http://chainsawsuit.com/", "logochainsawsuit"),
    CYANDH("CyAndH", "http://explosm.net/", "logocyandh");

    private ComicEnum(String source, String baseUrl, String logoPath) {
        this.source = source;
        this.baseUrl = baseUrl;
        this.logoPath = logoPath;
    }

    private final String source;
    private final String baseUrl;
    private final String logoPath;

    public String getSource() {
        return this.source;
    }

    public String getBaseUrl() {
        return this.baseUrl;
    }

    public String getLogoPath() {
        return this.logoPath;
    }

    public static ComicEnum getComicEnum(String source) {
        if (source.toLowerCase().equals("xkcd")) {
            return ComicEnum.XKCD;
        } else if (source.equals("PvP")) {
            return ComicEnum.PVP;
        } else if (source.equals("SMBC")) {
            return ComicEnum.SMBC;
        } else if (source.equals("PhD")) {
            return ComicEnum.PHD;
        } else if (source.equals("Dinosaur")) {
            return ComicEnum.DINOSAUR;
        } else if (source.equals("chainsawsuit")) {
            return ComicEnum.CHAINSAWSUIT;
        } else if (source.equals("CyAndH")) {
            return ComicEnum.CYANDH;
        }
        throw new NoSuchElementException();
    }

    public static String[] sources() {
        String[] sources = new String[ComicEnum.values().length];
        for (int i = 0; i < ComicEnum.values().length; i++) {
            sources[i] = ComicEnum.values()[i].getSource();
        }
        return sources;
    }

    public static String[] logos() {
        String[] logos = new String[ComicEnum.values().length];
        for (int i = 0; i < ComicEnum.values().length; i++) {
            logos[i] = ComicEnum.values()[i].getLogoPath();
        }
        return logos;
    }

    public static String[] urls() {
        String[] urls = new String[ComicEnum.values().length];
        for (int i = 0; i < ComicEnum.values().length; i++) {
            urls[i] = ComicEnum.values()[i].getBaseUrl();
        }
        return urls;
    }

}
