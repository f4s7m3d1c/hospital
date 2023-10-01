package io.github.f4s7m3d1c.hospital.repository

import io.github.f4s7m3d1c.hospital.entity.Version
import io.github.f4s7m3d1c.hospital.version.VersionStatus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
interface VersionRepo :JpaRepository<Version, Long> {

	fun findFirstByOrderByVersionDesc(): Version?

	fun findFirstByStatusOrderByVersionDesc(status: VersionStatus): Version?
}