package com.example.man.smartshop;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.man.smartshop.DBclass.CheckLogin;
import com.example.man.smartshop.DBclass.DBlocation;
import com.example.man.smartshop.DBclass.Friend;
import com.example.man.smartshop.DBclass.MyAsyncTask;
import com.example.man.smartshop.DBclass.Shops;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Shopinfo extends AppCompatActivity {
    EditText sid,sname,sloc,stime,sphone,info;
    Button change,submit;
    String uid,selurl,updateurl,oldsname,oldsloc,oldstime,oldsphone,oldinfo,newsname,newsloc,newstime,newsphone,newinfo;
    Boolean Islogin;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(Color.parseColor("#09c6df"));
        setContentView(R.layout.storestaffprofile);
        CheckLogin checkLogin=new CheckLogin(getApplicationContext());
        Islogin=checkLogin.Check();
        uid=checkLogin.Uid;
        Log.v("Shopinfo:uid",uid);
        sid=(EditText)findViewById(R.id.edtshopid);
        sname=(EditText)findViewById(R.id.edtsname);
        sloc=(EditText)findViewById(R.id.edtslocation);
        stime=(EditText)findViewById(R.id.edtstime);
        sphone=(EditText)findViewById(R.id.edtsphone);
        info=(EditText)findViewById(R.id.edtinfo);
        change=(Button)findViewById(R.id.btnchange);
        submit=(Button)findViewById(R.id.btnsubmit);

        DBlocation dBlocation=new DBlocation();
        selurl=dBlocation.DBURL;
        updateurl=dBlocation.UPDATE;
        HashMap sql=new HashMap();
        sql.put("col","shopid, phoneNo, location, time, shopName, info");
        sql.put("table","shops");
        sql.put("where","uid="+uid);
        sql.put("order","SsNoInput");
        DBconn dBconn=new DBconn();
        dBconn.GetSql(sql);
        dBconn.execute(selurl);

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setEditable();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap sql=new HashMap();
                sql.put("table","shops");
                sql.put("values","phoneNo ='"+sphone.getText().toString()+"', location='"+sloc.getText().toString()+"', time='"+stime.getText().toString()+"', info='"+info.getText().toString());
                Log.v("Shopinfo:submit:update","phoneNo ='"+sphone.getText().toString()+"', location='"+sloc.getText().toString()+"', time='"+stime.getText().toString()+"', info='"+info.getText().toString()+"'");
                sql.put("where","uid="+uid);
                UpdateConn updateConn=new UpdateConn();
                updateConn.GetSql(sql);
                updateConn.execute(updateurl);
            }
        });
    }
    public class UpdateConn extends MyAsyncTask{
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.v("UpdateConn:do","");
            Log.v("Shopinfo:updateconn",result);
        }
    }
    public class DBconn extends MyAsyncTask{
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.v("Shopinfo:dbconn:result",result);
            ArrayList<Shops> arrayList=new ArrayList<Shops>();
            arrayList=convJtoA(result);
            setinfo(arrayList);

        }
    }

    public static ArrayList<Shops> convJtoA(String jsonStr){
        JSONObject jsonObjectTable;
        ArrayList<Shops> arrayList=new ArrayList<Shops>();
        Log.v("conj2a:jasonstr:",jsonStr);
        try{
            jsonObjectTable = new JSONObject(jsonStr);
            JSONArray jsonArrayTable =jsonObjectTable.getJSONArray("result");

            for (int i=0;i<jsonArrayTable.length();i++){
                JSONObject jsonObjRow = jsonArrayTable.getJSONObject(i);
                Shops shops=new Shops();
                shops.time=jsonObjRow.getString("time");
                shops.shopeName=jsonObjRow.getString("shopName");
                shops.shopId=jsonObjRow.getString("shopid");
                shops.info=jsonObjRow.getString("info");
                shops.location=jsonObjRow.getString("location");
                shops.phoneNo=jsonObjRow.getString("phoneNo");
                arrayList.add(shops);
            }
        }catch (JSONException e){
            Log.v("mylog: ",e.toString());
        }
        return arrayList;
    }
    public void setinfo(ArrayList<Shops> arrayList){
        sid.setText(arrayList.get(0).shopId);
        sname.setText(arrayList.get(0).shopeName);
        sloc.setText(arrayList.get(0).location);
        sphone.setText(arrayList.get(0).phoneNo);
        stime.setText(arrayList.get(0).time);
        info.setText(arrayList.get(0).info);
    }
    public void setEditable(){
        sname.setFocusable(true);
        sloc.setFocusable(true);
        sphone.setFocusable(true);
        stime.setFocusable(true);
        info.setFocusable(true);
        sname.setFocusableInTouchMode(true);
        sloc.setFocusableInTouchMode(true);
        sphone.setFocusableInTouchMode(true);
        stime.setFocusableInTouchMode(true);
        info.setFocusableInTouchMode(true);
        oldsname=sname.getText().toString();
        oldinfo=info.getText().toString();
        oldsloc=sloc.getText().toString();
        oldstime=stime.getText().toString();
        oldsphone=sphone.getText().toString();
    }
}
