package backend.etec.controller

//agregado
import backend.etec.exceptions.PrecondicionInvalidaException


import com.ninjasquad.springmockk.MockkBean
import backend.etec.service.CalculadoraDeDescuentosService
import io.kotest.core.spec.style.BehaviorSpec
//agregado
import io.kotest.core.extensions.ApplyExtension
import io.kotest.extensions.spring.SpringExtension
//
import io.mockk.every
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

// 1. @WebMvcTest SOLO levanta la capa web (rutas, JSON, HTTP).
// No inicia el contexto completo de Spring Boot ni levanta repositorios ni servicios reales.
// (En un test de integración usaríamos @SpringBootTest + @AutoConfigureMockMvc).
@ApplyExtension(SpringExtension::class) // 👈 Habilita la inyección por constructor en Kotest
@WebMvcTest(CalculadoraDeDescuentosController::class)
class CalculadoraDeDescuentosControllerTest(
    private val navegador: MockMvc,

    // 2. Inyectamos un MOCK del servicio en el contexto reducido de Spring.
    // El código real dentro de CalculadoraDeDescuentos NUNCA se ejecutará en esta prueba.
    // (En integración eliminaríamos esta anotación para usar el bean real registrado por Spring).
    @MockkBean private val calculadora: CalculadoraDeDescuentosService,
) : BehaviorSpec({

    Given("un endpoint para calcular descuentos /api/descuentos") {
        And("un usuario 123 quiere aplicar descuentos a un monto menor a 0") {

                 // Configuramos el Mock para que simule lanzar la excepción de negocio
                 every {
                     calculadora.aplicarDescuentosSegunUsuario("123", -10.0)
                 } throws PrecondicionInvalidaException("El monto debe ser mayor a cero")

                 When("se hace la petición con el monto inválido") {
                     val respuesta = navegador.perform(
                         get("/api/descuentos")
                             .param("userID", "123")
                             .param("monto", "-10.0")
                     )

                     Then("el controlador debe traducir la excepción a un estado HTTP 400 Bad Request") {
                         respuesta.andExpect(status().isBadRequest)
                     }

                     And("debe devolver un formato JSON estructurado con el mensaje de error") {
                         respuesta.andExpect(jsonPath("$.error").value("El monto debe ser mayor a cero"))
                     }
                 }
             }
        And("un usuario 123 al cual se le aplica un descuento del 25% sin importar el monto") {

            // 3. Simulamos/cableamos la respuesta manualmente con 'every'.
            // Este test evaluará si el Controller mapea bien el resultado del servicio, NO si la
            // lógica matemática del servicio es correcta.
            // (En integración NO se usa 'every': el servicio real procesará la regla o consultará la BD).
            every {
                calculadora.aplicarDescuentosSegunUsuario("123", 100.0)
            } returns 75.0

            When("se hace una consulta GET /api/descuentos?userID=123&monto=100.0") {

                // 4. Simulamos la llamada HTTP a través de MockMvc.
                //    Valida la extracción de parámetros de la URL ("userID" y "monto").
                val respuesta =
                    navegador.perform(
                        get("/api/descuentos")
                            .param("userID", "123")
                            .param("monto", "100.0"),
                    )

                Then("deberia obtener respuesta con estado ok") {
                    // Valida que el controlador devuelva un código HTTP 200 OK
                    respuesta.andExpect(status().isOk)
                }

                And("un campo para el final de la compra que deberia ser de 75.0") {
                    // Valida que el objeto devuelto por el controller se serialice correctamente a JSON
                    respuesta.andExpect(jsonPath("$.montoFinal").value(75.0))
                }
            }
        }
    }
})
