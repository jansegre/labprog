cmake_minimum_required(VERSION 2.8)

project(condicionador)
set(lib ${PROJECT_NAME}_lib)
set(app ${PROJECT_NAME})

include_directories(.)

#add_library(${lib} lib.h lib.cpp)
add_executable(${app} main.cpp)
#target_link_libraries(${app} ${lib})

find_package(GTest)
if(GTEST_FOUND)
    include_directories(${GTEST_INCLUDE_DIR})
    add_executable(gtests
        gtests/main.cpp
        gtests/tests.cpp
    )
    #target_link_libraries(gtests ${lib})
    target_link_libraries(gtests ${GTEST_BOTH_LIBRARIES})
endif()
