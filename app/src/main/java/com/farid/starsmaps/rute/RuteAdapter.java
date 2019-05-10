package com.farid.starsmaps.rute;

import android.app.Activity;
import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.farid.starsmaps.R;

import java.util.ArrayList;

public class RuteAdapter extends BaseAdapter {
    private Activity activity;
    //private ArrayList<HashMap<String, String>> data;
    private ArrayList<Rute> data_node = new ArrayList<Rute>();

    private static LayoutInflater inflater = null;

    public RuteAdapter(Activity a, ArrayList<Rute> d) {
        activity = a;
        data_node = d;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    public int getCount() {
        return data_node.size();
    }

    public Object getItem(int position) {
        return data_node.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (convertView == null) {
            vi = inflater.inflate(R.layout.listview_rute, null);
        }
        TextView node = vi.findViewById(R.id.Node);
        TextView jarak = vi.findViewById(R.id.Jarak);
        TextView latitude = vi.findViewById(R.id.Latitude);
        TextView lattitude = vi.findViewById(R.id.Longitude);

        Rute daftar_node = data_node.get(position);
        node.setText(daftar_node.getNode());
        jarak.setText(daftar_node.getJarak());
        latitude.setText(daftar_node.getLatitude());
        lattitude.setText(daftar_node.getLongitude());

        return vi;
    }
}
