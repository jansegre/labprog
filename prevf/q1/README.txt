Questão
=======

Consider the following template definition in C++:

    template <class T> class cell {
     protected:
      T info;
     public:
      void set(T x){ info = x; }
      T get() { return info; }
    };

Define the subclass colored_cell by extending the class cell with:

- a field color, indicating the color of the cell, represented as a
  character ("w" for white, "b" for black, "r" for red, etc.),
- the method set_color, which set the content of the field color,
- the method get_color, which returns the content of the field color, and
- an updated method get, which returns the content of the field info
  if the color is not white, and returns 0 otherwise.

Choosing the right signature, types and parameters is part of the exercise.
