package com.example.man.smartshop;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.example.man.smartshop.DBclass.DBlocation;
import com.example.man.smartshop.DBclass.MyAsyncTask;
import com.example.man.smartshop.DBclass.Products;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

public class HoldP extends AppCompatActivity {
    public TextView view;
    public String url;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hold_view);
        view=(TextView)findViewById(R.id.hold);
        url=new DBlocation().DBURL;
        HashMap sql=new HashMap();
        sql.put("col","p.pid,p.pname,u.uid,u.uname, h.time");
        sql.put("table","usertable u, products p, hold h");
        sql.put("where","h.shopid=1 and h.pid=p.pid and h.uid=u.uid");
        sql.put("order","time desc");
        DBconn dBconn=new DBconn();
        dBconn.GetSql(sql);
        dBconn.execute(url);
    }
    public class DBconn extends MyAsyncTask{
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.v("HoldP:DBconn:result",result);
            String results=convJtoA(result);
            view.setText(results);
        }
    }
    public String convJtoA(String jsonStr){
        Log.v("JsonStr",jsonStr);
        JSONObject jsonObjectTable;
        Log.v("Wishlist:conj2a:result",jsonStr);
        String result="";
        try{
            jsonObjectTable = new JSONObject(jsonStr);
            JSONArray jsonArrayTable =jsonObjectTable.getJSONArray("result");
            for (int i=0;i<jsonArrayTable.length();i++){
                JSONObject jsonObjRow = jsonArrayTable.getJSONObject(i);
                result=result+jsonObjRow.getString("pname")+" 由用戶 ";
                result=result+jsonObjRow.getString("uname")+" 保留 ,時間: ";
                result=result+jsonObjRow.getString("time");
                result=result+"\n";
            }


        }catch (JSONException e){
            Log.v("mylog: ",e.toString());
        }
        return result;

    }
}
