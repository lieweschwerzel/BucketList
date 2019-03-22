package com.example.bucketlist;

    import android.content.Context;
    import android.graphics.Paint;
    import android.support.annotation.NonNull;
    import android.support.v7.widget.RecyclerView;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.CheckBox;
    import android.widget.CompoundButton;
    import android.widget.TextView;
    import java.util.List;

public class BucketItemAdapter extends RecyclerView.Adapter<BucketItemAdapter.ViewHolder>  {

    private List<BucketItem> mBucketItems;
    private final BucketChangedListener mBucketChangedListener;

    public BucketItemAdapter(List<BucketItem> mBucketItems, BucketChangedListener mBucketChangedListener) {
        this.mBucketItems = mBucketItems;
        this.mBucketChangedListener = mBucketChangedListener;
    }

    @NonNull
    @Override
    public BucketItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(
                viewGroup.getContext()
        ).inflate(
                R.layout.grid_cell,
                viewGroup,
                false
        );
        return new BucketItemAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final BucketItemAdapter.ViewHolder viewHolder, final int position) {
        BucketItem bucketItem = mBucketItems.get(position);

        viewHolder.tvName.setText(bucketItem.getBucketItemName());
        viewHolder.tvDetails.setText(bucketItem.getBucketItemDetails());
        viewHolder.bucketCheckBox.setChecked(bucketItem.isChecked());
        crossOutTextIfChecked(viewHolder.bucketCheckBox, viewHolder.tvName, viewHolder.tvDetails);
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
            this.bucketCheckBox = itemView.findViewById(R.id.checkbox_bucket);
            this.tvName = itemView.findViewById(R.id.text_title);
            this.tvDetails = itemView.findViewById(R.id.text_description);

            bucketCheckBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int clickedPosition = getAdapterPosition();
                    mBucketChangedListener.onBucketCheckBoxChanged(
                            clickedPosition,
                            bucketCheckBox.isChecked()
                    );
                    crossOutTextIfChecked(bucketCheckBox, tvName, tvDetails);
                }
            });
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

