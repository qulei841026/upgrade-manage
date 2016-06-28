package carsmart.upgrade.manager;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;

public class DownloadApk {

    String url = "http://pkg.fir.im/3bb68550c4aa15fa92841de39aad36eb2133286e.apk?attname=driving.behavior.analysis.apk_1.0.apk";

    private DownloadManager dm;

    private long downloadId = 0;

    private Context mContext;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            if (downloadId == id) {

            }
        }
    };

    public DownloadApk(Context context) {
        mContext = context;
        dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
    }

    public void download() {
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        long downloadId = dm.enqueue(request);

    }


    private void register() {
        IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        mContext.registerReceiver(receiver, filter);
    }

    private void unregister() {
        mContext.unregisterReceiver(receiver);
    }

}
