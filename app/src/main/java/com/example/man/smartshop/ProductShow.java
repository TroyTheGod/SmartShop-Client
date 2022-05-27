package com.example.man.smartshop;

import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.man.smartshop.DBclass.CheckLogin;
import com.example.man.smartshop.DBclass.DBlocation;
import com.example.man.smartshop.DBclass.Friend;
import com.example.man.smartshop.DBclass.MyAsyncTask;
import com.example.man.smartshop.DBclass.Products;
import com.example.man.smartshop.DBclass.Shops;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProductShow extends AppCompatActivity {
    private TextView  productName,productPrice,shopName,location,time,phoneNo;
    private String shopid,name,price,pid;
    private ImageButton wish;
    public Button hold;
    public boolean Islogin;
    public String insertUrl;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product);
        getWindow().setStatusBarColor(Color.parseColor("#09c6df"));
        DBlocation dBlocation=new DBlocation();
        insertUrl=dBlocation.GetInsertURL();
        String URL=dBlocation.GetURL();
        new MyAsyncTask().execute(URL);
        Bundle bundle=getIntent().getExtras();
        name =bundle.getString("name");
        price =bundle.getString("price");
        shopid=bundle.getString("shopid");
        pid=bundle.getString("pid");
        Log.v("ProLog",name);
        Log.v("ProLog",price);
        productName = (TextView)findViewById(R.id.productName);
        productPrice = (TextView)findViewById(R.id.productPrice);
        productName.setText(name);
        productPrice.setText("價錢: $"+price);
        wish=(ImageButton)findViewById(R.id.wish) ;
        hold=(Button)findViewById(R.id.hold) ;
        wish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dowish();
            }
        });
        hold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckLogin checkLogin=new CheckLogin(getApplicationContext());
                Islogin=checkLogin.Check();
                Log.v("ProdShow:dohold:islogin",""+Islogin);
                if(Islogin==false){
                    Toast.makeText(getApplicationContext(),"请先登入",Toast.LENGTH_LONG).show();
                    Intent intent=new Intent(ProductShow.this,Login.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }else{
                    HashMap sql=new HashMap();
                    sql.put("col","pid,uid,shopid");
                    sql.put("table","hold");
                    sql.put("values","'"+pid+"','"+checkLogin.Uid+"','"+shopid+"'");
                    Log.v("ProdShow:hold",checkLogin.Uid+","+pid);
                    DBconn dBconn=new DBconn();
                    dBconn.GetSql(sql);
                    dBconn.execute(insertUrl);
                }
            }
        });


    }
    public String executeHttpPost(String url){
        String result="";
        HttpClient client=new DefaultHttpClient();
        HttpPost request=new HttpPost(url);
        List<NameValuePair> nameValuePairs=new ArrayList<NameValuePair>(3);
        nameValuePairs.add(new BasicNameValuePair("col", "*"));
        nameValuePairs.add(new BasicNameValuePair("table", "shops"));
        //Log.v("shopid",shopid);
        nameValuePairs.add(new BasicNameValuePair("where", "shopid='"+shopid+"'"));
        nameValuePairs.add(new BasicNameValuePair("order", "SsNoInput"));

        HttpResponse response;
        try{
            request.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            response=client.execute(request);
            result= EntityUtils.toString(response.getEntity());
        }  catch (Exception e) {
            result="[ERROR]"+e.toString();
            result="[ERROR]"+e.toString();
            Log.v("Log",result);
        }
        return result;

    }

    private class MyAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... url) {
            return executeHttpPost(url[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.v("jsonRes:",result);
            ArrayList<Shops> arrayList= convJtoA(result);
            Log.v("mylog: ","arr: "+arrayList);
            //Log.v("mylog: ","products: "+arrayList.get(0).type+arrayList.get(0).name+arrayList.get(0).price+arrayList.get(0).shopid+arrayList.get(0).id);
            //todo
            shopName = (TextView)findViewById(R.id.shopName);
            location = (TextView)findViewById(R.id.location);
            time = (TextView)findViewById(R.id.time);
            phoneNo = (TextView)findViewById(R.id.phoneNo);
            shopName.setText("店鋪名稱: "+arrayList.get(0).shopeName);
            location.setText("店鋪位置: "+arrayList.get(0).location);
            time.setText("營業時間: "+arrayList.get(0).time);
            phoneNo.setText("聯絡電話: "+arrayList.get(0).phoneNo);



        }
    }
    public ArrayList<Shops> convJtoA(String jsonStr){
        Log.v("JsonStr",jsonStr);
        JSONObject jsonObjectTable;
        ArrayList<Shops> arrayList=new ArrayList<Shops>();
        try{
            jsonObjectTable = new JSONObject(jsonStr);
            JSONArray jsonArrayTable =jsonObjectTable.getJSONArray("result");

            for (int i=0;i<jsonArrayTable.length();i++){
                JSONObject jsonObjRow = jsonArrayTable.getJSONObject(i);
                Shops shops=new Shops();
                shops.shopId=jsonObjRow.getString("shopid");
                shops.shopeName=jsonObjRow.getString("shopName");
                shops.phoneNo=jsonObjRow.getString("phoneNo");
                shops.location=jsonObjRow.getString("location");
                shops.time=jsonObjRow.getString("time");
                shops.info=jsonObjRow.getString("info");
                arrayList.add(shops);
            }


        }catch (JSONException e){
            Log.v("mylog: ",e.toString());
        }
        return arrayList;

    }
    public void dowish(){
        CheckLogin checkLogin=new CheckLogin(getApplicationContext());
        Islogin=checkLogin.Check();
        Log.v("ProdShow:dohold:islogin",""+Islogin);
        if(Islogin==false){
            Toast.makeText(getApplicationContext(),"请先登入",Toast.LENGTH_LONG).show();
            Intent intent=new Intent(ProductShow.this,Login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }else{
            HashMap sql=new HashMap();
            sql.put("col","uid,pid");
            sql.put("table","wishlists");
            sql.put("values","'"+checkLogin.Uid+"','"+pid+"'");
            Log.v("ProdShow:dowish",checkLogin.Uid+","+pid);
            DBconn dBconn=new DBconn();
            dBconn.GetSql(sql);
            dBconn.execute(insertUrl);
        }
    }
    public class DBconn extends com.example.man.smartshop.DBclass.MyAsyncTask {
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.v("DBconn:result",result);

        }
    }
}
