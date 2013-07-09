#include <string>
#include <iostream>

class ComissionEmployee {
 public:
  std::string primeiro_nome, ultimo_nome;
  int numero_do_seguro_social;
  double taxa_de_comissao, venda_total;

  double salario() { return taxa_de_comissao * venda_total; }
  void imprimir() {
    std::cout << "Nome: " << primeiro_nome << ' ' << ultimo_nome << std::endl
              << "Numero do seguro social: " << numero_do_seguro_social << std::endl
              << "Taxa de comissao: " << taxa_de_comissao << std::endl
              << "Venda total: " << venda_total << std::endl
              << "Salario: " << salario() << std::endl;
  }
};

class BasePlusComissionEmployee : public ComissionEmployee {
 public:
  double salario_base;

  double salario() { return salario_base + ComissionEmployee::salario(); }
};
