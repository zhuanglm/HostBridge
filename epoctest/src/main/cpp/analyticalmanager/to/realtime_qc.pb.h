// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: realtime_qc.proto

#ifndef PROTOBUF_realtime_5fqc_2eproto__INCLUDED
#define PROTOBUF_realtime_5fqc_2eproto__INCLUDED

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
#include "humidity_struct.pb.h"
// @@protoc_insertion_point(includes)

namespace to {

// Internal implementation detail -- do not call these.
void protobuf_AddDesc_realtime_5fqc_2eproto();
void protobuf_AssignDesc_realtime_5fqc_2eproto();
void protobuf_ShutdownFile_realtime_5fqc_2eproto();

class RealTimeQC;

// ===================================================================

class RealTimeQC : public ::google::protobuf::MessageLite /* @@protoc_insertion_point(class_definition:to.RealTimeQC) */ {
 public:
  RealTimeQC();
  virtual ~RealTimeQC();

  RealTimeQC(const RealTimeQC& from);

  inline RealTimeQC& operator=(const RealTimeQC& from) {
    CopyFrom(from);
    return *this;
  }

  static const RealTimeQC& default_instance();

  #ifdef GOOGLE_PROTOBUF_NO_STATIC_INITIALIZER
  // Returns the internal default instance pointer. This function can
  // return NULL thus should not be used by the user. This is intended
  // for Protobuf internal code. Please use default_instance() declared
  // above instead.
  static inline const RealTimeQC* internal_default_instance() {
    return default_instance_;
  }
  #endif

  void Swap(RealTimeQC* other);

  // implements Message ----------------------------------------------

  inline RealTimeQC* New() const { return New(NULL); }

  RealTimeQC* New(::google::protobuf::Arena* arena) const;
  void CheckTypeAndMergeFrom(const ::google::protobuf::MessageLite& from);
  void CopyFrom(const RealTimeQC& from);
  void MergeFrom(const RealTimeQC& from);
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
  void InternalSwap(RealTimeQC* other);
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

  // optional bool enabled = 1;
  void clear_enabled();
  static const int kEnabledFieldNumber = 1;
  bool enabled() const;
  void set_enabled(bool value);

  // optional int32 startTime = 2;
  void clear_starttime();
  static const int kStartTimeFieldNumber = 2;
  ::google::protobuf::int32 starttime() const;
  void set_starttime(::google::protobuf::int32 value);

  // optional int32 intervalTime = 3;
  void clear_intervaltime();
  static const int kIntervalTimeFieldNumber = 3;
  ::google::protobuf::int32 intervaltime() const;
  void set_intervaltime(::google::protobuf::int32 value);

  // optional int32 type = 4;
  void clear_type();
  static const int kTypeFieldNumber = 4;
  ::google::protobuf::int32 type() const;
  void set_type(::google::protobuf::int32 value);

  // optional int32 numPoints = 5;
  void clear_numpoints();
  static const int kNumPointsFieldNumber = 5;
  ::google::protobuf::int32 numpoints() const;
  void set_numpoints(::google::protobuf::int32 value);

  // optional double extra1 = 6;
  void clear_extra1();
  static const int kExtra1FieldNumber = 6;
  double extra1() const;
  void set_extra1(double value);

  // optional double extra2 = 7;
  void clear_extra2();
  static const int kExtra2FieldNumber = 7;
  double extra2() const;
  void set_extra2(double value);

  // optional double extra3 = 8;
  void clear_extra3();
  static const int kExtra3FieldNumber = 8;
  double extra3() const;
  void set_extra3(double value);

  // optional double extra4 = 9;
  void clear_extra4();
  static const int kExtra4FieldNumber = 9;
  double extra4() const;
  void set_extra4(double value);

  // optional double extra5 = 10;
  void clear_extra5();
  static const int kExtra5FieldNumber = 10;
  double extra5() const;
  void set_extra5(double value);

  // optional double extra6 = 11;
  void clear_extra6();
  static const int kExtra6FieldNumber = 11;
  double extra6() const;
  void set_extra6(double value);

  // optional bool continueIfFailed = 12;
  void clear_continueiffailed();
  static const int kContinueIfFailedFieldNumber = 12;
  bool continueiffailed() const;
  void set_continueiffailed(bool value);

  // optional int32 humidityUntil = 13;
  void clear_humidityuntil();
  static const int kHumidityUntilFieldNumber = 13;
  ::google::protobuf::int32 humidityuntil() const;
  void set_humidityuntil(::google::protobuf::int32 value);

  // repeated .to.HumidityStruct humidityConfig = 14;
  int humidityconfig_size() const;
  void clear_humidityconfig();
  static const int kHumidityConfigFieldNumber = 14;
  const ::to::HumidityStruct& humidityconfig(int index) const;
  ::to::HumidityStruct* mutable_humidityconfig(int index);
  ::to::HumidityStruct* add_humidityconfig();
  ::google::protobuf::RepeatedPtrField< ::to::HumidityStruct >*
      mutable_humidityconfig();
  const ::google::protobuf::RepeatedPtrField< ::to::HumidityStruct >&
      humidityconfig() const;

  // @@protoc_insertion_point(class_scope:to.RealTimeQC)
 private:

  ::google::protobuf::internal::ArenaStringPtr _unknown_fields_;
  ::google::protobuf::Arena* _arena_ptr_;

  bool _is_default_instance_;
  ::google::protobuf::int32 starttime_;
  ::google::protobuf::int32 intervaltime_;
  ::google::protobuf::int32 type_;
  ::google::protobuf::int32 numpoints_;
  double extra1_;
  double extra2_;
  double extra3_;
  bool enabled_;
  bool continueiffailed_;
  ::google::protobuf::int32 humidityuntil_;
  double extra4_;
  double extra5_;
  double extra6_;
  ::google::protobuf::RepeatedPtrField< ::to::HumidityStruct > humidityconfig_;
  mutable int _cached_size_;
  #ifdef GOOGLE_PROTOBUF_NO_STATIC_INITIALIZER
  friend void  protobuf_AddDesc_realtime_5fqc_2eproto_impl();
  #else
  friend void  protobuf_AddDesc_realtime_5fqc_2eproto();
  #endif
  friend void protobuf_AssignDesc_realtime_5fqc_2eproto();
  friend void protobuf_ShutdownFile_realtime_5fqc_2eproto();

  void InitAsDefaultInstance();
  static RealTimeQC* default_instance_;
};
// ===================================================================


// ===================================================================

#if !PROTOBUF_INLINE_NOT_IN_HEADERS
// RealTimeQC

// optional bool enabled = 1;
inline void RealTimeQC::clear_enabled() {
  enabled_ = false;
}
inline bool RealTimeQC::enabled() const {
  // @@protoc_insertion_point(field_get:to.RealTimeQC.enabled)
  return enabled_;
}
inline void RealTimeQC::set_enabled(bool value) {
  
  enabled_ = value;
  // @@protoc_insertion_point(field_set:to.RealTimeQC.enabled)
}

// optional int32 startTime = 2;
inline void RealTimeQC::clear_starttime() {
  starttime_ = 0;
}
inline ::google::protobuf::int32 RealTimeQC::starttime() const {
  // @@protoc_insertion_point(field_get:to.RealTimeQC.startTime)
  return starttime_;
}
inline void RealTimeQC::set_starttime(::google::protobuf::int32 value) {
  
  starttime_ = value;
  // @@protoc_insertion_point(field_set:to.RealTimeQC.startTime)
}

// optional int32 intervalTime = 3;
inline void RealTimeQC::clear_intervaltime() {
  intervaltime_ = 0;
}
inline ::google::protobuf::int32 RealTimeQC::intervaltime() const {
  // @@protoc_insertion_point(field_get:to.RealTimeQC.intervalTime)
  return intervaltime_;
}
inline void RealTimeQC::set_intervaltime(::google::protobuf::int32 value) {
  
  intervaltime_ = value;
  // @@protoc_insertion_point(field_set:to.RealTimeQC.intervalTime)
}

// optional int32 type = 4;
inline void RealTimeQC::clear_type() {
  type_ = 0;
}
inline ::google::protobuf::int32 RealTimeQC::type() const {
  // @@protoc_insertion_point(field_get:to.RealTimeQC.type)
  return type_;
}
inline void RealTimeQC::set_type(::google::protobuf::int32 value) {
  
  type_ = value;
  // @@protoc_insertion_point(field_set:to.RealTimeQC.type)
}

// optional int32 numPoints = 5;
inline void RealTimeQC::clear_numpoints() {
  numpoints_ = 0;
}
inline ::google::protobuf::int32 RealTimeQC::numpoints() const {
  // @@protoc_insertion_point(field_get:to.RealTimeQC.numPoints)
  return numpoints_;
}
inline void RealTimeQC::set_numpoints(::google::protobuf::int32 value) {
  
  numpoints_ = value;
  // @@protoc_insertion_point(field_set:to.RealTimeQC.numPoints)
}

// optional double extra1 = 6;
inline void RealTimeQC::clear_extra1() {
  extra1_ = 0;
}
inline double RealTimeQC::extra1() const {
  // @@protoc_insertion_point(field_get:to.RealTimeQC.extra1)
  return extra1_;
}
inline void RealTimeQC::set_extra1(double value) {
  
  extra1_ = value;
  // @@protoc_insertion_point(field_set:to.RealTimeQC.extra1)
}

// optional double extra2 = 7;
inline void RealTimeQC::clear_extra2() {
  extra2_ = 0;
}
inline double RealTimeQC::extra2() const {
  // @@protoc_insertion_point(field_get:to.RealTimeQC.extra2)
  return extra2_;
}
inline void RealTimeQC::set_extra2(double value) {
  
  extra2_ = value;
  // @@protoc_insertion_point(field_set:to.RealTimeQC.extra2)
}

// optional double extra3 = 8;
inline void RealTimeQC::clear_extra3() {
  extra3_ = 0;
}
inline double RealTimeQC::extra3() const {
  // @@protoc_insertion_point(field_get:to.RealTimeQC.extra3)
  return extra3_;
}
inline void RealTimeQC::set_extra3(double value) {
  
  extra3_ = value;
  // @@protoc_insertion_point(field_set:to.RealTimeQC.extra3)
}

// optional double extra4 = 9;
inline void RealTimeQC::clear_extra4() {
  extra4_ = 0;
}
inline double RealTimeQC::extra4() const {
  // @@protoc_insertion_point(field_get:to.RealTimeQC.extra4)
  return extra4_;
}
inline void RealTimeQC::set_extra4(double value) {
  
  extra4_ = value;
  // @@protoc_insertion_point(field_set:to.RealTimeQC.extra4)
}

// optional double extra5 = 10;
inline void RealTimeQC::clear_extra5() {
  extra5_ = 0;
}
inline double RealTimeQC::extra5() const {
  // @@protoc_insertion_point(field_get:to.RealTimeQC.extra5)
  return extra5_;
}
inline void RealTimeQC::set_extra5(double value) {
  
  extra5_ = value;
  // @@protoc_insertion_point(field_set:to.RealTimeQC.extra5)
}

// optional double extra6 = 11;
inline void RealTimeQC::clear_extra6() {
  extra6_ = 0;
}
inline double RealTimeQC::extra6() const {
  // @@protoc_insertion_point(field_get:to.RealTimeQC.extra6)
  return extra6_;
}
inline void RealTimeQC::set_extra6(double value) {
  
  extra6_ = value;
  // @@protoc_insertion_point(field_set:to.RealTimeQC.extra6)
}

// optional bool continueIfFailed = 12;
inline void RealTimeQC::clear_continueiffailed() {
  continueiffailed_ = false;
}
inline bool RealTimeQC::continueiffailed() const {
  // @@protoc_insertion_point(field_get:to.RealTimeQC.continueIfFailed)
  return continueiffailed_;
}
inline void RealTimeQC::set_continueiffailed(bool value) {
  
  continueiffailed_ = value;
  // @@protoc_insertion_point(field_set:to.RealTimeQC.continueIfFailed)
}

// optional int32 humidityUntil = 13;
inline void RealTimeQC::clear_humidityuntil() {
  humidityuntil_ = 0;
}
inline ::google::protobuf::int32 RealTimeQC::humidityuntil() const {
  // @@protoc_insertion_point(field_get:to.RealTimeQC.humidityUntil)
  return humidityuntil_;
}
inline void RealTimeQC::set_humidityuntil(::google::protobuf::int32 value) {
  
  humidityuntil_ = value;
  // @@protoc_insertion_point(field_set:to.RealTimeQC.humidityUntil)
}

// repeated .to.HumidityStruct humidityConfig = 14;
inline int RealTimeQC::humidityconfig_size() const {
  return humidityconfig_.size();
}
inline void RealTimeQC::clear_humidityconfig() {
  humidityconfig_.Clear();
}
inline const ::to::HumidityStruct& RealTimeQC::humidityconfig(int index) const {
  // @@protoc_insertion_point(field_get:to.RealTimeQC.humidityConfig)
  return humidityconfig_.Get(index);
}
inline ::to::HumidityStruct* RealTimeQC::mutable_humidityconfig(int index) {
  // @@protoc_insertion_point(field_mutable:to.RealTimeQC.humidityConfig)
  return humidityconfig_.Mutable(index);
}
inline ::to::HumidityStruct* RealTimeQC::add_humidityconfig() {
  // @@protoc_insertion_point(field_add:to.RealTimeQC.humidityConfig)
  return humidityconfig_.Add();
}
inline ::google::protobuf::RepeatedPtrField< ::to::HumidityStruct >*
RealTimeQC::mutable_humidityconfig() {
  // @@protoc_insertion_point(field_mutable_list:to.RealTimeQC.humidityConfig)
  return &humidityconfig_;
}
inline const ::google::protobuf::RepeatedPtrField< ::to::HumidityStruct >&
RealTimeQC::humidityconfig() const {
  // @@protoc_insertion_point(field_list:to.RealTimeQC.humidityConfig)
  return humidityconfig_;
}

#endif  // !PROTOBUF_INLINE_NOT_IN_HEADERS

// @@protoc_insertion_point(namespace_scope)

}  // namespace to

// @@protoc_insertion_point(global_scope)

#endif  // PROTOBUF_realtime_5fqc_2eproto__INCLUDED
