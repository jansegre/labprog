#include "movie.h"
#include <cstdlib>

using std::atoi;

void Movie::Serialize(ostream &stream) {
  stream << title_ << ';'
         << title_ptbr_ << ';'
         << director_ << ';'
         << (int) genre_ << ';'
         << (int) rating_ << ';';

  for (int i = 0; i < nominations_.size(); ++i)
    stream << nominations_[i] << ';';

  stream << ';' << '.';
}

void Movie::Deserialize(istream &stream) {
  char c, temp[256];

  stream.get(temp, 256, ';');
  stream.get(c);// c must be equal to ';'
  title_ = temp;

  stream.get(temp, 256, ';');
  stream.get(c);// c must be equal to ';'
  title_ptbr_ = temp;

  stream.get(temp, 256, ';');
  stream.get(c);// c must be equal to ';'
  director_ = temp;

  stream.get(temp, 256, ';');
  stream.get(c);// c must be equal to ';'
  genre_ = (Genre)atoi(temp);

  stream.get(temp, 256, ';');
  stream.get(c);// c must be equal to ';'
  rating_ = (Rating)atoi(temp);

  int i;
  do {
    stream.get(temp, 256, ';');
    stream.get(c);
    string temp2(temp);
    i = temp2.size();
    if (i != 0)
      nominations_.push_back(temp2);
  } while (i != 0);
}
