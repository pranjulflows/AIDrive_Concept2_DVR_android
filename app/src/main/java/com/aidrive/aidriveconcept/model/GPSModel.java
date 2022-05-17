package com.aidrive.aidriveconcept.model;

import androidx.annotation.NonNull;

public class GPSModel {
    double latitude;
    double longitude;

    public GPSModel(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @NonNull
    @Override
    public String toString() {
        return "Latitude: -" + getLatitude() + "\nLongitude: -" + getLongitude();
    }
}
