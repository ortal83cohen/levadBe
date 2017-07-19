package com.query.social.app.service;

import android.arch.lifecycle.ViewModelProviders;
import android.net.Uri;

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

            List<String> groups = data.getQueryParameters("groupid");
            if (!groups.isEmpty()) {
                userViewModel.addToGroup(groups);
            }


        }
    }
}
