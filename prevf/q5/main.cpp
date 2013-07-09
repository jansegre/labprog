#include "condicionador.h"
#include <iostream>

using namespace std;

int main() {
  Condicionador a, b;
  a.AlimentarSensor(25.0);
  a.DefinirPotencia(10);
  cout << a.TemperaturaResultante() << endl;
  b.AlimentarSensor(31);
  b.DefinirPotencia(5);
  cout << b.TemperaturaResultante() << endl;
  return 0;
}
