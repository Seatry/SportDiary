package com.example.alexander.sportdiary.Utils;

import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.alexander.sportdiary.Dao.EditDao.EditDao;
import com.example.alexander.sportdiary.Entities.EditEntities.Edit;
import com.example.alexander.sportdiary.MainActivity;

import java.util.List;

public class ToolerOfSpinners {
    public static <T extends Edit> void toolSpinner(final EditDao<T> dao, Spinner spinner) {
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.getInstance(), android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                List<String> names = dao.getAllNames();
                adapter.addAll(names);
                adapter.notifyDataSetChanged();
            }
        });
        spinner.setAdapter(adapter);
        spinner.setSelection(0);
    }

    public static <T extends Edit> void toolMultiSpinner(final EditDao<T> dao, final MultiSelectionSpinner spinner,
                                                         MultiSelectionSpinner.OnMultipleItemsSelectedListener listener) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                List<String> names = dao.getAllNames();
                spinner.setItems(names);
                spinner.setSelection(0);
            }
        });
        spinner.setListener(listener);
    }
}
