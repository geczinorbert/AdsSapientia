package com.example.norbert.myapplicationgit.Main;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.norbert.myapplicationgit.Login.User;
import com.example.norbert.myapplicationgit.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Objects;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    FirebaseDatabase database;
    FirebaseAuth mAuth;
    DatabaseReference ref;
    List<Ads> listDataAds;
    Context context;;

    public RecyclerAdapter(List<Ads> listDataAds,Context context_) {
        this.listDataAds = listDataAds;
        this.context=context_;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        public int currentItem;
        private TextView itemTitle;
        private TextView itemDetail;
        private ImageView itemImage;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemImage = (ImageView)itemView.findViewById(R.id.item_image);
            this.itemTitle = (TextView)itemView.findViewById(R.id.item_title);
            this.itemDetail = (TextView)itemView.findViewById(R.id.item_detail);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    int position = getAdapterPosition();
                    database = FirebaseDatabase.getInstance();
                    ref = database.getReference("Ads");
                    Intent intent = new Intent (v.getContext(), DetailsActivity.class);
                    intent.putExtra("Title",listDataAds.get(position).getTitle());
                    intent.putExtra("Image",listDataAds.get(position).getImage());
                    intent.putExtra("LongDesc",listDataAds.get(position).getLongDescription());
                    intent.putExtra("LocationText",listDataAds.get(position).getLocationText());
                    intent.putExtra("Phonenumber",listDataAds.get(position).getPhoneNumber());
                    v.getContext().startActivity(intent);
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
        System.out.println("TAG 1" + listDataAds.get(i).getShortDescription());
        Glide.with(context)
                .load(this.listDataAds.get(i).getImage())
                .into(viewHolder.itemImage);

    }

    @Override
    public int getItemCount() {
        return this.listDataAds.size();
    }

}
