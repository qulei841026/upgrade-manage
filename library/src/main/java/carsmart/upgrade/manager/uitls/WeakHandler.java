package carsmart.upgrade.manager.uitls;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.lang.ref.WeakReference;

public abstract class WeakHandler<T> extends Handler {

    protected WeakReference<T> reference;

    public WeakHandler(Looper looper, T reference) {
        super(looper);
        this.reference = new WeakReference<>(reference);
    }

    public WeakHandler(T reference) {
        this.reference = new WeakReference<>(reference);
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        T t = reference.get();
        if (t == null)
            return;
        handleMessage(t, msg);
    }

    protected abstract void handleMessage(T t, Message message);

}
