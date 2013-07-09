#include <gtest/gtest.h>
#include "condicionador.h"

TEST(CondicionadorTest, AlimentarSensor) {
  Condicionador c;
  c.AlimentarSensor(15.0);
  EXPECT_FLOAT_EQ(15.0, c.TemperaturaResultante());
  c.AlimentarSensor(38.0);
  EXPECT_FLOAT_EQ(38.0, c.TemperaturaResultante());
}

TEST(CondicionadorTest, DefinirPotencia) {
  Condicionador c;
  c.AlimentarSensor(15.0);
  c.DefinirPotencia(10);
  EXPECT_FLOAT_EQ(-3.0, c.TemperaturaResultante());
  c.DefinirPotencia(1);
  EXPECT_FLOAT_EQ(13.2, c.TemperaturaResultante());
  c.DefinirPotencia(2);
  EXPECT_FLOAT_EQ(11.4, c.TemperaturaResultante());
}

TEST(CondicionadorTest, Desligar) {
  Condicionador c;
  c.AlimentarSensor(15.0);
  c.DefinirPotencia(10);
  EXPECT_FLOAT_EQ(-3.0, c.TemperaturaResultante());
  c.Desligar();
  EXPECT_FLOAT_EQ(15.0, c.TemperaturaResultante());
}
