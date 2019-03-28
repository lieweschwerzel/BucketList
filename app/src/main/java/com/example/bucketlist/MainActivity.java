package com.example.bucketlist;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


import com.example.bucketlist.database.BucketItemRoomDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements BucketChangedListener {

    //instance variables
    private List<BucketItem> mBucketItems;
    private BucketItemAdapter mAdapter;
    private RecyclerView mRecyclerView;

    private BucketItemRoomDatabase db;
    private Executor executor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Find the toolbar view inside the activity layout
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);

        db = BucketItemRoomDatabase.getDatabase(this);

        //Initialize the instance variables
        mRecyclerView = findViewById(R.id.recyclerView);
        mBucketItems = new ArrayList<>();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));

        final BucketItemAdapter mAdapter = new BucketItemAdapter(mBucketItems, this);
        mRecyclerView.setAdapter(mAdapter);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                startActivity(intent);
            }
        });

//        GestureDetector mGestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
//
//        });
//        mGestureDetector.setIsLongpressEnabled(true);

//            public void onLongPress(MotionEvent event) {
//                Log.e(TAG, "Longpress detected");
//            }
//            @Override
//            public void onLongPress(MotionEvent e) {
//                super.onLongPress(e);
//                View child = mRecyclerView.findChildViewUnder(e.getX(), e.getY());
//                if (child != null && mGestureDetector.onTouchEvent(e)) {
////                    int adapterPosition = mRecyclerView.getChildAdapterPosition(child);
////                    Toast.makeText(MainActivity.this, adapterPosition, Toast.LENGTH_SHORT).show();
////                    deleteBucketItem(mBucketItems.get(adapterPosition));
////                    View child = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
////                    int position = mRecyclerView.getChildAdapterPosition(child);
//
////                    if (child != null ) {
//                        Toast.makeText(MainActivity.this, "Checklist selected is set to false and ID is ", Toast.LENGTH_SHORT).show();
////                    }
//                }
//            }
//        });
//        mRecyclerView.addOnItemTouchListener(this);
//        getAllBucketItems();


        /* An ItemTouchHelper enables touch behavior (like swipe and move) on each ViewHolder,
                and uses callbacks to signal when a user is performing these actions.
                */
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback =

                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                        return false;
                    }
                    @Override
                    public boolean isLongPressDragEnabled() {
                        return true;
                    }
                    //Called when a user swipes left or right on a ViewHolder
                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                        //Get the index corresponding to the selected position
                        int position = (viewHolder.getAdapterPosition());//
                        deleteBucketItem(mBucketItems.get(position));
                        updateUI();
                    }
                };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
//        mRecyclerView.addOnItemTouchListener(this);

        //get all current items, refresh
        getAllBucketItems();
    }



    private void updateUI() {
//        mBucketItems = db.bucketItemDao().getAllBucketItems();
        if (mAdapter == null) {
            mAdapter = new BucketItemAdapter(mBucketItems, this);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.swapList(mBucketItems);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

            case R.id.action_remove:
                deleteAll();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onBucketCheckBoxChanged(int position, boolean isChecked) {
        BucketItem bucketItem = mBucketItems.get(position);
        bucketItem.setChecked(isChecked);
        updateBucketItem(mBucketItems.set(position, bucketItem));
        updateUI();
    }

    private void getAllBucketItems() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mBucketItems = db.bucketItemDao().getAllBucketItems();
                // In a background thread the user interface cannot be updated from this thread.
                // This method will perform statements on the main thread again.
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateUI();
                    }
                });
            }
        });
    }

    private void updateBucketItem(final BucketItem bucketItem) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                db.bucketItemDao().updateBucketItem(bucketItem);
                getAllBucketItems(); // Because the Room database has been modified we need to get the new list of items.
            }
        });
    }

    private void deleteBucketItem(final BucketItem bucketItem) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                db.bucketItemDao().deleteBucketItem(bucketItem);
                getAllBucketItems(); // Because the Room database has been modified we need to get the new list of items.
            }
        });
    }

    public void deleteAll() {
        for (int i = 0; i<mBucketItems.size(); i++ ){
            BucketItem DelBucket = mBucketItems.get(i);
            if (DelBucket.isChecked())
                deleteBucketItem(DelBucket);
        }
    }


}
