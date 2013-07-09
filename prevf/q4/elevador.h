class Elevador {
 public:

  void inicializa(int capacidade_, int total_) {
    capacidade = capacidade_;
    total = total_;
    atual = 0;
    pessoas = 0;
  }

  int getAtual() { return atual; }
  int getTotal() { return total; }
  int getCapacidade() { return capacidade; }
  int getPessoas() { return pessoas; }

  // os seguintes métodos retornam um booleano
  // indicando se a ação desejada pôde ser realizada
  bool entra();
  bool sai();
  bool sobe();
  bool desce();

 private:
  int atual, total, capacidade, pessoas;
};
