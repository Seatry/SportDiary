package com.example.alexander.sportdiary.Fragments;

import android.app.DatePickerDialog;
import android.database.sqlite.SQLiteConstraintException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
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

import static com.example.alexander.sportdiary.Enums.MenuItemIds.DIARY_GROUP;
import static com.example.alexander.sportdiary.Utils.DateUtil.sdf;

public class UpdateDiaryFragment extends DialogFragment implements View.OnClickListener {
    private EditText editNameText;
    private EditText editStartText;
    private Spinner maleSpinner;
    private EditText editHrMax;
    private EditText editHrRest;
    private EditText editPerformance;
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
        editStartText.setOnClickListener(v1 -> {
            Calendar cal = Calendar.getInstance();
            cal.setTime(seasonPlan.getStart());
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.getInstance(),
                    (view, year1, month1, dayOfMonth) -> {
                        month1 += 1;
                        String currentDate = dayOfMonth + "." + (month1 < 10 ? "0" + month1 : month1) + "." + year1;
                        editStartText.setText(currentDate);
                    }, year, month, day
            );
            datePickerDialog.show();
        });
        maleSpinner = v.findViewById(R.id.update_maleSpinner);
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.getInstance(), android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter.addAll(getString(R.string.male), getString(R.string.female));
        adapter.notifyDataSetChanged();
        maleSpinner.setAdapter(adapter);
        editHrMax = v.findViewById(R.id.update_hrMax);
        editHrRest = v.findViewById(R.id.update_hrRest);
        editPerformance = v.findViewById(R.id.update_performance);

        dao = MainActivity.getInstance().getDatabase().seasonPlanDao();
        dayDao = MainActivity.getInstance().getDatabase().dayDao();

        seasonPlan = dao.getSeasonPlanById(id);
        editNameText.setText(seasonPlan.getName());
        editStartText.setText(sdf.format(seasonPlan.getStart()));
        maleSpinner.setSelection(seasonPlan.getMale().equals(getString(R.string.male)) ? 0 : 1);
        editHrMax.setText(String.valueOf(seasonPlan.getHrMax()));
        editHrRest.setText(String.valueOf(seasonPlan.getHrRest()));
        editPerformance.setText(String.valueOf(seasonPlan.getLastPerformance()));

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
            if (editHrMax.getText().length() > 0) {
                seasonPlan.setHrMax(Integer.parseInt(editHrMax.getText().toString()));
            }
            if (editHrRest.getText().length() > 0) {
                seasonPlan.setHrRest(Integer.parseInt(editHrRest.getText().toString()));
            }
            if (editPerformance.getText().length() > 0) {
                seasonPlan.setLastPerformance(Integer.parseInt(editPerformance.getText().toString()));
            }
            if (maleSpinner.getSelectedItem() != null) {
                seasonPlan.setMale(maleSpinner.getSelectedItem().toString());
            }
            String dateText = editStartText.getText().toString();
            final Date date = sdf.parse(dateText);
            seasonPlan.setStart(date);
            dao.update(seasonPlan);

            AsyncTask.execute(() -> {
                dayDao.deleteBySeasonPlanId(id);
                for(int i = 0; i < 365; i++) {
                    dayDao.insert(new Day(DateUtil.addDays(date, i), id));
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
