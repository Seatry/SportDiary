package com.example.alexander.sportdiary.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.alexander.sportdiary.Enums.EditOption;
import com.example.alexander.sportdiary.Fragments.AddCompetitionScheduleFragment;
import com.example.alexander.sportdiary.MainActivity;
import com.example.alexander.sportdiary.R;
import com.example.alexander.sportdiary.ViewHolders.CompetitionScheduleViewHolder;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.alexander.sportdiary.CollectionContracts.Collections.COMPETITIONS;
import static com.example.alexander.sportdiary.CollectionContracts.Collections.IMPORTANCES;
import static com.example.alexander.sportdiary.CollectionContracts.DayCompetition.COMPETITION_ID;
import static com.example.alexander.sportdiary.CollectionContracts.DayCompetition.IMPORTANCE_ID;
import static com.example.alexander.sportdiary.CollectionContracts.Edit.NAME;
import static com.example.alexander.sportdiary.Utils.DateUtil.sdf;

public class CompetitionScheduleAdapter extends RecyclerView.Adapter<CompetitionScheduleViewHolder> {
    private static int countItems;
    private List<Pair<DocumentSnapshot, Date>> dayCompetitions = new ArrayList<>();
    private FirebaseFirestore db = MainActivity.getInstance().getDb();

    @NonNull
    @Override
    public CompetitionScheduleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.competition_schedule_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        CompetitionScheduleViewHolder viewHolder = new CompetitionScheduleViewHolder(view);
        if(countItems < dayCompetitions.size()) {
            setData(viewHolder, countItems);
        }
        countItems++;

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final CompetitionScheduleViewHolder competitionScheduleViewHolder, final int i) {
        setData(competitionScheduleViewHolder, i);
        competitionScheduleViewHolder.itemView.findViewById(R.id.competition_opts).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //creating a popup menu
                PopupMenu popup = new PopupMenu(MainActivity.getInstance(), competitionScheduleViewHolder.itemView);
                //inflating menu from xml resource
                popup.inflate(R.menu.edit_options);
                //adding click listener
                popup.setOnMenuItemClickListener(item -> {
                    switch (item.getItemId()) {
                        case R.id.update:
                            AddCompetitionScheduleFragment scheduleFragment = new AddCompetitionScheduleFragment();
                            scheduleFragment
                                    .setOption(EditOption.UPDATE)
                                    .setTitle(MainActivity.getInstance().getString(R.string.updateCompetition))
                                    .setUpdateItem(dayCompetitions.get(i));
                            scheduleFragment.show(MainActivity.getInstance().getSupportFragmentManager(), "updateSchedule");
                            break;
                        case R.id.delete:
                            dayCompetitions.get(i).first.getReference().delete();
                            break;
                    }
                    return false;
                });
                //displaying the popup
                popup.setGravity(Gravity.END);
                popup.show();
            }
        });
    }

    private void setData(final CompetitionScheduleViewHolder viewHolder, final int i) {
        String competitionId = dayCompetitions.get(i).first.getString(COMPETITION_ID);
        db.collection(COMPETITIONS).document(competitionId).get().addOnSuccessListener(documentSnapshot -> {
            String competition = documentSnapshot.getString(NAME);
            Object importanceIdObj = dayCompetitions.get(i).first.get(IMPORTANCE_ID);
            String importanceId = importanceIdObj == null ? "" : importanceIdObj.toString();
            db.collection(IMPORTANCES).document(importanceId).get().addOnSuccessListener(documentSnapshot1 -> {
                String importance = documentSnapshot1.exists() ? documentSnapshot1.getString(NAME) : "";
                String dateText = sdf.format(dayCompetitions.get(i).second);
                viewHolder.setData(dateText + ": " + competition + " (" + importance + ")");
            });
        });

    }

    @Override
    public int getItemCount() {
        return dayCompetitions.size();
    }

    public void addData(Pair<DocumentSnapshot, Date> day) {
        dayCompetitions.add(day);
    }

    public void removeData(Pair<DocumentSnapshot, Date> day) {
        dayCompetitions.remove(day);
    }

    public void updateData(Pair<DocumentSnapshot, Date> day) {
        dayCompetitions.remove(day);
        dayCompetitions.add(day);
    }
}
