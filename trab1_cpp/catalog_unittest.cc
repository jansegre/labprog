#include <gtest/gtest.h>
#include <iostream>
#include <fstream>
#include <cstdio>
#include "catalog.h"
#include "movie.h"

using std::stringstream;
using std::ifstream;
using std::ofstream;
using std::remove;

class CatalogTest : public testing::Test {
 protected:

  virtual void SetUp() {
    catalog = new Catalog();
  }

  Movie *foo_movie(string title="The Blabla") {
    vector<string> nms(0);
    nms.push_back("Oscar");
    nms.push_back("Mywards");
    nms.push_back("Blablabla");
    return new Movie(title, "O Blabla", "John Doe", ACTION, GOOD, nms);
  }

  Movie *foo_movie_genre(string title="The Blabla", Genre genre=NOGENRE) {
    vector<string> nms(0);
    nms.push_back("Oscar");
    nms.push_back("Mywards");
    nms.push_back("Blablabla");
    return new Movie(title, "O Blabla", "John Doe", genre, GOOD, nms);
  }

  Movie *foo_movie_rating(string title="The Blabla", Rating rating=NORATING) {
    vector<string> nms(0);
    nms.push_back("Oscar");
    nms.push_back("Mywards");
    nms.push_back("Blablabla");
    return new Movie(title, "O Blabla", "John Doe", ACTION, rating, nms);
  }

  Catalog *catalog;
};

TEST_F(CatalogTest, Constructor) {
  EXPECT_EQ(0, catalog->size());
}

TEST_F(CatalogTest, Insert) {
  EXPECT_EQ(0, catalog->size());

  EXPECT_TRUE(catalog->Insert(foo_movie()));
  EXPECT_EQ(1, catalog->size());

  EXPECT_TRUE(catalog->Insert(foo_movie()));
  EXPECT_EQ(2, catalog->size());
}

TEST_F(CatalogTest, Remove) {
  Movie *m1 = foo_movie();
  Movie *m2 = foo_movie();

  // We should be starting with nothing
  EXPECT_EQ(0, catalog->size());

  // Shuldn't be able to remove a non
  // existing movie
  EXPECT_FALSE(catalog->Remove(m1));
  EXPECT_EQ(0, catalog->size());

  // Should be ok to insert one
  // size shoul increase
  EXPECT_TRUE(catalog->Insert(m1));
  EXPECT_EQ(1, catalog->size());

  // Should be ok to remove that one
  // size should decrese
  EXPECT_TRUE(catalog->Remove(m1));
  EXPECT_EQ(0, catalog->size());

  // Should be ok to insert that
  // movie again
  EXPECT_TRUE(catalog->Insert(m1));
  EXPECT_EQ(1, catalog->size());

  // Insert another one
  EXPECT_TRUE(catalog->Insert(m2));
  EXPECT_EQ(2, catalog->size());

  // Remove the former
  EXPECT_TRUE(catalog->Remove(m1));
  EXPECT_EQ(1, catalog->size());

  // Remove the latter
  EXPECT_TRUE(catalog->Remove(m2));
  EXPECT_EQ(0, catalog->size());

  // Should still not be ok to remove
  // an unlisted movie
  EXPECT_FALSE(catalog->Remove(m1));
  EXPECT_EQ(0, catalog->size());

  // Should be ok to insert that
  // movie again
  EXPECT_TRUE(catalog->Insert(m1));
  EXPECT_EQ(1, catalog->size());

  // Insert another one
  EXPECT_TRUE(catalog->Insert(m2));
  EXPECT_EQ(2, catalog->size());

  // Remove the former
  EXPECT_TRUE(catalog->Remove(m2));
  EXPECT_EQ(1, catalog->size());

  // Remove the latter
  EXPECT_TRUE(catalog->Remove(m1));
  EXPECT_EQ(0, catalog->size());
}

TEST_F(CatalogTest, Iterate) {
  catalog->Insert(foo_movie("Lorem Ipsum"));
  catalog->Insert(foo_movie("Dolor sit Amet"));
  catalog->Insert(foo_movie("Nonsequitur"));

  Iterator *iter = catalog->Iterate();
  Movie *m;
  ASSERT_FALSE(iter == NULL);

  m = iter->Next();
  ASSERT_FALSE(m == NULL);
  EXPECT_EQ("Lorem Ipsum", m->title());

  m = iter->Next();
  ASSERT_FALSE(m == NULL);
  EXPECT_EQ("Dolor sit Amet", m->title());

  m = iter->Next();
  ASSERT_FALSE(m == NULL);
  EXPECT_EQ("Nonsequitur", m->title());

  m = iter->Next();
  ASSERT_TRUE(m == NULL);
}

TEST_F(CatalogTest, IterateGenre) {
  catalog->Insert(foo_movie_genre("Should Not Match"));
  catalog->Insert(foo_movie_genre("Shouldnt Match Either"));
  catalog->Insert(foo_movie_genre("Lorem Ipsum", ACTION));
  catalog->Insert(foo_movie_genre("The Enmatching Movie"));
  catalog->Insert(foo_movie_genre("Dolor sit Amet", ACTION));
  catalog->Insert(foo_movie_genre("Should Not Match", SCIFI));
  catalog->Insert(foo_movie_genre("Nonsequitur", ACTION));
  catalog->Insert(foo_movie_genre("Shouldnt Match Either", SCIFI));
  catalog->Insert(foo_movie_genre("The Enmatching Movie", SCIFI));

  Iterator *iter = catalog->IterateGenre(ACTION);
  Movie *m;
  ASSERT_FALSE(iter == NULL);

  m = iter->Next();
  ASSERT_FALSE(m == NULL);
  EXPECT_EQ("Lorem Ipsum", m->title());

  m = iter->Next();
  ASSERT_FALSE(m == NULL);
  EXPECT_EQ("Dolor sit Amet", m->title());

  m = iter->Next();
  ASSERT_FALSE(m == NULL);
  EXPECT_EQ("Nonsequitur", m->title());

  m = iter->Next();
  ASSERT_TRUE(m == NULL);
}

TEST_F(CatalogTest, IterateRating) {
  catalog->Insert(foo_movie_rating("Should Not Match"));
  catalog->Insert(foo_movie_rating("Shouldnt Match Either"));
  catalog->Insert(foo_movie_rating("Lorem Ipsum", BAD));
  catalog->Insert(foo_movie_rating("The Enmatching Movie"));
  catalog->Insert(foo_movie_rating("Dolor sit Amet", AVERAGE));
  catalog->Insert(foo_movie_rating("Should Not Match", AWESOME));
  catalog->Insert(foo_movie_rating("Nonsequitur", GOOD));
  catalog->Insert(foo_movie_rating("Shouldnt Match Either", GARBAGE));
  catalog->Insert(foo_movie_rating("The Enmatching Movie", AWESOME));

  Iterator *iter = catalog->IterateRating(BAD, GOOD);
  Movie *m;
  ASSERT_FALSE(iter == NULL);

  m = iter->Next();
  ASSERT_FALSE(m == NULL);
  EXPECT_EQ("Lorem Ipsum", m->title());

  m = iter->Next();
  ASSERT_FALSE(m == NULL);
  EXPECT_EQ("Dolor sit Amet", m->title());

  m = iter->Next();
  ASSERT_FALSE(m == NULL);
  EXPECT_EQ("Nonsequitur", m->title());

  m = iter->Next();
  EXPECT_EQ(NULL, m);

  m = iter->Next();
  EXPECT_EQ(NULL, m);
}

TEST_F(CatalogTest, Serialization) {
  stringstream stream;
  Movie *m;
  Iterator *iter;
  Catalog *c = new Catalog();

  catalog->Insert(foo_movie_rating("Should Not Match"));
  catalog->Insert(foo_movie_rating("Shouldnt Match Either"));
  catalog->Insert(foo_movie_rating("Lorem Ipsum", BAD));
  catalog->Insert(foo_movie_rating("The Enmatching Movie"));
  catalog->Insert(foo_movie_rating("Dolor sit Amet", AVERAGE));
  catalog->Insert(foo_movie_rating("Should Not Match", AWESOME));
  catalog->Insert(foo_movie_rating("Nonsequitur", GOOD));
  catalog->Insert(foo_movie_rating("Shouldnt Match Either", GARBAGE));
  catalog->Insert(foo_movie_rating("The Enmatching Movie", AWESOME));

  catalog->Serialize(stream);
  c->Deserialize(stream);

  ASSERT_EQ(9, c->size());

  iter = catalog->Iterate();

  m = iter->Next();
  ASSERT_FALSE(m == NULL);
  EXPECT_EQ("Should Not Match", m->title());
  m = iter->Next();
  ASSERT_FALSE(m == NULL);
  EXPECT_EQ("Shouldnt Match Either", m->title());
  m = iter->Next();
  ASSERT_FALSE(m == NULL);
  EXPECT_EQ("Lorem Ipsum", m->title());
  m = iter->Next();
  ASSERT_FALSE(m == NULL);
  EXPECT_EQ("The Enmatching Movie", m->title());
  m = iter->Next();
  ASSERT_FALSE(m == NULL);
  EXPECT_EQ("Dolor sit Amet", m->title());
  m = iter->Next();
  ASSERT_FALSE(m == NULL);
  EXPECT_EQ("Should Not Match", m->title());
  m = iter->Next();
  ASSERT_FALSE(m == NULL);
  EXPECT_EQ("Nonsequitur", m->title());
  m = iter->Next();
  ASSERT_FALSE(m == NULL);
  EXPECT_EQ("Shouldnt Match Either", m->title());
  m = iter->Next();
  ASSERT_FALSE(m == NULL);
  EXPECT_EQ("The Enmatching Movie", m->title());
  m = iter->Next();
  EXPECT_EQ(NULL, m);
  m = iter->Next();
  EXPECT_EQ(NULL, m);

  delete c;
  c = new Catalog();

  ofstream ofile;
  ifstream ifile;

  ofile.open("__test.txt");
  catalog->Serialize(ofile);
  ofile.close();

  ifile.open("__test.txt");
  c->Deserialize(ifile);
  ifile.close();
  remove("__test.txt");

  ASSERT_EQ(9, c->size());

  iter = catalog->Iterate();

  m = iter->Next();
  ASSERT_FALSE(m == NULL);
  EXPECT_EQ("Should Not Match", m->title());
  m = iter->Next();
  ASSERT_FALSE(m == NULL);
  EXPECT_EQ("Shouldnt Match Either", m->title());
  m = iter->Next();
  ASSERT_FALSE(m == NULL);
  EXPECT_EQ("Lorem Ipsum", m->title());
  m = iter->Next();
  ASSERT_FALSE(m == NULL);
  EXPECT_EQ("The Enmatching Movie", m->title());
  m = iter->Next();
  ASSERT_FALSE(m == NULL);
  EXPECT_EQ("Dolor sit Amet", m->title());
  m = iter->Next();
  ASSERT_FALSE(m == NULL);
  EXPECT_EQ("Should Not Match", m->title());
  m = iter->Next();
  ASSERT_FALSE(m == NULL);
  EXPECT_EQ("Nonsequitur", m->title());
  m = iter->Next();
  ASSERT_FALSE(m == NULL);
  EXPECT_EQ("Shouldnt Match Either", m->title());
  m = iter->Next();
  ASSERT_FALSE(m == NULL);
  EXPECT_EQ("The Enmatching Movie", m->title());
  m = iter->Next();
  EXPECT_EQ(NULL, m);
  m = iter->Next();
  EXPECT_EQ(NULL, m);

  delete c;
}
