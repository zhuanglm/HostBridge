package com.epocal.host4.aboutscreen;

import com.epocal.common.realmentities.User;
import com.epocal.datamanager.UserModel;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * Created by bmate on 3/21/2017.
 */

public class AboutPresenter implements AboutScreenContract.Presenter {

    AboutScreenContract.View mAboutView;

    // we do a constructor injection here (of the view)
    @Inject
    public AboutPresenter(AboutScreenContract.View view) {
        this.mAboutView = view;
    }

    @Override
    public ArrayList<User> loadUsers() {
        // get users( goes directly into recyclerviewadapter)
        ArrayList<User> userList = new UserModel().getAllUsers();
        mAboutView.showComplete();
        return userList;
    }
}