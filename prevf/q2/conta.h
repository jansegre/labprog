struct Data {
  int ano, mes, dia;
  Data() : ano(0), mes(0), dia(0) {}
  Data(int ano, int mes=1, int dia=1) : ano(ano), mes(mes), dia(dia) {}
  void avanca_ano() { ano += 1; mes = 1; dia = 1; }
  void avanca_mes() { if (mes >= 12) avanca_ano(); else { mes += 1; dia = 1; } }
  // todos os meses tem 30 dias, isso não é problema pra se resolver nessa questão
  void avanca_dia() { if (dia >= 30) avanca_mes(); else dia += 1; }
  bool operator> (Data d) {
    if (ano == d.ano)
      if (mes == d.mes)
        return dia > d.dia;
      else
        return mes > d.mes;
    else
      return ano > d.ano;
  }
};

class Conta {
 public:
  Conta() : saldo(0.0) {}

  // data global para todas as contas
  static Data data;

  double getSaldo() { return saldo; }

  virtual bool deposita(double quantia) { saldo += quantia; return true; }
  virtual bool saca(double quantia) { saldo -= quantia; return true; }

  // juros quando o saldo está negativo
  virtual double getJurosn() = 0;

  // juros quando o saldo está positivo
  virtual double getJurosp() = 0;

  // calcula juros
  virtual void computa() { saldo += saldo > 0 ? getJurosp() : getJurosn(); ultima_computacao = data; }

 protected:
  double saldo;
  Data ultima_computacao;
};

class ContaCorrente : public Conta {
 public:
  // taxa de juros padrão de 0.5% ao dia
  ContaCorrente(double limite=0.0) : limite(limite < 0 ? limite : -limite), taxa_juros(0.005) {}

  void setTaxaJuros(double taxa) { taxa_juros = taxa; }

  // definindo implementacao da interface:
  bool saca(double quantia);
  double getJurosn();
  double getJurosp();
  void computa();

 private:
  double taxa_juros;
  double limite;
};

class Poupanca : public Conta {
 public:
  // taxa de juros padrão de 10% ao mes
  Poupanca(int aniversario=1) : aniversario(aniversario), cancela_juros(false), saldo_para_juros(0.0), taxa_juros(0.1) {}

  void setTaxaJuros(double taxa) { taxa_juros = taxa; }
  void setAniversario(int dia) { aniversario = dia; }

  // definindo implementacao da interface:
  bool saca(double quantia);
  double getJurosn();
  double getJurosp();
  void computa();

 private:
  double taxa_juros;
  // ao contrario do que o nome sugere esse aniversário não ocorre
  // anualmente porém mensalmente, é um inteiro que definie o dia do mes
  // em que deve ser calculado o rendimento dessa poupança
  int aniversario;
  // saldo usado para calcular próximos juros
  double saldo_para_juros;
  // define se irá ou não ser cancelado o juros naquele mes
  bool cancela_juros;
};
