package carsmart.upgrade.manager.interceptor;

import android.app.Dialog;

import carsmart.upgrade.manager.OnConfirmUpgradeListener;
import carsmart.upgrade.manager.Upgrade;

public interface DialogInterceptor {

    Dialog intercept(Upgrade upgrade, OnConfirmUpgradeListener listener);
}
