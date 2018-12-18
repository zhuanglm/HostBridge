#ifndef LIB_CALL_RETURN_CODE_H
#define LIB_CALL_RETURN_CODE_H

#ifdef _MANAGED
#define ENUM_DEF(ENUM_NAME) public enum class ENUM_NAME     // Managed C++/CLI
#else
#define ENUM_DEF(ENUM_NAME) enum class ENUM_NAME : int          // C++11
#endif

ENUM_DEF(LibraryCallReturnCode)
{
    UNDEFINED = 0,          // We should never explicitly set LibraryCallReturnCode::UNDEFINED (some languages
                            // default values to zero e.g. Java), and this would signify that the
                            // LibraryCallReturnCode value was not initialized/set.

    SUCCESS = 1,            // Always set LibraryCallReturnCode::SUCCESS when all of the following are successful:
                            // - the serialization/deserialization process (Protobuf)
                            // - the call to the native method completed successfully (calls to AM)
                            //      NOTE: A native method returning an error code IS NOT a
                            //            call 'failure', when an unexpected exception is
                            //            caught... this IS a type of failure.
                            //
                            // ... otherwise set an appropriate LibraryCallReturnCode error.

    UNEXPECTED_ERROR = -1,  // We should only return this error when we do not know what the cause
                            // of the error was; whenever possible, use an existing LibraryCallReturnCode that
                            // matches the real cause of error, or add a new entry that describes
                            // the error condition.

    /// ANALYTICAL MANAGER-RELATED ERRORS (100 - 199)
    // NOTE: Do not set an error when AM returns an error, LibraryCallReturnCode should be set to SUCCESS
    //       whenever the AM method completes successfully.
    ANALYTICAL_MANAGER_DEFAULT_EXCEPTION = 100,                 // "Default exception" caught: catch (...) { }

    /// ERRORS FROM C++ (200+)
    AM_CPP_DEFAULT_EXCEPTION = 200,                             // "Default exception" caught: catch (...) { }

    AM_CPP_PROTOBUF_DECODING_ERROR = 210,                       // A generic Protobuf decoding error
    AM_CPP_PROTOBUF_DECODING_EMPTY_INPUT_BUFFER = 211,          // The contents of the Protobuf data buffer is empty
    AM_CPP_PROTOBUF_DECODING_MISSING_INPUT_PARAMETER = 212,     // The Protobuf data buffer is missing an expected input parameter
    AM_CPP_PROTOBUF_DECODING_FAILED_DESERIALIZE = 213,          // Failed to de-serialize the Protobuf data from its serialized string

    AM_CPP_PROTOBUF_ENCODING_ERROR = 230,                       // A generic Protobuf encoding error
    AM_CPP_PROTOBUF_ENCODING_FAILED_SERIALIZE = 231,            // Failed to encode the the Protobuf data into a serialized string

    /// ERRORS FROM C# (300+)
    AM_DOTNET_DEFAULT_EXCEPTION = 300,                          // "Default exception" caught: catch (...) { }

    AM_DOTNET_PROTOBUF_DECODING_ERROR = 310,                    // A generic Protobuf decoding error
    AM_DOTNET_PROTOBUF_DECODING_EMPTY_INPUT_BUFFER = 311,       // The contents of the Protobuf data buffer is empty
    AM_DOTNET_PROTOBUF_DECODING_MISSING_INPUT_PARAMETER = 312,  // The Protobuf data buffer is missing an expected input parameter
    AM_DOTNET_PROTOBUF_DECODING_FAILED_DESERIALIZE = 313,       // Failed to de-serialize the Protobuf data from its serialized string

    AM_DOTNET_PROTOBUF_ENCODING_ERROR = 330,                    // A generic Protobuf encoding error
    AM_DOTNET_PROTOBUF_ENCODING_FAILED_SERIALIZE = 331,         // Failed to encode the the Protobuf data into a serialized string

    AM_DOTNET_PROTOBUF_NULL_BUFFER = 340,                       // The Protobuf buffer was empty/'null'
    AM_DOTNET_PROTOBUF_COPY_BUFFER_ERROR = 341,                 // Failed to Marshal.Copy() the unmanaged Protobuf buffer to a managed byte array

    /// ERRORS FROM Java (400+)
    AM_JAVA_DEFAULT_EXCEPTION = 400,                            // "Default exception" caught: catch (...) { }

    AM_JAVA_PROTOBUF_DECODING_ERROR = 410,                      // A generic Protobuf decoding error
    AM_JAVA_PROTOBUF_DECODING_EMPTY_INPUT_BUFFER = 411,         // The contents of the Protobuf data buffer is empty
    AM_JAVA_PROTOBUF_DECODING_MISSING_INPUT_PARAMETER = 412,    // The Protobuf data buffer is missing an expected input parameter
    AM_JAVA_PROTOBUF_DECODING_FAILED_DESERIALIZE = 413,         // Failed to de-serialize the Protobuf data from its serialized string

    AM_JAVA_PROTOBUF_ENCODING_ERROR = 430,                      // A generic Protobuf encoding error
    AM_JAVA_PROTOBUF_ENCODING_FAILED_SERIALIZE = 431,           // Failed to encode the the Protobuf data into a serialized string

    /// ERRORS FROM Android-JNI (500+)
    AM_JNI_DEFAULT_EXCEPTION = 500,                             // "Default exception" caught: catch (...) { }
};

#endif // LIB_CALL_RETURN_CODE_H
