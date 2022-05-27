package com.example.man.smartshop;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.man.smartshop.R;

public class StoreMain extends AppCompatActivity {
public Button shopinfo,manage,addproduct,Hold;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.storestaff);
        shopinfo=(Button)findViewById(R.id.storestuffprofile);
        manage=(Button)findViewById(R.id.manage);
        addproduct=(Button)findViewById(R.id.Addproduct);
        Hold=(Button)findViewById(R.id.hold);
        shopinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(StoreMain.this,Shopinfo.class);
                startActivity(intent);
            }
        });
        manage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo add
                Intent intent=new Intent(StoreMain.this,ManageShop.class);
                startActivity(intent);
            }
        });
        addproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(StoreMain.this,Addproduct.class);
                startActivity(intent);
            }
        });
        Hold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(StoreMain.this,HoldP.class);
                startActivity(intent);
            }
        });


    }
}
