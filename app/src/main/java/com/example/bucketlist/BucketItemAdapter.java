package com.example.bucketlist;

    import android.content.Context;
    import android.graphics.Paint;
    import android.support.annotation.NonNull;
    import android.support.v7.widget.RecyclerView;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.CheckBox;
    import android.widget.TextView;
    import java.util.List;

public class BucketItemAdapter extends RecyclerView.Adapter<BucketItemAdapter.ViewHolder>  {

    private List<BucketItem> mBucketItems;

    public BucketItemAdapter(List<BucketItem> mBucketItems) {this.mBucketItems = mBucketItems;
    }

    @NonNull
    @Override
    public BucketItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(android.R.layout.two_line_list_item, null);
        // Return a new holder instance
        BucketItemAdapter.ViewHolder viewHolder = new BucketItemAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BucketItemAdapter.ViewHolder viewHolder, int position) {
        BucketItem bucketItem = mBucketItems.get(position);

        viewHolder.tvName.setText(bucketItem.getBucketItemName());
        viewHolder.tvDetails.setText(bucketItem.getBucketItemDetails());

    }

    @Override
    public int getItemCount() {
        return mBucketItems.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        TextView tvDetails;
        public CheckBox bucketCheckBox;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(android.R.id.text1);
            tvDetails = itemView.findViewById(android.R.id.text2);
            bucketCheckBox = itemView.findViewById(R.id.checkbox);
        }
    }

    public void swapList (List<BucketItem> newList) {
        mBucketItems = newList;
        if (newList != null) {
            // Force the RecyclerView to refresh
            this.notifyDataSetChanged();
        }
    }

    public void crossOutTextIfChecked(CheckBox bucket, TextView title, TextView description) {
        if (bucket.isChecked()) {
            title.setPaintFlags(title.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            description.setPaintFlags(description.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            title.setPaintFlags(title.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
            description.setPaintFlags(description.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
        }
    }




}

