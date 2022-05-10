package com.aidrive.aidriveconcept.ui.chats;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.aidrive.aidriveconcept.adapter.ChatAdapter;
import com.aidrive.aidriveconcept.databinding.ChatFragmentBinding;
import com.softradix.callbacks.OnChatCallback;
import com.softradix.mqttclient.MQTTClient;
import com.softradix.mqttclient.MqttManagerImpl;
import com.softradix.mqttclient.MqttStatusListener;
import com.softradix.mqttclient.utils.Constants;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;


public class ChatFragment extends Fragment implements OnChatCallback {

    private ChatViewModel mViewModel;

    public static ChatFragment newInstance() {
        return new ChatFragment();
    }

    private ChatFragmentBinding binding;
    private MqttManagerImpl mqttManager;
    private MQTTClient mqttClient;

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

    private void initMqttStatusListener() {

        mqttClient = new MQTTClient(requireActivity(), Constants.serverUri, "CLIENT_ID");
        mqttClient.connect("", "", new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                Log.d(this.getClass().getName(), "Connection success");
                Toast.makeText(requireActivity(), "MQTT Connection success", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                Log.d(this.getClass().getName(), "Connection failure: " + exception.toString());

                Toast.makeText(requireActivity(), "MQTT Connection fails: " + exception, Toast.LENGTH_SHORT).show();
            }
        }, new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                Log.d(this.getClass().getName(), "Connection lost: " + cause.toString());
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                String msg = "Receive message:" + message.toString() + " from topic: " + topic;
                Log.d(this.getClass().getName(), msg);

                Toast.makeText(requireActivity(), msg, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                Log.d(this.getClass().getName(), "delivery Complete");

            }
        });

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