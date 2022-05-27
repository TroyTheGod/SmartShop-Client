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

public class FriendsList extends AppCompatActivity {
    public HashMap<String,String> sql = new HashMap<String, String>();
    private ArrayList<Friend> arrayList=null;
    public ListView listView;
    public EditText etsearch;
    public ImageButton btnSearch;
    private boolean Islogin;
    private String Uid,pw,DELETEURL;
    public CusFriendApt adapter;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.home:
                    //mTextMessage.setText(R.string.title_home);
                    Intent intent=new Intent(FriendsList.this,MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                case R.id.fd:
                    //mTextMessage.setText(R.string.title_fd);
                    //startActivity(new Intent(fdProfile.this,fdProfile.class));

                    return true;
                case R.id.user:
                    //mTextMessage.setText(R.string.title_user);
                    startActivity(new Intent(FriendsList.this,Login.class));
                    return true;
            }
            return false;
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview_showfriends);
        getWindow().setStatusBarColor(Color.parseColor("#09c6df"));
        listView=(ListView)findViewById(R.id.listView);
        registerForContextMenu(listView);
        DELETEURL=new DBlocation().GETDELETEURL();
        btnSearch=(ImageButton)findViewById(R.id.btn3);
        etsearch=(EditText) findViewById(R.id.edittext4);
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
        if(Islogin==false){
            Toast.makeText(getApplicationContext(),"请先登入",Toast.LENGTH_LONG).show();
            Intent intent=new Intent(FriendsList.this,Login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }



        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        //todo check login

        sql.put("col","u.uid,uname");
        sql.put("table","friendsrel f, usertable u");
        sql.put("where","f.uid2 = u.uid AND f.uid1 ="+Uid);
        sql.put("order","SsNoInput");
        String URL=new DBlocation().DBURL;
        DBconn dBconn=new DBconn();
        dBconn.GetSql(sql);
        dBconn.execute(URL);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo
                String searching = etsearch.getText().toString();
                Intent intent=new Intent(FriendsList.this,FriendsSearch.class);
                Bundle bundle=new Bundle();
                bundle.putString("searching",searching);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
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
                Intent intent =new Intent(FriendsList.this,FdProfile.class);
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
        inflater.inflate(R.menu.listviewmenu,menu);
    }

    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info=(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int index=info.position;
        switch (item.getItemId()){
            case R.id.action_delete:
                HashMap sql=new HashMap();
                sql.put("table","friendsrel");
                sql.put("where","uid1 = "+Uid+" AND uid2 = "+adapter.getItem(index).uid);
                Log.v("FriendsList:ContextMenu",adapter.getItem(index).uid);
                DelFromDB delFromDB=new DelFromDB();
                delFromDB.GetSql(sql);
                delFromDB.execute(DELETEURL);
        }
        return super.onContextItemSelected(item);
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

}
