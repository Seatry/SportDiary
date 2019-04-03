package com.example.alexander.sportdiary.Fragments;

import android.database.sqlite.SQLiteConstraintException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.DialogFragment;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.alexander.sportdiary.Dao.DayDao;
import com.example.alexander.sportdiary.Dao.SeasonPlanDao;
import com.example.alexander.sportdiary.Entities.Day;
import com.example.alexander.sportdiary.Entities.SeasonPlan;
import com.example.alexander.sportdiary.MainActivity;
import com.example.alexander.sportdiary.Utils.DateUtil;
import com.example.alexander.sportdiary.Utils.NavigationItemLongPressInterceptor;
import com.example.alexander.sportdiary.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.example.alexander.sportdiary.Utils.DateUtil.sdf;

public class AddNewDiaryFragment extends DialogFragment implements View.OnClickListener {
    private EditText editNameText;
    private EditText editStartText;
    private SeasonPlanDao dao;
    private DayDao dayDao;
    private Menu menu;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_diary, container, false);
        v.findViewById(R.id.cancelAddDiary).setOnClickListener(this);
        v.findViewById(R.id.okAddDiary).setOnClickListener(this);
        editNameText = v.findViewById(R.id.add_diary_name);
        editStartText = v.findViewById(R.id.add_diary_start);

        dao = MainActivity.getInstance().getDatabase().seasonPlanDao();
        dayDao = MainActivity.getInstance().getDatabase().dayDao();
        NavigationView navigationView = MainActivity.getInstance().findViewById(R.id.nav_view);
        MenuItem diaryItem = navigationView.getMenu().findItem(R.id.diaries);
        menu = diaryItem.getSubMenu();
        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancelAddDiary:
                dismiss();
                break;
            case R.id.okAddDiary:
                add();
                break;
        }
    }

    public void add()  {
        SeasonPlan seasonPlan = new SeasonPlan();
        seasonPlan.setName(editNameText.getText().toString());
        try {
            String dateText = editStartText.getText().toString();
            final Date date = sdf.parse(dateText);
            seasonPlan.setStart(date);
            final long id = dao.insert(seasonPlan);
            MenuItem item = menu.add(R.id.diaryMenu,(int) id, 1,
                    seasonPlan.getName() + " " + dateText);
            item.setIcon(R.drawable.ic_menu_share);
            item.setActionView(new NavigationItemLongPressInterceptor(MainActivity.getInstance()));

            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    for(int i = 0; i < 365; i++) {
                        dayDao.insert(new Day(DateUtil.addDays(date, i), id));
                    }
                }
            });

            dismiss();
        } catch (SQLiteConstraintException e) {
            e.printStackTrace();
            Toast.makeText(MainActivity.getInstance(), R.string.unique_err, Toast.LENGTH_SHORT).show();
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(MainActivity.getInstance(), R.string.date_format_err, Toast.LENGTH_SHORT).show();

        }
    }

}