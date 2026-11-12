package backend.etec

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class EtecApplication

fun main(args: Array<String>) {
	runApplication<EtecApplication>(*args)
}
