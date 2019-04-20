package com.example.alexander.sportdiary.Sync;

import android.accounts.Account;
import android.arch.persistence.room.Room;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;

import com.example.alexander.sportdiary.DataBase.SportDataBase;

public class SyncAdapter extends AbstractThreadedSyncAdapter {
    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    public SyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        SportDataBase database = Room.databaseBuilder(getContext(), SportDataBase.class, "SportDataBase")
                .allowMainThreadQueries()
                .build();
    }
}
