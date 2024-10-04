package org.example

import org.w3c.dom.Document
import java.nio.file.Path
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.OutputKeys
import javax.xml.transform.Source
import javax.xml.transform.Transformer
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult

class Writer(private val path: Path) {

    /**
     * Crea un fichero XML a partir de una lista de Empleados
     * @param listaEmpleados: Lista mutable de empleados
     */
    fun createXML(listaEmpleados: MutableList<Empleado>){
        // 1º Instanciamos la clase DocumentBuilderFactory
        val factory = DocumentBuilderFactory.newInstance()

        // Instanciamos la clase DocumentBuilder
        val builder = factory.newDocumentBuilder()

        // Opcional: DOMImplementation contiene los metodos para crear un Document
        val imp = builder.domImplementation

        //2º Procedemos a crear un Document vacio (nameSpaceURI, qualifiedName, doctype)
        // Seria el nodo root
        val document: Document = imp.createDocument(null, "empleados", null)

        listaEmpleados.forEach { emple ->
            // Crear el elemento empleado, con su atributo id y asignarle el id
            val empleado = document.createElement("empleado")
            document.documentElement.appendChild(empleado)
            empleado.setAttribute("id", emple.id.toString())

            // Elementos nombre dep y salario
            val apellido = document.createElement("apellido")
            val dep = document.createElement("dep")
            val salario = document.createElement("salario")

            empleado.appendChild(apellido)
            empleado.appendChild(dep)
            empleado.appendChild(salario)

            // Contenido de los elementos
            val valorApellido = document.createTextNode(emple.apellido)
            val valorDep = document.createTextNode(emple.departamento)
            val valorSalario = document.createTextNode(emple.salario.toString())

            apellido.appendChild(valorApellido)
            dep.appendChild(valorDep)
            salario.appendChild(valorSalario)
        }

        // Procedemos a crear el XML
        val source: Source = DOMSource(document)

        // Clase que usaremos para escribir --> StreamResult()
        val result: StreamResult = StreamResult(path.resolve("empleados2.xml").toFile())

        // Herramienta que usamos para realizar la transformacion: Transformer
        val transformer: Transformer = TransformerFactory.newInstance().newTransformer()

        // BONUS POINT
        // Para indentar el XML correctamente
        transformer.setOutputProperty(OutputKeys.INDENT, "yes")

        // Y por ultimo transformamos
        transformer.transform(source, result)

    }


    /**
     * Edita el salario de un empleado con una ID en concreto, si no existe la ID
     * @param id: Id del empleado a editar
     * @param newSalary: Valor del nuevo salario deseado
     * @param fileXML: Listado de los empleados
     * @return fileXML: Una lista mutable de empleados, tras ser modificada en caso de existir dicha ID
     */
    fun editXML(id: Int, newSalary: Double, fileXML: MutableList<Empleado>): MutableList<Empleado> {
        // Find para obtener el empleado con esa ID
        val empleado = fileXML.find { it.id == id }
        if (empleado != null){
            val index = fileXML.indexOf(empleado)
            fileXML[index].salario = newSalary
        }else{
            println("No se encontró empleado con ID $id")
        }

        return fileXML
    }
}