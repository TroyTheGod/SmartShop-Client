package com.example.man.smartshop.DBclass;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class  MyAsyncTask extends AsyncTask<String, Void, String> {
    public HashMap sql;


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.v("DBconn","ConnectToDeServer...");
    }
    public void GetSql(HashMap sql){
        this.sql=sql;
    }

    @Override
    protected String doInBackground(String... url) {
        return executeHttpPost(url[0]);
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

    }
    public String executeHttpPost(String url){
        String result="";
        HttpClient client=new DefaultHttpClient();
        HttpPost request=new HttpPost(url);
        List<NameValuePair> nameValuePairs=new ArrayList<NameValuePair>(sql.size());
        for(Object key : sql.keySet()){
            nameValuePairs.add(new BasicNameValuePair(key.toString(), sql.get(key).toString()));
        }

        HttpResponse response;
        try{
            request.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            response=client.execute(request);
            result= EntityUtils.toString(response.getEntity());
        }  catch (Exception e) {
            result="[ERROR]"+e.toString();
            Log.v("Log",result);
        }
        return result;

    }
}
