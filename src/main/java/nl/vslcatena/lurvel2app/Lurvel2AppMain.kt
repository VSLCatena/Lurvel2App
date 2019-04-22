package nl.vslcatena.lurvel2app

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class Lurvel2AppMain

fun main(args: Array<String>) {
    SpringApplication.run(Lurvel2AppMain::class.java, *args)
}