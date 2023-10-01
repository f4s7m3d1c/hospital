package io.github.f4s7m3d1c.hospital.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import io.github.f4s7m3d1c.hospital.math.Vector2
import jakarta.persistence.*
import okio.internal.commonToUtf8String
import java.security.MessageDigest

@Entity
@Table(name = "hospital_info_db")
data class Hospital(

	@Column(length = 50)
	val name: String,

	@Column(columnDefinition = "INT UNSIGNED")
	@get:JsonIgnore
	val version: Long,

	@Column(columnDefinition = "DECIMAL(20, 16)")
	val latitude: Double,

	@Column(columnDefinition = "DECIMAL(20, 16)")
	val longitude: Double,

	@Column(nullable = false)
	val hasER: Boolean,

	@Embedded
	val openTime: OpenTime
) {
	@Id
	@Column(length = 64)
	@get:JsonIgnore
	val id: String = MessageDigest.getInstance("sha-256").digest("$name-$latitude-$longitude-$version".toByteArray()).commonToUtf8String()

	@get:JsonIgnore
	val position: Vector2 get() = Vector2(latitude, longitude)
}