package io.github.f4s7m3d1c.hospital.threads

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value

private object HospitalAPI {
	@Value("\${hospital.api.key}")
	private lateinit var key: String

	fun createUrl(page: Int) : String {
		var url = "https://safemap.go.kr/openApiService/data/getTotHospitalData.do"
		url += "?serviceKey=$key"
		url += "&numOfRows=1000"
		url += "&pageNo=$page"
		return url
	}
}

class HospitalUpdater: Thread() {

	private val logger: Logger = LoggerFactory.getLogger("HospitalUpdate")

	override fun run() {

	}

	fun updateData() {
		var page = 1
		val client = OkHttpClient()

		while (true) {
			val request: Request = Request.Builder()
				.url(HospitalAPI.createUrl(page))
				.build()

			val response: Response = client.newCall(request).execute()
			if(response.isSuccessful) {

			}else {
				logger.warn("Update failed!!!\ncode: ${response.code}\nmessage: ${response.message}")
				break
			}
			page++
		}
	}
}