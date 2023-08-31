package io.github.f4s7m3d1c.hospital.version

enum class VersionStatus(val code: UByte) {
	ERROR(1u), // 오류가 있는 버전
	DELETE(44u), // 삭제된 버전
	LOADING(100u), // 새로운 데이터를 불러오는 중
	STABLE(200u); // 최신 버전

	companion object {
		fun forCode(code: UByte): VersionStatus? {
			return values().firstOrNull{ it.code == code }
		}
	}
}