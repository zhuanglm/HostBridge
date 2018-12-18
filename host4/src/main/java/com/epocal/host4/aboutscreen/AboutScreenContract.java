package com.epocal.host4.aboutscreen;

import com.epocal.common.realmentities.User;

import java.util.ArrayList;

/**
 * Created by bmate on 3/20/2017.
 */

public interface AboutScreenContract {
    interface View {
        void showUsers();

        void showError(String message);

        void showComplete();
    }

    interface Presenter {
        ArrayList<User> loadUsers();
    }
}
