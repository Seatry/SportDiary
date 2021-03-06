package com.example.alexander.sportdiary.Adapters;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.alexander.sportdiary.Enums.EditOption;
import com.example.alexander.sportdiary.Entities.Rest;
import com.example.alexander.sportdiary.Enums.Table;
import com.example.alexander.sportdiary.Fragments.AddRestFragment;
import com.example.alexander.sportdiary.MainActivity;
import com.example.alexander.sportdiary.R;
import com.example.alexander.sportdiary.DataBase.SportDataBase;
import com.example.alexander.sportdiary.ViewHolders.RestViewHolder;

import java.util.ArrayList;
import java.util.List;

public class RestAdapter extends RecyclerView.Adapter<RestViewHolder> {
    private static int countItems;
    private SportDataBase sportDataBase = MainActivity.getDatabase();
    private List<Rest> rests = new ArrayList<>();

    @NonNull
    @Override
    public RestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.rest_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        RestViewHolder viewHolder = new RestViewHolder(view);
        if(countItems < rests.size()) {
            Rest rest = rests.get(countItems);
            String time = rest.getTimeId() == null ? "" : sportDataBase.timeDao().getNameByIdAndUserId(rest.getTimeId(), MainActivity.getUserId());
            String place = rest.getPlaceId() == null ? "" : sportDataBase.restPlaceDao().getNameByIdAndUserId(rest.getPlaceId(), MainActivity.getUserId());
            viewHolder.setData(place, time);
        }
        countItems++;

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final RestViewHolder restViewHolder, final int i) {
        Rest rest = rests.get(i);
        String time = rest.getTimeId() == null ? "" : sportDataBase.timeDao().getNameByIdAndUserId(rest.getTimeId(), MainActivity.getUserId());
        String place = rest.getPlaceId() == null ? "" : sportDataBase.restPlaceDao().getNameByIdAndUserId(rest.getPlaceId(), MainActivity.getUserId());
        restViewHolder.setData(place, time);
        restViewHolder.itemView.findViewById(R.id.rest_opts).setOnClickListener(view -> {
            //creating a popup menu
            PopupMenu popup = new PopupMenu(MainActivity.getInstance(), restViewHolder.itemView);
            //inflating menu from xml resource
            popup.inflate(R.menu.edit_options);
            //adding click listener
            popup.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.update:
                        AddRestFragment addRestFragment = new AddRestFragment();
                        addRestFragment
                                .setTitle(MainActivity.getInstance().getString(R.string.updateRest))
                                .setUpdateItem(rests.get(i))
                                .setOption(EditOption.UPDATE);
                        addRestFragment.show(MainActivity.getInstance().getSupportFragmentManager(), "updateRests");
                        break;
                    case R.id.delete:
                        AsyncTask.execute(() -> sportDataBase.restDao().delete(rests.get(i)));
                        MainActivity.syncDelete(rests.get(i).getId(), Table.REST);
                        break;
                }
                return false;
            });
            //displaying the popup
            popup.setGravity(Gravity.END);
            popup.show();
        });
    }

    @Override
    public int getItemCount() {
        return rests.size();
    }

    public List<Rest> getRests() {
        return rests;
    }

    public void setRests(List<Rest> rests) {
        this.rests = rests;
    }
}
