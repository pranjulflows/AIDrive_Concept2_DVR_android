package com.softradix.mqttclient;

import org.eclipse.paho.client.mqttv3.MqttMessage;

public interface MqttStatusListener {

    void onConnectComplete(Boolean reconnect, String serverURI);

    void onConnectFailure(Throwable exception);

    void onConnectionLost(Throwable exception);

    //
    void onTopicSubscriptionSuccess();

    void onTopicSubscriptionError(Throwable exception);

    //
    void onMessageArrived(String topic, MqttMessage message);

}
