package com.example.tablayoutviewpager.Fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tablayoutviewpager.Adapter.SampleAdapter;
import com.example.tablayoutviewpager.Model.Pictures;
import com.example.tablayoutviewpager.databinding.FragmentFaviourtPictureBinding;
import com.example.tablayoutviewpager.utlis.DataManager;

import java.util.ArrayList;
import java.util.List;

public class FaviourtPictureFragment extends Fragment implements SampleAdapter.SaveCheckBoxStateListener {


    private static final String PREF_NAME = "checkbox_prefs";
    private static final String KEY_CHECKBOX_STATE = "key_checkbox_state";
    private FragmentFaviourtPictureBinding binding;
    private RecyclerView recyclerView;
    private SampleAdapter checkedItemsAdapter;
    private List<Pictures> sharedDataList;


    DataManager dataManager;
    public FaviourtPictureFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFaviourtPictureBinding.inflate(inflater, container, false);
        View view = binding.getRoot();


        dataManager = new DataManager(requireContext());
       binding.recyclerViewFavourite.setLayoutManager(new GridLayoutManager(requireContext(),2));
       sharedDataList =getOriginalData();
       List<Pictures> checkedItems = getCheckedItems();
       checkedItemsAdapter = new SampleAdapter (requireContext(),checkedItems,this,true);
       binding.recyclerViewFavourite.setAdapter(checkedItemsAdapter);
       checkedItemsAdapter.notifyDataSetChanged();
       return view;

    }
    @Override
    public void onItemCheckedChanged(int position, boolean isChecked) {
//        Log.d("ItemChecked", "Position: " + position + ", isChecked: " + isChecked);
        saveCheckBoxState(sharedDataList.get(position).getId(), isChecked);
        /*sharedDataList.get(position).setFavorite(isChecked);

        // Post a Runnable to perform dataset modifications after a short delay
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isChecked) {
                    checkedItemsAdapter.removeData(position);
                }
                checkedItemsAdapter.updateData(sharedDataList);
                checkedItemsAdapter.notifyItemRemoved(position);
            }
        }, 100);*/
//
//        if (!isChecked){
//            checkedItemsAdapter.removeData(position);
//        }
//
//        checkedItemsAdapter.updateData(sharedDataList);
//
//        checkedItemsAdapter.notifyItemRemoved(position);

    }

    private void saveCheckBoxState(int itemId, boolean isChecked) {
        dataManager.saveCheckBoxState(itemId,isChecked,requireContext());
//        SharedPreferences prefs = getContext().getSharedPreferences(PREF_NAME, MODE_PRIVATE);
//        SharedPreferences.Editor editor = prefs.edit();
//        String key = KEY_CHECKBOX_STATE + itemId;
//        editor.putBoolean(key, isChecked);
//        editor.apply();
//        Log.d("Storage", "" + itemId);

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

        return dataManager.getCheckBoxState(itemId);
//        SharedPreferences prefs = getContext().getSharedPreferences(PREF_NAME, MODE_PRIVATE);
//        String key = KEY_CHECKBOX_STATE + itemId;
//        return prefs.getBoolean(key, false);
    }

    private List<Pictures> getOriginalData() {
        return dataManager.getAllPictures();
    }


    @Override
    public void onSaveCheckBoxState(int position, boolean isChecked) {
        //dataManager.saveCheckBoxState(position,isChecked,requireContext());
        saveCheckBoxState(position,isChecked);
       // sharedDataList.get(position).setFavorite(isChecked);
        // Post a Runnable to perform dataset modifications after a short delay
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isChecked) {
                    checkedItemsAdapter.removeData(position);
                }
               // checkedItemsAdapter.updateData(sharedDataList);
                checkedItemsAdapter.notifyItemRemoved(position);
            }
        }, 100);
    }

    @Override
    public void onResume() {
        super.onResume();
        getCheckedItems();
        binding.recyclerViewFavourite.setLayoutManager(new GridLayoutManager(requireContext(),2));
        sharedDataList = getOriginalData();
        List<Pictures> checkedItems = getCheckedItems();
        checkedItemsAdapter = new SampleAdapter(requireContext(),checkedItems, this,true);
        binding.recyclerViewFavourite.setAdapter(checkedItemsAdapter);
        checkedItemsAdapter.notifyDataSetChanged();

    }
}