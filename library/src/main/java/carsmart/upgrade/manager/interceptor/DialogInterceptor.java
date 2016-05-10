package carsmart.upgrade.manager.interceptor;

import android.app.Dialog;

import carsmart.upgrade.manager.OnConfirmUpgradeListener;

public interface DialogInterceptor {

    Dialog intercept(String prompt, boolean isForce, OnConfirmUpgradeListener listener);
}
