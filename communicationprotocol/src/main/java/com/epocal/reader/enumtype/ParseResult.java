package com.epocal.reader.enumtype;

/**
 * Created by dning on 6/7/2017.
 */

public enum ParseResult {
    Success,
    UnknownFailure,
    UnknownInnerCode,
    BufferLengthIncorrect,
    InvalidMessageCode,
    InvalidParameter,
    InvalidCall,
    CrcCheckFailed,
    LegacyFormat,
    AdvancedParseParamsRequired,
    NotApplicable
}
