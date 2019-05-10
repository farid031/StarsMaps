package com.farid.starsmaps.list_toko;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.farid.starsmaps.R;

import java.util.ArrayList;
import java.util.List;

public class ListTokoAdapter extends BaseAdapter {
    //private ArrayList<HashMap<String, String>> data;
    private ArrayList<ListToko> list_toko = new ArrayList<ListToko>();
    private static LayoutInflater inflater = null;

    public ListTokoAdapter(Activity a, ArrayList<ListToko> d) {
        list_toko = d;
        inflater = (LayoutInflater) a.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return list_toko.size();
    }

    public Object getItem(int position) {
        return list_toko.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (convertView == null) {
            vi = inflater.inflate(R.layout.listview_listtoko, null);
        }
        TextView no = vi.findViewById(R.id.No);
        TextView str_id = vi.findViewById(R.id.Str_id);
        TextView nama = vi.findViewById(R.id.NamaToko);
        TextView alamat = vi.findViewById(R.id.AlamatToko);
        TextView kabupaten = vi.findViewById(R.id.Kabupaten);
        TextView provinsi = vi.findViewById(R.id.Provinsi);
        TextView latitude = vi.findViewById(R.id.Latitude);
        TextView longitude = vi.findViewById(R.id.Longitude);
        TextView sms = vi.findViewById(R.id.Sms);


        ListToko daftar_toko = list_toko.get(position);
        no.setText(daftar_toko.getNo());
        str_id.setText(daftar_toko.getStr_id());
        nama.setText(daftar_toko.getNama());
        alamat.setText(daftar_toko.getAlamat());
        kabupaten.setText(daftar_toko.getKabupaten());
        provinsi.setText(daftar_toko.getProvinsi());
        latitude.setText(daftar_toko.getLatitude());
        longitude.setText(daftar_toko.getLongitude());
        sms.setText(daftar_toko.getSms());
        return vi;
    }

    public void setFilter(List<ListToko> newList){
        list_toko = new ArrayList<>();
        list_toko.addAll(newList);
        notifyDataSetChanged();
    }
}
