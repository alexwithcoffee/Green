package com.example.hyunwoo.greeneco;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by hyunwoo on 2017-11-24.
 */

public class BarcodeItemView extends LinearLayout {
    TextView textView5;
    TextView textView6;
    TextView textView7;
    TextView textView8;
    TextView textView9;
    TextView textView15;
    TextView textView17;

    public BarcodeItemView(Context context) {
        super(context);

        init(context);
    }

    private void init(Context context) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        inflater.inflate(R.layout.barcode_item, this, true);

        textView5 = (TextView) findViewById(R.id.textView5);
        textView6 = (TextView) findViewById(R.id.textView6);
        textView7 = (TextView) findViewById(R.id.textView7);
        textView8 = (TextView) findViewById(R.id.textView8);
        textView9 = (TextView) findViewById(R.id.textView9);
        textView15 = (TextView) findViewById(R.id.textView15);
        textView17 = (TextView) findViewById(R.id.textView17);
    }

    public void setKind(String kind) {
        textView6.setText(kind);
    }

    public void setDday(String day) {
        textView7.setText(day);
    }

    public void setName(Double name) {
        textView8.setText(Double.toString(name));
    }

    public void setKg(Double kg) {
        textView9.setText(Double.toString(kg));
    }

    public void setBc(String bc) {
        textView15.setText(bc);
    }

    public void setNo(String no) {
        textView5.setText(no + "");
    }

    public void setPro(String pro) {
        textView17.setText(pro);
    }
}

