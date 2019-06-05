package com.example.alexander.sportdiary.Sync;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.example.alexander.sportdiary.Auth.GoogleSignInActivity;
import com.example.alexander.sportdiary.Converters.EntityDtoConverter;
import com.example.alexander.sportdiary.DataBase.SportDataBase;
import com.example.alexander.sportdiary.Dto.DayDto;
import com.example.alexander.sportdiary.Dto.SeasonPlanDto;
import com.example.alexander.sportdiary.Entities.Version;
import com.example.alexander.sportdiary.Enums.Table;
import com.example.alexander.sportdiary.MainActivity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import cz.msebera.android.httpclient.Consts;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.HttpStatus;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.util.EntityUtils;

public class SyncWorker extends Worker {
    private static SportDataBase database;
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final EntityDtoConverter converter = new EntityDtoConverter();
    private Context context;

    public SyncWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
        database = Room.databaseBuilder(context, SportDataBase.class, "SportDataBase")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
    }

    private void signOut() {
        // Firebase sign out
        GoogleSignInActivity.getmAuth().signOut();

        // Google sign out
        GoogleSignInActivity.getmGoogleSignInClient().signOut().addOnCompleteListener(
                task -> MainActivity.getInstance().finish());
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.d("SYNC", "DO WORK");
        Data extras = getInputData();
        String table = extras.getString("table");
        String option = extras.getString("option");
        String userId = extras.getString("userId");
        String token = extras.getString("token");
        Long version =  database.versionDao().getByUserId(userId).getVersion();

        HttpClient httpClient = new DefaultHttpClient();
        String url = "https://limitless-shelf-62410.herokuapp.com/auth/save";

        HttpGet httpGet = new HttpGet("https://limitless-shelf-62410.herokuapp.com/auth/version?userId="+userId);
        httpGet.setHeader("X-Firebase-Auth", token);
        try {
            HttpResponse response = httpClient.execute(httpGet);
            Long result = 0L;
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                 result= Long.valueOf(EntityUtils.toString(response.getEntity()));
            }
            if (!version.equals(result)) {
                Log.d("VERSION", "old version - need unload");
                Log.d("VERSION", version + " " + result);
                Toast.makeText(context, "Данные устарели - необходимо синхронизировать с сервером", Toast.LENGTH_SHORT).show();
                signOut();
                return Result.failure();
            } else {
                Log.d("VERSION", "versions are equals - save allowed");
            }
            Log.d("VERSION", version + " " + result);
            EntityUtils.consumeQuietly(response.getEntity());
        } catch (IOException e) {
            e.printStackTrace();
        }

        HttpPost httpPost = new HttpPost();
        List<NameValuePair> nameValuePair = new ArrayList<>(2);
        nameValuePair.add(new BasicNameValuePair("table", table));
        nameValuePair.add(new BasicNameValuePair("userId", userId));
        nameValuePair.add(new BasicNameValuePair("version", version.toString()));
        switch (option) {
            case "SAVE":
                String data = extras.getString("data");
                httpPost = new HttpPost(url + "/insert");
                if (table.toLowerCase().equals(Table.SEASON_PLAN)) {
                    Long id = extras.getLong("id", -1);
                    SeasonPlanDto seasonPlanDto = converter
                            .convertEntityToDto(
                                    database.seasonPlanDao().getSeasonPlanById(id));
                    List<DayDto> dayDtos = database.dayDao().getAllBySeasonPlanId(id)
                            .stream()
                            .map(converter::convertEntityToDto)
                            .collect(Collectors.toList());
                    seasonPlanDto.setDays(dayDtos);
                    try {
                        data = objectMapper.writeValueAsString(seasonPlanDto);
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                }
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
        httpPost.setHeader("X-Firebase-Auth", token);
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(nameValuePair, Consts.UTF_8);
        httpPost.setEntity(entity);
        try {
            HttpResponse response = httpClient.execute(httpPost);
            Log.d("Http Response:", response.toString());
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK && !table.toLowerCase().equals(Table.VERSION)) {
                Version v = database.versionDao().getByUserId(userId);
                Log.d("VERSION", String.valueOf(v.getVersion()+1));
                v.setVersion(v.getVersion() + 1);
                database.versionDao().update(v);
            }
            return Result.success();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Result.failure();
    }
}
