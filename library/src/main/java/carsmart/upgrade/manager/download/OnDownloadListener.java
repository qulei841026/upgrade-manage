package carsmart.upgrade.manager.download;

public interface OnDownloadListener {

    void onFailed(int reason);

    void onProgress(int size, int totalSize);
}
