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
// File: ResultsCalculator.cpp
// Original C# Author: Nick Donais
// Ported C++ Author: Dean Michaud
// Original C# Date Created: 26-Sep-2016
// Ported C++ Date Created: 12-Oct-2017
// Description: The ResultsCalculator class
// 
// ============================================================================
#include <cmath>

#include "CooxModel.h"
#include "HgbResult.h"
#include "ResultsCalculator.h"

namespace Epoc {
    namespace AM {
        namespace Native {
            namespace Coox {
                const HgbResult ResultsCalculator::InUseHgbResults[] = {
                    HgbResult::O2Hb, HgbResult::COHb, HgbResult::HHb, HgbResult::MetHb
                };

                void ResultsCalculator::Version(int& major, int& minor, int& micro)
                {
                    major = versionMajor;
                    minor = versionMinor;
                    micro = versionMicro;
                }

                std::map<HgbResult, float> ResultsCalculator::CalculateResults(IN CooxModel model,
                                                                               IN float opticalPathLength,
                                                                               IN std::vector<Point> reference,
                                                                               IN std::vector<Point> sample,
                                                                               IN bool allowNegativeValues)
                {
                    std::map<HgbResult, float> results = {
                        { HgbResult::O2Hb, 0.0f },
                        { HgbResult::COHb, 0.0f },
                        { HgbResult::HHb, 0.0f },
                        { HgbResult::MetHb, 0.0f },
                        { HgbResult::tHb, 0.0f }
                    };

                    for (int i = model.CoeffStart; i <= model.CoeffEnd; i++) {
                        float yRef = reference[i + model.PixelMin - 1].Y;
                        float ySample = sample[i + model.PixelMin - 1].Y;

                        for (int idx = 0; idx < array_size(InUseHgbResults); idx++) {
                            HgbResult hgb = InUseHgbResults[idx];
                            float coeff = model.Coefficients[(int)hgb][i];
                            results[hgb] += HgbCalc(opticalPathLength, coeff, yRef, ySample);
                        }
                    }

                    if (!allowNegativeValues)
                        SetNegativeValuesToZero(results);

                    results[HgbResult::tHb] = SumHgbResults(results);

                    return results;
                }

                void ResultsCalculator::SetNegativeValuesToZero(std::map<HgbResult, float>& results)
                {
                    for (int idx = 0; idx < array_size(InUseHgbResults); idx++)
                    {
                        HgbResult hgb = InUseHgbResults[idx];

                        if (results[hgb] < 0.0f)
                            results[hgb] = 0.0f;
                    }
                }

                float ResultsCalculator::HgbCalc(float opticalPathLength, float coeff, float yRef, float ySample)
                {
                    float io = (yRef == 0.0f) ? 0.0f : ySample / yRef;
                    return (float)(coeff * (-log10(io) * 72.25f / opticalPathLength));
                }

                float ResultsCalculator::SumHgbResults(std::map<HgbResult, float>& results)
                {
                    float summedResult = 0.0f;
                    for (auto const &kv : results) {
                        if (kv.first != HgbResult::tHb)
                            summedResult += kv.second;
                    }

                    return summedResult;
                }
            }
        }
    }
}
