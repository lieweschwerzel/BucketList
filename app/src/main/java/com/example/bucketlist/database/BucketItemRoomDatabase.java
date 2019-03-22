package com.example.bucketlist.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.bucketlist.BucketItem;

@Database(entities = {BucketItem.class}, version = 2, exportSchema = false)
public abstract class BucketItemRoomDatabase extends RoomDatabase {
    private final static String NAME_DATABASE = "bucket_db4";
    public abstract BucketItemDao bucketItemDao();
    private static volatile BucketItemRoomDatabase INSTANCE;

    public static BucketItemRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (BucketItemRoomDatabase.class) {
                if (INSTANCE == null) {
                    // Create database here
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            BucketItemRoomDatabase.class, NAME_DATABASE)
//                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}
