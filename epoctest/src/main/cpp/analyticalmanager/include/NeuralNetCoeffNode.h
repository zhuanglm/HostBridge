#ifndef NEURAL_NET_COEFF_NODE_H
#define NEURAL_NET_COEFF_NODE_H

#include <memory>
#include <map>      // NOTE: Android NDK does not fully support C++11 unordered_map
#include <vector>

#include "NeuralNetCoeffVector.h"

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

                class NeuralNetCoeffNode
                {
                private:
                    std::map<NeuralNetCoeffVector, double> vectorValue;
                private:
                    std::string nodeExtraString;
                private:
                    std::string functionName;
                private:
                    double nodeWeight;
                private:
                    double intercept;

                public:
                    double Weight();

                public:
                    // 1- Extra String
                    // 2- Function Name
                    // 3- Weight
                    // 4- Intercept
                    static const int HEADER_COEFFICIENTS_COUNT = 4;

                public:
                    std::shared_ptr<std::vector<NeuralNetCoeffVector>> vectorDef = nullptr;

                public:
                    NeuralNetCoeffNode(std::shared_ptr<std::vector<std::string>> rawCoeff, std::shared_ptr<std::vector<NeuralNetCoeffVector>> theVectorDef);

                public:
                    NeuralNetCoeffNode(double nodeWeight, std::string functionName, double intercept, std::shared_ptr<std::vector <NeuralNetCoeffVector>> theVectorDef);
                private:
                    void ParseRawCoefficients(std::shared_ptr<std::vector<std::string>> rawCoeff);

                public:
                    double Calculate(std::map<NeuralNetCoeffVector, double> &standardizedInputs);

                private:
                    double ExecuteFunction(std::string functionName, double argument);

                private:
                    double CalculateHyperbolicTangent(double x);
                };
            }
        }
    }
}

#endif // NEURAL_NET_COEFF_NODE_H