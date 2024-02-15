package com.example.tablayoutviewpager.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tablayoutviewpager.Adapter.CategoryAdapter;
import com.example.tablayoutviewpager.Adapter.SampleAdapter;
import com.example.tablayoutviewpager.Model.Category;
import com.example.tablayoutviewpager.Model.Pictures;
import com.example.tablayoutviewpager.R;
import com.example.tablayoutviewpager.databinding.FragmentCategoryBinding;
import com.example.tablayoutviewpager.databinding.FragmentPicturesBinding;
import com.example.tablayoutviewpager.utlis.DataManager;

import java.util.ArrayList;
import java.util.List;

public class CategoryFragment extends Fragment {


    private CategoryAdapter adapter;
    FragmentCategoryBinding binding;
    List<Category> categoryList;

    DataManager dataManager;
    public CategoryFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCategoryBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        dataManager = new DataManager(requireContext());
        adapter = new CategoryAdapter(requireContext(), dataManager.getAllCategories());
        binding.recyclerViewCategory.setAdapter(adapter);
        binding.recyclerViewCategory.setLayoutManager(new GridLayoutManager(requireContext(),2));
        adapter.notifyDataSetChanged();
        return view;
    }
//    private List<Category> initData() {
//        categoryList = new ArrayList<>();
//        categoryList.add(new Category(1,"vegetables","vegetables"));
//        categoryList.add(new Category(2,"Fruits","fruit"));
//        categoryList.add(new Category(3,"Split chickpeas","split"));
//
//        return categoryList;
//    }
}