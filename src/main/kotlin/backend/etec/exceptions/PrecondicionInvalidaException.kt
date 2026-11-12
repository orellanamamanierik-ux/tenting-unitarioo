package backend.etec.exceptions

// Herda de RuntimeException para que Spring y Kotlin la reconozcan como una "alarma" de error
class PrecondicionInvalidaException(mensaje: String) : RuntimeException(mensaje)
