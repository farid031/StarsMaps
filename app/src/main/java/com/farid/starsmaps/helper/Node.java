package com.farid.starsmaps.helper;

public class Node {
    private String id_node;
    private String nama_node;
    private String latitude;
    private String longitude;
    private String jml;

    public String getIdNode() {
        return id_node;
    }
    public void setIdNode(String id_node) {
        this.id_node = id_node;
    }
    public String getNamaNode() {
        return nama_node;
    }
    public void setNamaNode(String nama_node) {
        this.nama_node = nama_node;
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
    public String getJml() {
        return jml;
    }
    public String setJml(String jml) {
        this.jml = jml;
        return jml;
    }

}
