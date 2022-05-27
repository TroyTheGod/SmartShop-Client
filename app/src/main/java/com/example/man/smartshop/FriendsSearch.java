package com.example.man.smartshop;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.man.smartshop.CusAdapter.CusFriendApt;
import com.example.man.smartshop.CusAdapter.CusProductAdapter;
import com.example.man.smartshop.DBclass.DBlocation;
import com.example.man.smartshop.DBclass.Friend;
import com.example.man.smartshop.DBclass.MyAsyncTask;
import com.example.man.smartshop.DBclass.MyDbAdapter;
import com.example.man.smartshop.DBclass.Products;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FriendsSearch extends AppCompatActivity {
    public HashMap<String,String> sql = new HashMap<String, String>();
    private ArrayList<Friend> arrayList=null;
    public ListView listView;
    public EditText etsearch;
    private boolean Islogin;
    private String Uid,pw,DELETEURL,search;
    public CusFriendApt adapter;
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview_searchfriends);
        getWindow().setStatusBarColor(Color.parseColor("#09c6df"));
        Bundle bundle=this.getIntent().getExtras();
        search = bundle.getString("searching");
        etsearch=(EditText) findViewById(R.id.edittext4);
        etsearch.setText(search);
        listView=(ListView)findViewById(R.id.listView);
        registerForContextMenu(listView);
        DELETEURL=new DBlocation().GETDELETEURL();
        MyDbAdapter myDbAdapter=new MyDbAdapter(getApplicationContext());
        myDbAdapter.select();
        try {
            Uid = myDbAdapter.userid;
            pw = myDbAdapter.passwd;


            if(Uid.isEmpty()){
                Islogin=false;

            }else{
                Islogin=true;
            }


        }catch (Exception e){
            Log.v("FriendList:DB:try", "ERROR: "+e);
        }
        Log.v("FriendsL:IsLogIn:",""+Islogin);
        Log.v("FriendList:DB:", Uid + "::" + pw);
        //todo check login

        sql.put("col","uid,uname");
        sql.put("table","usertable");
        sql.put("where","uname LIKE '%"+search+"%'");
        sql.put("order","SsNoInput");
        String URL=new DBlocation().DBURL;
        DBconn dBconn=new DBconn();
        dBconn.GetSql(sql);
        dBconn.execute(URL);

    }
    public class DBconn extends MyAsyncTask{
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.v("DBconn:result",result);
            ArrayList<Friend> arrayList= convJtoA(result);


                displayOutput(arrayList);

        }
    }
    public static ArrayList<Friend> convJtoA(String jsonStr){
        JSONObject jsonObjectTable;
        ArrayList<Friend> arrayList=new ArrayList<Friend>();
        Log.v("conj2a:jasonstr:",jsonStr);
        try{
            jsonObjectTable = new JSONObject(jsonStr);
            JSONArray jsonArrayTable =jsonObjectTable.getJSONArray("result");

            for (int i=0;i<jsonArrayTable.length();i++){
                JSONObject jsonObjRow = jsonArrayTable.getJSONObject(i);
                Friend friend=new Friend();
                friend.uname=jsonObjRow.getString("uname");
                friend.uid=jsonObjRow.getString("uid");
                arrayList.add(friend);
            }


        }catch (JSONException e){
            Log.v("mylog: ",e.toString());
        }
        return arrayList;

    }
    private void displayOutput(final ArrayList<Friend> arraylist){
        adapter = new CusFriendApt(this,R.layout.listview_friends,arraylist);
        Log.v("mylog",""+arraylist.size());
        listView.setAdapter(adapter);
        //todo Not finish Yet
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent =new Intent(FriendsSearch.this,FdProfile.class);
                Bundle bundle=new Bundle();
                Log.v("mylog",arraylist.get(i).uid);
                Log.v("mylog",arraylist.get(i).uname);
                bundle.putString("uid",arraylist.get(i).uid);
                bundle.putString("uname",arraylist.get(i).uname);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.friendmenu,menu);
    }

    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info=(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int index=info.position;
        switch (item.getItemId()){
            case R.id.action_follow:
                HashMap sql=new HashMap();
                sql.put("col","uid1,uid2");
                sql.put("table","friendsrel");
                sql.put("values","'"+Uid+"','"+adapter.getItem(index).uid+"'");
                Log.v("FriendsList:ContextMenu",adapter.getItem(index).uid);
                AddFromDB addFromDB=new AddFromDB();
                addFromDB.GetSql(sql);
                addFromDB.execute(new DBlocation().INSERT);
        }
        return super.onContextItemSelected(item);
    }
    public class AddFromDB extends MyAsyncTask{
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.v("WishList:DelDB",result.toString());

        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent intent =new Intent(FriendsSearch.this,FriendsList.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
