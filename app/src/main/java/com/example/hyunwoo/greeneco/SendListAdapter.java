package com.example.hyunwoo.greeneco;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by hyunwoo on 2018-01-22.
 */

public class SendListAdapter extends BaseAdapter {
    public ArrayList<SendViewItem> list = new ArrayList<>();
    private Activity activity;

    SendListAdapter(Activity activity) {
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.sendlist_item, parent, false);
        }

        TextView sendTextView1 = (TextView) convertView.findViewById(R.id.sendtextView1);
        TextView sendTextView2 = (TextView) convertView.findViewById(R.id.sendtextView2);
        TextView sendTextView3 = (TextView) convertView.findViewById(R.id.sendtextView3);
        TextView sendTextView4 = (TextView) convertView.findViewById(R.id.sendtextView4);

        SendViewItem sendViewItem = list.get(position);

        sendTextView1.setText(sendViewItem.getKind1());
        sendTextView2.setText(sendViewItem.getCreate1());
        sendTextView3.setText(Double.toString(sendViewItem.getKG1()));
        sendTextView4.setText(Double.toString(sendViewItem.getKg1()));

        if (list.get(position).getKG1().equals(list.get(position).getKg1())) {
            convertView.setBackgroundColor(0xFF99FF99);
        }
        if (list.get(position).status == false) {
            convertView.setBackgroundColor(0xFFFF9999);
        }
        if (list.get(position).getKG1() < list.get(position).getKg1() && list.get(position).getKG1() > 0) {
            convertView.setBackgroundColor(0xFF87CEEB);
        }
        return convertView;
    }

    public void addItem(String kind, String cre, Double KG, Double kg, boolean status) {
        SendViewItem item = new SendViewItem();

        item.setKind1(kind);
        item.setCreate1(cre);
        item.setKG1(KG);
        item.setKg1(kg);
        item.setStatus(status);
        list.add(item);
    }
}

