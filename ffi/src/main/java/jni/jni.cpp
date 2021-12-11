#include "com_example_jni_JniLibrary.h"

JNIEXPORT jint

JNICALL Java_com_example_jni_JniLibrary_add
        (JNIEnv *env, jclass clazz, jint a, jint b) {
    // 参数 1：class、参数 2：方法名、参数 3：方法返回类型签名（通过 `javap -s 类名称` 获取）
    jmethodID method = env->GetMethodID(clazz, "call", "()V");
    // 创建 class 实例
    jobject obj = env->AllocObject(clazz);
    // 调用方法
    env->CallVoidMethod(obj, method);

    jmethodID static_method = env->GetStaticMethodID(clazz, "call_text", "(Ljava/lang/String;)V");
    env->CallStaticVoidMethod(clazz, static_method, env->NewStringUTF("text"));
    return a + b;
}
