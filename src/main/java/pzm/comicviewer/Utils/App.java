package pzm.comicviewer.Utils;

import android.app.Application;
import android.content.Context;

/**
 * Created by pat on 9/10/2016.
 */
public class App extends Application {
    private static App instance;

    public static App getInstance() {
        return instance;
    }

    public static Context getContext() {
        return instance;
    }

    @Override
    public void onCreate() {
        instance = this;
        super.onCreate();
    }


}