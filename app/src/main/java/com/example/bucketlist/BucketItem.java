package com.example.bucketlist;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "bucketlist")
public class BucketItem {

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
        this.mChecked = false;
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
        return this.mBucketItemDetails;
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
}
