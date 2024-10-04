package org.example
import java.nio.file.Path

/**
 * https://github.com/Lainezz/ADAT01-03
 */

fun main() {
    val path = Path.of("src/main/resources")
    val reader = Reader(path)
    val writer = Writer(path)

    // 1. Lectura de empleados desde archivo de texto:
    val listadoEmpleados = reader.getListadoEmpleados()


    // 2. Generar un archivo XML
    writer.createXML(listadoEmpleados)


    // 3. Modificación de un nodo en el archivo XML:
    // Leemos el XML y devuelve una lista de empleados
    val pathXml = Path.of("src/main/resources/empleados2.xml")
    val empleados = reader.readXml(pathXml)
    // Obtenemos una nueva lista de empleados, con las modificaciones hechas
    val listaModificada = writer.editXML(1, 50.0, empleados)
    // Creamos un nuevo XML con la lista modificada
    writer.createXML(listaModificada)


    // 4.Lectura del archivo XML modificado y salida en consola:
    reader.imprimirXml(listaModificada)



}