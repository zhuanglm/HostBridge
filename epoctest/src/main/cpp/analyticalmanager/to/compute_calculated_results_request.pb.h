// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: compute_calculated_results_request.proto

#ifndef PROTOBUF_compute_5fcalculated_5fresults_5frequest_2eproto__INCLUDED
#define PROTOBUF_compute_5fcalculated_5fresults_5frequest_2eproto__INCLUDED

#include <string>

#include <google/protobuf/stubs/common.h>

#if GOOGLE_PROTOBUF_VERSION < 3000000
#error This file was generated by a newer version of protoc which is
#error incompatible with your Protocol Buffer headers.  Please update
#error your headers.
#endif
#if 3000000 < GOOGLE_PROTOBUF_MIN_PROTOC_VERSION
#error This file was generated by an older version of protoc which is
#error incompatible with your Protocol Buffer headers.  Please
#error regenerate this file with a newer version of protoc.
#endif

#include <google/protobuf/arena.h>
#include <google/protobuf/arenastring.h>
#include <google/protobuf/generated_message_util.h>
#include <google/protobuf/message_lite.h>
#include <google/protobuf/repeated_field.h>
#include <google/protobuf/extension_set.h>
#include "final_result.pb.h"
// @@protoc_insertion_point(includes)

namespace to {

// Internal implementation detail -- do not call these.
void protobuf_AddDesc_compute_5fcalculated_5fresults_5frequest_2eproto();
void protobuf_AssignDesc_compute_5fcalculated_5fresults_5frequest_2eproto();
void protobuf_ShutdownFile_compute_5fcalculated_5fresults_5frequest_2eproto();

class ComputeCalculatedResultsRequest;

// ===================================================================

class ComputeCalculatedResultsRequest : public ::google::protobuf::MessageLite /* @@protoc_insertion_point(class_definition:to.ComputeCalculatedResultsRequest) */ {
 public:
  ComputeCalculatedResultsRequest();
  virtual ~ComputeCalculatedResultsRequest();

  ComputeCalculatedResultsRequest(const ComputeCalculatedResultsRequest& from);

  inline ComputeCalculatedResultsRequest& operator=(const ComputeCalculatedResultsRequest& from) {
    CopyFrom(from);
    return *this;
  }

  static const ComputeCalculatedResultsRequest& default_instance();

  #ifdef GOOGLE_PROTOBUF_NO_STATIC_INITIALIZER
  // Returns the internal default instance pointer. This function can
  // return NULL thus should not be used by the user. This is intended
  // for Protobuf internal code. Please use default_instance() declared
  // above instead.
  static inline const ComputeCalculatedResultsRequest* internal_default_instance() {
    return default_instance_;
  }
  #endif

  void Swap(ComputeCalculatedResultsRequest* other);

  // implements Message ----------------------------------------------

  inline ComputeCalculatedResultsRequest* New() const { return New(NULL); }

  ComputeCalculatedResultsRequest* New(::google::protobuf::Arena* arena) const;
  void CheckTypeAndMergeFrom(const ::google::protobuf::MessageLite& from);
  void CopyFrom(const ComputeCalculatedResultsRequest& from);
  void MergeFrom(const ComputeCalculatedResultsRequest& from);
  void Clear();
  bool IsInitialized() const;

  int ByteSize() const;
  bool MergePartialFromCodedStream(
      ::google::protobuf::io::CodedInputStream* input);
  void SerializeWithCachedSizes(
      ::google::protobuf::io::CodedOutputStream* output) const;
  void DiscardUnknownFields();
  int GetCachedSize() const { return _cached_size_; }
  private:
  void SharedCtor();
  void SharedDtor();
  void SetCachedSize(int size) const;
  void InternalSwap(ComputeCalculatedResultsRequest* other);
  private:
  inline ::google::protobuf::Arena* GetArenaNoVirtual() const {
    return _arena_ptr_;
  }
  inline ::google::protobuf::Arena* MaybeArenaPtr() const {
    return _arena_ptr_;
  }
  public:

  ::std::string GetTypeName() const;

  // nested types ----------------------------------------------------

  // accessors -------------------------------------------------------

  // repeated .to.FinalResult measuredResults = 1;
  int measuredresults_size() const;
  void clear_measuredresults();
  static const int kMeasuredResultsFieldNumber = 1;
  const ::to::FinalResult& measuredresults(int index) const;
  ::to::FinalResult* mutable_measuredresults(int index);
  ::to::FinalResult* add_measuredresults();
  ::google::protobuf::RepeatedPtrField< ::to::FinalResult >*
      mutable_measuredresults();
  const ::google::protobuf::RepeatedPtrField< ::to::FinalResult >&
      measuredresults() const;

  // repeated .to.FinalResult calculatedResults = 2;
  int calculatedresults_size() const;
  void clear_calculatedresults();
  static const int kCalculatedResultsFieldNumber = 2;
  const ::to::FinalResult& calculatedresults(int index) const;
  ::to::FinalResult* mutable_calculatedresults(int index);
  ::to::FinalResult* add_calculatedresults();
  ::google::protobuf::RepeatedPtrField< ::to::FinalResult >*
      mutable_calculatedresults();
  const ::google::protobuf::RepeatedPtrField< ::to::FinalResult >&
      calculatedresults() const;

  // optional double passedctHb = 3;
  void clear_passedcthb();
  static const int kPassedctHbFieldNumber = 3;
  double passedcthb() const;
  void set_passedcthb(double value);

  // optional double FiO2 = 4;
  void clear_fio2();
  static const int kFiO2FieldNumber = 4;
  double fio2() const;
  void set_fio2(double value);

  // optional double temperature = 5;
  void clear_temperature();
  static const int kTemperatureFieldNumber = 5;
  double temperature() const;
  void set_temperature(double value);

  // optional double pressure = 6;
  void clear_pressure();
  static const int kPressureFieldNumber = 6;
  double pressure() const;
  void set_pressure(double value);

  // optional int32 testMode = 7;
  void clear_testmode();
  static const int kTestModeFieldNumber = 7;
  ::google::protobuf::int32 testmode() const;
  void set_testmode(::google::protobuf::int32 value);

  // optional double patientAge = 8;
  void clear_patientage();
  static const int kPatientAgeFieldNumber = 8;
  double patientage() const;
  void set_patientage(double value);

  // optional int32 gender = 9;
  void clear_gender();
  static const int kGenderFieldNumber = 9;
  ::google::protobuf::int32 gender() const;
  void set_gender(::google::protobuf::int32 value);

  // optional int32 egfrFormula = 10;
  void clear_egfrformula();
  static const int kEgfrFormulaFieldNumber = 10;
  ::google::protobuf::int32 egfrformula() const;
  void set_egfrformula(::google::protobuf::int32 value);

  // optional double patientHeight = 11;
  void clear_patientheight();
  static const int kPatientHeightFieldNumber = 11;
  double patientheight() const;
  void set_patientheight(double value);

  // optional int32 ageCategory = 12;
  void clear_agecategory();
  static const int kAgeCategoryFieldNumber = 12;
  ::google::protobuf::int32 agecategory() const;
  void set_agecategory(::google::protobuf::int32 value);

  // optional double RQ = 13;
  void clear_rq();
  static const int kRQFieldNumber = 13;
  double rq() const;
  void set_rq(double value);

  // optional bool calculateAlveolar = 14;
  void clear_calculatealveolar();
  static const int kCalculateAlveolarFieldNumber = 14;
  bool calculatealveolar() const;
  void set_calculatealveolar(bool value);

  // optional bool theApplymTCO2 = 15;
  void clear_theapplymtco2();
  static const int kTheApplymTCO2FieldNumber = 15;
  bool theapplymtco2() const;
  void set_theapplymtco2(bool value);

  // @@protoc_insertion_point(class_scope:to.ComputeCalculatedResultsRequest)
 private:

  ::google::protobuf::internal::ArenaStringPtr _unknown_fields_;
  ::google::protobuf::Arena* _arena_ptr_;

  bool _is_default_instance_;
  ::google::protobuf::RepeatedPtrField< ::to::FinalResult > measuredresults_;
  ::google::protobuf::RepeatedPtrField< ::to::FinalResult > calculatedresults_;
  double passedcthb_;
  double fio2_;
  double temperature_;
  double pressure_;
  double patientage_;
  ::google::protobuf::int32 testmode_;
  ::google::protobuf::int32 gender_;
  double patientheight_;
  ::google::protobuf::int32 egfrformula_;
  ::google::protobuf::int32 agecategory_;
  double rq_;
  bool calculatealveolar_;
  bool theapplymtco2_;
  mutable int _cached_size_;
  #ifdef GOOGLE_PROTOBUF_NO_STATIC_INITIALIZER
  friend void  protobuf_AddDesc_compute_5fcalculated_5fresults_5frequest_2eproto_impl();
  #else
  friend void  protobuf_AddDesc_compute_5fcalculated_5fresults_5frequest_2eproto();
  #endif
  friend void protobuf_AssignDesc_compute_5fcalculated_5fresults_5frequest_2eproto();
  friend void protobuf_ShutdownFile_compute_5fcalculated_5fresults_5frequest_2eproto();

  void InitAsDefaultInstance();
  static ComputeCalculatedResultsRequest* default_instance_;
};
// ===================================================================


// ===================================================================

#if !PROTOBUF_INLINE_NOT_IN_HEADERS
// ComputeCalculatedResultsRequest

// repeated .to.FinalResult measuredResults = 1;
inline int ComputeCalculatedResultsRequest::measuredresults_size() const {
  return measuredresults_.size();
}
inline void ComputeCalculatedResultsRequest::clear_measuredresults() {
  measuredresults_.Clear();
}
inline const ::to::FinalResult& ComputeCalculatedResultsRequest::measuredresults(int index) const {
  // @@protoc_insertion_point(field_get:to.ComputeCalculatedResultsRequest.measuredResults)
  return measuredresults_.Get(index);
}
inline ::to::FinalResult* ComputeCalculatedResultsRequest::mutable_measuredresults(int index) {
  // @@protoc_insertion_point(field_mutable:to.ComputeCalculatedResultsRequest.measuredResults)
  return measuredresults_.Mutable(index);
}
inline ::to::FinalResult* ComputeCalculatedResultsRequest::add_measuredresults() {
  // @@protoc_insertion_point(field_add:to.ComputeCalculatedResultsRequest.measuredResults)
  return measuredresults_.Add();
}
inline ::google::protobuf::RepeatedPtrField< ::to::FinalResult >*
ComputeCalculatedResultsRequest::mutable_measuredresults() {
  // @@protoc_insertion_point(field_mutable_list:to.ComputeCalculatedResultsRequest.measuredResults)
  return &measuredresults_;
}
inline const ::google::protobuf::RepeatedPtrField< ::to::FinalResult >&
ComputeCalculatedResultsRequest::measuredresults() const {
  // @@protoc_insertion_point(field_list:to.ComputeCalculatedResultsRequest.measuredResults)
  return measuredresults_;
}

// repeated .to.FinalResult calculatedResults = 2;
inline int ComputeCalculatedResultsRequest::calculatedresults_size() const {
  return calculatedresults_.size();
}
inline void ComputeCalculatedResultsRequest::clear_calculatedresults() {
  calculatedresults_.Clear();
}
inline const ::to::FinalResult& ComputeCalculatedResultsRequest::calculatedresults(int index) const {
  // @@protoc_insertion_point(field_get:to.ComputeCalculatedResultsRequest.calculatedResults)
  return calculatedresults_.Get(index);
}
inline ::to::FinalResult* ComputeCalculatedResultsRequest::mutable_calculatedresults(int index) {
  // @@protoc_insertion_point(field_mutable:to.ComputeCalculatedResultsRequest.calculatedResults)
  return calculatedresults_.Mutable(index);
}
inline ::to::FinalResult* ComputeCalculatedResultsRequest::add_calculatedresults() {
  // @@protoc_insertion_point(field_add:to.ComputeCalculatedResultsRequest.calculatedResults)
  return calculatedresults_.Add();
}
inline ::google::protobuf::RepeatedPtrField< ::to::FinalResult >*
ComputeCalculatedResultsRequest::mutable_calculatedresults() {
  // @@protoc_insertion_point(field_mutable_list:to.ComputeCalculatedResultsRequest.calculatedResults)
  return &calculatedresults_;
}
inline const ::google::protobuf::RepeatedPtrField< ::to::FinalResult >&
ComputeCalculatedResultsRequest::calculatedresults() const {
  // @@protoc_insertion_point(field_list:to.ComputeCalculatedResultsRequest.calculatedResults)
  return calculatedresults_;
}

// optional double passedctHb = 3;
inline void ComputeCalculatedResultsRequest::clear_passedcthb() {
  passedcthb_ = 0;
}
inline double ComputeCalculatedResultsRequest::passedcthb() const {
  // @@protoc_insertion_point(field_get:to.ComputeCalculatedResultsRequest.passedctHb)
  return passedcthb_;
}
inline void ComputeCalculatedResultsRequest::set_passedcthb(double value) {
  
  passedcthb_ = value;
  // @@protoc_insertion_point(field_set:to.ComputeCalculatedResultsRequest.passedctHb)
}

// optional double FiO2 = 4;
inline void ComputeCalculatedResultsRequest::clear_fio2() {
  fio2_ = 0;
}
inline double ComputeCalculatedResultsRequest::fio2() const {
  // @@protoc_insertion_point(field_get:to.ComputeCalculatedResultsRequest.FiO2)
  return fio2_;
}
inline void ComputeCalculatedResultsRequest::set_fio2(double value) {
  
  fio2_ = value;
  // @@protoc_insertion_point(field_set:to.ComputeCalculatedResultsRequest.FiO2)
}

// optional double temperature = 5;
inline void ComputeCalculatedResultsRequest::clear_temperature() {
  temperature_ = 0;
}
inline double ComputeCalculatedResultsRequest::temperature() const {
  // @@protoc_insertion_point(field_get:to.ComputeCalculatedResultsRequest.temperature)
  return temperature_;
}
inline void ComputeCalculatedResultsRequest::set_temperature(double value) {
  
  temperature_ = value;
  // @@protoc_insertion_point(field_set:to.ComputeCalculatedResultsRequest.temperature)
}

// optional double pressure = 6;
inline void ComputeCalculatedResultsRequest::clear_pressure() {
  pressure_ = 0;
}
inline double ComputeCalculatedResultsRequest::pressure() const {
  // @@protoc_insertion_point(field_get:to.ComputeCalculatedResultsRequest.pressure)
  return pressure_;
}
inline void ComputeCalculatedResultsRequest::set_pressure(double value) {
  
  pressure_ = value;
  // @@protoc_insertion_point(field_set:to.ComputeCalculatedResultsRequest.pressure)
}

// optional int32 testMode = 7;
inline void ComputeCalculatedResultsRequest::clear_testmode() {
  testmode_ = 0;
}
inline ::google::protobuf::int32 ComputeCalculatedResultsRequest::testmode() const {
  // @@protoc_insertion_point(field_get:to.ComputeCalculatedResultsRequest.testMode)
  return testmode_;
}
inline void ComputeCalculatedResultsRequest::set_testmode(::google::protobuf::int32 value) {
  
  testmode_ = value;
  // @@protoc_insertion_point(field_set:to.ComputeCalculatedResultsRequest.testMode)
}

// optional double patientAge = 8;
inline void ComputeCalculatedResultsRequest::clear_patientage() {
  patientage_ = 0;
}
inline double ComputeCalculatedResultsRequest::patientage() const {
  // @@protoc_insertion_point(field_get:to.ComputeCalculatedResultsRequest.patientAge)
  return patientage_;
}
inline void ComputeCalculatedResultsRequest::set_patientage(double value) {
  
  patientage_ = value;
  // @@protoc_insertion_point(field_set:to.ComputeCalculatedResultsRequest.patientAge)
}

// optional int32 gender = 9;
inline void ComputeCalculatedResultsRequest::clear_gender() {
  gender_ = 0;
}
inline ::google::protobuf::int32 ComputeCalculatedResultsRequest::gender() const {
  // @@protoc_insertion_point(field_get:to.ComputeCalculatedResultsRequest.gender)
  return gender_;
}
inline void ComputeCalculatedResultsRequest::set_gender(::google::protobuf::int32 value) {
  
  gender_ = value;
  // @@protoc_insertion_point(field_set:to.ComputeCalculatedResultsRequest.gender)
}

// optional int32 egfrFormula = 10;
inline void ComputeCalculatedResultsRequest::clear_egfrformula() {
  egfrformula_ = 0;
}
inline ::google::protobuf::int32 ComputeCalculatedResultsRequest::egfrformula() const {
  // @@protoc_insertion_point(field_get:to.ComputeCalculatedResultsRequest.egfrFormula)
  return egfrformula_;
}
inline void ComputeCalculatedResultsRequest::set_egfrformula(::google::protobuf::int32 value) {
  
  egfrformula_ = value;
  // @@protoc_insertion_point(field_set:to.ComputeCalculatedResultsRequest.egfrFormula)
}

// optional double patientHeight = 11;
inline void ComputeCalculatedResultsRequest::clear_patientheight() {
  patientheight_ = 0;
}
inline double ComputeCalculatedResultsRequest::patientheight() const {
  // @@protoc_insertion_point(field_get:to.ComputeCalculatedResultsRequest.patientHeight)
  return patientheight_;
}
inline void ComputeCalculatedResultsRequest::set_patientheight(double value) {
  
  patientheight_ = value;
  // @@protoc_insertion_point(field_set:to.ComputeCalculatedResultsRequest.patientHeight)
}

// optional int32 ageCategory = 12;
inline void ComputeCalculatedResultsRequest::clear_agecategory() {
  agecategory_ = 0;
}
inline ::google::protobuf::int32 ComputeCalculatedResultsRequest::agecategory() const {
  // @@protoc_insertion_point(field_get:to.ComputeCalculatedResultsRequest.ageCategory)
  return agecategory_;
}
inline void ComputeCalculatedResultsRequest::set_agecategory(::google::protobuf::int32 value) {
  
  agecategory_ = value;
  // @@protoc_insertion_point(field_set:to.ComputeCalculatedResultsRequest.ageCategory)
}

// optional double RQ = 13;
inline void ComputeCalculatedResultsRequest::clear_rq() {
  rq_ = 0;
}
inline double ComputeCalculatedResultsRequest::rq() const {
  // @@protoc_insertion_point(field_get:to.ComputeCalculatedResultsRequest.RQ)
  return rq_;
}
inline void ComputeCalculatedResultsRequest::set_rq(double value) {
  
  rq_ = value;
  // @@protoc_insertion_point(field_set:to.ComputeCalculatedResultsRequest.RQ)
}

// optional bool calculateAlveolar = 14;
inline void ComputeCalculatedResultsRequest::clear_calculatealveolar() {
  calculatealveolar_ = false;
}
inline bool ComputeCalculatedResultsRequest::calculatealveolar() const {
  // @@protoc_insertion_point(field_get:to.ComputeCalculatedResultsRequest.calculateAlveolar)
  return calculatealveolar_;
}
inline void ComputeCalculatedResultsRequest::set_calculatealveolar(bool value) {
  
  calculatealveolar_ = value;
  // @@protoc_insertion_point(field_set:to.ComputeCalculatedResultsRequest.calculateAlveolar)
}

// optional bool theApplymTCO2 = 15;
inline void ComputeCalculatedResultsRequest::clear_theapplymtco2() {
  theapplymtco2_ = false;
}
inline bool ComputeCalculatedResultsRequest::theapplymtco2() const {
  // @@protoc_insertion_point(field_get:to.ComputeCalculatedResultsRequest.theApplymTCO2)
  return theapplymtco2_;
}
inline void ComputeCalculatedResultsRequest::set_theapplymtco2(bool value) {
  
  theapplymtco2_ = value;
  // @@protoc_insertion_point(field_set:to.ComputeCalculatedResultsRequest.theApplymTCO2)
}

#endif  // !PROTOBUF_INLINE_NOT_IN_HEADERS

// @@protoc_insertion_point(namespace_scope)

}  // namespace to

// @@protoc_insertion_point(global_scope)

#endif  // PROTOBUF_compute_5fcalculated_5fresults_5frequest_2eproto__INCLUDED
