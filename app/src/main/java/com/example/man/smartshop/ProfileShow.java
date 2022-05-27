package com.example.man.smartshop;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.man.smartshop.DBclass.DBlocation;
import com.example.man.smartshop.DBclass.Friend;
import com.example.man.smartshop.DBclass.MyAsyncTask;
import com.example.man.smartshop.DBclass.MyDbAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ProfileShow extends AppCompatActivity {
    private String uid;
    public TextView name,userid,email,phone,favnum;
    public ImageView icon;
    public HashMap sql;
    public String uname;
    public Button searchfav,logout,gotoshop;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.home:
                    //mTextMessage.setText(R.string.title_home);
                    Intent intent=new Intent(ProfileShow.this,MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    break;
                case R.id.fd:
                    //mTextMessage.setText(R.string.title_fd);
                    Intent intent1=new Intent(ProfileShow.this,FriendsList.class);
                    intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent1);

                    return true;
                case R.id.user:
                    //mTextMessage.setText(R.string.title_user);
                    //startActivity(new Intent(FdProfile.this,Login.class));
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getWindow().setStatusBarColor(Color.parseColor("#09c6df"));
        Bundle bundle=this.getIntent().getExtras();
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        uid=bundle.getString("uid");
        Log.v("ProfileShow:uid",uid);
        name=(TextView)findViewById(R.id.name);
        userid=(TextView)findViewById(R.id.userid);
        email=(TextView)findViewById(R.id.email);
        phone=(TextView)findViewById(R.id.phone);
        icon=(ImageView)findViewById(R.id.icon);
        gotoshop=(Button)findViewById(R.id.gotoshop);
        searchfav=(Button)findViewById(R.id.searchfav);
        favnum=(TextView)findViewById(R.id.favnum);
        logout=(Button)findViewById(R.id.logout);
        sql=new HashMap();
        sql.put("col","uname,email , PhoneNo ,count(u.uid)");
        sql.put("table","wishlists w , usertable u");
        sql.put("where","u.uid = w.uid AND u.uid = "+uid);
        sql.put("order","SsNoInput");

        String URL=new DBlocation().DBURL;
        DBconn dBconn =new DBconn();
        dBconn.GetSql(sql);
        dBconn.execute(URL);
        searchfav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("Profileshow","wishlist");
                Intent intent = new Intent(ProfileShow.this,Wishlist.class);
                Bundle bundle= new Bundle();
                bundle.putString("uid",uid);
                bundle.putBoolean("Editable",true);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDbAdapter myDbAdapter=new MyDbAdapter(getApplicationContext());
                myDbAdapter.delete();
                Intent intent =new Intent(ProfileShow.this,MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        gotoshop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(ProfileShow.this,StoreMain.class);
                startActivity(intent);
            }
        });
    }


public class DBconn extends MyAsyncTask {
    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        Log.v("fdProfile:result",result);
        ArrayList<Friend> arrayList= convJtoA(result);
        Log.v("Dbconn: ","arr: "+arrayList);
        userid.setText(uid);
        name.setText(arrayList.get(0).uname);
        email.setText(arrayList.get(0).Email);
        phone.setText(arrayList.get(0).phoneNo);
        favnum.setText(arrayList.get(0).wish);
    }
}
    public static ArrayList<Friend> convJtoA(String jsonStr){
        JSONObject jsonObjectTable;
        ArrayList<Friend> arrayList=new ArrayList<Friend>();
        Log.v("fdProfile:Conj2a:jsnstr",jsonStr);
        try{
            jsonObjectTable = new JSONObject(jsonStr);
            JSONArray jsonArrayTable =jsonObjectTable.getJSONArray("result");

            for (int i=0;i<jsonArrayTable.length();i++){
                JSONObject jsonObjRow = jsonArrayTable.getJSONObject(i);
                Friend friend=new Friend();
                friend.uname=jsonObjRow.getString("uname");
                friend.Email=jsonObjRow.getString("email");
                friend.phoneNo=jsonObjRow.getString("PhoneNo");
                friend.wish=jsonObjRow.getString("count(u.uid)");
                Log.v("fdProfile:conj2a:for",friend.Email+friend.phoneNo);
                arrayList.add(friend);
            }


        }catch (JSONException e){
            Log.v("mylog: ",e.toString());
        }
        return arrayList;

    }
}
