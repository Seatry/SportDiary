package com.example.alexander.sportdiary.CollectionContracts;

public class Edit {
    public static final String USER_ID = "userId";
    public static final String NAME = "name";

    private String id, editName;

    public Edit(String editId, String editName) {
        this.id = editId;
        this.editName = editName;
    }

    @android.support.annotation.NonNull
    @Override
    public String toString() {
        return this.editName;
    }

    public String getId() {
        return id;
    }
}
