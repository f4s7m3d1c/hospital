package io.github.f4s7m3d1c.hospital.hospital

import io.github.f4s7m3d1c.hospital.math.Vector2

data class Hospital(
	val name: String, // 병원 이름
	val latitude: Double, // 위도
	val longitude: Double, // 경도
	val hasER: Boolean, // 응급실 보유 여부
	val openTime: OpenTime // 운영 시간
) {
	val position: Vector2 get() = Vector2(latitude, longitude)
}