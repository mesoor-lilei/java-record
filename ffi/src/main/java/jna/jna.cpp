#include "jna.h"

// 使用 g++ 编译
// macOS 编译为 dylib
// `g++ jna.cpp -fPIC -shared -o ../../resources/darwin-aarch64/libjna.dylib`
// Linux 编译为 SO
// `g++ jna.cpp -fPIC -shared -o ../../resources/linux-x86-64/libjna.so`
int add(const int a, const int b) {
    return a + b;
}
