Questão
=======

Construir uma classe abstrata chamada Conta. Esta classe define a interface de contas
bancárias, que se constitui das operações: deposita, saca, getSaldo, getJurosn (juros quando
saldo está negativo), getJurosp (juros quando o saldo está positivo) e computa (calcula juros).
Todas as funções membro são virtuais exceto o destrutor. Preste atenção nas especificações a
seguir. Da classe base abstrata descrita acima, crie duas classes concretas com as seguintes
propriedades:

- ContaCorrente:
  a) Neste tipo de conta as computações dos juros são feitas pelo banco diariamente.
  b) Permite taxa de juros diferente para saldos negativos e positivos.
  c) Possui um atributo menor que zero chamado limite. Saques que levem o saldo para abaixo
     deste valor são recusados. Esta definição não implica que o saldo tenha que estar sempre
     acima de limite. Ela só vale para saques do cliente, mas não para débitos por conta de juros
     cobrados sobre saldos negativos.
  d) O valor de limite é definido na criação da conta, isto é, sua instanciação.
  e) Fica claro que este tipo de conta permite saldos negativos.
  f) A taxa de juros para saldos positivos é zero ou seja, não há rendimento.

- Poupanca:
  a) Possui uma data de aniversário, sendo que só neste dia é que se computa juros
     mensalmente.
  b) Os juros acrescentados são referentes ao saldo após a última computação, isto significa que
     depósitos intermediários não rendem juros.
  c) Se houver algum saque que não seja no dia da computação, os juros referentes a aquele
     mês são cancelados.
  d) Só é permitido saldo maior ou igual a zero.


Comentários
===========

A parte mais dificil dessa questão foi decidir como serão computados os juros.
Não fica claro na questão o mecanismo que será usado para chamar o método `computa`,
nem de quem é a atribuição de chamar rotineiramente tal método, na conta corrente
dá a se entender que é uma responsabilidade externa, na `Poupanca` é passado uma
ideia que a biblioteca deve ter algum tipo de gerenciamento.
Não é uma boa ideia usar o relógio do sistema para computar os juros pois
fica muito dificil de testar, já que deveria se esperar um dia real para o
dia da conta mudar.
Para tanto decidi criar uma classe Data, com dia, mes a ano para auxiliar no gerenciamento
desse método e incluir na biblioteca a responsabilidade de garantir que não será
computado mais de uma vez o mesmo desconto.
