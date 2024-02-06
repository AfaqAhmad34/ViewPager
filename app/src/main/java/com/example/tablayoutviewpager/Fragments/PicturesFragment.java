package com.example.tablayoutviewpager.Fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tablayoutviewpager.Adapter.SampleAdapter;
import com.example.tablayoutviewpager.Model.Pictures;
import com.example.tablayoutviewpager.databinding.FragmentPicturesBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


public class PicturesFragment extends Fragment implements SampleAdapter.SaveCheckBoxStateListener{




    private RecyclerView recyclerView;
    private SampleAdapter adapter;

    private static final String PREF_NAME = "checkbox_prefs";
    private static final String KEY_CHECKBOX_STATE = "key_checkbox_state";

    FragmentPicturesBinding binding;

    public PicturesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentPicturesBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        List<Pictures> dataList = initData();

        adapter = new SampleAdapter(dataList, this);
        binding.recyclerViewAllPic.setAdapter(adapter);
        binding.recyclerViewAllPic.setLayoutManager(new GridLayoutManager(requireContext(),2));
        return view;


    }
    public void onItemCheckedChanged(int position, boolean isChecked) {
        saveCheckBoxState(position, isChecked);
    }
    private List<Pictures> initData() {
        List<Pictures> dataList = new ArrayList<>();
        dataList.add(new Pictures(0,"apple", "Apple", false));
        dataList.add(new Pictures(1,"orange", "Orange", false));
        dataList.add(new Pictures(2,"pineaplle", "PineApple", false));
        dataList.add(new Pictures(3,"berry", "Berry", false));
        dataList.add(new Pictures(4,"strawberry", "StrawBerry", false));

        for (int i = 0; i < dataList.size(); i++) {
            boolean isSavedState = getCheckBoxState(dataList.get(i).getId());
            Log.d("CheckedState",""+isSavedState);
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
        Log.d("Storage",""+itemId);
    }
    private boolean getCheckBoxState(int itemId) {
        SharedPreferences prefs = getContext().getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        String key = KEY_CHECKBOX_STATE + itemId;
        Log.d("Retrive",""+KEY_CHECKBOX_STATE+itemId);
        return prefs.getBoolean(key, false);
    }


    @Override
    public void onResume() {
        super.onResume();
        List<Pictures> dataList = initData();
        adapter = new SampleAdapter(dataList, this);
        binding.recyclerViewAllPic.setAdapter(adapter);
        binding.recyclerViewAllPic.setLayoutManager(new GridLayoutManager(requireContext(),2));
    }
}