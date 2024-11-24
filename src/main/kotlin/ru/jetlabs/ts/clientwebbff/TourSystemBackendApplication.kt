package ru.jetlabs.ts.clientwebbff

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TourSystemBackendApplication

fun main(args: Array<String>) {
	runApplication<TourSystemBackendApplication>(*args)

	//val k = Example() // FROM COMMON MODULE
}
