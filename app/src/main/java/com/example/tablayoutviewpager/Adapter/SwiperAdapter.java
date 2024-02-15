package com.example.tablayoutviewpager.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners;
import com.example.tablayoutviewpager.Model.Pictures;
import com.example.tablayoutviewpager.R;
import com.example.tablayoutviewpager.databinding.SwipItemBinding;

import java.util.List;
import java.util.Random;

public class SwiperAdapter extends RecyclerView.Adapter<SwiperAdapter.SwiperHolder> {


    Context context;
    private List<Pictures> list;

    public SwiperAdapter(Context context, List<Pictures> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public SwiperAdapter.SwiperHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.swip_item,parent,false);
        return new SwiperHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SwiperAdapter.SwiperHolder holder, int position) {


        int drawableResourced  = holder.itemView.getResources().getIdentifier(list.get(position).getImageUrl(),
                "drawable",holder.itemView.getContext().getPackageName());

        Glide.with(holder.itemView.getContext())
                .load(drawableResourced)
                .transform(new GranularRoundedCorners(30,30,0,0))
                .into(holder.binding.imageView);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class SwiperHolder extends RecyclerView.ViewHolder{

        SwipItemBinding binding;
        public SwiperHolder(@NonNull View itemView) {
            super(itemView);
            binding =SwipItemBinding.bind(itemView);
        }
    }
    public Pictures getItem(int position) {
        if (position >= 0 && position < list.size()) {
            return list.get(position);
        }
        return null;
    }
}
