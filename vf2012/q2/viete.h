// O primeiro passo é encontrar a fórmula de recursão,
// vamos fazer o cálculo em tempo de compilação usando
// templates, já que nada foi dito sobre como será
// entrado o `i` do i-ésimo termo.
//
// Isto é, vamos calcular apenas para `i`s passados por
// código para um template.
//
// Chamamaremos o i-ésimo termo de v(i) por enquanto.
//
// v(1) = sqrt(2) / 2
// v(2) = (sqrt(2) / 2) * (sqrt(2 + sqrt(2)) / 2)
//      = v(1) * sqrt(2 + sqrt(2)) / 2
// v(3) = v(2) * sqrt(2 + sqrt(2 + sqrt(2))) / 2
//
// Note que precisamos generalizar mais um termo, a parte
// que está dentro da raiz, note como esse termo se comporta:
//
// r(1) = sqrt(2)
// r(2) = sqrt(2 + sqrt(2)) = sqrt(2 + r(1))
// r(3) = sqrt(2 + sqrt(2 + sqrt(2))) = sqrt(2 + r(2))
//
// Assim podemos generalizar o termo r acima:
//
// r(1) = sqrt(2)
// r(n) = sqrt(2 + r(n - 1))
//
// Com esse termo podemos generalizar o termo de Viète:
//
// v(1) = r(1) / 2
// v(n) = v(n - 1) * r(n) / 2
//
// Assim podemos começar a implementar a solução:
//
#include <cmath>

// estrutura
template<unsigned n>
class viete {
 public:
  static const double r;
  static const double v;
};

// termos genérico
template<unsigned n> const double viete<n>::r = std::sqrt(2 + viete<n - 1>::r);
template<unsigned n> const double viete<n>::v = viete<n - 1>::v * viete<n>::r / 2;

// termos iniciais
template<> const double viete<1>::r = std::sqrt(2);
template<> const double viete<1>::v = std::sqrt(2) / 2;
