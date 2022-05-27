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
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.man.smartshop.DBclass.DBlocation;
import com.example.man.smartshop.DBclass.Friend;
import com.example.man.smartshop.DBclass.MyAsyncTask;
import com.example.man.smartshop.DBclass.MyDbAdapter;
import com.example.man.smartshop.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Login extends AppCompatActivity {
    private Button reg,login;
    private EditText ac,pw;
    public HashMap sql;
    private boolean Islogin;
    private String Uid,passwd;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.home:
                    //mTextMessage.setText(R.string.title_home);
                    Intent intent=new Intent(Login.this,MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);

                    startActivity(intent);
                    return true;
                case R.id.fd:
                    //mTextMessage.setText(R.string.title_fd);
                    Intent intent1=new Intent(Login.this,FriendsList.class);
                    intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent1);

                    return true;
                case R.id.user:
                    //mTextMessage.setText(R.string.title_user);
                    //startActivity(new Intent(login.this,login.class));
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        getWindow().setStatusBarColor(Color.parseColor("#09c6df"));
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        MyDbAdapter myDbAdapter=new MyDbAdapter(getApplicationContext());
        myDbAdapter.select();
        try {
            Uid = myDbAdapter.userid;
            passwd = myDbAdapter.passwd;
            Log.v("MainA:DB:", Uid + "::" + pw);

            if(Uid.isEmpty()){
                Islogin=false;
            }else{
                Islogin=true;
                Intent intent = new Intent(Login.this,ProfileShow.class);
                Bundle bundle=new Bundle();
                bundle.putString("uid",Uid);
                intent.putExtras(bundle);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
            Log.v("IsLogIn:",""+Islogin);
        }catch (Exception e){
            Log.v("MainA:DB:", "ERROR: "+e);
        }


        reg=(Button)findViewById(R.id.reg);
        ac=(EditText)findViewById(R.id.ac);
        pw=(EditText)findViewById(R.id.pw);
        String URL=new DBlocation().GetURL();
        login=(Button)findViewById(R.id.login);
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Login.this,Reg.class);
                startActivity(intent);
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account=ac.getText().toString();
                String passwd=pw.getText().toString();
                if(account.length()==0||passwd.length()==0){
                    AlertDialog.Builder dialog = new AlertDialog.Builder(Login.this);
                    dialog.setMessage("請輸入賬戶與密碼");
                    dialog.setNegativeButton("OK",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            // TODO Auto-generated method stub
                            finish();
                        }

                    });
                }else {
                    sql = new HashMap();
                    sql.put("col", "uname, pw, uid");
                    sql.put("table", "usertable");
                    sql.put("where", "uname = '"+ac.getText().toString()+"' OR uid = '"+ac.getText().toString()+"'");
                    sql.put("order", "SsNoInput");
                    String URL=new DBlocation().DBURL;
                    DBconn dBconn=new DBconn();
                    dBconn.GetSql(sql);
                    dBconn.execute(URL);
                }
            }
        });




    }
    public class DBconn extends MyAsyncTask{
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.v("Login:DBconn:result",result);
            ArrayList<Friend> arrayList= convJtoA(result);
            Log.v("Login:uname and passwd",ac.getText().toString()+":::"+pw.getText().toString());
            if ((ac.getText().toString().equals(arrayList.get(0).uid)||ac.getText().toString().equals(arrayList.get(0).uname))&&pw.getText().toString().equals(arrayList.get(0).pw)){
                Toast.makeText(getApplicationContext(),"Login Success!",Toast.LENGTH_LONG).show();
                MyDbAdapter myDbAdapter=new MyDbAdapter(getApplicationContext());
                myDbAdapter.insertSql(arrayList.get(0).uid,arrayList.get(0).pw);
                Intent intent=new Intent(Login.this,MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }else{
                Toast.makeText(getApplicationContext(),"Login Fail, please check your account and password!",Toast.LENGTH_LONG).show();
            }
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
                friend.pw=jsonObjRow.getString("pw");
                arrayList.add(friend);
            }


        }catch (JSONException e){
            Log.v("mylog: ",e.toString());
        }
        return arrayList;

    }
}
