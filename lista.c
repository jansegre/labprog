#include <stdio.h>
#include <stdlib.h>

void ex_1(int n) {
  int i;
  for (i = 1; i < n; ++i)
    if (i % 2 == 0)
      printf("%i\n", i);
}

void _ex_2(int i, int n) {
  if (i < n) {
    printf("%i\n", i);
    // return for allowing tail optimization
    return _ex_2(i + 2, n);
  }
}

void ex_2(int n) {
  return _ex_2(2, n);
}

void ex_3(char *filename) {
  FILE* f = fopen(filename, "r");
  int prod=1, sum=0, i;
  if (f == NULL) {
    fprintf(stderr, "Could not open file '%s'.\n", filename);
    return;
  }
  while (!feof(f)) {
    fscanf(f, "%d\n", &i);
    if (i % 2 == 0) {
      prod *= i;
    } else {
      sum += i;
    }
  }
  fclose(f);
  printf("%d\n%d\n", prod, sum);
}

void ex_4(char *filename) {
  FILE* f = fopen(filename, "r");
  double am=0.0, af=0.0, grade; // average male/female
  int nm=0, nf=0; // count male/female
  char sex;
  if (f == NULL) {
    fprintf(stderr, "Could not open file '%s'.\n", filename);
    return;
  }
  while (!feof(f)) {
    fscanf(f, "%c %lf\n", &sex, &grade);
    // note that since we're doing memory space optimization
    // by not storing every grade, the average's precision
    // will degrade over each iteration
    switch (sex) {
      case 'm':
      case 'M':
        am = (nm * am + grade) / (++nm);
        break;
      case 'f':
      case 'F':
        af = (nf * af + grade) / (++nf);
        break;
      default:
        fprintf(stderr, "Unidentified sex '%c'. Ignoring it.\n", sex);
    }
  }
  fclose(f);
  if (af > am) {
    printf("f %.2f\n", af);
  } else if (am > af) {
    printf("m %.2f\n", af);
  } else {
    printf("m/f %.2f\n", af);
  }
}

int _fibonacci(int n) {
  switch (n) {
    case 0:
    case 1:
      return 1;
    default:
      return _fibonacci(n - 1) + _fibonacci(n - 2);
  }
}

// note that the example from says fib(6) is 8, but it isn't
// fib(0) = fib(1) = 1, thus fib(6) is 13.
// if that definition is desired, it is a matter of calling
// _fibonacci(n - 1) instead.
void ex_5(int n) {
  printf("%d\n", _fibonacci(n));
}

void ex_6(int n) {
  int a=1, b=1, i;
  for (i = 0; i < n; ++i) {
    int c;
    c = a;
    a += b;
    b = c;
  }
  printf("%i\n", b);
}

// simple quick sort
void _sort(int n, int *vec) {
  int i=0, min_val=vec[0], min_i, temp;
  if (n == 0)
    return;
  for (i = 1; i < n; ++i) {
    if (vec[i] < min_val) {
      min_val = vec[i];
      min_i = i;
    }
  }
  temp = vec[0];
  vec[0] = min_val;
  vec[min_i] = temp;
  return _sort(n - 1, vec + 1);
}

void ex_7(int n) {
  int i, *v;
  v = (int *)malloc(n * sizeof(int));
  for (i = 0; i < n; ++i) {
    scanf("%i", &v[i]);
  }
  fflush(stdin);
  _sort(n, v);
  for (i = 0; i < n - 1; ++i) {
    printf("%i ", v[i]);
  }
  printf("%i\n", v[i]);
}

int main(int argv, char **args) {
  if (argv > 1) {
    int n = atoi(args[1]);

    printf("exercicio 1\n");
    ex_1(n);

    printf("exercicio 2\n");
    ex_2(n);

    printf("exercicio 3\n");
    ex_3("file.txt");

    printf("exercicio 4\n");
    ex_4("notas.txt");

    printf("exercicio 5\n");
    ex_5(n);

    printf("exercicio 6\n");
    ex_6(n);

    printf("exercicio 7\n");
    ex_7(n);

    return 0;
  } else {
    fprintf(stderr, "Needs at least one argument\n");
    return 1;
  }
}
