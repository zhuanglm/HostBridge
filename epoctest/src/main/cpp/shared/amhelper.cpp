#include "amhelper.h"

void as_unsigned_char_array(JNIEnv *env, jbyteArray inputjbyteArray, unsigned char* outputCharArray) {
    int len = env->GetArrayLength (inputjbyteArray);

    // GetByteArrayRegion() does not require a release call to be made, whereas GetByteArrayElements() requires a call to ReleaseByteArrayElements().
    env->GetByteArrayRegion(inputjbyteArray, 0, len, reinterpret_cast<jbyte*>(outputCharArray));
}

int GetNumericCharCount(int value)
{
    int charCount = 1;  // There's always at least '1' character

    while(value > 9)
    {
        value /= 10;
        ++charCount;
    }

    return charCount;
}

int GetVersionNumberCharCount(const int major, const int minor, const int micro)
{
    // +2 => the dots in the version string: e.g. '1.2.3'
    // +1 for the string's null-terminator
    int charCount = 2 + 1;

    charCount += GetNumericCharCount(major);
    charCount += GetNumericCharCount(minor);
    charCount += GetNumericCharCount(micro);

    return charCount;
}
