package com.aidrive.aidriveconcept.adapter;

import static com.aidrive.aidriveconcept.utils.Constants.RECEIVED_MESSAGE;
import static com.aidrive.aidriveconcept.utils.Constants.SEND_MESSAGE;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aidrive.aidriveconcept.R;
import com.softradix.callbacks.OnChatCallback;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public OnChatCallback callback;

    public ChatAdapter(OnChatCallback callback) {
        this.callback = callback;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == SEND_MESSAGE) {
            View v2 = inflater.inflate(R.layout.right_chat_layout, parent, false);
            viewHolder = new SenderViewHolder(v2);
        } else {
            View v = inflater.inflate(R.layout.left_chat_layout, parent, false);
            viewHolder = new ChatAdapter.ReceiverViewHolder(v);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return RECEIVED_MESSAGE;
        } else {
            return SEND_MESSAGE;
        }
    }

    public static class SenderViewHolder extends RecyclerView.ViewHolder {
        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public static class ReceiverViewHolder extends RecyclerView.ViewHolder {
        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }


}
