// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: compute_corrected_results_response.proto

#define INTERNAL_SUPPRESS_PROTOBUF_FIELD_DEPRECATION
#include "compute_corrected_results_response.pb.h"

#include <algorithm>

#include <google/protobuf/stubs/common.h>
#include <google/protobuf/stubs/port.h>
#include <google/protobuf/stubs/once.h>
#include <google/protobuf/io/coded_stream.h>
#include <google/protobuf/wire_format_lite_inl.h>
#include <google/protobuf/io/zero_copy_stream_impl_lite.h>
// @@protoc_insertion_point(includes)

namespace to {

void protobuf_ShutdownFile_compute_5fcorrected_5fresults_5fresponse_2eproto() {
  delete ComputeCorrectedResultsResponse::default_instance_;
}

#ifdef GOOGLE_PROTOBUF_NO_STATIC_INITIALIZER
void protobuf_AddDesc_compute_5fcorrected_5fresults_5fresponse_2eproto_impl() {
  GOOGLE_PROTOBUF_VERIFY_VERSION;

#else
void protobuf_AddDesc_compute_5fcorrected_5fresults_5fresponse_2eproto() GOOGLE_ATTRIBUTE_COLD;
void protobuf_AddDesc_compute_5fcorrected_5fresults_5fresponse_2eproto() {
  static bool already_here = false;
  if (already_here) return;
  already_here = true;
  GOOGLE_PROTOBUF_VERIFY_VERSION;

#endif
  ::to::protobuf_AddDesc_final_5fresult_2eproto();
  ComputeCorrectedResultsResponse::default_instance_ = new ComputeCorrectedResultsResponse();
  ComputeCorrectedResultsResponse::default_instance_->InitAsDefaultInstance();
  ::google::protobuf::internal::OnShutdown(&protobuf_ShutdownFile_compute_5fcorrected_5fresults_5fresponse_2eproto);
}

#ifdef GOOGLE_PROTOBUF_NO_STATIC_INITIALIZER
GOOGLE_PROTOBUF_DECLARE_ONCE(protobuf_AddDesc_compute_5fcorrected_5fresults_5fresponse_2eproto_once_);
void protobuf_AddDesc_compute_5fcorrected_5fresults_5fresponse_2eproto() {
  ::google::protobuf::GoogleOnceInit(&protobuf_AddDesc_compute_5fcorrected_5fresults_5fresponse_2eproto_once_,
                 &protobuf_AddDesc_compute_5fcorrected_5fresults_5fresponse_2eproto_impl);
}
#else
// Force AddDescriptors() to be called at static initialization time.
struct StaticDescriptorInitializer_compute_5fcorrected_5fresults_5fresponse_2eproto {
  StaticDescriptorInitializer_compute_5fcorrected_5fresults_5fresponse_2eproto() {
    protobuf_AddDesc_compute_5fcorrected_5fresults_5fresponse_2eproto();
  }
} static_descriptor_initializer_compute_5fcorrected_5fresults_5fresponse_2eproto_;
#endif

// ===================================================================

#if !defined(_MSC_VER) || _MSC_VER >= 1900
const int ComputeCorrectedResultsResponse::kErrorCodeFieldNumber;
const int ComputeCorrectedResultsResponse::kErrorMessageFieldNumber;
const int ComputeCorrectedResultsResponse::kCorrectedResultsFieldNumber;
#endif  // !defined(_MSC_VER) || _MSC_VER >= 1900

ComputeCorrectedResultsResponse::ComputeCorrectedResultsResponse()
  : ::google::protobuf::MessageLite(), _arena_ptr_(NULL) {
  SharedCtor();
  // @@protoc_insertion_point(constructor:to.ComputeCorrectedResultsResponse)
}

void ComputeCorrectedResultsResponse::InitAsDefaultInstance() {
  _is_default_instance_ = true;
}

ComputeCorrectedResultsResponse::ComputeCorrectedResultsResponse(const ComputeCorrectedResultsResponse& from)
  : ::google::protobuf::MessageLite(),
    _arena_ptr_(NULL) {
  SharedCtor();
  MergeFrom(from);
  // @@protoc_insertion_point(copy_constructor:to.ComputeCorrectedResultsResponse)
}

void ComputeCorrectedResultsResponse::SharedCtor() {
    _is_default_instance_ = false;
  ::google::protobuf::internal::GetEmptyString();
  _cached_size_ = 0;
  errorcode_ = 0;
  errormessage_.UnsafeSetDefault(&::google::protobuf::internal::GetEmptyStringAlreadyInited());
}

ComputeCorrectedResultsResponse::~ComputeCorrectedResultsResponse() {
  // @@protoc_insertion_point(destructor:to.ComputeCorrectedResultsResponse)
  SharedDtor();
}

void ComputeCorrectedResultsResponse::SharedDtor() {
  errormessage_.DestroyNoArena(&::google::protobuf::internal::GetEmptyStringAlreadyInited());
  #ifdef GOOGLE_PROTOBUF_NO_STATIC_INITIALIZER
  if (this != &default_instance()) {
  #else
  if (this != default_instance_) {
  #endif
  }
}

void ComputeCorrectedResultsResponse::SetCachedSize(int size) const {
  GOOGLE_SAFE_CONCURRENT_WRITES_BEGIN();
  _cached_size_ = size;
  GOOGLE_SAFE_CONCURRENT_WRITES_END();
}
const ComputeCorrectedResultsResponse& ComputeCorrectedResultsResponse::default_instance() {
#ifdef GOOGLE_PROTOBUF_NO_STATIC_INITIALIZER
  protobuf_AddDesc_compute_5fcorrected_5fresults_5fresponse_2eproto();
#else
  if (default_instance_ == NULL) protobuf_AddDesc_compute_5fcorrected_5fresults_5fresponse_2eproto();
#endif
  return *default_instance_;
}

ComputeCorrectedResultsResponse* ComputeCorrectedResultsResponse::default_instance_ = NULL;

ComputeCorrectedResultsResponse* ComputeCorrectedResultsResponse::New(::google::protobuf::Arena* arena) const {
  ComputeCorrectedResultsResponse* n = new ComputeCorrectedResultsResponse;
  if (arena != NULL) {
    arena->Own(n);
  }
  return n;
}

void ComputeCorrectedResultsResponse::Clear() {
// @@protoc_insertion_point(message_clear_start:to.ComputeCorrectedResultsResponse)
  errorcode_ = 0;
  errormessage_.ClearToEmptyNoArena(&::google::protobuf::internal::GetEmptyStringAlreadyInited());
  correctedresults_.Clear();
}

bool ComputeCorrectedResultsResponse::MergePartialFromCodedStream(
    ::google::protobuf::io::CodedInputStream* input) {
#define DO_(EXPRESSION) if (!GOOGLE_PREDICT_TRUE(EXPRESSION)) goto failure
  ::google::protobuf::uint32 tag;
  // @@protoc_insertion_point(parse_start:to.ComputeCorrectedResultsResponse)
  for (;;) {
    ::std::pair< ::google::protobuf::uint32, bool> p = input->ReadTagWithCutoff(127);
    tag = p.first;
    if (!p.second) goto handle_unusual;
    switch (::google::protobuf::internal::WireFormatLite::GetTagFieldNumber(tag)) {
      // optional int32 errorCode = 1;
      case 1: {
        if (tag == 8) {
          DO_((::google::protobuf::internal::WireFormatLite::ReadPrimitive<
                   ::google::protobuf::int32, ::google::protobuf::internal::WireFormatLite::TYPE_INT32>(
                 input, &errorcode_)));

        } else {
          goto handle_unusual;
        }
        if (input->ExpectTag(18)) goto parse_errorMessage;
        break;
      }

      // optional string errorMessage = 2;
      case 2: {
        if (tag == 18) {
         parse_errorMessage:
          DO_(::google::protobuf::internal::WireFormatLite::ReadString(
                input, this->mutable_errormessage()));
          DO_(::google::protobuf::internal::WireFormatLite::VerifyUtf8String(
            this->errormessage().data(), this->errormessage().length(),
            ::google::protobuf::internal::WireFormatLite::PARSE,
            "to.ComputeCorrectedResultsResponse.errorMessage"));
        } else {
          goto handle_unusual;
        }
        if (input->ExpectTag(26)) goto parse_correctedResults;
        break;
      }

      // repeated .to.FinalResult correctedResults = 3;
      case 3: {
        if (tag == 26) {
         parse_correctedResults:
          DO_(input->IncrementRecursionDepth());
         parse_loop_correctedResults:
          DO_(::google::protobuf::internal::WireFormatLite::ReadMessageNoVirtualNoRecursionDepth(
                input, add_correctedresults()));
        } else {
          goto handle_unusual;
        }
        if (input->ExpectTag(26)) goto parse_loop_correctedResults;
        input->UnsafeDecrementRecursionDepth();
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
  // @@protoc_insertion_point(parse_success:to.ComputeCorrectedResultsResponse)
  return true;
failure:
  // @@protoc_insertion_point(parse_failure:to.ComputeCorrectedResultsResponse)
  return false;
#undef DO_
}

void ComputeCorrectedResultsResponse::SerializeWithCachedSizes(
    ::google::protobuf::io::CodedOutputStream* output) const {
  // @@protoc_insertion_point(serialize_start:to.ComputeCorrectedResultsResponse)
  // optional int32 errorCode = 1;
  if (this->errorcode() != 0) {
    ::google::protobuf::internal::WireFormatLite::WriteInt32(1, this->errorcode(), output);
  }

  // optional string errorMessage = 2;
  if (this->errormessage().size() > 0) {
    ::google::protobuf::internal::WireFormatLite::VerifyUtf8String(
      this->errormessage().data(), this->errormessage().length(),
      ::google::protobuf::internal::WireFormatLite::SERIALIZE,
      "to.ComputeCorrectedResultsResponse.errorMessage");
    ::google::protobuf::internal::WireFormatLite::WriteStringMaybeAliased(
      2, this->errormessage(), output);
  }

  // repeated .to.FinalResult correctedResults = 3;
  for (unsigned int i = 0, n = this->correctedresults_size(); i < n; i++) {
    ::google::protobuf::internal::WireFormatLite::WriteMessage(
      3, this->correctedresults(i), output);
  }

  // @@protoc_insertion_point(serialize_end:to.ComputeCorrectedResultsResponse)
}

int ComputeCorrectedResultsResponse::ByteSize() const {
// @@protoc_insertion_point(message_byte_size_start:to.ComputeCorrectedResultsResponse)
  int total_size = 0;

  // optional int32 errorCode = 1;
  if (this->errorcode() != 0) {
    total_size += 1 +
      ::google::protobuf::internal::WireFormatLite::Int32Size(
        this->errorcode());
  }

  // optional string errorMessage = 2;
  if (this->errormessage().size() > 0) {
    total_size += 1 +
      ::google::protobuf::internal::WireFormatLite::StringSize(
        this->errormessage());
  }

  // repeated .to.FinalResult correctedResults = 3;
  total_size += 1 * this->correctedresults_size();
  for (int i = 0; i < this->correctedresults_size(); i++) {
    total_size +=
      ::google::protobuf::internal::WireFormatLite::MessageSizeNoVirtual(
        this->correctedresults(i));
  }

  GOOGLE_SAFE_CONCURRENT_WRITES_BEGIN();
  _cached_size_ = total_size;
  GOOGLE_SAFE_CONCURRENT_WRITES_END();
  return total_size;
}

void ComputeCorrectedResultsResponse::CheckTypeAndMergeFrom(
    const ::google::protobuf::MessageLite& from) {
  MergeFrom(*::google::protobuf::down_cast<const ComputeCorrectedResultsResponse*>(&from));
}

void ComputeCorrectedResultsResponse::MergeFrom(const ComputeCorrectedResultsResponse& from) {
// @@protoc_insertion_point(class_specific_merge_from_start:to.ComputeCorrectedResultsResponse)
  if (GOOGLE_PREDICT_FALSE(&from == this)) {
    ::google::protobuf::internal::MergeFromFail(__FILE__, __LINE__);
  }
  correctedresults_.MergeFrom(from.correctedresults_);
  if (from.errorcode() != 0) {
    set_errorcode(from.errorcode());
  }
  if (from.errormessage().size() > 0) {

    errormessage_.AssignWithDefault(&::google::protobuf::internal::GetEmptyStringAlreadyInited(), from.errormessage_);
  }
}

void ComputeCorrectedResultsResponse::CopyFrom(const ComputeCorrectedResultsResponse& from) {
// @@protoc_insertion_point(class_specific_copy_from_start:to.ComputeCorrectedResultsResponse)
  if (&from == this) return;
  Clear();
  MergeFrom(from);
}

bool ComputeCorrectedResultsResponse::IsInitialized() const {

  return true;
}

void ComputeCorrectedResultsResponse::Swap(ComputeCorrectedResultsResponse* other) {
  if (other == this) return;
  InternalSwap(other);
}
void ComputeCorrectedResultsResponse::InternalSwap(ComputeCorrectedResultsResponse* other) {
  std::swap(errorcode_, other->errorcode_);
  errormessage_.Swap(&other->errormessage_);
  correctedresults_.UnsafeArenaSwap(&other->correctedresults_);
  _unknown_fields_.Swap(&other->_unknown_fields_);
  std::swap(_cached_size_, other->_cached_size_);
}

::std::string ComputeCorrectedResultsResponse::GetTypeName() const {
  return "to.ComputeCorrectedResultsResponse";
}

#if PROTOBUF_INLINE_NOT_IN_HEADERS
// ComputeCorrectedResultsResponse

// optional int32 errorCode = 1;
void ComputeCorrectedResultsResponse::clear_errorcode() {
  errorcode_ = 0;
}
 ::google::protobuf::int32 ComputeCorrectedResultsResponse::errorcode() const {
  // @@protoc_insertion_point(field_get:to.ComputeCorrectedResultsResponse.errorCode)
  return errorcode_;
}
 void ComputeCorrectedResultsResponse::set_errorcode(::google::protobuf::int32 value) {
  
  errorcode_ = value;
  // @@protoc_insertion_point(field_set:to.ComputeCorrectedResultsResponse.errorCode)
}

// optional string errorMessage = 2;
void ComputeCorrectedResultsResponse::clear_errormessage() {
  errormessage_.ClearToEmptyNoArena(&::google::protobuf::internal::GetEmptyStringAlreadyInited());
}
 const ::std::string& ComputeCorrectedResultsResponse::errormessage() const {
  // @@protoc_insertion_point(field_get:to.ComputeCorrectedResultsResponse.errorMessage)
  return errormessage_.GetNoArena(&::google::protobuf::internal::GetEmptyStringAlreadyInited());
}
 void ComputeCorrectedResultsResponse::set_errormessage(const ::std::string& value) {
  
  errormessage_.SetNoArena(&::google::protobuf::internal::GetEmptyStringAlreadyInited(), value);
  // @@protoc_insertion_point(field_set:to.ComputeCorrectedResultsResponse.errorMessage)
}
 void ComputeCorrectedResultsResponse::set_errormessage(const char* value) {
  
  errormessage_.SetNoArena(&::google::protobuf::internal::GetEmptyStringAlreadyInited(), ::std::string(value));
  // @@protoc_insertion_point(field_set_char:to.ComputeCorrectedResultsResponse.errorMessage)
}
 void ComputeCorrectedResultsResponse::set_errormessage(const char* value, size_t size) {
  
  errormessage_.SetNoArena(&::google::protobuf::internal::GetEmptyStringAlreadyInited(),
      ::std::string(reinterpret_cast<const char*>(value), size));
  // @@protoc_insertion_point(field_set_pointer:to.ComputeCorrectedResultsResponse.errorMessage)
}
 ::std::string* ComputeCorrectedResultsResponse::mutable_errormessage() {
  
  // @@protoc_insertion_point(field_mutable:to.ComputeCorrectedResultsResponse.errorMessage)
  return errormessage_.MutableNoArena(&::google::protobuf::internal::GetEmptyStringAlreadyInited());
}
 ::std::string* ComputeCorrectedResultsResponse::release_errormessage() {
  // @@protoc_insertion_point(field_release:to.ComputeCorrectedResultsResponse.errorMessage)
  
  return errormessage_.ReleaseNoArena(&::google::protobuf::internal::GetEmptyStringAlreadyInited());
}
 void ComputeCorrectedResultsResponse::set_allocated_errormessage(::std::string* errormessage) {
  if (errormessage != NULL) {
    
  } else {
    
  }
  errormessage_.SetAllocatedNoArena(&::google::protobuf::internal::GetEmptyStringAlreadyInited(), errormessage);
  // @@protoc_insertion_point(field_set_allocated:to.ComputeCorrectedResultsResponse.errorMessage)
}

// repeated .to.FinalResult correctedResults = 3;
int ComputeCorrectedResultsResponse::correctedresults_size() const {
  return correctedresults_.size();
}
void ComputeCorrectedResultsResponse::clear_correctedresults() {
  correctedresults_.Clear();
}
const ::to::FinalResult& ComputeCorrectedResultsResponse::correctedresults(int index) const {
  // @@protoc_insertion_point(field_get:to.ComputeCorrectedResultsResponse.correctedResults)
  return correctedresults_.Get(index);
}
::to::FinalResult* ComputeCorrectedResultsResponse::mutable_correctedresults(int index) {
  // @@protoc_insertion_point(field_mutable:to.ComputeCorrectedResultsResponse.correctedResults)
  return correctedresults_.Mutable(index);
}
::to::FinalResult* ComputeCorrectedResultsResponse::add_correctedresults() {
  // @@protoc_insertion_point(field_add:to.ComputeCorrectedResultsResponse.correctedResults)
  return correctedresults_.Add();
}
::google::protobuf::RepeatedPtrField< ::to::FinalResult >*
ComputeCorrectedResultsResponse::mutable_correctedresults() {
  // @@protoc_insertion_point(field_mutable_list:to.ComputeCorrectedResultsResponse.correctedResults)
  return &correctedresults_;
}
const ::google::protobuf::RepeatedPtrField< ::to::FinalResult >&
ComputeCorrectedResultsResponse::correctedresults() const {
  // @@protoc_insertion_point(field_list:to.ComputeCorrectedResultsResponse.correctedResults)
  return correctedresults_;
}

#endif  // PROTOBUF_INLINE_NOT_IN_HEADERS

// @@protoc_insertion_point(namespace_scope)

}  // namespace to

// @@protoc_insertion_point(global_scope)
