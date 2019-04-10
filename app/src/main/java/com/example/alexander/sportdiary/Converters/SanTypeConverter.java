package com.example.alexander.sportdiary.Converters;

import android.arch.persistence.room.TypeConverter;

import com.example.alexander.sportdiary.Enums.SanType;


public class SanTypeConverter {
    @TypeConverter
    public static Integer fromSanType(SanType type){
        if (type != null) {
            return type.getValue();
        } else {
            return null;
        }
    }

    @TypeConverter
    public static SanType toSanType(Integer type){
        if (SanType.HEALTH.getValue() == type) return SanType.HEALTH;
        else if (SanType.ACTIVITY.getValue() == type) return SanType.ACTIVITY;
        else if (SanType.MOOD.getValue() == type) return SanType.MOOD;
        else return null;
    }
}
