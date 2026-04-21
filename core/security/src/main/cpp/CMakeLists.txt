cmake_minimum_required(VERSION 3.22.1)
project("catalog-secrets")

add_library(
    catalog-secrets
    SHARED
    native-lib.cpp
)

find_library(
    log-lib
    log
)

target_link_libraries(
    catalog-secrets
    ${log-lib}
)
