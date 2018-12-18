package com.epocal.host4.dmsettingscreen;

public interface DMSettingScreenContract {
    interface View {
        String getAddress();
        String getPort();
        boolean isPresent();

        void setAddress(String address);
        void setPort(String port);
        void setPresent(boolean present);
    }

    interface Presenter {
        void load();
        boolean validateUI();
        void save();
        void unload();
    }
}
