package com.example.tablayoutviewpager.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;

import com.example.tablayoutviewpager.Adapter.SampleAdapter;
import com.example.tablayoutviewpager.Model.Pictures;
import com.example.tablayoutviewpager.R;
import com.example.tablayoutviewpager.databinding.ActivityListBinding;
import com.example.tablayoutviewpager.utlis.DataManager;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity implements SampleAdapter.SaveCheckBoxStateListener {


    ActivityListBinding binding;
    List<Pictures> dataList;
    private SampleAdapter adapter;
    int catId;

    DataManager dataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListBinding.inflate(getLayoutInflater());
        Window window = getWindow();
        if (window != null) {
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.green));
        }
        setContentView(binding.getRoot());

        dataManager = new DataManager(this);
        catId = getIntent().getIntExtra("catId",0);
        String name = getIntent().getStringExtra("catName");

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(name);
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

       dataManager.initPictures();
        dataList = dataManager.getPicturesByCategory(catId);
        adapter = new SampleAdapter(this, dataList, this,false);
        binding.listRecyclerView.setAdapter(adapter);
        binding.listRecyclerView.setLayoutManager(new GridLayoutManager(this,2));
        adapter.notifyDataSetChanged();


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onItemCheckedChanged(int itemId, boolean isChecked) {
        dataManager.saveCheckBoxState(itemId,isChecked,this);
    }

    @Override
    public void onSaveCheckBoxState(int position, boolean isChecked) {
        dataManager.saveCheckBoxState(position,isChecked,this);
    }
}