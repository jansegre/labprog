#include <gtest/gtest.h>
#include "cell.h"

TEST(ColoredCellTest, Int) {
  colored_cell<int> c;
  c.set(34);
  EXPECT_EQ(34, c.get());
  c.set_color('b');
  EXPECT_EQ(34, c.get());
  c.set_color('w');
  EXPECT_EQ(0, c.get());
}

TEST(ColoredCellTest, Float) {
  colored_cell<float> c;
  c.set(34.0);
  EXPECT_EQ(34.0, c.get());
  c.set_color('b');
  EXPECT_EQ(34.0, c.get());
  c.set_color('w');
  EXPECT_EQ(0, c.get());
}

TEST(ColoredCellTest, Double) {
  colored_cell<double> c;
  c.set(34.0);
  EXPECT_EQ(34.0, c.get());
  c.set_color('b');
  EXPECT_EQ(34.0, c.get());
  c.set_color('w');
  EXPECT_EQ(0, c.get());
}

TEST(ColoredCellTest, Char) {
  colored_cell<char> c;
  c.set('z');
  EXPECT_EQ('z', c.get());
  c.set_color('b');
  EXPECT_EQ('z', c.get());
  c.set_color('w');
  EXPECT_EQ(0, c.get());
}
