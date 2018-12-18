//
// Created by michde on 10/19/2017.
//
#include <sstream>
#include <ctime>

#if !defined __ANDROID__
#include <iostream>
#endif

#include "AMSerializerHelper.h"

///////////////////////////////////////////////////////////////////////////////////////////////////////
// Utility methods to format error string

std::string AMSerializerHelper::formatErrorMessage(const char* errorMessage, const char* moduleName, const int lineNo)
{
    std::stringstream ss;

    time_t now;
    time(&now);

    // Local time
    //char* dt = ctime(&now);

    // UTC time
    tm gmtm;
    char dt[30];

#ifdef __ANDROID__
    gmtime_r(&now, &gmtm);
    asctime_r(&gmtm, dt);
#else
    gmtime_s(&gmtm, &now);
    asctime_s(dt, &gmtm);
#endif

    stripNewLineFromCharArray(dt);

    ss << dt << "-";
    ss << moduleName;
    if ((*moduleName == 0) && lineNo != -1)
    {
        ss << " (Line" << lineNo << ") ";
    }
    ss << errorMessage;

#if !defined __ANDROID__
    // Below is for debugging purpose - to printout to console.
    std::cout << ss.str();
#endif

    return ss.str();
}

void AMSerializerHelper::stripNewLineFromCharArray(char* inChar)
{
    char* cpyChar = inChar;
    while (inChar != 0 && *inChar != '\0')
    {
        if (*inChar == '\n')
        {
            inChar++; // skip "\n" entry
            *cpyChar = *inChar;
        }
        else
        {
            *cpyChar = *inChar;
        }
        cpyChar++;
        inChar++;
    }

    *cpyChar = '\0'; // terminate char string
}

///////////////////////////////////////////////////////////////////////////////////////////////////////
// Utility methods

void AMSerializerHelper::freeMemory(char* byteArrayPtr)
{
    if (byteArrayPtr != nullptr)
    {
        delete[] byteArrayPtr;
        byteArrayPtr = nullptr;
    }
}
