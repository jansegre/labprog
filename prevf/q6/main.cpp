#include "carro.h"
#include <iostream>

using namespace std;

int main() {
  Carro a, b;
  a.Abastecer(20.0);
  a.Mover(200);
  cout << "carro 1" << endl
       << "distancia percorrida: " << a.getKilometragem() << endl
       << "combustivel restante: " << a.getTanque() << endl;
  b.Abastecer(30.0);
  b.Mover(400);
  cout << "carro 2" << endl
       << "distancia percorrida: " << b.getKilometragem() << endl
       << "combustivel restante: " << b.getTanque() << endl;
  return 0;
}
