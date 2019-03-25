package com.example.bucketlist;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
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

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                startActivity(intent);
            }
        });

//       /* Add a touch helper to the RecyclerView to recognize when a user swipes to delete a list entry.
//        An ItemTouchHelper enables touch behavior (like swipe and move) on each ViewHolder,
//                and uses callbacks to signal when a user is performing these actions.
//                */
//        ItemTouchHelper.SimpleCallback simpleItemTouchCallback =
//
//                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
//                    @Override
//                    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
//                        return false;
//                    }
//                    //Called when a user swipes left or right on a ViewHolder
//                    @Override
//                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
//                        //Get the index corresponding to the selected position
//                        int position = (viewHolder.getAdapterPosition());
////                        mBucketItems.remove(position);
////                        mAdapter.notifyItemRemoved(position);
////                        db.bucketItemDao().deleteBucketItem(mBucketItems.get(position));
//                        deleteBucketItem(mBucketItems.get(position));
//                        updateUI();
//                    }
//                };
//
//        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
//        itemTouchHelper.attachToRecyclerView(mRecyclerView);
//        mRecyclerView.addOnItemTouchListener(this);

        getAllBucketItems();
    }

    private void updateUI() {
//        mBucketItems = db.bucketItemDao().getAllBucketItems();
        if (mAdapter == null) {
            mAdapter = new BucketItemAdapter(mBucketItems, this );
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

//    @Override
//    public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
//        View child = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
//        int position = recyclerView.getChildAdapterPosition(child);
//
//        if (!mBucketItems.get(position).isChecked() && child != null && mGestureDetector.onTouchEvent(motionEvent)) {
//            Toast.makeText(this, "Checklist selected is set to false and ID is " + mBucketItems.get(position).getId(), Toast.LENGTH_SHORT).show();
//        }
//        return true;
//
////        View child = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
////        int mAdapterPosition = recyclerView.getChildAdapterPosition(child);
////        if (child != null && mGestureDetector.onTouchEvent(motionEvent)) {
////            Intent intent = new Intent(MainActivity.this, WebActivity.class);
//////            mNewPosition = mAdapterPosition;
////            intent.putExtra(EXTRA_BOOKMARK, mBucketItems.get(mAdapterPosition));
////            startActivityForResult(intent, REQUESTCODE);
////        }
//
//    }

//    @Override
//    public void onTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
//
//    }
//
//    @Override
//    public void onRequestDisallowInterceptTouchEvent(boolean b) {
//
//    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == REQUESTCODE) {
//            if (resultCode == RESULT_OK) {
//                BucketItem newBucketItem = data.getParcelableExtra(MainActivity.EXTRA_BOOKMARK);
//                // New timestamp: timestamp of update
////                mBucketItems.add(mNewPosition, newBucketItem);
////                db.bucketItemDao().insertBucketItem(newBucketItem);
//                updateBucketItem(newBucketItem);
//                updateUI();
//            }
//        }
//    }

    public void onBucketCheckBoxChanged(int position, boolean isChecked) {
        BucketItem bucketItem = mBucketItems.get(position);
        bucketItem.setChecked(isChecked);
        mBucketItems.set(position, bucketItem);
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
                getAllBucketItems(); // Because the Room database has been modified we need to get the new list of reminders.
            }
        });
    }

    private void deleteBucketItem(final BucketItem bucketItem) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                db.bucketItemDao().deleteBucketItem(bucketItem);
                getAllBucketItems(); // Because the Room database has been modified we need to get the new list of reminders.
            }
        });
    }
}
