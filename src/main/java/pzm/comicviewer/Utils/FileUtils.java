package pzm.comicviewer.Utils;

import android.graphics.Bitmap;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;

import pzm.comicviewer.Comics.CyAndHComic;
import pzm.comicviewer.Comics.DinosaurComic;
import pzm.comicviewer.Comics.IComic;
import pzm.comicviewer.Comics.PhDComic;
import pzm.comicviewer.Comics.PvPComic;
import pzm.comicviewer.Comics.SMBCComic;
import pzm.comicviewer.Comics.chainsawsuitComic;
import pzm.comicviewer.Comics.xkcdComic;

/**
 * Created by pat on 9/6/2016.
 */
public class FileUtils {


    public static String getFileName(IComic comic) {
        String filename = "";
        if (comic.getDoc() != null) {
            filename = (comic.getSource() + StaticVars.fileNameBreak + comic.getTitle().replaceAll("/", StaticVars.FILEFIX) + StaticVars.fileNameBreak + comic.getLocation().replaceAll("/", StaticVars.FILEFIX));
        } else {
            System.out.println("url" + comic.getTitle() + "title:" + comic.getTitle());
            filename = (comic.getSource() + StaticVars.fileNameBreak + comic.getTitle().replaceAll("/", StaticVars.FILEFIX) + StaticVars.fileNameBreak + comic.getUrl().replaceAll("/", StaticVars.FILEFIX));
        }
        if (filename.endsWith(StaticVars.PNG)) {
            return filename;
        }
        return filename + StaticVars.PNG;
    }

    public static IComic getComicFromUrl(String s) {
        if (s == null) {
            return null;
        }
        s = s.replaceAll(StaticVars.FILEFIX, "/");
        String[] sarr = s.split(StaticVars.fileNameBreak);
        String source = sarr[0];
        String title = sarr[1];
        String url = sarr[2];

        switch (source) {
            case StaticVars.smbcString:
                return new SMBCComic(url, title);
            case StaticVars.xkcdString:
                return new xkcdComic(url, title);
            case StaticVars.pvpString:
                return new PvPComic(url, title);
            case StaticVars.phdString:
                return new PhDComic(url, title);
            case StaticVars.DinosaurString:
                return new DinosaurComic(url, title);
            case StaticVars.chainsawsuitString:
                return new chainsawsuitComic(url, title);
            case StaticVars.cyAndHString:
                return new CyAndHComic(url, title);
            default:
        }
        return new SMBCComic(StaticVars.smbcUrl);
    }

    public static boolean isSaved(IComic comic) {
        File folder = new File(App.getContext().getFilesDir() + File.separator + "comics");

        File[] listOfFiles = folder.listFiles();


        try {
            File file = new File(App.getContext().getFilesDir() + File.separator
                    + "comics" + File.separator + getFileName(comic));
            if (file.exists()) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }

        return false;
    }


    public static IComic getBookmarkedComic(IComic currentComic) {
        File bookmarkFile = new File(App.getContext().getFilesDir(), currentComic.getSource() + StaticVars.BOOKMARKFILE);
        //bookmarkFile.delete();
        try {
            bookmarkFile.createNewFile();
            InputStream fis = new FileInputStream(bookmarkFile);
            InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
            BufferedReader br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
                return getComicFromUrl(line);
            }
            fis.close();
            isr.close();
            br.close();
        } catch (Exception e) {
        }

        return null;
    }

    public static boolean isBookmarked(IComic comic) {
        File bookmarkFile = new File(App.getContext().getFilesDir(), comic.getSource() + StaticVars.BOOKMARKFILE);
        try {
            bookmarkFile.createNewFile();
            String str = getFileName(comic);
            InputStream fis = new FileInputStream(bookmarkFile);
            InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
            BufferedReader br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
                if (line.equals(str)) {
                    return true;
                }
            }
            fis.close();
            isr.close();
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void bookmarkComic(IComic comic) {
        File bookmarkFile = new File(App.getContext().getFilesDir(), comic.getSource() + StaticVars.BOOKMARKFILE);
        FileOutputStream outstream;
        String filename = getFileName(comic);
        try {
            outstream = new FileOutputStream(bookmarkFile);
            outstream.write(filename.getBytes());
            outstream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void unBookmarkComic(IComic comic) {
        File bookmarkFile = new File(App.getContext().getFilesDir(), comic.getSource() + StaticVars.BOOKMARKFILE);
        PrintWriter writer;
        try {
            writer = new PrintWriter(bookmarkFile);
            writer.print("");
            writer.close();
        } catch (Exception e) {
        }

    }

    public static boolean isFavorite(IComic comic) {
        File lastViewed = new File(App.getContext().getFilesDir(), StaticVars.favoriteFilename);
        try {
            String str = getFileName(comic);
            InputStream fis = new FileInputStream(lastViewed);
            InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
            BufferedReader br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
                if (line.equals(str)) {
                    return true;
                }
            }
            fis.close();
            isr.close();
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void saveComic(IComic comic) {

        File file = new File(App.getContext().getFilesDir() + File.separator
                + "comics" + File.separator + getFileName(comic));
        FileOutputStream outstream;
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            outstream = new FileOutputStream(file);

            comic.getBitmap().compress(Bitmap.CompressFormat.PNG, 85, outstream);

            outstream.flush();

            outstream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void unSaveComic(IComic comic) {

        File file = new File(App.getContext().getFilesDir() + File.separator
                + "comics" + File.separator + getFileName(comic));
        if (file.exists()) {
            file.delete();
        }
    }

    public static void favoriteComic(IComic comic) {
        File favoriteFile = new File(App.getContext().getFilesDir(), StaticVars.favoriteFilename);
        FileOutputStream outstream;
        String filename = getFileName(comic);
        try {
            InputStream fis = new FileInputStream(favoriteFile);
            InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
            BufferedReader br = new BufferedReader(isr);
            String line;
            boolean shouldWrite = true;
            while ((line = br.readLine()) != null) {
                if (line.equals(filename)) {
                    shouldWrite = false;
                    break;
                }
            }
            fis.close();
            isr.close();
            br.close();
            if (shouldWrite) {
                outstream = new FileOutputStream(favoriteFile, true);
                outstream.write((filename + "\n").getBytes());
                outstream.close();
            }
            fis.close();
            isr.close();
            br.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void unFavoriteComic(IComic comic) {
        File favoriteFile = new File(App.getContext().getFilesDir(), StaticVars.favoriteFilename);
        FileOutputStream outstream;

        try {
            File lastViewedtemp = new File(App.getContext().getFilesDir(), "temp" + StaticVars.favoriteFilename);

            InputStream fis = new FileInputStream(favoriteFile);
            InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
            BufferedReader br = new BufferedReader(isr);
            String line;
            outstream = new FileOutputStream(lastViewedtemp);
            while ((line = br.readLine()) != null) {
                if (!line.equals(getFileName(comic))) {
                    outstream.write((line + "\n").getBytes());
                }
            }
            fis.close();
            isr.close();
            br.close();
            outstream.close();
            lastViewedtemp.renameTo(favoriteFile);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<IComic> getSavedComics() {
        File favoriteFile = new File(App.getContext().getFilesDir(), StaticVars.favoriteFilename);
        List<IComic> comicList = new ArrayList<>();
        File folder = new File(App.getContext().getFilesDir() + File.separator + "comics");
        File[] listOfFiles = folder.listFiles();
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                IComic comic = getComicFromUrl(listOfFiles[i].getName());
                comic.setSaved(true);
                comicList.add(comic);
            }
        }

        try {
            InputStream fis = new FileInputStream(favoriteFile);
            InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
            BufferedReader br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
                IComic comic = getComicFromUrl(line);
                if (comicList.contains(comic)) {
                    comic.setFav(true);
                    comic.setSaved(true);
                    comicList.set(comicList.indexOf(comic), comic);
                }
            }
            fis.close();
            isr.close();
            br.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return comicList;
    }

    public static List<IComic> getSavedOrFavoriteComics() {
        File favoriteFile = new File(App.getContext().getFilesDir(), StaticVars.favoriteFilename);

        List<IComic> comicList = new ArrayList<>();

        File folder = new File(App.getContext().getFilesDir() + File.separator + "comics");
        File[] listOfFiles = folder.listFiles();
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                IComic comic = getComicFromUrl(listOfFiles[i].getName());
                comic.setSaved(true);
                comicList.add(comic);
            }
        }

        try {
            InputStream fis = new FileInputStream(favoriteFile);
            InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
            BufferedReader br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
                IComic comic = getComicFromUrl(line);
                if (comicList.contains(comic)) {
                    comic.setSaved(true);
                    comic.setFav(true);
                    comicList.set(comicList.indexOf(comic), comic);
                } else {
                    comic.setFav(true);
                    comicList.add(comic);
                }
            }
            fis.close();
            isr.close();
            br.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return comicList;
    }

    public static void deleteAllSavedComics() {
        File folder = new File(App.getContext().getFilesDir() + File.separator + StaticVars.comicsDirectory);
        for (File file : folder.listFiles())
            if (!file.isDirectory())
                file.delete();
    }

    public static void deleteAllFavoriteComics() {
        try {
            PrintWriter pw = new PrintWriter(App.getContext().getFilesDir() + File.separator + StaticVars.favoriteFilename);
            pw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteAllNonFavoriteComics() {
        Set<String> favSet = new HashSet<>();
        File favoriteFile = new File(App.getContext().getFilesDir(), StaticVars.favoriteFilename);
        try {
            InputStream fis = new FileInputStream(favoriteFile);
            InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
            BufferedReader br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
                favSet.add(line);
            }
            fis.close();
            isr.close();
            br.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        File folder = new File(App.getContext().getFilesDir() + File.separator + StaticVars.comicsDirectory);
        for (File file : folder.listFiles()) {
            if (!file.isDirectory() && !favSet.contains(file.getName())) {
                file.delete();
            }
        }
    }

    public static int getMaxTextureSize() {
        // Safe minimum default size
        final int IMAGE_MAX_BITMAP_DIMENSION = 2048;

        // Get EGL Display
        EGL10 egl = (EGL10) EGLContext.getEGL();
        EGLDisplay display = egl.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);

        // Initialise
        int[] version = new int[2];
        egl.eglInitialize(display, version);

        // Query total number of configurations
        int[] totalConfigurations = new int[1];
        egl.eglGetConfigs(display, null, 0, totalConfigurations);

        // Query actual list configurations
        EGLConfig[] configurationsList = new EGLConfig[totalConfigurations[0]];
        egl.eglGetConfigs(display, configurationsList, totalConfigurations[0], totalConfigurations);

        int[] textureSize = new int[1];
        int maximumTextureSize = 0;

        // Iterate through all the configurations to located the maximum texture size
        for (int i = 0; i < totalConfigurations[0]; i++) {
            // Only need to check for width since opengl textures are always squared
            egl.eglGetConfigAttrib(display, configurationsList[i], EGL10.EGL_MAX_PBUFFER_WIDTH, textureSize);

            // Keep track of the maximum texture size
            if (maximumTextureSize < textureSize[0])
                maximumTextureSize = textureSize[0];
        }

        // Release
        egl.eglTerminate(display);

        // Return largest texture size found, or default
        return Math.max(maximumTextureSize, IMAGE_MAX_BITMAP_DIMENSION);
    }

}
