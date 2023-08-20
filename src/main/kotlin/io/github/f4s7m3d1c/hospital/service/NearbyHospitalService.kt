package io.github.f4s7m3d1c.hospital.service

import io.github.f4s7m3d1c.hospital.database.HospitalDB
import io.github.f4s7m3d1c.hospital.hospital.Hospital
import io.github.f4s7m3d1c.hospital.math.Vector2
import org.springframework.stereotype.Service

@Service
class NearbyHospitalService {

	fun getNearbyHospital(pos: Vector2): MutableList<Hospital> {
		val hospitals: MutableList<Hospital> = HospitalDB.INSTANCE.getHospitals(pos.calculateBoundingBox(10000.0))
		val nearbyHospitals: MutableList<Hospital> = mutableListOf()
		hospitals.forEach {
			if (it.position.distance(pos) <= 10000) {
				nearbyHospitals.add(it)
			}
		}
		return nearbyHospitals
	}
}