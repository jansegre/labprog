#include <gtest/gtest.h>
#include "elevador.h"

TEST(ElevadorTest, InicializaGet) {
  Elevador e;
  e.inicializa(5, 10);
  EXPECT_EQ(5, e.getCapacidade());
  EXPECT_EQ(10, e.getTotal());
  EXPECT_EQ(0, e.getAtual());
  EXPECT_EQ(0, e.getPessoas());
}

TEST(ElevadorTest, EntraSai) {
  Elevador e;
  e.inicializa(5, 0);
  EXPECT_FALSE(e.sai());
  EXPECT_FALSE(e.sai());
  EXPECT_TRUE(e.entra());
  EXPECT_TRUE(e.entra());
  EXPECT_EQ(2, e.getPessoas());
  EXPECT_TRUE(e.entra());
  EXPECT_TRUE(e.entra());
  EXPECT_EQ(4, e.getPessoas());
  EXPECT_TRUE(e.sai());
  EXPECT_EQ(3, e.getPessoas());
  EXPECT_TRUE(e.entra());
  EXPECT_TRUE(e.entra());
  EXPECT_EQ(5, e.getPessoas());
  EXPECT_FALSE(e.entra());
  EXPECT_FALSE(e.entra());
  EXPECT_FALSE(e.entra());
  EXPECT_TRUE(e.sai());
  EXPECT_TRUE(e.sai());
  EXPECT_EQ(3, e.getPessoas());
}

TEST(ElevadorTest, SobeDesce) {
  Elevador e;
  e.inicializa(0, 5);
  EXPECT_FALSE(e.desce());
  EXPECT_FALSE(e.desce());
  EXPECT_TRUE(e.sobe());
  EXPECT_TRUE(e.sobe());
  EXPECT_EQ(2, e.getAtual());
  EXPECT_TRUE(e.sobe());
  EXPECT_TRUE(e.sobe());
  EXPECT_EQ(4, e.getAtual());
  EXPECT_TRUE(e.desce());
  EXPECT_EQ(3, e.getAtual());
  EXPECT_TRUE(e.sobe());
  EXPECT_TRUE(e.sobe());
  EXPECT_EQ(5, e.getAtual());
  EXPECT_FALSE(e.sobe());
  EXPECT_FALSE(e.sobe());
  EXPECT_FALSE(e.sobe());
  EXPECT_TRUE(e.desce());
  EXPECT_TRUE(e.desce());
  EXPECT_EQ(3, e.getAtual());
}
