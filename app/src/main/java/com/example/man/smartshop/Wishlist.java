package com.example.man.smartshop;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.example.man.smartshop.CusAdapter.CusProductAdapter;
import com.example.man.smartshop.DBclass.CheckLogin;
import com.example.man.smartshop.DBclass.DBlocation;
import com.example.man.smartshop.DBclass.MyAsyncTask;
import com.example.man.smartshop.DBclass.Products;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.zip.Inflater;

public class Wishlist extends AppCompatActivity {
    public ListView listview;
    public HashMap sql;
    public boolean Islogin,Editable;
    public CusProductAdapter adapter;
    public String uid,DELETEURL;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wishlist);
        Bundle bundle=this.getIntent().getExtras();
        uid = bundle.getString("uid");
        Editable=bundle.getBoolean("Editable");

        getWindow().setStatusBarColor(Color.parseColor("#09c6df"));

        listview=(ListView)findViewById(R.id.listView);
        if (Editable==true){registerForContextMenu(listview);}
        DELETEURL=new DBlocation().GETDELETEURL();
        Log.v("Wishlist:onCreate:uid",uid);
        String URL= new DBlocation().GetURL();
        sql=new HashMap();
        sql.put("col","p.pid, p.pname, p.price, p.shopid, p.type");
        sql.put("table","wishlists w, products p");
        sql.put("where","w.uid ="+uid+" AND p.pid = w.pid");
        sql.put("order","SsNoInput");
        DBconn dBconn=new DBconn();
        dBconn.GetSql(sql);
        dBconn.execute(URL);


    }
    public class DBconn extends MyAsyncTask{
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.v("Wishlist:result",result);
            ArrayList<Products> arrayList=convJtoA(result);
            if (arrayList.isEmpty()){
                Log.v("makeToast","nothing find");
                AlertDialog.Builder dialog = new AlertDialog.Builder(Wishlist.this);
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
    public class DelFromDB extends MyAsyncTask{
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.v("WishList:DelDB",result.toString());
            finish();
            startActivity(getIntent());
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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.listviewmenu,menu);
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info=(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int index=info.position;
        switch (item.getItemId()){
            case R.id.action_delete:
                HashMap sql=new HashMap();
                sql.put("table","wishlists");
                sql.put("where","pid = "+adapter.getItem(index).id+" AND uid = "+uid);
                Log.v("Wishlist:ContextMenu",adapter.getItem(index).id);
                DelFromDB delFromDB=new DelFromDB();
                delFromDB.GetSql(sql);
                delFromDB.execute(DELETEURL);
        }
        return super.onContextItemSelected(item);
    }


    private void displayOutput(final ArrayList<Products> arraylist){
        listview=(ListView)findViewById(R.id.listView);
        adapter = new CusProductAdapter(this,R.layout.listview_products,arraylist);
        Log.v("mylog",""+arraylist.size());
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent =new Intent(Wishlist.this,ProductShow.class);
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
