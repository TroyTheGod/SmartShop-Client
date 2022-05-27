package com.example.man.smartshop.CusAdapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.man.smartshop.DBclass.Products;
import com.example.man.smartshop.R;

import java.util.ArrayList;

public class CusProductAdapter extends ArrayAdapter<Products> {
    private ArrayList<Products> arrayList;
    int layoutId;
    Context context;
    public CusProductAdapter(Context context,int layoutId, ArrayList<Products> arrayList){
        super(context,layoutId,arrayList);
        this.layoutId=layoutId;
        this.context=context;
        this.arrayList=arrayList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row=convertView;
        Holder holder=null;
        if (row==null){

            LayoutInflater inflater=((Activity)context).getLayoutInflater();
            row=inflater.inflate(layoutId,parent,false);

            holder = new Holder();
            holder.icon=(ImageView)row.findViewById(R.id.icon);
            holder.name=(TextView)row.findViewById(R.id.name);
            holder.info=(TextView)row.findViewById(R.id.info);
            row.setTag(holder);
        }else{
            holder=(Holder)row.getTag();
        }
        holder.name.setText(arrayList.get(position).name);
        holder.info.setText(arrayList.get(position).price);
        holder.icon.setImageResource(R.drawable.cat);
        return row;

    }
    static class Holder{
        ImageView icon;
        TextView name,info;
    }
}
