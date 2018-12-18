#ifndef EPOC_STRINGHELP_H
#define EPOC_STRINGHELP_H

#include <vector>

namespace Epoc {
    namespace AM {
        namespace Native {
            namespace Bge {

                class StringHelp {
                private:
                    StringHelp();
                    ~StringHelp();

                public:
                    static int ToInt(const std::string& val);
                    static double ToDouble(const std::string& val);

                    static std::string Trim(const std::string& str, const std::string& whitespace = " \t");
                    static std::string ToLower(std::string str);
                    static std::vector<std::string> Split(const std::string& str, const std::string& delimiter);
                };

            }
        }
    }
}

#endif //EPOC_STRINGHELP_H
