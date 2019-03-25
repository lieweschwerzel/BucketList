package com.example.bucketlist;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.bucketlist.BucketItem;
import com.example.bucketlist.BucketItemAdapter;
import com.example.bucketlist.R;
import com.example.bucketlist.database.BucketItemRoomDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AddActivity extends AppCompatActivity  {

    //instance variables
    private Button button;
    private List<BucketItem> mBucketItems;
    private BucketItemAdapter mAdapter;

    private EditText mBucketItemView;
    private EditText mBucketItemDetailsView;
    public CheckBox bucketCheckBox;

    private BucketItemRoomDatabase db;
    private Executor executor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        db = BucketItemRoomDatabase.getDatabase(this);
        //Initialize the instance variables
        mBucketItemView = findViewById(R.id.editTitle_add);
        mBucketItemDetailsView = findViewById(R.id.editDescription_add);
        mBucketItems = new ArrayList<>();

        mBucketItemDetailsView.setText("https://");

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = mBucketItemView.getText().toString();
                String details = mBucketItemDetailsView.getText().toString();

                BucketItem newBucketItem = new BucketItem(title, details);
                //Check if some text has been added
                if (!(TextUtils.isEmpty(title))) {
//                    mBookmarks.add(newBookmark);
//                    db.bucketItemDao().insertBucketItem(newBucketItem);

                    insertBucketItem(newBucketItem);
                    Intent intent = new Intent(AddActivity.this, MainActivity.class);
//                    resultIntent.putExtra(MainActivity.EXTRA_BOOKMARK, newBucketItem);
//                    setResult(Activity.RESULT_OK, resultIntent);
//                    finish();
                    startActivity(intent);
                } else {
                    //Show a message to the user if the textfield is empty
                    Snackbar.make(view, "Please enter some text in the textfield", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void insertBucketItem(final BucketItem bucketItem) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                db.bucketItemDao().insertBucketItem(bucketItem);
//                getAllBucketItems(); // Because the Room database has been modified we need to get the new list of reminders.
            }
        });
    }

}
