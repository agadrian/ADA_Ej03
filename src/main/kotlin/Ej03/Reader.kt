package org.example.Ej03

import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import java.io.BufferedReader
import java.nio.file.Files
import java.nio.file.Path
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory

class Reader(private val path: Path) {

    private val listaEmpleados = mutableListOf<Empleado>()

    // Al inicializar la clase, es lo segundo que se inicializa, despues de la variable listaEmpleados
    init {
        readCsv()
    }


    /**
     * Lee un fichero csv y añade a la lista de Empleados, los empleados leidos
     */
    private fun readCsv(){
        // Ruta completa hasta el archivo csv
        val fichero = path.resolve("empleados.csv")

        if (Files.notExists(fichero)){
            // ** CREARE SI NO EXISTE EL CSV PARA RELLENARLO YO A MANO**

            // Crea los directorios en caso de que no existan hasta esta ruta
            Files.createDirectories(path)
            // Crea el csv vacio
            Files.createFile(fichero)
        }

        val br: BufferedReader = Files.newBufferedReader(fichero)

        br.use { flujo ->
            // Para leer la primera linea e ignorarla:
            flujo.readLine()


            flujo.forEachLine { linea ->
                val datosLinea = linea.split(",")

                try {
                    val id = datosLinea[0].toInt()
                    val apellido = datosLinea[1]
                    val departamento = datosLinea[2]
                    val salario = datosLinea[3].toDouble()

                    listaEmpleados.add(Empleado(id, apellido, departamento, salario))

                }catch (e: NumberFormatException){
                    println("* ERROR *: Linea: $linea. No se pudo convertir a numero: ${e.message}")
                }catch (e: Exception) {
                    println("* ERROR *: Linea: $linea. Detalles: ${e.message}")
                }
            }
        }
    }

    /**
     * Retorna la lista de empleados
     */
    fun getListadoEmpleados(): MutableList<Empleado> {return listaEmpleados}


    /**
     * Lee un fichero XML, se le pasa el path de este.
     * @param fileXML: Path del fichero XML
     * @return Una lista mutable de tipo Empleado
     */
    fun readXml(fileXML: Path): MutableList<Empleado> {
        // 1º Instanciar un objeto DocoumentBuilderFactory
        val dbf: DocumentBuilderFactory = DocumentBuilderFactory.newInstance()

        // 2º Con el dbf instanciado, creamos un objeto de tipo DocumentBuilder
        val db: DocumentBuilder = dbf.newDocumentBuilder()

        // 3º Con el db, ya podemos parsear nuestro documento en texto plano a DOM
        val document = db.parse(fileXML.toFile())

        // Root principal
        val root: Element = document.documentElement
        root.normalize()

        val listaNodos: NodeList = root.getElementsByTagName("empleado")

        val listaEmpleados = mutableListOf<Empleado>()

        for (i in 0..<listaNodos.length){
            // Accedemos a traves del indice
            val nodo: Node = listaNodos.item(i)

            // Para acceder al tipo del nodo, usamos .nodeType()
            if (nodo.nodeType == Node.ELEMENT_NODE) {
                // Casteamos a Element
                val nodoElemento = nodo as Element


                val atributoId = nodoElemento.getAttribute("id")
                val elementoApellido = nodoElemento.getElementsByTagName("apellido")
                val elementoDep = nodoElemento.getElementsByTagName("dep")
                val elementoSalario = nodoElemento.getElementsByTagName("salario")

                // Obviaremos que puedan estar vacios o ser nulos
                val id = atributoId.toIntOrNull() ?: -1
                val elementoApellidoContent = elementoApellido.item(0).textContent.trim()
                val elementoDepContent = elementoDep.item(0).textContent
                val elementoSalarioContent = elementoSalario.item(0).textContent.toDouble()

                // Añadimos a la lista de empleados
                listaEmpleados.add(
                    Empleado(
                        id,
                        elementoApellidoContent,
                        elementoDepContent,
                        elementoSalarioContent
                    )
                )
            }
        }

        return listaEmpleados
    }


    /**
     * Muestra por cosola la informacion de una lista de tipo Empleado
     */
    fun imprimirXml(listadoXML: MutableList<Empleado>){
        listadoXML.forEach { empleado ->
            println("ID: ${empleado.id}, Apellido: ${empleado.apellido}, Departamento: ${empleado.departamento}, Salario: ${empleado.salario}")
        }
    }
}