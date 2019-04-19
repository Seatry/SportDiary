package com.example.alexander.sportdiary.Fragments;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.alexander.sportdiary.Enums.EditOption;
import com.example.alexander.sportdiary.MainActivity;
import com.example.alexander.sportdiary.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import static com.example.alexander.sportdiary.CollectionContracts.Edit.NAME;
import static com.example.alexander.sportdiary.CollectionContracts.Edit.USER_ID;

public class AddNewEditFragment extends DialogFragment implements View.OnClickListener {

    private String title;
    private EditText editText;
    private EditOption option;
    private DocumentSnapshot updateItem;
    private String collection;
    private FirebaseFirestore db;

    public void setClass(String collection, String title, EditOption option) {
        this.collection = collection;
        this.title = title;
        this.option = option;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_edit, container, false);
        v.findViewById(R.id.cancelAddButton).setOnClickListener(this);
        v.findViewById(R.id.okAddButton).setOnClickListener(this);
        ((TextView) v.findViewById(R.id.add_edit_title)).setText(title);
        editText = v.findViewById(R.id.add_edit_text);
        if (option == EditOption.UPDATE) {
            editText.setText(updateItem.get(NAME).toString());
        }
        db = MainActivity.getInstance().getDb();
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

    public void add() {
        Map<String, Object> elem = new HashMap<>();
        elem.put(USER_ID, MainActivity.getUserId());
        elem.put(NAME, editText.getText().toString());
        db.collection(collection).add(elem);
        dismiss();
    }

    public void update() {
        updateItem.getReference().update(NAME, editText.getText().toString());
        dismiss();
    }

    public void setUpdateItem(DocumentSnapshot snapshot) {
        updateItem = snapshot;
    }
}
