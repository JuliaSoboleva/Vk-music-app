package com.soboleva.vkmusicloader.ui.fragments;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;
import com.afollestad.materialdialogs.MaterialDialog;
import com.soboleva.vkmusicloader.R;
import timber.log.Timber;


public class AboutDialog extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Context context = getActivity();

        TextView contentView = new TextView(context);
//        contentView.setLinksClickable(true);
        contentView.setText(/*context.getString(R.string.dialog_content_about) + " " +*/
                Html.fromHtml(context.getString(R.string.dialog_content_about) + " " +
                        "<a href=\"mailto:julia.soboleva26@gmail.com\">julia.soboleva26@gmail.com</a>"));
        contentView.setMovementMethod(LinkMovementMethod.getInstance());

        Dialog res = new MaterialDialog.Builder(getActivity())
                .title(R.string.dialog_title_about)
//                .content(content)
                .positiveText(android.R.string.ok)
                .positiveColor(getResources().getColor(R.color.colorPrimary))
                .neutralText(context.getString(R.string.rate))
                .neutralColor(getResources().getColor(R.color.colorPrimary))
                .titleColor(getResources().getColor(R.color.colorPrimary))
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onNeutral(MaterialDialog dialog) {
                        Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
                        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET |
                                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                        try {
                            startActivity(goToMarket);
                            Timber.d("startActivity(goToMarket);");
                        } catch (ActivityNotFoundException e) {
                            Timber.d("ActivityNotFoundException");
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id="
                                    + context.getPackageName())));
                        }
                        super.onNeutral(dialog);
                    }
                })
                .customView(contentView, true)
                .build();


        return res;


    }

    public void show(FragmentActivity activity) {
        show(activity.getSupportFragmentManager(), AboutDialog.class.getName());
    }
}
