package com.example.alexander.sportdiary.Fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.alexander.sportdiary.Adapters.CompetitionScheduleAdapter;
import com.example.alexander.sportdiary.Enums.EditOption;
import com.example.alexander.sportdiary.MainActivity;
import com.example.alexander.sportdiary.R;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Source;

import java.util.Date;

import static com.example.alexander.sportdiary.CollectionContracts.Collections.COMPETITIONS;
import static com.example.alexander.sportdiary.CollectionContracts.Collections.DAYS;
import static com.example.alexander.sportdiary.CollectionContracts.Collections.DAY_COMPETITIONS;
import static com.example.alexander.sportdiary.CollectionContracts.Collections.DIARIES;
import static com.example.alexander.sportdiary.CollectionContracts.Collections.IMPORTANCES;
import static com.example.alexander.sportdiary.CollectionContracts.Day.DATE;
import static com.example.alexander.sportdiary.CollectionContracts.DayCompetition.COMPETITION_ID;
import static com.example.alexander.sportdiary.CollectionContracts.DayCompetition.IMPORTANCE_ID;


public class CompetitionScheduleFragment extends DialogFragment implements View.OnClickListener {
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_competition_schedule, container, false);
        v.findViewById(R.id.cancelCompetitionButton).setOnClickListener(this);
        v.findViewById(R.id.add_competition_button).setOnClickListener(this);

        RecyclerView recyclerView = v.findViewById(R.id.competition_schedule_items);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        final CompetitionScheduleAdapter adapter = new CompetitionScheduleAdapter();
        recyclerView.setAdapter(adapter);

        FirebaseFirestore db = MainActivity.getInstance().getDb();

        db.collection(DIARIES).document(MainActivity.getSeasonPlanId()).collection(DAYS).get().addOnSuccessListener(queryDocumentSnapshots -> {
            if (queryDocumentSnapshots == null) return;
            for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {
                Date date = snapshot.getDate(DATE);
                snapshot.getReference().collection(DAY_COMPETITIONS).addSnapshotListener((queryDocumentSnapshots1, e) -> {
                    if (queryDocumentSnapshots1 == null) return;
                    for (DocumentChange change : queryDocumentSnapshots1.getDocumentChanges()) {
                        DocumentSnapshot document = change.getDocument();
                        db.collection(COMPETITIONS).document(document.getString(COMPETITION_ID)).get(Source.CACHE).addOnSuccessListener(documentSnapshot -> {
                            if (!documentSnapshot.exists()) {
                                document.getReference().delete();
                            } else {
                                String importanceId = document.getString(IMPORTANCE_ID);
                                if (importanceId != null) {
                                    db.collection(IMPORTANCES).document(importanceId).get(Source.CACHE).addOnSuccessListener(documentSnapshot1 -> {
                                        if (!documentSnapshot1.exists()) {
                                            document.getReference().update(IMPORTANCE_ID, FieldValue.delete());
                                        }
                                        handleChange(adapter, change, document, date);
                                    });
                                } else {
                                    handleChange(adapter, change, document, date);
                                }
                            }
                        });
                    }
                    adapter.notifyDataSetChanged();
                });
            }
        });

        return v;
    }

    public void handleChange(final CompetitionScheduleAdapter adapter, DocumentChange change, final DocumentSnapshot document, final Date date) {
        switch (change.getType()) {
            case ADDED:
                adapter.addData(new Pair<>(document, date));
                break;
            case MODIFIED:
                adapter.updateData(new Pair<>(document, date));
                break;
            case REMOVED:
                adapter.removeData(new Pair<>(document, date));
                break;
        }
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
            case R.id.cancelCompetitionButton:
                dismiss();
                break;
            case R.id.add_competition_button:
                AddCompetitionScheduleFragment scheduleFragment = new AddCompetitionScheduleFragment();
                scheduleFragment
                        .setOption(EditOption.INSERT)
                        .setTitle(getString(R.string.addCompetition));
                scheduleFragment.show(MainActivity.getInstance().getSupportFragmentManager(), "addSchedule");
                break;
        }
    }
}
