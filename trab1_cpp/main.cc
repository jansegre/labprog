#include <iostream>
#include <fstream>
#include <string>
#include "movie.h"
#include "catalog.h"
//#define ITERATE(m, c) Iterator *__i = c->Iterate(); for (Movie *m = __i->Next(); m != NULL; m = __i->Next())
#define LOOP(m, i) Iterator *__i = i; for (Movie *m = __i->Next(); m != NULL; m = __i->Next())

using namespace std;

// get an integer from user
// will be 0 on any error
int getopt() {
  int i;
  cout << "Escolha uma opcao: ";
  cin >> i;
  if (cin.fail())
    i = 0;
  return i;
}

// waits for a key press
void dummyopt() {
  string s;
  cout << "Pressione enter para continuar...";
  cin.ignore(numeric_limits<streamsize>::max(), '\n');
  getline(cin, s);
}

// prints short representation of a movie
void shortmovie(Movie *m) {
  cout << m->title() << endl;
}

string showgenre(Genre genre) {
  switch(genre) {
    case NOGENRE: return "Sem genero";
    case SCIFI: return "Sci-fi";
    case ROMANCE: return "Romance";
    case DRAMA: return "Drama";
    case ACTION: return "Acao";
    case OTHER: return "Outro";
  }
}

string showrating(Rating rating) {
  switch(rating) {
    case NORATING: return "Sem avaliacao";
    case GARBAGE: return "Terrivel";
    case BAD: return "Ruim";
    case AVERAGE: return "Moderado";
    case GOOD: return "Bom";
    case AWESOME: return "Incrivel";
  }
}

// prints full representation of a movie
void fullmovie(Movie *m) {
  cout << endl
       << "- Titulo Original: " << m->title() << endl
       << "- Titulo Traduzido: " << m->title_ptbr() << endl
       << "- Diretor: " << m->director() << endl
       << "- Genero: " << showgenre(m->genre()) << endl
       << "- Avaliacao: " << showrating(m->rating()) << endl
       << "- Indicacoes:" << endl;
  for (int i = 0; i < m->nominations().size(); ++i) {
    cout << "  - " << m->nominations()[i] << endl;
  }
  if (m->nominations().size() == 0) {
    cout << "  Nenhuma" << endl;
  }
}

Genre getgenre(const char *msg="Entre com o genero") {
  int genre = 0;
  cout << "Generos:" << endl;
  cout << "1) Sci-fi" << endl;
  cout << "2) Romance" << endl;
  cout << "3) Drama" << endl;
  cout << "4) Acao" << endl;
  cout << "5) Outro" << endl;
  cout << msg << ": "; cin >> genre;
  return (Genre)genre;
}

Rating getrating(const char *msg="Entre com a avaliacao") {
  int rating = 0;
  cout << "Avaliacao:" << endl;
  cout << "1) Terrivel" << endl;
  cout << "2) Ruim" << endl;
  cout << "3) Moderado" << endl;
  cout << "4) Bom" << endl;
  cout << "5) Incrivel" << endl;
  cout << msg << ": "; cin >> rating;
  return (Rating) rating;
}

// prompts user for a movie
Movie *getmovie() {
  string title, title_ptbr, director;
  vector<string> noms;
  Genre genre;
  Rating rating;

  cin.ignore(numeric_limits<streamsize>::max(), '\n');
  cout << "Entre com os seguintes campos:" << endl;
  cout << "Titulo original: "; getline(cin, title);
  cout << "Titulo traduzido: "; getline(cin, title_ptbr);
  cout << "Diretor: "; getline(cin, director);
  genre = getgenre();
  rating = getrating();
  cout << "Entre com as indicacoes:" << endl;
  cout << "(deixe em branco para terminar)" << endl;
  cin.ignore(numeric_limits<streamsize>::max(), '\n');
  int i;
  do {
    string nom;
    cout << "Indicacao: "; getline(cin, nom);
    i = nom.size();
    if (i != 0) {
      noms.push_back(nom);
    }
  } while (i != 0);
  return new Movie(title, title_ptbr, director, genre, rating, noms);
}

// User interface is written directly in portuguese.
// Should use GNU gettext, but that's for another time.
int main(int argc, char **argv) {
  int i, I = 1;
  Catalog *catalog = new Catalog();

  // welcome message
  cout << "Catalogo de Filmes" << endl;

  home: {
    i = 0;
    cout << endl;
    cout << "## Inicio" << endl;
    cout << i++ << ") Sair" << endl;
    cout << i++ << ") Listar filmes" << endl;
    cout << i++ << ") Inserir filmes" << endl;
    cout << i++ << ") Salvar para arquivo" << endl;
    cout << i++ << ") Carregar de arquivo" << endl;
    cout << i++ << ") Sobre" << endl;
    cout << i++ << ") Ajuda" << endl;
    cout << endl;

    switch(getopt()) {
      case 0: goto exit;
      case 1: goto list;
      case 2: goto add;
      case 3: goto save;
      case 4: goto load;
      case 5: goto about;
      case 6: goto help;
      default: return 1;
    }
  }

  help: {
    cout << endl
         << "Para navegar escolha o numero da opcao" << endl
         << "desejada e pressione enter." << endl
         << endl;
    dummyopt();
    goto home;
  }

  list: {
    i = 0;
    cout << endl;
    cout << "## Listar" << endl;
    cout << i++ << ") Inicio" << endl;
    cout << i++ << ") Todos" << endl;
    cout << i++ << ") Por genero" << endl;
    cout << i++ << ") Com avaliacao maior que" << endl;
    cout << i++ << ") Com avaliacao menor que" << endl;
    cout << i++ << ") Com avaliacao entre" << endl;
    cout << endl;

    switch(getopt()) {
      case 0: goto home;
      case 1: goto all;
      case 2: goto genre;
      case 3: goto minrating;
      case 4: goto maxrating;
      case 5: goto rating;
      default: return 1;
    }

    all: {
      int i = 0;
      Iterator *iter = catalog->Iterate();
      cout << endl << "----" << endl;
      for(Movie *m = iter->Next(); m != NULL; m = iter->Next()) {
        cout << m->i << ") ";
        shortmovie(m);
        i++;
      }
      if (i == 0) {
        cout << "Este catalog esta vazio." << endl;
        cout << "----" << endl;
        goto home;
      }
      cout << "----" << endl;
      goto more;
    }

    genre: {
      Genre genre = getgenre();
      int i = 0;
      Iterator *iter = catalog->IterateGenre(genre);
      cout << endl << "----" << endl;
      for(Movie *m = iter->Next(); m != NULL; m = iter->Next()) {
        cout << m->i << ") ";
        shortmovie(m);
        i++;
      }
      if (i == 0) {
        cout << "Nao ha filmes desse genero." << endl;
        cout << "----" << endl;
        goto home;
      }
      cout << "----" << endl;
      goto more;
    }

    minrating: {
      Rating rating = getrating("Entre com a avaliagao minima");
      int i = 0;
      cout << endl << "----" << endl;
      LOOP(m, catalog->IterateRating(rating)) {
        cout << m->i << ") ";
        shortmovie(m);
        i++;
      }
      if (i == 0) {
        cout << "Nao ha filmes com essa avaliacao." << endl;
        cout << "----" << endl;
        goto home;
      }
      cout << "----" << endl;
      goto more;
    }

    maxrating: {
      Rating rating = getrating("Entre com a avaliacao maxima");
      int i = 0;
      cout << endl << "----" << endl;
      LOOP(m, catalog->IterateRating(GARBAGE, rating)) {
        cout << m->i << ") ";
        shortmovie(m);
        i++;
      }
      if (i == 0) {
        cout << "Nao ha filmes com essa avaliacao." << endl;
        cout << "----" << endl;
        goto home;
      }
      cout << "----" << endl;
      goto more;
    }

    rating: {
      Rating minrating = getrating("Entre com a avaliacao minima");
      Rating maxrating = getrating("Entre com a avaliacao maxima");
      int i = 0;
      cout << endl << "----" << endl;
      LOOP(m, catalog->IterateRating(minrating, maxrating)) {
        cout << m->i << ") ";
        shortmovie(m);
        i++;
      }
      if (i == 0) {
        cout << "Nao ha filmes com essa avaliacao." << endl;
        cout << "----" << endl;
        goto home;
      }
      cout << "----" << endl;
      goto more;
    }

    more: {
    i = 0;
    cout << endl;
    cout << "## Acoes" << endl;
    cout << i++ << ") Inicio" << endl;
    cout << i++ << ") Voltar" << endl;
    cout << i++ << ") Ver Detalhes" << endl;
    cout << i++ << ") Remover" << endl;
    cout << endl;
    }

    switch(getopt()) {
      case 0: goto home;
      case 1: goto list;
      case 2: goto detail;
      case 3: goto remove;
      default: return 1;
    }

    detail: {
      int i, k = 0;
      cout << "Entre o id do filme: "; cin >> i;
      LOOP(movie, catalog->Iterate()) {
        if (movie->i == i) {
          fullmovie(movie);
          k++;
        }
      }
      if (k == 0) {
        cout << "Filme nao encontrado." << endl;
      }
      goto list;
    }

    remove: {
      int i, k = 0;
      cout << "Entre o id do filme a ser removido: "; cin >> i;
      LOOP(movie, catalog->Iterate()) {
        if (movie->i == i) {
          catalog->Remove(movie);
          k++;
        }
      }
      if (k == 0) {
        cout << "Filme nao encontrado." << endl;
      } else {
        cout << "Filme removido com sucesso!" << endl;
      }
      goto list;
    }
  }

  add: {
    Movie *m = getmovie();
    m->i = I++;
    catalog->Insert(m);
    cout << "Filme inserido com sucesso" << endl;
    goto home;
  }

  save: {
    string s;
    ofstream file;
    cout << "Entre o nome do arquivo: ";
    cin >> s;
    file.open(s.c_str());
    if (file.is_open()) {
      catalog->Serialize(file);
      file.close();
      cout << "Catalogo salvo com sucesso!" << endl;
    } else {
      cout << "Nao foi possivel abrir o arquivo para escrita." << endl;
    }
    goto home;
  }

  load: {
    string s;
    ifstream file;
    if (catalog->size() != 0) {
      char s;
      cout << "Ira apagar os filmes existentes!" << endl;
      cout << "Confirmar (S/N): "; cin >> s;
      switch(s) {
        case 's':
        case 'S':
        case 'y':
        case 'Y':
          break;
        default:
          goto home;
      }
      delete catalog;
      catalog = new Catalog();
      I = 1;
    }
    cout << "Entre o nome do arquivo: ";
    cin >> s;
    file.open(s.c_str());
    if (file.is_open()) {
      catalog->Deserialize(file);
      file.close();
      cout << "Catalogo carregado com sucesso!" << endl;
      LOOP(m, catalog->Iterate())
        m->i = I++;
    } else {
      cout << "Nao foi possivel abrir o arquivo para leitura." << endl;
    }
    goto home;
  }

  about: {
    cout << endl
         << "Elaborado para a cadeira de LabProg" << endl
         << "Autor: Jan Segre <jan@segre.in>" << endl
         << endl;
    dummyopt();
    goto home;
  }

  exit: {
    cout << endl
         << "Fechando catalogo de filmes." << endl;
  }
  return 0;
}
