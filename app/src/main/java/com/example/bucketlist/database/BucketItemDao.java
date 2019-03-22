package com.example.bucketlist.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.bucketlist.BucketItem;

import java.util.List;

@Dao
public interface BucketItemDao {

    @Query("SELECT * FROM bucketlist")
    List<BucketItem> getAllReminders();

    @Insert
    void insertBucketItem(BucketItem bucketItem);

    @Delete
    void deleteBucketItem(BucketItem bucketItem);

    @Update
    void updateBucketItem(BucketItem bucketItem);
}