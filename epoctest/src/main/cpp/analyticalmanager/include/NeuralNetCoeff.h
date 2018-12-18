#ifndef NEURAL_NET_COEFF_H
#define NEURAL_NET_COEFF_H

#include <memory>
#include <map>      // NOTE: Android NDK does not fully support C++11 unordered_map
#include <vector>

#include "NeuralNetCoeffVector.h"
#include "NeuralNetCoeffNode.h"

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
    namespace AM {
        namespace Dotnet {
            namespace Bge {
#else
namespace Epoc {
    namespace AM {
        namespace Native {
            namespace Bge {
#endif

                class NeuralNetCoeff
                {
                public:
                    std::map<NeuralNetCoeffVector, double> vectorMappingValue;

                private:
                    std::shared_ptr<std::vector<NeuralNetCoeffVector>> vectorDefinition = std::make_shared<std::vector<NeuralNetCoeffVector>>();

                private:
                    std::map<NeuralNetCoeffVector, double> vectorMeans;

                private:
                    std::map<NeuralNetCoeffVector, double> vectorStandardDeviations;

                public:
                    std::shared_ptr<std::vector<std::shared_ptr<NeuralNetCoeffNode>>> nodes = std::make_shared<std::vector<std::shared_ptr<NeuralNetCoeffNode>>>();

                public:
                    std::string versionInfo;            // idx = 0
                public:
                    double intercept;                   // idx = 1
                public:
                    int numberNodes;                    // idx = 2
                public: 
                    int numberVectorsPerNode;           // idx = 3

                public:
                    double concCut;                     // idx = 4
                public:
                    double pivot;                       // idx = 5
                public:
                    double slopeAdjusted;               // idx = 6
                public:
                    double interceptAdjusted;           // idx = 7
                public:
                    double extraWindowStart;            // idx = 8
                public:
                    double extraWindowSize;             // idx = 9

                public:
                    std::string extraString;            // idx = 10

                private:
                    ENUM_DEF(ParsingStatus)
                    {
                        /// NOTICE: The order these enums are defined in shall remain unchanged,
                        /// only their values may be updated.
                        ///
                        /// The order each enum definition dictates the values applied to the enum's
                        /// Java representation, and thus any changes (adding, deleting, re-oerdering)
                        /// of enum elements must be reflected in the Java/JNI and C#/C++(CLI) code
                        /// as well.

                        NOT_STARTED,
                        PARSING_MODEL_HEADER,
                        PARSING_MODEL_VECTOR_DICTIONARY,
                        PARSING_MODEL_VECTOR_MEANS,
                        PARSING_MODEL_VECTOR_STANDARD_DEVIATIONS,
                        PARSING_NODES,
                        COMPLETED
                    };

                public:
                    ENUM_DEF(ParsingCoeffsErrorCode)
                    {
                        /// NOTICE: The order these enums are defined in shall remain unchanged,
                        /// only their values may be updated.
                        ///
                        /// The order each enum definition dictates the values applied to the enum's
                        /// Java representation, and thus any changes (adding, deleting, re-oerdering)
                        /// of enum elements must be reflected in the Java/JNI and C#/C++(CLI) code
                        /// as well.

                        ERROR_UNDEFINED,
                        ERROR_MISSING_COEFFICIENTS,
                        ERROR_PARSING_MODEL_HEADER,
                        ERROR_PARSING_MODEL_VECTOR_DICTIONARY,
                        ERROR_PARSING_MODEL_VECTOR_MEANS,
                        ERROR_PARSING_MODEL_VECTOR_STANDARD_DEVIATIONS,
                        ERROR_PARSING_NODES,
                        SUCCESS
                    };

                private:
                    ParsingCoeffsErrorCode parsingErrorCode = ParsingCoeffsErrorCode::ERROR_UNDEFINED;
                public:
                    ParsingCoeffsErrorCode ParsingErrorCode();

                public:
                    NeuralNetCoeff();

                public:
                    ParsingCoeffsErrorCode ParseRawCoefficients(std::string rawCoeff);

                private:
                    ParsingCoeffsErrorCode GetParsingErrorCode(ParsingStatus parsingStatus);

                public:
                    double Calculate();

                private:
                    std::map<NeuralNetCoeffVector, double> StandardizeInputs();

                private:
                    double Calculate(std::map<NeuralNetCoeffVector, double> &standardizedInputs);

                private:
                    NeuralNetCoeffVector VectorMapping(std::string vectorName);
                };
            }
        }
    }
}

#endif // NEURAL_NET_COEFF_H