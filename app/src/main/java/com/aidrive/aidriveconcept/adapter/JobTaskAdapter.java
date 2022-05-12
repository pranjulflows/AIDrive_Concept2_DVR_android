package com.aidrive.aidriveconcept.adapter;


import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aidrive.aidriveconcept.R;
import com.aidrive.aidriveconcept.databinding.ListItemBinding;
import com.softradix.network.model.JobResponse;

import java.util.ArrayList;
import java.util.List;

public class JobTaskAdapter extends RecyclerView.Adapter<JobTaskAdapter.ViewHolder> {
    List<JobResponse> items = new ArrayList<>();
    //    ArrayList<String> items = new ArrayList<>();
    OnJobJobListener listener;

    public JobTaskAdapter(List<JobResponse> items, OnJobJobListener listener) {
        this.listener = listener;
        this.items = items;
    }

    int row_index = -1;
    private ListItemBinding itemBinding;

    public interface OnJobJobListener {
        void onJobSelected(JobResponse data);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        itemBinding = ListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.onBind(items.get(position));
        holder.binding.listItem.setOnClickListener(view -> {
            row_index = position;
            notifyDataSetChanged();
            listener.onJobSelected(items.get(position));
        });
        if(row_index==position){
            holder.binding.getRoot().setBackgroundColor(Color.parseColor("#CCCCCC"));
        }
        else
        {
            holder.binding.getRoot().setBackgroundColor(Color.parseColor("#ffffff"));

        }

    }


//    @Override
//    public long getItemId(int position) {
//        return super.getItemId(position);
//    }

 /*   @Override
    public int getItemViewType(int position) {
        if (position==0) {
            return HEADER_VIEW;
        } else {
            return DETAIL_VIEW;
        }
    }*/

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ListItemBinding binding;

        public ViewHolder(@NonNull ListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void onBind(JobResponse item) {
            binding.taskTxt.setText(item.getName());
            binding.statusTxt.setText(item.getStatus());
            binding.dueTxt.setText(item.getDuedate());
            binding.acceptIv.setVisibility(item.getIsAccepted().equals("1") ? View.VISIBLE : View.INVISIBLE);
        }
    }

}
