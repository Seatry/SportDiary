package com.example.alexander.sportdiary.Fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.alexander.sportdiary.Adapters.EditAdapter;
import com.example.alexander.sportdiary.Entities.EditEntities.Edit;
import com.example.alexander.sportdiary.Enums.EditOption;
import com.example.alexander.sportdiary.MainActivity;
import com.example.alexander.sportdiary.R;
import com.google.firebase.firestore.FirebaseFirestore;

import static com.example.alexander.sportdiary.CollectionContracts.Edit.USER_ID;

public class EditFragment<T extends Edit> extends DialogFragment implements View.OnClickListener {
    private EditAdapter adapter;
    private String title;
    private String addDialogTitle;
    private String updateDialogTitle;
    private String collection;

    public void setClass(String collection, String title, String addDialogTitle, String updateDialogTitle) {
        this.collection = collection;
        this.title = title;
        this.addDialogTitle = addDialogTitle;
        this.updateDialogTitle = updateDialogTitle;
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
        adapter = new EditAdapter(getContext());
        adapter.setClass(collection, updateDialogTitle);
        recyclerView.setAdapter(adapter);

        FirebaseFirestore db = MainActivity.getInstance().getDb();

        db.collection(collection).whereEqualTo(USER_ID, MainActivity.getUserId())
                .addSnapshotListener((queryDocumentSnapshots, e) -> {
                    if (queryDocumentSnapshots == null) return;
                    adapter.setData(queryDocumentSnapshots.getDocuments());
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
                AddNewEditFragment dialogFragment = new AddNewEditFragment();
                dialogFragment.setClass(collection, addDialogTitle, EditOption.INSERT);
                dialogFragment.show(MainActivity.getInstance().getSupportFragmentManager(), "addNewDialog");
                break;
        }
    }

}
