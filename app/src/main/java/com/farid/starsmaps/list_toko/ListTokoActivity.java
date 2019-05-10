package com.farid.starsmaps.list_toko;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.farid.starsmaps.R;
import com.farid.starsmaps.helper.JSONParser;
import com.farid.starsmaps.helper.Konfigurasi;
import com.farid.starsmaps.maps_toko_activity.MapsTokoActivity;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ListTokoActivity extends AppCompatActivity implements TextWatcher {

    ListView list;
    JSONParser jParser = new JSONParser();
    ArrayList<ListToko> list_toko = new ArrayList<ListToko>();
    JSONArray listToko, daftarSearchToko = null;
    ListTokoAdapter adapter;
    EditText Query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_toko);

        list = findViewById(R.id.ListViewToko);
        Query = findViewById(R.id.EditTxtCariDataToko);

        Query.addTextChangedListener(this);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int urutan, long id) {
                //dapatkan data id, nama, nim mahasiswa dan simpan dalam variable string
                String str_id       = ((TextView) view.findViewById(R.id.Str_id)).getText().toString();
                String nama_toko    = ((TextView) view.findViewById(R.id.NamaToko)).getText().toString();
                String alamat       = ((TextView) view.findViewById(R.id.AlamatToko)).getText().toString();
                String kabupaten    = ((TextView) view.findViewById(R.id.Kabupaten)).getText().toString();
                String provinsi     = ((TextView) view.findViewById(R.id.Provinsi)).getText().toString();
                String latitude     = ((TextView) view.findViewById(R.id.Latitude)).getText().toString();
                String longitude    = ((TextView) view.findViewById(R.id.Longitude)).getText().toString();
                String sms          = ((TextView) view.findViewById(R.id.Sms)).getText().toString();

                //varible string tadi kita simpan dalam suatu Bundle, dan kita parse bundle tersebut bersama intent menuju kelas ViewDataActivity
                Intent i = null;
                i = new Intent(ListTokoActivity.this, MapsTokoActivity.class);
                Bundle b = new Bundle();
                b.putString("str_id", str_id);
                b.putString("nama_toko", nama_toko);
                b.putString("alamat", alamat);
                b.putString("kabupaten", kabupaten);
                b.putString("provinsi", provinsi);
                b.putString("latitude", latitude);
                b.putString("longitude", longitude);
                b.putString("sms", sms);
                i.putExtras(b);
                startActivity(i);
            }
        });

        //jalankan ReadDataTask
        ReadDataTask m= (ReadDataTask) new ReadDataTask().execute();
    }

    @SuppressLint("StaticFieldLeak")
    class ReadDataTask extends AsyncTask<String, String, String> {
        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ListTokoActivity.this);
            pDialog.setTitle("Mengambil Daftar Data Toko");
            pDialog.setMessage("Tunggu Sebentar");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... sText) {
            return getDataList();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pDialog.dismiss();
            if(result.equalsIgnoreCase("Exception Caught")){
                Toast.makeText(ListTokoActivity.this, "Unable to connect to server,please check your internet connection!", Toast.LENGTH_LONG).show();
            }
            if(result.equalsIgnoreCase("no results")){
                Toast.makeText(ListTokoActivity.this, "Data empty", Toast.LENGTH_LONG).show();
            }else {
                //mengambil adatper data toko
                adapter = new ListTokoAdapter(ListTokoActivity.this,list_toko);
                list.setAdapter(adapter);
            }
        }

        //method untuk memperoleh daftar mahasiswa dari JSON
        String getDataList(){
            ListToko tempToko = new ListToko();
            List<NameValuePair> parameter = new ArrayList<NameValuePair>();
            try {
                JSONObject json = jParser.makeHttpRequest(Konfigurasi.URL_READ_TOKO,"POST", parameter);

                int success = json.getInt(Konfigurasi.TAG_SUCCESS);
                if (success == 1) { //Ada record Data (SUCCESS = 1)
                    //Getting Array of daftar_mhs
                    listToko = json.getJSONArray(Konfigurasi.TAG_TOKO);
                    // looping through All daftar_mhs
                    for (int i = 0; i < listToko.length(); i++){

                        JSONObject c = listToko.getJSONObject(i);
                        tempToko = new ListToko();
                        tempToko.setNo(c.getString(Konfigurasi.TAG_NO));
                        tempToko.setStr_id(c.getString(Konfigurasi.TAG_STR_ID));
                        tempToko.setNama(c.getString(Konfigurasi.TAG_NAMA));
                        tempToko.setAlamat(c.getString(Konfigurasi.TAG_ALAMAT));
                        tempToko.setKabupaten(c.getString(Konfigurasi.TAG_KABUPATEN));
                        tempToko.setProvinsi(c.getString(Konfigurasi.TAG_PROVINSI));
                        tempToko.setLatitude(c.getString(Konfigurasi.TAG_LATITUDE));
                        tempToko.setLongitude(c.getString(Konfigurasi.TAG_LONGITUDE));
                        tempToko.setSms(c.getString(Konfigurasi.TAG_SMS));
                        list_toko.add(tempToko);
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
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String names = s.toString();
        String alamat = s.toString();
        List<ListToko> newList = new ArrayList<>();

        for (ListToko r : list_toko) {
            //ListToko akan mereplace list_toko
            String name = r.getNama();
            String alamat1 = r.getAlamat();
            if (name.toLowerCase().contains(names.toLowerCase()) || alamat1.toLowerCase().contains(alamat.toLowerCase())) {
                newList.add(r);
            }
        }

        //make seFilter method in adapter and pass your new list
        adapter.setFilter(newList);
    }
}
