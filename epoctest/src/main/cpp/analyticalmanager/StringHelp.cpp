#include <algorithm>
#include <locale>

#include "StringHelp.h"

using namespace Epoc::AM::Native::Bge;

StringHelp::StringHelp()
{
}

StringHelp::~StringHelp()
{
}

int StringHelp::ToInt(const std::string& val)
{
    // NOTE: Android NDK does not support std::stoi() from C++11
    return atoi(val.c_str());
}

double StringHelp::ToDouble(const std::string& val)
{
    // NOTE: Android NDK does not support std::stod() from C++11
    return strtod(val.c_str(), NULL);
}

std::string StringHelp::Trim(const std::string& str, const std::string& whitespace)
{
    const auto strBegin = str.find_first_not_of(whitespace);
    if (strBegin == std::string::npos)
        return "";

    const auto strEnd = str.find_last_not_of(whitespace);

    const auto strRange = (strEnd - strBegin) + 1;

    return str.substr(strBegin, strRange);
}

std::string StringHelp::ToLower(std::string str)
{
    std::locale loc;
    std::string newStr;
    for (auto currChar : str)
    {
        newStr += std::tolower(currChar, loc);
    }

    return newStr;
}

std::vector<std::string> StringHelp::Split(const std::string& str, const std::string& delimiter)
{
    size_t pos = 0;
    std::string token;

    std::string tmpStr = str;
    std::vector<std::string> tokens;

    while ((pos = tmpStr.find(delimiter)) != std::string::npos)
    {
        token = tmpStr.substr(0, pos);

        tokens.push_back(token);

        tmpStr.erase(0, pos + delimiter.length());
    }

    tokens.push_back(tmpStr);

    return tokens;
}
