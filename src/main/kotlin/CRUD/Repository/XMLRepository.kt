package CRUD.Repository

import CRUD.Model.Empleado
import org.example.Ej03.Reader
import org.example.Ej03.Writer
import org.w3c.dom.Document
import org.w3c.dom.Element
import java.nio.file.Path
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.OutputKeys
import javax.xml.transform.Source
import javax.xml.transform.Transformer
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult
import kotlin.io.path.notExists

class XMLRepository {

    /* Aqui es donde vamos a implementar las funciones CRUD*/

    // C --> Create / Insertar un nuevo empleado

    fun insert(emple: Empleado, rutaXML: Path){

        if (rutaXML.notExists()){
            throw Exception("Ruta XML no existe")
        }

        if (emple.id.isBlank()
            || emple.apellido.isBlank()
            || emple.dept.isBlank()
            ){
            throw Exception("Atributos incorrectos")
        }

        // Parsear el documento
        val document = parseXML(rutaXML)
        // Root principal
        val root: Element = document.documentElement
        root.normalize()

        // Insertar el Empleado
        val elementoNuevoEmple = document.createElement("empleado")
        elementoNuevoEmple.setAttribute("id", emple.id)

        root.appendChild(elementoNuevoEmple)

        val elementoApellido = document.createElement("apellido")
        val elementoDept = document.createElement("dept")
        val elementoSalario = document.createElement("salario")

        val textNodeApellido = document.createTextNode(emple.apellido)
        val textNodeDept = document.createTextNode(emple.dept)
        val textNodeSalario = document.createTextNode(emple.salario.toString())

        elementoApellido.appendChild(textNodeApellido)
        elementoDept.appendChild(textNodeDept)
        elementoSalario.appendChild(textNodeSalario)

        elementoNuevoEmple.appendChild(elementoApellido)
        elementoNuevoEmple.appendChild(elementoDept)
        elementoNuevoEmple.appendChild(elementoSalario)


        // Procedemos a crear el XML
        val source: Source = DOMSource(document)

        // Clase que usaremos para escribir --> StreamResult()
        val result: StreamResult = StreamResult(rutaXML.toFile())

        // Herramienta que usamos para realizar la transformacion: Transformer
        val transformer: Transformer = TransformerFactory.newInstance().newTransformer()


        // BONUS POINT
        // Para indentar el XML correctamente
        transformer.setOutputProperty(OutputKeys.INDENT, "yes")
        transformer.setOutputProperty(OutputKeys.METHOD, "xml")
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no")

        // Y por ultimo transformamos
        transformer.transform(source, result)

    }

    private fun parseXML(rutaXML: Path): Document {
        val dbf = DocumentBuilderFactory.newInstance()
        val db = dbf.newDocumentBuilder()
        return db.parse(rutaXML.toFile())
    }


    fun delete(id: String, rutaXML: Path){
        if (rutaXML.notExists()){
            throw Exception("Ruta XML no existe")
        }

        val listaEmpleados = Reader(rutaXML).readXml(rutaXML)
        val empleadoID = listaEmpleados.find { it.id == id.toInt() }
        if (empleadoID != null){
            val pos = listaEmpleados.indexOf(empleadoID)
            listaEmpleados.removeAt(pos)

            //Crear el XML con el elemento borrado
            Writer(rutaXML).createXML(listaEmpleados, "src/main/resources/empleadosCRUD.xml")
        }




    }

}