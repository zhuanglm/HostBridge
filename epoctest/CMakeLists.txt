# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.4.1)

# Create protobuf-lite
find_package(Threads REQUIRED)
if (CMAKE_USE_PTHREADS_INIT)
  add_definitions(-DHAVE_PTHREAD)
endif (CMAKE_USE_PTHREADS_INIT)
set(protobuf_source_dir thirdparty/protobuf-3.0.0)
set(libprotobuf_lite_files
  ${protobuf_source_dir}/src/google/protobuf/arena.cc
  ${protobuf_source_dir}/src/google/protobuf/arenastring.cc
  ${protobuf_source_dir}/src/google/protobuf/extension_set.cc
  ${protobuf_source_dir}/src/google/protobuf/generated_message_util.cc
  ${protobuf_source_dir}/src/google/protobuf/io/coded_stream.cc
  ${protobuf_source_dir}/src/google/protobuf/io/zero_copy_stream.cc
  ${protobuf_source_dir}/src/google/protobuf/io/zero_copy_stream_impl_lite.cc
  ${protobuf_source_dir}/src/google/protobuf/message_lite.cc
  ${protobuf_source_dir}/src/google/protobuf/repeated_field.cc
  ${protobuf_source_dir}/src/google/protobuf/stubs/atomicops_internals_x86_gcc.cc
  ${protobuf_source_dir}/src/google/protobuf/stubs/atomicops_internals_x86_msvc.cc
  ${protobuf_source_dir}/src/google/protobuf/stubs/bytestream.cc
  ${protobuf_source_dir}/src/google/protobuf/stubs/common.cc
  ${protobuf_source_dir}/src/google/protobuf/stubs/int128.cc
  ${protobuf_source_dir}/src/google/protobuf/stubs/once.cc
  ${protobuf_source_dir}/src/google/protobuf/stubs/status.cc
  ${protobuf_source_dir}/src/google/protobuf/stubs/statusor.cc
  ${protobuf_source_dir}/src/google/protobuf/stubs/stringpiece.cc
  ${protobuf_source_dir}/src/google/protobuf/stubs/stringprintf.cc
  ${protobuf_source_dir}/src/google/protobuf/stubs/structurally_valid.cc
  ${protobuf_source_dir}/src/google/protobuf/stubs/strutil.cc
  ${protobuf_source_dir}/src/google/protobuf/stubs/time.cc
  ${protobuf_source_dir}/src/google/protobuf/wire_format_lite.cc
)

add_library(libprotobuf-lite
            SHARED
            ${libprotobuf_lite_files})
target_include_directories(libprotobuf-lite PUBLIC ${protobuf_source_dir}/src)

set(protobuf_generated_am_bgem_cpp_source_dir src/main/cpp/analyticalmanager/to)

set(protobuf_generated_files
    # AM BGEM Files
    ${protobuf_generated_am_bgem_cpp_source_dir}/bge_parameters.pb.cc
    ${protobuf_generated_am_bgem_cpp_source_dir}/calc_bge_request.pb.cc
    ${protobuf_generated_am_bgem_cpp_source_dir}/calc_bge_response.pb.cc
    ${protobuf_generated_am_bgem_cpp_source_dir}/check_for_early_injection_request.pb.cc
    ${protobuf_generated_am_bgem_cpp_source_dir}/check_for_early_injection_response.pb.cc
    ${protobuf_generated_am_bgem_cpp_source_dir}/compute_calculated_results_request.pb.cc
    ${protobuf_generated_am_bgem_cpp_source_dir}/compute_calculated_results_response.pb.cc
    ${protobuf_generated_am_bgem_cpp_source_dir}/compute_corrected_results_request.pb.cc
    ${protobuf_generated_am_bgem_cpp_source_dir}/compute_corrected_results_response.pb.cc
    ${protobuf_generated_am_bgem_cpp_source_dir}/final_result.pb.cc
    ${protobuf_generated_am_bgem_cpp_source_dir}/humidity_struct.pb.cc
    ${protobuf_generated_am_bgem_cpp_source_dir}/levels.pb.cc
    ${protobuf_generated_am_bgem_cpp_source_dir}/perform_realtime_qc_request.pb.cc
    ${protobuf_generated_am_bgem_cpp_source_dir}/perform_realtime_qc_response.pb.cc
    ${protobuf_generated_am_bgem_cpp_source_dir}/reading.pb.cc
    ${protobuf_generated_am_bgem_cpp_source_dir}/realtime_qc.pb.cc
    ${protobuf_generated_am_bgem_cpp_source_dir}/sensor_info.pb.cc
    ${protobuf_generated_am_bgem_cpp_source_dir}/sensor_readings.pb.cc
)
# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds them for you.
# Gradle automatically packages shared libraries with your APK.

# Compile library for use with Epocal's Host software
add_definitions(-DEPOCAL_TARGET_HOST)

add_library( # Sets the name of the library.
             analyticalmanager-lib

             # Sets the library as a shared library.
             SHARED

             # AM BGEM Files
             src/main/cpp/analyticalmanager/analyticalmanager.cpp
             src/main/cpp/analyticalmanager/analyticalmanager2.cpp
             src/main/cpp/analyticalmanager/analyticalmanager-lib.cpp
             src/main/cpp/analyticalmanager/MathHelp.cpp
             src/main/cpp/analyticalmanager/NeuralNetCoeff.cpp
             src/main/cpp/analyticalmanager/NeuralNetCoeffNode.cpp
             src/main/cpp/analyticalmanager/StringHelp.cpp
             src/main/cpp/analyticalmanager/AMSerializer.cpp
             src/main/cpp/analyticalmanager/amUtil_common.cpp
             src/main/cpp/analyticalmanager/amUtil_CheckForEarlyInjection.cpp
             src/main/cpp/analyticalmanager/amUtil_PerformRealTimeQC.cpp
             src/main/cpp/analyticalmanager/amUtil_CalculateBGE.cpp
             src/main/cpp/analyticalmanager/amUtil_ComputeCalculatedResults.cpp
             src/main/cpp/analyticalmanager/amUtil_ComputeCorrectedResults.cpp

             src/main/cpp/shared/amhelper.cpp
             src/main/cpp/shared/AMSerializerHelper.cpp

             # protocol buffer generated files
             ${protobuf_generated_files}
             )

include_directories( # Specifies a path to native header files.
                     src/main/cpp/analyticalmanager/include/
                     src/main/cpp/analyticalmanager/to/
                     src/main/cpp/shared/include/
                     ../common/src/main/cpp/include/
                     )

# Searches for a specified prebuilt library and stores the path as a
# variable. Because CMake includes system libraries in the search path by
# default, you only need to specify the name of the public NDK library
# you want to add. CMake verifies that the library exists before
# completing its build.

find_library( # Sets the name of the path variable.
              log-lib

              # Specifies the name of the NDK library that
              # you want CMake to locate.
              log )

# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in this
# build script, prebuilt third-party libraries, or system libraries.
target_link_libraries(libprotobuf-lite ${log-lib} ${CMAKE_THREAD_LIBS_INIT})
target_link_libraries( # Specifies the target library.
                       analyticalmanager-lib

                       # Links the target library to the log library
                       # included in the NDK.
                       ${log-lib}
                       libprotobuf-lite
                       ${CMAKE_THREAD_LIBS_INIT})