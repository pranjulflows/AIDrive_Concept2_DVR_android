package com.softradix.mqttclient;

import android.content.Context;
import android.util.Log;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MqttManagerImpl implements MqttManager {
    private MqttAndroidClient mqttAndroidClient = null;
    public MqttStatusListener mqttStatusListener = null;
    Context applicationContext;
    String serverUri;
    String clientId;
    String[] topics;
    int[] topicQos;
    private static final String TAG = MqttManagerImpl.class.getName();
    public MqttManagerImpl(Context applicationContext,
                           String serverUri,
                           String clientId,
                           String[] topics,
                           int[] topicQos) {
        this.applicationContext = applicationContext;
        this.serverUri = serverUri;
        this.clientId = clientId;
        this.topics = topics;
        this.topicQos = topicQos;

    }

    @Override
    public void init() {
        mqttAndroidClient = new MqttAndroidClient(applicationContext, serverUri, clientId);
        mqttAndroidClient.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {
                mqttStatusListener.onConnectComplete(reconnect, serverURI);
                if (reconnect) {
                    // Because Clean Session is true, we need to re-subscribe
                    subscribeToTopic();
                }
                Log.i(TAG, reconnect+" connectComplete: server URI "+serverURI);
            }

            @Override
            public void connectionLost(Throwable cause) {
                mqttStatusListener.onConnectionLost(cause);
                Log.e(TAG, "connectionLost: "+cause );
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                mqttStatusListener.onMessageArrived(topic, message);
                Log.i(TAG, " messageArrived: topic: "+topic+" MqttMessage: "+message.toString());

            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                try {
                    Log.i(TAG, " deliveryComplete: IMqttDeliveryToken: "+token.toString()+" Message: "+token.getMessage());
                } catch (MqttException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    public void connect() {
        try {
            mqttAndroidClient.connect(createConnectOptions(), new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    mqttAndroidClient.setBufferOpts(createDisconnectedBufferOptions());
                    subscribeToTopic();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    mqttStatusListener.onConnectFailure(exception);

                }
            });
        } catch (MqttException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void sendMessage(String message, String topic) {
        if (mqttAndroidClient != null) {

            try {
                MqttMessage mqttMessage = new MqttMessage();
                mqttMessage.setPayload(message.getBytes());
                mqttAndroidClient.publish(topic, mqttMessage);
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }


    }

    private void subscribeToTopic() {
        try {
            mqttAndroidClient.subscribe(topics, topicQos, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    mqttStatusListener.onTopicSubscriptionSuccess();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    mqttStatusListener.onTopicSubscriptionError(exception);

                }
            });
        } catch (MqttException ex) {
            ex.printStackTrace();
        }
    }

    private DisconnectedBufferOptions createDisconnectedBufferOptions() {
        DisconnectedBufferOptions disconnectedBufferOptions = new DisconnectedBufferOptions();
        disconnectedBufferOptions.setBufferSize(100);
        disconnectedBufferOptions.setBufferEnabled(true);
        disconnectedBufferOptions.setPersistBuffer(false);
        disconnectedBufferOptions.setDeleteOldestMessages(false);
        return disconnectedBufferOptions;
    }

    private MqttConnectOptions createConnectOptions() {
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setCleanSession(true);
        mqttConnectOptions.setAutomaticReconnect(true);
        return mqttConnectOptions;
    }
}
