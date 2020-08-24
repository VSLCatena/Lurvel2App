package nl.vslcatena.lurvel2app

import nl.vslcatena.lurvel2app.connections.FirebaseConnection
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import kotlin.concurrent.fixedRateTimer

@SpringBootApplication
class Lurvel2AppMain

fun main(args: Array<String>) {
    SpringApplication.run(Lurvel2AppMain::class.java, *args)

    // We run it as daemon so it stops when our spring application stops
    fixedRateTimer("UpdateCommittees", true, 60 * 1000, 60 * 60 * 1000) {
        FirebaseConnection.updateCommittees()
    }
}