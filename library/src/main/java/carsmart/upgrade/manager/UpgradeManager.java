package carsmart.upgrade.manager;

import android.app.Dialog;
import android.support.annotation.NonNull;

public final class UpgradeManager {

    UpgradeConfig upgradeConfig;

    DownloadApk downloadApk;

    public UpgradeManager(@NonNull UpgradeConfig config) {
        this.upgradeConfig = config;
        downloadApk = new DownloadApk(config.context);
    }

    private OnConfirmUpgradeListener upgradeListener = new OnConfirmUpgradeListener() {
        @Override
        public void onUpgrade() {

        }
    };

    public void setUpgrade(@NonNull Upgrade upgrade) {
        Dialog dialog;

        if (upgradeConfig.dialogInterceptor == null) {
            dialog = UpgradeDialog.createDialog(upgradeConfig.context, upgrade.prompt,
                    upgrade.isForce, upgradeListener);
        } else {
            dialog = upgradeConfig.dialogInterceptor.intercept(upgrade.prompt, upgrade.isForce,
                    upgradeListener);
        }

        if (dialog != null) {
            dialog.show();
        }
    }

//    private void upgrade() {
//        loadApk.download();
//    }

}
