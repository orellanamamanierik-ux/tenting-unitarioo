package backend.etec.service

import backend.etec.exceptions.PrecondicionInvalidaException

import backend.etec.service.CalculadoraDeDescuentosService
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class CalculadoraDeDescuentosServiceTest : BehaviorSpec({

    val calculadora = CalculadoraDeDescuentosService()

    Given("la calculadora de descuentos") {

        When("se intenta aplicar un descuento a un monto negativo (-50.0)") {

            Then("debe lanzar la excepción PrecondicionInvalidaException") {
                // Capturamos y verificamos la excepción con Kotest
                val excepcion = shouldThrow<PrecondicionInvalidaException> {
                    calculadora.aplicarDescuentosSegunUsuario(userID = "123", monto = -50.0)
                }

                // Opcional: También podemos validar el mensaje dentro de la excepción
                excepcion.message shouldBe "El monto debe ser mayor a cero"
            }
        }
    }
})
