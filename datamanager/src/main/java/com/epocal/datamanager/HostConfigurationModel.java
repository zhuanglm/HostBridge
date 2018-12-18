package com.epocal.datamanager;

import com.epocal.common.realmentities.HostConfiguration;
import com.epocal.common.types.AuthorizationLogin;
import com.epocal.common.types.LogoutPowerOffType;
import com.epocal.common.types.am.Temperatures;
import com.epocal.datamanager.realmrepository.RepositoryManager;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmModel;

/**
 * Created by bmate on 4/5/2017.
 */

public class HostConfigurationModel {

    Realm mRealm = null;

    public void openRealmInstance() {
        if (mRealm == null)
            mRealm = Realm.getDefaultInstance();
    }

    public void closeRealmInstance() {
        if (mRealm != null) {
            mRealm.close();
            mRealm = null;
        }
    }

    public void updateHostConfiguration(final HostConfiguration input) {
        Realm realm = (mRealm == null) ? Realm.getDefaultInstance() : mRealm;

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                HostConfiguration hf = realm.where(HostConfiguration.class)
                        .findFirst();
                hf.setSaveRawDataMode(input.getSaveRawDataMode());
                hf.setAuthorizationLogin(input.getAuthorizationLogin());
        //        hf.setHemodilutionPolicy(input.getHemodilutionPolicy());
                hf.setDisplayBEb(input.isDisplayBEb());
                hf.setDisplayBEecf(input.isDisplayBEecf());
                hf.setAllowExpiredCards(input.isAllowExpiredCards());
                hf.setAllowIndividualTestSelection(input.isAllowIndividualTestSelection());
                hf.setCloseUnattendedTests(input.isCloseUnattendedTests());
                hf.setEnableInactivityTimer(input.isEnableInactivityTimer());
                hf.setDepartment(input.getDepartment());
                hf.setHospitalName(input.getHospitalName());
                hf.setLogoutWhenInactive(input.isLogoutWhenInactive());
                hf.setTemperatureUnit(input.getTemperatureUnit());
                hf.setBackgroundsyncEnabled(input.isBackgroundsyncEnabled());
                hf.setScheduledsyncEnabled(input.isScheduledsyncEnabled());
                hf.setAgapType(input.getAgapType());
                hf.setAllowRecallData(input.isAllowRecallData());
                hf.setRetainPatientId(input.isRetainPatientId());
                hf.setRetainSampleType(input.isRetainSampleType());
                hf.setReaderMaintenanceEnabled(input.isReaderMaintenanceEnabled());
                hf.setAllowRejectTests(input.isAllowRejectTests());
                hf.setEnforceCriticalHandling(input.isEnforceCriticalHandling());
                hf.setEnablePatientIdLookup(input.isEnablePatientIdLookup());
                hf.setAllowCustomEntryTypes(input.getAllowCustomEntryTypes());
                hf.setReaderType(input.getReaderType());
                hf.setBUNEnabled(input.isBUNEnabled());
                hf.setInactivityTimer(input.getInactivityTimer());
                hf.setSensorConfigVersion(input.getSensorConfigVersion());
                hf.setLogoutPowerOffType(input.getLogoutPowerOffType());
            }
        });
        realm.close();
    }

    public HostConfiguration getUnmanagedHostConfiguration() {
        Realm realm = (mRealm == null) ? Realm.getDefaultInstance() : mRealm;

        HostConfiguration hostConfiguration = getHostConfiguration(realm, false);

        if(mRealm == null)
            realm.close();

        return hostConfiguration;
    }

    private HostConfiguration getHostConfiguration(Realm realm, boolean managed) {
        HostConfiguration hc = realm.where(HostConfiguration.class).findFirst();
        if (managed) {
            return hc;
        } else {
            return realm.copyFromRealm(hc);
        }
    }

    public void resetToFactoryDefault() {
        Realm realm = (mRealm == null) ? Realm.getDefaultInstance() : mRealm;

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                HostConfiguration hc = realm.where(HostConfiguration.class)
                        .findFirst();
                hc.resetFactoryDefault(RepositoryManager.NEXTGEN_MODE);
            }
        });

        if (mRealm == null)
            realm.close();
    }

    public final <E extends RealmModel> void addChangeListener(RealmChangeListener<E> realmChangeListener) {
        Realm realm = (mRealm == null) ? Realm.getDefaultInstance() : mRealm;

        getHostConfiguration(realm, true).addChangeListener(realmChangeListener);

        if (mRealm == null)
            realm.close();
    }

    public void setHospitalName(final String hospitalName) {
        Realm realm = (mRealm == null) ? Realm.getDefaultInstance() : mRealm;

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                HostConfiguration hostConfiguration = getHostConfiguration(realm, true);
                hostConfiguration.setHospitalName(hospitalName);
            }
        });

        if (mRealm == null)
            realm.close();
    }

    public String getHospitalName() {
        Realm realm = (mRealm == null) ? Realm.getDefaultInstance() : mRealm;

        String hospitalName = getUnmanagedHostConfiguration().getHospitalName();

        if (mRealm == null)
            realm.close();

        return hospitalName;
    }

    public void setAuthorizationLogin(final AuthorizationLogin authorizationLogin) {
        Realm realm = (mRealm == null) ? Realm.getDefaultInstance() : mRealm;

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                HostConfiguration hostConfiguration = getHostConfiguration(realm, true);
                hostConfiguration.setAuthorizationLogin(authorizationLogin);
            }
        });

        if (mRealm == null)
            realm.close();
    }

    public AuthorizationLogin getAuthorizationLogin() {
        Realm realm = (mRealm == null) ? Realm.getDefaultInstance() : mRealm;

        AuthorizationLogin authorizationLogin = getUnmanagedHostConfiguration().getAuthorizationLogin();

        if (mRealm == null)
            realm.close();

        return authorizationLogin;
    }

    public void setEnableInactivity(final boolean enableInactivity) {
        Realm realm = (mRealm == null) ? Realm.getDefaultInstance() : mRealm;

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                HostConfiguration hostConfiguration = getHostConfiguration(realm, true);
                hostConfiguration.setEnableInactivityTimer(enableInactivity);
            }
        });

        if (mRealm == null)
            realm.close();
    }

    public boolean isEnableInactivityTimer() {
        Realm realm = (mRealm == null) ? Realm.getDefaultInstance() : mRealm;

        boolean enableInactivityTimer = getUnmanagedHostConfiguration().isEnableInactivityTimer();

        if (mRealm == null)
            realm.close();

        return enableInactivityTimer;
    }

    public void setInactivityTimer(final int inactivityTimer) {
        Realm realm = (mRealm == null) ? Realm.getDefaultInstance() : mRealm;

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                HostConfiguration hostConfiguration = getHostConfiguration(realm, true);
                hostConfiguration.setInactivityTimer(inactivityTimer);
            }
        });

        if (mRealm == null)
            realm.close();
    }

    public int getInactivityTimer() {
        Realm realm = (mRealm == null) ? Realm.getDefaultInstance() : mRealm;

        int inactivityTimer = getUnmanagedHostConfiguration().getInactivityTimer();

        if (mRealm == null)
            realm.close();

        return inactivityTimer;
    }

    public void setLogoutWhenInactive(final boolean inactivityLogout) {
        Realm realm = (mRealm == null) ? Realm.getDefaultInstance() : mRealm;

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                HostConfiguration hostConfiguration = getHostConfiguration(realm, true);
                hostConfiguration.setLogoutWhenInactive(inactivityLogout);
            }
        });

        if (mRealm == null)
            realm.close();
    }

    public boolean isLogoutWhenInactive() {
        Realm realm = (mRealm == null) ? Realm.getDefaultInstance() : mRealm;

        boolean logoutWhenInactive = getUnmanagedHostConfiguration().isLogoutWhenInactive();

        if (mRealm == null)
            realm.close();

        return logoutWhenInactive;
    }

    public void setLogoutPowerOffType(final LogoutPowerOffType powerOffType) {
        Realm realm = (mRealm == null) ? Realm.getDefaultInstance() : mRealm;

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                HostConfiguration hostConfiguration = getHostConfiguration(realm, true);
                hostConfiguration.setLogoutPowerOffType(powerOffType);
            }
        });

        if (mRealm == null)
            realm.close();
    }

    public LogoutPowerOffType getLogoutPowerOffType() {
        Realm realm = (mRealm == null) ? Realm.getDefaultInstance() : mRealm;

        LogoutPowerOffType logoutPowerOffType = getUnmanagedHostConfiguration().getLogoutPowerOffType();

        if (mRealm == null)
            realm.close();

        return logoutPowerOffType;
    }

    public void setTemperatureUnit(final Temperatures temperatureType) {
        Realm realm = (mRealm == null) ? Realm.getDefaultInstance() : mRealm;

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                HostConfiguration hostConfiguration = getHostConfiguration(realm, true);
                hostConfiguration.setTemperatureUnit(temperatureType);
            }
        });

        if (mRealm == null)
            realm.close();
    }

    public Temperatures getTemperatureUnit() {
        Realm realm = (mRealm == null) ? Realm.getDefaultInstance() : mRealm;

        Temperatures temperatureUnit = getUnmanagedHostConfiguration().getTemperatureUnit();

        if (mRealm == null)
            realm.close();

        return temperatureUnit;
    }

    public void setAllowRejectTests(final boolean isChecked) {
        Realm realm = (mRealm == null) ? Realm.getDefaultInstance() : mRealm;

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                HostConfiguration hostConfiguration = getHostConfiguration(realm, true);
                hostConfiguration.setAllowRejectTests(isChecked);
            }
        });

        if (mRealm == null)
            realm.close();
    }

    public boolean isAllowRejectTests() {
        Realm realm = (mRealm == null) ? Realm.getDefaultInstance() : mRealm;

        boolean allowRejectedTests = getUnmanagedHostConfiguration().isAllowRejectTests();

        if (mRealm == null)
            realm.close();

        return allowRejectedTests;
    }

    public void setEnforceCriticalHandling(final boolean isChecked) {
        Realm realm = (mRealm == null) ? Realm.getDefaultInstance() : mRealm;

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                HostConfiguration hostConfiguration = getHostConfiguration(realm, true);
                hostConfiguration.setEnforceCriticalHandling(isChecked);
            }
        });

        if (mRealm == null)
            realm.close();
    }

    public boolean isEnforceCriticalHandling() {
        Realm realm = (mRealm == null) ? Realm.getDefaultInstance() : mRealm;

        boolean enforceCriticalHandling = getUnmanagedHostConfiguration().isEnforceCriticalHandling();

        if (mRealm == null)
            realm.close();

        return enforceCriticalHandling;
    }

    public void setRetainPatientId(final boolean isChecked) {
        Realm realm = (mRealm == null) ? Realm.getDefaultInstance() : mRealm;

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                HostConfiguration hostConfiguration = getHostConfiguration(realm, true);
                hostConfiguration.setRetainPatientId(isChecked);
            }
        });

        if (mRealm == null)
            realm.close();
    }

    public boolean isRetainPatientId() {
        Realm realm = (mRealm == null) ? Realm.getDefaultInstance() : mRealm;

        boolean retainPatientId = getUnmanagedHostConfiguration().isRetainPatientId();

        if (mRealm == null)
            realm.close();

        return retainPatientId;
    }

    public void setRetainSampleType(final boolean isChecked) {
        Realm realm = (mRealm == null) ? Realm.getDefaultInstance() : mRealm;

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                HostConfiguration hostConfiguration = getHostConfiguration(realm, true);
                hostConfiguration.setRetainSampleType(isChecked);
            }
        });

        if (mRealm == null)
            realm.close();
    }

    public boolean isRetainSampleType() {
        Realm realm = (mRealm == null) ? Realm.getDefaultInstance() : mRealm;

        boolean retainSampleType = getUnmanagedHostConfiguration().isRetainSampleType();

        if (mRealm == null)
            realm.close();

        return retainSampleType;
    }

    public void setAllowRecallData(final boolean isChecked) {
        Realm realm = (mRealm == null) ? Realm.getDefaultInstance() : mRealm;

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                HostConfiguration hostConfiguration = getHostConfiguration(realm, true);
                hostConfiguration.setAllowRecallData(isChecked);
            }
        });

        if (mRealm == null)
            realm.close();
    }

    public boolean isAllowRecallData() {
        Realm realm = (mRealm == null) ? Realm.getDefaultInstance() : mRealm;

        boolean allowRecallData = getUnmanagedHostConfiguration().isAllowRecallData();

        if (mRealm == null)
            realm.close();

        return allowRecallData;
    }

    public void setCloseUnattendedTests(final boolean isChecked) {
        Realm realm = (mRealm == null) ? Realm.getDefaultInstance() : mRealm;

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                HostConfiguration hostConfiguration = getHostConfiguration(realm, true);
                hostConfiguration.setCloseUnattendedTests(isChecked);
            }
        });

        if (mRealm == null)
            realm.close();
    }

    public boolean isCloseUnattendedTests() {
        Realm realm = (mRealm == null) ? Realm.getDefaultInstance() : mRealm;

        boolean closeUnattendedTests = getUnmanagedHostConfiguration().isCloseUnattendedTests();

        if (mRealm == null)
            realm.close();

        return closeUnattendedTests;
    }

    public void setAllowExpiredCards(final boolean isChecked) {
        Realm realm = (mRealm == null) ? Realm.getDefaultInstance() : mRealm;

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                HostConfiguration hostConfiguration = getHostConfiguration(realm, true);
                hostConfiguration.setAllowExpiredCards(isChecked);
            }
        });

        if (mRealm == null)
            realm.close();
    }

    public boolean isAllowExpiredCards() {
        Realm realm = (mRealm == null) ? Realm.getDefaultInstance() : mRealm;

        boolean allowExpiredCards = getUnmanagedHostConfiguration().isAllowExpiredCards();

        if (mRealm == null)
            realm.close();

        return allowExpiredCards;
    }

    public void setEnablePatientIdLookup(final boolean isChecked) {
        Realm realm = (mRealm == null) ? Realm.getDefaultInstance() : mRealm;

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                HostConfiguration hostConfiguration = getHostConfiguration(realm, true);
                hostConfiguration.setEnablePatientIdLookup(isChecked);
            }
        });

        if (mRealm == null)
            realm.close();
    }

    public boolean isEnablePatientIdLookup() {
        Realm realm = (mRealm == null) ? Realm.getDefaultInstance() : mRealm;

        boolean patientIdLookupEnabled = getUnmanagedHostConfiguration().isEnablePatientIdLookup();

        if (mRealm == null)
            realm.close();

        return patientIdLookupEnabled;
    }

    public void setPrintRangeIfHighLow(final boolean isChecked) {
        Realm realm = (mRealm == null) ? Realm.getDefaultInstance() : mRealm;

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                HostConfiguration hostConfiguration = getHostConfiguration(realm, true);
                hostConfiguration.setPrintRangeIfHighLow(isChecked);
            }
        });

        if (mRealm == null)
            realm.close();
    }

    public boolean isPrintRangeIfHighLow() {
        Realm realm = (mRealm == null) ? Realm.getDefaultInstance() : mRealm;

        boolean printRangeIfHighLow = getUnmanagedHostConfiguration().getPrintRangeIfHighLow();

        if (mRealm == null)
            realm.close();

        return printRangeIfHighLow;
    }

    public void setPrintQARange(final boolean isChecked) {
        Realm realm = (mRealm == null) ? Realm.getDefaultInstance() : mRealm;

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                HostConfiguration hostConfiguration = getHostConfiguration(realm, true);
                hostConfiguration.setPrintQARange(isChecked);
            }
        });

        if (mRealm == null)
            realm.close();
    }

    public boolean isPrintQARange() {
        Realm realm = (mRealm == null) ? Realm.getDefaultInstance() : mRealm;

        boolean printQARange = getUnmanagedHostConfiguration().getPrintQARange();

        if (mRealm == null)
            realm.close();

        return printQARange;
    }

    public void setPrintQAInfo(final boolean isChecked) {
        Realm realm = (mRealm == null) ? Realm.getDefaultInstance() : mRealm;

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                HostConfiguration hostConfiguration = getHostConfiguration(realm, true);
                hostConfiguration.setPrintQAInfo(isChecked);
            }
        });

        if (mRealm == null)
            realm.close();
    }

    public boolean isPrintQAInfo() {
        Realm realm = (mRealm == null) ? Realm.getDefaultInstance() : mRealm;

        boolean printQAInfo = getUnmanagedHostConfiguration().getPrintQAInfo();

        if (mRealm == null)
            realm.close();

        return printQAInfo;
    }
}
