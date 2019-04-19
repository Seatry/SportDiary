package com.example.alexander.sportdiary.Entities.EditEntities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;

@Entity(indices = {@Index(value = {"name", "userId"}, unique = true)})
public class Importance extends Edit{
    public Importance(String name) {
        super(name);
    }

    public Importance() {

    }
}
