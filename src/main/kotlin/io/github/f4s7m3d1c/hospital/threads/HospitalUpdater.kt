package io.github.f4s7m3d1c.hospital.threads

import org.springframework.beans.factory.annotation.Value

private object HospitalAPI {
	@Value("\${hospital.api.key}")
	private lateinit var key: String

	fun createUrl(page: Int = 1) : String {
		var url = "https://safemap.go.kr/openApiService/data/getTotHospitalData.do"
		url += "?serviceKey=$key"
		url += "&numOfRows=1000"
		url += "&pageNo=$page"
		return url
	}
}

class HospitalUpdater: Thread() {

	override fun run() {

	}
}