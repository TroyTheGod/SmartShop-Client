package com.example.man.smartshop.CusAdapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.man.smartshop.DBclass.Friend;
import com.example.man.smartshop.DBclass.Products;
import com.example.man.smartshop.R;

import java.util.ArrayList;

public class CusFriendApt extends ArrayAdapter<Friend> {
    private ArrayList<Friend> arrayList;
    int layoutId;
    Context context;
    public CusFriendApt(Context context,int layoutId, ArrayList<Friend> arrayList){
        super(context,layoutId,arrayList);
        this.layoutId=layoutId;
        this.context=context;
        this.arrayList=arrayList;
    }
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row=convertView;
        CusFriendApt.Holder holder=null;
        if (row==null){

            LayoutInflater inflater=((Activity)context).getLayoutInflater();
            row=inflater.inflate(layoutId,parent,false);

            holder = new CusFriendApt.Holder();
            holder.icon=(ImageView)row.findViewById(R.id.icon);
            holder.name=(TextView)row.findViewById(R.id.name);
            row.setTag(holder);
        }else{
            holder=(CusFriendApt.Holder)row.getTag();
        }
        holder.name.setText(arrayList.get(position).uname);
        holder.icon.setImageResource(R.drawable.cat);
        return row;

    }
    static class Holder{
        ImageView icon;
        TextView name;
    }
}
