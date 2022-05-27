package com.example.man.smartshop;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.man.smartshop.DBclass.DBlocation;
import com.example.man.smartshop.DBclass.Friend;
import com.example.man.smartshop.DBclass.MyAsyncTask;
import com.example.man.smartshop.DBclass.MyDbAdapter;
import com.example.man.smartshop.DBclass.Products;
import com.example.man.smartshop.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

public class Reg extends AppCompatActivity {
    protected EditText uname,pw,repw,email,phone;
    protected Button submit,reset;
    public String suname,spw,srepw,semail,sphone,inserturl,selurl;
    public boolean IsEmpty,IsInt,Ismatch;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reg);
        getWindow().setStatusBarColor(Color.parseColor("#09c6df"));
        uname=(EditText)findViewById(R.id.edittext1);
        pw=(EditText)findViewById(R.id.edittext2);
        repw=(EditText)findViewById(R.id.edittext3);
        email=(EditText)findViewById(R.id.edittext5);
        inserturl=new DBlocation().GetInsertURL();
        selurl=new DBlocation().GetURL();
        phone=(EditText)findViewById(R.id.edittext6);
        submit=(Button) findViewById(R.id.btn1);
        reset=(Button) findViewById(R.id.btn2);
        IsEmpty=false;
        IsInt=false;
        Ismatch=false;
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gettext();
                if (gettext()==true){
                    checkRepeat();
                }
            }
        });
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uname.setText("");
                pw.setText("");
                repw.setText("");
                email.setText("");
                phone.setText("");
            }
        });
    }
    public boolean gettext(){
        suname=uname.getText().toString();
        spw=pw.getText().toString();
        srepw=repw.getText().toString();
        semail=email.getText().toString();
        sphone=phone.getText().toString();
        if(docheck()==true)
            return true;
        return false;
    }
    public boolean docheck(){
        IsEmpty=checkEmpty();
        IsInt=checkId();
        Ismatch=checkPw();
        String Error="";
        Log.v("pw:repw",spw+"::"+srepw);
        if (IsEmpty==true){
            Error="請輸入所有資料!";
        }else {
            if (IsInt == true)
                Error = "\n 用戶名稱不能為純數字!";
            if (Ismatch == true)
                Error =Error+ "\n 密碼不一致!";
            if (checkPhone()==true)
                Error =Error+ "\n 請輸入正確的電話號碼";
            if(checkEmail()==true)
                Error =Error+ "\n 請輸入正確的電郵地址";

        }
        if(!Error.equals("")){
        AlertDialog.Builder dialog = new AlertDialog.Builder(Reg.this);
        dialog.setMessage(Error);
        dialog.setNegativeButton("OK",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub

            }

        });
        dialog.show();
        return false;
        }else{
            return true;
        }
    }
    public boolean checkEmpty(){
        if(suname.matches("")||spw.matches("")||srepw.matches("")||semail.matches("")||sphone.matches(""))
            return true;
        return false;
    }
    public boolean checkId(){
        try {
            int temp=Integer.parseInt(suname);
            Log.v("Reg:checkId","uname is not int");
        }catch (NumberFormatException e){
            Log.v("Reg:checkId","uname is int");
            return false;
        }
        return true;
    }
    public boolean checkPw(){
        if (spw.equals(srepw)){
            return false;
        }else {
            return true;
        }
    }
    public void upload(){
        HashMap sql=new HashMap();
        sql.put("col","uname,pw,email,phoneNo");
        sql.put("table","usertable");
        sql.put("values","'"+suname+"','"+spw+"','"+semail+"','"+sphone+"'");
        DBconn dBconn=new DBconn();
        dBconn.GetSql(sql);
        dBconn.execute(inserturl);

    }
    public boolean checkPhone(){
        if (sphone.length()==8){
            sphone="+852"+sphone;
            return false;
        }else {
            return true;
        }
    }
    public boolean checkEmail(){
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (pat.matcher(semail).matches()){
            return false;
        }else {
            return true;
        }

    }
    public class DBconn extends MyAsyncTask{
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.v("Reg:dbconn:result",result);
        }
    }

    public void checkRepeat(){
        HashMap sql=new HashMap();
        sql.put("col","uname");
        sql.put("table","usertable");
        sql.put("where","uname = '"+suname+"'");
        sql.put("order","SsNoInput");
        selDbconn dBconn=new selDbconn();
        dBconn.GetSql(sql);
        dBconn.execute(selurl);
    }

    public class selDbconn extends MyAsyncTask{
        @Override
        protected void onPostExecute(String result) {
            //todo
            super.onPostExecute(result);
            ArrayList<Friend> arrayList= convJtoA(result);
            Log.v("Reg:selDbconn:uname",uname.getText().toString()+""+arrayList.get(0).uname);
            if (uname.getText().toString().equals(arrayList.get(0).uname)){
                AlertDialog.Builder dialog = new AlertDialog.Builder(Reg.this);
                dialog.setMessage("用戶名稱與其他用戶相同 \n請使用其他用戶名稱!");
                dialog.setNegativeButton("OK",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        // TODO Auto-generated method stub

                    }

                });
                dialog.show();
            }else{
                upload();
                Log.v("reg:upload","upload!");
                Toast.makeText(getApplicationContext(),"注冊成功,請登入!",Toast.LENGTH_LONG).show();
                Intent intent=new Intent(Reg.this,Login.class);
                startActivity(intent);
            }

        }
    }

    public ArrayList<Friend> convJtoA(String jsonStr){
        Log.v("JsonStr",jsonStr);
        JSONObject jsonObjectTable;
        ArrayList<Friend> arrayList=new ArrayList<Friend>();
        Log.v("Reg:conj2a:result",jsonStr);
        try{
            jsonObjectTable = new JSONObject(jsonStr);
            JSONArray jsonArrayTable =jsonObjectTable.getJSONArray("result");
            for (int i=0;i<jsonArrayTable.length();i++){
                JSONObject jsonObjRow = jsonArrayTable.getJSONObject(i);
                Friend friend=new Friend();
                friend.uname=jsonObjRow.getString("uname");
                arrayList.add(friend);
            }


        }catch (JSONException e){
            Log.v("mylog: ",e.toString());
        }
        if(arrayList.isEmpty()){
            Log.v("arraylistreturn",arrayList.toString()+"isempty");
            Friend friend1 =new Friend();
            friend1.uname="No";
            arrayList.add(friend1);
        }else {
            Log.v("arraylistreturn",arrayList.toString()+"not empty");
        }

        return arrayList;

    }
    public void logout(){
        MyDbAdapter myDbAdapter=new MyDbAdapter(getApplicationContext());

    }
}
