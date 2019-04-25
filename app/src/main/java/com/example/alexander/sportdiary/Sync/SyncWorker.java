package com.example.alexander.sportdiary.Sync;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import cz.msebera.android.httpclient.Consts;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.protocol.HTTP;

public class SyncWorker extends Worker {

    public SyncWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Data extras = getInputData();
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
                Long id = extras.getLong("id", -1);
                httpPost = new HttpPost(url + "/delete");
                nameValuePair.add(new BasicNameValuePair("id", id.toString()));
                Log.d("DELETE", table + " " + id);
                break;
        }
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(nameValuePair, Consts.UTF_8);
        httpPost.setEntity(entity);
        try {
            HttpResponse response = httpClient.execute(httpPost);
            Log.d("Http Response:", response.toString());
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Result.success();
    }
}
