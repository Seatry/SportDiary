package com.example.alexander.sportdiary.Adapters;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.alexander.sportdiary.Dao.EditDao.EditDao;
import com.example.alexander.sportdiary.Enums.EditOption;
import com.example.alexander.sportdiary.Entities.EditEntities.Edit;
import com.example.alexander.sportdiary.Fragments.AddNewEditFragment;
import com.example.alexander.sportdiary.MainActivity;
import com.example.alexander.sportdiary.ViewHolders.EditViewHolder;
import com.example.alexander.sportdiary.R;

import java.util.ArrayList;
import java.util.List;

public class EditAdapter<T extends Edit> extends RecyclerView.Adapter<EditViewHolder> {
    private static int countItems;
    private Context mCtx;
    private List<T> data = new ArrayList<>();
    private EditDao<T> dao;
    private String dialogTitle;
    private Class<T> cls;

    public void setClass(Class<T> cls, String title) {
        this.cls = cls;
        this.dialogTitle = title;
    }

    public EditAdapter(Context mCtx, EditDao<T> dao) {
        this.mCtx = mCtx;
        countItems = 0;
        this.dao = dao;
    }

    @NonNull
    @Override
    public EditViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.edit_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        EditViewHolder viewHolder = new EditViewHolder(view);
        if(countItems < data.size()) {
            viewHolder.setData(data.get(countItems).getName());
        }
        countItems++;

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final EditViewHolder editViewHolder, final int i) {
        editViewHolder.setData(data.get(i).getName());
        editViewHolder.itemView.findViewById(R.id.edit_opts).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //creating a popup menu
                PopupMenu popup = new PopupMenu(mCtx, editViewHolder.itemView);
                //inflating menu from xml resource
                popup.inflate(R.menu.edit_options);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.update:
                                AddNewEditFragment<T> dialogFragment = new AddNewEditFragment<>();
                                dialogFragment.setClass(cls, dialogTitle, dao, EditOption.UPDATE);
                                dialogFragment.setUpdateItem(data.get(i));
                                dialogFragment.show(MainActivity.getInstance().getSupportFragmentManager(), "updateDialog");
                                break;
                            case R.id.delete:
                                AsyncTask.execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        dao.delete(data.get(i));
                                    }
                                });
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

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public List<T> getData() {
        return data;
    }
}
