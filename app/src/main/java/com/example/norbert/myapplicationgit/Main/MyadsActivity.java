package com.example.norbert.myapplicationgit.Main;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.example.norbert.myapplicationgit.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MyadsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private DrawerLayout mdrawerlayout;
    private ActionBarDrawerToggle mtoogle;
    private NavigationView navigationView;
    private FirebaseDatabase database;
    private DatabaseReference ref;
    private  String s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myads);


            RecyclerView recyclerView = findViewById(R.id.recycler_view);
            layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);

            database = FirebaseDatabase.getInstance();
            ref = database.getReference("Ads");
            List<Ads> listDataAds =  getDataFromDatabase();
            adapter = new RecyclerAdapter(listDataAds,this);

            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences.Editor editor = settings.edit();
            s = settings.getString("username","Dummy");


            recyclerView.setAdapter(adapter);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
        }

        public List<Ads> getDataFromDatabase(){
            final List<Ads> list = new ArrayList<>();
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                        Ads ads = dataSnapshot1.getValue(Ads.class);
                        Ads listDataAds = new Ads();
                        assert ads != null;
                        String author = ads.getAuthor();
                        if(!author.equals(s)){
                            continue;
                        }
                        String title = ads.getTitle();
                        String shortDescription = ads.getShortDescription();
                        String longDescription = ads.getLongDescription();
                        String phoneNumber = ads.getPhoneNumber();
                        String locationText = ads.getLocationText();
                        String image = ads.getImage();
                        listDataAds.setTitle(title);
                        listDataAds.setShortDescription(shortDescription);
                        listDataAds.setImage(image);
                        listDataAds.setLongDescription(longDescription);
                        listDataAds.setPhoneNumber(phoneNumber);
                        listDataAds.setLocationText(locationText);
                        list.add(listDataAds);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            return list;
        }
}
