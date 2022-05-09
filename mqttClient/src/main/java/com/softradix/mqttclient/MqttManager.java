package com.softradix.mqttclient;

public interface MqttManager {
    void init();

    void connect();

    void sendMessage(String message, String topic);
}

