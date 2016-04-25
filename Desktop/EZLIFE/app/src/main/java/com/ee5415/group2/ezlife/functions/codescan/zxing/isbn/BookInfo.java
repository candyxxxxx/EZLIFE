package com.ee5415.group2.ezlife.functions.codescan.zxing.isbn;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Harry on 2016/3/11.
 */
public class BookInfo implements Parcelable {

    private String mTitle="";
    private Bitmap mBitmap;
    private String mAuthor="";
    private String mPublisher="";
    private String mPublishDate="";
    private String mISBN="";
    private String mSummary="";

    public void setTitle(String Title)
    {
        mTitle=Title;
    }
    public void setBitmap(Bitmap bitmap)
    {
        mBitmap=bitmap;
    }
    public void setAuthor(String Author)
    {
        mAuthor=Author;
    }
    public void setISBN(String ISBN)
    {
        mISBN=ISBN;
    }
    public void setPublishDate(String PublishDate)
    {
        mPublishDate=PublishDate;
    }
    public void setPublisher(String Publisher)
    {
        mPublisher=Publisher;
    }
    public void setSummary(String Summary)
    {
        mSummary=Summary;
    }

    public String getTitle()
    {
        return mTitle;
    }
    public Bitmap getBitmap()
    {
        return mBitmap;
    }
    public String getAuthor()
    {
        return mAuthor;
    }

    public String getISBN()
    {
        return mISBN;
    }
    public String getPublishDate()
    {
        return mPublishDate;
    }
    public String getPublisher()
    {
        return mPublisher;
    }
    public String getSummary()
    {
        return mSummary;
    }

    public static final Creator<BookInfo> CREATOR = new Creator<BookInfo>() {
        public BookInfo createFromParcel(Parcel source) {
            BookInfo bookInfo = new BookInfo();
            bookInfo.mTitle = source.readString();
            bookInfo.mBitmap = source.readParcelable(Bitmap.class.getClassLoader());
            bookInfo.mAuthor = source.readString();
            bookInfo.mPublisher = source.readString();
            bookInfo.mPublishDate = source.readString();
            bookInfo.mISBN = source.readString();
            bookInfo.mSummary = source.readString();
            return bookInfo;
        }
        public BookInfo[] newArray(int size) {
            return new BookInfo[size];
        }
    };
    public int describeContents() {
        return 0;
    }
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mTitle);
        dest.writeParcelable(mBitmap, flags);
        dest.writeString(mAuthor);
        dest.writeString(mPublisher);
        dest.writeString(mPublishDate);
        dest.writeString(mISBN);
        dest.writeString(mSummary);
    }

}