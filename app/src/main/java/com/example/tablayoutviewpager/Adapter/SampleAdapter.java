package com.example.tablayoutviewpager.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners;
import com.example.tablayoutviewpager.Activities.PreViewActivity;
import com.example.tablayoutviewpager.Model.Pictures;
import com.example.tablayoutviewpager.R;
import com.example.tablayoutviewpager.databinding.ItemPicsBinding;

import java.util.ArrayList;
import java.util.List;

public class SampleAdapter extends RecyclerView.Adapter<SampleAdapter.ViewHolder> {
    Context context;
    private List<Pictures> dataList;
    private boolean isFavoriteFragment;

    public interface SaveCheckBoxStateListener {
        void onItemCheckedChanged(int position, boolean isChecked);

        void onSaveCheckBoxState(int position, boolean isChecked);
    }
    private SaveCheckBoxStateListener saveCheckBoxStateListener;


    public SampleAdapter(Context context,List<Pictures> dataList, SaveCheckBoxStateListener listener,boolean isFavoriteFragment) {
        this.context = context;
        this.dataList = dataList;
        this.saveCheckBoxStateListener = listener;
        this.isFavoriteFragment = isFavoriteFragment;
    }
    public void updateData(List<Pictures> newDataList) {
        dataList = newDataList;
    }
    @NonNull
    @Override
    public SampleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pics, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SampleAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        //Pictures currentItem = dataList.get(position);
        int drawableResourced  = holder.itemView.getResources().getIdentifier(dataList.get(position).getImageUrl(),
                "drawable",holder.itemView.getContext().getPackageName());

        Glide.with(holder.itemView.getContext())
                .load(drawableResourced)
                .transform(new GranularRoundedCorners(30,30,0,0))
                .into(holder.binding.img);
        holder.binding.titleTxt.setText(dataList.get(position).getName());

        holder.binding.favBtn.setChecked(dataList.get(position).isFavorite());

        holder.binding.favBtn.setOnCheckedChangeListener((buttonView, isChecked) -> {
            dataList.get(position).setFavorite(isChecked);

            saveCheckBoxStateListener.onSaveCheckBoxState(dataList.get(position).getId(), isChecked);
                if (!isChecked && isFavoriteFragment) {
                    removeData(dataList.get(position).getId());
                }
        });

        holder.binding.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(holder.itemView.getContext().getApplicationContext(), PreViewActivity.class);
                intent.putExtra("position", position);
                intent.putParcelableArrayListExtra("picturesList", (ArrayList<? extends Parcelable>) new ArrayList<>(dataList));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ItemPicsBinding binding;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemPicsBinding.bind(itemView);
        }
    }
//    public void removeData(int itemId) {
//        if (itemId >= 0 && itemId < dataList.size()) {
//            dataList.remove(itemId);
//            notifyItemRemoved(itemId);
//            notifyDataSetChanged();
//        }
//    }
   public void removeData(int itemId) {
       for (int i = 0; i < dataList.size(); i++) {
           if (dataList.get(i).getId() == itemId) {
               dataList.remove(i);
               notifyItemRemoved(i);
               notifyDataSetChanged();
               return;
           }
       }
   }
}
