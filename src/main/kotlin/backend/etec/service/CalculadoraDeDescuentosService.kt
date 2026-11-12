package backend.etec.service

import backend.etec.exceptions.PrecondicionInvalidaException
import org.springframework.stereotype.Service

@Service
class CalculadoraDeDescuentosService{

    fun aplicarDescuentosSegunUsuario(userID: String, monto: Double): Double {
        // La implementación real de la regla de negocio se probará en el test de integración.

        // 1. Verificación de regla de negocio
              if (monto <= 0) {
                  // 2. Si no cumple, "lanzamos" la alarma e interrumpimos la función
                  throw PrecondicionInvalidaException("El monto debe ser mayor a cero")
              }
              return monto - ((monto/100)*25)
    }
}
