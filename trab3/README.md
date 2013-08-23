Huffman
=======

Usando
------

Para compilar e gerar o `dist/huffman.jar`:

    ant

Também pode ser usada sua IDE preferida.

É suportada compressão e descompressão de arquivos texto e binário.

Também existe um modo iterativo para testes.

### Comprimindo um arquivo

    $ java -jar dist/huffman.jar -c arquivo.txt arquivo.txt.hz
    compressing 'arquivo.tzt' to 'arquivo.txt.hz'... ok

### Descomprimindo um arquivo

    $ java -jar dist/huffman.jar -d arquivo.txt.hz arquivo2.txt
    decompressing 'arquivo.tzt.hz' to 'arquivo2.txt'... ok

### Modo iterativo

Para um `arquivo.txt` com conteúdo:

    Lorem ipsum dolor sit amet, consectetur adipiscing elit.

Comprimir o `arquivo.txt` e ver a árvore gerada.

    $ java -jar dist/huffman.jar
    > compress arquivo.txt arquivo.txt.hz
    compressed to file.
    > comptree
    <HuffmanTree: 57 24 11 5 2 {110} 3 {99} 6 3 1 {44} 2 {117} 3 {109} 13 6 {105} 7 {32} 33 15 7 3 {114} 4 {115} 8 4 {111} 4 2 {112} 2 {108} 18 8 4 2 {100} 2 {97} 4 2 1 {76} 1 {10} 2 1 0 {null} 1 {103} 1 {46} 10 5 {101} 5 {116}>
    nullterm: 1101100
    00001010: 110101
    00100000: 011
    00101110: 110111
    00101100: 00100
    01001100: 110100
    01100111: 1101101
    01100100: 11000
    01100101: 1110
    01100011: 0001
    01100001: 11001
    01101110: 0000
    01101111: 1010
    01101100: 10111
    01101101: 0011
    01101001: 010
    01110101: 00101
    01110100: 1111
    01110011: 1001
    01110010: 1000
    01110000: 10110
    
    > q
    quitting... bye!

Descomprimir o `arquivo.txt.hz` anterior para a memória, cuidado ao fazer
isso com arquivos binários, nem todos os bytes são caractéres amigáveis.

    $ java -jar dist/huffman.jar
    > memdec arquivo.txt.hz
    decompressed to memory
    > print
    Lorem ipsum dolor sit amet, consectetur adipiscing elit.
    > q
    quiting... bye!
