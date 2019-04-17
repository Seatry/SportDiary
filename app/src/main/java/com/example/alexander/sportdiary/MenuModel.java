package com.example.alexander.sportdiary;


import java.util.List;

public class MenuModel {

    private String menuName;
    private boolean hasChildren, isGroup;
    private String id;


    public MenuModel(String menuName, boolean isGroup, boolean hasChildren, String id) {

        this.menuName = menuName;
        this.isGroup = isGroup;
        this.hasChildren = hasChildren;
        this.id = id;
    }

    public static MenuModel getMenuModelById(List<MenuModel> menuModels, String id) {
        for(MenuModel menuModel : menuModels) {
            if (menuModel.getId().equals(id)) {
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
