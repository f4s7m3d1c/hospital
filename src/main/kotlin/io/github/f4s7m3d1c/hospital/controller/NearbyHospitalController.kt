package io.github.f4s7m3d1c.hospital.controller

import io.github.f4s7m3d1c.hospital.math.Vector2
import io.github.f4s7m3d1c.hospital.service.NearbyHospitalService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api")
class NearbyHospitalController(
	private val service: NearbyHospitalService
) {

	@GetMapping("/hospital")
	fun getNearbyHospital(
		@RequestParam lat: Double,
		@RequestParam lon: Double
	): MutableMap<String, Any> {
		return service.getNearbyHospital(Vector2(lat, lon))
	}
}