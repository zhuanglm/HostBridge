// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: humidity_struct.proto

#ifndef PROTOBUF_humidity_5fstruct_2eproto__INCLUDED
#define PROTOBUF_humidity_5fstruct_2eproto__INCLUDED

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
// @@protoc_insertion_point(includes)

namespace to {

// Internal implementation detail -- do not call these.
void protobuf_AddDesc_humidity_5fstruct_2eproto();
void protobuf_AssignDesc_humidity_5fstruct_2eproto();
void protobuf_ShutdownFile_humidity_5fstruct_2eproto();

class HumidityStruct;

// ===================================================================

class HumidityStruct : public ::google::protobuf::MessageLite /* @@protoc_insertion_point(class_definition:to.HumidityStruct) */ {
 public:
  HumidityStruct();
  virtual ~HumidityStruct();

  HumidityStruct(const HumidityStruct& from);

  inline HumidityStruct& operator=(const HumidityStruct& from) {
    CopyFrom(from);
    return *this;
  }

  static const HumidityStruct& default_instance();

  #ifdef GOOGLE_PROTOBUF_NO_STATIC_INITIALIZER
  // Returns the internal default instance pointer. This function can
  // return NULL thus should not be used by the user. This is intended
  // for Protobuf internal code. Please use default_instance() declared
  // above instead.
  static inline const HumidityStruct* internal_default_instance() {
    return default_instance_;
  }
  #endif

  void Swap(HumidityStruct* other);

  // implements Message ----------------------------------------------

  inline HumidityStruct* New() const { return New(NULL); }

  HumidityStruct* New(::google::protobuf::Arena* arena) const;
  void CheckTypeAndMergeFrom(const ::google::protobuf::MessageLite& from);
  void CopyFrom(const HumidityStruct& from);
  void MergeFrom(const HumidityStruct& from);
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
  void InternalSwap(HumidityStruct* other);
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

  // optional int32 sensorType = 1;
  void clear_sensortype();
  static const int kSensorTypeFieldNumber = 1;
  ::google::protobuf::int32 sensortype() const;
  void set_sensortype(::google::protobuf::int32 value);

  // optional int32 windowStart = 2;
  void clear_windowstart();
  static const int kWindowStartFieldNumber = 2;
  ::google::protobuf::int32 windowstart() const;
  void set_windowstart(::google::protobuf::int32 value);

  // optional int32 windowSize = 3;
  void clear_windowsize();
  static const int kWindowSizeFieldNumber = 3;
  ::google::protobuf::int32 windowsize() const;
  void set_windowsize(::google::protobuf::int32 value);

  // optional double low = 4;
  void clear_low();
  static const int kLowFieldNumber = 4;
  double low() const;
  void set_low(double value);

  // optional double high = 5;
  void clear_high();
  static const int kHighFieldNumber = 5;
  double high() const;
  void set_high(double value);

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

  // optional double extra7 = 12;
  void clear_extra7();
  static const int kExtra7FieldNumber = 12;
  double extra7() const;
  void set_extra7(double value);

  // optional double extra8 = 13;
  void clear_extra8();
  static const int kExtra8FieldNumber = 13;
  double extra8() const;
  void set_extra8(double value);

  // optional double extra9 = 14;
  void clear_extra9();
  static const int kExtra9FieldNumber = 14;
  double extra9() const;
  void set_extra9(double value);

  // optional double extra10 = 15;
  void clear_extra10();
  static const int kExtra10FieldNumber = 15;
  double extra10() const;
  void set_extra10(double value);

  // @@protoc_insertion_point(class_scope:to.HumidityStruct)
 private:

  ::google::protobuf::internal::ArenaStringPtr _unknown_fields_;
  ::google::protobuf::Arena* _arena_ptr_;

  bool _is_default_instance_;
  ::google::protobuf::int32 sensortype_;
  ::google::protobuf::int32 windowstart_;
  double low_;
  double high_;
  double extra1_;
  double extra2_;
  double extra3_;
  double extra4_;
  double extra5_;
  double extra6_;
  double extra7_;
  double extra8_;
  double extra9_;
  double extra10_;
  ::google::protobuf::int32 windowsize_;
  mutable int _cached_size_;
  #ifdef GOOGLE_PROTOBUF_NO_STATIC_INITIALIZER
  friend void  protobuf_AddDesc_humidity_5fstruct_2eproto_impl();
  #else
  friend void  protobuf_AddDesc_humidity_5fstruct_2eproto();
  #endif
  friend void protobuf_AssignDesc_humidity_5fstruct_2eproto();
  friend void protobuf_ShutdownFile_humidity_5fstruct_2eproto();

  void InitAsDefaultInstance();
  static HumidityStruct* default_instance_;
};
// ===================================================================


// ===================================================================

#if !PROTOBUF_INLINE_NOT_IN_HEADERS
// HumidityStruct

// optional int32 sensorType = 1;
inline void HumidityStruct::clear_sensortype() {
  sensortype_ = 0;
}
inline ::google::protobuf::int32 HumidityStruct::sensortype() const {
  // @@protoc_insertion_point(field_get:to.HumidityStruct.sensorType)
  return sensortype_;
}
inline void HumidityStruct::set_sensortype(::google::protobuf::int32 value) {
  
  sensortype_ = value;
  // @@protoc_insertion_point(field_set:to.HumidityStruct.sensorType)
}

// optional int32 windowStart = 2;
inline void HumidityStruct::clear_windowstart() {
  windowstart_ = 0;
}
inline ::google::protobuf::int32 HumidityStruct::windowstart() const {
  // @@protoc_insertion_point(field_get:to.HumidityStruct.windowStart)
  return windowstart_;
}
inline void HumidityStruct::set_windowstart(::google::protobuf::int32 value) {
  
  windowstart_ = value;
  // @@protoc_insertion_point(field_set:to.HumidityStruct.windowStart)
}

// optional int32 windowSize = 3;
inline void HumidityStruct::clear_windowsize() {
  windowsize_ = 0;
}
inline ::google::protobuf::int32 HumidityStruct::windowsize() const {
  // @@protoc_insertion_point(field_get:to.HumidityStruct.windowSize)
  return windowsize_;
}
inline void HumidityStruct::set_windowsize(::google::protobuf::int32 value) {
  
  windowsize_ = value;
  // @@protoc_insertion_point(field_set:to.HumidityStruct.windowSize)
}

// optional double low = 4;
inline void HumidityStruct::clear_low() {
  low_ = 0;
}
inline double HumidityStruct::low() const {
  // @@protoc_insertion_point(field_get:to.HumidityStruct.low)
  return low_;
}
inline void HumidityStruct::set_low(double value) {
  
  low_ = value;
  // @@protoc_insertion_point(field_set:to.HumidityStruct.low)
}

// optional double high = 5;
inline void HumidityStruct::clear_high() {
  high_ = 0;
}
inline double HumidityStruct::high() const {
  // @@protoc_insertion_point(field_get:to.HumidityStruct.high)
  return high_;
}
inline void HumidityStruct::set_high(double value) {
  
  high_ = value;
  // @@protoc_insertion_point(field_set:to.HumidityStruct.high)
}

// optional double extra1 = 6;
inline void HumidityStruct::clear_extra1() {
  extra1_ = 0;
}
inline double HumidityStruct::extra1() const {
  // @@protoc_insertion_point(field_get:to.HumidityStruct.extra1)
  return extra1_;
}
inline void HumidityStruct::set_extra1(double value) {
  
  extra1_ = value;
  // @@protoc_insertion_point(field_set:to.HumidityStruct.extra1)
}

// optional double extra2 = 7;
inline void HumidityStruct::clear_extra2() {
  extra2_ = 0;
}
inline double HumidityStruct::extra2() const {
  // @@protoc_insertion_point(field_get:to.HumidityStruct.extra2)
  return extra2_;
}
inline void HumidityStruct::set_extra2(double value) {
  
  extra2_ = value;
  // @@protoc_insertion_point(field_set:to.HumidityStruct.extra2)
}

// optional double extra3 = 8;
inline void HumidityStruct::clear_extra3() {
  extra3_ = 0;
}
inline double HumidityStruct::extra3() const {
  // @@protoc_insertion_point(field_get:to.HumidityStruct.extra3)
  return extra3_;
}
inline void HumidityStruct::set_extra3(double value) {
  
  extra3_ = value;
  // @@protoc_insertion_point(field_set:to.HumidityStruct.extra3)
}

// optional double extra4 = 9;
inline void HumidityStruct::clear_extra4() {
  extra4_ = 0;
}
inline double HumidityStruct::extra4() const {
  // @@protoc_insertion_point(field_get:to.HumidityStruct.extra4)
  return extra4_;
}
inline void HumidityStruct::set_extra4(double value) {
  
  extra4_ = value;
  // @@protoc_insertion_point(field_set:to.HumidityStruct.extra4)
}

// optional double extra5 = 10;
inline void HumidityStruct::clear_extra5() {
  extra5_ = 0;
}
inline double HumidityStruct::extra5() const {
  // @@protoc_insertion_point(field_get:to.HumidityStruct.extra5)
  return extra5_;
}
inline void HumidityStruct::set_extra5(double value) {
  
  extra5_ = value;
  // @@protoc_insertion_point(field_set:to.HumidityStruct.extra5)
}

// optional double extra6 = 11;
inline void HumidityStruct::clear_extra6() {
  extra6_ = 0;
}
inline double HumidityStruct::extra6() const {
  // @@protoc_insertion_point(field_get:to.HumidityStruct.extra6)
  return extra6_;
}
inline void HumidityStruct::set_extra6(double value) {
  
  extra6_ = value;
  // @@protoc_insertion_point(field_set:to.HumidityStruct.extra6)
}

// optional double extra7 = 12;
inline void HumidityStruct::clear_extra7() {
  extra7_ = 0;
}
inline double HumidityStruct::extra7() const {
  // @@protoc_insertion_point(field_get:to.HumidityStruct.extra7)
  return extra7_;
}
inline void HumidityStruct::set_extra7(double value) {
  
  extra7_ = value;
  // @@protoc_insertion_point(field_set:to.HumidityStruct.extra7)
}

// optional double extra8 = 13;
inline void HumidityStruct::clear_extra8() {
  extra8_ = 0;
}
inline double HumidityStruct::extra8() const {
  // @@protoc_insertion_point(field_get:to.HumidityStruct.extra8)
  return extra8_;
}
inline void HumidityStruct::set_extra8(double value) {
  
  extra8_ = value;
  // @@protoc_insertion_point(field_set:to.HumidityStruct.extra8)
}

// optional double extra9 = 14;
inline void HumidityStruct::clear_extra9() {
  extra9_ = 0;
}
inline double HumidityStruct::extra9() const {
  // @@protoc_insertion_point(field_get:to.HumidityStruct.extra9)
  return extra9_;
}
inline void HumidityStruct::set_extra9(double value) {
  
  extra9_ = value;
  // @@protoc_insertion_point(field_set:to.HumidityStruct.extra9)
}

// optional double extra10 = 15;
inline void HumidityStruct::clear_extra10() {
  extra10_ = 0;
}
inline double HumidityStruct::extra10() const {
  // @@protoc_insertion_point(field_get:to.HumidityStruct.extra10)
  return extra10_;
}
inline void HumidityStruct::set_extra10(double value) {
  
  extra10_ = value;
  // @@protoc_insertion_point(field_set:to.HumidityStruct.extra10)
}

#endif  // !PROTOBUF_INLINE_NOT_IN_HEADERS

// @@protoc_insertion_point(namespace_scope)

}  // namespace to

// @@protoc_insertion_point(global_scope)

#endif  // PROTOBUF_humidity_5fstruct_2eproto__INCLUDED
