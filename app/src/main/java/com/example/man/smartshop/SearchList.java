package com.example.man.smartshop;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.man.smartshop.CusAdapter.CusProductAdapter;
import com.example.man.smartshop.DBclass.DBlocation;
import com.example.man.smartshop.DBclass.Products;

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
import java.util.List;

public class SearchList extends AppCompatActivity {
    private ListView listView;
    public ArrayList<Products> arraylist=null;
    public String name,type;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        getWindow().setStatusBarColor(Color.parseColor("#09c6df"));
        Bundle bundle=this.getIntent().getExtras();
        name = bundle.getString("name");
        type=bundle.getString("type");
        DBlocation dBlocation=new DBlocation();
        String URL=dBlocation.GetURL();
        new MyAsyncTask().execute(URL);





    }
    public String executeHttpPost(String url){
        String result="";
        HttpClient client=new DefaultHttpClient();
        HttpPost request=new HttpPost(url);
        Log.v("Searchtype",type);
        List<NameValuePair> nameValuePairs=new ArrayList<NameValuePair>(3);
        nameValuePairs.add(new BasicNameValuePair("col", "*"));
        nameValuePairs.add(new BasicNameValuePair("table", "Products"));
        switch (type){
                case "Stype":Log.v("Searchtype","searchType==typeSearch");
                    nameValuePairs.add(new BasicNameValuePair("where", "type='"+name+"'"));
                    break;
                case"Sname":Log.v("Searchtype","searchType!=typeSearch");
                    nameValuePairs.add(new BasicNameValuePair("where", "pname LIKE '"+name+"%'"));
                    break;
        }
        nameValuePairs.add(new BasicNameValuePair("order", "SsNoInput"));
        HttpResponse response;
        try{
            request.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            response=client.execute(request);
            result= EntityUtils.toString(response.getEntity());
        }  catch (Exception e) {
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
            ArrayList<Products> arrayList= convJtoA(result);

            if (arrayList.isEmpty()){
                Log.v("makeToast","nothing find");
                AlertDialog.Builder dialog = new AlertDialog.Builder(SearchList.this);
                dialog.setMessage("Cannot Find Item "+name);
                dialog.setNegativeButton("OK",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        // TODO Auto-generated method stub
                        finish();
                    }

                });

                dialog.show();
            }else{
                Log.v("mylog: ","arr: "+arrayList);
                Log.v("mylog: ","products: "+arrayList.get(0).type+arrayList.get(0).name+arrayList.get(0).price+arrayList.get(0).shopid+arrayList.get(0).id);
                displayOutput(arrayList);
            }


        }
    }
        public ArrayList<Products> convJtoA(String jsonStr){
            Log.v("JsonStr",jsonStr);
            JSONObject jsonObjectTable;
            ArrayList<Products> arrayList=new ArrayList<Products>();
            try{
                jsonObjectTable = new JSONObject(jsonStr);
                JSONArray jsonArrayTable =jsonObjectTable.getJSONArray("result");

                for (int i=0;i<jsonArrayTable.length();i++){
                    JSONObject jsonObjRow = jsonArrayTable.getJSONObject(i);
                    Products products=new Products();
                    products.id=jsonObjRow.getString("pid");
                    products.name=jsonObjRow.getString("pname");
                    products.price=jsonObjRow.getString("price");
                    products.shopid=jsonObjRow.getString("shopid");
                    products.type=jsonObjRow.getString("type");
                    arrayList.add(products);
                }


        }catch (JSONException e){
            Log.v("mylog: ",e.toString());
        }
        if(arrayList.isEmpty()){
             Log.v("arraylistreturn",arrayList.toString()+"isempty");
            }else {
                Log.v("arraylistreturn",arrayList.toString()+"not empty");
        }

        return arrayList;

    }
    private void displayOutput(final ArrayList<Products> arraylist){
        listView=(ListView)findViewById(R.id.listView1);
        CusProductAdapter adapter = new CusProductAdapter(this,R.layout.listview_products,arraylist);
        Log.v("mylog",""+arraylist.size());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent =new Intent(SearchList.this,ProductShow.class);
                Bundle bundle=new Bundle();
                Log.v("mylog",arraylist.get(i).name);
                Log.v("mylog",arraylist.get(i).price);
                Log.v("mylog",arraylist.get(i).id);
                Log.v("mylog",arraylist.get(i).shopid);
                Log.v("mylog",arraylist.get(i).type);
                bundle.putString("name",arraylist.get(i).name);
                bundle.putString("shopid",arraylist.get(i).shopid);
                bundle.putString("pid",arraylist.get(i).id);
                bundle.putString("price",arraylist.get(i).price);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
}
