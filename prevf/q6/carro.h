#define KM_POR_LITRO 15.0

class Carro {
 public:
  Carro() : tanque(0.0), kilometragem(0.0) {}

  double getTanque() { return tanque; }
  double getKilometragem() { return kilometragem; }
  void Abastecer(double litros) { tanque += litros; }
  // retorna se foi possível andar tal distancia
  // se não anda até onde a gasolina alcançar.
  bool Mover(double kilometros) {
    double max_distancia = tanque * KM_POR_LITRO;
    if (kilometros >= max_distancia) {
      tanque = 0.0;
      kilometragem += max_distancia;
      return false;
    } else {
      tanque -= kilometros / KM_POR_LITRO;
      kilometragem += kilometros;
      return true;
    }
  }

 private:
  double tanque, kilometragem;
};
