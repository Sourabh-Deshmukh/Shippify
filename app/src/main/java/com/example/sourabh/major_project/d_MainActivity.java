package com.example.sourabh.major_project;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

public class d_MainActivity extends RecyclerView.Adapter<d_MainActivity.d_DataObjectHolder> {

    private static final String TAG = "MainActivity";
    private static String LOG_TAG = "MyRecyclerViewAdapter";
    private ArrayList<d_DataObject> mDataset;
    private static MyClickListener myClickListener;
    private Context context;

    public static class d_DataObjectHolder extends
            RecyclerView.ViewHolder implements View
            .OnClickListener {
        TextView label;
        TextView dateTime;
        TextView name;
        ImageView proImg;

        public d_DataObjectHolder(View itemView) {
            super(itemView);
            label = (TextView) itemView.findViewById(R.id.d_textView);
            dateTime = (TextView) itemView.findViewById(R.id.d_textView2);
            name = (TextView) itemView.findViewById(R.id.d_tvname);
            proImg = (ImageView) itemView.findViewById(R.id.d_proImageView);
            Log.i(LOG_TAG, "Adding Listener");
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v)
        {
            myClickListener.onItemClick(getAdapterPosition(), v);
        }

    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public d_MainActivity()
    {

    }

    public d_MainActivity(Context context, ArrayList<d_DataObject> myDataset)
    {
        mDataset = myDataset;
        this.context = context;
    }//this receives the data and provides it to the adapter

    @Override
    public d_DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {

        /*
        Called when RecyclerView needs a new RecyclerView.ViewHolder
         of the given type to represent an item.
         */
        Log.d(TAG, "onCreateViewHolder:");
        View view = LayoutInflater.from(parent.getContext()).inflate
                (R.layout.d_card_view, parent, false);

        d_DataObjectHolder dataObjectHolder = new d_DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(d_DataObjectHolder holder, int position) {

        /*
      Called by RecyclerView to display the data at the specified position.
      This method should update the contents of the itemView to reflect the item at the given position.
         */
        holder.label.setText(mDataset.get(position).getmText1());
        holder.dateTime.setText(mDataset.get(position).getmText2());
        holder.name.setText(mDataset.get(position).getmText3());
        Glide.with(context).load(mDataset.get(position).getmImg())
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.proImg);
        Log.d(TAG, "onBindViewHolder: ");
    }

    public void addItem(d_DataObject dataObj, int index) {
        mDataset.add(index, dataObj);
        //notifyItemInserted(index);
        /*
        Notify any registered observers that the item reflected at position has been newly inserted.
         */
    }

    public void deleteItem(int index) {
        mDataset.remove(index);
      //  notifyItemRemoved(index);
        /*
        Notify any registered observers that the item reflected at position has been removed.
         */
    }


    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }
}