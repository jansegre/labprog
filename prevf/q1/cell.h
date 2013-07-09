template<class T>
class cell {
 protected:
  T info;
 public:
  void set(T x){ info = x; }
  T get() { return info; }
};

// a implementação dessa classe é feita inline por uma limitação do c++
// não é possível implementar um template em um cpp para formar uma lib
// separada pois o compilador precisa do tipo para fazer a lib, e o tipo
// só estará disponível quando a template for usada, que normalmente é
// feito pelo usuário da lib
template<typename T>
class colored_cell : public cell<T> {
 public:
  void set_color(char c) { color = c; }
  char get_color() { return color; }
  T get() {
    // note que o exercicio pede para retornar 0 se
    // a cor for branca ('w'), tal escolha obriga o
    // tipo T a possuir uma contrução implicita a
    // partir do 0, por exemplo, se tiver um construtor
    // que aceite 0 como parametro.
    if (color == 'w')
      return 0;
    else
      // poderia ser retornado info, no caso é preferível
      // manter a implementação original em vez de reimplementar
      // a mesma coisa, mesmo que isso custe uma chamada a mais.
      return cell<T>::get();
  }
 protected:
  char color;
};
