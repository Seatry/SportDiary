package com.example.alexander.sportdiary.Fragments;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alexander.sportdiary.Enums.EditOption;
import com.example.alexander.sportdiary.Entities.DayToTest;
import com.example.alexander.sportdiary.MainActivity;
import com.example.alexander.sportdiary.R;
import com.example.alexander.sportdiary.DataBase.SportDataBase;

import static com.example.alexander.sportdiary.Utils.ToolerOfSpinners.toolSpinner;

public class AddTestFragment extends DialogFragment implements View.OnClickListener {
    private String title;
    private EditOption option;
    private DayToTest updateItem = new DayToTest();
    private SportDataBase sportDataBase;
    private Spinner testSpinner;
    private long dayId;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_tests, container, false);
        v.findViewById(R.id.cancelAddTest).setOnClickListener(this);
        v.findViewById(R.id.okAddTest).setOnClickListener(this);
        ((TextView)v.findViewById(R.id.testTitle)).setText(title);

        sportDataBase= MainActivity.getInstance().getDatabase();
        testSpinner = v.findViewById(R.id.testSpinner);

        toolSpinner(sportDataBase.testDao(), testSpinner, sportDataBase.testDao().getNameByIdAndUserId(updateItem.getTestId(), MainActivity.getUserId()));
        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancelAddTest:
                dismiss();
                break;
            case R.id.okAddTest:
                switch (option) {
                    case INSERT:
                        add();
                        break;
                    case UPDATE:
                        update();
                        break;
                }
                dismiss();
                break;
        }
    }

    public void add() {
        Long testId = testSpinner.getSelectedItem() == null ? null :
                sportDataBase.testDao().getIdByName(testSpinner.getSelectedItem().toString());
        if (testId == null) {
            Toast.makeText(MainActivity.getInstance(), R.string.no_test_err, Toast.LENGTH_SHORT).show();
            return;
        }
        DayToTest dayToTest = new DayToTest(dayId, testId);
        sportDataBase.dayToTestDao().insert(dayToTest);
    }

    public void update() {
        Long testId = testSpinner.getSelectedItem() == null ? null :
                sportDataBase.testDao().getIdByName(testSpinner.getSelectedItem().toString());
        if (testId == null) {
            Toast.makeText(MainActivity.getInstance(), R.string.no_test_err, Toast.LENGTH_SHORT).show();
            return;
        }
        updateItem.setTestId(testId);
        sportDataBase.dayToTestDao().update(updateItem);
    }

    public String getTitle() {
        return title;
    }

    public AddTestFragment setTitle(String title) {
        this.title = title;
        return this;
    }

    public EditOption getOption() {
        return option;
    }

    public AddTestFragment setOption(EditOption option) {
        this.option = option;
        return this;
    }

    public DayToTest getUpdateItem() {
        return updateItem;
    }

    public AddTestFragment setUpdateItem(DayToTest updateItem) {
        this.updateItem = updateItem;
        return this;
    }

    public AddTestFragment setDayId(long dayId) {
        this.dayId = dayId;
        return this;
    }
}
