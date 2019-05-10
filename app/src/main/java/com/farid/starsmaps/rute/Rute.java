package com.farid.starsmaps.rute;

public class Rute {
    private String node;
    private String jarak;
    private String waktu;
    private String latitude;
    private String longitude;
    private String id_node;
    private String nama_node;
    private String lat_node;
    private String lon_node;

    public String setNode(String node) {
        this.node = node;
        return node;
    }

    public String getNode() {
        return node;
    }

    public String setJarak(String jarak) {
        this.jarak = jarak;
        return jarak;
    }

    public String getJarak() {
        return jarak;
    }

    public String getWaktu() {
        return waktu;
    }

    public String setWaktu(String waktu) {
        this.waktu = waktu;
        return waktu;
    }

    public String setLatitude(String latitude) {
        this.latitude = latitude;
        return latitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public String setLongitude(String longitude) {
        this.longitude = longitude;
        return longitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String setId_Node(String id_node) {
        this.id_node = id_node;
        return id_node;
    }

    public String getId_Node() {
        return id_node;
    }

    public String setNama_Node(String nama_node) {
        this.nama_node = nama_node;
        return nama_node;
    }

    public String getNama_Node() {
        return nama_node;
    }

    public String setLat_Node(String lat_node) {
        this.lat_node = lat_node;
        return lat_node;
    }

    public String getLat_Node() {
        return lat_node;
    }

    public String setLon_Node(String lon_node) {
        this.lon_node = lon_node;
        return lon_node;
    }

    public String getLon_Node() {
        return lon_node;
    }
}
