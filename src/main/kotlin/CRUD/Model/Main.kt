package CRUD.Model

import CRUD.Repository.XMLRepository
import org.example.Ej03.Empleado
import java.nio.file.Path

fun main(){
    val ficheroXML = Path.of("src/main/resources/empleadosCRUD.xml")

    val emple = Empleado("15", "Navas", "IT", 6000.0)

    val repository = XMLRepository()
    //repository.insert(emple, ficheroXML)
    repository.delete("15", ficheroXML)
}