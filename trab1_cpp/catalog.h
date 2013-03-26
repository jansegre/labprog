#ifndef CATALOG_H
#define CATALOG_H

#include <fstream>
#include "movie.h"

using std::istream;
using std::ostream;


// This class is used to iterate over
// a catalog of movies
// Call Next() on every iteration until
// it return NULL
class Iterator {
 public:
  virtual Movie *Next() = 0;
};


// Use this class to store a list
// of movies and query about them
class Catalog {
  friend class Iterator;

 public:
  // c'tors and d'tors
  Catalog();
  ~Catalog();

  // insert movie to the end of the list, returns true if successful
  // note that the lifespan of the movie will be dealt by the catalog
  // from now on, that means if the catalog is destructed so is the movie
  // unless it is removed
  bool Insert(Movie *movie);

  // remove the given movie
  // note that by removig the catalog will no longer deal with the movie
  // lifespan, that means one has to delete it manually after removing
  bool Remove(Movie *movie);

  // will return an iterator that allows running through all movies
  // by calling Next() the iterator will yield the next movie until
  // there are no movies in that case it will return NULL
  Iterator *Iterate();

  // works like Iterate() but only yields movies from the given genre
  Iterator *IterateGenre(Genre genre);

  // works like Iterate() but only yields movies within the given rating
  // range, naturally the range is inclusive
  Iterator *IterateRating(Rating min=GARBAGE, Rating max=AWESOME);

  // dump the database to a stream that may be a file with write perms
  void Serialize(ostream &stream);

  // load the database from a stream that may be a file with read perms
  void Deserialize(istream &stream);

  // size of the database
  int size();

 private:
  // private implementation
  struct CatalogPimpl *pimpl;
};

#endif
