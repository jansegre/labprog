#include <gtest/gtest.h>
#include "carro.h"

TEST(CarroTest, Construtor) {
  Carro c;
  EXPECT_EQ(0.0, c.getTanque());
  EXPECT_EQ(0.0, c.getKilometragem());
}

TEST(CarroTest, Abastecer) {
  Carro c;
  c.Abastecer(10.0);
  EXPECT_FLOAT_EQ(10.0, c.getTanque());
  c.Abastecer(10.0);
  EXPECT_FLOAT_EQ(20.0, c.getTanque());
  c.Abastecer(100.0);
  EXPECT_FLOAT_EQ(120.0, c.getTanque());
}

TEST(CarroTest, Mover) {
  Carro c;
  c.Abastecer(60.0);
  EXPECT_TRUE(c.Mover(120));
  EXPECT_FLOAT_EQ(120.0, c.getKilometragem());
  EXPECT_FLOAT_EQ(52.0, c.getTanque());
  EXPECT_TRUE(c.Mover(120));
  EXPECT_FLOAT_EQ(240.0, c.getKilometragem());
  EXPECT_FLOAT_EQ(44.0, c.getTanque());
  EXPECT_FALSE(c.Mover(1200));
  EXPECT_FLOAT_EQ(900.0, c.getKilometragem());
  EXPECT_FLOAT_EQ(0.0, c.getTanque());
}
