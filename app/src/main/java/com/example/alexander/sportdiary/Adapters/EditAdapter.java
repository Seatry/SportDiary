package com.example.alexander.sportdiary.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.alexander.sportdiary.Enums.EditOption;
import com.example.alexander.sportdiary.Fragments.AddNewEditFragment;
import com.example.alexander.sportdiary.MainActivity;
import com.example.alexander.sportdiary.R;
import com.example.alexander.sportdiary.ViewHolders.EditViewHolder;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import static com.example.alexander.sportdiary.CollectionContracts.Edit.NAME;

public class EditAdapter extends RecyclerView.Adapter<EditViewHolder> {
    private static int countItems;
    private Context mCtx;
    private List<DocumentSnapshot> data = new ArrayList<>();
    private String dialogTitle;
    private String collection;
    private FirebaseFirestore db;

    public void setClass(String collection, String title) {
        this.collection = collection;
        this.dialogTitle = title;
    }

    public EditAdapter(Context mCtx) {
        this.mCtx = mCtx;
        db = MainActivity.getInstance().getDb();
        countItems = 0;
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
            viewHolder.setData(data.get(countItems).get(NAME).toString());
        }
        countItems++;

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final EditViewHolder editViewHolder, final int i) {
        editViewHolder.setData(data.get(i).get(NAME).toString());
        editViewHolder.itemView.findViewById(R.id.edit_opts).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //creating a popup menu
                PopupMenu popup = new PopupMenu(mCtx, editViewHolder.itemView);
                //inflating menu from xml resource
                popup.inflate(R.menu.edit_options);
                //adding click listener
                popup.setOnMenuItemClickListener(item -> {
                    switch (item.getItemId()) {
                        case R.id.update:
                            AddNewEditFragment dialogFragment = new AddNewEditFragment();
                            dialogFragment.setClass(collection, dialogTitle, EditOption.UPDATE);
                            dialogFragment.setUpdateItem(data.get(i));
                            dialogFragment.show(MainActivity.getInstance().getSupportFragmentManager(), "updateDialog");
                            break;
                        case R.id.delete:
                            db.collection(collection).document(data.get(i).getId()).delete();
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

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<DocumentSnapshot> data) {
        this.data = data;
    }
}
