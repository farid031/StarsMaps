package com.farid.starsmaps.rute;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.farid.starsmaps.R;
import com.farid.starsmaps.helper.JSONParser;
import com.farid.starsmaps.helper.Konfigurasi;
import com.farid.starsmaps.helper.Node;
import com.farid.starsmaps.helper.NodeHandler;
import com.farid.starsmaps.helper.node_helper.ApiClient;
import com.farid.starsmaps.helper.node_helper.ApiNodeService;
import com.farid.starsmaps.helper.node_helper.ListLocationModel;
import com.farid.starsmaps.helper.node_helper.LocationModel;
import com.farid.starsmaps.helper.directions.TaskLoadedCallback;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class RuteActivity extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback, TaskLoadedCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    TextView TextLatitude, TextLongitude, TextTujuan, Status_Asal, TextLatAwal, TextLonAwal, TextMode, TextAvoid, TextJmlNode,
            TextLatAkhir, TextLonAkhir, TextJarak, TextNamaNodeAwal, TextNamanNodeAkhir, TextWaktu, TextJarakGoogle;
    String latitude, longitude, tujuan, lat_awal, lon_awal, lat_akhir, lon_akhir;
    ListView list;
    Button BtnHitung;
    List<String> valueIdNode = new ArrayList<String>();
    List<String> valueNamaNode = new ArrayList<String>();
    List<String> valueLatitude = new ArrayList<String>();
    List<String> valueLongitude = new ArrayList<String>();
    ProgressDialog pDialog;
    JSONArray JsonArrayNode = null;
    JSONArray daftarNodeAwal, daftarJmlNode, daftarJarakWaktu = null;
    private GoogleMap mMap;
    private List<LocationModel> mListMarker = new ArrayList<>();
    private Polyline currentPolyline;
    private MarkerOptions place1, place2;
    GoogleApiClient mGoogleApiClient;
    JSONParser jParser = new JSONParser();
    private Marker mCurrLocationMarker;
    Spinner spinnerTujuan, spinnerMode, spinnerAvoid;
    String[] str_avoid = {"Tidak Ada", "Hindari Jalan Tol", "Hindari Jalan Raya", "Hindari Kapal Feri"};
    String[] str_mode = {"Berkendara", "Bersepeda", "Jalan Kaki"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rute);
        //getJSON(Konfigurasi.URL_READ_TOKO);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        list = findViewById(R.id.ListJalur);

        TextLatitude = findViewById(R.id.TxtLat);
        TextLongitude = findViewById(R.id.TxtLon);
        TextTujuan = findViewById(R.id.TxtTujuan);
        Status_Asal = findViewById(R.id.StatusAsal);
        BtnHitung = findViewById(R.id.ButtonHitung);
        TextLatAwal = findViewById(R.id.Lat_awal);
        TextLonAwal = findViewById(R.id.Lon_awal);
        TextLatAkhir = findViewById(R.id.Lat_akhir);
        TextLonAkhir = findViewById(R.id.Lon_akhir);
        TextJarak = findViewById(R.id.TxtJarak);
        TextNamaNodeAwal = findViewById(R.id.TxtNamaNodeAwal);
        TextNamanNodeAkhir = findViewById(R.id.TxtNamaNodeAkhir);
        TextWaktu = findViewById(R.id.TxtWaktu);
        spinnerTujuan = findViewById(R.id.SpinnerTujuan);
        spinnerMode = findViewById(R.id.SpinnerMode);
        spinnerAvoid = findViewById(R.id.SpinnerAvoid);
        TextMode = findViewById(R.id.TxtMode);
        TextAvoid = findViewById(R.id.TxtAvoid);
        latitude = TextLatitude.getText().toString();
        longitude = TextLongitude.getText().toString();
        TextJarakGoogle = findViewById(R.id.textJarakGoogle);
        TextJmlNode = findViewById(R.id.textJmlNode);

        BtnHitung.setOnClickListener(this);

        //new getDataNodeTujuan().execute();

        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // inisialiasi Array Adapter dengan memasukkan string array di atas
        final ArrayAdapter<String> adapterAvoid = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, str_avoid);
        final ArrayAdapter<String> adapterMode = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, str_mode);

        adapterAvoid.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterMode.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // mengeset Array Adapter tersebut ke Spinner
        spinnerAvoid.setAdapter(adapterAvoid);
        spinnerMode.setAdapter(adapterMode);

        // mengeset listener untuk mengetahui saat item dipilih
        spinnerAvoid.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // memunculkan toast + value Spinner yang dipilih (diambil dari adapter)
                if (adapterAvoid.getItem(i).equalsIgnoreCase("Hindari Jalan Tol")) {
                    TextAvoid.setText(getString(R.string.tolls));
                }
                if (adapterAvoid.getItem(i).equalsIgnoreCase("Hindari Jalan Raya")) {
                    TextAvoid.setText(getString(R.string.highways));
                }
                if (adapterAvoid.getItem(i).equalsIgnoreCase("Hindari Kapal Feri")) {
                    TextAvoid.setText(getString(R.string.ferries));
                }
                if (adapterAvoid.getItem(i).equalsIgnoreCase("Tidak Ada")) {
                    TextAvoid.setText(getString(R.string.none));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerMode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // memunculkan toast + value Spinner yang dipilih (diambil dari adapter)
                if (adapterMode.getItem(i).equalsIgnoreCase("Berkendara")) {
                    TextMode.setText(getString(R.string.driving));
                }
                if (adapterMode.getItem(i).equalsIgnoreCase("Bersepeda")) {
                    TextMode.setText(getString(R.string.bicycling));
                }
                if (adapterMode.getItem(i).equalsIgnoreCase("Jalan Kaki")) {
                    TextMode.setText(getString(R.string.walking));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        LocationListener locListener = new MyLocationListener();
        //Mengolah data lokasi menggunakan GPS
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            return;
        }
        assert lm != null;
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 1, locListener);

        pertama();
    }

    @Override
    public void onClick(View v) {
        tujuan = TextTujuan.getText().toString();
        latitude = TextLatitude.getText().toString();
        longitude = TextLongitude.getText().toString();
        boolean isEmptyFields = false;
        if (TextUtils.isEmpty(latitude)) {
            isEmptyFields = true;
            TextLatitude.setError("Latitude Asal belum didapat");
        }
        if (TextUtils.isEmpty(longitude)) {
            isEmptyFields = true;
            TextLongitude.setError("Longitude Asal belum didapat");
        }
        if (!isEmptyFields) {
            Status_Asal.setText("Koordinat Asal telah didapat");

            //new ReadDataNodeTask().execute();
            //new ReadJarak().execute();
//            new UpdateDataAwalAkhir().execute();
//            new UpdateNodeAwal().execute();
//            new ReadDataNodeTask().execute();
//            getJarWak();


            double lat11 = Double.parseDouble((String) TextLatAwal.getText().toString().trim());
            double long11 = Double.parseDouble((String) TextLonAwal.getText().toString().trim());
            double lat22 = Double.parseDouble((String) TextLatAkhir.getText().toString().trim());
            double long22 = Double.parseDouble((String) TextLonAkhir.getText().toString().trim());
            String nawal = TextNamaNodeAwal.getText().toString().trim();
            String nakhir = TextNamanNodeAkhir.getText().toString().trim();
            DecimalFormat df = new DecimalFormat("#.###");
            mMap.clear();

            LatLng awal = new LatLng(lat11, long11);
            LatLng akhir = new LatLng(lat22, long22);

            mMap.addMarker(new MarkerOptions().position(awal).title(nawal).icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_start)));
            mMap.addMarker(new MarkerOptions().position(akhir).title(nakhir).icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_finish)));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(awal, 10));


            //double j = 111.319;
            //double euclid = (sqrt(pow((lat11 - lat22), 2) + pow((long11 - long22), 2))) * j;
            //String h = String.valueOf(df.format(euclid));
            //String koma = String.valueOf(TextJarakNode.getText());
            //String hasil = h.substring(-3);
            //TextJarak.setText(h + " Km");
            //System.out.println("Jarak Node = " + hasil);
            //getAllDataLocationLatLng();

            place1 = new MarkerOptions().position(awal).title("Location 1");
            place2 = new MarkerOptions().position(akhir).title("Location 2");
//            String url = getUrl(place1.getPosition(), place2.getPosition(),"driving");
//            new FetchURL(RuteActivity.this).execute(url,"driving");
            onJalurPressed();
        }
    }

    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
        double lat11 = Double.parseDouble(TextLatAwal.getText().toString().trim());
        double long11 = Double.parseDouble(TextLonAwal.getText().toString().trim());
        double lat22 = Double.parseDouble(TextLatAkhir.getText().toString().trim());
        double long22 = Double.parseDouble(TextLonAkhir.getText().toString().trim());
        double latitude = Double.parseDouble(TextLatitude.getText().toString().trim());
        double longitude = Double.parseDouble(TextLongitude.getText().toString().trim());
        //String avoid        = TextAvoid.getText().toString().trim();
        //String mode         = TextMode.getText().toString().trim();

        // Origin of route
        String str_origin = "origin=" + lat11 + "," + long11;
        // Destination of route
        String str_dest = "destination=" + lat22 + "," + long22;
        //Waypoints of route
        //String str_waypoints = "waypoints=via:" + latitude + "%2C" + longitude;

        //avoid route
        //String str_avoid = "avoid=" + avoid;
        String str_avoid = "avoid=tolls";

        // Mode
        //String str_mode = "mode=" + mode;
        String str_mode = "mode=driving";
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + str_avoid + "&" + str_mode;
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/json" + "?" + parameters + "&key=AIzaSyBTVcGt2fegGE6taEgiwhrQL7QWU5dgJC0";
        return url;
    }

    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = mMap.addPolyline((PolylineOptions) values[0]);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //Memulai Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        } else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }
    }

    protected void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
        mGoogleApiClient.connect();
    }

    private void getAllDataLocationLatLng() {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Menampilkan data marker ..");
        dialog.show();

        ApiNodeService apiComeService = ApiClient.getClient().create(ApiNodeService.class);
        Call<ListLocationModel> call = apiComeService.getAllLocation();
        call.enqueue(new Callback<ListLocationModel>() {
            @Override
            public void onResponse(Call<ListLocationModel> call, retrofit2.Response<ListLocationModel> response) {
                dialog.dismiss();
                mListMarker = response.body().getmData();
                initMarker(mListMarker);
            }

            @Override
            public void onFailure(Call<ListLocationModel> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(RuteActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initMarker(List<LocationModel> listData) {
        double latAwal = Double.parseDouble((String) TextLatAwal.getText());
        double lonAwal = Double.parseDouble((String) TextLonAwal.getText());
        double latAkhir = Double.parseDouble((String) TextLatAkhir.getText());
        double lonAkhir = Double.parseDouble((String) TextLonAkhir.getText());
        LatLng awal = new LatLng(latAwal, lonAwal);
        LatLng akhir = new LatLng(latAkhir, lonAkhir);

        //iterasi semua data dan tampilkan markernya
        //for (int i = 0; i < mListMarker.size(); i++)
        //int jml = Integer.parseInt(TextJmlNode.getText().toString().trim());
        for (int i = 0; i < mListMarker.size(); i++) {
            //set latlng nya
            LatLng location = new LatLng(Double.parseDouble(mListMarker.get(i).getLatitude()), Double.parseDouble(mListMarker.get(i).getLongitude()));
            //tambahkan markernya
            Marker markers = mMap.addMarker(new MarkerOptions().position(location).title(mListMarker.get(i).getNode()));

            System.out.println("Node = " + location);
            //set latlng index ke 0
            LatLng latLng = new LatLng(-7.346961, 112.739786);
            //lalu arahkan zooming ke marker index ke 0
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latLng.latitude, latLng.longitude), 11.0f));

//            String str_origin = "origin=" + TextLatAwal + "," + TextLonAwal;
//            String str_dest = "destination=" + TextLatAkhir + "," + TextLonAkhir;
//            String sensor = "sensor=false";
//            String parameters = str_origin + "&" + str_dest + "&" + sensor;
//            String output = "json";
//            String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters+ "&key=" +getString(R.string.google_maps_key);
//
//            Log.d("onMapClick", url.toString());
//            FetchUrl FetchUrl = new FetchUrl();
//            FetchUrl.execute(url);
        }

        mMap.addMarker(new MarkerOptions().position(awal));
        mMap.addMarker(new MarkerOptions().position(akhir));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(awal, 10));
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                TextLatitude.setText(String.valueOf(location.getLatitude()));
                TextLongitude.setText(String.valueOf(location.getLongitude()));
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String arg0) {

        }

        @Override
        public void onProviderDisabled(String arg0) {

        }
    }

    @Override
    public void onLocationChanged(Location location) {
        Location mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(latLng.latitude, latLng.longitude)).zoom(16).build();

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        //menghentikan pembaruan lokasi
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }

    }

    //Update Node Awal
    @SuppressLint("StaticFieldLeak")
    class UpdateNodeAwal extends AsyncTask<String, Void, String> {

        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(RuteActivity.this);
            pDialog.setMessage("Mengupdate Node Awal..");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... sText) {
            List<NameValuePair> parameter = new ArrayList<NameValuePair>();

            try {
                JSONObject json = jParser.makeHttpRequest(Konfigurasi.URL_UPDATE_AWAL, "POST", parameter);
                jParser.makeHttpRequest(Konfigurasi.URL_HITUNG, "POST", parameter);

                int success = json.getInt(Konfigurasi.TAG_SUCCESS);
                if (success == 1) {
                    return "OK";
                } else {
                    return "FAIL";
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "Exception Caught";
            }
        }

        @Override
        protected void onPostExecute(String result) {

            super.onPostExecute(result);
            pDialog.dismiss();
            //new UpdateDataAwalAkhir().execute();
        }
    }

    //Menampilkan data tujuan di spinner
    @SuppressLint("StaticFieldLeak")
    class ReadDataNodeTask extends AsyncTask<String, String, String> {
        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(RuteActivity.this);
            pDialog.setMessage("Mengambil koordinat Node Awal...");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... sText) {
            Rute tempMarkerAwal = new Rute();
            List<NameValuePair> parameter = new ArrayList<NameValuePair>();
            try {
                JSONObject json = jParser.makeHttpRequest(Konfigurasi.URL_NODE_AWAL, "POST", parameter);

                Log.d("Response: ", "> " + json);

                int success = json.getInt(Konfigurasi.TAG_SUCCESS);
                if (success == 1) { //Ada record Data (SUCCESS = 1)
                    //Getting Array of daftar_mhs
                    daftarNodeAwal = json.getJSONArray(Konfigurasi.TAG_NODE_AWAL);
                    //looping through All daftar_mhs
                    for (int i = 0; i < daftarNodeAwal.length(); i++) {
                        JSONObject c = daftarNodeAwal.getJSONObject(i);
                        //tempMarkerAwal = new Rute();
                        String id_node = tempMarkerAwal.setId_Node(c.getString(Konfigurasi.TAG_ID_NODE));
                        final String nama_awal = tempMarkerAwal.setNama_Node(c.getString(Konfigurasi.TAG_NAMA_AWAL));
                        final String lat_node_awal = tempMarkerAwal.setLat_Node(c.getString(Konfigurasi.TAG_LAT_NODE));
                        final String lon_node_awal = tempMarkerAwal.setLon_Node(c.getString(Konfigurasi.TAG_LON_NODE));

                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            public void run() {
                                TextNamaNodeAwal.setText(nama_awal);
                                TextLatAwal.setText(lat_node_awal);
                                TextLonAwal.setText(lon_node_awal);
                            }
                        });
                    }
                    return "OK";
                } else {
                    //Tidak Ada Record Data (SUCCESS = 0)
                    return "no results";
                }

            } catch (Exception e) {
                e.printStackTrace();
                return "Exception Caught";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pDialog.dismiss();
//            if(result.equalsIgnoreCase("Exception Caught")){
//                Toast.makeText(RuteActivity.this, "Gagal mengambil data node, tidak bisa terhubung ke server", Toast.LENGTH_LONG).show();
//            }
//            if(result.equalsIgnoreCase("no results")){
//                Toast.makeText(RuteActivity.this, "Data empty", Toast.LENGTH_LONG).show();
//            }else {
//                Toast.makeText(RuteActivity.this, "Data Node awal berhasil didapat", Toast.LENGTH_SHORT).show();
//            }
            //new getDataNodeTujuan().execute();
        }
    }

    //Mendapatkan Node Tujuan
    @SuppressLint("StaticFieldLeak")
    private class getDataNodeTujuan extends AsyncTask<Void, Void, Void> {
        ProgressDialog loading;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = new ProgressDialog(RuteActivity.this);
            loading.setTitle("Mengambil Data Tujuan");
            loading.setMessage("Tunggu Sebentar");
            loading.setIndeterminate(false);
            loading.setCancelable(false);
            loading.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            //Membuat Service "ServiceHandler"
            NodeHandler ah = new NodeHandler();

            // Memanggil URL untuk mendapatkan respon data
            String jsonStr = ah.makeServiceCall(Konfigurasi.URL_READ_NODE, NodeHandler.GET); //siswaManager.php?mode=getAllDataSiswa

            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Mendapatkan data Array JSON
                    JsonArrayNode = jsonObj.getJSONArray("values");

                    ArrayList<Node> listDataNode = new ArrayList<Node>();
                    listDataNode.clear();

                    //Melakukan perulangan untuk memecah data
                    for (int i = 0; i < JsonArrayNode.length(); i++) {
                        JSONObject obj = JsonArrayNode.getJSONObject(i);

                        Node node = new Node();
                        node.setIdNode(obj.getString("id_node"));
                        node.setNamaNode(obj.getString("nama_node"));
                        node.setLatitude(obj.getString("latitude"));
                        node.setLongitude(obj.getString("longitude"));
                        listDataNode.add(node);
                    }

                    valueIdNode = new ArrayList<String>();
                    valueNamaNode = new ArrayList<String>();
                    valueLatitude = new ArrayList<String>();
                    valueLongitude = new ArrayList<String>();

                    for (int i = 0; i < listDataNode.size(); i++) {
                        valueIdNode.add(listDataNode.get(i).getIdNode());
                        valueNamaNode.add(listDataNode.get(i).getNamaNode());
                        valueLatitude.add(listDataNode.get(i).getLatitude());
                        valueLongitude.add(listDataNode.get(i).getLongitude());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (loading.isShowing())
                loading.dismiss();

            // Membuat adapter untuk spinner
            ArrayAdapter<String> spinnerAdapterNode = new ArrayAdapter<String>(RuteActivity.this,
                    android.R.layout.simple_spinner_item, valueNamaNode);

            spinnerAdapterNode.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            //Mengaitkan adapter spinner dengan spinner yang ada di layout
            spinnerTujuan.setAdapter(spinnerAdapterNode);
            spinnerTujuan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    String IdNode = valueIdNode.get(position);
                    String NamaNode = valueNamaNode.get(position);
                    String Latitude = valueLatitude.get(position);
                    String Longitude = valueLongitude.get(position);
                    TextTujuan.setText(IdNode);
                    TextLatAkhir.setText(Latitude);
                    TextLonAkhir.setText(Longitude);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    // your code here
                }
            });
            //new UpdateNodeAwal().execute();
        }
    }

    @SuppressLint("StaticFieldLeak")
    class ReadJumlahNode extends AsyncTask<String, String, String> {
        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            pDialog = new ProgressDialog(RuteActivity.this);
//            pDialog.setTitle("Mengambil Daftar Data Toko");
//            pDialog.setMessage("Tunggu Sebentar");
//            pDialog.setIndeterminate(false);
//            pDialog.setCancelable(false);
//            pDialog.show();
        }

        @Override
        protected String doInBackground(String... sText) {
            Node tempToko = new Node();
            List<NameValuePair> parameter = new ArrayList<NameValuePair>();
            try {
                JSONObject json = jParser.makeHttpRequest(Konfigurasi.URL_JML_NODE,"POST", parameter);

                int success = json.getInt(Konfigurasi.TAG_SUCCESS);
                if (success == 1) { //Ada record Data (SUCCESS = 1)
                    //Getting Array of daftar_mhs
                    daftarJmlNode = json.getJSONArray(Konfigurasi.TAG_JML_NODE);
                    // looping through All daftar_mhs
                    for (int i = 0; i < daftarJmlNode.length(); i++){

                        JSONObject c = daftarJmlNode.getJSONObject(i);
                        tempToko = new Node();
                        final String jml = tempToko.setJml(c.getString(Konfigurasi.TAG_JML));

                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            public void run() {
                                TextJmlNode.setText(jml);
                            }
                        });
                    }
                    return "OK";
                }else{
                    //Tidak Ada Record Data (SUCCESS = 0)
                    return "no results";
                }

            } catch (Exception e) {
                e.printStackTrace();
                return "Exception Caught";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }
    }

    //Mengupdate node awal dan akhir
    @SuppressLint("StaticFieldLeak")
    class UpdateDataAwalAkhir extends AsyncTask<String, Void, String> {

        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(RuteActivity.this);
            pDialog.setMessage("Mengupdate Node Pertama..");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... sText) {

            String tujuanAwal = TextTujuan.getText().toString().trim();
            String latitudeAwal = TextLatitude.getText().toString().trim();
            String longitudeAwal = TextLongitude.getText().toString().trim();

            List<NameValuePair> parameter = new ArrayList<NameValuePair>();
            parameter.add(new BasicNameValuePair(Konfigurasi.TAG_TUJUAN, tujuanAwal));
            parameter.add(new BasicNameValuePair(Konfigurasi.TAG_LATITUDE, latitudeAwal));
            parameter.add(new BasicNameValuePair(Konfigurasi.TAG_LONGITUDE, longitudeAwal));

            try {
                JSONObject json = jParser.makeHttpRequest(Konfigurasi.URL_INPUT, "POST", parameter);

                int success = json.getInt(Konfigurasi.TAG_SUCCESS);
                if (success == 1) {
                    return "OK";
                } else {
                    return "FAIL";
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "Exception Caught";
            }
        }

        @Override
        protected void onPostExecute(String result) {

            super.onPostExecute(result);
            pDialog.dismiss();
        }
    }

    private void getJarWak() {

        final String lat11 = TextLatAwal.getText().toString().trim();
        final String long11 = TextLonAwal.getText().toString().trim();
        final String lat22 = TextLatAkhir.getText().toString().trim();
        final String long22 = TextLonAkhir.getText().toString().trim();

        // Origin of route
        String str_origin = "origin=" + lat11 + "," + long11;
        // Destination of route
        String str_dest = "destination=" + lat22 + "," + long22;
        //Waypoints of route
        //String str_waypoints = "waypoints=via:" + latitude + "%2C" + longitude;

        //avoid route
        //String str_avoid = "avoid=" + avoid;
        String str_avoid = "avoid=tolls";

        // Mode
        //String str_mode = "mode=" + mode;
        String str_mode = "mode=driving";
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + str_avoid + "&" + str_mode;
        // Building the url to the web service
        final String url = "https://maps.googleapis.com/maps/api/directions/json" + "?" + parameters + "&key=AIzaSyBTVcGt2fegGE6taEgiwhrQL7QWU5dgJC0";

        @SuppressLint("StaticFieldLeak")
        class getJarakWaktu extends AsyncTask<String, String, String> {
            private ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = new ProgressDialog(RuteActivity.this);
                loading.setTitle("Mengambil Jarak Google Maps");
                loading.setMessage("Tunggu Sebentar");
                loading.setIndeterminate(false);
                loading.setCancelable(false);
                loading.show();
                //loading = ProgressDialog.show(RuteActivity.this, "Menambahkan...", "Tunggu...", false, false);
            }

            @Override
            protected String doInBackground(String... strings) {
                Rute tempMarkerAwal = new Rute();
                List<NameValuePair> parameter = new ArrayList<NameValuePair>();
                parameter.add(new BasicNameValuePair(Konfigurasi.TAG_LAT_AWAL, lat11));
                parameter.add(new BasicNameValuePair(Konfigurasi.TAG_LON_AWAL, long11));
                parameter.add(new BasicNameValuePair(Konfigurasi.TAG_LAT_AKHIR, lat22));
                parameter.add(new BasicNameValuePair(Konfigurasi.TAG_LON_AKHIR, long22));

                try {
                    JSONObject json = jParser.makeHttpRequest(Konfigurasi.URL_CREATE_JARWAK, "POST", parameter);
                    int success = json.getInt(Konfigurasi.TAG_SUCCESS);
                    if (success == 1) {
                        //Getting Array of daftar_mhs
                        daftarJarakWaktu = json.getJSONArray(Konfigurasi.TAG_JARWAK);
                        //looping through All daftar_mhs
                        for (int i = 0; i < daftarJarakWaktu.length(); i++) {
                            JSONObject c = daftarJarakWaktu.getJSONObject(i);
                            //tempMarkerAwal = new Rute();
                            final String jarak = tempMarkerAwal.setJarak(c.getString(Konfigurasi.TAG_JARAK));
                            final String waktu = tempMarkerAwal.setWaktu(c.getString(Konfigurasi.TAG_WAKTU));

                            Handler handler = new Handler(Looper.getMainLooper());
                            handler.post(new Runnable() {
                                public void run() {
                                    TextJarakGoogle.setText(jarak);
                                    //TextWaktu.setText(waktu);
                                }
                            });
                        }
                        return "OK";
                    } else {
                        return "FAIL";
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return "Exception Caught";
                }

//                RequestHandler rh = new RequestHandler();
//                return rh.sendPostRequest(Konfigurasi.URL_CREATE_JARWAK, params);
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                loading.dismiss();
                //Toast.makeText(RuteActivity.this, s, Toast.LENGTH_LONG).show();
            }
        }

        new getJarakWaktu().execute();
    }

    @SuppressLint("StaticFieldLeak")
    class getJarakWaktu extends AsyncTask<String, String, String> {
        private ProgressDialog loading;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = new ProgressDialog(RuteActivity.this);
            loading.setTitle("Membuat rute jalan");
            loading.setMessage("Tunggu Sebentar");
            loading.setIndeterminate(false);
            loading.setCancelable(false);
            loading.show();
            //loading = ProgressDialog.show(RuteActivity.this, "Menambahkan...", "Tunggu...", false, false);
        }

        @Override
        protected String doInBackground(String... strings) {
            Rute tempMarkerAwal = new Rute();
            List<NameValuePair> parameter = new ArrayList<NameValuePair>();

            try {
                JSONObject json = jParser.makeHttpRequest(Konfigurasi.URL_READ_JARAK, "POST", parameter);
                int success = json.getInt(Konfigurasi.TAG_SUCCESS);
                if (success == 1) {
                    //Getting Array of daftar_mhs
                    daftarJarakWaktu = json.getJSONArray(Konfigurasi.TAG_JARAK);
                    //looping through All daftar_mhs
                    for (int i = 0; i < daftarJarakWaktu.length(); i++) {
                        JSONObject c = daftarJarakWaktu.getJSONObject(i);
                        //tempMarkerAwal = new Rute();
                        final String jarak = tempMarkerAwal.setJarak(c.getString(Konfigurasi.TAG_JARAK));

                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            public void run() {
                                TextJarak.setText(jarak + " Km");
                                //TextWaktu.setText(waktu);
                            }
                        });
                    }
                    return "OK";
                } else {
                    return "FAIL";
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "Exception Caught";
            }

//                RequestHandler rh = new RequestHandler();
//                return rh.sendPostRequest(Konfigurasi.URL_CREATE_JARWAK, params);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            loading.dismiss();
            //Toast.makeText(RuteActivity.this, s, Toast.LENGTH_LONG).show();
        }
    }

    @SuppressLint("StaticFieldLeak")
    class getPolyline extends AsyncTask<String, String, String> {
        private ProgressDialog loading;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            loading = new ProgressDialog(RuteActivity.this);
//            loading.setTitle("Membuat rute jalan");
//            loading.setMessage("Tunggu Sebentar");
//            loading.setIndeterminate(false);
//            loading.setCancelable(false);
//            loading.show();
            //loading = ProgressDialog.show(RuteActivity.this, "Menambahkan...", "Tunggu...", false, false);
        }

        @Override
        protected String doInBackground(String... strings) {
            Rute tempMarkerAwal = new Rute();
            List<NameValuePair> parameter = new ArrayList<NameValuePair>();
            double lat = Double.parseDouble(String.valueOf(TextLatitude.getText()));
            double lon = Double.parseDouble(String.valueOf(TextLongitude.getText()));

            final LatLng asal = new LatLng(lat,lon);

            final PolylineOptions options = new PolylineOptions().width(3).color(Color.RED).geodesic(true);
            options.add(asal);
            try {
                JSONObject json = jParser.makeHttpRequest(Konfigurasi.URL_POLYLINE, "POST", parameter);
                int success = json.getInt(Konfigurasi.TAG_SUCCESS);
                if (success == 1) {
                    //Getting Array of daftar_mhs
                    daftarJarakWaktu = json.getJSONArray("polyline");
                    //looping through All daftar_mhs

                    for (int i = 0; i < daftarJarakWaktu.length(); i++) {
                        JSONObject c = daftarJarakWaktu.getJSONObject(i);
                        //tempMarkerAwal = new Rute();
                        final String nama = tempMarkerAwal.setJarak(c.getString("node"));
                        final String latitude = tempMarkerAwal.setJarak(c.getString("latitude"));
                        final String longitude = tempMarkerAwal.setJarak(c.getString("longitude"));

                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            public void run() {
                                for (int z = 0; z < daftarJarakWaktu.length(); z++) {
                                    LatLng point = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
                                    options.add(point);
                                }

                                mMap.addPolyline(options);
                                //TextWaktu.setText(waktu);
                            }
                        });
                    }
                    return "OK";
                } else {
                    return "FAIL";
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "Exception Caught";
            }

//                RequestHandler rh = new RequestHandler();
//                return rh.sendPostRequest(Konfigurasi.URL_CREATE_JARWAK, params);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //loading.dismiss();
            //Toast.makeText(RuteActivity.this, s, Toast.LENGTH_LONG).show();
        }
    }

    public void onJalurPressed() {
        new UpdateNodeAwal().execute();
        new UpdateDataAwalAkhir().execute();
        new ReadDataNodeTask().execute();
        new UpdateNodeAwal().execute();
        mMap.clear();
        new getJarakWaktu().execute();
        new getPolyline().execute();
        //new ReadJumlahNode().execute();
        getAllDataLocationLatLng();
        getJarWak();
    }

    public void pertama() {
        //new ReadJumlahNode().execute();
        new ReadDataNodeTask().execute();
        new UpdateNodeAwal().execute();
        new getDataNodeTujuan().execute();
    }
}