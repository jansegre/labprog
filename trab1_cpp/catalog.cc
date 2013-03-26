#include "catalog.h"
#include <sstream>
#include <string>
#include <iostream>

using std::endl;
using std::stringstream;

// Linked list structure
//
struct Node {
  Movie *movie;
  Node *next;

  Node(Movie *m=NULL) : movie(m), next(NULL) {}
};

struct IteratorAll : public Iterator {
  IteratorAll(Catalog *catalog) : catalog(catalog) {}

  Catalog *catalog;
  Node *node;

  virtual Movie *Next() {
    Node *n = node;
    if (node == NULL) return NULL;
    node = node->next;
    return n->movie;
  }
};

struct IteratorGenre : public Iterator {
  IteratorGenre(Catalog *catalog) : catalog(catalog) {}

  Catalog *catalog;
  Node *node;
  Genre genre;

  virtual Movie *Next() {
    Node *n = node;
    if (node == NULL) return NULL;
    node = node->next;
    if (n->movie->genre() == genre)
      return n->movie;
    else
      return Next();
  }
};

struct IteratorRating : public Iterator {
  IteratorRating(Catalog *catalog) : catalog(catalog) {}

  Catalog *catalog;
  Node *node;
  Rating min;
  Rating max;

  virtual Movie *Next() {
    Node *n = node;
    if (node == NULL) return NULL;
    node = node->next;
    if (n->movie->rating() >= min && n->movie->rating() <= max)
      return n->movie;
    else
      return Next();
  }
};

struct CatalogPimpl {
  int size;
  Node *origin;
  IteratorAll iter_all;
  IteratorGenre iter_genre;
  IteratorRating iter_rating;

  CatalogPimpl(Catalog *cat)
    : size(0),
    origin(NULL),
    iter_all(cat),
    iter_genre(cat),
    iter_rating(cat) {
  }

  ~CatalogPimpl() {
  }
};

Catalog::Catalog()
    : pimpl(new CatalogPimpl(this)) {
}

Catalog::~Catalog() {
  delete pimpl;
}

bool Catalog::Insert(Movie *movie) {
  if (pimpl->origin == NULL) {
    pimpl->origin = new Node(movie);
  } else {
    Node *node;
    for (Node *n = pimpl->origin; n != NULL; n = n->next) {
      node = n;
    }
    node->next = new Node(movie);
  }
  pimpl->size++;
  return true;
}

bool Catalog::Remove(Movie *movie) {
  if (pimpl->origin == NULL) {
    return false;
  } else {
    Node *node = NULL;
    for (Node *n = pimpl->origin; n != NULL; n = n->next) {
      if (n->movie == movie) {
        if (node == NULL) {
          pimpl->origin = n->next;
        } else {
          node->next = n->next;
        }
        pimpl->size--;
      }
      node = n;
    }
  }
  return true;
}

Iterator *Catalog::Iterate() {
  pimpl->iter_all.node = pimpl->origin;
  return &(pimpl->iter_all);
}

Iterator *Catalog::IterateGenre(Genre genre) {
  pimpl->iter_genre.node = pimpl->origin;
  pimpl->iter_genre.genre = genre;
  return &(pimpl->iter_genre);
}

Iterator *Catalog::IterateRating(Rating min, Rating max) {
  pimpl->iter_rating.node = pimpl->origin;
  pimpl->iter_rating.min = min;
  pimpl->iter_rating.max = max;
  return &(pimpl->iter_rating);
}

void Catalog::Serialize(ostream &stream) {
  if (pimpl->origin != NULL) {
    for(Node *n = pimpl->origin; n != NULL; n = n->next) {
      n->movie->Serialize(stream);
      stream << endl;
    }
  }
}

void Catalog::Deserialize(istream &stream) {
  string s;
  while (getline(stream, s)) {
    stringstream ss;
    ss << s;
    Movie *m = new Movie();
    m->Deserialize(ss);
    Insert(m);
  }
}

int Catalog::size() {
  return pimpl->size;
}
