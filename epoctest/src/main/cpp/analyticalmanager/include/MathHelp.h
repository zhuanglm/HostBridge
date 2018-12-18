#ifndef EPOC_MATHHELP_H
#define EPOC_MATHHELP_H

namespace Epoc {
    namespace AM {
        namespace Native {
            namespace Bge {

                class MathHelp {
                private:
                    MathHelp();

                    ~MathHelp();

                public:
                    static double Round(const double value);

                    static double Round(const double value, const unsigned int fractionalDigits);
                };

            }
        }
    }
}

#endif //EPOC_MATHHELP_H
