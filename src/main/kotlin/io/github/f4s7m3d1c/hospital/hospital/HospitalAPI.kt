package io.github.f4s7m3d1c.hospital.hospital

import org.springframework.stereotype.Component

@Component
object HospitalAPI {
	const val SUCCESS_OK = "SERVICE SUCCESS"
	const val SUCCESS_NO_DATA = "SERVICE SUCCESS BUT NO DATA"

	private lateinit var key: String

	fun initialKey(key: String)  {
		this.key = key
	}

	fun createUrl(page: Int) : String {
		var url = "https://safemap.go.kr/openApiService/data/getTotHospitalData.do"
		url += "?serviceKey=$key"
		url += "&dataType=json"
		url += "&numOfRows=1000"
		url += "&pageNo=$page"
		return url
	}
}