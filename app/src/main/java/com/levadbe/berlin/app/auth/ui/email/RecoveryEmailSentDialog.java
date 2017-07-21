package com.levadbe.berlin.app.auth.ui.email;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;

import com.levadbe.berlin.app.R;
import com.levadbe.berlin.app.auth.ResultCodes;
import com.levadbe.berlin.app.auth.ui.DialogBase;
import com.levadbe.berlin.app.auth.ui.ExtraConstants;

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
public class RecoveryEmailSentDialog extends DialogBase {
    private static final String TAG = "RecoveryEmailSentDialog";

    public static void show(String email, FragmentManager manager) {
        RecoveryEmailSentDialog result = new RecoveryEmailSentDialog();
        Bundle bundle = new Bundle();
        bundle.putString(ExtraConstants.EXTRA_EMAIL, email);
        result.setArguments(bundle);
        result.show(manager, TAG);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getContext())
                .setTitle(R.string.title_confirm_recover_password)
                .setMessage(getString(R.string.confirm_recovery_body,
                        getArguments().getString(ExtraConstants.EXTRA_EMAIL)))
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface anInterface) {
                        finish(ResultCodes.OK, new Intent());
                    }
                })
                .setPositiveButton(android.R.string.ok, null)
                .show();
    }
}
