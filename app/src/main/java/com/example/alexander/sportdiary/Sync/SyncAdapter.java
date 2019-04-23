package com.example.alexander.sportdiary.Sync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

public class SyncAdapter extends AbstractThreadedSyncAdapter {
    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    public SyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        String table = extras.getString("table");
        String option = extras.getString("option");
        HttpClient httpClient = new DefaultHttpClient();
        String url = "http://192.168.1.136:8082/save";

        HttpPost httpPost = new HttpPost();
        List<NameValuePair> nameValuePair = new ArrayList<>(2);
        nameValuePair.add(new BasicNameValuePair("table", table));
        switch (option) {
            case "SAVE":
                String data = extras.getString("data");
                httpPost = new HttpPost(url + "/insert");
                nameValuePair.add(new BasicNameValuePair("data", data));
                Log.d("SAVE", table + " " + data);
                break;
            case "DELETE":
                Long id = extras.getLong("id");
                httpPost = new HttpPost(url + "/delete");
                nameValuePair.add(new BasicNameValuePair("id", id.toString()));
                Log.d("DELETE", table + " " + id);
                break;
        }
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
        } catch (UnsupportedEncodingException e) {
            // writing error to Log
            e.printStackTrace();
        }
        try {
            HttpResponse response = httpClient.execute(httpPost);
            Log.d("Http Response:", response.toString());
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
