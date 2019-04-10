package com.example.alexander.sportdiary.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.alexander.sportdiary.Enums.EditOption;
import com.example.alexander.sportdiary.Entities.Day;
import com.example.alexander.sportdiary.Fragments.AddCompetitionScheduleFragment;
import com.example.alexander.sportdiary.MainActivity;
import com.example.alexander.sportdiary.R;
import com.example.alexander.sportdiary.SportDataBase;
import com.example.alexander.sportdiary.ViewHolders.CompetitionScheduleViewHolder;

import java.util.ArrayList;
import java.util.List;

import static com.example.alexander.sportdiary.Utils.DateUtil.sdf;

public class CompetitionScheduleAdapter extends RecyclerView.Adapter<CompetitionScheduleViewHolder> {
    private static int countItems;
    private List<Day> days = new ArrayList<>();
    private SportDataBase sportDataBase = MainActivity.getInstance().getDatabase();

    @NonNull
    @Override
    public CompetitionScheduleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.competition_schedule_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        CompetitionScheduleViewHolder viewHolder = new CompetitionScheduleViewHolder(view);
        if(countItems < days.size()) {
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
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.update:
                                AddCompetitionScheduleFragment scheduleFragment = new AddCompetitionScheduleFragment();
                                scheduleFragment
                                        .setOption(EditOption.UPDATE)
                                        .setTitle(MainActivity.getInstance().getString(R.string.updateCompetition))
                                        .setSeasonPlanId(days.get(i).getSeasonPlanId())
                                        .setUpdateDay(days.get(i));
                                scheduleFragment.show(MainActivity.getInstance().getSupportFragmentManager(), "updateSchedule");
                                break;
                            case R.id.delete:
                                Day updateDay = days.get(i);
                                Long competitionToImportanceId = sportDataBase.dayDao()
                                        .getCompetitionToImportanceIdByDateAndSeasonId(updateDay.getDate(), updateDay.getSeasonPlanId());
                                sportDataBase.competitionToImportanceDao().deleteById(competitionToImportanceId);
                                break;
                        }
                        return false;
                    }
                });
                //displaying the popup
                popup.setGravity(Gravity.END);
                popup.show();
            }
        });
    }

    private void setData(CompetitionScheduleViewHolder viewHolder, int i) {
        Day day = days.get(i);
        Long competitionId = sportDataBase.competitionToImportanceDao()
                .getCompetitionIdById(day.getCompetitionToImportanceId());
        String competition = sportDataBase.competitionDao().getNameById(competitionId);
        Long importanceId = sportDataBase.competitionToImportanceDao()
                .getImportanceIdById(day.getCompetitionToImportanceId());
        String importance = importanceId == null ? "" : sportDataBase.importanceDao().getNameById(importanceId);
        String dateText = sdf.format(day.getDate());
        viewHolder.setData(dateText + ": " + competition + " (" + importance + ")");
    }

    @Override
    public int getItemCount() {
        return days.size();
    }

    public void setData(List<Day> days) {
        this.days = days;
    }
}
