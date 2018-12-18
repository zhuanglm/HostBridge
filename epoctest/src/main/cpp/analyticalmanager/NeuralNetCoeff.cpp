#include <locale>
#include <math.h>

#include "NeuralNetCoeff.h"

#include "StringHelp.h"

using namespace Epoc::AM::Native::Bge;

NeuralNetCoeff::ParsingCoeffsErrorCode NeuralNetCoeff::ParsingErrorCode()
{
    return parsingErrorCode;
}

NeuralNetCoeff::NeuralNetCoeff()
{
}

NeuralNetCoeff::ParsingCoeffsErrorCode NeuralNetCoeff::ParseRawCoefficients(std::string rawCoeff)
{
    ParsingStatus parsingStatus = ParsingStatus::NOT_STARTED;

    try
    {
        if (rawCoeff.empty())
        {
            // No input data was provided
            parsingErrorCode = ParsingCoeffsErrorCode::ERROR_MISSING_COEFFICIENTS;
        }
        else
        {
            std::vector<std::string> coeffArray = StringHelp::Split(rawCoeff, ",");

            // Step 1 - get the coefficient header data
            parsingStatus = ParsingStatus::PARSING_MODEL_HEADER;

            int parsedIdx = 0;

            versionInfo = coeffArray[parsedIdx++];
            intercept = StringHelp::ToDouble(coeffArray[parsedIdx++]);
            numberNodes = StringHelp::ToInt(coeffArray[parsedIdx++]);
            numberVectorsPerNode = StringHelp::ToInt(coeffArray[parsedIdx++]);

            concCut = StringHelp::ToDouble(coeffArray[parsedIdx++]);
            pivot = StringHelp::ToDouble(coeffArray[parsedIdx++]);
            slopeAdjusted = StringHelp::ToDouble(coeffArray[parsedIdx++]);
            interceptAdjusted = StringHelp::ToDouble(coeffArray[parsedIdx++]);
            extraWindowStart = StringHelp::ToDouble(coeffArray[parsedIdx++]);
            extraWindowSize = StringHelp::ToDouble(coeffArray[parsedIdx++]);

            extraString = coeffArray[parsedIdx++];

            // Step 2 - Parse coefficient array to get all defined vector dictionary values
            for (int i = 0; i < numberVectorsPerNode; i++)
            {
                parsingStatus = ParsingStatus::PARSING_MODEL_VECTOR_DICTIONARY;
                NeuralNetCoeffVector vector = VectorMapping(coeffArray[parsedIdx++]);
                if (vector == NeuralNetCoeffVector::Unknown)
                {
                    throw;
                }
                vectorDefinition->push_back(vector);
            }

            // Step 3 - Parse the model's vector means
            for (int i = 0; i < numberVectorsPerNode; i++)
            {
                parsingStatus = ParsingStatus::PARSING_MODEL_VECTOR_MEANS;
                double vectorMean = StringHelp::ToDouble(coeffArray[parsedIdx++]);
                vectorMeans.insert({ vectorDefinition->at(i), vectorMean });
            }

            // Step 4 - Parse the model's vector standard deviations
            for (int i = 0; i < numberVectorsPerNode; i++)
            {
                parsingStatus = ParsingStatus::PARSING_MODEL_VECTOR_STANDARD_DEVIATIONS;
                double vectorSD = StringHelp::ToDouble(coeffArray[parsedIdx++]);
                vectorStandardDeviations.insert({ vectorDefinition->at(i), vectorSD });
            }

            // Step 5 - Parse all node coefficient arrays to get all of the defined nodes' data

            // Each node coefficients array holds the following data:
            //      1 weight
            //      2 function (e.g. tanh)
            //      3 intercept
            //      4 extra (string for future usage)
            //      5 value1 .. valueN
            //
            int nodeCoeffCount = numberVectorsPerNode + NeuralNetCoeffNode::HEADER_COEFFICIENTS_COUNT;
            for (int i = 0; i < numberNodes; i++)
            {
                parsingStatus = ParsingStatus::PARSING_NODES;
                std::shared_ptr<std::vector<std::string>> nodeCoeff = std::make_shared<std::vector<std::string>>(coeffArray.begin() + parsedIdx, coeffArray.begin() + parsedIdx + nodeCoeffCount);

                std::shared_ptr<NeuralNetCoeffNode> node = std::make_shared<NeuralNetCoeffNode>(nodeCoeff, vectorDefinition);
                nodes->push_back(node);

                // Update to point to the next node's start index in coeffArray
                parsedIdx += nodeCoeffCount;
            }

            parsingStatus = ParsingStatus::COMPLETED;
            parsingErrorCode = ParsingCoeffsErrorCode::SUCCESS;
        }
    }
    catch (...)
    {
        // grave parsing error occured, return an error code based on the last known parsing status
        parsingErrorCode = GetParsingErrorCode(parsingStatus);
    }

    return parsingErrorCode;
}

NeuralNetCoeff::ParsingCoeffsErrorCode NeuralNetCoeff::GetParsingErrorCode(ParsingStatus parsingStatus)
{
    ParsingCoeffsErrorCode errCode = ParsingCoeffsErrorCode::ERROR_UNDEFINED;

    switch (parsingStatus)
    {
    case ParsingStatus::PARSING_MODEL_HEADER:
        errCode = ParsingCoeffsErrorCode::ERROR_PARSING_MODEL_HEADER;
        break;

    case ParsingStatus::PARSING_MODEL_VECTOR_DICTIONARY:
        errCode = ParsingCoeffsErrorCode::ERROR_PARSING_MODEL_VECTOR_DICTIONARY;
        break;

    case ParsingStatus::PARSING_MODEL_VECTOR_MEANS:
        errCode = ParsingCoeffsErrorCode::ERROR_PARSING_MODEL_VECTOR_MEANS;
        break;

    case ParsingStatus::PARSING_MODEL_VECTOR_STANDARD_DEVIATIONS:
        errCode = ParsingCoeffsErrorCode::ERROR_PARSING_MODEL_VECTOR_STANDARD_DEVIATIONS;
        break;

    case ParsingStatus::PARSING_NODES:
        errCode = ParsingCoeffsErrorCode::ERROR_PARSING_NODES;
        break;

    default:
        errCode = ParsingCoeffsErrorCode::ERROR_UNDEFINED;
        break;
    }

    return errCode;
}
double NeuralNetCoeff::Calculate()
{
    double concAdjusted = NAN;

    if (parsingErrorCode == ParsingCoeffsErrorCode::SUCCESS)
    {
        // Step 1 - Standardize the inputs
        std::map<NeuralNetCoeffVector, double> standardizedInputs = StandardizeInputs();

        // Step 2 - Apply Model (Mixed or Blood)
        concAdjusted = Calculate(standardizedInputs);
    }

    return concAdjusted;
}

std::map<NeuralNetCoeffVector, double> NeuralNetCoeff::StandardizeInputs()
{
    std::map<NeuralNetCoeffVector, double> standardizedInput;

    // Step 1 - Standardize the inputs
    //      1a - For each input in Vector:
    //      Standardized_input1 = (input1 ... Mean_input1) / StandardDeviation_input1,
    //      Standardized_input2 = (input2 ... Mean_input2) / StandardDeviation_input2,
    //      ...
    //      Standardized_inputV = (inputV ... Mean_inputV) / StandardDeviation_inputV,
    for (std::pair<NeuralNetCoeffVector, double> input : vectorMappingValue)
    {
        try
        {
            double standardizedValue = (input.second - vectorMeans.at(input.first)) / vectorStandardDeviations.at(input.first);
            standardizedInput.insert({ input.first, standardizedValue });
        }
        catch (...)
        {
            // This entry is not in the model's dictionary... do nothing / skip it
        }
    }

    return standardizedInput;
}

double NeuralNetCoeff::Calculate(std::map<NeuralNetCoeffVector, double> &standardizedInputs)
{
    // Step 2a - Calculate the output for each Node
    //              nodeScalar = NodeIntercept + Sum(Node1Value1 x Standardized_input1, ..., Node1ValueV x Standardized_inputV)
    //              nodeOutput = TanH (0.5 x nodeScalar)
    std::vector<double> nodeOutputList;
    for (size_t i = 0; i < nodes->size(); i++)
    {
        double nodeOutput = nodes->at(i)->Calculate(standardizedInputs);

        if (isnan(nodeOutput))
            return NAN;

        nodeOutputList.push_back(nodeOutput);
    }

    // Step 2b - ConcCrea = ModelIntercept + Sum(node1Output x node1Weight, ..., nodeNOutput x nodeNWeight)
    double concCrea = intercept;
    for (int i = 0; i < nodeOutputList.size(); i++)
    {
        concCrea += nodeOutputList[i] * nodes->at(i)->Weight();
    }

    // Step 2c - Apply slope and offset:
    //           ConcAdjusted = (ConcCrea ... Pivot) x SlopeAdjusted + InterceptAdjusted + Pivot
    double concAdjusted = ((concCrea - pivot) * slopeAdjusted) + interceptAdjusted + pivot;

    return concAdjusted;
}

NeuralNetCoeffVector NeuralNetCoeff::VectorMapping(std::string vectorName)
{
    std::string vectorNameLowerCase = StringHelp::ToLower(vectorName);

    if (vectorNameLowerCase.compare("calmean") == 0)
        return NeuralNetCoeffVector::CalMean;
    if (vectorNameLowerCase.compare("caldrift") == 0)
        return NeuralNetCoeffVector::CalDrift;
    if (vectorNameLowerCase.compare("calsecond") == 0)
        return NeuralNetCoeffVector::CalSecond;
    if (vectorNameLowerCase.compare("calnoise") == 0)
        return NeuralNetCoeffVector::CalNoise;
    if (vectorNameLowerCase.compare("calextrap") == 0)
        return NeuralNetCoeffVector::CalExtrap;

    if (vectorNameLowerCase.compare("samplemean") == 0)
        return NeuralNetCoeffVector::SampleMean;
    if (vectorNameLowerCase.compare("sampledrift") == 0)
        return NeuralNetCoeffVector::SampleDrift;
    if (vectorNameLowerCase.compare("samplesecond") == 0)
        return NeuralNetCoeffVector::SampleSecond;
    if (vectorNameLowerCase.compare("samplenoise") == 0)
        return NeuralNetCoeffVector::SampleNoise;

    if (vectorNameLowerCase.compare("postmean") == 0)
        return NeuralNetCoeffVector::PostMean;
    if (vectorNameLowerCase.compare("postdrift") == 0)
        return NeuralNetCoeffVector::PostDrift;
    if (vectorNameLowerCase.compare("postsecond") == 0)
        return NeuralNetCoeffVector::PostSecond;
    if (vectorNameLowerCase.compare("postnoise") == 0)
        return NeuralNetCoeffVector::PostNoise;

    if (vectorNameLowerCase.compare("additionalmean") == 0)
        return NeuralNetCoeffVector::AdditionalMean;
    if (vectorNameLowerCase.compare("additionaldrift") == 0)
        return NeuralNetCoeffVector::AdditionalDrift;

    if (vectorNameLowerCase.compare("extramean") == 0)
        return NeuralNetCoeffVector::ExtraMean;
    if (vectorNameLowerCase.compare("extradrift") == 0)
        return NeuralNetCoeffVector::ExtraDrift;
    if (vectorNameLowerCase.compare("extrasecond") == 0)
        return NeuralNetCoeffVector::ExtraSecond;
    if (vectorNameLowerCase.compare("extranoise") == 0)
        return NeuralNetCoeffVector::ExtraNoise;

    if (vectorNameLowerCase.compare("sampledetect") == 0)
        return NeuralNetCoeffVector::SampleDetect;
    if (vectorNameLowerCase.compare("bubbledetect") == 0)
        return NeuralNetCoeffVector::BubbleDetect;
    if (vectorNameLowerCase.compare("ageofcard") == 0)
        return NeuralNetCoeffVector::AgeOfCard;
    if (vectorNameLowerCase.compare("topheaterpower") == 0)
        return NeuralNetCoeffVector::TopHeaterPower;
    if (vectorNameLowerCase.compare("hematocritconc") == 0)
        return NeuralNetCoeffVector::HematocritConc;
    if (vectorNameLowerCase.compare("oxygenconc") == 0)
        return NeuralNetCoeffVector::OxygenConc;
    if (vectorNameLowerCase.compare("bicarbconc") == 0)
        return NeuralNetCoeffVector::BicarbConc;

    return NeuralNetCoeffVector::Unknown;
}
