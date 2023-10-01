package io.github.f4s7m3d1c.hospital.service

import io.github.f4s7m3d1c.hospital.entity.Hospital
import io.github.f4s7m3d1c.hospital.math.Vector2
import io.github.f4s7m3d1c.hospital.repository.HospitalRepo
import io.github.f4s7m3d1c.hospital.repository.VersionRepo
import io.github.f4s7m3d1c.hospital.version.VersionStatus
import org.springframework.stereotype.Service

@Service
class NearbyHospitalService(
	private val versionRepo: VersionRepo,
	private val hospitalRepo: HospitalRepo
) {

	fun getNearbyHospital(pos: Vector2, distance: Double): MutableMap<String, Any> {
		val box: Pair<Vector2, Vector2> = pos.calculateBoundingBox(distance * 2 + 1.5)
		val hospitals: List<Hospital> = hospitalRepo.findHospitalsInRange(
			versionRepo.findFirstByStatusOrderByVersionDesc(VersionStatus.STABLE)?.version ?: 0,
			box.first.lat,
			box.second.lat,
			box.first.lon,
			box.second.lon
		)
		val nearbyHospitals: MutableList<Hospital> = mutableListOf()
		hospitals.forEach {
			if (it.position.distance(pos) <= distance + 0.2) {
				nearbyHospitals.add(it)
			}
		}
		return mutableMapOf(
			"count" to nearbyHospitals.size,
			"distance" to distance,
			"hospitals" to nearbyHospitals
		)
	}
}