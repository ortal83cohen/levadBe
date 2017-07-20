package com.query.social.app.service;

import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AlertDialog;

import com.query.social.app.ui.BaseActivity;
import com.query.social.app.viewmodel.UserViewModel;

import java.util.List;

/**
 * Created by Ortal Cohen on 19/7/2017.
 */

public class Router {
    public static void handel(BaseActivity activity, Uri data) {
        if (data != null) {
            UserViewModel userViewModel = ViewModelProviders.of(activity).get(UserViewModel.class);
            userViewModel.init(activity);
            List<String> groups = data.getQueryParameters("groupid");
            if (!groups.isEmpty()) {
                userViewModel.addToGroup(groups);
            }
            AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(activity, android.R.style.Theme_Material_Dialog_Alert);
            } else {
                builder = new AlertDialog.Builder(activity);
            }
            builder.setTitle("הוספת לקבוצה")
                    .setMessage(" שם הקבוצה " + groups.get(0))
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with delete
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();

        }
    }
}
