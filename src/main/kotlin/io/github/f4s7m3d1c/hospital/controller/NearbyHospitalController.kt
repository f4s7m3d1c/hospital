package io.github.f4s7m3d1c.hospital.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api")
class NearbyHospitalController {

	@GetMapping("/hospital")
	fun getNearbyHospital(
		@RequestParam latitude: Double,
		@RequestParam longitude: Double
	): String {
		return "${latitude}:${longitude}"
	}
}