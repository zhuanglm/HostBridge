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
// File: CooxModel.h
// Original C# Author: Nick Donais
// Ported C++ Author: Dean Michaud
// Original C# Date Created: 26-Sep-2016
// Ported C++ Date Created: 12-Oct-2017
// Description: The CooxModel class
// 
// ============================================================================
#ifndef EPOC_COOX_COOXMODEL_H
#define EPOC_COOX_COOXMODEL_H
// NOTE: Cannot use nested namespace declarations (e.g. Epoc::AM::Native::Bge); nested namespace declarations is a C++17 feature.
//       (C++17 is not officially supported by clang++ for Android native C++ builds at this time)

#include <vector>

namespace Epoc {
    namespace AM {
        namespace Native {
            namespace Coox {
                class CooxModel
                {
                public:
                    static const int NumResults = 5;
                    static const int WavelengthColumn = 1;

                private:
                    CooxModel() {}

                public:
                    CooxModel(int min, int max, int start, int end)
                    {
                        PixelMin = min;
                        PixelMax = max;
                        CoeffStart = start;
                        CoeffEnd = end;
                    }

                public:
                    int PixelMin;
                    int PixelMax;
                    int CoeffStart;
                    int CoeffEnd;

                public:
                    std::vector<float> Coefficients[NumResults];

                public:
                    // float array usage: float* wlengths = Wavelengths();  float wl = wlengths[idx];
                    // NOTE: Caller is responsible for memory cleanup
                    float* Wavelengths()
                    {
                        auto ret = new float[NumResults];
                        for (int y = 0; y < NumResults; y++) {
                            ret[y] = Coefficients[y][WavelengthColumn];
                        }

                        return ret;
                    }
                };
            }
        }
    }
}
#endif  // EPOC_COOX_COOXMODEL_H
