#include <gtest/gtest.h>
#include <iostream>
#include "movie.h"

using std::stringstream;

TEST(MovieTest, DefaultConstructor) {
  Movie m = Movie();
  EXPECT_EQ("", m.title());
  EXPECT_EQ("", m.title_ptbr());
  EXPECT_EQ("", m.director());
  EXPECT_EQ(0, m.nominations().size());
  EXPECT_EQ(0, m.genre());
  EXPECT_EQ(0, m.rating());
}

TEST(MovieTest, Constructor) {
  vector<string> nms(0);
  nms.push_back("Oscar");
  nms.push_back("Mywards");
  nms.push_back("Blablabla");
  Movie m = Movie("The Blabla", "O Blabla", "John Doe", ACTION, GOOD, nms);
  EXPECT_EQ("The Blabla", m.title());
  EXPECT_EQ("O Blabla", m.title_ptbr());
  EXPECT_EQ("John Doe", m.director());
  EXPECT_EQ(ACTION, m.genre());
  EXPECT_EQ(GOOD, m.rating());
  EXPECT_EQ(3, m.nominations().size());
  EXPECT_EQ("Oscar", m.nominations()[0]);
  EXPECT_EQ("Mywards", m.nominations()[1]);
  EXPECT_EQ("Blablabla", m.nominations()[2]);
}

TEST(MovieTest, Serialization) {
  stringstream stream;
  vector<string> nms(0);
  nms.push_back("Oscar");
  nms.push_back("Mywards");
  nms.push_back("Blablabla");
  Movie n = Movie("The Blabla", "O Blabla", "John Doe", ACTION, GOOD, nms);
  n.Serialize(stream);
  Movie m;
  m.Deserialize(stream);
  EXPECT_EQ("The Blabla", m.title());
  EXPECT_EQ("O Blabla", m.title_ptbr());
  EXPECT_EQ("John Doe", m.director());
  EXPECT_EQ(ACTION, m.genre());
  EXPECT_EQ(GOOD, m.rating());
  ASSERT_EQ(3, m.nominations().size());
  EXPECT_EQ("Oscar", m.nominations()[0]);
  EXPECT_EQ("Mywards", m.nominations()[1]);
  EXPECT_EQ("Blablabla", m.nominations()[2]);
}
