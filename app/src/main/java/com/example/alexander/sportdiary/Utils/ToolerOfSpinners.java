package com.example.alexander.sportdiary.Utils;

import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.alexander.sportdiary.Dao.EditDao.EditDao;
import com.example.alexander.sportdiary.Entities.EditEntities.Edit;
import com.example.alexander.sportdiary.MainActivity;

import java.util.List;

public class ToolerOfSpinners {
    public static <T extends Edit> void toolSpinner(final EditDao<T> dao, final Spinner spinner, @Nullable final String itemToSelect) {
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.getInstance(), android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                List<String> names = dao.getAllNames();
                adapter.addAll(names);
                adapter.notifyDataSetChanged();
                if (names.size() > 0 && itemToSelect != null) {
                    System.out.println(names.indexOf(itemToSelect));
                    spinner.setSelection(names.indexOf(itemToSelect));
                }
            }
        });
        spinner.setAdapter(adapter);
    }

    public static <T extends Edit> void toolMultiSpinner(
            final EditDao<T> dao, final MultiSelectionSpinner spinner,
            MultiSelectionSpinner.OnMultipleItemsSelectedListener listener, @Nullable final List<String> itemsToSelect) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                List<String> names = dao.getAllNames();
                if(names.size() > 0) {
                    spinner.setItems(names);
                    if (itemsToSelect != null) {
                        spinner.setSelection(itemsToSelect);
                    }
                }
            }
        });
        spinner.setListener(listener);
    }
}
