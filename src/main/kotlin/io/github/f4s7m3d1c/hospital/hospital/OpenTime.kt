package io.github.f4s7m3d1c.hospital.hospital

data class OpenTime(
	val mon: String,
	val tue: String,
	val wen: String,
	val thu: String,
	val fri: String,
	val sat: String,
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