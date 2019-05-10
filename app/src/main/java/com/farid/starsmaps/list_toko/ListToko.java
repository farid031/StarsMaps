package com.farid.starsmaps.list_toko;

//ini adalah getter dan setter list toko
public class ListToko {
    private String no;
    private String str_id;
    private String nama;
    private String alamat;
    private String kabupaten;
    private String provinsi;
    private String latitude;
    private String longitude;
    private String sms;

    public void setNo(String no) {
        this.no = no;
    }
    String getNo() {
        return no;
    }

    public void setStr_id (String str_id) {
        this.str_id = str_id;
    }
    String getStr_id() {
        return str_id;
    }

    public void setNama (String nama) {
        this.nama = nama;
    }
    public String getNama() {
        return nama;
    }

    public void setAlamat (String alamat) {
        this.alamat = alamat;
    }
    String getAlamat() {
        return alamat;
    }

    public void setKabupaten (String kabupaten) {
        this.kabupaten = kabupaten;
    }
    String getKabupaten() {
        return kabupaten;
    }

    public void setProvinsi (String provinsi) {
        this.provinsi = provinsi;
    }
    String getProvinsi() {
        return provinsi;
    }

    public void setLatitude (String latitude) {
        this.latitude = latitude;
    }
    String getLatitude() {
        return latitude;
    }

    public void setLongitude (String longitude) {
        this.longitude = longitude;
    }
    String getLongitude() {
        return longitude;
    }

    public void setSms (String sms) {
        this.sms = sms;
    }
    String getSms() {
        return sms;
    }
}
