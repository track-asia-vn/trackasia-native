if(NOT TARGET boringssl::crypto_static)
add_library(boringssl::crypto_static STATIC IMPORTED)
set_target_properties(boringssl::crypto_static PROPERTIES
    IMPORTED_LOCATION "/Users/sangnguyen/.gradle/caches/8.8/transforms/108171cef65a1d4ef847ab0b0f476dd1/transformed/boringssl-4.0/prefab/modules/crypto_static/libs/android.x86/libcrypto_static.a"
    INTERFACE_INCLUDE_DIRECTORIES "/Users/sangnguyen/.gradle/caches/8.8/transforms/108171cef65a1d4ef847ab0b0f476dd1/transformed/boringssl-4.0/prefab/modules/crypto_static/include"
    INTERFACE_LINK_LIBRARIES ""
)
endif()

if(NOT TARGET boringssl::ssl_static)
add_library(boringssl::ssl_static STATIC IMPORTED)
set_target_properties(boringssl::ssl_static PROPERTIES
    IMPORTED_LOCATION "/Users/sangnguyen/.gradle/caches/8.8/transforms/108171cef65a1d4ef847ab0b0f476dd1/transformed/boringssl-4.0/prefab/modules/ssl_static/libs/android.x86/libssl_static.a"
    INTERFACE_INCLUDE_DIRECTORIES "/Users/sangnguyen/.gradle/caches/8.8/transforms/108171cef65a1d4ef847ab0b0f476dd1/transformed/boringssl-4.0/prefab/modules/ssl_static/include"
    INTERFACE_LINK_LIBRARIES "boringssl::crypto_static"
)
endif()

