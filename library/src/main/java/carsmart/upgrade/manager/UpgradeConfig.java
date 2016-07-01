package carsmart.upgrade.manager;

import android.content.Context;

import carsmart.upgrade.manager.download.DownloadPath;
import carsmart.upgrade.manager.interceptor.DialogInterceptor;

public class UpgradeConfig {

    Context context;

    DialogInterceptor dialogInterceptor;

    DownloadPath downloadPath;

    Boolean isAutoInstall = true;

    UpgradeConfig() {

    }

    public static class Builder {

        UpgradeConfig config;

        public Builder() {
            config = new UpgradeConfig();
        }


        public Builder setContext(Context context) {
            config.context = context;
            return this;
        }

        public Builder setDialogInterceptor(DialogInterceptor interceptor) {
            config.dialogInterceptor = interceptor;
            return this;
        }

        public Builder setAutoInstall(Boolean autoInstall) {
            config.isAutoInstall = autoInstall;
            return this;
        }

        public Builder setDownloadPath(DownloadPath downloadPath) {
            config.downloadPath = downloadPath;
            return this;
        }

        public UpgradeConfig create() {
            return config;
        }
    }

}
