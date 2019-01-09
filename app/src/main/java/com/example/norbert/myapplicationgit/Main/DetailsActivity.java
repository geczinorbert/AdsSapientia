package com.example.norbert.myapplicationgit.Main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.norbert.myapplicationgit.R;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        TextView title = findViewById(R.id.activity_details_title);
        TextView longDesc = findViewById(R.id.activity_details_longDesc);
        TextView phonenumber = findViewById(R.id.activity_details_phonenumber);
        TextView locationText = findViewById(R.id.activity_details_location);
        ImageView image = findViewById(R.id.activity_details_image);

        String ad_title = getIntent().getStringExtra("Title");
        String ad_longDesc = getIntent().getStringExtra("LongDesc");
        String ad_phonenumber = getIntent().getStringExtra("Phonenumber");
        String ad_locationText = getIntent().getStringExtra("LocationText");
        String ad_image = getIntent().getStringExtra("Image");

        title.setText(ad_title);
        longDesc.setText(ad_longDesc);
        phonenumber.setText(ad_phonenumber);
        locationText.setText(ad_locationText);
        Glide.with(this).load(ad_image).into(image);
    }
}
