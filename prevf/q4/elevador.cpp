#include "elevador.h"

bool Elevador::entra() {
  if (pessoas < capacidade) {
    ++pessoas;
    return true;
  } else {
    return false;
  }
}

bool Elevador::sai() {
  if (pessoas > 0) {
    --pessoas;
    return true;
  } else {
    return false;
  }
}

bool Elevador::sobe() {
  if (atual < total) {
    ++atual;
    return true;
  } else {
    return false;
  }
}

bool Elevador::desce() {
  if (atual > 0) {
    --atual;
    return true;
  } else {
    return false;
  }
}
