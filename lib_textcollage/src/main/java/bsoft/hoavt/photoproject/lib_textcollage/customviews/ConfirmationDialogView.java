package bsoft.hoavt.photoproject.lib_textcollage.customviews;

import android.content.Context;
import android.support.v7.app.AlertDialog;

import bsoft.hoavt.photoproject.lib_textcollage.R;

/**
 * Created by Adm on 5/19/2017.
 */
public class ConfirmationDialogView {

    public static void showConfirmationDialog(final Context context, String title, android.content.DialogInterface.OnClickListener positiveListener) {

//        View view = LayoutInflater.from(context).inflate(R.layout.confirm_dialog, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);
        builder.setTitle(title);
//        builder.setView(view);
        builder.setPositiveButton(android.R.string.ok, positiveListener);
        builder.setNegativeButton(android.R.string.cancel, null);
        builder.show();

    }
}
