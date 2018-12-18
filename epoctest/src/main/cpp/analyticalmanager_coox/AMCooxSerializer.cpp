//
// Created by michde on 10/16/2017.
//
#include <stdexcept>

#include "AMCooxSerializer.h"
#include "AMSerializerHelper.h"

///////////////////////////////////////////////////////////////////////////////////////////////////////
// Utility methods to convert between Transfer Objects and C++ AM Coox types

void AMCooxSerializer::convert(const CooxModel& cooxModel, to::CooxModel& cooxModelTO) {
    cooxModelTO.set_pixelmin(cooxModel.PixelMin);
    cooxModelTO.set_pixelmax(cooxModel.PixelMax);
    cooxModelTO.set_coeffstart(cooxModel.CoeffStart);
    cooxModelTO.set_coeffend(cooxModel.CoeffEnd);

    // The 'Coefficients' are stored in 5 separate float arrays; remember this when unpacking the transfer object.
    for (int resultIdx = 0; resultIdx < cooxModel.NumResults; resultIdx++)
    {
        int totalPixels = cooxModel.Coefficients[resultIdx].size();
        for (int pixelIdx = 0; pixelIdx < totalPixels; pixelIdx++)
        {
            switch (resultIdx)
            {
            case 0:
                cooxModelTO.add_coefficients0(cooxModel.Coefficients[resultIdx][pixelIdx]);
                break;

            case 1:
                cooxModelTO.add_coefficients1(cooxModel.Coefficients[resultIdx][pixelIdx]);
                break;

            case 2:
                cooxModelTO.add_coefficients2(cooxModel.Coefficients[resultIdx][pixelIdx]);
                break;

            case 3:
                cooxModelTO.add_coefficients3(cooxModel.Coefficients[resultIdx][pixelIdx]);
                break;

            case 4:
                cooxModelTO.add_coefficients4(cooxModel.Coefficients[resultIdx][pixelIdx]);
                break;

            default:
                throw std::out_of_range("cooxModel.Coefficients index is out of range.");
                break;
            }
        }
    }
}

void AMCooxSerializer::convert(const to::CooxModel& cooxModelTO, CooxModel& cooxModel) {
    cooxModel.PixelMin = cooxModelTO.pixelmin();
    cooxModel.PixelMax = cooxModelTO.pixelmax();
    cooxModel.CoeffStart = cooxModelTO.coeffstart();
    cooxModel.CoeffEnd = cooxModelTO.coeffend();

    // The 'Coefficients' are stored in the transfer object as 5 separate float arrays:

    cooxModel.Coefficients[0].reserve(cooxModelTO.coefficients0_size());
    cooxModel.Coefficients[1].reserve(cooxModelTO.coefficients1_size());
    cooxModel.Coefficients[2].reserve(cooxModelTO.coefficients2_size());
    cooxModel.Coefficients[3].reserve(cooxModelTO.coefficients3_size());
    cooxModel.Coefficients[4].reserve(cooxModelTO.coefficients4_size());

    for (int idx = 0; idx < cooxModelTO.coefficients0_size(); idx++)
        cooxModel.Coefficients[0].push_back(cooxModelTO.coefficients0(idx));

    for (int idx = 0; idx < cooxModelTO.coefficients0_size(); idx++)
        cooxModel.Coefficients[1].push_back(cooxModelTO.coefficients1(idx));

    for (int idx = 0; idx < cooxModelTO.coefficients0_size(); idx++)
        cooxModel.Coefficients[2].push_back(cooxModelTO.coefficients2(idx));

    for (int idx = 0; idx < cooxModelTO.coefficients0_size(); idx++)
        cooxModel.Coefficients[3].push_back(cooxModelTO.coefficients3(idx));

    for (int idx = 0; idx < cooxModelTO.coefficients0_size(); idx++)
        cooxModel.Coefficients[4].push_back(cooxModelTO.coefficients4(idx));
}

void AMCooxSerializer::convert(const ResultsCalculator::Point& point, to::Point& pointTO) {
    pointTO.set_x(point.X);
    pointTO.set_y(point.Y);
}

void AMCooxSerializer::convert(const to::Point& pointTO, ResultsCalculator::Point& point) {
    point.X = pointTO.x();
    point.Y = pointTO.y();
}

void AMCooxSerializer::convert(const HgbResult& hgbResultType, const float& hgbResultValue, to::CalculatedHgbResult& hgbResultTO) {
    hgbResultTO.set_hgbresulttype((int)hgbResultType);
    hgbResultTO.set_resultvalue(hgbResultValue);
}

void AMCooxSerializer::convert(const to::CalculatedHgbResult& hgbResultTO, HgbResult& hgbResultType, float& hgbResultValue) {
    hgbResultType = (HgbResult)hgbResultTO.hgbresulttype();
    hgbResultValue = hgbResultTO.resultvalue();
}

///////////////////////////////////////////////////////////////////////////////////////////////////////
// Utility methods to deserialize Request Transfer Objects

LibraryCallReturnCode AMCooxSerializer::deserializeCalculateResultsRequest(
    const char serializedInputData[],
    const int inputDataSize,
    CooxModel& cooxModel,
    float& opticalPathLength,
    std::vector<ResultsCalculator::Point>& reference,
    std::vector<ResultsCalculator::Point>& sample,
    bool& allowNegativeValues,
    std::string& errorMessage)
{
    to::CalculateResultsRequest calculateResultsRequestTO;
    if (!calculateResultsRequestTO.ParseFromArray(serializedInputData, inputDataSize))
    {
        // ERROR:
        errorMessage = AMSerializerHelper::formatErrorMessage(
            AMSerializerHelper::ERROR_MESSAGE_PROTOBUFDECODE_REQUEST,
                "AMCooxSerializer::deserializeCalculateResultsRequest", __LINE__);
        return LibraryCallReturnCode::AM_CPP_PROTOBUF_DECODING_FAILED_DESERIALIZE;
    }

    // Copy CooxModel out
    if (calculateResultsRequestTO.has_cooxmodel())
    {
        convert(calculateResultsRequestTO.cooxmodel(), cooxModel);
    }
    else
    {
        std::string tmpString = std::string(AMSerializerHelper::ERROR_MESSAGE_MISSING_INPUT_PARAMETER) + " CooxModel is NULL";
        errorMessage = AMSerializerHelper::formatErrorMessage(
                tmpString.c_str(),
                "AMCooxSerializer::deserializeCalculateResultsRequest", __LINE__);
        return LibraryCallReturnCode::AM_CPP_PROTOBUF_DECODING_MISSING_INPUT_PARAMETER;
    }

    opticalPathLength = calculateResultsRequestTO.opl();

    reference.reserve(calculateResultsRequestTO.reference_size());

    for (int i = 0; i < calculateResultsRequestTO.reference_size(); i++)
    {
        ResultsCalculator::Point toPoint;
        convert(calculateResultsRequestTO.reference(i), toPoint);
        reference.push_back(toPoint);   // object is copied over
    }

    sample.reserve(calculateResultsRequestTO.sample_size());

    for (int i = 0; i < calculateResultsRequestTO.sample_size(); i++)
    {
        ResultsCalculator::Point toPoint;
        convert(calculateResultsRequestTO.sample(i), toPoint);
        sample.push_back(toPoint);
    }

    allowNegativeValues = calculateResultsRequestTO.allownegativevalues();

    return LibraryCallReturnCode::SUCCESS;
}

///////////////////////////////////////////////////////////////////////////////////////////////////////
// Utility methods to serialize Response Transfer Objects

LibraryCallReturnCode AMCooxSerializer::serializeCalculateResultsResponse(
        const std::map<HgbResult, float>& calculatedResults,
        char*& serializedResponseData,
        int& serializedResponseDataSize,
        std::string& errorMessage) {
    to::CalculateResultsResponse calculateResultsResponseTO;

    calculateResultsResponseTO.set_errorcode((int)LibraryCallReturnCode::SUCCESS);

    for (auto const &kv : calculatedResults)
    {
        to::CalculatedHgbResult* calculatedHgbResultTO = calculateResultsResponseTO.add_hgbresult();
        convert(kv.first, kv.second, *calculatedHgbResultTO);
    }

    serializedResponseDataSize = calculateResultsResponseTO.ByteSize();
    serializedResponseData = new char[serializedResponseDataSize];
    // Caller of this method calls "freeMemory" to delete array.

    if (calculateResultsResponseTO.SerializeToArray((void*)serializedResponseData, serializedResponseDataSize))
    {
        return LibraryCallReturnCode::SUCCESS;
    }
    else
    {
        AMSerializerHelper::freeMemory(serializedResponseData);
        errorMessage = AMSerializerHelper::formatErrorMessage(
            AMSerializerHelper::ERROR_MESSAGE_PROTOBUFENCODE_RESPONSE,
            "AMCooxSerializer::serializeCalculateResultsResponse", __LINE__);
        return LibraryCallReturnCode::AM_CPP_PROTOBUF_ENCODING_FAILED_SERIALIZE;
    }
}
