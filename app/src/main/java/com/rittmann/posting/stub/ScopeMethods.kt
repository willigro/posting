package com.rittmann.posting.stub

/*
* Source: http://marcoscezar.net/2018/07/04/metodos-escopo-kotlin/
* */
fun applyScopeMethod() {
    /*
    Ao executar no repel vai obter:

    targetObject
    tcejbOtegrat
    targetobject
    targetObject
*/

    "targetObject".apply {

        //Aqui novamente é impresso targetObject
        println(this)

    }.apply {

        //Aplicamos a operação de reversão que altera o objeto para tcejbOtegrat
        println(this.reversed())

    }.apply {

        //É possível observar que a configuração é propagada pelos blocos
        println(this.replace("O", "o"))
    }
}

fun alsoScopeMethod() {
    /*
    Se executar esse trecho no repl vai obter como saída:
    targetObject
    targetobject
    targetObject
*/
    "targetObject".also {
        //Aqui vai imprimir o bom e velho targetObject
        println(it)
    }.also {
        /*
         * Aqui o comando não tem nenhum efeito no resultado final desse bloco.
         */
        it.reversed()
    }.also {
        //Aqui vai imprimir targetobject
        println(it.replace("O", "o"))
    }
}

fun letScopeMethod() {
    /*
    Ao executar esse trecho no repl vai visualizar:
    targetObject
    tcejbOtegrat
*/
    "targetObject".let {
        /*
        Aqui vai exibir o valor do objeto alvo.
        Notem a variável it que por convenção é o objeto passado por parâmetro para o lambda
        */
        println(it)

        //Ao final do bloco o objeto referenciado por it é retornado
        it.reversed()
    }
}

fun runScopeMethod() {
    /*
    Se executar no repl de kotlin vai obter como resultado:
    targetObject
    TargeTObjecT
*/
    "targetObject".run {

        //Aqui imprime o valor da string
        println(this)

        //posso retornar um valor ao final do bloco nesse caso será TargeTObjecT
        this.replace("t", "T")
    }
}

fun withScopeMethod() {
    /*
  Resultado: Your Object
             YOUR OBJECT
*/
    with("Your Object") {

        //A string foi passada por parâmetro e será exibida na saída padrão
        println(this)

        //É possível também retornar um valor no bloco o último comando será retornado caso o comando retorne um valor
        this.toUpperCase()
    }
}