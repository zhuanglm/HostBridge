// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: calculated_hgb_result.proto

#ifndef PROTOBUF_calculated_5fhgb_5fresult_2eproto__INCLUDED
#define PROTOBUF_calculated_5fhgb_5fresult_2eproto__INCLUDED

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
void protobuf_AddDesc_calculated_5fhgb_5fresult_2eproto();
void protobuf_AssignDesc_calculated_5fhgb_5fresult_2eproto();
void protobuf_ShutdownFile_calculated_5fhgb_5fresult_2eproto();

class CalculatedHgbResult;

// ===================================================================

class CalculatedHgbResult : public ::google::protobuf::MessageLite /* @@protoc_insertion_point(class_definition:to.CalculatedHgbResult) */ {
 public:
  CalculatedHgbResult();
  virtual ~CalculatedHgbResult();

  CalculatedHgbResult(const CalculatedHgbResult& from);

  inline CalculatedHgbResult& operator=(const CalculatedHgbResult& from) {
    CopyFrom(from);
    return *this;
  }

  static const CalculatedHgbResult& default_instance();

  #ifdef GOOGLE_PROTOBUF_NO_STATIC_INITIALIZER
  // Returns the internal default instance pointer. This function can
  // return NULL thus should not be used by the user. This is intended
  // for Protobuf internal code. Please use default_instance() declared
  // above instead.
  static inline const CalculatedHgbResult* internal_default_instance() {
    return default_instance_;
  }
  #endif

  void Swap(CalculatedHgbResult* other);

  // implements Message ----------------------------------------------

  inline CalculatedHgbResult* New() const { return New(NULL); }

  CalculatedHgbResult* New(::google::protobuf::Arena* arena) const;
  void CheckTypeAndMergeFrom(const ::google::protobuf::MessageLite& from);
  void CopyFrom(const CalculatedHgbResult& from);
  void MergeFrom(const CalculatedHgbResult& from);
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
  void InternalSwap(CalculatedHgbResult* other);
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

  // optional int32 hgbResultType = 1;
  void clear_hgbresulttype();
  static const int kHgbResultTypeFieldNumber = 1;
  ::google::protobuf::int32 hgbresulttype() const;
  void set_hgbresulttype(::google::protobuf::int32 value);

  // optional float resultValue = 2;
  void clear_resultvalue();
  static const int kResultValueFieldNumber = 2;
  float resultvalue() const;
  void set_resultvalue(float value);

  // @@protoc_insertion_point(class_scope:to.CalculatedHgbResult)
 private:

  ::google::protobuf::internal::ArenaStringPtr _unknown_fields_;
  ::google::protobuf::Arena* _arena_ptr_;

  bool _is_default_instance_;
  ::google::protobuf::int32 hgbresulttype_;
  float resultvalue_;
  mutable int _cached_size_;
  #ifdef GOOGLE_PROTOBUF_NO_STATIC_INITIALIZER
  friend void  protobuf_AddDesc_calculated_5fhgb_5fresult_2eproto_impl();
  #else
  friend void  protobuf_AddDesc_calculated_5fhgb_5fresult_2eproto();
  #endif
  friend void protobuf_AssignDesc_calculated_5fhgb_5fresult_2eproto();
  friend void protobuf_ShutdownFile_calculated_5fhgb_5fresult_2eproto();

  void InitAsDefaultInstance();
  static CalculatedHgbResult* default_instance_;
};
// ===================================================================


// ===================================================================

#if !PROTOBUF_INLINE_NOT_IN_HEADERS
// CalculatedHgbResult

// optional int32 hgbResultType = 1;
inline void CalculatedHgbResult::clear_hgbresulttype() {
  hgbresulttype_ = 0;
}
inline ::google::protobuf::int32 CalculatedHgbResult::hgbresulttype() const {
  // @@protoc_insertion_point(field_get:to.CalculatedHgbResult.hgbResultType)
  return hgbresulttype_;
}
inline void CalculatedHgbResult::set_hgbresulttype(::google::protobuf::int32 value) {
  
  hgbresulttype_ = value;
  // @@protoc_insertion_point(field_set:to.CalculatedHgbResult.hgbResultType)
}

// optional float resultValue = 2;
inline void CalculatedHgbResult::clear_resultvalue() {
  resultvalue_ = 0;
}
inline float CalculatedHgbResult::resultvalue() const {
  // @@protoc_insertion_point(field_get:to.CalculatedHgbResult.resultValue)
  return resultvalue_;
}
inline void CalculatedHgbResult::set_resultvalue(float value) {
  
  resultvalue_ = value;
  // @@protoc_insertion_point(field_set:to.CalculatedHgbResult.resultValue)
}

#endif  // !PROTOBUF_INLINE_NOT_IN_HEADERS

// @@protoc_insertion_point(namespace_scope)

}  // namespace to

// @@protoc_insertion_point(global_scope)

#endif  // PROTOBUF_calculated_5fhgb_5fresult_2eproto__INCLUDED
