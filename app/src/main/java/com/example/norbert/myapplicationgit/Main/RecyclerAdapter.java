package com.example.norbert.myapplicationgit.Main;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.norbert.myapplicationgit.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    FirebaseDatabase database;
    FirebaseAuth mAuth;
    DatabaseReference ref;
    List<Ads> listDataAds;

    public RecyclerAdapter(List<Ads> listDataAds) {
        this.listDataAds = listDataAds;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        public int currentItem;
        //public ImageView itemImage;
        public TextView itemTitle;
        public TextView itemDetail;

        public ViewHolder(View itemView) {
            super(itemView);
            //itemImage = (ImageView)itemView.findViewById(R.id.item_image);
            this.itemTitle = (TextView)itemView.findViewById(R.id.item_title);
            this.itemDetail = (TextView)itemView.findViewById(R.id.item_detail);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    int position = getAdapterPosition();

                    Snackbar.make(v, "Click detected on item " + position,
                            Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                    //Intent intent = new Intent (v.getContext(), DetailsActivity.class);
                    //v.getContext().startActivity(intent);
                }
            });
        }
    }

    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_layout, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerAdapter.ViewHolder viewHolder,final int i) {
        viewHolder.itemTitle.setText(this.listDataAds.get(i).getTitle());
        viewHolder.itemDetail.setText(this.listDataAds.get(i).getShortDescription());
        //viewHolder.itemImage.setImageResource(images[i]);
    }

    @Override
    public int getItemCount() {
        return this.listDataAds.size();
    }
}
