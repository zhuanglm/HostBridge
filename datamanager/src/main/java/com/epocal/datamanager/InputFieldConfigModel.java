package com.epocal.datamanager;

import com.epocal.common.realmentities.InputFieldConfig;
import com.epocal.common.types.InputFieldType;

import java.util.EnumSet;

import io.realm.Realm;

/**
 * Created by bmate on 8/3/2017.
 */

public class InputFieldConfigModel {
    public void createFactoryDefault(Realm realm){
        final EnumSet<InputFieldType> allInputFields = EnumSet.allOf(InputFieldType.class);
        realm.executeTransaction(new Realm.Transaction(){
            @Override
            public void execute(Realm realm){
                for (InputFieldType aFieldType: allInputFields ) {

                    createInputFieldConfiguration(realm,aFieldType);
                }


            }
        });
    }
    private void createInputFieldConfiguration(Realm realm, InputFieldType inputFieldType){
        InputFieldConfig inputFieldConfig = realm.createObject(InputFieldConfig.class);
        inputFieldConfig.setInputFieldType(inputFieldType);
        inputFieldConfig.setBarcodes(131071);
        inputFieldConfig.setTrimBeginning(0);
        inputFieldConfig.setTrimEnding(0);
        inputFieldConfig.setTrim2DBeginning(0);
        inputFieldConfig.setTrim2DEnding(0);
        switch (inputFieldType){
            case UNKNOWN:
                inputFieldConfig.setMinimumLength(0);
                inputFieldConfig.setMaximumLength(255);
                break;
            case USER_ID:
                inputFieldConfig.setMinimumLength(1);
                inputFieldConfig.setMaximumLength(255);
                break;
            case PASSWORD:
                inputFieldConfig.setMinimumLength(4);
                inputFieldConfig.setMaximumLength(255);
                break;
            case PATIENT_ID:
                inputFieldConfig.setMinimumLength(1);
                inputFieldConfig.setMaximumLength(23);
                break;
            case PATIENT_OR_LOT_ID:
                inputFieldConfig.setMinimumLength(1);
                inputFieldConfig.setMaximumLength(23);
                break;
            case ID_2:
                inputFieldConfig.setMinimumLength(0);
                inputFieldConfig.setMaximumLength(23);
                break;
            case COMMENT:
                inputFieldConfig.setMinimumLength(0);
                inputFieldConfig.setMaximumLength(255);
                break;
            case OTHER:
                inputFieldConfig.setMinimumLength(0);
                inputFieldConfig.setMaximumLength(255);
                break;
        }
    }
    /*
     * returns unmanaged InputFieldConfig object
     * to be manipulated in UI, and later passed back to be persisted
     */
    public InputFieldConfig getInputFieldConfig (InputFieldType inputFieldType){
        InputFieldConfig retval = null;
        Realm realm = Realm.getDefaultInstance();
        InputFieldConfig managed = realm.where(InputFieldConfig.class).equalTo("InputFieldTypeValue", inputFieldType.value).findFirst();
        if (managed !=null) {
            retval = realm.copyFromRealm(managed);
        }
        realm.close();
        return  retval;
    }
    /*
     * persists an unmanaged InputFieldConfig object into its managed Realm instance
     */
    public void saveInputFieldConfig (final InputFieldConfig input){
        Realm realm = Realm.getDefaultInstance();
        final InputFieldConfig managed = realm.where(InputFieldConfig.class).equalTo("InputFieldTypeValue", input.getInputFieldType().value).findFirst();
        realm.executeTransaction(new Realm.Transaction(){
            @Override
            public void execute(Realm realm){
                managed.setBarcodes(input.getBarcodes());
                managed.setMaximumLength(input.getMaximumLength());
                managed.setMinimumLength(input.getMinimumLength());
                managed.setTrim2DBeginning(input.getTrim2DBeginning());
                managed.setTrim2DEnding(input.getTrim2DEnding());
                managed.setTrimBeginning(input.getTrimBeginning());
                managed.setTrimEnding(input.getTrimEnding());

            }
        });
        realm.close();
    }
}
