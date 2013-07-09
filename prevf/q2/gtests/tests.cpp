#include <gtest/gtest.h>
#include "conta.h"

TEST(ContaCorrenteTest, SaldoDeposito) {
  ContaCorrente cc;
  EXPECT_FLOAT_EQ(0.0, cc.getSaldo());
  EXPECT_TRUE(cc.deposita(150.0));
  EXPECT_FLOAT_EQ(150.0, cc.getSaldo());
  EXPECT_TRUE(cc.deposita(50.0));
  EXPECT_FLOAT_EQ(200.0, cc.getSaldo());
  EXPECT_TRUE(cc.deposita(50.0));
  EXPECT_FLOAT_EQ(250.0, cc.getSaldo());
}

TEST(ContaCorrenteTest, Saque) {
  ContaCorrente cc(-100.0);
  EXPECT_FLOAT_EQ(0.0, cc.getSaldo());
  EXPECT_TRUE(cc.deposita(150.0));
  EXPECT_TRUE(cc.saca(100.0));
  EXPECT_FLOAT_EQ(50.0, cc.getSaldo());
  EXPECT_TRUE(cc.saca(100.0));
  EXPECT_FLOAT_EQ(-50.0, cc.getSaldo());
  EXPECT_FALSE(cc.saca(100.0));
  EXPECT_FLOAT_EQ(-50.0, cc.getSaldo());
  EXPECT_TRUE(cc.saca(50.0));
  EXPECT_FLOAT_EQ(-100.0, cc.getSaldo());
  EXPECT_FALSE(cc.saca(1.0));
  EXPECT_FLOAT_EQ(-100.0, cc.getSaldo());
}

TEST(ContaCorrenteTest, Juros) {
  ContaCorrente cc(-50.0);
  EXPECT_FLOAT_EQ(0.0, cc.getJurosp());
  EXPECT_FLOAT_EQ(0.0, cc.getJurosn());
  cc.deposita(20.0);
  EXPECT_FLOAT_EQ(0.0, cc.getJurosp());
  EXPECT_FLOAT_EQ(0.0, cc.getJurosn());
  cc.saca(40.0);
  EXPECT_FLOAT_EQ(0.0, cc.getJurosp());
  EXPECT_FLOAT_EQ(-0.1, cc.getJurosn());  // 0.005 * 20.0 de juros
  cc.setTaxaJuros(0.01);
  EXPECT_FLOAT_EQ(0.0, cc.getJurosp());
  EXPECT_FLOAT_EQ(-0.2, cc.getJurosn());  // 0.01 * 20.0
}

TEST(ContaCorrenteTest, Computa) {
  ContaCorrente cc(-20.0);
  cc.saca(20.0);
  cc.setTaxaJuros(0.01);
  EXPECT_FLOAT_EQ(-0.2, cc.getJurosn());
  cc.computa();
  EXPECT_FLOAT_EQ(-20.2, cc.getSaldo());
  // não computar duas vezes no mesmo dia:
  cc.computa();
  EXPECT_FLOAT_EQ(-20.2, cc.getSaldo());
  // nem duas nem várias
  cc.computa();
  cc.computa();
  cc.computa();
  cc.computa();
  EXPECT_FLOAT_EQ(-20.2, cc.getSaldo());
  // continuar computando se o dia mudar
  cc.data.avanca_dia();
  cc.computa();
  EXPECT_FLOAT_EQ(-20.402, cc.getSaldo());
  cc.computa();
  EXPECT_FLOAT_EQ(-20.402, cc.getSaldo());
}

TEST(PoupancaTest, SaldoDeposito) {
  Poupanca p;
  EXPECT_FLOAT_EQ(0.0, p.getSaldo());
  EXPECT_TRUE(p.deposita(150.0));
  EXPECT_FLOAT_EQ(150.0, p.getSaldo());
  EXPECT_TRUE(p.deposita(50.0));
  EXPECT_FLOAT_EQ(200.0, p.getSaldo());
  EXPECT_TRUE(p.deposita(50.0));
  EXPECT_FLOAT_EQ(250.0, p.getSaldo());
}

TEST(PoupancaTest, Saque) {
  Poupanca p;
  EXPECT_FALSE(p.saca(10.0));
  EXPECT_FLOAT_EQ(0.0, p.getSaldo());
  EXPECT_TRUE(p.deposita(100.0));
  EXPECT_FLOAT_EQ(100.0, p.getSaldo());
  EXPECT_TRUE(p.saca(50.0));
  EXPECT_FLOAT_EQ(50.0, p.getSaldo());
  EXPECT_TRUE(p.saca(50.0));
  EXPECT_FLOAT_EQ(0.0, p.getSaldo());
  EXPECT_FALSE(p.saca(1.0));
  EXPECT_FLOAT_EQ(0.0, p.getSaldo());
}

TEST(PoupancaTest, Juros) {
  Poupanca p;
  EXPECT_FLOAT_EQ(0.0, p.getJurosp());
  EXPECT_FLOAT_EQ(0.0, p.getJurosn());
  p.deposita(80.0);
  // juros só devem contar após a ultima computação
  EXPECT_FLOAT_EQ(0.0, p.getJurosp());
  EXPECT_FLOAT_EQ(0.0, p.getJurosn());
  p.saca(30.0);
  EXPECT_FLOAT_EQ(0.0, p.getJurosp());
  EXPECT_FLOAT_EQ(0.0, p.getJurosn());
  EXPECT_FLOAT_EQ(50.0, p.getSaldo());
  // não deve alterar o saldo já que os juros são 0.0
  // para garantir que computar os juros deve funcionar
  // avançamos o mes para resetar o dia
  p.data.avanca_mes();
  p.computa();
  EXPECT_FLOAT_EQ(50.0, p.getSaldo());
  // agora devemos ter uma nova base para os juros:
  EXPECT_FLOAT_EQ(5.0, p.getJurosp());  // 0.1 * 50.0 de juros
  EXPECT_FLOAT_EQ(0.0, p.getJurosn());
  p.setTaxaJuros(0.2);
  EXPECT_FLOAT_EQ(10.0, p.getJurosp()); // 0.2 * 50.0
  EXPECT_FLOAT_EQ(0.0, p.getJurosn());
  // que deve permanecer inalterada mesmo com saques
  p.saca(30.0);
  EXPECT_FLOAT_EQ(10.0, p.getJurosp()); // 0.2 * 50.0
  EXPECT_FLOAT_EQ(0.0, p.getJurosn());
  // ou depósitos
  p.deposita(100.0);
  EXPECT_FLOAT_EQ(10.0, p.getJurosp()); // 0.2 * 50.0
  EXPECT_FLOAT_EQ(0.0, p.getJurosn());
}

TEST(PoupancaTest, Computa) {
  Poupanca p(3);
  p.data.avanca_mes();
  p.deposita(100.0);
  p.data.dia = 3;
  p.computa();
  // o primeiro mes nunca irá render pois o saldo anterior era 0.0
  EXPECT_FLOAT_EQ(100.0, p.getSaldo());
  // avancando para o proximo mes devemos ter o primeiro rendimento
  p.data.avanca_mes();
  p.data.dia = 3;
  p.computa();
  EXPECT_FLOAT_EQ(110.0, p.getSaldo()); // 100.0 + 100.0 * 0.1
  // próximas computações não devem fazer efeito
  p.computa();
  p.computa();
  p.computa();
  EXPECT_FLOAT_EQ(110.0, p.getSaldo()); // 100.0 + 100.0 * 0.1
  // até que o mês passe e o dia chegue
  p.data.avanca_mes();
  p.data.dia = 3;
  p.computa();
  EXPECT_FLOAT_EQ(121.0, p.getSaldo()); // 110.0 + 110.0 * 0.1
  // c) Se houver algum saque que não seja no dia da computação, os juros referentes a aquele mês são cancelados.
  // esse saque é válido:
  EXPECT_TRUE(p.saca(21.0));
  EXPECT_FLOAT_EQ(100.0, p.getSaldo());
  // mes que vem será computado:
  p.data.avanca_mes();
  p.data.dia = 3;
  p.computa();
  EXPECT_FLOAT_EQ(112.1, p.getSaldo()); // 100 + 121.0 * 0.1
  // no dia seguinte é feito um saque
  p.data.avanca_dia();
  EXPECT_TRUE(p.saca(12.1));
  EXPECT_FLOAT_EQ(100.0, p.getSaldo());
  // esse saque deve anular os juros daquele mes:
  p.data.avanca_mes();
  p.data.dia = 3;
  p.computa();
  EXPECT_FLOAT_EQ(100.0, p.getSaldo());
}
