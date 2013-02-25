#include <stdio.h>
#include <stdlib.h>

// Initial length of the buffer
#define INIT_BUFF_LEN 10
#define OUTPUT_FORMAT "AVG: %lf\n"

// This function will return the average
// of the values in the vector `vec` of
// size `n`
double average(int n, double *vec) {
  int i;
  double avg;
  for (i = 0; i < n; ++i)
    avg += vec[i];
  return avg / n;
}

int main(int argv, char **args) {
  int buff_len = INIT_BUFF_LEN, n = 0;
  double *grades;
  FILE *f = stdin;

  // Let's be nice and allow passing a file through command line
  if (argv > 1) {
    f = fopen(args[1], "r");
    if (f == NULL) {
      fprintf(stderr, "Fatal Error: Could not open file '%s'.", args[1]);
      return 1;
    }
  }

  // First we allocate the buffered vector
  grades =(double *)malloc(buff_len * sizeof(double));
  // Let's be friendly and warn if we don't have enough memory
  if (grades == NULL) {
    fprintf(stderr, "Fatal Error: Could not allocate initial memory.");
    return 2;
  }

  // Wait for input until it's over
  while(!feof(f)) {
    fscanf(f, "%lf", &grades[n++]);
    // If we hit our buffer size we have to
    // grow our buffer, as a simple technic
    // we simple double its size
    if (n >= buff_len) {
      buff_len *= 2;
      grades = (double *)realloc(grades, buff_len * sizeof(double));
      // Remember to halt if we cannot gain more memory
      if (grades == NULL) {
        fprintf(stderr, "Fatal Error: Could not allocate more memory!");
        return 2;
      }
    }
  }

  // Finally we print the average when input has finished
  // and return with no error
  fprintf(stdout, OUTPUT_FORMAT, average(n, grades));
  return 0;
}
