package com.example.alexander.sportdiary;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;

import com.example.alexander.sportdiary.Dao.EditDao.EditDao;
import com.example.alexander.sportdiary.Entities.Day;
import com.example.alexander.sportdiary.Entities.DreamQuestion;
import com.example.alexander.sportdiary.Entities.EditEntities.*;
import com.example.alexander.sportdiary.Entities.HeartRate;
import com.example.alexander.sportdiary.Entities.SanQuestion;
import com.example.alexander.sportdiary.Entities.SeasonPlan;
import com.example.alexander.sportdiary.Entities.Training;
import com.example.alexander.sportdiary.Entities.TrainingExercise;
import com.example.alexander.sportdiary.Enums.SanType;
import com.example.alexander.sportdiary.Utils.DateUtil;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static com.example.alexander.sportdiary.Enums.MenuItemIds.DIARY_GROUP;
import static com.example.alexander.sportdiary.Utils.DateUtil.sdf;

public class InitializeDataBase extends AsyncTask {
    @Override
    protected Object doInBackground(Object[] objects) {
        Log.i("do", "DO IN BACKGROUND");
        SportDataBase dataBase = MainActivity.getInstance().getDatabase();
        for(int i = 1; i < 4; i++) {
            fillEdit(Exercise.class, "exercise" + i, dataBase.exerciseDao());
            fillEdit(Zone.class, "zone" + i, dataBase.zoneDao());
            fillEdit(Aim.class, "aim" + i, dataBase.aimDao());
            fillEdit(Equipment.class, "equipment" + i, dataBase.equipmentDao());
            fillEdit(Time.class, "time" + i, dataBase.timeDao());
            fillEdit(TrainingPlace.class, "trainingPlace" + i, dataBase.trainingPlaceDao());
            fillEdit(Borg.class, "borg" + i, dataBase.borgDao());
            fillEdit(Style.class, "style" + i, dataBase.styleDao());
            fillEdit(Tempo.class, "tempo" + i, dataBase.tempoDao());
            fillEdit(Competition.class, "competition" + i, dataBase.competitionDao());
            fillEdit(Importance.class, "importance" + i, dataBase.importanceDao());
            fillEdit(Block.class, "block" + i, dataBase.blockDao());
            fillEdit(Stage.class, "stage" + i, dataBase.stageDao());
            fillEdit(Type.class, "type" + i, dataBase.typeDao());
            fillEdit(Camp.class, "camp" + i, dataBase.campDao());
            fillEdit(RestPlace.class, "restPlace" + i, dataBase.restPlaceDao());
            fillEdit(Test.class, "test" + i, dataBase.testDao());
            dataBase.dreamQuestionDao().insert(new DreamQuestion("dream" + i));
            dataBase.sanQuestionDao().insert(new SanQuestion("positiveH" + i, "negativeH"+i, SanType.HEALTH));
            dataBase.sanQuestionDao().insert(new SanQuestion("positiveA" + i, "negativeA"+i, SanType.ACTIVITY));
        }
        return createTestDiary();
//        return null;
    }

    private <T extends Edit> void fillEdit(Class<T> cls, String name, EditDao<T> dao) {
        try {
            T elem = cls.newInstance();
            elem.setName(name);
            dao.insert(elem);
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPostExecute(Object object) {
        Log.i("post", "POST EXECUTE");
        ProgressBar progressBar = MainActivity.getInstance().findViewById(R.id.loadBar);
        progressBar.setVisibility(View.GONE);
        ExpandableListView listView = MainActivity.getInstance().findViewById(R.id.expandableListView);
        listView.setEnabled(true);
        if (object == null) {
            super.onPostExecute(object);
            return;
        }
        SeasonPlan seasonPlan = (SeasonPlan) object;
        long id = seasonPlan.getId();

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

    private SeasonPlan createTestDiary() {
        SportDataBase database = MainActivity.getInstance().getDatabase();
        Log.i("diary", "Create Test Banister Diary");
        Random random = new Random();
        Date date = new Date();
        try {
            date = sdf.parse("20.05.2010");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SeasonPlan seasonPlan = new SeasonPlan("Test", date);
        long id = database.seasonPlanDao().insert(seasonPlan);
        for(int i = 0; i < 366; i++, date = DateUtil.addDays(date, 1)) {
            Day day = new Day(date, id);
            day.setDream(random.nextDouble() * 50 + 50);
            day.setHealth(random.nextDouble() * 6 + 3);
            day.setActivity(random.nextDouble() * 3);
            day.setMood(random.nextDouble() * 6 + 3);
            long did = database.dayDao().insert(day);
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
