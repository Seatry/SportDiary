package com.example.alexander.sportdiary.Utils;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.example.alexander.sportdiary.R;

public class SwipeGestureListener extends GestureDetector.SimpleOnGestureListener implements
        View.OnTouchListener {
    private Context context;
    private GestureDetector gDetector;
    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    private View view;

    public SwipeGestureListener() {
        super();
    }

    public SwipeGestureListener(Context context, View v) {
        this(context, null, v);
    }

    private SwipeGestureListener(Context context, GestureDetector gDetector, View v) {
        this.view = v;
        if (gDetector == null)
            gDetector = new GestureDetector(context, this);

        this.context = context;
        this.gDetector = gDetector;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                           float velocityY) {
        if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH) {
            if (Math.abs(e1.getX() - e2.getX()) > SWIPE_MAX_OFF_PATH
                    || Math.abs(velocityY) < SWIPE_THRESHOLD_VELOCITY) {
                return false;
            }
            if (e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE) {
                // bot to top
                view.findViewById(R.id.calendar).setVisibility(View.GONE);
            } else if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE) {
                // top to bot
                view.findViewById(R.id.calendar).setVisibility(View.VISIBLE);
            }
        } else {
            if (Math.abs(velocityX) < SWIPE_THRESHOLD_VELOCITY) {
                return false;
            }
            if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE) {
                // right to left
            } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE) {
                // left to right
            }
        }

        return super.onFling(e1, e2, velocityX, velocityY);

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return gDetector.onTouchEvent(event);
    }

    public GestureDetector getDetector() {
        return gDetector;
    }

}