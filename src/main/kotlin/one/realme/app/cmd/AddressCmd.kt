package one.realme.app.cmd

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.int

class AddressCmd : CliktCommand(name = "address") {
    private val create: Int by option(help = "Number of greetings").int().default(1)

    override fun run() {
        println("hello")
    }

}