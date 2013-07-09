#include "conta.h"

Data Conta::data(2013);

bool ContaCorrente::saca(double quantia) {
  if (saldo - quantia < limite) return false;
  else return Conta::saca(quantia);
}

double ContaCorrente::getJurosn() {
  return saldo < 0.0 ? taxa_juros * saldo : 0.0;
}

double ContaCorrente::getJurosp() {
  return 0.0;
}

void ContaCorrente::computa() {
  if (data > ultima_computacao)
    Conta::computa();
}

bool Poupanca::saca(double quantia) {
  if (saldo < quantia) return false;
  else {
    // cancelar os juros se o dia não for o do aniversario
    if (data.dia != aniversario) cancela_juros = true;
    // fazer o saque mesmo assim
    return Conta::saca(quantia);
  }
}

double Poupanca::getJurosn() {
  return 0.0;
}

double Poupanca::getJurosp() {
  return taxa_juros * saldo_para_juros;
}

void Poupanca::computa() {
  if (data > ultima_computacao && data.dia == aniversario) {
    if (cancela_juros)
      ultima_computacao = data;
    else
      Conta::computa();
    saldo_para_juros = saldo;
  }
}
