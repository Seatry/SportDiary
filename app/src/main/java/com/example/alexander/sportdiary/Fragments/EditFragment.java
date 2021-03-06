package com.example.alexander.sportdiary.Fragments;

import android.app.Dialog;
import android.arch.lifecycle.LiveData;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.alexander.sportdiary.Adapters.EditAdapter;
import com.example.alexander.sportdiary.Dao.EditDao.EditDao;
import com.example.alexander.sportdiary.Enums.EditOption;
import com.example.alexander.sportdiary.Entities.EditEntities.Edit;
import com.example.alexander.sportdiary.MainActivity;
import com.example.alexander.sportdiary.R;

import java.util.List;

public class EditFragment<T extends Edit> extends DialogFragment implements View.OnClickListener {

    private EditAdapter<T> adapter;
    private Class<T> cls;
    private String title;
    private String addDialogTitle;
    private String updateDialogTitle;
    private String table;
    private EditDao<T> dao;

    public void setClass(Class<T> cls, String title, EditDao<T> dao, String addDialogTitle, String updateDialogTitle, String table) {
        this.cls = cls;
        this.title = title;
        this.dao = dao;
        this.addDialogTitle = addDialogTitle;
        this.updateDialogTitle = updateDialogTitle;
        this.table = table;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_edit, container, false);
        v.findViewById(R.id.cancelButton).setOnClickListener(this);
        v.findViewById(R.id.add_button).setOnClickListener(this);
        ((TextView) v.findViewById(R.id.edit_title)).setText(title);

        RecyclerView recyclerView = v.findViewById(R.id.edit_items);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new EditAdapter<>(getContext(), dao);
        adapter.setClass(cls, updateDialogTitle, table);
        recyclerView.setAdapter(adapter);

        LiveData<List<T>> editLiveData = dao.getAllByUserId(MainActivity.getUserId());

        editLiveData.observe(this, elems -> {
            adapter.setData(elems);
            adapter.notifyDataSetChanged();
        });
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null)
        {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancelButton:
                dismiss();
                break;
            case R.id.add_button:
                AddNewEditFragment<T> dialogFragment = new AddNewEditFragment<>();
                dialogFragment.setClass(cls, addDialogTitle, dao, EditOption.INSERT, table);
                dialogFragment.show(MainActivity.getInstance().getSupportFragmentManager(), "addNewDialog");
                break;
        }
    }

}
