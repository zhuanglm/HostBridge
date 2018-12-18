#include <cmath>
#include <iomanip>
#include <sstream>

#include <stdlib.h>

#include "MathHelp.h"

#include <fenv.h>

using namespace Epoc::AM::Native::Bge;

MathHelp::MathHelp()
{
}

MathHelp::~MathHelp()
{
}

double MathHelp::Round(const double value)
{
    return Round(value, 0);
}

double MathHelp::Round(const double value, const unsigned int fractionalDigits)
{
    double rounded = 0.0;

    if (fractionalDigits > 15)
        throw std::out_of_range("Simulating C# Math.Round() argument out of range: factionalDigits is larger than 15.");

    if (!isfinite(value))
        throw std::out_of_range("Simulating C# Math.Round() argument out of range: value not a finite value.");

    if (fractionalDigits == 0)
    {
        // NOTE: DO NOT USE "round()" - it always rounds up
        int mode = fegetround();            // Remember the current mode
        fesetround(FE_TONEAREST);           // This mimics C# Math.Round()'s round to nearest even
        rounded = std::nearbyint(value);
        fesetround(mode);                   // Set the mode back to whatever it was before
    }
    else
    {
        // Try: Round(value * 10^x) * 10^-x
        double tmp = value * std::pow(10, fractionalDigits);

        if (std::isfinite(tmp))
        {
            rounded = Round(tmp) * std::pow(10, -1.0 * fractionalDigits);
        }
        else
        {
            // We can't multiply 'value' by 10^x, it overflows. We'll use this as a fallback for this scenario.
            // NOTE: This fallback should not be called often.
            std::stringstream ss;
            ss << std::setprecision(fractionalDigits) << std::fixed << value;
            std::string roundedStr = ss.str();
            rounded = strtod(roundedStr.c_str(), NULL);
        }
    }

    if (!isfinite(rounded))
        throw std::overflow_error("Simulating C# Math.Round() result overflow: result is not a finite value.");

    return rounded;
}
