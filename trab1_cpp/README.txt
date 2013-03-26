Trabalho do Oscar de LabProg
============================


Compilando
----------

É nescessário compilar os arquivos movie.cc, catalog.cc e main.cc apenas.

Os arquivos *_unittest.cc e main_test.cc são testes unitários e não fazem
parte da resolução do problema.

Porém se for desejável rodar os testes unitários é nescessário compilar a
biblioteca Google Test <https://code.google.com/p/googletest/> e usar seus
includes e linkar com os respectivos libs.

Os teste unitários foram implementados antes da biblioteca de catalogo como
forma de facilitar sua implementacao.

Se desejável pode ser usado o CMake para gerar o ambiente de compilação
para várias IDEs incluindo o CodeBlocks, Visual Studio, XCode ou Makefiles
entre outras.


Executando
----------

O executavel gerado apartir da main.cc é auto explicativo e possui um menu
interativo. Usando o CMake o executável irá se chamar oscar.exe ou oscar.

Os arquivos gerados são textuais e independente de plataforma.

O programa em questão foi testado apenas no Mac OS X 10.7 usando o compilador
Clang com o conjunto do LLVM, porém foi escrito usando apenas recursos
independentes de plataforma e portanto sua execução deve ser uniforme em
outras plataformas.


Licensa
-------

O conteúdo desse trabalho está no domínio público sem copyright. Mais
no arquivo UNLICENSE.txt ou no site <http://unlicense.org/>

