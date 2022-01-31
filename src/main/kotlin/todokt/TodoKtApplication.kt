package todokt

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TodoKtApplication

fun main(args: Array<String>) {
    runApplication<TodoKtApplication>(*args)
}
