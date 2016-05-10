package carsmart.upgrade.manager;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.util.Log;

public final class UpgradeManager {

    static final String TAG = "UpgradeManager";

    UpgradeConfig upgradeConfig;

    public UpgradeManager(@NonNull UpgradeConfig config) {
        this.upgradeConfig = config;
    }

    public void setUpgrade(@NonNull Upgrade upgrade) {
        Dialog dialog;
        if (upgradeConfig.dialogInterceptor == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(upgradeConfig.context)
                    .setMessage(upgrade.prompt)
                    .setCancelable(!upgrade.isForce)
                    .setPositiveButton("升级", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            upgrade();
                        }
                    });
            if (!upgrade.isForce) {
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
            }
            dialog = builder.create();
        } else {
            dialog = upgradeConfig.dialogInterceptor.intercept(upgrade.prompt, upgrade.isForce, new OnConfirmUpgradeListener() {
                @Override
                public void onUpgrade() {
                    upgrade();
                }
            });
        }

        if (dialog != null) {
            dialog.show();
        }
    }

    private void upgrade() {
        Log.d(TAG, "upgrade");
    }

}
