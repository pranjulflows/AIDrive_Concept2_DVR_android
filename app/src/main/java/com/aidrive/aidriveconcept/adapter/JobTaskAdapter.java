package com.aidrive.aidriveconcept.adapter;

import static com.aidrive.aidriveconcept.utils.Constants.DETAIL_VIEW;
import static com.aidrive.aidriveconcept.utils.Constants.HEADER_VIEW;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aidrive.aidriveconcept.R;

import java.util.ArrayList;

public class JobTaskAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<String> items = new ArrayList<>();

    public JobTaskAdapter() {
        items.add("Hello");
        items.add("Hello");
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == DETAIL_VIEW) {
            View v2 = inflater.inflate(R.layout.list_item, parent, false);
            viewHolder = new ViewHolder(v2);
        } else {
            View v = inflater.inflate(R.layout.header_view_layout, parent, false);
            viewHolder = new HeaderViewHolder(v);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
//        if(holder.getItemViewType()==HEADER_VIEW){ }
    }


//    @Override
//    public long getItemId(int position) {
//        return super.getItemId(position);
//    }

    @Override
    public int getItemViewType(int position) {
        if (position==0) {
            return HEADER_VIEW;
        } else {
            return DETAIL_VIEW;
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class HeaderViewHolder extends RecyclerView.ViewHolder {

        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);

        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

}
