package com.example.alexander.sportdiary.Sync;

import android.accounts.Account;
import android.arch.persistence.room.Room;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Log;

import com.example.alexander.sportdiary.DataBase.SportDataBase;
import com.example.alexander.sportdiary.Enums.SyncOption;

import static com.example.alexander.sportdiary.Enums.SyncOption.DELETE;
import static com.example.alexander.sportdiary.Enums.SyncOption.SAVE;

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
        switch (option) {
            case "SAVE":
                String data = extras.getString("data");
                Log.e("SAVE " + table, data);
                break;
            case "DELETE":
                Long id = extras.getLong("id");
                Log.e("DELETE " + table, id.toString());
                break;
        }
    }
}
