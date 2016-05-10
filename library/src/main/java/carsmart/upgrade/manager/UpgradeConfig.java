package carsmart.upgrade.manager;

import android.content.Context;

import carsmart.upgrade.manager.interceptor.DialogInterceptor;

public class UpgradeConfig {

    Context context;

    DialogInterceptor dialogInterceptor;

    UpgradeConfig() {

    }

    public static class Builder {

        UpgradeConfig config = new UpgradeConfig();

        public Builder setContext(Context context) {
            config.context = context;
            return this;
        }

        public Builder setDialogInterceptor(DialogInterceptor interceptor) {
            config.dialogInterceptor = interceptor;
            return this;
        }

        public UpgradeConfig create() {
            return config;
        }
    }

}
