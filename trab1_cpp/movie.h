#ifndef MOVIE_H
#define MOVIE_H

#include <string>
#include <vector>
#include <fstream>

using std::string;
using std::vector;
using std::istream;
using std::ostream;


// Should add more genres
// these are merley stubs
enum Genre {
  NOGENRE,
  SCIFI,
  ROMANCE,
  DRAMA,
  ACTION,
  OTHER
};


// Movie will use this rating.
// DO NOT USE IT LIKE: Rating::AWESOME
// USE LIKE: AWESOME
enum Rating {
  NORATING,
  GARBAGE,
  BAD,
  AVERAGE,
  GOOD,
  AWESOME
};


// The long awaited movie class
// One should not pay much attention
// to privates, forget about them.
// This class is used to represent
// a movie, but that's obvious, right?
class Movie {
 public:
  // the constructors
  Movie() : genre_(NOGENRE), rating_(NORATING) {}
  Movie(string title, string title_ptbr, string director,
        Genre genre, Rating rating,
        vector<string> nominations=vector<string>(0))
    : title_(title), title_ptbr_(title_ptbr), director_(director),
      genre_(genre), rating_(rating), nominations_(nominations) {}

  // these are getters, they are pretty much
  // self explaning
  string title() { return title_; }
  string title_ptbr() { return title_ptbr_; }
  string director() { return director_; }
  vector<string> nominations() { return nominations_; }
  Genre genre() { return genre_; }
  Rating rating() { return rating_; }

  // dump the movie to a stream
  void Serialize(ostream &stream);

  // load the movie from a stream
  void Deserialize(istream &stream);

 int i;//TEMPORARY
 private:
  string title_;
  string title_ptbr_;
  string director_;
  enum Genre genre_;
  enum Rating rating_;
  vector<string> nominations_;
};

#endif
