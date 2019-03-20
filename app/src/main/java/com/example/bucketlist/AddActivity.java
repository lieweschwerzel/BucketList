package com.example.bucketlist;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class AddActivity extends AppCompatActivity  {

    //instance variables
    private Button button;
    private List<BucketItem> mBucketItems;
    private BucketItemAdapter mAdapter;

    private EditText mBucketItemView;
    private EditText mBucketItemDetailsView;
    public CheckBox bucketCheckBox;

    private BucketItemRoomDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        db = BucketItemRoomDatabase.getDatabase(this);
        //Initialize the instance variables
        mBucketItemView = findViewById(R.id.editText_add);
        mBucketItemDetailsView = findViewById(R.id.editURL_add);
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
                    db.bucketItemDao().insertBucketItem(newBucketItem);

                    Intent resultIntent = new Intent(AddActivity.this, MainActivity.class);
//                    resultIntent.putExtra(MainActivity.EXTRA_BOOKMARK, newBucketItem);
//                    setResult(Activity.RESULT_OK, resultIntent);
//                    finish();
                    startActivity(resultIntent);
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

}
