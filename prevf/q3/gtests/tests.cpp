#include <gtest/gtest.h>
#include "empregados.h"

TEST(ComissionEmployeeTest, Salario) {
  ComissionEmployee ce;
  ce.taxa_de_comissao = 0.2;
  ce.venda_total = 15000.0;
  EXPECT_FLOAT_EQ(3000.0, ce.salario());
}

TEST(BasePlusComissionEmployeeTest, Salario) {
  BasePlusComissionEmployee ce;
  ce.taxa_de_comissao = 0.2;
  ce.venda_total = 15000.0;
  ce.salario_base = 2000.0;
  EXPECT_FLOAT_EQ(5000.0, ce.salario());
}
