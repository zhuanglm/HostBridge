// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: calc_bge_request.proto

#ifndef PROTOBUF_calc_5fbge_5frequest_2eproto__INCLUDED
#define PROTOBUF_calc_5fbge_5frequest_2eproto__INCLUDED

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
#include "sensor_readings.pb.h"
#include "bge_parameters.pb.h"
// @@protoc_insertion_point(includes)

namespace to {

// Internal implementation detail -- do not call these.
void protobuf_AddDesc_calc_5fbge_5frequest_2eproto();
void protobuf_AssignDesc_calc_5fbge_5frequest_2eproto();
void protobuf_ShutdownFile_calc_5fbge_5frequest_2eproto();

class CalculateBGERequest;

// ===================================================================

class CalculateBGERequest : public ::google::protobuf::MessageLite /* @@protoc_insertion_point(class_definition:to.CalculateBGERequest) */ {
 public:
  CalculateBGERequest();
  virtual ~CalculateBGERequest();

  CalculateBGERequest(const CalculateBGERequest& from);

  inline CalculateBGERequest& operator=(const CalculateBGERequest& from) {
    CopyFrom(from);
    return *this;
  }

  static const CalculateBGERequest& default_instance();

  #ifdef GOOGLE_PROTOBUF_NO_STATIC_INITIALIZER
  // Returns the internal default instance pointer. This function can
  // return NULL thus should not be used by the user. This is intended
  // for Protobuf internal code. Please use default_instance() declared
  // above instead.
  static inline const CalculateBGERequest* internal_default_instance() {
    return default_instance_;
  }
  #endif

  void Swap(CalculateBGERequest* other);

  // implements Message ----------------------------------------------

  inline CalculateBGERequest* New() const { return New(NULL); }

  CalculateBGERequest* New(::google::protobuf::Arena* arena) const;
  void CheckTypeAndMergeFrom(const ::google::protobuf::MessageLite& from);
  void CopyFrom(const CalculateBGERequest& from);
  void MergeFrom(const CalculateBGERequest& from);
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
  void InternalSwap(CalculateBGERequest* other);
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

  // repeated .to.SensorReadings sensorReadings = 1;
  int sensorreadings_size() const;
  void clear_sensorreadings();
  static const int kSensorReadingsFieldNumber = 1;
  const ::to::SensorReadings& sensorreadings(int index) const;
  ::to::SensorReadings* mutable_sensorreadings(int index);
  ::to::SensorReadings* add_sensorreadings();
  ::google::protobuf::RepeatedPtrField< ::to::SensorReadings >*
      mutable_sensorreadings();
  const ::google::protobuf::RepeatedPtrField< ::to::SensorReadings >&
      sensorreadings() const;

  // optional .to.BGEParameters params = 2;
  bool has_params() const;
  void clear_params();
  static const int kParamsFieldNumber = 2;
  const ::to::BGEParameters& params() const;
  ::to::BGEParameters* mutable_params();
  ::to::BGEParameters* release_params();
  void set_allocated_params(::to::BGEParameters* params);

  // optional bool allowNegativeValues = 3;
  void clear_allownegativevalues();
  static const int kAllowNegativeValuesFieldNumber = 3;
  bool allownegativevalues() const;
  void set_allownegativevalues(bool value);

  // @@protoc_insertion_point(class_scope:to.CalculateBGERequest)
 private:

  ::google::protobuf::internal::ArenaStringPtr _unknown_fields_;
  ::google::protobuf::Arena* _arena_ptr_;

  bool _is_default_instance_;
  ::google::protobuf::RepeatedPtrField< ::to::SensorReadings > sensorreadings_;
  ::to::BGEParameters* params_;
  bool allownegativevalues_;
  mutable int _cached_size_;
  #ifdef GOOGLE_PROTOBUF_NO_STATIC_INITIALIZER
  friend void  protobuf_AddDesc_calc_5fbge_5frequest_2eproto_impl();
  #else
  friend void  protobuf_AddDesc_calc_5fbge_5frequest_2eproto();
  #endif
  friend void protobuf_AssignDesc_calc_5fbge_5frequest_2eproto();
  friend void protobuf_ShutdownFile_calc_5fbge_5frequest_2eproto();

  void InitAsDefaultInstance();
  static CalculateBGERequest* default_instance_;
};
// ===================================================================


// ===================================================================

#if !PROTOBUF_INLINE_NOT_IN_HEADERS
// CalculateBGERequest

// repeated .to.SensorReadings sensorReadings = 1;
inline int CalculateBGERequest::sensorreadings_size() const {
  return sensorreadings_.size();
}
inline void CalculateBGERequest::clear_sensorreadings() {
  sensorreadings_.Clear();
}
inline const ::to::SensorReadings& CalculateBGERequest::sensorreadings(int index) const {
  // @@protoc_insertion_point(field_get:to.CalculateBGERequest.sensorReadings)
  return sensorreadings_.Get(index);
}
inline ::to::SensorReadings* CalculateBGERequest::mutable_sensorreadings(int index) {
  // @@protoc_insertion_point(field_mutable:to.CalculateBGERequest.sensorReadings)
  return sensorreadings_.Mutable(index);
}
inline ::to::SensorReadings* CalculateBGERequest::add_sensorreadings() {
  // @@protoc_insertion_point(field_add:to.CalculateBGERequest.sensorReadings)
  return sensorreadings_.Add();
}
inline ::google::protobuf::RepeatedPtrField< ::to::SensorReadings >*
CalculateBGERequest::mutable_sensorreadings() {
  // @@protoc_insertion_point(field_mutable_list:to.CalculateBGERequest.sensorReadings)
  return &sensorreadings_;
}
inline const ::google::protobuf::RepeatedPtrField< ::to::SensorReadings >&
CalculateBGERequest::sensorreadings() const {
  // @@protoc_insertion_point(field_list:to.CalculateBGERequest.sensorReadings)
  return sensorreadings_;
}

// optional .to.BGEParameters params = 2;
inline bool CalculateBGERequest::has_params() const {
  return !_is_default_instance_ && params_ != NULL;
}
inline void CalculateBGERequest::clear_params() {
  if (GetArenaNoVirtual() == NULL && params_ != NULL) delete params_;
  params_ = NULL;
}
inline const ::to::BGEParameters& CalculateBGERequest::params() const {
  // @@protoc_insertion_point(field_get:to.CalculateBGERequest.params)
#ifdef GOOGLE_PROTOBUF_NO_STATIC_INITIALIZER
  return params_ != NULL ? *params_ : *default_instance().params_;
#else
  return params_ != NULL ? *params_ : *default_instance_->params_;
#endif
}
inline ::to::BGEParameters* CalculateBGERequest::mutable_params() {
  
  if (params_ == NULL) {
    params_ = new ::to::BGEParameters;
  }
  // @@protoc_insertion_point(field_mutable:to.CalculateBGERequest.params)
  return params_;
}
inline ::to::BGEParameters* CalculateBGERequest::release_params() {
  // @@protoc_insertion_point(field_release:to.CalculateBGERequest.params)
  
  ::to::BGEParameters* temp = params_;
  params_ = NULL;
  return temp;
}
inline void CalculateBGERequest::set_allocated_params(::to::BGEParameters* params) {
  delete params_;
  params_ = params;
  if (params) {
    
  } else {
    
  }
  // @@protoc_insertion_point(field_set_allocated:to.CalculateBGERequest.params)
}

// optional bool allowNegativeValues = 3;
inline void CalculateBGERequest::clear_allownegativevalues() {
  allownegativevalues_ = false;
}
inline bool CalculateBGERequest::allownegativevalues() const {
  // @@protoc_insertion_point(field_get:to.CalculateBGERequest.allowNegativeValues)
  return allownegativevalues_;
}
inline void CalculateBGERequest::set_allownegativevalues(bool value) {
  
  allownegativevalues_ = value;
  // @@protoc_insertion_point(field_set:to.CalculateBGERequest.allowNegativeValues)
}

#endif  // !PROTOBUF_INLINE_NOT_IN_HEADERS

// @@protoc_insertion_point(namespace_scope)

}  // namespace to

// @@protoc_insertion_point(global_scope)

#endif  // PROTOBUF_calc_5fbge_5frequest_2eproto__INCLUDED
