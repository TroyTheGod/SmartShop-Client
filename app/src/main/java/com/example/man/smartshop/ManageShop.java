package com.example.man.smartshop;

import android.content.DialogInterface;
import android.content.Intent;
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
import com.example.man.smartshop.DBclass.MyAsyncTask;
import com.example.man.smartshop.DBclass.Products;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ManageShop extends AppCompatActivity {
    protected ListView listView;
    public HashMap sql;
    public CusProductAdapter adapter;
    public String SelURL,DelURL;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shop_manage);
        listView=(ListView)findViewById(R.id.listView);
        DBlocation dBlocation=new DBlocation();
        SelURL=dBlocation.DBURL;
        DelURL=dBlocation.DELETE;
        sql=new HashMap();
        sql.put("col","pid, pname, price, shopid, type");
        sql.put("table","products");
        sql.put("where","shopid=1");
        sql.put("order","SsNoInput");
        DBconnSel dBconnSel=new DBconnSel();
        dBconnSel.GetSql(sql);
        dBconnSel.execute(SelURL);
    }
    public class DBconnSel extends MyAsyncTask{
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.v("Wishlist:result",result);
            ArrayList<Products> arrayList=convJtoA(result);
            if (arrayList.isEmpty()){
                Log.v("makeToast","nothing find");
                AlertDialog.Builder dialog = new AlertDialog.Builder(ManageShop.this);
                dialog.setMessage("此用戶沒有收藏物品!");
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
        Log.v("Wishlist:conj2a:result",jsonStr);
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
        listView=(ListView)findViewById(R.id.listView);
        adapter = new CusProductAdapter(this,R.layout.listview_products,arraylist);
        Log.v("mylog",""+arraylist.size());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent =new Intent(ManageShop.this,ProductShow.class);
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

