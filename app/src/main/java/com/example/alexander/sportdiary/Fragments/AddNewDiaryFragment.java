package com.example.alexander.sportdiary.Fragments;

import android.database.sqlite.SQLiteConstraintException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.Date;
import java.util.List;

import static com.example.alexander.sportdiary.MenuItemIds.DIARY_GROUP;
import static com.example.alexander.sportdiary.Utils.DateUtil.sdf;

public class AddNewDiaryFragment extends DialogFragment implements View.OnClickListener {
    private EditText editNameText;
    private EditText editStartText;
    private SeasonPlanDao dao;
    private DayDao dayDao;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_diary, container, false);
        v.findViewById(R.id.cancelAddDiary).setOnClickListener(this);
        v.findViewById(R.id.okAddDiary).setOnClickListener(this);
        editNameText = v.findViewById(R.id.add_diary_name);
        editStartText = v.findViewById(R.id.add_diary_start);

        dao = MainActivity.getInstance().getDatabase().seasonPlanDao();
        dayDao = MainActivity.getInstance().getDatabase().dayDao();

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
            MenuModel menuModel = MenuModel.getMenuModelById(MainActivity.getHeaderList(), DIARY_GROUP.getValue());
            List<MenuModel> childs = MainActivity.getChildList().get(menuModel);
            String diaryName = seasonPlan.getName() + " " + sdf.format(seasonPlan.getStart());
            MenuModel childModel = new MenuModel(diaryName, false, false, (int) id);
            childs.add(childModel);
            MainActivity.putToChildList(menuModel, childs);
            MainActivity.getExpandableListAdapter().notifyDataSetChanged();
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