#include <jni.h>

// Function calls shared by analyticalmanager-lib.cpp and analyticalmanager_coox-lib.cpp

void as_unsigned_char_array(JNIEnv *env, jbyteArray inputjbyteArray, unsigned char* outputCharArray);

int GetNumericCharCount(int value);

int GetVersionNumberCharCount(const int major, const int minor, const int micro);
