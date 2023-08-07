package io.github.f4s7m3d1c.hospital.hospital

data class Hospital(
	val name: String, // 병원 이름
	val latitude: Double, // 위도
	val longitude: Double, // 경도
	val hasER: Boolean, // 응급실 보유 여부
	val openTime: Map<String, String> // 운영 시간
)