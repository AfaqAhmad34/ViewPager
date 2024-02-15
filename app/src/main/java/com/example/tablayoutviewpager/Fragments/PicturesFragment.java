package com.example.tablayoutviewpager.Fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.tablayoutviewpager.Adapter.SampleAdapter;
import com.example.tablayoutviewpager.Model.Pictures;
import com.example.tablayoutviewpager.databinding.FragmentPicturesBinding;
import com.example.tablayoutviewpager.utlis.DataManager;

import java.util.ArrayList;
import java.util.List;


public class PicturesFragment extends Fragment implements SampleAdapter.SaveCheckBoxStateListener{


    private SampleAdapter adapter;

    private static final String PREF_NAME = "checkbox_prefs";
    private static final String KEY_CHECKBOX_STATE = "key_checkbox_state";

    FragmentPicturesBinding binding;
    List<Pictures> dataList;



    public PicturesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPicturesBinding.inflate(inflater, container, false);
            View view = binding.getRoot();

            dataList = initData();
            adapter = new SampleAdapter(requireContext(), dataList, this,false);
            binding.recyclerViewAllPic.setAdapter(adapter);
            binding.recyclerViewAllPic.setLayoutManager(new GridLayoutManager(requireContext(),2));
            adapter.notifyDataSetChanged();
            return view;
    }


    public void onItemCheckedChanged(int position, boolean isChecked) {
        saveCheckBoxState(position, isChecked);
    }
    private List<Pictures> initData() {
        dataList = new ArrayList<>();
        dataList.add(new Pictures(0,"apple", "Apple", false,1));
        dataList.add(new Pictures(1,"orange", "Orange", false,1));
        dataList.add(new Pictures(2,"pineaplle", "PineApple", false,1));
        dataList.add(new Pictures(3,"berry", "Berry", false,1));
        dataList.add(new Pictures(4,"strawberry", "StrawBerry", false,1));


        for (int i = 0; i < dataList.size(); i++) {
            boolean isSavedState = getCheckBoxState(dataList.get(i).getId());
            dataList.get(i).setFavorite(isSavedState);
        }

        return dataList;
    }

    @Override
    public void onSaveCheckBoxState(int itemId, boolean isChecked) {
        saveCheckBoxState(itemId, isChecked);
    }
    private void saveCheckBoxState(int itemId, boolean isChecked) {
        SharedPreferences prefs = getContext().getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        String key = KEY_CHECKBOX_STATE + itemId;
        editor.putBoolean(key, isChecked);
        editor.apply();
    }
    private boolean getCheckBoxState(int itemId) {
        SharedPreferences prefs = getContext().getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        String key = KEY_CHECKBOX_STATE + itemId;
        return prefs.getBoolean(key, false);
    }


    @Override
    public void onResume() {
        super.onResume();
        dataList = initData();
        adapter = new SampleAdapter(requireContext(),dataList, this,false);
        binding.recyclerViewAllPic.setAdapter(adapter);
        binding.recyclerViewAllPic.setLayoutManager(new GridLayoutManager(requireContext(),2));
        adapter.notifyDataSetChanged();
    }

}