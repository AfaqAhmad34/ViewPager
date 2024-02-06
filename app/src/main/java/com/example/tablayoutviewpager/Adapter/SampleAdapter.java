package com.example.tablayoutviewpager.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners;
import com.example.tablayoutviewpager.Model.Pictures;
import com.example.tablayoutviewpager.R;
import com.example.tablayoutviewpager.databinding.ItemPicsBinding;

import java.util.List;

public class SampleAdapter extends RecyclerView.Adapter<SampleAdapter.ViewHolder> {

    private List<Pictures> dataList;

    public interface SaveCheckBoxStateListener {
        void onItemCheckedChanged(int position, boolean isChecked);

        void onSaveCheckBoxState(int position, boolean isChecked);
    }
    private SaveCheckBoxStateListener saveCheckBoxStateListener;

    public SampleAdapter(List<Pictures> dataList, SaveCheckBoxStateListener listener) {
        this.dataList = dataList;
        this.saveCheckBoxStateListener = listener;
    }
    @NonNull
    @Override
    public SampleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pics, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SampleAdapter.ViewHolder holder, int position) {
        Pictures currentItem = dataList.get(position);
        int drawableResourced  = holder.itemView.getResources().getIdentifier(dataList.get(position).getImageUrl(),
                "drawable",holder.itemView.getContext().getPackageName());

        Glide.with(holder.itemView.getContext())
                .load(drawableResourced)
                .transform(new GranularRoundedCorners(30,30,0,0))
                .into(holder.binding.img);
        holder.binding.titleTxt.setText(currentItem.getName());


        holder.binding.favBtn.setChecked(currentItem.isFavorite());

        // Add a listener to handle CheckBox state changes
        holder.binding.favBtn.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Save the state to your data model when CheckBox is clicked
            currentItem.setFavorite(isChecked);

            // Notify the activity to save the state to SharedPreferences
            if (saveCheckBoxStateListener != null) {
                saveCheckBoxStateListener.onSaveCheckBoxState(currentItem.getId(), isChecked);
                notifyDataSetChanged();
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
}
