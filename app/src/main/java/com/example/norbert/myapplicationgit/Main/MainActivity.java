package com.example.norbert.myapplicationgit.Main;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.example.norbert.myapplicationgit.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNav = findViewById(R.id.button_navigation_layout_above);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.button_navigation_layout,
                    new HomeFragment()).commit();
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment fragment = null;

                    switch (item.getItemId()){
                        case R.id.nav_add:
                            fragment = new AddFragment();
                            break;
                        case R.id.nav_home:
                            fragment = new HomeFragment();
                            break;
                        case R.id.nav_profile:
                            fragment =new ProfilFragment();
                            break;
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.button_navigation_layout,
                            fragment).commit();
                    return true;
                }
            };

    }
