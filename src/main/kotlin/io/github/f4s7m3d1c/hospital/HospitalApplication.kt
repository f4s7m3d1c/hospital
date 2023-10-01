package io.github.f4s7m3d1c.hospital

import io.github.f4s7m3d1c.hospital.hospital.HospitalAPI
import io.github.f4s7m3d1c.hospital.service.HospitalUpdater
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication


@SpringBootApplication
class HospitalApplication {

	@Value("\${hospital.api.key}")
	private lateinit var key: String

	@Autowired
	private lateinit var updater: HospitalUpdater

	@PostConstruct
	fun onEnable() {
		HospitalAPI.initialKey(key)
		updater.checkUpdate()
	}
}

fun main(args: Array<String>) {
	runApplication<HospitalApplication>(*args)
}
