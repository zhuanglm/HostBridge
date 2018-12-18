#include <cmath>
#include <locale>
#include <math.h>

#include "NeuralNetCoeffNode.h"

#include "StringHelp.h"

using namespace Epoc::AM::Native::Bge;
double NeuralNetCoeffNode::Weight()
{
    return nodeWeight;
}

NeuralNetCoeffNode::NeuralNetCoeffNode(std::shared_ptr<std::vector<std::string>> rawCoeff, std::shared_ptr<std::vector<NeuralNetCoeffVector>> theVectorDef)
{
    vectorDef = theVectorDef;

    ParseRawCoefficients(rawCoeff);
}

NeuralNetCoeffNode::NeuralNetCoeffNode(double nodeWeight, std::string functionName, double intercept, std::shared_ptr<std::vector <NeuralNetCoeffVector>> theVectorDef)
{
    this->nodeWeight = nodeWeight;
    this->functionName = functionName;
    this->intercept = intercept;
    this->vectorDef = theVectorDef;
}

void NeuralNetCoeffNode::ParseRawCoefficients(std::shared_ptr<std::vector<std::string>> rawCoeff)
{
    if (rawCoeff != nullptr)
    {
        // Step 1 - get the node's coefficient header data
        int parsedIdx = 0;
        nodeWeight = StringHelp::ToDouble(rawCoeff->at(parsedIdx++));
        functionName = rawCoeff->at(parsedIdx++);
        intercept = StringHelp::ToDouble(rawCoeff->at(parsedIdx++));
        nodeExtraString = rawCoeff->at(parsedIdx++);

        // Step 2 - Parse node coefficient array to get all defined node data
        for (auto nnCoeffVector : *vectorDef)
        {
            double val = 0;
            try
            {
                val = StringHelp::ToDouble(rawCoeff->at(parsedIdx++));
            }
            catch (...)
            {
                throw;
            }
            vectorValue.insert({ nnCoeffVector, val });
        }
    }
}

double NeuralNetCoeffNode::Calculate(std::map<NeuralNetCoeffVector, double> &standardizedInputs)
{
    double nodeOutput = 0.0;

    // Step 2a - Calculate the output for each Node
    //               nodeScalar = NodeIntercept + {NodeValue1, NodeValue2�NodeValueV} x {Standardized_input1, Standardized_input2... Standardized_inputV}
    //               nodeOutput = TanH (0.5 x nodeScalar)

    try
    {
         //Sub-step 1 - nodeScalar = NodeIntercept + Sum(Node1Value1 x Standardized_input1, ..., Node1ValueV x Standardized_inputV)
        double nodeScalar = intercept;

        for (auto standardizedInput : standardizedInputs)
        {
            try
            {
                double nodeValue = vectorValue.at(standardizedInput.first);
                nodeScalar += nodeValue * standardizedInput.second;
            }
            catch (...)
            {
                // This vector is not defined in this node
            }
        }

        // Sub-step 2 - Execute the node's specified function:
        //                e.g. nodeOutput = TanH (0.5 x nodeScalar)
        nodeOutput = ExecuteFunction(functionName, 0.5 * nodeScalar);
    }
    catch (...)
    {
        nodeOutput = NAN;
    }

    return nodeOutput;
}

double NeuralNetCoeffNode::ExecuteFunction(std::string functionName, double argument)
{
    if (StringHelp::Trim(StringHelp::ToLower(functionName)).compare("tanh") == 0)
        return CalculateHyperbolicTangent(argument);

    return argument;
}

double NeuralNetCoeffNode::CalculateHyperbolicTangent(double x)
{
    //sinh(x) = ( e(x) - e(-x) )/2
    //cosh(x) = ( e(x) + e(-x) )/2
    //tanh(x) = sinh(x)/cosh(x) = ( e(x) - e(-x) )/( e(x) + e(-x) )
    return (std::exp(x) - std::exp(-x)) / (std::exp(x) + std::exp(-x));
}
