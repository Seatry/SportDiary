package com.example.alexander.sportdiary.Entities.EditEntities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;

@Entity(indices = {@Index(value = {"name", "userId"}, unique = true)})
public class Test extends Edit {
    public Test(String name) {
        super(name);
    }

    public Test() {

    }
}
