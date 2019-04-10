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

import com.example.alexander.sportdiary.EditOption;
import com.example.alexander.sportdiary.Entities.DayToTest;
import com.example.alexander.sportdiary.Fragments.AddTestFragment;
import com.example.alexander.sportdiary.MainActivity;
import com.example.alexander.sportdiary.R;
import com.example.alexander.sportdiary.SportDataBase;
import com.example.alexander.sportdiary.ViewHolders.TestViewHolder;

import java.util.ArrayList;
import java.util.List;

public class TestAdapter extends RecyclerView.Adapter<TestViewHolder> {
    private static int countItems;
    private SportDataBase sportDataBase = MainActivity.getInstance().getDatabase();
    private List<DayToTest> tests = new ArrayList<>();

    @NonNull
    @Override
    public TestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.test_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        TestViewHolder viewHolder = new TestViewHolder(view);
        if(countItems < tests.size()) {
            viewHolder.setData(sportDataBase.testDao().getNameById(tests.get(countItems).getTestId()));
        }
        countItems++;

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final TestViewHolder testViewHolder, final int i) {
        testViewHolder.setData(sportDataBase.testDao().getNameById(tests.get(i).getTestId()));
        testViewHolder.itemView.findViewById(R.id.test_opts).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //creating a popup menu
                PopupMenu popup = new PopupMenu(MainActivity.getInstance(), testViewHolder.itemView);
                //inflating menu from xml resource
                popup.inflate(R.menu.edit_options);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.update:
                                AddTestFragment addTestFragment = new AddTestFragment();
                                addTestFragment
                                        .setTitle(MainActivity.getInstance().getString(R.string.updateTest))
                                        .setUpdateItem(tests.get(i))
                                        .setOption(EditOption.UPDATE);
                                addTestFragment.show(MainActivity.getInstance().getSupportFragmentManager(), "updateTests");
                                break;
                            case R.id.delete:
                                AsyncTask.execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        sportDataBase.dayToTestDao().delete(tests.get(i));
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
        return tests.size();
    }

    public List<DayToTest> getTests() {
        return tests;
    }

    public void setTests(List<DayToTest> tests) {
        this.tests = tests;
    }

}
