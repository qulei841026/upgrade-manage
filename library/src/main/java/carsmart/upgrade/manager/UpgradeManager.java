package carsmart.upgrade.manager;

import android.app.Dialog;
import android.support.annotation.NonNull;

import carsmart.upgrade.manager.download.DownloadApk;

public final class UpgradeManager {

    String url = "http://pkg3.fire.im/3bb68550c4aa15fa92841de39aad36eb2133286e.apk";

    UpgradeConfig upgradeConfig;

    DownloadApk downloadApk;

    public UpgradeManager(@NonNull UpgradeConfig config) {
        this.upgradeConfig = config;
        downloadApk = new DownloadApk(config.context);
    }

    private OnConfirmUpgradeListener upgradeListener = new OnConfirmUpgradeListener() {
        @Override
        public void onUpgrade() {
            downloadApk.download(url);
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


}
