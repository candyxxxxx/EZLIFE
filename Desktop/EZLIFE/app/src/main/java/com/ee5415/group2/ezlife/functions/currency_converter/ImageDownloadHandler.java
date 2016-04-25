package com.ee5415.group2.ezlife.functions.currency_converter;

/**
 * Created by Harry on 2016/3/3.
 */
public class ImageDownloadHandler{
    private ImageDownloadInfo idi;
    private FindImageDownload fid;
    private String from;
    private String to;

    public void setFrom(String from) {
        this.from = from;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public void imageDownloadHandler() {
        fid = new FindImageDownload();
        idi = new ImageDownloadInfo(fid.findImageDownload(from, to));
    }
    public ImageDownloadInfo getidi() {
        return idi;
    }
}
