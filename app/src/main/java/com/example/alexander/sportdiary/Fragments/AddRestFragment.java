package com.example.alexander.sportdiary.Fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.alexander.sportdiary.Enums.EditOption;
import com.example.alexander.sportdiary.Entities.Rest;
import com.example.alexander.sportdiary.Enums.Table;
import com.example.alexander.sportdiary.MainActivity;
import com.example.alexander.sportdiary.R;
import com.example.alexander.sportdiary.DataBase.SportDataBase;
import com.fasterxml.jackson.core.JsonProcessingException;

import static com.example.alexander.sportdiary.Utils.ToolerOfSpinners.toolSpinner;

public class AddRestFragment extends DialogFragment implements View.OnClickListener {
    private String title;
    private EditOption option;
    private Rest updateItem = new Rest();
    private SportDataBase sportDataBase;
    private Spinner timeSpinner;
    private Spinner placeSpinner;
    private long dayId;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_rest, container, false);
        v.findViewById(R.id.cancelAddRest).setOnClickListener(this);
        v.findViewById(R.id.okAddRest).setOnClickListener(this);
        ((TextView)v.findViewById(R.id.restTitle)).setText(title);

        sportDataBase= MainActivity.getDatabase();
        timeSpinner = v.findViewById(R.id.restTimeSpinner);
        placeSpinner = v.findViewById(R.id.restPlaceSpinner);

        toolSpinner(sportDataBase.timeDao(), timeSpinner, sportDataBase.timeDao().getNameByIdAndUserId(updateItem.getTimeId(), MainActivity.getUserId()));
        toolSpinner(sportDataBase.restPlaceDao(), placeSpinner, sportDataBase.restPlaceDao().getNameByIdAndUserId(updateItem.getPlaceId(), MainActivity.getUserId()));

        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancelAddRest:
                dismiss();
                break;
            case R.id.okAddRest:
                switch (option) {
                    case INSERT:
                        AsyncTask.execute(this::add);
                        break;
                    case UPDATE:
                        AsyncTask.execute(this::update);
                        break;
                }
                dismiss();
                break;
        }
    }

    public void add() {
        Long timeId = timeSpinner.getSelectedItem() == null ? null :
                sportDataBase.timeDao().getIdByName(timeSpinner.getSelectedItem().toString());
        Long restPlaceId = placeSpinner.getSelectedItem() == null ? null
                : sportDataBase.restPlaceDao().getIdByName(placeSpinner.getSelectedItem().toString());
        Rest rest = new Rest(dayId, timeId, restPlaceId);
        long id = sportDataBase.restDao().insert(rest);
        rest.setId(id);
        save(rest);
    }

    public void update() {
        Long timeId = timeSpinner.getSelectedItem() == null ? null :
                sportDataBase.timeDao().getIdByName(timeSpinner.getSelectedItem().toString());
        Long restPlaceId = placeSpinner.getSelectedItem() == null ? null
                : sportDataBase.restPlaceDao().getIdByName(placeSpinner.getSelectedItem().toString());
        updateItem.setTimeId(timeId);
        updateItem.setPlaceId(restPlaceId);
        sportDataBase.restDao().update(updateItem);
        save(updateItem);
    }

    private void save(Rest rest) {
        AsyncTask.execute(() -> {
                    try {
                        MainActivity.syncSave(
                                MainActivity.getObjectMapper().writeValueAsString(
                                        MainActivity.getConverter().convertEntityToDto(rest)
                                ), Table.REST
                        );
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                }
        );
    }

    public String getTitle() {
        return title;
    }

    public AddRestFragment setTitle(String title) {
        this.title = title;
        return this;
    }

    public EditOption getOption() {
        return option;
    }

    public AddRestFragment setOption(EditOption option) {
        this.option = option;
        return this;
    }

    public Rest getUpdateItem() {
        return updateItem;
    }

    public AddRestFragment setUpdateItem(Rest updateItem) {
        this.updateItem = updateItem;
        return this;
    }

    public AddRestFragment setDayId(long dayId) {
        this.dayId = dayId;
        return this;
    }
}
