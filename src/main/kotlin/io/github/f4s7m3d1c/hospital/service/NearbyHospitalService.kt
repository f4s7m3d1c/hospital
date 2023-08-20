package io.github.f4s7m3d1c.hospital.service

import io.github.f4s7m3d1c.hospital.database.HospitalDB
import io.github.f4s7m3d1c.hospital.hospital.Hospital
import io.github.f4s7m3d1c.hospital.math.Vector2
import org.springframework.stereotype.Service

@Service
class NearbyHospitalService {

	fun getNearbyHospital(pos: Vector2): MutableMap<String, Any> {
		val hospitals: MutableList<Hospital> = HospitalDB.INSTANCE.getHospitals(pos.calculateBoundingBox(1.5))
		val nearbyHospitals: MutableList<Hospital> = mutableListOf()
		hospitals.forEach {
			if (it.position.distance(pos) <= 1.3) {
				nearbyHospitals.add(it)
			}
		}
		return mutableMapOf(
			"count" to nearbyHospitals.size,
			"hospitals" to nearbyHospitals
		)
	}
}