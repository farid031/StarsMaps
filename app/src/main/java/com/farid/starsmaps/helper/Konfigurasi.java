package com.farid.starsmaps.helper;

public class Konfigurasi {
    //Dibawah ini merupakan Pengalamatan dimana Lokasi Skrip CRUD PHP disimpan
    //Pada tutorial Kali ini, karena kita membuat localhost maka alamatnya tertuju ke IP komputer
    //dimana File PHP tersebut berada
    //PENTING! JANGAN LUPA GANTI IP SESUAI DENGAN IP KOMPUTER DIMANA DATA PHP BERADA
    //Base URL
    public static final String BASE_URL          = "http://runway.ikc.co.id/ta/";
    //public static final String BASE_URL    = "http://192.168.43.28/app_ta/android/";
    //public static final String BASE_URL        = "http://tugasakhirfarid.000webhostapp.com/";
    //Toko
    public static final String URL_READ_TOKO    = BASE_URL + "read_toko.php";
    //node
    public static final String URL_READ_NODE    = BASE_URL + "node.php?mode=getDataNode";
    //Perhitungan
    public static final String URL_HITUNG       = BASE_URL + "calPath.php";
    //Inputan hitungan
    public static final String URL_INPUT        = BASE_URL + "input.php";
    //Node awal dan akhir
    public static final String URL_NODE_AWAL         = BASE_URL + "awal_akhir.php";
    //Node awal dan akhir
    public static final String URL_UPDATE_AWAL  = BASE_URL + "update_awal.php";
    //Jarak
    public static final String URL_CREATE_JARWAK  = BASE_URL + "create_jw.php";

    public static final String URL_READ_JARAK  = BASE_URL + "read_jarak.php";

    public static final String URL_POLYLINE  = BASE_URL + "polyline.php";

    public static final String URL_JML_NODE  = BASE_URL + "read_jml_node.php";


    //Dibawah ini merupakan Kunci yang akan digunakan untuk mengirim permintaan ke Skrip PHP
    public static final String KEY_LATITUDE     = "lat";
    public static final String KEY_LONGITUDE    = "lon";
    public static final String KEY_TUJUAN       = "tujuan";
    public static final String KEY_JARAK_WAKTU  = "jarak_waktu";
    public static final String KEY_RUTE         = "url_rute";

    //JSON Tags
    public static final String TAG_SUCCESS   = "success";
    public static final String TAG_TOKO      = "toko";
    public static final String TAG_NODE      = "node";
    public static final String TAG_NODE_AWAL = "node_awal";
    public static final String TAG_NODE_AKHIR= "node_akhir";
    public static final String TAG_HASIL     = "hasil";
    public static final String TAG_JARAK     = "jarak";
    public static final String TAG_NO        = "no";
    public static final String TAG_STR_ID    = "str_id";
    public static final String TAG_NAMA      = "nama";
    public static final String TAG_ALAMAT    = "alamat";
    public static final String TAG_KABUPATEN = "kabupaten";
    public static final String TAG_PROVINSI  = "provinsi";
    public static final String TAG_LATITUDE  = "latitude";
    public static final String TAG_LONGITUDE = "longitude";
    public static final String TAG_TUJUAN    = "tujuan";
    public static final String TAG_SMS       = "sms";
    public static final String TAG_ASAL      = "asal";
    public static final String TAG_ID_NODE   = "id_node";
    public static final String TAG_NAMA_AWAL = "nama_node";
    public static final String TAG_LAT_NODE  = "lat_node";
    public static final String TAG_LON_NODE  = "lon_node";
    public static final String TAG_WAKTU     = "waktu";
    public static final String TAG_JARWAK    = "jarwak";
    public static final String TAG_LAT_AWAL  = "lat_awal";
    public static final String TAG_LON_AWAL  = "lon_awal";
    public static final String TAG_LAT_AKHIR = "lat_akhir";
    public static final String TAG_LON_AKHIR = "lon_akhir";
    public static final String TAG_JML_NODE  = "jml_node";
    public static final String TAG_JML       = "jml";
}
