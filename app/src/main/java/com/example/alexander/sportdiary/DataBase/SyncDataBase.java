package com.example.alexander.sportdiary.DataBase;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;

import com.example.alexander.sportdiary.MainActivity;
import com.example.alexander.sportdiary.R;

public class SyncDataBase extends AsyncTask {
    @Override
    protected Object doInBackground(Object[] objects) {
        return null;
    }

    @Override
    protected void onPostExecute(Object object) {
        Log.i("post", "POST EXECUTE");
        ProgressBar progressBar = MainActivity.getInstance().findViewById(R.id.loadBar);
        progressBar.setVisibility(View.GONE);
        ExpandableListView listView = MainActivity.getInstance().findViewById(R.id.expandableListView);
        listView.setEnabled(true);
        MainActivity.getInstance().fillDiariesMenu();
        super.onPostExecute(object);
    }

    @Override
    protected void onPreExecute() {
        Log.i("pre", "PRE EXECUTE");
        ProgressBar progressBar = MainActivity.getInstance().findViewById(R.id.loadBar);
        progressBar.setVisibility(View.VISIBLE);
        ExpandableListView listView = MainActivity.getInstance().findViewById(R.id.expandableListView);
        listView.setEnabled(false);
        super.onPreExecute();
    }
}
