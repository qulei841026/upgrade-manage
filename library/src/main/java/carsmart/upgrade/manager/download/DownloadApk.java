package carsmart.upgrade.manager.download;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.util.regex.Pattern;

public class DownloadApk {

    private static final Uri CONTENT_URI = Uri.parse("content://downloads/my_downloads");
    private static final String MIME_TYPE_APK = "application/vnd.android.package-archive";

    private Context mContext;
    private ContentObserver mObserver;
    private BroadcastReceiver mReceiver;
    private DownloadManager mDownloadManager;

    private OnDownloadListener onDownloadListener;

    public DownloadPath downloadPath;
    public boolean isAutoInstall = true;

    private HandlerThread handlerThread;

    private long downloadId = 0;

    public DownloadApk(Context context) {
        mContext = context;
        mDownloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

        Log.d("qulei", "a:"
                + DownloadManager.STATUS_PENDING + ","
                + DownloadManager.STATUS_RUNNING + ","
                + DownloadManager.STATUS_PAUSED + ","
                + DownloadManager.STATUS_SUCCESSFUL + ","
                + DownloadManager.STATUS_FAILED);
    }

    private Handler getThreadHandler() {
        handlerThread = new HandlerThread(DownloadApk.class.toString());
        handlerThread.start();
        return new Handler(handlerThread.getLooper());
    }

    public DownloadApk setDownloadPath(DownloadPath downloadPath) {
        this.downloadPath = downloadPath;
        return this;
    }

    public DownloadApk setAutoInstall(boolean autoInstall) {
        isAutoInstall = autoInstall;
        return this;
    }

    public void setOnDownloadListener(OnDownloadListener listener) {
        this.onDownloadListener = listener;
    }

    public void download(String url) {
        register();

        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setMimeType(MIME_TYPE_APK);

        DownloadPath path = matchPath(downloadPath);
        request.setDestinationInExternalPublicDir(path.dir, path.path);

        request.setVisibleInDownloadsUi(!isAutoInstall);

        if (!isAutoInstall) {
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        }

        downloadId = mDownloadManager.enqueue(request);

    }

    private void register() {

        mReceiver = new DownloadBroadcastReceiver();
        mContext.registerReceiver(mReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        mObserver = new DownloadContentObserver(getThreadHandler());
        mContext.getContentResolver().registerContentObserver(CONTENT_URI, true, mObserver);
    }

    private void unregister() {

        if (mReceiver != null) {
            mContext.unregisterReceiver(mReceiver);
        }

        if (mObserver != null) {
            mContext.getContentResolver().unregisterContentObserver(mObserver);
        }

        if (handlerThread != null) {
            handlerThread.quit();
        }

    }

    private DownloadPath matchPath(DownloadPath downloadPath) {
        String dir = "Download";
        String path = mContext.getPackageName() + ".apk";

        if (downloadPath == null) {
            return new DownloadPath(dir, path);
        }

        if (TextUtils.isEmpty(downloadPath.dir)) {
            downloadPath.dir = dir;
        }

        if (TextUtils.isEmpty(downloadPath.path)) {
            downloadPath.path = path;
        } else if (!Pattern.matches(".*\\.(?i)apk", downloadPath.path)) {
            downloadPath.path = downloadPath.path + ".apk";
        }

        return downloadPath;
    }

    private void installApk(Context context, Uri uri, String mimeType) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, mimeType);
        context.startActivity(intent);
    }

    private String queryFileInfo(DownloadManager downloadManager, long downloadId) {
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(downloadId);
        Cursor cursor = downloadManager.query(query);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int fileIdx = cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME);
                return cursor.getString(fileIdx);
            }
            cursor.close();
        }
        return null;
    }

    private class DownloadBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(intent.getAction())) {
                unregister();
                String filePath = queryFileInfo(mDownloadManager, downloadId);
                if (isAutoInstall) {
                    if (!TextUtils.isEmpty(filePath)) {
                        installApk(context, Uri.fromFile(new File(filePath)), MIME_TYPE_APK);
                    } else {
                        if (onDownloadListener != null) {
                            onDownloadListener.onFailed(100);
                        }
                    }
                }

            }
        }
    }


    private class DownloadContentObserver extends ContentObserver {

        public DownloadContentObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            super.onChange(selfChange, uri);
            if (ContentUris.withAppendedId(CONTENT_URI, downloadId).equals(uri)) {
                int[] status = queryDownloadStatus(mDownloadManager, downloadId);
                doDownloadStatus(mDownloadManager, downloadId, status);
            }
        }
    }

    private int[] queryDownloadStatus(DownloadManager manager, long id) {
        int[] status = new int[]{-1, -1, -1, -1};//status,reason,byte_download,byte_total
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(id);
        Cursor cursor = manager.query(query);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                status[0] = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
                status[1] = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_REASON));

                status[2] = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                status[3] = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
            }
            cursor.close();
        }
        return status;
    }

    private void doDownloadStatus(DownloadManager manager, long id, int[] status) {
        int state = status[0];
        if (DownloadManager.STATUS_PAUSED == state || DownloadManager.STATUS_FAILED == state) {
            manager.remove(id);
            unregister();
            if (onDownloadListener != null) {
                onDownloadListener.onFailed(status[1]);
            }
        }

        if (DownloadManager.STATUS_RUNNING == state && status[2] > 0 && status[3] > 0) {
            if (onDownloadListener != null) {
                onDownloadListener.onProgress(status[2], status[3]);
            }
        }

    }
}
