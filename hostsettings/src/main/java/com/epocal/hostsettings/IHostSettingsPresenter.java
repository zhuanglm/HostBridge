package com.epocal.hostsettings;

import java.util.HashMap;

/**
 * Created by Zeeshan A Zakaria on 4/11/2017.
 */

interface IHostSettingsPresenter {
    boolean itemClicked(int childId);

    HashMap<String, String[]> getListItems();

    String[] getListHeaders();
}
