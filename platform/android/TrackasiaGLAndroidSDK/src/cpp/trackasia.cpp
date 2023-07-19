#include "trackasia.hpp"

namespace mbgl {
namespace android {

jni::Local<jni::Object<AssetManager>> Trackasia::getAssetManager(jni::JNIEnv& env) {
    static auto& javaClass = jni::Class<Trackasia>::Singleton(env);
    auto method = javaClass.GetStaticMethod<jni::Object<AssetManager>()>(env, "getAssetManager");
    return javaClass.Call(env, method);
}

jboolean Trackasia::hasInstance(jni::JNIEnv& env) {
    static auto& javaClass = jni::Class<Trackasia>::Singleton(env);
    auto method = javaClass.GetStaticMethod<jboolean()>(env, "hasInstance");
    return javaClass.Call(env, method);
}

void Trackasia::registerNative(jni::JNIEnv& env) {
    jni::Class<Trackasia>::Singleton(env);
}

} // namespace android
} // namespace mbgl
