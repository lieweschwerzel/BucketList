package com.example.bucketlist;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;


@Entity(tableName = "bucketlist")
public class BucketItem implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    Long id;
    @ColumnInfo(name = "bucketname")
    String mBucketItemName;
    @ColumnInfo(name = "bucketdetails")
    String mBucketItemDetails;
    @ColumnInfo(name = "checked")
    private boolean mChecked;

    public BucketItem(String bucketItemName, String bucketItemDetails) {
        this.mBucketItemName = bucketItemName;
        this.mBucketItemDetails = bucketItemDetails;
    }

    @Override
    public String toString() {
        return "BucketItem{" +
                "id=" + id +
                ", mBucketItemName='" + mBucketItemName + '\'' +
                ", mBucketItemDetails='" + mBucketItemDetails + '\'' +
                ", mChecked=" + mChecked +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBucketItemName() {
        return mBucketItemName;
    }

    public void setBucketItemName(String bucketItemName) {
        mBucketItemName = bucketItemName;
    }

    public String getBucketItemDetails() {
        return mBucketItemDetails;
    }

    public void setBucketItemDetails(String bucketItemDetails) {
        mBucketItemDetails = bucketItemDetails;
    }

    public boolean isChecked() {
        return mChecked;
    }

    public void setChecked(boolean mChecked) {
        this.mChecked = mChecked;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(id);
        }
        dest.writeString(mBucketItemName);
        dest.writeString(mBucketItemDetails);
    }

    protected BucketItem(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        mBucketItemName = in.readString();
        mBucketItemDetails = in.readString();
    }

    public static final Creator<BucketItem> CREATOR = new Creator<BucketItem>() {
        @Override
        public BucketItem createFromParcel(Parcel in) {
            return new BucketItem(in);
        }

        @Override
        public BucketItem[] newArray(int size) {
            return new BucketItem[size];
        }
    };
}
