package com.example.alexander.sportdiary.Utils;

import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.alexander.sportdiary.Dao.EditDao.EditDao;
import com.example.alexander.sportdiary.Entities.EditEntities.Edit;
import com.example.alexander.sportdiary.MainActivity;
import com.example.alexander.sportdiary.R;

import java.util.ArrayList;
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

    public static void toolDreamAnswer(Spinner spinner, @Nullable Integer itemToSelect) {
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.getInstance(), android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        List<String> answers = new ArrayList<>();
        answers.add("-");
        answers.add(MainActivity.getInstance().getString(R.string.no));
        answers.add(MainActivity.getInstance().getString(R.string.yes));
        adapter.addAll(answers);
        spinner.setAdapter(adapter);
        if (itemToSelect == null || (itemToSelect != 0 && itemToSelect != 1)) {
            spinner.setSelection(0);
        } else {
            spinner.setSelection(itemToSelect+1);
        }
    }

    public static void toolSanAnswer(Spinner spinner, @Nullable Integer itemToSelect) {
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.getInstance(), android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        List<String> answers = new ArrayList<>();
        answers.add("-");
        for(int i = 3; i >= -3; i--) {
            answers.add(String.valueOf(i));
        }
        adapter.addAll(answers);
        spinner.setAdapter(adapter);
        if (itemToSelect == null || (itemToSelect < -3 && itemToSelect > 3)) {
            spinner.setSelection(0);
        } else {
            spinner.setSelection(answers.indexOf(String.valueOf(itemToSelect)));
        }
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
