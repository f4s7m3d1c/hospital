package io.github.f4s7m3d1c.hospital.hospital

data class Hospital(
	val name: String, // 병원 이름
	val latitude: Double, // 위도
	val longitude: Double, // 경도
	val hasER: Boolean, // 응급실 보유 여부
	val openTime: Map<String, String> // 운영 시간
)

fun createOpenTimeMap(
	timeMonS: String?, timeMonC: String?, // 월요일
	timeTueS: String?, timeTueC: String?, // 화요일
	timeWenS: String?, timeWenC: String?, // 수요일
	timeThuS: String?, timeThuC: String?, // 목요일
	timeFriS: String?, timeFriC: String?, // 금요일
	timeSatS: String?, timeSatC: String?, // 토요일
	timeSunS: String?, timeSunC: String?  // 일요일
): MutableMap<String, String> = mutableMapOf(
	"월요일" to timeFormat(timeMonS, timeMonC),
	"화요일" to timeFormat(timeTueS, timeTueC),
	"수요일" to timeFormat(timeWenS, timeWenC),
	"목요일" to timeFormat(timeThuS, timeThuC),
	"금요일" to timeFormat(timeFriS, timeFriC),
	"토요일" to timeFormat(timeSatS, timeSatC),
	"일요일" to timeFormat(timeSunS, timeSunC)
)

private fun timeFormat(start: String?, close: String?): String {
	if(start === null || close === null) {
		return "X"
	}
	return "${start.take(2)}:${start.subSequence(2, 4)} ~ ${close.take(2)}:${close.subSequence(2, 4)}"
}