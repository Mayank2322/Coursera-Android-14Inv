package android.cousera.modernartui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;

/**
 * Created by Kadete on 02/11/2014.
 */
public class AlertDialogFragment extends DialogFragment {
    private final static String moma_url = "http://www.moma.org/";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                // Set Dialog Icon
                .setIcon(R.drawable.moma_logo)
                // Set Dialog Title
                .setTitle(Html.fromHtml("<b>" + "Museum of Modern Art" + "</b>"))
                // Set Dialog Message
                .setMessage(Html.fromHtml("Inpired by the works of artists such as " +
                        "<b>Piet Mondrien</b>" + " and " + "<b>Ben Nicholson</b>.<br/>" +
                        "Click below to learn more!"))

                // Positive button
                .setPositiveButton("Visit MOMA", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(moma_url));
                        startActivity(myIntent);
                    }
                })

                // Negative Button
                .setNegativeButton("Not Now", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Do Nothing
                    }
                }).create();
    }
}