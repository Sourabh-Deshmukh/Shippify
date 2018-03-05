package com.example.sourabh.major_project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Sourabh on 03-03-2018.
 */
/*
class MyAdapter {
    public MyAdapter(User_type user_type, String[] version, int[] images) {
    }
}*/
public class MyAdapter extends ArrayAdapter<String> {
    Context ctx;
    String[] v;
    int[] i;


    public MyAdapter(Context c,String[] version,int[] images){
        super(c, R.layout.row,version);
        this.ctx=c;
        this.v=version;
        this.i=images;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getcustomView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getcustomView(position, convertView, parent);
    }


    public View getcustomView(int position, View convertView, ViewGroup parent){
        if(convertView==null){
            LayoutInflater inflater=(LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView =inflater.inflate(R.layout.row,null);
        }
        TextView tv=(TextView)convertView.findViewById(R.id.tv1);
        tv.setText(v[position]);
        ImageView iv=(ImageView)convertView.findViewById(R.id.icon);
        iv.setImageResource(i[position]);
        return convertView;
    }




}
