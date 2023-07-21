package io.github.f4s7m3d1c.hospital

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class HospitalApplication

fun main(args: Array<String>) {
	runApplication<HospitalApplication>(*args)
}
