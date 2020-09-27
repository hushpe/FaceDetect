#ifndef Util_hpp
#define Util_hpp

#pragma once

#include <jni.h>
#include <string>
#include <chrono>

inline uint64_t getTS() {
    auto tpnow = std::chrono::system_clock::now();
    auto tpms = std::chrono::time_point_cast<std::chrono::milliseconds>(tpnow);
    uint64_t ts = tpms.time_since_epoch().count();
    return ts;
}

#endif
