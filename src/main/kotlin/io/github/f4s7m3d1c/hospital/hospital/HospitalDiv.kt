package io.github.f4s7m3d1c.hospital.hospital

enum class HospitalDiv(val code: String) {

	HOSPITAL("B"),
	CLINIC("C");

	companion object {
		fun code2div(code: String) : HospitalDiv? {
			return when(code) {
				"B" -> HOSPITAL
				"C" -> CLINIC
				else -> null
			}
		}
	}
}