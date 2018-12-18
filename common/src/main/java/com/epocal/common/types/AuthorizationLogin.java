package com.epocal.common.types;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * Created by bmate on 4/4/2017.
 */

public enum AuthorizationLogin {
        None(0)
                {@Override
                public String toString() {
                    return "None";
                }},
        UserIdOnly(1)
                {@Override
                public String toString() {
                    return "User Id Only";
                }},
        UserIdAndPassword(2)
                {@Override
                public String toString() {
                    return "User Id And Password";
                }},;
    public final Integer value;

    AuthorizationLogin(Integer value)
    {
        this.value = value;
    }

    private  static final Map<Integer, AuthorizationLogin> typeMap = new HashMap<>();

    static {
        for (AuthorizationLogin type : AuthorizationLogin.values()){
            typeMap.put(type.value,type);
        }
    }

    public static AuthorizationLogin fromInt(int i) {
        AuthorizationLogin retval = typeMap.get(Integer.valueOf(i));
        if (retval ==null){
            return AuthorizationLogin.None;
        }
        return retval;
    }

}
