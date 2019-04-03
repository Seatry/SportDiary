package com.example.alexander.sportdiary.Fragments;

import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alexander.sportdiary.Dao.EditDao.EditDao;
import com.example.alexander.sportdiary.EditOption;
import com.example.alexander.sportdiary.Entities.EditEntities.Edit;
import com.example.alexander.sportdiary.MainActivity;
import com.example.alexander.sportdiary.R;

public class AddNewEditFragment<T extends Edit> extends DialogFragment implements View.OnClickListener {

    private String title;
    private Class<T> cls;
    private EditDao<T> dao;
    private EditText editText;
    private EditOption option;
    private T updateItem;

    public void setClass(Class<T> cls, String title, EditDao<T> dao, EditOption option) {
        this.cls = cls;
        this.title = title;
        this.dao = dao;
        this.option = option;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_edit, container, false);
        v.findViewById(R.id.cancelAddButton).setOnClickListener(this);
        v.findViewById(R.id.okAddButton).setOnClickListener(this);
        ((TextView) v.findViewById(R.id.add_edit_title)).setText(title);
        editText = v.findViewById(R.id.add_edit_text);
        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancelAddButton:
                dismiss();
                break;
            case R.id.okAddButton:
                switch (option) {
                    case INSERT:
                        add();
                        break;
                    case UPDATE:
                        update();
                        break;
                }
                break;
        }
    }

    public void add()  {
        try {
            T elem = cls.newInstance();
            elem.setName(editText.getText().toString());
            dao.insert(elem);
            dismiss();
        } catch (IllegalAccessException | java.lang.InstantiationException e) {
            e.printStackTrace();
        } catch (SQLiteConstraintException e) {
            e.printStackTrace();
            Toast.makeText(MainActivity.getInstance(), R.string.unique_err, Toast.LENGTH_SHORT).show();
        }

    }

    public void update() {
        updateItem.setName(editText.getText().toString());
        try {
            dao.update(updateItem);
            dismiss();
        } catch (SQLiteConstraintException e) {
            e.printStackTrace();
            Toast.makeText(MainActivity.getInstance(), R.string.unique_err, Toast.LENGTH_SHORT).show();
        }
    }

    public void setUpdateItem(T t) {
        updateItem = t;
    }
}
