package com.example.alexander.sportdiary;

import android.os.AsyncTask;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;

import com.example.alexander.sportdiary.Entities.Day;
import com.example.alexander.sportdiary.Entities.HeartRate;
import com.example.alexander.sportdiary.Entities.SeasonPlan;
import com.example.alexander.sportdiary.Entities.Training;
import com.example.alexander.sportdiary.Entities.TrainingExercise;
import com.example.alexander.sportdiary.Utils.DateUtil;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static com.example.alexander.sportdiary.MenuItemIds.DIARY_GROUP;
import static com.example.alexander.sportdiary.Utils.DateUtil.sdf;

public class InitializeDataBase extends AsyncTask {
    @Override
    protected Object doInBackground(Object[] objects) {
        Log.i("do", "DO IN BACKGROUND");
        return createTestBanisterDiary();
    }

    @Override
    protected void onPostExecute(Object object) {
        SeasonPlan seasonPlan = (SeasonPlan) object;
        long id = seasonPlan.getId();

        Log.i("post", "POST EXECUTE");
        ProgressBar progressBar = MainActivity.getInstance().findViewById(R.id.loadBar);
        progressBar.setVisibility(View.GONE);
        ExpandableListView listView = MainActivity.getInstance().findViewById(R.id.expandableListView);
        listView.setEnabled(true);

        MenuModel menuModel = MenuModel.getMenuModelById(MainActivity.getHeaderList(), DIARY_GROUP.getValue());
        List<MenuModel> childs = MainActivity.getChildList().get(menuModel);
        String diaryName = seasonPlan.getName() + " " + sdf.format(seasonPlan.getStart());
        MenuModel childModel = new MenuModel(diaryName, false, false, (int) id);
        childs.add(childModel);
        MainActivity.putToChildList(menuModel, childs);
        MainActivity.getExpandableListAdapter().notifyDataSetChanged();
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

    private SeasonPlan createTestBanisterDiary() {
        SportDataBase database = MainActivity.getInstance().getDatabase();
        Log.i("diary", "Create Test Banister Diary");
        Random random = new Random();
        Date date = new Date();
        try {
            date = sdf.parse("20.05.2010");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SeasonPlan seasonPlan = new SeasonPlan("Banister", date);
        long id = database.seasonPlanDao().insert(seasonPlan);
        for(int i = 0; i < 366; i++, date = DateUtil.addDays(date, 1)) {
            long did = database.dayDao().insert(new Day(date, id));
            long tid = database.trainingDao().insert(new Training(did));
            for (int j = 0; j < 2; j++) {
                int time = random.nextInt(25) + 20;
                TrainingExercise trainingExercise = new TrainingExercise(tid);
                trainingExercise.setMinutes(time);
                long eid = database.trainingExerciseDao().insert(trainingExercise);
                double hrAvg = 0;
                for (int k = 0; k < 5; k++) {
                    int hr = random.nextInt(100) + 80;
                    HeartRate heartRate = new HeartRate(eid);
                    heartRate.setHr(hr);
                    database.heartRateDao().insert(heartRate);
                    hrAvg += hr;
                }
                hrAvg /= 10.0;
                database.trainingExerciseDao().updateHrById(hrAvg, eid);
            }
        }
        seasonPlan.setId(id);
        return seasonPlan;
    }
}
