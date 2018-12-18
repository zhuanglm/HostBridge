package com.epocal.host4.homescreen;

public class QAMenuModel {
    public interface MenuListener {
        public void onMenuClicked();
    }

    private int icon;
    private String title;
    private boolean isGroupHeader = false;
    private MenuListener listener;

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isGroupHeader() {
        return isGroupHeader;
    }

    public void setGroupHeader(boolean groupHeader) {
        isGroupHeader = groupHeader;
    }

    public MenuListener getActionListener() {
        return listener;
    }

    public void addActionListener(MenuListener listener) {
        this.listener = listener;
    }

    public QAMenuModel(String title) {
        this(-1, title, null);
        isGroupHeader = true;
    }

    public QAMenuModel(int icon, String title, MenuListener listener) {
        super();
        this.icon = icon;
        this.title = title;
        this.listener = listener;
    }
}
