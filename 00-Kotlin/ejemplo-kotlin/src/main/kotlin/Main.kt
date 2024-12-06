package org.example

import java.util.Date
import javax.swing.text.StyledEditorKit.BoldAction

fun main(args: Array<String>) {
    //inmutable no se reasigna "="
    val inmutable: String = "Adrian"
    //inmutable = "Vicente" //ERROR
    //mutables se pueden reasignar
    var mutable: String = "Vicente"
    mutable = "Adrian" //OK
    //val mayor que var

    //Duck Typing
    val ejemploVariable = "Adrain Eguez"
    ejemploVariable.trim()
    val edadEjemplo: Int = 12
    //ejemploVariable = edadEjemplo //ERROR
    // varibles primitivas
    val nombreProfesor: String = "Adrian Eguez"
    val sueldo: Double = 1.5
    val estadoCivil: Char = 'C'
    val mayorEdad: Boolean = true
    // clases en java
    val fechaNacimiento: Date = Date()

    //when (switch)
    val estadoCivilWhen = "C"
    when (estadoCivilWhen){
        ("C")->{
            println("Casado")
        }
        "S"->{
            println("Soltero")
        }
        else->{
            println("No sabemos")
        }
    }
    val esSoltero = (estadoCivilWhen == "S" )
    val coqueteo = if (esSoltero) "Si" else "No"
    imprimirNombre("ADrIanNa vIcENtE")

    //"Named parameters"
    calcularSueldo(10.00)
    calcularSueldo(10.00,15.00,20.00)
    calcularSueldo(10.00, bonoEspecial = 20.00)

    val sumaA= Suma(1,1)
    val sumaB= Suma(null,1)
    val sumaC= Suma(1,null)
    val sumaD= Suma(null,null)

    sumaA.sumar()
    sumaB.sumar()
    sumaC.sumar()
    sumaD.sumar()

    println(Suma.pi)
    println(Suma.elevarAlCuadrado(2))
    println(Suma.historialSumas)

    //arreglos
    //estaticos
    val arregloEstatico: Array<Int> = arrayOf<Int>(1,2,3)
    println(arregloEstatico)
    //dinamicos
    val  arregloDinamico: ArrayList<Int> = arrayListOf<Int>(1,2,3,4,5,6,7,8,9,10)
    println(arregloDinamico)
    arregloDinamico.add(11)
    arregloDinamico.add(12)
    println(arregloDinamico)

    //foreach
    //iterara un arrreglo
    val  respuestaForEach: Unit = arregloDinamico
        .forEach{valorActual:Int ->
            println("Valor actual: ${valorActual}");
        }
    //"it" "eso" significa el elemento iterado
    arregloDinamico.forEach{ println("Valor actual (it): ${it}") }
    //MAP -> muta(modifica o cambia) el arreglo
    //1) enviamos el nuevo valor de la iteracion
    //2) nos devuelve un nuevo arreglo con valores de las iterraciones
    val respuestaMap: List<Double> = arregloDinamico
        .map { valorActual: Int ->
            return@map valorActual.toDouble() + 100.00
        }
    println(respuestaMap)
    val respuestaMapDos = arregloDinamico.map { it + 15 }
    println(respuestaMapDos)

    //filtrar el arreglo
    //1)devolver una expresion true o false
    //2) nuevo arreglo filtrado
    val respuestaFilter: List<Int> = arregloDinamico
        .filter { valorActual: Int ->
            //expresion o condicion
            val mayorACinco: Boolean=valorActual > 5
            return@filter mayorACinco
        }

    val repuestaFilterDos = arregloDinamico.filter { it <= 5 }
    println(respuestaFilter)
    println(repuestaFilterDos)

    //or and
    //or -> any cualquiera cumple?
    //and -> all todos cumplen?
    val respuestaAny: Boolean = arregloDinamico
        .any{ valorActual: Int ->
            return@any (valorActual > 5)
        }
    println(respuestaAny)//true

    val respuestaAll: Boolean = arregloDinamico
        .all { valorActual:Int->
            return@all (valorActual > 5)
        }
    println(respuestaAll)//false
}

//funciones
fun imprimirNombre (nombre:String): Unit{ //unit es opcional es como el semejante del void
    fun otraFuncionAdentro(){
        println("otra funcion adentro")
    }

    println("Nombre $nombre")//uso sin llaves
    println("Nombre ${nombre}")//uso con llaves opcional
    println("Nombre ${nombre + nombre}") //concatenacion
    println("Nombre ${nombre.uppercase()}") //usar funcion
    println("Nombre $nombre.uppercase()") //incorrecto no se puede usar sin llaves

    otraFuncionAdentro()
}

fun calcularSueldo (
    sueldo:Double,//requerido
    tasa:Double = 12.00,//opcional
    bonoEspecial:Double? = null //opcional(nullable)
    //varialble? -> "?" es Nullable osea que puede en algun moento
):Double {
    //Int ->Int? Nullable
    //String ->String? Nullable
    //Date -> Date? Nullable
    if (bonoEspecial == null) {
        return sueldo * (100 / tasa)
    } else {
        return sueldo * (100 / tasa) * bonoEspecial
    }
}

abstract class NumerosJava{
    protected val numeroUno: Int
    private val numeroDos: Int

    constructor(
        uno:Int,
        dos:Int
    ){
        this.numeroUno = uno
        this.numeroDos = dos
        println("Inicializando")
    }
}

    abstract class Numeros(
        protected val numeroUno: Int,
        protected val numeroDos: Int,
        parametroNoUsadoPropiedadDeLaClase:Int? = null
    ){
        init {
            this.numeroUno
            this.numeroDos
            println("Inicializado")
        }
    }

    class Suma(
        unoParametro: Int,
        dosParametro: Int
    ):Numeros(
        unoParametro,
        dosParametro
    ){
        public val soyPublicoExplicito: String = "Publicas"
                val soyPublicoImplicite: String = "Publico Implicito"
        init {
            this.numeroUno
            this.numeroDos
            numeroUno
            numeroDos
            this.soyPublicoImplicite
            soyPublicoExplicito
        }

        constructor(
            uno: Int?,
            dos: Int,
        ):this(
            if(uno== null) 0 else uno,
            dos
        ){

        }

        constructor(
            uno: Int,
            dos: Int?,
        ):this(
            uno,
            if(dos== null) 0 else dos
        )

        constructor(
            uno: Int?,
            dos: Int?,
        ):this(
            if(uno== null) 0 else uno,
            if(dos== null) 0 else dos
        )

        fun sumar(): Int{
            val total = numeroUno + numeroDos
            agregarHistorial(total)
            return total
        }

        companion object{
            val pi= 3.14
            fun elevarAlCuadrado(num:Int):Int {return  num * num}
            val historialSumas = arrayListOf<Int>()
            fun agregarHistorial(valorTotalSuma:Int){
                historialSumas.add(valorTotalSuma)
            }
        }
}

