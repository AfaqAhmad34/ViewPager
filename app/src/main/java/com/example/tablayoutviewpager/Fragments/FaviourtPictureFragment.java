package com.example.tablayoutviewpager.Fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tablayoutviewpager.Adapter.SampleAdapter;
import com.example.tablayoutviewpager.Model.Pictures;
import com.example.tablayoutviewpager.databinding.FragmentFaviourtPictureBinding;

import java.util.ArrayList;
import java.util.List;

public class FaviourtPictureFragment extends Fragment implements SampleAdapter.SaveCheckBoxStateListener {


    private static final String PREF_NAME = "checkbox_prefs";
    private static final String KEY_CHECKBOX_STATE = "key_checkbox_state";
    private FragmentFaviourtPictureBinding binding;
    private RecyclerView recyclerView;
    private SampleAdapter checkedItemsAdapter;
    private List<Pictures> sharedDataList;


    public FaviourtPictureFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFaviourtPictureBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

       binding.recyclerViewFavourite.setLayoutManager(new GridLayoutManager(requireContext(),2));
       sharedDataList = getOriginalData();
       List<Pictures> checkedItems = getCheckedItems();
       checkedItemsAdapter = new SampleAdapter(checkedItems, this);
       binding.recyclerViewFavourite.setAdapter(checkedItemsAdapter);
       return view;

    }

    @Override
    public void onItemCheckedChanged(int position, boolean isChecked) {
        Log.d("ItemChecked", "Position: " + position + ", isChecked: " + isChecked);
        saveCheckBoxState(sharedDataList.get(position).getId(), isChecked);
        sharedDataList.get(position).setFavorite(isChecked);
        checkedItemsAdapter.notifyItemChanged(position);

    }

    private void saveCheckBoxState(int itemId, boolean isChecked) {
        SharedPreferences prefs = getContext().getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        String key = KEY_CHECKBOX_STATE + itemId; // Use a unique key for each item
        editor.putBoolean(key, isChecked);
        editor.apply();
        Log.d("Storage", "" + itemId);

    }

    private List<Pictures> getCheckedItems() {
        List<Pictures> checkedItemsList = new ArrayList<>();

        for (int i = 0; i < sharedDataList.size(); i++) {
            Pictures item = sharedDataList.get(i);
            boolean isChecked = getCheckBoxState(item.getId());
            Log.d("Checked", "Item Status " + isChecked + " for ID: " + item.getId());
            item.setFavorite(isChecked);
            if (isChecked) {
                checkedItemsList.add(item);
            }
        }

        return checkedItemsList;
    }

    private boolean getCheckBoxState(int itemId) {
        SharedPreferences prefs = getContext().getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        String key = KEY_CHECKBOX_STATE + itemId;
        return prefs.getBoolean(key, false);
    }

    private List<Pictures> getOriginalData() {

        List<Pictures> dataList = new ArrayList<>();
        dataList.add(new Pictures(0,"apple", "Apple", false));
        dataList.add(new Pictures(1,"orange", "Orange", false));
        dataList.add(new Pictures(2,"pineaplle", "PineApple", false));
        dataList.add(new Pictures(3,"berry", "Berry", false));
        dataList.add(new Pictures(4,"strawberry", "StrawBerry", false));
        // Add more items as needed
        return dataList;
    }

    @Override
    public void onSaveCheckBoxState(int position, boolean isChecked) {
        saveCheckBoxState(position, isChecked);
    }

    @Override
    public void onResume() {
        super.onResume();

        binding.recyclerViewFavourite.setLayoutManager(new GridLayoutManager(requireContext(),2));
        sharedDataList = getOriginalData();
        List<Pictures> checkedItems = getCheckedItems();
        checkedItemsAdapter = new SampleAdapter(checkedItems, this);
        binding.recyclerViewFavourite.setAdapter(checkedItemsAdapter);

    }
}