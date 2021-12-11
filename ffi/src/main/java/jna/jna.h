#ifndef JNA_LIB

#define JNA_LIB

/**
 * extern "C" 使用 C 函数命名规则
 *  __declspec(dllexport) 导出函数
 * Linux 不需要声明导出函数
 */
#ifdef _WIN32 // https://docs.microsoft.com/cpp/preprocessor/predefined-macros
extern "C" __declspec(dllexport) int add(int a, int b);
#else
extern "C" int add(int a, int b);
#endif

#endif
