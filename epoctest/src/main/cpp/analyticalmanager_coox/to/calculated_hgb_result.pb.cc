// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: calculated_hgb_result.proto

#define INTERNAL_SUPPRESS_PROTOBUF_FIELD_DEPRECATION
#include "calculated_hgb_result.pb.h"

#include <algorithm>

#include <google/protobuf/stubs/common.h>
#include <google/protobuf/stubs/port.h>
#include <google/protobuf/stubs/once.h>
#include <google/protobuf/io/coded_stream.h>
#include <google/protobuf/wire_format_lite_inl.h>
#include <google/protobuf/io/zero_copy_stream_impl_lite.h>
// @@protoc_insertion_point(includes)

namespace to {

void protobuf_ShutdownFile_calculated_5fhgb_5fresult_2eproto() {
  delete CalculatedHgbResult::default_instance_;
}

#ifdef GOOGLE_PROTOBUF_NO_STATIC_INITIALIZER
void protobuf_AddDesc_calculated_5fhgb_5fresult_2eproto_impl() {
  GOOGLE_PROTOBUF_VERIFY_VERSION;

#else
void protobuf_AddDesc_calculated_5fhgb_5fresult_2eproto() GOOGLE_ATTRIBUTE_COLD;
void protobuf_AddDesc_calculated_5fhgb_5fresult_2eproto() {
  static bool already_here = false;
  if (already_here) return;
  already_here = true;
  GOOGLE_PROTOBUF_VERIFY_VERSION;

#endif
  CalculatedHgbResult::default_instance_ = new CalculatedHgbResult();
  CalculatedHgbResult::default_instance_->InitAsDefaultInstance();
  ::google::protobuf::internal::OnShutdown(&protobuf_ShutdownFile_calculated_5fhgb_5fresult_2eproto);
}

#ifdef GOOGLE_PROTOBUF_NO_STATIC_INITIALIZER
GOOGLE_PROTOBUF_DECLARE_ONCE(protobuf_AddDesc_calculated_5fhgb_5fresult_2eproto_once_);
void protobuf_AddDesc_calculated_5fhgb_5fresult_2eproto() {
  ::google::protobuf::GoogleOnceInit(&protobuf_AddDesc_calculated_5fhgb_5fresult_2eproto_once_,
                 &protobuf_AddDesc_calculated_5fhgb_5fresult_2eproto_impl);
}
#else
// Force AddDescriptors() to be called at static initialization time.
struct StaticDescriptorInitializer_calculated_5fhgb_5fresult_2eproto {
  StaticDescriptorInitializer_calculated_5fhgb_5fresult_2eproto() {
    protobuf_AddDesc_calculated_5fhgb_5fresult_2eproto();
  }
} static_descriptor_initializer_calculated_5fhgb_5fresult_2eproto_;
#endif

// ===================================================================

#if !defined(_MSC_VER) || _MSC_VER >= 1900
const int CalculatedHgbResult::kHgbResultTypeFieldNumber;
const int CalculatedHgbResult::kResultValueFieldNumber;
#endif  // !defined(_MSC_VER) || _MSC_VER >= 1900

CalculatedHgbResult::CalculatedHgbResult()
  : ::google::protobuf::MessageLite(), _arena_ptr_(NULL) {
  SharedCtor();
  // @@protoc_insertion_point(constructor:to.CalculatedHgbResult)
}

void CalculatedHgbResult::InitAsDefaultInstance() {
  _is_default_instance_ = true;
}

CalculatedHgbResult::CalculatedHgbResult(const CalculatedHgbResult& from)
  : ::google::protobuf::MessageLite(),
    _arena_ptr_(NULL) {
  SharedCtor();
  MergeFrom(from);
  // @@protoc_insertion_point(copy_constructor:to.CalculatedHgbResult)
}

void CalculatedHgbResult::SharedCtor() {
    _is_default_instance_ = false;
  _cached_size_ = 0;
  hgbresulttype_ = 0;
  resultvalue_ = 0;
}

CalculatedHgbResult::~CalculatedHgbResult() {
  // @@protoc_insertion_point(destructor:to.CalculatedHgbResult)
  SharedDtor();
}

void CalculatedHgbResult::SharedDtor() {
  #ifdef GOOGLE_PROTOBUF_NO_STATIC_INITIALIZER
  if (this != &default_instance()) {
  #else
  if (this != default_instance_) {
  #endif
  }
}

void CalculatedHgbResult::SetCachedSize(int size) const {
  GOOGLE_SAFE_CONCURRENT_WRITES_BEGIN();
  _cached_size_ = size;
  GOOGLE_SAFE_CONCURRENT_WRITES_END();
}
const CalculatedHgbResult& CalculatedHgbResult::default_instance() {
#ifdef GOOGLE_PROTOBUF_NO_STATIC_INITIALIZER
  protobuf_AddDesc_calculated_5fhgb_5fresult_2eproto();
#else
  if (default_instance_ == NULL) protobuf_AddDesc_calculated_5fhgb_5fresult_2eproto();
#endif
  return *default_instance_;
}

CalculatedHgbResult* CalculatedHgbResult::default_instance_ = NULL;

CalculatedHgbResult* CalculatedHgbResult::New(::google::protobuf::Arena* arena) const {
  CalculatedHgbResult* n = new CalculatedHgbResult;
  if (arena != NULL) {
    arena->Own(n);
  }
  return n;
}

void CalculatedHgbResult::Clear() {
// @@protoc_insertion_point(message_clear_start:to.CalculatedHgbResult)
#if defined(__clang__)
#define ZR_HELPER_(f) \
  _Pragma("clang diagnostic push") \
  _Pragma("clang diagnostic ignored \"-Winvalid-offsetof\"") \
  __builtin_offsetof(CalculatedHgbResult, f) \
  _Pragma("clang diagnostic pop")
#else
#define ZR_HELPER_(f) reinterpret_cast<char*>(\
  &reinterpret_cast<CalculatedHgbResult*>(16)->f)
#endif

#define ZR_(first, last) do {\
  ::memset(&first, 0,\
           ZR_HELPER_(last) - ZR_HELPER_(first) + sizeof(last));\
} while (0)

  ZR_(hgbresulttype_, resultvalue_);

#undef ZR_HELPER_
#undef ZR_

}

bool CalculatedHgbResult::MergePartialFromCodedStream(
    ::google::protobuf::io::CodedInputStream* input) {
#define DO_(EXPRESSION) if (!GOOGLE_PREDICT_TRUE(EXPRESSION)) goto failure
  ::google::protobuf::uint32 tag;
  // @@protoc_insertion_point(parse_start:to.CalculatedHgbResult)
  for (;;) {
    ::std::pair< ::google::protobuf::uint32, bool> p = input->ReadTagWithCutoff(127);
    tag = p.first;
    if (!p.second) goto handle_unusual;
    switch (::google::protobuf::internal::WireFormatLite::GetTagFieldNumber(tag)) {
      // optional int32 hgbResultType = 1;
      case 1: {
        if (tag == 8) {
          DO_((::google::protobuf::internal::WireFormatLite::ReadPrimitive<
                   ::google::protobuf::int32, ::google::protobuf::internal::WireFormatLite::TYPE_INT32>(
                 input, &hgbresulttype_)));

        } else {
          goto handle_unusual;
        }
        if (input->ExpectTag(21)) goto parse_resultValue;
        break;
      }

      // optional float resultValue = 2;
      case 2: {
        if (tag == 21) {
         parse_resultValue:
          DO_((::google::protobuf::internal::WireFormatLite::ReadPrimitive<
                   float, ::google::protobuf::internal::WireFormatLite::TYPE_FLOAT>(
                 input, &resultvalue_)));

        } else {
          goto handle_unusual;
        }
        if (input->ExpectAtEnd()) goto success;
        break;
      }

      default: {
      handle_unusual:
        if (tag == 0 ||
            ::google::protobuf::internal::WireFormatLite::GetTagWireType(tag) ==
            ::google::protobuf::internal::WireFormatLite::WIRETYPE_END_GROUP) {
          goto success;
        }
        DO_(::google::protobuf::internal::WireFormatLite::SkipField(input, tag));
        break;
      }
    }
  }
success:
  // @@protoc_insertion_point(parse_success:to.CalculatedHgbResult)
  return true;
failure:
  // @@protoc_insertion_point(parse_failure:to.CalculatedHgbResult)
  return false;
#undef DO_
}

void CalculatedHgbResult::SerializeWithCachedSizes(
    ::google::protobuf::io::CodedOutputStream* output) const {
  // @@protoc_insertion_point(serialize_start:to.CalculatedHgbResult)
  // optional int32 hgbResultType = 1;
  if (this->hgbresulttype() != 0) {
    ::google::protobuf::internal::WireFormatLite::WriteInt32(1, this->hgbresulttype(), output);
  }

  // optional float resultValue = 2;
  if (this->resultvalue() != 0) {
    ::google::protobuf::internal::WireFormatLite::WriteFloat(2, this->resultvalue(), output);
  }

  // @@protoc_insertion_point(serialize_end:to.CalculatedHgbResult)
}

int CalculatedHgbResult::ByteSize() const {
// @@protoc_insertion_point(message_byte_size_start:to.CalculatedHgbResult)
  int total_size = 0;

  // optional int32 hgbResultType = 1;
  if (this->hgbresulttype() != 0) {
    total_size += 1 +
      ::google::protobuf::internal::WireFormatLite::Int32Size(
        this->hgbresulttype());
  }

  // optional float resultValue = 2;
  if (this->resultvalue() != 0) {
    total_size += 1 + 4;
  }

  GOOGLE_SAFE_CONCURRENT_WRITES_BEGIN();
  _cached_size_ = total_size;
  GOOGLE_SAFE_CONCURRENT_WRITES_END();
  return total_size;
}

void CalculatedHgbResult::CheckTypeAndMergeFrom(
    const ::google::protobuf::MessageLite& from) {
  MergeFrom(*::google::protobuf::down_cast<const CalculatedHgbResult*>(&from));
}

void CalculatedHgbResult::MergeFrom(const CalculatedHgbResult& from) {
// @@protoc_insertion_point(class_specific_merge_from_start:to.CalculatedHgbResult)
  if (GOOGLE_PREDICT_FALSE(&from == this)) {
    ::google::protobuf::internal::MergeFromFail(__FILE__, __LINE__);
  }
  if (from.hgbresulttype() != 0) {
    set_hgbresulttype(from.hgbresulttype());
  }
  if (from.resultvalue() != 0) {
    set_resultvalue(from.resultvalue());
  }
}

void CalculatedHgbResult::CopyFrom(const CalculatedHgbResult& from) {
// @@protoc_insertion_point(class_specific_copy_from_start:to.CalculatedHgbResult)
  if (&from == this) return;
  Clear();
  MergeFrom(from);
}

bool CalculatedHgbResult::IsInitialized() const {

  return true;
}

void CalculatedHgbResult::Swap(CalculatedHgbResult* other) {
  if (other == this) return;
  InternalSwap(other);
}
void CalculatedHgbResult::InternalSwap(CalculatedHgbResult* other) {
  std::swap(hgbresulttype_, other->hgbresulttype_);
  std::swap(resultvalue_, other->resultvalue_);
  _unknown_fields_.Swap(&other->_unknown_fields_);
  std::swap(_cached_size_, other->_cached_size_);
}

::std::string CalculatedHgbResult::GetTypeName() const {
  return "to.CalculatedHgbResult";
}

#if PROTOBUF_INLINE_NOT_IN_HEADERS
// CalculatedHgbResult

// optional int32 hgbResultType = 1;
void CalculatedHgbResult::clear_hgbresulttype() {
  hgbresulttype_ = 0;
}
 ::google::protobuf::int32 CalculatedHgbResult::hgbresulttype() const {
  // @@protoc_insertion_point(field_get:to.CalculatedHgbResult.hgbResultType)
  return hgbresulttype_;
}
 void CalculatedHgbResult::set_hgbresulttype(::google::protobuf::int32 value) {
  
  hgbresulttype_ = value;
  // @@protoc_insertion_point(field_set:to.CalculatedHgbResult.hgbResultType)
}

// optional float resultValue = 2;
void CalculatedHgbResult::clear_resultvalue() {
  resultvalue_ = 0;
}
 float CalculatedHgbResult::resultvalue() const {
  // @@protoc_insertion_point(field_get:to.CalculatedHgbResult.resultValue)
  return resultvalue_;
}
 void CalculatedHgbResult::set_resultvalue(float value) {
  
  resultvalue_ = value;
  // @@protoc_insertion_point(field_set:to.CalculatedHgbResult.resultValue)
}

#endif  // PROTOBUF_INLINE_NOT_IN_HEADERS

// @@protoc_insertion_point(namespace_scope)

}  // namespace to

// @@protoc_insertion_point(global_scope)
