#define MAX_POTENCIA 10
#define FATOR 1.8

class Condicionador {
 public:
  // a temperatura ambiente foi intencionalmente deixada não inicializada
  // pois não faz sentido inicializar com algum valor que não seja o do sensor
  Condicionador() : potencia(0) {}

  void AlimentarSensor(double temperatura) { temperatura_ambiente = temperatura; }
  void DefinirPotencia(int pot) { potencia = pot > MAX_POTENCIA ? MAX_POTENCIA : pot < 0 ? 0 : pot; }
  void Desligar() { potencia = 0; }
  double TemperaturaResultante() { return temperatura_ambiente - potencia * FATOR; }

 private:
  int potencia;
  double temperatura_ambiente;
};
