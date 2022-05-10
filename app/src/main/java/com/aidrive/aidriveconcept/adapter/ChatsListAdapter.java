package com.aidrive.aidriveconcept.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.aidrive.aidriveconcept.R;
import com.softradix.callbacks.OnChatCallback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChatsListAdapter extends RecyclerView.Adapter<ChatsListAdapter.RecyclerHolder> {
    List<String> users = new ArrayList<String>();
    private OnChatCallback callback;

    public ChatsListAdapter(OnChatCallback callback) {
        users.add("John");
        users.add("Stefan");
        users.add("Damon");
        users.add("Klaus");
        this.callback = callback;
    }

    @NonNull
    @Override
    public RecyclerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v2 = LayoutInflater.from(parent.getContext()).inflate(R.layout.users_chat_list_item, parent, false);
        return new RecyclerHolder(v2);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerHolder holder, int position) {
        holder.onBind(users.get(position));
        holder.itemView.setOnClickListener(view -> callback.onSelectUserChat(position));
    }

    public static class RecyclerHolder extends RecyclerView.ViewHolder {
        AppCompatTextView userTxt;

        public RecyclerHolder(@NonNull View itemView) {
            super(itemView);
            userTxt = itemView.findViewById(R.id.chatUserNameTxt);
        }

        public void onBind(String s) {
            userTxt.setText(s);

        }
    }

    @Override
    public int getItemCount() {
        return users.size();
    }
}
