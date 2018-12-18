package com.epocal.common.types;

import java.util.EnumSet;

/**
 * Created by bmate on 3/23/2017.
 */
public enum Permissions {
    NONE,
    RUNPATIENTTESTS,
    RUNQAANDPATIENTTESTS,
    HOSTADMINISTRATOR
}

//public enum Permissions {
//    None (0),
//    RunPatientTests (1),
//    RunQaTests (2),
//    ManageDepartment (4),
//    SystemAdministrator  (8),
//    HostAdministrator (16),
//    SendTest(32),
//    ResendTest (64),
//    HostGuestUser (128);
//
//    private final Integer value;
//            Permissions(Integer value){
//                this.value = value;
//            }
//}
//
//public class UserPermissions{
//    public static EnumSet<Permissions> HostPermissions = EnumSet.of(
//        Permissions.None,
//        Permissions.RunPatientTests,
//        Permissions.RunQaTests,
//        Permissions.HostAdministrator
//    );
//
//    public static EnumSet<Permissions> EDMPermissions = EnumSet.of(
//            Permissions.None,
//            Permissions.ManageDepartment,
//            Permissions.SystemAdministrator,
//            Permissions.SendTest,
//            Permissions.ResendTest
//    );
//}
