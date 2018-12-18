// ============================================================================
// 
// Copyright (c) 2017 by Epocal.
// 
// All rights reserved.
// 
// THIS SOURCE CODE CONTAINS CONFIDENTIAL INFORMATION THAT IS OWNED BY EPOCAL,
// AND MAY NOT BE COPIED, DISCLOSED OR OTHERWISE USED WITHOUT
// THE EXPRESS WRITTEN CONSENT OF EPOCAL.
// 
// File: ResultsCalculator.h
// Original C# Author: Nick Donais
// Ported C++ Author: Dean Michaud
// Original C# Date Created: 26-Sep-2016
// Ported C++ Date Created: 12-Oct-2017
// Description: The ResultsCalculator class
// 
// ============================================================================
#ifndef EPOC_COOX_EXTENSIONS_H
#define EPOC_COOX_EXTENSIONS_H

#include <map>
#include <vector>

#include "CooxModel.h"
#include "HgbResult.h"

#ifndef IN_OUT
#define IN_OUT
#endif

#ifndef IN
#define IN
#endif

#ifndef OUT
#define OUT
#endif

using namespace Epoc::Common::Native::Definitions;

// NOTE: Cannot use nested namespace declarations (e.g. Epoc::AM::Native::Bge); nested namespace declarations is a C++17 feature.
//       (C++17 is not officially supported by clang++ for Android native C++ builds at this time)
namespace Epoc {
    namespace AM {
        namespace Native {
            namespace Coox {
                class ResultsCalculator
                {
                private:
                    // Version      Change Log
                    // 1.0.0        First version of the Native C++ Coox AM
                    // 1.0.1        Added allowNegativeValues flag to CalculateResults()
                    //                  - C++/C#:   ResultsCalculator.CalculateResults() includes the flag as a parameter
                    //                  - Java:     AnalyticalManager.calculateAMCooxResults() does not include the flag as a parameter;
                    //                                  the flag is forced to 'false' to ensure the Host does not allow negative values
                    static const int versionMajor = 1;
                    static const int versionMinor = 0;
                    static const int versionMicro = 1;

                public:
                    static void Version(int& major, int& minor, int& micro);

                private:
                    static const HgbResult InUseHgbResults[4];

                public:
                    struct Point
                    {
                        float X;
                        float Y;
                    };

                public:
                    static std::map<HgbResult, float> CalculateResults(IN CooxModel model,
                                                                       IN float opticalPathLength,
                                                                       IN std::vector<Point> reference,
                                                                       IN std::vector<Point> sample,
                                                                       IN bool allowNegativeValues);

                private:
                    static void SetNegativeValuesToZero(std::map<HgbResult, float>& results);

                    static float HgbCalc(float opticalPathLength, float coeff, float yRef, float ySample);

                    static float SumHgbResults(std::map<HgbResult, float>& results);

                    template<size_t SIZE, typename T>
                    inline static size_t array_size(T(&arr)[SIZE]) {
                        return SIZE;
                    }
                };
            }
        }
    }
}
#endif