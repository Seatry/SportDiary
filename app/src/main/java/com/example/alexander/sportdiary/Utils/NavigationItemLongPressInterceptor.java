package com.example.alexander.sportdiary.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;

@SuppressWarnings("unused")
public class NavigationItemLongPressInterceptor extends View {
    public NavigationItemLongPressInterceptor(Context context) {
        super(context);
        init();
    }

    public NavigationItemLongPressInterceptor(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NavigationItemLongPressInterceptor(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init()
    {

        setVisibility(View.INVISIBLE);
        setLayoutParams(new ViewGroup.LayoutParams(0,0));
    }
    public interface OnNavigationItemLongClickListener {
        void onNavigationItemLongClick(SelectedItem itemHandle, View view);
    }


    public static class SelectedItem {
        private final View actionView;

        private SelectedItem(View actionView) {
            this.actionView = actionView;
        }

        public int getItemId(NavigationView navigationView)
        {
            return getItemId(navigationView.getMenu());
        }

        private int getItemId(Menu menu) {
            for (int i = 0; i < menu.size(); ++i) {
                MenuItem item = menu.getItem(i);
                if (item.getActionView() == actionView) {
                    return item.getItemId();
                }
                SubMenu subMenu = item.getSubMenu();
                if (subMenu != null) {
                    int itemId = getItemId(subMenu);
                    if (itemId != -1) {
                        return itemId;
                    }
                }
            }
            return -1;
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        View parent = getMenuItemParent();
        parent.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                OnNavigationItemLongClickListener receiver = getReceiver();
                if (receiver == null)
                {
                    throw new RuntimeException("Your main activity must implement NavigationViewLongPressInterceptorView.OnNavigationItemLongClickListener");
                }
                View parent = getMenuItemParent();
                receiver.onNavigationItemLongClick(
                        new SelectedItem(NavigationItemLongPressInterceptor.this),parent);
                return true;
            }
        });
    }

    private Activity getActivity() {
        Context context = getContext();
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity)context;
            }
            context = ((ContextWrapper)context).getBaseContext();
        }
        return null;
    }
    private OnNavigationItemLongClickListener getReceiver() {
        Activity activity = getActivity();
        if (activity == null) return null;
        if (activity instanceof OnNavigationItemLongClickListener)
        {
            return (OnNavigationItemLongClickListener)activity;
        }
        return null;
    }

    private View getMenuItemParent() {
        View parent = (View)getParent();
        while (true)
        {
            if (parent.isClickable())
            {
                return parent;

            }
            parent = (View) parent.getParent();
        }
    }
}