Questão
=======

A fórmula de Viète, mostrada abaixo, é um produto infinito de uma
série de termos que pode ser utilizada para calcular o valor de PI.

2/pi = (sqrt(2) / 2) * (sqrt(2 + sqrt(2)) / 2) * (sqrt(2 + sqrt(2 + sqrt(2))) / 2) * ...

Implemente o cálculo do i-ésimo termo da série de Viète utilizando apenas
modelos, especializações de modelos e variáveis de classe (sem nenhum método).

Comentários
===========

Em `modelo` leia-se `template`.

Ao compilar com o clang se o termo `viete<n>::r` não for chamado explicitamente
ele irá inferir o valor 0 ao calcular o `viete<n>::v` mesmo que esse referencie
o primeiro. Tal anomalia não ocorre ao compilar com o gcc, não foram testados
outros compiladores.

Note também que o método adotado (fazer com que o compilador faça a conta) implica
num limite do i, no caso do clang por padrão a profundidade da recursão pode ir até
512 níveis, no caso do gcc 4.8 a recursão por padrão pode ir até 900 níveis.
