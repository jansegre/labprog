cmake_minimum_required(VERSION 2.8)

add_library(oscar_lib
    movie.h
    movie.cc
    catalog.h
    catalog.cc
)

add_executable(oscar main.cc)
target_link_libraries(oscar oscar_lib)

#find_package(Curses)
#if(CURSES_FOUND)
#    add_definitions(-DHAVE_CURSES)
#    include_directories(${CURSES_INCLUDE_DIR})
#    target_link_libraries(oscar ${CURSES_LIBRARIES})
#endif()

find_package(GTest)
if(GTEST_FOUND)
    include_directories(${GTEST_INCLUDE_DIR})
    add_executable(oscar_test
        test_main.cc
        movie_unittest.cc
        catalog_unittest.cc
    )
    target_link_libraries(oscar_test oscar_lib ${GTEST_BOTH_LIBRARIES})
endif()
