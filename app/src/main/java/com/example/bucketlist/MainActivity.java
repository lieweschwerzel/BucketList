package com.example.bucketlist;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;

import com.example.bucketlist.database.BucketItemRoomDatabase;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RecyclerView.OnItemTouchListener {

    //instance variables
    private List<BucketItem> mBucketItems;
    private BucketItemAdapter mAdapter;
    private RecyclerView mRecyclerView;

    //Constants used when calling the update activity
    public static final String EXTRA_BOOKMARK = "Bookmark";
    public static final int REQUESTCODE = 1234;
    private int mNewPosition;
    private GestureDetector mGestureDetector;

    private BucketItemRoomDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        db = BucketItemRoomDatabase.getDatabase(this);
        //Initialize the instance variables

        mRecyclerView = findViewById(R.id.recyclerView);

        mBucketItems = new ArrayList<>();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false
        ));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));

        mGestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
//                intent.putExtra(EXTRA_BOOKMARK, "");
//                startActivityForResult(intent, REQUESTCODE);
                startActivity(intent);
            }
        });

       /* Add a touch helper to the RecyclerView to recognize when a user swipes to delete a list entry.
        An ItemTouchHelper enables touch behavior (like swipe and move) on each ViewHolder,
                and uses callbacks to signal when a user is performing these actions.

                */

        android.support.v7.widget.helper.ItemTouchHelper.SimpleCallback simpleItemTouchCallback =

                new ItemTouchHelper.SimpleCallback(0, android.support.v7.widget.helper.ItemTouchHelper.LEFT | android.support.v7.widget.helper.ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                        return false;
                    }

                    //Called when a user swipes left or right on a ViewHolder
                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                        //Get the index corresponding to the selected position
                        int position = (viewHolder.getAdapterPosition());
//                        mBucketItems.remove(position);
//                        mAdapter.notifyItemRemoved(position);
                        db.bucketItemDao().deleteBucketItem(mBucketItems.get(position));
                        updateUI();
                    }
                };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
        mRecyclerView.addOnItemTouchListener(this);

        updateUI();
    }

    private void updateUI() {
        mBucketItems = db.bucketItemDao().getAllReminders();
        if (mAdapter == null) {
            mAdapter = new BucketItemAdapter(mBucketItems);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.swapList(mBucketItems);
        }

    }

    @Override
    public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
//        View child = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
//        int mAdapterPosition = recyclerView.getChildAdapterPosition(child);
//        if (child != null && mGestureDetector.onTouchEvent(motionEvent)) {
//            Intent intent = new Intent(MainActivity.this, WebActivity.class);
////            mNewPosition = mAdapterPosition;
//            intent.putExtra(EXTRA_BOOKMARK, mBucketItems.get(mAdapterPosition));
//            startActivityForResult(intent, REQUESTCODE);
//        }
        return false;
    }

    @Override
    public void onTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean b) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUESTCODE) {
            if (resultCode == RESULT_OK) {
                BucketItem newBucketItem = data.getParcelableExtra(MainActivity.EXTRA_BOOKMARK);
                // New timestamp: timestamp of update
//                mBucketItems.add(mNewPosition, newBucketItem);
                db.bucketItemDao().updateBucketItem(newBucketItem);
                updateUI();

            }
        }
    }


    public void onBucketCheckBoxChanged(int position, boolean isChecked) {
        BucketItem bucketItem = mBucketItems.get(position);
        bucketItem.setChecked(isChecked);
        mBucketItems.set(position, bucketItem);
//        mMainViewModel.update(mBucketItems.get(position));
    }

}
