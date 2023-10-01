package io.github.f4s7m3d1c.hospital.repository

import io.github.f4s7m3d1c.hospital.entity.Hospital
import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface HospitalRepo: JpaRepository<Hospital, String> {

	@Query("SELECT h FROM Hospital h WHERE h.version = :version " +
			"AND h.latitude BETWEEN :minLatitude AND :maxLatitude " +
			"AND h.longitude BETWEEN :minLongitude AND :maxLongitude")
	fun findHospitalsInRange(
		version: Long,
		minLatitude: Double,
		maxLatitude: Double,
		minLongitude: Double,
		maxLongitude: Double
	) : List<Hospital>

	@Transactional
	fun deleteByVersionLessThan(version: Long)

	@Transactional
	fun deleteByVersion(version: Long)
}