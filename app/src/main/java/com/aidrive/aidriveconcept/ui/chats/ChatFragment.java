package com.aidrive.aidriveconcept.ui.chats;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.aidrive.aidriveconcept.adapter.ChatAdapter;
import com.aidrive.aidriveconcept.databinding.ChatFragmentBinding;
import com.softradix.callbacks.OnChatCallback;
import com.softradix.mqttclient.MqttManagerImpl;
import com.softradix.mqttclient.MqttStatusListener;

import org.eclipse.paho.client.mqttv3.MqttMessage;


public class ChatFragment extends Fragment implements OnChatCallback {

    private ChatViewModel mViewModel;

    public static ChatFragment newInstance() {
        return new ChatFragment();
    }

    private ChatFragmentBinding binding;
    private MqttManagerImpl mqttManager;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = ChatFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setRecyclerView();
    }

    private void setRecyclerView() {
        binding.chatsRv.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false));
        binding.chatsRv.setAdapter(new ChatAdapter(this));

    }
    private void initMqttStatusListener(){
        mqttManager.mqttStatusListener = new MqttStatusListener() {
            @Override
            public void onConnectComplete(Boolean reconnect, String serverURI) {

            }

            @Override
            public void onConnectFailure(Throwable exception) {

            }

            @Override
            public void onConnectionLost(Throwable exception) {

            }

            @Override
            public void onTopicSubscriptionSuccess() {

            }

            @Override
            public void onTopicSubscriptionError(Throwable exception) {

            }

            @Override
            public void onMessageArrived(String topic, MqttMessage message) {

            }


        };
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ChatViewModel.class);
    }

    @Override
    public void onSelectUserChat(int position) {

    }
}