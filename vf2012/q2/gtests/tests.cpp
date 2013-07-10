#include <gtest/gtest.h>
#include "viete.h"

TEST(VieteTest, r1) {
  ASSERT_FLOAT_EQ(1.4142135623730951, viete<1>::r);
}

TEST(VieteTest, r2) {
  ASSERT_FLOAT_EQ(1.8477590650225735, viete<2>::r);
}

TEST(VieteTest, r3) {
  ASSERT_FLOAT_EQ(1.9615705608064609, viete<3>::r);
}

TEST(VieteTest, r4) {
  ASSERT_FLOAT_EQ(1.9903694533443939, viete<4>::r);
}

TEST(VieteTest, v1) {
  ASSERT_FLOAT_EQ(0.7071067811865476, viete<1>::v);
}

TEST(VieteTest, v2) {
  ASSERT_FLOAT_EQ(0.6532814824381883, viete<2>::v);
}

TEST(VieteTest, v3) {
  ASSERT_FLOAT_EQ(0.6407288619353766, viete<3>::v);
}

TEST(VieteTest, v4) {
  ASSERT_FLOAT_EQ(0.6376435773361455, viete<4>::v);
}
