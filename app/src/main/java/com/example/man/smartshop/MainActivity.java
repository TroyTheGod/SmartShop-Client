package com.example.man.smartshop;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.man.smartshop.DBclass.MyDbAdapter;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private ImageButton btnFood, btnClothes, btnSports, btnHA, btnFU, btnOther;
    //private SearchView search;
    private EditText searchBar;
    private ImageButton search;
    private boolean Islogin;
    private String uid,pw;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.home:
                    //mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.fd:
                    //mTextMessage.setText(R.string.title_fd);
                    Intent intent=new Intent(MainActivity.this,FriendsList.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                    return true;
                case R.id.user:
                    //mTextMessage.setText(R.string.title_user);
                    Intent intent1=new Intent(MainActivity.this,Login.class);
                    intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent1);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setStatusBarColor(Color.parseColor("#09c6df"));
        MyDbAdapter myDbAdapter=new MyDbAdapter(getApplicationContext());
        myDbAdapter.select();
        try {
            uid = myDbAdapter.userid;
             pw = myDbAdapter.passwd;
            Log.v("MainA:DB:", uid + "::" + pw);
            if(uid.isEmpty()){
                Islogin=false;
            }else{
                Islogin=true;
            }
        }catch (Exception e){
            Log.v("MainA:DB:", "ERROR: "+e);
        };

        Log.v("Main:IsLogIn:",""+Islogin);
        btnFood=(ImageButton)findViewById(R.id.Food);
        btnClothes=(ImageButton)findViewById(R.id.Clothes);
        btnSports=(ImageButton)findViewById(R.id.Sports);
        btnHA=(ImageButton)findViewById(R.id.HA);
        btnFU=(ImageButton)findViewById(R.id.FU);
        btnOther=(ImageButton)findViewById(R.id.Other);
        mTextMessage = (TextView) findViewById(R.id.message);
        searchBar=(EditText) findViewById(R.id.searchBar);
        search=(ImageButton)findViewById(R.id.search);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,SearchList.class);
                Bundle bundle=new Bundle();
                bundle.putString("name",searchBar.getText().toString()+"%");
                bundle.putString("type","Sname");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        btnFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,SearchList.class);
                Bundle bundle=new Bundle();
                bundle.putString("name","Food");
                bundle.putString("type","Stype");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        btnClothes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,SearchList.class);
                Bundle bundle=new Bundle();
                bundle.putString("name","Clothes");
                bundle.putString("type","Stype");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        btnSports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,SearchList.class);
                Bundle bundle=new Bundle();
                bundle.putString("name","Sports");
                bundle.putString("type","Stype");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        //家電
        btnHA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,SearchList.class);
                Bundle bundle=new Bundle();
                bundle.putString("name","HA");
                bundle.putString("type","Stype");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        //家具
        btnFU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,SearchList.class);
                Bundle bundle=new Bundle();
                bundle.putString("name","Home");
                bundle.putString("type","Stype");
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });

        btnOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,SearchList.class);
                Bundle bundle=new Bundle();
                bundle.putString("name","Other");
                bundle.putString("type","Stype");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });


    }




}
