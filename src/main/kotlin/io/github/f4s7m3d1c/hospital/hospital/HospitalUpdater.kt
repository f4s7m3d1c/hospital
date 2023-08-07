package io.github.f4s7m3d1c.hospital.hospital

import com.google.gson.Gson
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object HospitalUpdater {

	private val logger: Logger = LoggerFactory.getLogger("HospitalUpdater")

	@OptIn(DelicateCoroutinesApi::class)
	fun update() {
		logger.info("Hospital data update started.")
		GlobalScope.launch(Dispatchers.IO) {
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

			logger.info("Hospital data update completed")
		}
	}
}