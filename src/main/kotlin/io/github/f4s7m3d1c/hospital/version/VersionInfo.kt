package io.github.f4s7m3d1c.hospital.version

import java.sql.Date

data class VersionInfo(
	val version: UInt,
	val date: Date,
	val status: VersionStatus
)
