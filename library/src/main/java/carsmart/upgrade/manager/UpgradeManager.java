package carsmart.upgrade.manager;

import android.app.Dialog;
import android.support.annotation.NonNull;

import carsmart.upgrade.manager.download.DownloadApk;
import carsmart.upgrade.manager.download.OnDownloadListener;

public final class UpgradeManager {

    UpgradeConfig upgradeConfig;

    DownloadApk downloadApk;

    public UpgradeManager(@NonNull UpgradeConfig config) {
        this.upgradeConfig = config;
        downloadApk = new DownloadApk(config.context)
                .setDownloadPath(config.downloadPath)
                .setAutoInstall(config.isAutoInstall);
    }

    public void setOnDownloadListener(OnDownloadListener listener) {
        downloadApk.setOnDownloadListener(listener);
    }

    private OnConfirmUpgradeListener upgradeListener = new OnConfirmUpgradeListener() {
        @Override
        public void onUpgrade(String url) {
            downloadApk.download(url);
        }
    };

    public void setUpgrade(@NonNull Upgrade upgrade) {
        Dialog dialog;

        if (upgradeConfig.dialogInterceptor == null) {
            dialog = UpgradeDialog.createDialog(upgradeConfig.context, upgrade, upgradeListener);
        } else {
            dialog = upgradeConfig.dialogInterceptor.intercept(upgrade, upgradeListener);
        }

        if (dialog != null) {
            dialog.show();
        }
    }


}
