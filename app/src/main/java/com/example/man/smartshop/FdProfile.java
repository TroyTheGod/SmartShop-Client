package com.example.man.smartshop;

import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.man.smartshop.DBclass.DBlocation;
import com.example.man.smartshop.DBclass.Friend;
import com.example.man.smartshop.DBclass.MyAsyncTask;
import com.example.man.smartshop.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FdProfile extends AppCompatActivity {

    private TextView name,userid,email,phone,favnum;
    private ImageView icon;
    public HashMap sql;
    public String uid,uname;
    public Button searchfav;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.home:
                    //mTextMessage.setText(R.string.title_home);
                    Intent intent=new Intent(FdProfile.this,MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                case R.id.fd:
                    //mTextMessage.setText(R.string.title_fd);
                    //startActivity(new Intent(fdProfile.this,fdProfile.class));

                    return true;
                case R.id.user:
                    //mTextMessage.setText(R.string.title_user);
                    startActivity(new Intent(FdProfile.this,Login.class));
                    return true;
            }
            return false;
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fdprofile);
        getWindow().setStatusBarColor(Color.parseColor("#09c6df"));
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        Bundle bundle=getIntent().getExtras();
        uid =bundle.getString("uid");
        uname=bundle.getString("uname");
        Log.v("FDprofile:uname",uname);
        Log.v("FDprofile:uid",uid);
        name=(TextView)findViewById(R.id.name);
        userid=(TextView)findViewById(R.id.userid);
        email=(TextView)findViewById(R.id.email);
        phone=(TextView)findViewById(R.id.phone);
        icon=(ImageView)findViewById(R.id.icon);
        searchfav=(Button)findViewById(R.id.searchfav);
        favnum=(TextView)findViewById(R.id.favnum);
        sql=new HashMap();
        sql.put("col","email , PhoneNo ,count(u.uid)");
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
                Intent intent = new Intent(FdProfile.this,Wishlist.class);
                Bundle bundle= new Bundle();
                bundle.putString("uid",uid);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    public class DBconn extends MyAsyncTask{
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.v("fdProfile:result",result);
            ArrayList<Friend> arrayList= convJtoA(result);
            Log.v("Dbconn: ","arr: "+arrayList);
            userid.setText(uid);
            name.setText(uname);
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
