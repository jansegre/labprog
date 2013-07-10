#include <gtest/gtest.h>
#include "viete.h"

TEST(VieteTest, Termo0) {
  EXPECT_FLOAT_EQ(1.4142135623730951, viete<0>::r);
  EXPECT_FLOAT_EQ(0.7071067811865476, viete<0>::v);
}

TEST(VieteTest, Termo1) {
  EXPECT_FLOAT_EQ(1.8477590650225735, viete<1>::r);
  EXPECT_FLOAT_EQ(0.6532814824381883, viete<1>::v);
}

TEST(VieteTest, Termo2) {
  EXPECT_FLOAT_EQ(1.9615705608064609, viete<2>::r);
  EXPECT_FLOAT_EQ(0.6407288619353766, viete<2>::v);
}

TEST(VieteTest, Termo3) {
  EXPECT_FLOAT_EQ(1.9903694533443939, viete<3>::r);
  EXPECT_FLOAT_EQ(0.6376435773361455, viete<3>::v);
}
