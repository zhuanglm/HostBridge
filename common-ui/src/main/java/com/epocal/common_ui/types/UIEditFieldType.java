package com.epocal.common_ui.types;

import java.util.HashMap;
import java.util.Map;

public enum UIEditFieldType {
    Unknown (0),
    TextView (1),
    EditTextBox (2),
    EditPlusSwitch (3),
    RadioButton (4),
    CheckBox (5),
    Combination (6),
    CustomLayout(7);


    public final int value;
    UIEditFieldType(Integer value)
    {
        this.value = value;
    }
    public static UIEditFieldType convert(Integer value) {return UIEditFieldType.values()[value];}


    private  static final Map<Integer,UIEditFieldType> typeMap = new HashMap<Integer,UIEditFieldType>();
    static {
        for (UIEditFieldType type : UIEditFieldType.values()){
            typeMap.put(type.value,type);
        }
    }
    public  static UIEditFieldType fromInt(int i){
        UIEditFieldType retval = typeMap.get(Integer.valueOf(i));
        if (retval ==null){
            return UIEditFieldType.Unknown;
        }
        return retval;
    }
    public int getValue() {
        return value;
    }
}
