package com.example.alexander.sportdiary.Menu;


import android.support.annotation.Nullable;

import java.util.List;

public class MenuModel {

    private String menuName;
    private boolean hasChildren, isGroup;
    private int id;
    @Nullable
    private Integer menuIcon, expandIcon;


    public MenuModel(String menuName, boolean isGroup, boolean hasChildren, int id) {

        this.menuName = menuName;
        this.isGroup = isGroup;
        this.hasChildren = hasChildren;
        this.id = id;
    }

    public static MenuModel getMenuModelById(List<MenuModel> menuModels, int id) {
        for(MenuModel menuModel : menuModels) {
            if (menuModel.getId() == id) {
                return menuModel;
            }
        }
        return null;
    }

    public boolean isHasChildren() {
        return hasChildren;
    }

    public void setHasChildren(boolean hasChildren) {
        this.hasChildren = hasChildren;
    }

    public boolean isGroup() {
        return isGroup;
    }

    public void setGroup(boolean group) {
        isGroup = group;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMenuIcon(Integer menuIcon) {
        this.menuIcon = menuIcon;
    }

    public Integer getMenuIcon() {
        return menuIcon;
    }

    @Nullable
    public Integer getExpandIcon() {
        return expandIcon;
    }

    public void setExpandIcon(@Nullable Integer expandIcon) {
        this.expandIcon = expandIcon;
    }
}
