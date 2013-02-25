#include <stdio.h>
#include <string.h>

int main(int args, char **argv) {
  char input[256], check[256], *filename="test.txt";
  FILE *f;

  // check for optional different filename
  if (args > 1) {
    filename = argv[1];
  }

  // getting input
  fgets(input, 256, stdin);

  // writing to file
  f = fopen(filename, "w");
  fputs(input, f);
  fclose(f);

  // reading from file
  f = fopen(filename, "r");
  fgets(check, 256, f);
  fclose(f);

  // checking
  if (strcmp(input, check) == 0) {
    fputs("OK! Everyting as expected.\n", stdout);
  } else {
    fprintf(stdout, "WOOPS! Something went wrong. File was '%s'.\n", filename);
  }

  return 0;
}
