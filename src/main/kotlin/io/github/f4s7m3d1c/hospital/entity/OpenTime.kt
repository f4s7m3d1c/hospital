package io.github.f4s7m3d1c.hospital.entity

import jakarta.persistence.Column
import jakarta.persistence.Embeddable

@Embeddable
data class OpenTime(
	@Column(
		nullable = false,
		name = "timeMon",
		length = 20
	)
	val mon: String,

	@Column(
		nullable = false,
		name = "timeTue",
		length = 20
	)
	val tue: String,

	@Column(
		nullable = false,
		name = "timeWen",
		length = 20
	)
	val wen: String,

	@Column(
		nullable = false,
		name = "timeThu",
		length = 20
	)
	val thu: String,

	@Column(
		nullable = false,
		name = "timeFri",
		length = 20
	)
	val fri: String,

	@Column(
		nullable = false,
		name = "timeSat",
		length = 20
	)
	val sat: String,

	@Column(
		nullable = false,
		name = "timeSun",
		length = 20
	)
	val sun: String
) {
	companion object {
		fun createOpenTimeMap(
			timeMonS: String?, timeMonC: String?, // 월요일
			timeTueS: String?, timeTueC: String?, // 화요일
			timeWenS: String?, timeWenC: String?, // 수요일
			timeThuS: String?, timeThuC: String?, // 목요일
			timeFriS: String?, timeFriC: String?, // 금요일
			timeSatS: String?, timeSatC: String?, // 토요일
			timeSunS: String?, timeSunC: String?  // 일요일
		): OpenTime = OpenTime(
			timeFormat(timeMonS, timeMonC),
			timeFormat(timeTueS, timeTueC),
			timeFormat(timeWenS, timeWenC),
			timeFormat(timeThuS, timeThuC),
			timeFormat(timeFriS, timeFriC),
			timeFormat(timeSatS, timeSatC),
			timeFormat(timeSunS, timeSunC)
		)

		private fun timeFormat(start: String?, close: String?): String {
			if(start === null || close === null) {
				return "휴진"
			}
			return "${start.take(2)}:${start.subSequence(2, 4)} ~ ${close.take(2)}:${close.subSequence(2, 4)}"
		}
	}
}