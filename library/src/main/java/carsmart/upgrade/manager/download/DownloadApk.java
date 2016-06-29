package carsmart.upgrade.manager.download;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

public class DownloadApk {

    private static final String MIME_TYPE_APK = "application/vnd.android.package-archive";

    private Context mContext;

    private DownloadManager dm;

    private long downloadId = 0;

    public boolean isAutoInstall = true;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(intent.getAction())) {

                unregister();

                if (isAutoInstall) {
                    long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                    if (downloadId == id) {
                        queryFileInfo(dm, downloadId);
                    }
                }
            }

        }
    };

    public DownloadApk(Context context) {
        mContext = context;
        dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
    }

    public void download(String url) {
        register();

        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setMimeType(MIME_TYPE_APK);

//        if (isAutoInstall) {
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
//        }


        request.setDestinationInExternalPublicDir("Download", "updata.apk");

//        request.allowScanningByMediaScanner();

        downloadId = dm.enqueue(request);
    }


    private void register() {
        IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        mContext.registerReceiver(receiver, filter);
    }

    private void unregister() {
        mContext.unregisterReceiver(receiver);
    }


    private void installApk(Context context, Uri uri, String mimeType) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, mimeType);
        context.startActivity(intent);
    }

    protected void queryFileInfo(DownloadManager downloadManager, long downloadId) {

        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(downloadId);

        Cursor cursor = downloadManager.query(query);
        if (cursor.moveToFirst()) {
            int fileNameIdx = cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME);
            int fileUriIdx = cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI);
            String fileName = cursor.getString(fileNameIdx);
            String fileUri = cursor.getString(fileUriIdx);
            Log.d("qulei", fileName + " : " + fileUri);
//            installApk(mContext, Uri.fromFile(new File(fileName)), "application/vnd.android.package-archive");
        }
        cursor.close();


    }

}


//    String url = "http://pkg3.fir.im/3bb68550c4aa15fa92841de39aad36eb2133286e.apk";

//    String url = "http://imgstore.cdn.sogou.com/app/a/100540002/714860.jpg";
