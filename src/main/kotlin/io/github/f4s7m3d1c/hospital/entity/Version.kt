package io.github.f4s7m3d1c.hospital.entity

import io.github.f4s7m3d1c.hospital.version.VersionStatus
import jakarta.persistence.*
import java.util.Date

@Entity
@Table(name = "version_log_db")
data class Version(
	@Id
	@Column(columnDefinition = "INT UNSIGNED")
	val version: Long,

	@Temporal(TemporalType.DATE)
	@Column(nullable = false)
	val date: Date,

	@Enumerated
	@Column(nullable = false)
	var status: VersionStatus = VersionStatus.LOADING
) {
	fun setStatus(status: VersionStatus) : Version {
		this.status = status
		return this
	}
}