#include <jni.h>
#include <string>

extern "C"
JNIEXPORT jstring JNICALL
Java_com_pascal_catalog_core_security_NativeSecrets_nativeBaseUrl(
        JNIEnv* env,
        jobject /* this */) {
    const char host[] = {'h','t','t','p','s',':','/','/','f','a','k','e','s','t','o','r','e','a','p','i','.','c','o','m','/'};
    std::string url(host, sizeof(host));
    return env->NewStringUTF(url.c_str());
}
