package pzm.comicviewer.ListActivity;

import android.content.DialogInterface;
import android.view.View;

import pzm.comicviewer.Comics.IComic;

/**
 * Created by pat on 9/10/2016.
 */
public class CustomDialogInterfaceListener implements DialogInterface.OnClickListener {

    public View v;

    public CustomDialogInterfaceListener(View v) {
        this.v = v;
    }

    @Override
    public void onClick(DialogInterface dialog, int whichButton) {
        IComic itemToRemove = (IComic) v.getTag();

        dialog.dismiss();
    }
}
