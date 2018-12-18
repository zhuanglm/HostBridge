package com.epocal.epoctest.testconfiguration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dning on 9/22/2017.
 */

public class HostTestResultConfig {
    public List<CardSetup> CardSetups;
    public List<CardTabSetup> CardTabSetups;

    public HostTestResultConfig() {
        CardSetups = new ArrayList<CardSetup>();
        CardTabSetups = new ArrayList<CardTabSetup>();
    }
}
