package com.example.tablayoutviewpager.Activities;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.example.tablayoutviewpager.Adapter.ViewPagerAdapter;
import com.example.tablayoutviewpager.Fragments.CategoryFragment;
import com.example.tablayoutviewpager.Fragments.FaviourtPictureFragment;
import com.example.tablayoutviewpager.Fragments.PicturesFragment;
import com.example.tablayoutviewpager.R;
import com.example.tablayoutviewpager.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    ActivityMainBinding binding;

    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;
    FaviourtPictureFragment faviourtPictureFragment;

    public ActionBarDrawerToggle actionBarDrawerToggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Wallpapers");
            actionBar.setDisplayShowTitleEnabled(true);
        }
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, binding.myDrawerLayout, R.string.nav_open, R.string.nav_close);
        binding.myDrawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        binding.navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id==R.id.nav_rate){
                    rateMe();
                } else if (id==R.id.nav_share) {
                    openShareList();
                }else{
                   openMoreApps();
                }

                binding.myDrawerLayout.closeDrawer(GravityCompat.START);

                return true;
            }
        });

        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);


        List<Fragment> fragments = new ArrayList<>();
        faviourtPictureFragment = new FaviourtPictureFragment();
        fragments.add(new CategoryFragment());
        fragments.add(faviourtPictureFragment);


        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(),fragments);
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        viewPager.setOffscreenPageLimit(1);

        statusBarColor();
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

                if (binding.myDrawerLayout.isDrawerOpen(GravityCompat.START)){
                    binding.myDrawerLayout.closeDrawer(GravityCompat.START);
                }else {
                    showExitConfirmationDialog();
                }

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void statusBarColor(){
        Window window = MainActivity.this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(MainActivity.this, R.color.green));
    }
    private void showExitConfirmationDialog() {

        Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.cutom_dialog);
        dialog.setCancelable(false);

        Button yesBtn = dialog.findViewById(R.id.btn_yes);
        Button noBtn = dialog.findViewById(R.id.btn_no);

        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        yesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        dialog.show();
    }


    public void rateMe(){
        try {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("market://details?id="+"com.android.chrome")));
        }catch (ActivityNotFoundException e){
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id="+getPackageName())));
        }
    }

    public void openMoreApps(){
        try {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("market://search?q=pub:Funn+Media")));
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/search?q=pub:Level+Infinite")));
        }
    }
    public void openShareList() {
        String appPackageName ="com.android.chrome";
        String playStoreLink = "https://play.google.com/store/apps/details?id=" + appPackageName;

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT,  playStoreLink);
        startActivity(Intent.createChooser(shareIntent, "Share App Link"));

    }
}