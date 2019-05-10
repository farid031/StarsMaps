package com.farid.starsmaps.helper.node_helper;

import com.google.gson.annotations.SerializedName;

public class LocationModel {
    @SerializedName("id_jalur")
    private String id_jalur;
    @SerializedName("node")
    private String node;
    @SerializedName("latitude")
    private String latitude;
    @SerializedName("longitude")
    private String longitude;


    public LocationModel(
            String id_jalur,
            String node,
            String latutide,
            String longitude) {
        this.id_jalur = id_jalur;
        this.node = node;
        this.latitude = latutide;
        this.longitude = longitude;
    }

    public LocationModel() {

    }

    public String getId_jalur() {
        return id_jalur;
    }
    public void setId_jalur(String id_jalur) {
        this.id_jalur = id_jalur;
    }

    public String getNode() {
        return node;
    }
    public void setNode(String node) {
        this.node = node;
    }

    public String getLatitude() {
        return latitude;
    }
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}

