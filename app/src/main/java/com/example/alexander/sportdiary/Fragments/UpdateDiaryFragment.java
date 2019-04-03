package com.example.alexander.sportdiary.Fragments;

import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.alexander.sportdiary.Dao.SeasonPlanDao;
import com.example.alexander.sportdiary.Entities.SeasonPlan;
import com.example.alexander.sportdiary.MainActivity;
import com.example.alexander.sportdiary.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class UpdateDiaryFragment extends DialogFragment implements View.OnClickListener {
    private EditText editNameText;
    private EditText editStartText;
    private int id;
    private Menu menu;
    private SeasonPlanDao dao;

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

        NavigationView navigationView = MainActivity.getInstance().findViewById(R.id.nav_view);
        MenuItem diaryItem = navigationView.getMenu().findItem(R.id.diaries);
        menu = diaryItem.getSubMenu();
        dao = MainActivity.getInstance().getDatabase().seasonPlanDao();
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

        SeasonPlan seasonPlan = dao.getSeasonPlanById(id);
        try {
            seasonPlan.setName(editNameText.getText().toString());
            String dateText = editStartText.getText().toString();
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
            Date date = sdf.parse(dateText);
            seasonPlan.setStart(date);
            dao.update(seasonPlan);
            MenuItem item = menu.findItem(id);
            item.setTitle(seasonPlan.getName() + " " + seasonPlan.getStart());
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
        menu.removeItem(id);
        dismiss();
    }

}
