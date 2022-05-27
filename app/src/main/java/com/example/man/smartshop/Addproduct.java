package com.example.man.smartshop;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.example.man.smartshop.DBclass.DBlocation;
import com.example.man.smartshop.DBclass.MyAsyncTask;

import java.util.HashMap;

public class Addproduct extends AppCompatActivity {
    private EditText edtName,edtPrice,edtHoldtime;
    private Button btnsubmit;
    private Spinner spinner;
    protected String type,name,price,Holdtime,URL;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addproduct);
        edtName=(EditText)findViewById(R.id.edtpname);
        edtPrice=(EditText)findViewById(R.id.edtprice);
        spinner=(Spinner)findViewById(R.id.Stype);
        final String[] Catetype={"美食", "服裝", "運動", "家電", "家具", "其他"};
        ArrayAdapter arrayAdapter=new ArrayAdapter(Addproduct.this,android.R.layout.simple_spinner_item,Catetype);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        edtHoldtime=(EditText)findViewById(R.id.edtkeeptime);
        btnsubmit=(Button) findViewById(R.id.btnsubmit);
        URL=new DBlocation().INSERT;
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                type=convCat(position,Catetype);
                Log.v("addproduct:spinner",type);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name=edtName.getText().toString();
                price=edtPrice.getText().toString();
                Holdtime=edtHoldtime.getText().toString();
                HashMap sql=new HashMap();
                sql.put("col","pname,price,shopid,type,holdtime");
                sql.put("table","products");
                sql.put("values","'"+name+"','"+price+"','"+"1"+"','"+type+"','"+Holdtime+"'");
                Log.v("Addproducts","'"+name+"','"+price+"','"+"1"+"','"+type+"','"+Holdtime+"'");
                DBconn dBconn=new DBconn();
                dBconn.GetSql(sql);
                dBconn.execute(URL);
                }
        });
    }
    public class DBconn extends MyAsyncTask{
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            goback();
        }
    }
    public String convCat(int pos,String[] cate){
        String temp=cate[pos];
        String type;
        switch (temp){
            case "美食": type="Food";break;
            case "服裝": type="Clothes";break;
            case "運動": type="Sports";break;
            case "家電": type="HA";break;
            case "家具": type="Home";break;
            case "其他": type="Other";break;
            default: type="Other";break;
        }
        return type;
    }
    public void goback(){
        Toast.makeText(getApplicationContext(),"成功!",Toast.LENGTH_LONG).show();
        finish();
    }
}
