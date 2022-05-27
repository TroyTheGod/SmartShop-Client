package com.example.man.smartshop.DBclass;

import android.content.Context;
import android.util.Log;


public class CheckLogin {
    Context context;
    private boolean Islogin;
    public String Uid,passwd;
    public CheckLogin(Context context){
        this.context=context;
    }
    public boolean Check(){
        MyDbAdapter myDbAdapter=new MyDbAdapter(context);
        myDbAdapter.select();
        try {
            Uid = myDbAdapter.userid;
            passwd = myDbAdapter.passwd;
            Log.v("CheckLogin", Uid + "::" + passwd);

            if(Uid.isEmpty()){
                Islogin=false;
            }else{
                Islogin=true;
            }
            Log.v("CheckLogin:IsLogIn:",""+Islogin);
        }catch (Exception e){
            Log.v("CheckLogin:DB:", "ERROR: "+e);
        }
        return Islogin;
    }

}
