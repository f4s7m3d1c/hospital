package io.github.f4s7m3d1c.hospital.threads

import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value

private object HospitalAPI {
	const val SUCCESS_OK = "SERVICE SUCCESS"
	const val SUCCESS_NO_DATA = "SERVICE SUCCESS BUT NO DATA"

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
			if(!response.isSuccessful || response.body === null) {
				logger.warn("Update failed!!!\ncode: ${response.code}\nmessage: ${response.message}")
				break
			}
			try {
				@Suppress("UNCHECKED_CAST")
				val body: MutableMap<String, MutableMap<String, MutableMap<String, Any>>> = Gson()
					.fromJson(response.body!!.string(), MutableMap::class.java)
						as MutableMap<String, MutableMap<String, MutableMap<String, Any>>>
				if(body["response"]!!["header"]!!["resultMsg"] == HospitalAPI.SUCCESS_NO_DATA) {
					logger.info("Update Success!!")
					break
				}
				if(body["response"]!!["header"]!!["resultMsg"] != HospitalAPI.SUCCESS_OK) {
					logger.warn("Hospital API service is not success")
					break
				}
				@Suppress("UNCHECKED_CAST")
				val items: MutableList<MutableMap<String, String?>> = body["response"]!!["body"]!!["items"]
					as MutableList<MutableMap<String, String?>>
				for (item: MutableMap<String, String?> in items) {
					//TODO: Hospital DB에 저장
				}
			} catch (e: Exception) {
				logger.warn("Update failed!!!", e)
				break
			}
			page++
		}
	}
}