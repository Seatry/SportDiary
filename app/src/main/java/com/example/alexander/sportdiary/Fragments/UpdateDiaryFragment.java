package com.example.alexander.sportdiary.Fragments;

import android.app.DatePickerDialog;
import android.database.sqlite.SQLiteConstraintException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.alexander.sportdiary.Dao.DayDao;
import com.example.alexander.sportdiary.Dao.SeasonPlanDao;
import com.example.alexander.sportdiary.Entities.Day;
import com.example.alexander.sportdiary.Entities.SeasonPlan;
import com.example.alexander.sportdiary.MainActivity;
import com.example.alexander.sportdiary.MenuModel;
import com.example.alexander.sportdiary.R;
import com.example.alexander.sportdiary.Utils.DateUtil;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import static com.example.alexander.sportdiary.MenuItemIds.CALENDAR;
import static com.example.alexander.sportdiary.MenuItemIds.DIARY_GROUP;
import static com.example.alexander.sportdiary.Utils.DateUtil.sdf;

public class UpdateDiaryFragment extends DialogFragment implements View.OnClickListener {
    private EditText editNameText;
    private EditText editStartText;
    private int id;
    private SeasonPlanDao dao;
    private DayDao dayDao;
    private SeasonPlan seasonPlan;

    public void setItemId(int id) {
        this.id = id;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_update_diary, container, false);
        v.findViewById(R.id.cancelUpdateDiary).setOnClickListener(this);
        v.findViewById(R.id.okUpdateDiary).setOnClickListener(this);
        v.findViewById(R.id.removeDiary).setOnClickListener(this);
        editNameText = v.findViewById(R.id.update_diary_name);
        editStartText = v.findViewById(R.id.update_diary_start);
        editStartText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(seasonPlan.getStart());
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.getInstance(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                month +=1;
                                String currentDate = dayOfMonth + "." + (month < 10 ? "0" + month : month) + "." + year;
                                editStartText.setText(currentDate);
                            }
                        }, year, month, day
                );
                datePickerDialog.show();
            }
        });

        dao = MainActivity.getInstance().getDatabase().seasonPlanDao();
        dayDao = MainActivity.getInstance().getDatabase().dayDao();

        seasonPlan = dao.getSeasonPlanById(id);
        editNameText.setText(seasonPlan.getName());
        editStartText.setText(sdf.format(seasonPlan.getStart()));

        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancelUpdateDiary:
                dismiss();
                break;
            case R.id.okUpdateDiary:
                update();
                break;
            case R.id.removeDiary:
                remove();
        }
    }

    public void update() {

        try {
            seasonPlan.setName(editNameText.getText().toString());
            String dateText = editStartText.getText().toString();
            final Date date = sdf.parse(dateText);
            seasonPlan.setStart(date);
            dao.update(seasonPlan);

            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    dayDao.deleteBySeasonPlanId(id);
                    for(int i = 0; i < 365; i++) {
                        dayDao.insert(new Day(DateUtil.addDays(date, i), id));
                    }
                }
            });

            if (MainActivity.getSeasonPlanId() != null && MainActivity.getSeasonPlanId() == id) {
                DayFragment dayFragment = new DayFragment();
                dayFragment.setSeasonPlanId(id);
                MainActivity.getInstance().setTitle("(" + seasonPlan.getName().charAt(0) + ")");
                MainActivity.getInstance().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_frame, dayFragment)
                        .commit();
                MainActivity.getInstance().setDayFragment(dayFragment);
            }

            MenuModel menuModel = MenuModel.getMenuModelById(MainActivity.getHeaderList(), DIARY_GROUP.getValue());
            MenuModel diary = MenuModel.getMenuModelById(MainActivity.getChildList().get(menuModel), id);
            diary.setMenuName(seasonPlan.getName() + " " + sdf.format(seasonPlan.getStart()));
            MainActivity.getExpandableListAdapter().notifyDataSetChanged();
            dismiss();
        } catch (SQLiteConstraintException e) {
            e.printStackTrace();
            Toast.makeText(MainActivity.getInstance(), R.string.unique_err, Toast.LENGTH_SHORT).show();
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(MainActivity.getInstance(), R.string.date_format_err, Toast.LENGTH_SHORT).show();

        }
    }

    public void remove() {
        SeasonPlan seasonPlan = dao.getSeasonPlanById(id);
        dao.delete(seasonPlan);
        MenuModel menuModel = MenuModel.getMenuModelById(MainActivity.getHeaderList(), DIARY_GROUP.getValue());
        MenuModel diary = MenuModel.getMenuModelById(MainActivity.getChildList().get(menuModel), id);
        MainActivity.getChildList().get(menuModel).remove(diary);
        MainActivity.getExpandableListAdapter().notifyDataSetChanged();
        dismiss();
    }

}
