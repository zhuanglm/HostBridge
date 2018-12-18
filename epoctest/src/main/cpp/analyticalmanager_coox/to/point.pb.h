// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: point.proto

#ifndef PROTOBUF_point_2eproto__INCLUDED
#define PROTOBUF_point_2eproto__INCLUDED

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
void protobuf_AddDesc_point_2eproto();
void protobuf_AssignDesc_point_2eproto();
void protobuf_ShutdownFile_point_2eproto();

class Point;

// ===================================================================

class Point : public ::google::protobuf::MessageLite /* @@protoc_insertion_point(class_definition:to.Point) */ {
 public:
  Point();
  virtual ~Point();

  Point(const Point& from);

  inline Point& operator=(const Point& from) {
    CopyFrom(from);
    return *this;
  }

  static const Point& default_instance();

  #ifdef GOOGLE_PROTOBUF_NO_STATIC_INITIALIZER
  // Returns the internal default instance pointer. This function can
  // return NULL thus should not be used by the user. This is intended
  // for Protobuf internal code. Please use default_instance() declared
  // above instead.
  static inline const Point* internal_default_instance() {
    return default_instance_;
  }
  #endif

  void Swap(Point* other);

  // implements Message ----------------------------------------------

  inline Point* New() const { return New(NULL); }

  Point* New(::google::protobuf::Arena* arena) const;
  void CheckTypeAndMergeFrom(const ::google::protobuf::MessageLite& from);
  void CopyFrom(const Point& from);
  void MergeFrom(const Point& from);
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
  void InternalSwap(Point* other);
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

  // optional float X = 1;
  void clear_x();
  static const int kXFieldNumber = 1;
  float x() const;
  void set_x(float value);

  // optional float Y = 2;
  void clear_y();
  static const int kYFieldNumber = 2;
  float y() const;
  void set_y(float value);

  // @@protoc_insertion_point(class_scope:to.Point)
 private:

  ::google::protobuf::internal::ArenaStringPtr _unknown_fields_;
  ::google::protobuf::Arena* _arena_ptr_;

  bool _is_default_instance_;
  float x_;
  float y_;
  mutable int _cached_size_;
  #ifdef GOOGLE_PROTOBUF_NO_STATIC_INITIALIZER
  friend void  protobuf_AddDesc_point_2eproto_impl();
  #else
  friend void  protobuf_AddDesc_point_2eproto();
  #endif
  friend void protobuf_AssignDesc_point_2eproto();
  friend void protobuf_ShutdownFile_point_2eproto();

  void InitAsDefaultInstance();
  static Point* default_instance_;
};
// ===================================================================


// ===================================================================

#if !PROTOBUF_INLINE_NOT_IN_HEADERS
// Point

// optional float X = 1;
inline void Point::clear_x() {
  x_ = 0;
}
inline float Point::x() const {
  // @@protoc_insertion_point(field_get:to.Point.X)
  return x_;
}
inline void Point::set_x(float value) {
  
  x_ = value;
  // @@protoc_insertion_point(field_set:to.Point.X)
}

// optional float Y = 2;
inline void Point::clear_y() {
  y_ = 0;
}
inline float Point::y() const {
  // @@protoc_insertion_point(field_get:to.Point.Y)
  return y_;
}
inline void Point::set_y(float value) {
  
  y_ = value;
  // @@protoc_insertion_point(field_set:to.Point.Y)
}

#endif  // !PROTOBUF_INLINE_NOT_IN_HEADERS

// @@protoc_insertion_point(namespace_scope)

}  // namespace to

// @@protoc_insertion_point(global_scope)

#endif  // PROTOBUF_point_2eproto__INCLUDED
