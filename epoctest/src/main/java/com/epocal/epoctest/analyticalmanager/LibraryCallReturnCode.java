package com.epocal.epoctest.analyticalmanager;

public enum LibraryCallReturnCode {
    /**
     * Set each entry by referencing the C++ enum entry's index for each entry, in
     * sequential order.  The index is used to lookup the value for the enum as defined
     * in the native enum definition.
     *
     * NOTE: enumValue(xyz) => "xyz" is not the property's value, rather it is the index of the
     * entry as defined in the native C++ enum definition.
     *      e.g. C++11
     *                  enum class MyEnum : int
     *                  {
     *                      enumEntry0 = 123,
     *                      enumEntry1 = 456,
     *                      enumEntry2 = 789,
     *                  };
     *
     *           Java
     *                  public enum MyEnum {
     *                      enumEntry_Index_0(0),
     *                      enumEntry_Index_1(1),
     *                      enumEntry_Index_2(2);
     *
     *                      ...
     *                  }
     */

    UNDEFINED (0),
    SUCCESS (1),
    UNEXPECTED_ERROR (2),
    ANALYTICAL_MANAGER_DEFAULT_EXCEPTION (3),
    AM_CPP_DEFAULT_EXCEPTION (4),
    AM_CPP_PROTOBUF_DECODING_ERROR (5),
    AM_CPP_PROTOBUF_DECODING_EMPTY_INPUT_BUFFER (6),
    AM_CPP_PROTOBUF_DECODING_MISSING_INPUT_PARAMETER (7),
    AM_CPP_PROTOBUF_DECODING_FAILED_DESERIALIZE (8),
    AM_CPP_PROTOBUF_ENCODING_ERROR (9),
    AM_CPP_PROTOBUF_ENCODING_FAILED_SERIALIZE (10),
    AM_DOTNET_DEFAULT_EXCEPTION (11),
    AM_DOTNET_PROTOBUF_DECODING_ERROR (12),
    AM_DOTNET_PROTOBUF_DECODING_EMPTY_INPUT_BUFFER (13),
    AM_DOTNET_PROTOBUF_DECODING_MISSING_INPUT_PARAMETER (14),
    AM_DOTNET_PROTOBUF_DECODING_FAILED_DESERIALIZE (15),
    AM_DOTNET_PROTOBUF_ENCODING_ERROR (16),
    AM_DOTNET_PROTOBUF_ENCODING_FAILED_SERIALIZE (17),
    AM_DOTNET_PROTOBUF_NULL_BUFFER(18),
    AM_DOTNET_PROTOBUF_COPY_BUFFER_ERROR(19),
    AM_JAVA_DEFAULT_EXCEPTION (20),
    AM_JAVA_PROTOBUF_DECODING_ERROR (21),
    AM_JAVA_PROTOBUF_DECODING_EMPTY_INPUT_BUFFER (22),
    AM_JAVA_PROTOBUF_DECODING_MISSING_INPUT_PARAMETER (23),
    AM_JAVA_PROTOBUF_DECODING_FAILED_DESERIALIZE (24),
    AM_JAVA_PROTOBUF_ENCODING_ERROR (25),
    AM_JAVA_PROTOBUF_ENCODING_FAILED_SERIALIZE (26),
    AM_JNI_DEFAULT_EXCEPTION (27),
    ENUM_UNINITIALIZED; // Keep this one as the last element

    public final int value;
    public final int index;

    LibraryCallReturnCode() {
        // If index is not specified, invalid value 0xDEAD is assigned as value.
        // ENUM_UNINITIALIZED is assigned 0xDEAD value and is used to indicate invalid enum value.
        this.value = 0xDEAD;
        this.index = this.ordinal();
    }

    LibraryCallReturnCode(int enumIndex) {
        // Get the actual enum value from the C++ enum via the jniLibraryCallReturnCodeValue() JNI call
        this.value = AnalyticalManager.getLibraryCallReturnCodeValue(enumIndex);
        this.index = enumIndex;
    }

    public static LibraryCallReturnCode convert(int value) {
        int enumIndex = Integer.MAX_VALUE;

        for (LibraryCallReturnCode rc : LibraryCallReturnCode.values()) {
            if (rc.value == value) {
                enumIndex = rc.index;
                break;
            }
        }

        if (enumIndex == Integer.MAX_VALUE) {
            return LibraryCallReturnCode.ENUM_UNINITIALIZED;
        }

        return LibraryCallReturnCode.values()[enumIndex];
    }

    public static LibraryCallReturnCode fromInt(int i) {
        return convert(i);
    }
}
