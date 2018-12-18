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
// File: HgbResult.h
// Original C# Author: Nick Donais
// Ported C++ Author: Dean Michaud
// Original C# Date Created: 26-Sep-2016
// Ported C++ Date Created: 12-Oct-2017
// Description: The HgbResult enumerated type
// 
// ============================================================================
#ifndef EPOC_COOX_HGBRESULT_H
#define EPOC_COOX_HGBRESULT_H
// Define enums based on the project-type that includes this header file
#ifdef _MANAGED
#define ENUM_DEF(ENUM_NAME) public enum class ENUM_NAME     // Managed C++/CLI
#else
#define ENUM_DEF(ENUM_NAME) enum class ENUM_NAME : int          // C++11
#endif


// NOTE: Cannot use nested namespace declarations (e.g. Epoc::AM::Native::Bge); nested namespace declarations is a C++17 feature.
//       (C++17 is not officially supported by clang++ for Android native C++ builds at this time)
#ifdef _MANAGED
namespace Epoc {
    namespace Common {
        namespace Dotnet {
            namespace Definitions {
#else
namespace Epoc {
    namespace Common {
        namespace Native {
            namespace Definitions {
#endif
                ENUM_DEF(HgbResult)
                {
                    /// NOTICE: The order these enums are defined in shall remain unchanged,
                    /// only their values may be updated.
                    ///
                    /// The order each enum definition dictates the values applied to the enum's
                    /// Java representation, and thus any changes (adding, deleting, re-oerdering)
                    /// of enum elements must be reflected in the Java/JNI and C#/C++(CLI) code
                    /// as well.

                    O2Hb = 1,
                    COHb = 2,
                    HHb = 3,
                    MetHb = 4,
                    tHb
                };
            }
        }
    }
}
#endif // EPOC_COOX_HGBRESULT_H
