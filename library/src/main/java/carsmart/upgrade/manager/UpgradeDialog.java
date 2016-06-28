package carsmart.upgrade.manager;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

public class UpgradeDialog {

    static Dialog createDialog(Context context, String prompt, boolean isForce,
                               final OnConfirmUpgradeListener listener) {

        if (!(context instanceof Activity)) {
            throw new RuntimeException("Context must be belong Activity");
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setMessage(prompt)
                .setCancelable(!isForce)
                .setPositiveButton(R.string.upgrade, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onUpgrade();
                    }
                });
        if (!isForce) {
            builder.setNegativeButton(R.string.cancel, null);
        }
        return builder.create();
    }
}
