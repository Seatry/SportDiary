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
import com.example.alexander.sportdiary.MenuModel;
import com.example.alexander.sportdiary.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.example.alexander.sportdiary.MenuItemIds.DIARY_GROUP;
import static com.example.alexander.sportdiary.Utils.DateUtil.sdf;

public class UpdateDiaryFragment extends DialogFragment implements View.OnClickListener {
    private EditText editNameText;
    private EditText editStartText;
    private int id;
    private SeasonPlanDao dao;
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

        dao = MainActivity.getInstance().getDatabase().seasonPlanDao();

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
            Date date = sdf.parse(dateText);
            seasonPlan.setStart(date);
            dao.update(seasonPlan);
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
